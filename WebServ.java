import java.net.*;
import java.io.*;
import java.util.*;

class WebServ 
{
	public static void main(String[] args) 
	{
		ServerSocket	server;
		Socket			client;
		try {
			server = new ServerSocket(9999);
		} catch(Exception e)	{
			e.printStackTrace();
			//System.out.println("IOException: " + e);
			return;
		}

		while(true){
			try {
				client = server.accept();	//接受连接
				System.out.println("\n\naccept...");
				BufferedReader bs = 
						new BufferedReader(
						new InputStreamReader(
						client.getInputStream()));
				String cmd = bs.readLine();
				System.out.println("--=> "+cmd);
				
				StringTokenizer st = new StringTokenizer(cmd);
				st.nextToken();
				String filename = st.nextToken().substring(1);
				System.out.println("Filename -> "+filename);

				DataOutputStream ps = 
						new DataOutputStream(
						new BufferedOutputStream(
						client.getOutputStream()));
				DataInputStream br =
						new DataInputStream(
						new BufferedInputStream(
						new FileInputStream(filename)));

				Thread w =new Thread(new DataTran(ps, br));
				w.setDaemon(false);	// Thread is daemon
				w.start();

			} catch(IOException e)	{
				System.out.println("IOException End."+e);
			}
		}	//while
	}
}
class DataTran
	implements Runnable	// extends Thread
{
	DataOutputStream ps;
	DataInputStream	 br;
	DataTran(DataOutputStream p, DataInputStream b)
	{	ps=p;	br=b;
//	    setDaemon(false);	// Thread is daemon
	}
	public void run(){
		try{
		try	{
			while(true){
				byte ch =br.readByte();
				if(ch=='#')ps.writeBytes(
					"<h1>HZLOU</h1>"+(new Date()));
				else ps.writeByte(ch);
			}
//			while(true)ps.writeByte(br.readByte());
		} catch(EOFException e)	{
			System.out.println("End write(read).");
			ps.close();
			br.close();
		}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
};