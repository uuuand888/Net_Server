package src;

public class NetPack {
	private final int MAX_LEN=4096;
	private String stx="";
	private String etx="";
	private String ver="";
	private int cmd=0;
	private String body="";
	private byte[] pack=null;
	
	public NetPack(int cmd, String body, String ver, String stx, String etx) {
		pack = new byte[MAX_LEN];
		this.stx = stx;
		this.etx = etx;
		this.ver = ver;
		this.cmd = cmd;
		this.body = body;
      System.arraycopy(stx.getBytes(), 0, pack, 0, 2);
      System.arraycopy(ver.getBytes(), 0, pack, 2, 4);
      System.arraycopy(toHH(body.length()), 0, pack, 6, 4);
      System.arraycopy(toHH(cmd), 0, pack, 10, 4);
      System.arraycopy(body.getBytes(), 0, pack, 14, body.length());
      System.arraycopy(etx.getBytes(), 0, pack, 14+body.length(), 2);		
	}
	public NetPack(int cmd, String body) {
		this(cmd, body, "1.00", "SX", "EX");
	}
	public NetPack() {
		;
	}
	public byte[] pack() {
		return pack;
	}
	public int unPack(byte[] pkg) {
		byte[] r = new byte[4096];
		System.arraycopy(pkg, 0, r, 0, 2);
		stx = new String(r);
		if (!"SX".equals(stx.substring(0, 2))) {
			return -1;
		}
		System.arraycopy(pkg, 2, r, 0, 4);
		ver = new String(r);
		System.arraycopy(pkg, 6, r, 0, 4);
		int len = hBytesToInt(r);
		System.arraycopy(pkg, 10, r, 0, 4);
		cmd = hBytesToInt(r);
		System.arraycopy(pkg, 14, r, 0, len);
		body = new String(r);
		System.arraycopy(pkg, 14+len, r, 0, 2);
		etx = new String(r);
		if (!etx.substring(0,2).equals("EX")) {
			return -2;
		}
		return 0;
	}
	public String getVer() {
		return ver;
	}
	public int getCmd() {
		return cmd;
	}
	public String getBody() {
		return body;
	}
	private static byte[] toHH(int n) {  
		  byte[] b = new byte[4];  
		  b[3] = (byte) (n & 0xff);  
		  b[2] = (byte) (n >> 8 & 0xff);  
		  b[1] = (byte) (n >> 16 & 0xff);  
		  b[0] = (byte) (n >> 24 & 0xff);  
		  return b;  
	}
  private static int hBytesToInt(byte[] b) {  
	  int s = 0;  
	  for (int i = 0; i < 3; i++) {  
	    if (b[i] >= 0) {  
	    s = s + b[i];  
	    } else {  
	    s = s + 256 + b[i];  
	    }  
	    s = s * 256;  
	  }  
	  if (b[3] >= 0) {  
	    s = s + b[3];  
	  } else {  
	    s = s + 256 + b[3];  
	  }  
	  return s;  
	} 
}
