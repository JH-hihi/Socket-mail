package socket;

import java.net.*;
import java.io.*;
import javax.swing.*;

public class SMTPServer extends JFrame  {
	private ServerSocket server;
	private BufferedReader reader;
	private PrintWriter writter;
	String str;
	JFrame jf=new JFrame("������");
	JPanel jp=new JPanel(null);
	private JLabel show=new JLabel("Show:");
	private JTextArea showText=new JTextArea();
	private JScrollPane jsp=new JScrollPane(showText);	
	
	public SMTPServer() {
		jf.setSize(500,400);
		jf.setLocation(400,200);
		//���ñ�ǩ��λ�úʹ�С
		show.setBounds(50,0,50,20);
		//���ù�����
		jsp.setBounds(100,0,350,300);
		jsp.setViewportView(showText);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//������
		jp.add(show);
		jp.add(jsp);
		jf.add(jp);
		jf.setContentPane(jp);
		jf.setLayout(null);
		jf.setVisible(true);  //ʹ����ɼ�
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//���ô���رշ�ʽ	
		
	}
	public void run() {
		try {
			//����Socket���ӣ�Ĭ�϶˿ں�Ϊ25
			server=new ServerSocket(25,3);
			Socket s=server.accept();
			InputStream in=s.getInputStream();
			OutputStream out=s.getOutputStream();
			try{				
				reader=new BufferedReader(new InputStreamReader(in));				
				writter=new PrintWriter(out,true);				
				send("220 Service ready");				
				receive();
				send("250 OK,pleased to meet you");
				receive();
				send("250 Sender OK");
				receive();
				send("250 Recipient OK");
				receive();
				send("354 Start mail input,end with <CRLF>\".\"<CRLF>");
				String str=null;
				while(!(str=reader.readLine()).equalsIgnoreCase(".")){					
					showText.append(str);
					showText.append("\n");
				}
				send("250 Message accepted for delivery");
				receive();
				send("221 closing connection");
		    }catch(Exception ex) {
				
			}finally {
				//�ر���Դ
				reader.close();
				writter.close();
				in.close();
				out.close();
		    	s.close();
		    }
	  }catch(IOException ex) {
		ex.printStackTrace();
	  }
	}
	//���ͻ��˷�����Ϣ
	public void send(String s) {
		writter.print(s+"\r\n");
		writter.flush();
	}
	//���տͻ�����Ϣ
	public void receive() throws IOException {	
		str=reader.readLine();			
	}
	public static void main(String[] args) {
		SMTPServer server=new SMTPServer();   
		server.run();
	}
}
