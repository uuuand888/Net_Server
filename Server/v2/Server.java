package v2;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import src.NetPack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class React implements Runnable {
	   	Socket fd;
        DataOutputStream out = null;
        DataInputStream in = null;
	   	React(String TName, Socket fd) {
	        this.fd=fd;
	        Thread.currentThread().setName(TName);
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
	        System.out.println("Exiting child thread."+Thread.currentThread().getName());
	    }
}

public class Server{
    public static void main(String[] args){
    	ExecutorService pool = Executors.newFixedThreadPool(1);
        ServerSocket server = null;
        Socket you = null;
		int	i=0;
        try{
            server = new ServerSocket(4441);
        	while(true) {
        		you = server.accept();
        		pool.execute(new React("Thread_"+i, you));
        		i++;
        	}
        }catch(IOException e1){
            System.out.println("ERROR:"+e1);
        }
    }
}