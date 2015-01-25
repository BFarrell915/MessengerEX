
package messengerex;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class MessengerEx {

    public static void main(String[] args) throws Exception {
        
        Object[] possibleValues = {"Server", "Client"};
        
        Object select = JOptionPane.showInputDialog(null, "Chat Client or Chat Server", "MessengerEx", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
        
        if (select == "Server") {
            
            ChatServer.Server();
        }
        
        else {
            
            ChatClient.Client();
        }
    }
}