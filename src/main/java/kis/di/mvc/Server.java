package kis.di.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author naoki
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSoc = new ServerSocket(8989);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        for (;;) {
            Socket s = serverSoc.accept();
            executors.execute(() -> {
                try (InputStream is = s.getInputStream();
                     BufferedReader bur = new BufferedReader(new InputStreamReader(is))) 
                {
                    String firstLine = bur.readLine();
                    for (String line; (line = bur.readLine()) != null && !line.isEmpty(););
                    try (OutputStream os = s.getOutputStream();
                         PrintWriter pw = new PrintWriter(os))
                    {
                        pw.println("HTTP/1.0 200 OK");
                        pw.println("Content-Type: text/html");
                        pw.println();
                        pw.println("<h1>Hello!</h1>");
                        pw.println(LocalDateTime.now());
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });
        }
    }
}
