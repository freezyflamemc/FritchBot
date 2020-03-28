package main;
import javax.swing.JFrame;

import gui.Guimainclass;
public class Main {
	public static void main(String[] args){
		Guimainclass gui = new Guimainclass();
		//configure gui
		gui.setSize(720,480);
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("fritchbotlogo.png")));
		Thread t1 = new Thread(gui.client.pointStorage);
		Runtime.getRuntime().addShutdownHook(t1);
	}
}
