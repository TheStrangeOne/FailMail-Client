/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmail_client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aslan
 */
public class DownloadHandler extends Thread {
    Socket tunnel;
    OutputStreamWriter reqTunnel;
    InputStreamReader respTunnel;
    BufferedReader resTunnel;
    Reader read;
    String[] info;
    String filename;
    int size, port;
    public DownloadHandler(String[] encoded){
        info=encoded;
    }

    @Override
    public void run() {
        filename = info[0];
        size = Integer.valueOf(info[1]);
        port = Integer.valueOf(info[3]);
        System.out.println("Filename: "+filename);
        System.out.println("Size: "+ size);
        System.out.println("Port: "+port);
        
        try {
            tunnel =  new Socket("localhost", port);
            
            reqTunnel = new OutputStreamWriter(tunnel.getOutputStream());
            respTunnel = new InputStreamReader(tunnel.getInputStream());
            reqTunnel.write("Download:"+filename+"\n");
            reqTunnel.flush();
            File creat = new File(Client.path+filename);
            
           /* if(!creat.exists()){
                creat.createNewFile();
            }*/
            byte[] data=new byte[size];
            DataInputStream down = new DataInputStream(tunnel.getInputStream());
            int off=0;
            while(true){//Getting files in chunks
                int siz=100;
                if(off+siz>size){
                    siz=size-off;
                }
                int byt=down.read(data, off, siz);
                System.out.println(off+" "+siz+" "+byt);
                if(byt>0)off+=byt;
                else break;
                
                    }
            System.out.println("Finished downloading");
            FileOutputStream fos = new FileOutputStream(Client.path+filename);
            fos.write(data);
            fos.flush();
            fos.close();
            tunnel.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
            
            
       
    }
    
}
