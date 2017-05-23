/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmail_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aslan
 */
public class PeerServer extends Thread {
        ServerSocket server;
        boolean stop=true;
    public PeerServer(ServerSocket server) {
        this.server=server;
    }
    public void run(){
        while(stop){
            try {
                if(!server.isClosed()){
                Socket user = server.accept();
                System.out.println("Have a new connection");
                new PeerHandler(user).start();
                }
            } catch (IOException ex) {
                
                
                Logger.getLogger(PeerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }
    public void stopIt() throws IOException, SocketException{
        if(!server.isClosed()){
        server.setSoTimeout(1000);
        server.close();
        stop=false;
        }
    }
}
