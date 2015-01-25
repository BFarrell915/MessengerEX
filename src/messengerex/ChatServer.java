
package messengerex;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    
    private static final int PORT = 9001;
    private static HashSet<String> names = new HashSet<String>();
    private static String password;
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
  
    public static void Server() throws Exception {
        
        System.out.println("The Chat server is running.");
        ServerSocket socket = new ServerSocket(PORT);
        
        try {
            
            while (true) {
                
                new Handler(socket.accept()).start();
            }
        }
        
        finally {

            socket.close();
        }
    }
    
    private static class Handler extends Thread {
        
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        public Handler (Socket socket) {
            
            this.socket = socket;
        }
        
        public void run () {
            
            try {
                
                in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
                out = new PrintWriter (socket.getOutputStream (), true);
                
                while (true) {
                    
                    out.println("SUBMITNAME");
                    name = in.readLine ();
                    
                    if (name == null) {
                        
                        return;
                    }
                    
                    out.println("SUBMITPASSWORD");
                    password = in.readLine();
                    
                    System.out.println(password);
                    
                    if (password.startsWith("student")) {
                        
                        synchronized (names) {

                            if (!names.contains(name)) {

                                names.add(name);
                                break;
                            }
                        }
                    }
                }
                
                System.out.println(name + " is online.");
                out.println("NAMEACCEPTED");
                writers.add(out);
                
                while (true) {
                    
                    String input = in.readLine();
                    
                    if (input == null) {
                        
                        return;
                    }
                    
                    for (PrintWriter writer : writers) {
                        
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            }
            
            catch (IOException e) {
                
                System.out.println(e);
            }
            
            finally {
                
                if (name != null) {
                    
                    System.out.println(name + " is offline.");
                    names.remove(name);
                }
                
                if (out != null) {
                    
                    writers.remove(out);
                }
                
                try {
                    
                    socket.close();
                }
                
                catch (IOException e) {
                    
                }
            }
        }
    }
}
