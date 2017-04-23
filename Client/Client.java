/*
 * Client
 *
 */
import java.io.*;
import java.net.*;
public class Client{
    public static void main(String[] args) throws Exception{
        String s = null;
        Socket mySocket;
        DataInputStream in = null;
        DataOutputStream out = null;
        try{
            mySocket = new Socket("127.0.0.1",4441);
            in = new DataInputStream(mySocket.getInputStream());
            out = new DataOutputStream(mySocket.getOutputStream());
            for (int i=0;i<10;i++) {
            	out.writeUTF("hello server!"+i);
            	s = in.readUTF();
            	System.out.println(s);
            	Thread.sleep(1000);
            }
            mySocket.close();
        }catch(IOException e){
            System.out.println("can’t connect");
        }
    }
}