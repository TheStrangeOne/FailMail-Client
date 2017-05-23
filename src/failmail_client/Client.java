/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmail_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Set;
/**
 *
 * @author Aslan Assylkhanov, Altyn Zhelambayeva, Tair Maralov
 */
public class Client {
    public static String path=System.getProperty("user.dir")+"/FailMail_Shared/";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // TODO code application logic here
        
        Boolean weGood=false;
        
        PeerServer serv=null;
        
        System.out.println("Welcome to the FailMail");
         BufferedReader console = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.println("Enter either 1 or 2 to choose folder");
        File shared;
        int ent = Integer.valueOf(console.readLine());
        if(ent<2){
            ent=1;
        }
        else{
            ent=2;
        }
        path = path.concat("/"+ent+"/");
         shared = new File(path);
        if(!shared.exists()){
            shared.mkdir();
        }
        
       
        System.out.println("Your shared folder is " + shared.toString());
        System.out.println("You can add any files you want to share in the shared folder");
        System.out.println("After you finish, please press \"Start\" to connect to Server "
                + "and start sharing files with other peers");
        System.out.println("If you wish to finish the session please enter \"Exit\"");
        
        while(true){
            String input = console.readLine();
            input = input.toLowerCase();
            
            if(input.equals("start")){
                weGood = true;
                break;
            }
            else if(input.equals("exit")){
                weGood = false; 
                break;
            }
            else{
                System.out.println("Invalid input. Please, enter again");
            }
        }
        if(weGood){
            
            
           /* ServerSocket loc = new ServerSocket(0);
                System.out.println(loc.getLocalPort());
                loc.close();*/
            System.out.println("Server connection");
            Socket toServer = new Socket("localhost", 8080);
            
            OutputStreamWriter out = new OutputStreamWriter(toServer.getOutputStream());
           // String input = console.readLine();
            out.write("Hello\n");
            out.flush();
            
            
                
            
                
            
            BufferedReader buffer;
            buffer = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
            
            String msg = buffer.readLine();
            System.out.println(msg);
            if(msg.compareTo("Hi")==0){  
                System.out.println(shared.toString());
                ServerSocket clientServer = new ServerSocket(0);
                serv = new PeerServer(clientServer);
                serv.start();
                String[] files = shared.list();
                
                System.out.println(files.length);
                int i=0;
                for(i=0;i<files.length;i++){
                    String fileInfo;
                    
                    File file = new File(shared.toString()+"/"+files[i]);
                    if(file.isFile()){
                        
                    fileInfo="file:"+files[i]+":"+file.length()+":";
                    
                    Date date = new Date(file.lastModified());
                    fileInfo = fileInfo.concat(date.getDate()+ "/" + date.getMonth() + "/" + date.getYear());          
                    
                    fileInfo = fileInfo.concat(":"+clientServer.getLocalPort());
                    System.out.println(fileInfo);
                    out.write(fileInfo+"\n");
                    out.flush();
                    }
                }
                while(true){
                    String input = console.readLine().toLowerCase();
                    
                    if(input.startsWith("search")){
                        
                        String toGet = input.split(" ")[1];
                        out.write("search:"+toGet+"\n");
                        out.flush();
                        int num=0;
                        ArrayList<String> resList = new ArrayList(); 
                        while(true){
                            String t;
                            t = buffer.readLine();
                            if(t.compareTo("end")==0){
                                break;
                            }
                            else{
                                
                                System.out.println(num+" "+t);
                                if(num!=0) resList.add(t);
                                num++;
                            }
                        }
                        if(num>1){
                        System.out.println("Please choose the file by writing its number, if you don'"
                                + "t want to download, just enter 0 " );
                        
                        input = console.readLine();
                        num=Integer.valueOf(input);
                        if(num>0 && num<=resList.size()){
                        new DownloadHandler(resList.get(num-1).split(":")).start();
                        }
                        }
                    }
                    else if(input.compareTo("bye")==0){
                        out.write(input+":"+clientServer.getLocalPort()+"\n");
                        out.flush();
                        
                        
                        break;
                        
                    }
                    else{
                        System.out.println("Invalid input. Please, enter again");
                    }
                }
                
            }
            
                
                    
        }
 
       
        
        

    }
   
    
    
    
}
