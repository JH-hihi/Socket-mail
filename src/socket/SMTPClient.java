package socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SMTPClient extends JFrame{
	private Socket s;
	private BufferedReader reader;
	private PrintWriter writter;
	String str;
	JFrame jf=new JFrame("客户端");
	JPanel jp=new JPanel(null);
	private JLabel from=new JLabel("From:");
	private JLabel to=new JLabel("To:");
	private JLabel Message=new JLabel("Message:");
	private JButton send=new JButton("Send Message");
	private JTextField fromText=new JTextField();
	private JTextField toText=new JTextField();
	private JTextArea MessageText=new JTextArea();
	private JScrollPane jsp=new JScrollPane(MessageText);
	
	public SMTPClient() {
		
		jf.setSize(600,400);
		jf.setLocation(900,200);
		//设置标签的位置和大小
		from.setBounds(50,0,50,20);
		to.setBounds(50,40,50,20);
		Message.setBounds(40,80,60,20);
		//设置文本框的位置和大小
		fromText.setBounds(100,0,350,30);
		toText.setBounds(100, 40,350,30);
		//设置按钮位置和大小
		send.setBounds(100,250,150,30);	
		//设置滚动条
		jsp.setBounds(100,80,350,150);
		jsp.setViewportView(MessageText);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//添加组件
		jp.add(from);
		jp.add(to);
		jp.add(Message);
		jp.add(send);	
		jp.add(fromText);
		jp.add(toText);
		jp.add(jsp);
		jf.add(jp);
		jf.setContentPane(jp);
		jf.setLayout(null);
		jf.setVisible(true);  //使窗体可见
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置窗体关闭方式	
		//监听send按钮
		send.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {	    	
					try {
						start();
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
		      }	
		});		
	}
		public void start() throws InterruptedException {
			try {
				Socket s=new Socket("localhost",25);
				System.out.println("建立连接");
				InputStream in=s.getInputStream();
				OutputStream out=s.getOutputStream();
				try {
					reader=new BufferedReader(new InputStreamReader(in));
					writter=new PrintWriter(out,true);
					String host=InetAddress.getLocalHost().getHostName();
					receive();
					Thread.sleep(1000);
					send("HELLO "+host);
					Thread.sleep(1000);
					receive();
					Thread.sleep(1000);
					send("MAIL FROM:<"+fromText.getText().trim()+">");
					Thread.sleep(1000);
					receive();
					Thread.sleep(1000);
					send("RCPT TO:<"+toText.getText().trim()+">");
					Thread.sleep(1000);
					receive();
					Thread.sleep(1000);
					send("DATA");
					Thread.sleep(1000);
					String ss[]=MessageText.getText().split("\n");
					for(int i=0;i<ss.length;i++) {
						send(ss[i]);
					}
					Thread.sleep(ss.length*1000);
					receive();
					Thread.sleep(1000);
					receive();
					Thread.sleep(1000);
					send("QUIT");
					Thread.sleep(1000);
					receive();
				}finally {
					s.close();
				}
			}catch(IOException ex) {
				System.out.println("连接失败");
				ex.printStackTrace();
			}
		}
		void send(String s) {
			writter.print(s+"\r\n");
			writter.flush();
			System.out.println("C:"+s);
		}
		void receive() throws IOException {
			str=reader.readLine();			
			System.out.println("S:"+str);
		}
	public static void main(String[] args) {
        SMTPClient client=new SMTPClient(); 
	}
}
