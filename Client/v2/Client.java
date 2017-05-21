package v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import src.*;

public class Client{
    public static void main(String[] args) throws Exception{
        String str = null;
        Socket mySocket;
        final int MAX_PACK_LEN = 4096;
        final int cmd = 101;
        DataInputStream in = null;
        DataOutputStream out = null;
        try{
            mySocket = new Socket("127.0.0.1",4441);
            in = new DataInputStream(mySocket.getInputStream());
            out = new DataOutputStream(mySocket.getOutputStream());
            String s = "hello server!";
            NetPack np = new NetPack(101, s); 
            out.write(np.pack());
            str = in.readUTF();
            System.out.println(str);
            mySocket.close();
        }catch(IOException e){
            System.out.println("can’t connect");
        }
    }
}
