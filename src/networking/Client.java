package networking;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import gui.Guimainclass;
import main.UserGroup;
public class Client extends PircBot{
	public String channelname = ConnectTwitch.channelname;
	String oauth = ConnectTwitch.oauth;
	public boolean clientReady = false;
	Date date = new Date();
	public String userPoint = "";
	public String currentTime = date.toString();
	int tempValue = 0;
	//FROM READ FILE
	int temppoint = 0;
	//User object
	public static Map<String,Integer> useronline = new HashMap<String,Integer>();
	public static Map<String,Integer> userbuffer = new HashMap<String,Integer>();
	//User object
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService raffleexecutor = Executors.newScheduledThreadPool(1);
	int userpointduration = 60;
	int userpointamount = 1;
	ScheduledFuture<?> handle;
	ScheduledFuture<?> handle2;
	int userpointstorage = 60;
	boolean joinnametrue,leavenametrue,commanderrortrue;
	static boolean userrafflestart = false;
	List<String> userraffleentry = null;
	int userraffleamount = 0;
	//COMMAND LIST
	static public String[] commandlist = {"!points","!gamble ","!setpoints ","!addpoints ","!luckydraw","!pointsdraw",/*6*/"!fstartraffle ","!fstopraffle","!fraffle","!chatmodlist","!fraffledraw"};
	boolean containcommand = false;
	//CONSTRUCTOR
	public Client(){
		if(!isConnected()){
		try{
			getStoredPoints();
			this.setName(ConnectTwitch.botName);
			setVerbose(true);
			connect("irc.chat.twitch.tv",6667,oauth);
			joinChannel(channelname);
			sendRawLine("CAP REQ :twitch.tv/membership");	
			runPointSettings();
			clientReady = true;
			joinnametrue = getCommandSettings("joinname");
			leavenametrue = getCommandSettings("leavename");
			commanderrortrue = getCommandSettings("error");
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Failed to Connect.\nPlease set correct settings in 'Settings' tab and\nrestart the application.","Connection Failed.",JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	//CONSRUCTOR END
	public void onDisconnect(){
		appendGui("Disconnected from server,attempting to reconnect"+"\n");
		for(int i = 0;i<5;i++){
			try{
				connect("irc.chat.twitch.tv",6667,oauth);
				appendGui("Attempting to connect ...");
			}catch(Exception e){
				appendGui("Unable to reconnect,retrying in 5 seconds ...");
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e2){
					e2.printStackTrace();
				}
			}
		}
	}
	public void appendGui(String appendText){
		Guimainclass.textArea.append(appendText+"\n");
	}
	public void appendUser(String appendText){
		Guimainclass.textArea_2.append(appendText+"\n");
	}
	public void setUser(){
		Guimainclass.textArea_2.setText("Online Users:"+"\n");
	}
	public void sendUser(String message){
		sendMessage(channelname,message);
		appendGui(ConnectTwitch.botName+ " : "+message);
	}
	//on Join
	public void getStoredPoints(){
		BufferedReader br;
		FileInputStream fis;
		String readline;
		try{
			fis = new FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(fis));
			while((readline = br.readLine())!=null){
				String[] tempreadline = readline.split(",");
				userbuffer.put(tempreadline[0], Integer.parseInt(tempreadline[1]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}	    
	int outtempoint = 0;
	public void setTemppoint(int temppoint){
		outtempoint = temppoint;
	}
	public int getTemppoint(){
		return outtempoint;
	}
	List<String> oplist = new ArrayList<String>();
	public void onJoin(String channel, String sender, String login, String hostname){
		appendGui(sender+" has joined the channel!");
		if(joinnametrue){
			sendMessage(channelname,sender+" has joined the channel! "+"\n");
		}
		if(userbuffer.containsKey(sender)){
			useronline.put(sender,userbuffer.get(sender));
		}else{
			useronline.put(sender, 0);
			userbuffer.put(sender, 0);			
		}
		Guimainclass.textArea_2.setText("Online Users:"+"\n");
		for(Map.Entry<String, Integer> entry:useronline.entrySet()){
				appendUser(entry.getKey()+"    "+entry.getValue());
		}
	}
	//on Quit
	public void onPart(String channel, String sender, String login, String hostname){
		appendGui(sender+" has left the channel! ");
		if(leavenametrue){
			sendMessage(channelname,sender+" has left the channel! "+"\n");
		}
		useronline.remove(sender);
		Guimainclass.textArea_2.setText("Online Users:"+"\n");
		for(Map.Entry<String, Integer> entry:useronline.entrySet()){
			appendUser(entry.getKey()+"    "+entry.getValue());
		}
	}
	
	//MESSAGES!!!
	public void onMessage(String channel, final String sender, String login, String hostname,String message){
		//If user types POINTS
		if(message.equalsIgnoreCase(commandlist[0])&&checkAuth(sender,commandlist[0])){
			showNotice(sender + " has " + userbuffer.get(sender)+" points!");
		}
		//IF USER TYPES GAMBLE
		if(message.toLowerCase().contains(commandlist[1])&&checkAuth(sender,commandlist[1])){
			try{
				int gamblemessage = Integer.parseInt(message.replace("!gamble ",""));
				if(gamblemessage < 0 || gamblemessage > useronline.get(sender)){
					throw new ArrayIndexOutOfBoundsException();
				}
					Random dice = new Random();
					int luckygamble = dice.nextInt(2);
					if(luckygamble == 0){
						userbuffer.put(sender, userbuffer.get(sender)+gamblemessage);
						useronline.put(sender, userbuffer.get(sender)+gamblemessage);
						showNotice("Congratulatoins ! "+sender+" now have "+userbuffer.get(sender)+" points!");
					}else{
						userbuffer.put(sender, userbuffer.get(sender)-gamblemessage);
						useronline.put(sender, userbuffer.get(sender)-gamblemessage);
						showNotice("Sorry ! "+sender+" now only have "+userbuffer.get(sender)+" points...");
				}
					Guimainclass.textArea_2.setText("Online Users:"+"\n");
					for(Map.Entry<String, Integer> entry:useronline.entrySet()){
						appendUser(entry.getKey()+"    "+entry.getValue());
					}
			}catch(ArrayIndexOutOfBoundsException e){
				showError("You don't have enough points...");
			}catch(Exception e){
				showError("You entered an invalid command...");
			}finally{
				pointStorage.run();
			}
		}
		//USER SET POINTS
		if(message.toLowerCase().contains(commandlist[2])&&checkAuth(sender,commandlist[2])){
			String[] setpointmessage = message.replace("!setpoints ","").split(" ");
			try{
				if(setpointmessage.length!=2){
					throw new ConnectException();
				}else{
					String usernameset = setpointmessage[0];
					int userpointset = Integer.parseInt(setpointmessage[1]);
					if(userbuffer.get(usernameset) == null ||userpointset < 0){
						throw new IOException();
					}else{
						userbuffer.put(usernameset, userpointset);
						useronline.put(usernameset, userpointset);
						showNotice(usernameset+" now is set to "+userbuffer.get(usernameset)+" points!");
					}
				}
			}catch(IOException ie){
				showError("User does not exist or point amount is invalid.");
			}catch(Exception e){
				showError("You entered an invalid command...");
			}
				finally{
					Guimainclass.textArea_2.setText("Online Users:"+"\n");
					for(Map.Entry<String, Integer> entry:useronline.entrySet()){
						appendUser(entry.getKey()+"    "+entry.getValue());
					}
				pointStorage.run();
			}
		}
		if(message.toLowerCase().contains(commandlist[3])&&checkAuth(sender,commandlist[3])){
			String[] addpointmessage = message.replace("!addpoints ","").split(" ");
			try{
				if(addpointmessage.length!=2){
					throw new ConnectException();
				}else{
					String usernameset = addpointmessage[0];
					int userpointset = Integer.parseInt(addpointmessage[1]);
					if(userbuffer.get(usernameset) == null ||userpointset < 0){
						throw new IOException();
					}else{
						userbuffer.put(usernameset, userbuffer.get(usernameset)+userpointset);
						useronline.put(usernameset, userbuffer.get(usernameset)+userpointset);
						showNotice(usernameset+" now is set to "+userbuffer.get(usernameset)+" points!");
					}
				}
			}catch(IOException ie){
				showError("User does not exist or point amount is invalid.");
			}catch(Exception e){
				showError("You entered an invalid command...");
			}
				finally{
					Guimainclass.textArea_2.setText("Online Users:"+"\n");
					for(Map.Entry<String, Integer> entry:useronline.entrySet()){
						appendUser(entry.getKey()+"    "+entry.getValue());
					}
				pointStorage.run();
			}
		
		}
		if(message.equalsIgnoreCase(commandlist[4])&&checkAuth(sender,commandlist[4])){
			try{
				User[] userdraw = getUsers(channelname);
				Random luckydraw = new Random();
				String drawnuser = userdraw[luckydraw.nextInt(userdraw.length)].toString();
				showError(drawnuser+" has been selected!");
			}catch(Exception e){
				showError("You entered an invalid command...");
			}
			
		}
		if(message.equalsIgnoreCase(commandlist[5])&&checkAuth(sender,commandlist[5])){
			try{
				Map<String,Integer> pointslist = new HashMap<String,Integer>();
				int accupoints = 0;
				int winner = 0;
				Random dice = new Random();
				for(Entry<String,Integer> entry:useronline.entrySet()){
					accupoints += entry.getValue();
					pointslist.put(entry.getKey(), accupoints);
					winner = dice.nextInt(accupoints);
					System.out.println(accupoints);
				}
				System.out.println(winner);
				String winnername = "";
				for(Entry<String,Integer> entry:pointslist.entrySet()){
					if(winner<entry.getValue()){
						winnername = entry.getKey();
						break;
						}
				}
					showNotice(winnername+" has been won the pointsdraw with "+useronline.get(winnername)+" points!");
			}catch(Exception e){
				showError("You entered an invalid command...");
			}
		}
		if(message.toLowerCase().contains(commandlist[6])&&checkAuth(sender,commandlist[6])){
			try{
				if(!userrafflestart){
						userrafflestart = true;
						userraffleentry = new ArrayList<String>();
						String rafflemessage = message.replace("!fstartraffle ","");
						raffleexecutor = Executors.newScheduledThreadPool(1);
						userraffleamount = Integer.parseInt(rafflemessage);
						showNotice("A raffle has started ! Type '!raffle' to join ! Ends in "+rafflemessage+" seconds!");
						raffleexecutor.schedule(rafflerun(userraffleentry), userraffleamount, TimeUnit.SECONDS);
				}else{
					showError("A raffle is already going on!");
				}
			}catch(Exception e){
				showError("You entered an invalid command...");
				e.printStackTrace();
			}
		}
		if(message.equalsIgnoreCase(commandlist[8])&&checkAuth(sender,commandlist[8])){
			try{
				if(userrafflestart){
					if(!userraffleentry.contains(sender)){
						userraffleentry.add(sender);
					}else{
						showError(sender+" is already in the raffle.");
					}
				}else{
					showError("The raffle has not started.");
				}
			}catch(Exception e){
				showError("You entered an invalid command...");
				e.printStackTrace();
			}
		}if(message.equalsIgnoreCase(commandlist[7])&&checkAuth(sender,commandlist[7])){
			try{
				if(userrafflestart){
					userrafflestart = false;
					raffleexecutor.shutdownNow();
					Thread t1 = new Thread(rafflerun(userraffleentry));
					t1.start();
				}
			}catch(Exception e){
				showError("You entered an invalid command...");
				e.printStackTrace();
			}
		}if(message.equalsIgnoreCase(commandlist[9])&&checkAuth(sender,commandlist[9])){
			String modstring = "Chat Moderator List :";
			for(String entry:UserGroup.userlist.get("Operator")){
				modstring += " "+entry+" ,";
			}
			showNotice(modstring);
		}if(message.equalsIgnoreCase(commandlist[10])&&checkAuth(sender,commandlist[10])){
			try{
				if(userraffleentry.size()!=0){
					Random dice = new Random();
					String rafflewinner = userraffleentry.get(dice.nextInt(userraffleentry.size()));
					showNotice(rafflewinner+" is the winner of the raffle ! Congratulations !");
				}else{
					showError("Your raffle list is empty!");
				}
			}catch(Exception e){
				showError("You entered an invalid command...");
			}
		}
		if(Arrays.asList(commandlist).contains(message.toLowerCase())){
			containcommand = true;
		}
	}
	//MESSAGES!!!
	//Utilities
	private Runnable rafflerun(List<String> userraffleentry){
		Runnable raffletimer = new Runnable(){
			public void run(){
				String rafflestring = "Raffle ended ! These people : ";
				for(String s:userraffleentry){
					rafflestring += s+",";
				}
				rafflestring += "\nhas entered the raffle !";
				sendMessage(channelname,rafflestring);
				appendGui(rafflestring);
				userrafflestart = false;
			}
		};
		return raffletimer;
	}
	 public static String getKeyFromValue(Map hm, Object value) {
		    for (Object o : hm.keySet()) {
		      if (hm.get(o).equals(value)) {
		        return o.toString();
		      }
		    }
		    return null;
		  }
	List<String> containedUser = new ArrayList<String>();
	public void searchUser(String regex){
		containedUser.clear();
		if(Guimainclass.textField_8.getText()!=""){
			for(Map.Entry<String, Integer> entry:useronline.entrySet()){
				if(entry.getKey().toLowerCase().contains(regex.toLowerCase())){
					containedUser.add(entry.getKey());
					}
				}
			setUser();
			for(int i = 0;i<containedUser.size();i++){
				appendUser(containedUser.get(i)+"    "+useronline.get(containedUser.get(i)));
			}
		}
		compareTwo(containedUser);
	}
	/*
	public void searchMax(int max){
		containedUser2.clear();
		setCompare(containedUser);
		for(Map.Entry<String, Integer> entry:useronline.entrySet()){
			if(entry.getValue()<max){
				containedUser2.add(entry.getKey());
				}
		}
		setUser();
	}
	*/
	List<String> containedUser3 = new ArrayList<String>();
	public void searchMin(int min){
		containedUser3.clear();
		if(Guimainclass.textField_10.getText()!=""){
			for(Map.Entry<String, Integer> entry:useronline.entrySet()){
				if(entry.getValue()>min){
					containedUser3.add(entry.getKey());
					}
			}
			setUser();
			for(int i = 0;i<containedUser3.size();i++){
				appendUser(containedUser3.get(i)+"    "+useronline.get(containedUser3.get(i)));
			}
		}
		compareTwo(containedUser3);
	}
	Set<String> compareset;
	public void compareTwo(List<String> list){
		compareset = new HashSet<String>(list);
		list.retainAll(containedUser);
		list.retainAll(containedUser3);
	}
	public Set<String> getCompareTwo(){
		return compareset;
	}
	boolean setReady = true;
	    Runnable pointSetText = new Runnable() {
	        public void run() {     
	        	setReady = true;
	            for(Map.Entry<String, Integer> entry:useronline.entrySet()){
					if(entry.getValue()<0){
						useronline.put(entry.getKey(), 0);
					}
	            	if(setReady){
	            		setUser();
	            	}
	            	tempValue = entry.getValue();
	            	useronline.put(entry.getKey(),tempValue+=userpointamount);
	            	userbuffer.put(entry.getKey(),tempValue+=userpointamount);
    				if(Guimainclass.textField_8.getText().equals("")&&Guimainclass.textField_10.getText().equals("")){
    					appendUser(entry.getKey()+"    "+entry.getValue());
    				}else{
    					if(getCompareTwo().contains(entry.getKey())){
    						appendUser(entry.getKey()+"    "+entry.getValue());
    					}
    				}
	            	setReady = false;
	            }
	        }
	    }; 

	    //STORAGE
		ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
		String path = "userpoints.txt";
		BufferedWriter bw;
	    public Runnable pointStorage = new Runnable(){
	    	public void run(){
		    		try{
		    			File userpoints = new File(path);
		    			if(!userpoints.exists()){			
		    					userpoints.createNewFile();
		    			}else{
		    				try{
		    					bw = new BufferedWriter(new FileWriter(path));
			    	    		if(!useronline.isEmpty()){
			    					for(Map.Entry<String, Integer> entry:useronline.entrySet()){
			    						bw.write(entry.getKey()+","+entry.getValue());
			    						bw.newLine();
			    					}
			    	    	}			
			    	    		bw.close();
		    				}catch(Exception e){
		    					e.printStackTrace();
		    				}	
		    	    
		    			}
		    			}catch(Exception e){
		    				e.printStackTrace();
		    		}	    		
	    	}
	    };
	    //POINTS SETTINGS			
	    List<String> thisarray = new ArrayList<String>();
		public void pointSettings(String pointduration,String pointamount,String pointsaving){
			String pointPath = "pointSettings.txt";
			try{
				File pointPath2 = new File(pointPath);
				if(!pointPath2.exists()){
					pointPath2.createNewFile();
				}
				//write file
				try{
					bw = new BufferedWriter(new FileWriter(pointPath));
					bw.write(pointduration);
					bw.newLine();
					bw.write(pointamount);
					bw.newLine();
					bw.write(pointsaving);
					bw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
					try{
						InputStream fis = new FileInputStream(pointPath);
						BufferedReader br = new BufferedReader(new InputStreamReader(fis));
						String readline;
						while((readline = br.readLine())!=null){
							thisarray.add(readline);
						}
						br.close();
						userpointduration = Integer.parseInt(thisarray.get(0));
						userpointamount = Integer.parseInt(thisarray.get(1));
						userpointstorage = Integer.parseInt(thisarray.get(2));
						handle = scheduler.scheduleAtFixedRate(pointSetText, 0, userpointduration,TimeUnit.SECONDS);
					    handle2 = scheduler.scheduleAtFixedRate(pointStorage, 0, userpointstorage,TimeUnit.SECONDS);
					}catch(IOException e){
						e.printStackTrace();
					}
			}catch(Exception e){
				JOptionPane.showMessageDialog(null,"There is some problems about the settings abount points.\nPlease go to the 'Points' tab to fix it.","Points settings error.",JOptionPane.PLAIN_MESSAGE);
			}	
		}
		public void runPointSettings(){
			try{
				String pointPath = "pointSettings.txt";
				InputStream fis = new FileInputStream(pointPath);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String readline;
				while((readline = br.readLine())!=null){
					thisarray.add(readline);
				}
				br.close();
				userpointamount = Integer.parseInt(thisarray.get(1));
				handle = scheduler.scheduleAtFixedRate(pointSetText, 0, Integer.parseInt(thisarray.get(0)),TimeUnit.SECONDS);
			    handle2 = scheduler.scheduleAtFixedRate(pointStorage, 0,Integer.parseInt(thisarray.get(2)),TimeUnit.SECONDS);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		public String returnDuration(){
			String userchannel = "";
			try{
				userchannel = thisarray.get(0);
			}catch(Exception e){
				e.printStackTrace();
			}
			return userchannel;
		}
		public String returnAmount(){
			String userchannel = "";
			try{
				userchannel = thisarray.get(1);
			}catch(Exception e){
				e.printStackTrace();
			}
			return userchannel;
		}
		public String returnSave(){
			String userchannel = "";
			try{
				userchannel = thisarray.get(2);
			}catch(Exception e){
				e.printStackTrace();
			}
			return userchannel;
		}
		//TRIGGER MESSAGE
		public boolean triggerMessage(String message){
			onMessage(channelname,ConnectTwitch.botName,"","",message);
			return containcommand;
		}
		public boolean checkAuth(String sender,String command){
			boolean auth = true;
			try{
				if(!UserGroup.grouplist.isEmpty()&&!UserGroup.userlist.isEmpty()){
					for(Entry<String,List<String>> entry : UserGroup.grouplist.entrySet()){
						if(entry.getValue().contains(command) && UserGroup.userlist.get(entry.getKey()).contains(sender)){
							auth = false;
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(!auth){
				sendMessage(channelname,"You cannot use this command...");
				appendGui("You cannot use this command...");
			}
			return auth;
		}
		//STORE SETTINGS
		public void writeCommandSettings(boolean joinname,boolean leavename,boolean error){
		String path = "commandsettings.txt";
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
				bw.write("joinname : " + joinname);
				bw.newLine();
				bw.write("leavename : " + leavename);
				bw.newLine();
				bw.write("error : " + error);
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		public boolean getCommandSettings(String regex){
			String path = "commandsettings.txt";
			File settings = new File(path);
			BufferedReader br;
			String readline;
			String gottenline = "false";
			boolean gottenbool = false;
			if(settings.exists()){
				try{
					FileInputStream fis = new FileInputStream(path);
					br = new BufferedReader(new InputStreamReader(fis));
					while((readline = br.readLine())!=null){
						if(readline.toLowerCase().contains(regex)){
							gottenline = readline.replace(regex+" : ", "");
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					}
			}
			if(gottenline.contains("true")){
				gottenbool = true;
			}else if(gottenline.contains("false")){
				gottenbool = false;
			}
			return gottenbool;
		}
		public void showNotice(String notice){
			appendGui(notice);
			sendMessage(channelname,notice);
		}
		public void showError(String error){
			appendGui(error);
			if(commanderrortrue){
				sendMessage(channelname,error);
			}
		}
}

	

