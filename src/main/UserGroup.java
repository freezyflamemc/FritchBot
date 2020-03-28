package main;
import networking.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JList;
import javax.swing.JOptionPane;

import gui.*;

public class UserGroup {
	public static Map<String,List<String>> grouplist;
	public static Map<String,List<String>> userlist;
	String path = "UserGroup.txt";
	//import classes
	public UserGroup(){
		grouplist = new HashMap<String,List<String>>();
		userlist = new HashMap<String,List<String>>();
		readGroup();
		getGroup();
	}
	public void readGroup(){
		String readline;
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
					String[] readarray = readline.split("/",-1);
						if(readarray.length==3){
							if(!readarray[0].isEmpty()){
							try{
								String[] herearray = readarray[1].split(",");
								LinkedList<String> templist = new LinkedList<String>(Arrays.asList(herearray));
								templist.removeAll(Collections.singleton(""));
								String[] herearray2 = readarray[2].split(",");
								LinkedList<String> templist2 = new LinkedList<String>(Arrays.asList(herearray2));
								templist2.removeAll(Collections.singleton(""));
								grouplist.put(readarray[0], templist);
								userlist.put(readarray[0], templist2);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
				br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public String[] getGroup(){
		String[] getlist;
		List<String> herelist = new ArrayList<String>();
		for(Entry<String, List<String>> entry:grouplist.entrySet()){
			herelist.add(entry.getKey());
		}
		getlist = herelist.toArray(new String[herelist.size()]);
		return getlist;
	}
	public String[] getStringList(String sentvalue){
			List<String> herelist = grouplist.get(sentvalue);
			String[] stringlist = herelist.toArray(new String[herelist.size()]);
			return stringlist;
	}
	public String[] getUserList(String sentvalue){
		List<String> herelist = userlist.get(sentvalue);
		String[] stringlist = herelist.toArray(new String[herelist.size()]);
		return stringlist;
}
	public void addGroup(String added){
		try{	
			if(!added.contains("/")||!added.contains(",")){
				grouplist.put(added,new ArrayList<String>());
			}else{
				throw new IOException();
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"Error adding group.","Error adding group.",JOptionPane.PLAIN_MESSAGE);
		}
	}
	public void removeGroup(String added){
		if(grouplist.get(added) != null){
			if(grouplist.size()>2){
				grouplist.remove(added);
			}else{
				JOptionPane.showMessageDialog(null,"You need to have at least two groups.","Error deleting group.",JOptionPane.PLAIN_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null,"Group does not exist.","Error deleting group.",JOptionPane.PLAIN_MESSAGE);
		}
	}
	//ADD COMMAND
	public void addCommand(String selected,String added){
		if(grouplist.get(selected)!=null&&Arrays.asList(Client.commandlist).contains(added.toLowerCase())){
			if(!grouplist.get(selected).contains(added.trim())){
				grouplist.get(selected).add(added);
			}else{
				JOptionPane.showMessageDialog(null,"Command already in the list.","Error adding command.",JOptionPane.PLAIN_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null,"Command does not exist , please refer to the command list.","Error adding command.",JOptionPane.PLAIN_MESSAGE);
		}
	}
	//DELETE COMMAND
	public void removeCommand(String selected,String added){
		if(grouplist.get(selected)!=null&&grouplist.get(selected).contains(added)){
			grouplist.get(selected).remove(added);
		}else{
			JOptionPane.showMessageDialog(null,"Command does not exist in the list.","Error adding command.",JOptionPane.PLAIN_MESSAGE);
		}
	}
	public void storeGroup(){
		try{
			BufferedWriter bw;
			File userpoints = new File(path);
			if(!userpoints.exists()){			
					userpoints.createNewFile();
			}else{
				try{
					bw = new BufferedWriter(new FileWriter(path));
    	    		if(!grouplist.isEmpty()){
    					for(Entry<String,List<String>> entry:grouplist.entrySet()){
    						bw.write(entry.getKey()+"/");
    						for(int i=0;i<entry.getValue().size();i++){
    							if(i==0){
    								bw.write(entry.getValue().get(i));
    							}else{
    								bw.write(","+entry.getValue().get(i));
    							}
    						}
    						bw.write("/");
            	    		if(!userlist.isEmpty()){
            					for(int i=0;i<userlist.get(entry.getKey()).size();i++){
            						if(i==0){
        								bw.write(userlist.get(entry.getKey()).get(i));
        							}else{
        								bw.write(","+userlist.get(entry.getKey()).get(i));
        							}
            					}
            					bw.newLine();
            	    		}	
    					}				
    	    		bw.close();
				}
				}catch(Exception e){
					e.printStackTrace();
				}	
	    
			}
			}catch(Exception e){
				e.printStackTrace();
		}	    		
	}
	public void addUser(String selected,String added){
		if(userlist.get(selected)!=null){
			if(!userlist.get(selected).contains(added)){
				userlist.get(selected).add(added);
			}else{
				JOptionPane.showMessageDialog(null,"User already in the list.","Error adding user.",JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	public void removeUser(String selected,String added){
		if(userlist.get(selected)!=null&&userlist.get(selected).contains(added)){
			userlist.get(selected).remove(added);
		}else{
			JOptionPane.showMessageDialog(null,"User does not exist in the list.","Error adding user.",JOptionPane.PLAIN_MESSAGE);
		}
	}
}
