
package messengerex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient {

    BufferedReader in; 
    PrintWriter out;
    OutputStream fileout;

    JFrame frame = new JFrame ("Messenger");
    JTextField text = new JTextField(40);
    JTextArea message = new JTextArea(9, 40);
    JPasswordField pass = new JPasswordField();
    JButton sendfile = new JButton("Attach File");
    JFileChooser chooser = new JFileChooser();
    
    public ChatClient () {
        
        text.setEditable(false);
        message.setEditable(false);
        
        frame.getContentPane().add(text, "North");
        frame.getContentPane().add(new JScrollPane(message), "Center");
        frame.getContentPane().add(sendfile, "South");
        frame.pack();
        
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                out.println(text.getText());
                text.setText("");
            }
        });
        
        sendfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                chooser = new JFileChooser();
                chooser.showOpenDialog(frame);
                
                out.println("FILE: " + sendFile().getName());
            }
        });
    }
    
    private String getServerAddress() {
        
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server", "Welcome to the Messenger", JOptionPane.QUESTION_MESSAGE);
    }
    
    private String getName() {
        
        return JOptionPane.showInputDialog(frame, "Choose a username:", "Username Selection", JOptionPane.PLAIN_MESSAGE);
    }
    
    private String getPassword() {
        
        return JOptionPane.showInputDialog(frame, "Enter Messenger password:", "Password Input", JOptionPane.PLAIN_MESSAGE);
    }
    
    private File sendFile () {
        
        return chooser.getSelectedFile();
    }
    
    private String getFile () {
        
        return "end";
    }
    
    private void run() throws IOException {
        
        String serverAddress = getServerAddress();
        Socket clientSocket = new Socket(serverAddress, 9001);
        
        in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter (clientSocket.getOutputStream(), true);
        
        while (true) {
            
            String line = in.readLine();
   
            if (line.startsWith("SUBMITNAME")) {
                
                out.println(getName());
            }
            
            else if (line.startsWith("SUBMITPASSWORD")) {
                
                out.println(getPassword());
            }
            
            else if (line.startsWith("NAMEACCEPTED")) {
                
                text.setEditable (true);
            }
            
            else if (line.startsWith("MESSAGE")) {
                
                message.append(line.substring(8) + "\n");
            }
            
            else if (line.startsWith("FILEMESSAGE")) {
                
                out.println(getFile());
                message.append(line.substring(11) + "\n");
            }
        }
    }
    
    public static void Client() throws Exception {
        
        ChatClient client = new ChatClient();
        
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        
        client.run();
    }
}