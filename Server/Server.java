/*
 *server
 *
 */
import java.io.*;
import java.net.*;
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
		                String s = in.readUTF();
		                if(s==null) 
		                	break;
		                System.out.println(s);
		                out.writeUTF("客户，你好，我是服务器"+Thread.currentThread().getName());
		    	}
	    		in.close();
	    		out.close();
	    	}catch(IOException e) {
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