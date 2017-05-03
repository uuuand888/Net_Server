/*
 *server
 * stx(2)+ver(4)+len(4)+cmd(4)+body(n)+etx(2)
 * stx="SX"
 * etx="EX"
 * cmd=101
 * ver="1.0" 
 */

package v1;

import java.io.*;
import java.net.*;

import Utils.NetPack;
class React implements Runnable {
	   	Thread t;
	   	Socket fd;
        DataOutputStream out = null;
        DataInputStream in = null;
	   	React(String TName, Socket fd) {
	        // Create a new, second thread
	        t = new Thread(this, TName);
	        this.fd=fd;
	        System.out.println("Child thread: " + t);
	        t.start(); // Start the thread
	    }

	    // This is the entry point for the second thread.
	    public void run() {
	        try {
	            in = new DataInputStream(fd.getInputStream());
	            out = new DataOutputStream(fd.getOutputStream());
		    	while (true){
		    		byte[] b = new byte[4096];
		    		int res = in.read(b, 0, 4096);
		    		if (res<0)
		    			break;
		    		System.out.println("res="+res);
		    		NetPack np = new NetPack();
		    		res = np.unPack(b);
		    		System.out.println("res="+res);
		    		System.out.println("ver="+np.getVer());
		    		System.out.println("cmd="+np.getCmd());
		    		System.out.println("body="+np.getBody());	
	                out.writeUTF("客户，你好，我是服务器"+Thread.currentThread().getName());
		    	}
	    		in.close();
	    		out.close();
	    	}catch(IOException e) {
	    		try {
					in.close();
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		System.out.println("ERROR:"+e);
	    	}
	        System.out.println("Exiting child thread.");
	    }
}

public class Server{
    public static void main(String[] args){
        ServerSocket server = null;
        Socket you = null;
		int	i=0;
        try{
            server = new ServerSocket(4441);
        	while(true) {
        		you = server.accept();
        		new React("Thread_"+i, you);
        		i++;
        	}
        }catch(IOException e1){
            System.out.println("ERROR:"+e1);
        }
    }
}