package networking;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
public class ConnectTwitch{
	public static String oauth = "";
	public static String channelname = "";
	public static String botName = "";
	String user = "Placeholder";
	String path = "userauth.txt";
	BufferedWriter bw;
	String readline;
	public List<String> readlinelist = new ArrayList<String>();
	public ConnectTwitch(){
		try{
			File userauth = new File(path);
			if(!userauth.exists()){
				userauth.createNewFile();
			}
			//write file
			//read file
			try{
				InputStream fis = new FileInputStream(userauth);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				while((readline = br.readLine())!=null){
					readlinelist.add(readline);
				}
				channelname = readlinelist.get(0);
				botName = readlinelist.get(1);
				oauth = readlinelist.get(2);
				br.close();
			}catch(IOException e){
				e.printStackTrace();
			}catch(IndexOutOfBoundsException aoob){
				JOptionPane.showMessageDialog(null, "You have not set your user information.\nPlease go to the 'Settings' tab to set it","User information not set.",JOptionPane.PLAIN_MESSAGE);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setNewUserCred(String channel,String thisoauth,String thisbotName){
		try{
			File userauth = new File(path);
			if(!userauth.exists()){
				userauth.createNewFile();
			}
			//write file
			PrintWriter pw = new PrintWriter(userauth);
			pw.print("");
			pw.close();
			try{bw = new BufferedWriter(new FileWriter(userauth));
				bw.write("#"+channel.toLowerCase()+"");
				bw.newLine();
				bw.write(thisbotName+"");
				bw.newLine();
				bw.write(thisoauth);
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			try{
				InputStream fis = new FileInputStream(userauth);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				while((readline = br.readLine())!=null){
					readlinelist.add(readline);
				}
				channelname = readlinelist.get(0);
				botName = readlinelist.get(1);
				oauth = readlinelist.get(2);
				br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
	}catch(Exception e){
		e.printStackTrace();
	}
}
	public String returnChannel(){
		String userchannel = "";
		try{
			userchannel = readlinelist.get(0).replaceAll("#", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return userchannel;
	}
	public String returnBotName(){
		String botname = "";
		try{
			botname = readlinelist.get(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return botname;
	}
	public String returnOAuth(){
		String oauth = "";
		try{
			oauth = readlinelist.get(2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return oauth;
	}
}
