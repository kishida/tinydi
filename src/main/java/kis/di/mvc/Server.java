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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        
        ServerSocket serverSoc = new ServerSocket(8989);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        for (;;) {
            Socket s = serverSoc.accept();
            executors.execute(() -> {
                try (InputStream is = s.getInputStream();
                     BufferedReader bur = new BufferedReader(new InputStreamReader(is))) 
                {
                    String first = bur.readLine();
                    Matcher mat = pattern.matcher(first);
                    mat.find();
                    String httpMethod = mat.group(1);
                    String path = mat.group(2);
                    String protocol = mat.group(3);
                    
                    for (String line; (line = bur.readLine()) != null && !line.isEmpty(););
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
                            pw.println();
                            pw.println(output);
                        } catch (Exception ex) {
                            pw.println("HTTP/1.0 200 OK");
                            pw.println("Content-Type: text/html");
                            pw.println();
                            pw.println("<h1>500 Internal Server Error</h1>");
                            pw.println(ex);
                        }
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });
        }
    }
}
