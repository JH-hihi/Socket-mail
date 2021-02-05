package socket;

import java.net.*;
import java.io.*;
import javax.swing.*;

public class SMTPServer extends JFrame  {
	private ServerSocket server;
	private BufferedReader reader;
	private PrintWriter writter;
	String str;
	JFrame jf=new JFrame("服务器");
	JPanel jp=new JPanel(null);
	private JLabel show=new JLabel("Show:");
	private JTextArea showText=new JTextArea();
	private JScrollPane jsp=new JScrollPane(showText);	
	
	public SMTPServer() {
		jf.setSize(500,400);
		jf.setLocation(400,200);
		//设置标签的位置和大小
		show.setBounds(50,0,50,20);
		//设置滚动条
		jsp.setBounds(100,0,350,300);
		jsp.setViewportView(showText);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//添加组件
		jp.add(show);
		jp.add(jsp);
		jf.add(jp);
		jf.setContentPane(jp);
		jf.setLayout(null);
		jf.setVisible(true);  //使窗体可见
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置窗体关闭方式	
		
	}
	public void run() {
		try {
			//创建Socket链接，默认端口号为25
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
				//关闭资源
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
	//给客户端发送消息
	public void send(String s) {
		writter.print(s+"\r\n");
		writter.flush();
	}
	//接收客户端消息
	public void receive() throws IOException {	
		str=reader.readLine();			
	}
	public static void main(String[] args) {
		SMTPServer server=new SMTPServer();   
		server.run();
	}
}
