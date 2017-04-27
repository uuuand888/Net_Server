/*
 * send/recv
 * stx(2)+ver(4)+len(4)+cmd(4)+body(n)+etx(2)
 * stx="SX"
 * etx="EX"
 * cmd=101
 * ver="1.0" 
 * 
 */

package v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
            byte[] buf=new byte[MAX_PACK_LEN]; 
//            System.arraycopy(src, srcPos, dest, destPos, length);
            System.arraycopy("SX".getBytes(), 0, buf, 0, 2);
            System.arraycopy("1.00".getBytes(), 0, buf, 2, 4);
            System.arraycopy(toHH(s.length()), 0, buf, 6, 4);
            System.arraycopy(toHH(cmd), 0, buf, 10, 4);
            System.arraycopy(s.getBytes(), 0, buf, 14, s.length());
            System.arraycopy("EX".getBytes(), 0, buf, 14+s.length(), 2);
            out.write(buf);
            str = in.readUTF();
            System.out.println(str);
            mySocket.close();
        }catch(IOException e){
            System.out.println("can’t connect");
        }
    }
	public static byte[] toHH(int n) {  
		  byte[] b = new byte[4];  
		  b[3] = (byte) (n & 0xff);  
		  b[2] = (byte) (n >> 8 & 0xff);  
		  b[1] = (byte) (n >> 16 & 0xff);  
		  b[0] = (byte) (n >> 24 & 0xff);  
		  return b;  
		}  
}