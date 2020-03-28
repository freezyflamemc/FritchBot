package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.UserGroup;
import networking.Client;
import networking.ConnectTwitch;
import java.awt.GridLayout;
import javax.swing.JList;
import net.miginfocom.swing.MigLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import java.awt.Color;

public class Guimainclass extends JFrame{
	//classes
	public ConnectTwitch ctwitch = new ConnectTwitch();
	public Client client = new Client();
	public UserGroup group = new UserGroup();
	//other variables.
	public static String userText = "",serverText = "";
	private JPanel contentPane;
	private JTextField textField;
	private boolean guiReady = false;
	//Textarea LIST
	static public JTextArea textArea;
	static public JTextArea textArea_2;
	public int haveMessage = 0;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	public static JTextField textField_8;
	public static JTextField textField_10;
	public static JList list;
	public static JCheckBox chckbxNewCheckBox,chckbxNewCheckBox_1,checkBox;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Guimainclass() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Guimainclass.class.getResource("/gui/fritchbotlogo.png")));
		setTitle("FritchBot Version 1.0.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(720,480));
		setBounds(100, 100, 720, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("     Twitch Chat     ", null, panel, null);
		JList list_2 = new JList();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1);
		JList list_1 = new JList();
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setColumns(60);
		textArea.setRows(18);
		textArea.setEditable(false);
		textField = new JTextField();
		//TEXT FIELD ENTER
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!(userText = ae.getActionCommand()).trim().isEmpty()){
					if(!client.triggerMessage(userText)){
						textArea.append(ConnectTwitch.botName+" : "+userText+"\n");
					}
					textField.setText("");
					client.sendUser(userText);
					}
				};
			}
		);
		panel.add(textField);
		textField.setColumns(62);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("     User's points     ", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow][]", "[][][][][][][]"));
		
		JLabel lblSearchFor = new JLabel("Search for:");
		panel_1.add(lblSearchFor, "cell 0 0 2 1");
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "cell 2 0 1 7,grow");
		textArea_2 = new JTextArea();
		scrollPane.setViewportView(textArea_2);
		textArea_2.setRows(18);
		textArea_2.setColumns(40);
		textArea_2.setEditable(false);
		
		JLabel lblUsername = new JLabel("Username:");
		panel_1.add(lblUsername, "cell 1 1");
		
		//SEARCH USER
		textField_8 = new JTextField();
		panel_1.add(textField_8, "cell 1 2,growx");
		textField_8.setColumns(10);
		textField_8.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent user) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(textField_8.getText()!=""){
					client.searchUser(textField_8.getText());
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(textField_8.getText()!=""){
					client.searchUser(textField_8.getText());
				}
			}
					
				}
			);
		
		JLabel lblMinimumPoints = new JLabel("Minimum points");
		panel_1.add(lblMinimumPoints, "cell 1 3");
		
		//SEARCH MINIMUM POINTS
		textField_10 = new JTextField();
		panel_1.add(textField_10, "cell 1 4,growx,aligny top");
		textField_10.setColumns(10);
		textField_10.getDocument().addDocumentListener(new DocumentListener(){


			@Override
			public void changedUpdate(DocumentEvent user) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(textField_10.getText()!=""){
					try{
						client.searchMin(Integer.parseInt(textField_10.getText()));
					}catch(Exception e){
						
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(textField_10.getText()!=""){
					try{
						client.searchMin(Integer.parseInt(textField_10.getText()));
					}catch(Exception e){
						
					}
				}
			}
					
				}
			);
		
		JLayeredPane panel_3 = new JLayeredPane();
		panel_3.setBackground(Color.LIGHT_GRAY);
		tabbedPane.addTab("        Settings        ", null, panel_3, null);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{29, 319, 100, 1, 70, 21, 18, 0};
		gbl_panel_3.rowHeights = new int[]{154, 155, 122, 0, 204, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridwidth = 5;
		gbc_panel_5.insets = new Insets(0, 0, 5, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 1;
		panel_3.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{9, 187, -15, -19, 90, 178, 139, 0};
		gbl_panel_5.rowHeights = new int[]{23, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JLabel lblNewLabel = new JLabel("Channel Name:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel_5.add(lblNewLabel, gbc_lblNewLabel);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 4;
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 0;
		panel_5.add(textField_1, gbc_textField_1);
		textField_1.setText(ctwitch.returnChannel());
		textField_1.setColumns(20);
		
		JLabel lblTwitchBotAccount = new JLabel("Bot Account Name:");
		GridBagConstraints gbc_lblTwitchBotAccount = new GridBagConstraints();
		gbc_lblTwitchBotAccount.anchor = GridBagConstraints.WEST;
		gbc_lblTwitchBotAccount.insets = new Insets(0, 0, 5, 5);
		gbc_lblTwitchBotAccount.gridx = 1;
		gbc_lblTwitchBotAccount.gridy = 1;
		panel_5.add(lblTwitchBotAccount, gbc_lblTwitchBotAccount);
		
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.gridwidth = 4;
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 2;
		gbc_textField_3.gridy = 1;
		panel_5.add(textField_3, gbc_textField_3);
		textField_3.setText(ctwitch.returnBotName());
		JLabel lblNewLabel_1 = new JLabel("OAuth:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 2;
		panel_5.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 4;
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 2;
		gbc_textField_2.gridy = 2;
		panel_5.add(textField_2, gbc_textField_2);
		textField_2.setColumns(20);
		textField_2.setText(ctwitch.returnOAuth());
		JButton btnNewButton = new JButton("Submit");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 3;
		panel_5.add(btnNewButton, gbc_btnNewButton);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 5;
		gbc_panel_6.insets = new Insets(0, 0, 5, 5);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 1;
		gbc_panel_6.gridy = 2;
		panel_3.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{5, 169, 84, 44, 0};
		gbl_panel_6.rowHeights = new int[]{30, 0, 0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JLabel lblNewLabel_2 = new JLabel("Point award duration:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 0;
		panel_6.add(lblNewLabel_2, gbc_lblNewLabel_2);
		//POINTS DURATION
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 2;
		gbc_textField_4.gridy = 0;
		panel_6.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JLabel lblSeconds = new JLabel("seconds");
		GridBagConstraints gbc_lblSeconds = new GridBagConstraints();
		gbc_lblSeconds.insets = new Insets(0, 0, 5, 0);
		gbc_lblSeconds.gridx = 3;
		gbc_lblSeconds.gridy = 0;
		panel_6.add(lblSeconds, gbc_lblSeconds);
		
		JLabel lblPointAwardAmount = new JLabel("Point award amount:");
		GridBagConstraints gbc_lblPointAwardAmount = new GridBagConstraints();
		gbc_lblPointAwardAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblPointAwardAmount.anchor = GridBagConstraints.WEST;
		gbc_lblPointAwardAmount.gridx = 1;
		gbc_lblPointAwardAmount.gridy = 1;
		panel_6.add(lblPointAwardAmount, gbc_lblPointAwardAmount);
		//POINTS AMOUNT
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 2;
		gbc_textField_5.gridy = 1;
		panel_6.add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Points saving duraion:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 2;
		panel_6.add(lblNewLabel_3, gbc_lblNewLabel_3);
		//POINTS SAVING		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pointse) {
				if(!(textField_4.getText().trim().isEmpty())||(textField_5.getText().trim().isEmpty())||(textField_6.getText().trim().isEmpty())){
					client.pointSettings(textField_4.getText(),textField_5.getText(),textField_6.getText());
					JOptionPane.showMessageDialog(null,"You have successfully changed the points settings.","User information changed.",JOptionPane.PLAIN_MESSAGE);
				}	
			}
		});
		textField_4.setText(client.returnDuration());
		textField_5.setText(client.returnAmount());
		textField_6 = new JTextField();
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 2;
		gbc_textField_6.gridy = 2;
		panel_6.add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);
		textField_6.setText(client.returnSave());
		JLabel lblSeconds_1 = new JLabel("seconds");
		GridBagConstraints gbc_lblSeconds_1 = new GridBagConstraints();
		gbc_lblSeconds_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblSeconds_1.gridx = 3;
		gbc_lblSeconds_1.gridy = 2;
		panel_6.add(lblSeconds_1, gbc_lblSeconds_1);
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.anchor = GridBagConstraints.WEST;
		gbc_btnSubmit.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmit.gridx = 2;
		gbc_btnSubmit.gridy = 3;
		panel_6.add(btnSubmit, gbc_btnSubmit);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.gridwidth = 5;
		gbc_panel_7.insets = new Insets(0, 0, 5, 5);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 1;
		gbc_panel_7.gridy = 3;
		panel_3.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_7.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		JLabel lblNewLabel_4 = new JLabel("Welcome message in chat");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 0;
		panel_7.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		chckbxNewCheckBox = new JCheckBox();
		chckbxNewCheckBox.setSelected(client.getCommandSettings("joinname"));
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 2;
		gbc_chckbxNewCheckBox.gridy = 0;
		panel_7.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		
		JLabel lblShowNameOn = new JLabel("Depart message in chat:");
		GridBagConstraints gbc_lblShowNameOn = new GridBagConstraints();
		gbc_lblShowNameOn.anchor = GridBagConstraints.WEST;
		gbc_lblShowNameOn.insets = new Insets(0, 0, 5, 5);
		gbc_lblShowNameOn.gridx = 1;
		gbc_lblShowNameOn.gridy = 1;
		panel_7.add(lblShowNameOn, gbc_lblShowNameOn);
		
		chckbxNewCheckBox_1 = new JCheckBox("");
		chckbxNewCheckBox_1.setSelected(client.getCommandSettings("leavename"));
		GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
		gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox_1.gridx = 2;
		gbc_chckbxNewCheckBox_1.gridy = 1;
		panel_7.add(chckbxNewCheckBox_1, gbc_chckbxNewCheckBox_1);
		
		JLabel lblDisplayErrorMessage = new JLabel("Display error message in chat:");
		GridBagConstraints gbc_lblDisplayErrorMessage = new GridBagConstraints();
		gbc_lblDisplayErrorMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisplayErrorMessage.gridx = 1;
		gbc_lblDisplayErrorMessage.gridy = 2;
		panel_7.add(lblDisplayErrorMessage, gbc_lblDisplayErrorMessage);
		
		checkBox = new JCheckBox("");
		checkBox.setSelected(client.getCommandSettings("error"));
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox.gridx = 2;
		gbc_checkBox.gridy = 2;
		panel_7.add(checkBox, gbc_checkBox);
		
		JButton btnSubmit_1 = new JButton("Submit");
		btnSubmit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean joinname = chckbxNewCheckBox.isSelected();;
				boolean leavename = chckbxNewCheckBox_1.isSelected();
				boolean error = checkBox.isSelected();
				client.writeCommandSettings(joinname,leavename,error);
				JOptionPane.showMessageDialog(null,"You have successfully changed the command information.","Command information changed.",JOptionPane.PLAIN_MESSAGE);
			}
		});
		GridBagConstraints gbc_btnSubmit_1 = new GridBagConstraints();
		gbc_btnSubmit_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmit_1.gridx = 2;
		gbc_btnSubmit_1.gridy = 3;
		panel_7.add(btnSubmit_1, gbc_btnSubmit_1);
		//USER CHANNEL SETTINGS
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userChannel = textField_1.getText();
				String userOAuth = textField_2.getText();
				String thisbotName = textField_3.getText();
				ctwitch.setNewUserCred(userChannel,userOAuth,thisbotName);
				JOptionPane.showMessageDialog(null,"You have successfully changed the user information.\nPlease restart the application to enact the changes.","User information changed.",JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("       Permissions      ", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{117, 235, 155, 0, 0};
		gbl_panel_2.rowHeights = new int[]{29, 0, 0, 0, 233, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		//CREATE CLASS BUTTON
		JButton btnNewButton_1 = new JButton("Create new Class");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String createclass = JOptionPane.showInputDialog(null,"Please enter the class name:","Create new class",JOptionPane.PLAIN_MESSAGE);
				group.addGroup(createclass);
				list.setListData(group.getGroup());
				group.storeGroup();
			}
		});
		//DELETE CLASS BUTTON
		JButton btnNewButton_2 = new JButton("Delete Class");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deleteclass = JOptionPane.showInputDialog(null,"Please enter the class name:","Delete class",JOptionPane.PLAIN_MESSAGE);
				group.removeGroup(deleteclass);
				list.setListData(group.getGroup());
				group.storeGroup();
			}
		});
		//ALLOWED COMMAND BUTTON
		JButton btnNewButton_3 = new JButton("Add Banned Command");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String allowcommand = JOptionPane.showInputDialog(null,"Please enter the command name:","Add new allowed command",JOptionPane.PLAIN_MESSAGE);
				String selected;
				if(list.getSelectedValue() != null && allowcommand !=null){
					selected = list.getSelectedValue().toString();
					group.addCommand(selected, allowcommand);
					String selectedvalue = list.getSelectedValue().toString();
					list_2.setListData(group.getStringList(selectedvalue));
					group.storeGroup();
				}
			}
		});
		list = new JList();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		panel_2.add(list, gbc_list);
		list.setListData(group.getGroup());
		//SET SELECTED STUFF
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent liste) {
				String selectedvalue = list.getSelectedValue().toString();
				list_2.setListData(group.getStringList(selectedvalue));
				list_1.setListData(group.getUserList(selectedvalue));
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridheight = 5;
		gbc_scrollPane_2.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_2.gridx = 1;
		gbc_scrollPane_2.gridy = 0;
		panel_2.add(scrollPane_2, gbc_scrollPane_2);
		
		scrollPane_2.setViewportView(list_1);
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_3.gridx = 3;
		gbc_btnNewButton_3.gridy = 0;
		panel_2.add(btnNewButton_3, gbc_btnNewButton_3);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 1;
		//panel_2.add(btnNewButton_2, gbc_btnNewButton_2);
		//DELETE COMMAND
		JButton btnDeleteAllowedCommand = new JButton("Delete Banned Command");
		btnDeleteAllowedCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected;
				if(list.getSelectedValue() != null&& list_2.getSelectedValue()!=null){
					String deletecommand = list_2.getSelectedValue().toString();
					selected = list.getSelectedValue().toString();
					group.removeCommand(selected, deletecommand);
					String selectedvalue = list.getSelectedValue().toString();
					list_2.setListData(group.getStringList(selectedvalue));
					group.storeGroup();
				}
			}
		});
		GridBagConstraints gbc_btnDeleteAllowedCommand = new GridBagConstraints();
		gbc_btnDeleteAllowedCommand.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteAllowedCommand.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteAllowedCommand.anchor = GridBagConstraints.NORTH;
		gbc_btnDeleteAllowedCommand.gridx = 3;
		gbc_btnDeleteAllowedCommand.gridy = 1;
		panel_2.add(btnDeleteAllowedCommand, gbc_btnDeleteAllowedCommand);
		//ADD USER
		JButton btnNewButton_4 = new JButton("Add User");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String allowcommand = JOptionPane.showInputDialog(null,"Please enter the user name:","Add user",JOptionPane.PLAIN_MESSAGE);
				String selected;
				if(list.getSelectedValue() != null && allowcommand !=null){
					selected = list.getSelectedValue().toString();
					group.addUser(selected, allowcommand);
					String selectedvalue = list.getSelectedValue().toString();
					list_1.setListData(group.getUserList(selectedvalue));
					group.storeGroup();
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_4.gridx = 3;
		gbc_btnNewButton_4.gridy = 2;
		panel_2.add(btnNewButton_4, gbc_btnNewButton_4);
		
		GridBagConstraints gbc_list_2 = new GridBagConstraints();
		gbc_list_2.gridheight = 5;
		gbc_list_2.insets = new Insets(0, 0, 0, 5);
		gbc_list_2.fill = GridBagConstraints.BOTH;
		gbc_list_2.gridx = 2;
		gbc_list_2.gridy = 0;
		panel_2.add(list_2, gbc_list_2);
		//DELETE USER
		JButton btnNewButton_5 = new JButton("Delete User");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selected;
				if(list.getSelectedValue() != null && list_1.getSelectedValue() !=null){
					selected = list.getSelectedValue().toString();
					String deletecommand = list_1.getSelectedValue().toString();
					group.removeUser(selected, deletecommand);
					list_1.setListData(group.getUserList(selected));
					group.storeGroup();
			}
		}});
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_5.gridx = 3;
		gbc_btnNewButton_5.gridy = 3;
		panel_2.add(btnNewButton_5, gbc_btnNewButton_5);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("          Help          ", null, panel_4, null);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrFritchbotMade = new JTextArea();
		txtrFritchbotMade.setText("FritchBot 1.0.0 Made by Freezyflame in Dec 2016.\r\nFacebook : www.facebook.com/freezyflameyt\r\nGithub : https://github.com/freezyflamemc\r\nFeel free to contact me if you found any bugs or have any suggestions.\r\n-------------------------------------------------------------------------------\r\nCommands:\r\n!points : show how many points a user have.\r\n!addpoints <user> <amount>: add a number of points to a user.\r\n!setpoints <user> <amount>: set a user's points to a desired amount.\r\n!luckydraw : Randomly selects a person in the chat.\r\n!pointsdraw : Draw a person in the chat based on the amount of points they have.\r\n!gamble <amount> : 50% chance to double your coins that you gambled.\r\n!fstartraffle <seconds> : Start a raffle allowing people to join.\r\n!fstopraffle : Stop the raffle and list all the participants.\r\n!raffle : (For viewers)Join a started raffle.\r\n!fraffledraw : Draw a random person from the people who entered the raffle.\r\n-------------------------------------------------------------------------------\r\nKnown Bugs:\r\n-Search function in 'Users' tab doesn't function well when both the fields are entered.\r\n-'Chat' tab sometimes show duplicate of messages.\r\n-Slow connection time to Twitch.\r\n-Needs a 'Reset' button for all files.");
		panel_4.add(txtrFritchbotMade, BorderLayout.CENTER);
		txtrFritchbotMade.setEditable(false);
		
		guiReady = true;
		//INIT SERVER
		channelReady();
	}
	public void channelReady(){
		if(client.clientReady && guiReady){
			client.appendGui("Connected to channel: "+client.channelname+" on "+client.currentTime);
		}
	}
}