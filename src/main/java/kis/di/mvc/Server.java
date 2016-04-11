package kis.di.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import kis.di.Context;
import kis.di.annotation.Path;

/**
 *
 * @author naoki
 */
public class Server {
    static class ProcessorMethod {
        public ProcessorMethod(String name, Method method) {
            this.name = name;
            this.method = method;
        }
        String name;
        Method method;
    }
    
    static String trimSlash(String str) {
        return str.replaceFirst("^/", "").replaceFirst("/$", "");
    }
    
    public static void main(String[] args) throws IOException {
        Context.autoRegister();
        BeanSession beanSession = new BeanSession();
        Context.setBeanSession(beanSession);
        Map<String, ProcessorMethod> methods = new HashMap<>();
        Context.registeredClasses().forEach(entry -> {
            Class cls = entry.getValue();
            Path rootAno = (Path) cls.getAnnotation(Path.class);
            if (rootAno == null) {
                return; // continue
            }
            String root = trimSlash(rootAno.value());
            if (!root.isEmpty()) {
                root = "/" + root;
            }
            for (Method m : cls.getMethods()) {
                Path pathAno = m.getAnnotation(Path.class);
                if (pathAno == null) {
                    continue;
                }
                if (!m.getReturnType().equals(String.class)) {
                    continue;
                }
                if (m.getParameterCount() > 0) {
                    continue;
                }
                String path = root + "/" + pathAno.value();
                if (path.endsWith("/index")) {
                    path = path.replaceFirst("index$", "");
                }
                methods.put(path, new ProcessorMethod(entry.getKey(), m));
            }
        });
        
        Pattern pattern = Pattern.compile("([A-Z]+) ([^ ]+) (.+)");
        Pattern patternHeader = Pattern.compile("([A-Za-z-]+): (.+)");
        AtomicLong lastSessionId = new AtomicLong(10);
        ServerSocket serverSoc = new ServerSocket(8989);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        for (;;) {
            Socket s = serverSoc.accept();
            executors.execute(() -> {
                try (InputStream is = s.getInputStream();
                     BufferedReader bur = new BufferedReader(new InputStreamReader(is))) 
                {
                    String first = bur.readLine();
                    if (first == null) {
                        System.out.println("null request");
                        return;
                    }
                    Matcher mat = pattern.matcher(first);
                    mat.find();
                    String httpMethod = mat.group(1);
                    String path = mat.group(2);
                    String protocol = mat.group(3);
                    
                    RequestInfo info = (RequestInfo) Context.getBean("requestInfo");
                    info.setLocalAddress(s.getLocalAddress());
                    info.setPath(path);
                    Map<String, String> cookies = new HashMap<>();
                    for (String line; (line = bur.readLine()) != null && !line.isEmpty();) {
                        Matcher matHeader = patternHeader.matcher(line);
                        if (matHeader.find()) {
                            String value = matHeader.group(2);
                            switch (matHeader.group(1)) {
                                case "User-Agent":
                                    info.setUserAgent(value);
                                    break;
                                case "Cookie":
                                    Stream.of(value.split(";"))
                                          .map(exp -> exp.trim().split("="))
                                          .filter(kv -> kv.length == 2)
                                          .forEach(kv -> cookies.put(kv[0], kv[1]));
                            }
                        }
                    }
                    
                    
                    String sessionId = cookies.get("jsessionid");
                    if (sessionId != null) {
                        if (!beanSession.isSessionRegistered(sessionId)) {
                            sessionId = null;
                        }
                    }
                    if (sessionId == null) {
                        sessionId = Long.toString(lastSessionId.incrementAndGet());
                    }
                    beanSession.setSessionId(sessionId);
                    info.setSessionId(sessionId);
                    try (OutputStream os = s.getOutputStream();
                         PrintWriter pw = new PrintWriter(os))
                    {
                        ProcessorMethod method = methods.get(path.replaceFirst("/index$", "/"));
                        if (method == null) {
                            pw.println("HTTP/1.0 404 Not Found");
                            pw.println("Content-Type: text/html");
                            pw.println();
                            pw.println("<h1>404 Not Found</h1>");
                            pw.println(path + " Not Found");
                            return;
                        }
                        try{
                            Object bean = Context.getBean(method.name);
                            Object output = method.method.invoke(bean);
                            pw.println("HTTP/1.0 200 OK");
                            pw.println("Content-Type: text/html");
                            pw.println("Set-Cookie: jsessionid=" + sessionId + "; path=/");
                            pw.println();
                            pw.println(output);
                        } catch (Exception ex) {
                            pw.println("HTTP/1.0 500 Internal Server Error");
                            pw.println("Content-Type: text/html");
                            pw.println();
                            pw.println("<h1>500 Internal Server Error</h1>");
                            pw.println(ex);
                            ex.printStackTrace();
                        }
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });
        }
    }
}
