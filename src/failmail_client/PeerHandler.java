/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmail_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aslan
 */
public class PeerHandler extends Thread{
    Socket peer;
    BufferedReader reqTunnel;
    OutputStreamWriter respTunnel;
    public PeerHandler(Socket socket){
        peer=socket;
    }
    public void run(){
        try {
            reqTunnel = new BufferedReader(new InputStreamReader(peer.getInputStream()));
            respTunnel = new OutputStreamWriter(peer.getOutputStream());
            String request = reqTunnel.readLine(), filename="";
            if(request.startsWith("Download")){
                filename = request.substring(9);
                System.out.println(filename);
                
                File toSend = new File(Client.path+filename);
                if(toSend.exists()){
                    byte[] data = Files.readAllBytes(toSend.toPath());
                    DataOutputStream up = new DataOutputStream(peer.getOutputStream());
                    up.write(data,0,(int)toSend.length());
                    up.flush();
                    peer.close();
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
}
