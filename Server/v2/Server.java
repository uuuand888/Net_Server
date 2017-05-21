/*
 * 线程池处理具体的事务；
 * 线程池处理accept；
 * 
 */

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

class PoolAccept implements Runnable {
	ExecutorService pool = Executors.newFixedThreadPool(2);
	ServerSocket lfd;
	Socket fd;
	int	i=0;
	PoolAccept(ServerSocket listenfd) {
		this.lfd = listenfd;
	}
	@Override
	public void run() {
		while(true) {
			try {
				fd = lfd.accept();
				pool.execute(new React("Thread_"+i, fd));
				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Server{
    public static void main(String[] args){
    	ExecutorService pool = Executors.newFixedThreadPool(10);
        ServerSocket server = null;
		int	i;
        try{
            server = new ServerSocket(4441);
            for (i=0; i<10; i++) {
            	pool.execute(new PoolAccept(server));
            }
        }catch(IOException e1){
            System.out.println("ERROR:"+e1);
        }
    }
}