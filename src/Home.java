import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

public class Home extends JFrame{
	
	private String gamelevel;
	JRadioButtonMenuItem novice ;
    JRadioButtonMenuItem intermediate ;
    JRadioButtonMenuItem advanced ;
	
	public Home() {
		super("Pang");
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu gamemenu = new JMenu("Game");
		JMenuItem registerItem = new JMenuItem("Register");
		JMenuItem newGameItem = new JMenuItem("New");
		JMenuItem quitItem = new JMenuItem("Quit");
		
		JMenu optionsmenu = new JMenu("Options");
		JMenuItem historyItem = new JMenuItem("History");
		JMenuItem highScoreItem = new JMenuItem("High Score");
		
		JMenu levels = new JMenu("Level");
		novice = new JRadioButtonMenuItem("Novice");
        intermediate = new JRadioButtonMenuItem("Intermediate");
        advanced = new JRadioButtonMenuItem("Advanced");
        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.add(novice);
        levelGroup.add(intermediate);
        levelGroup.add(advanced);
        
        gamemenu.add(registerItem);
        gamemenu.add(newGameItem);
        gamemenu.add(quitItem);
        
        levels.add(novice);
        levels.add(intermediate);
        levels.add(advanced);
        
        optionsmenu.add(historyItem);
        optionsmenu.add(highScoreItem);
        optionsmenu.add(levels);
		
		JMenu helpmenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		helpmenu.add(about);

		menubar.add(gamemenu);
	    menubar.add(optionsmenu);
	    menubar.add(helpmenu);
	    
	    setJMenuBar(menubar);
	    
	    Registerhandler registerhandler = new Registerhandler();
	    Newhandler newhandler = new Newhandler();
	    Helphandler hph = new Helphandler();
	    Gamelevelhandler gl = new Gamelevelhandler();
	    Historyhandler hl = new Historyhandler();
	    Highscorehandler hsh = new Highscorehandler();
	    
	    about.addActionListener(hph);
	    registerItem.addActionListener(registerhandler);
	    newGameItem.addActionListener(newhandler);
	    novice.addActionListener(gl);
	    intermediate.addActionListener(gl);
	    advanced.addActionListener(gl);
	    historyItem.addActionListener(hl);
	    highScoreItem.addActionListener(hsh);
	    
	    QuitHandler qh = new QuitHandler();
	    quitItem.addActionListener(qh);
	    
	    
	    

	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(null); 
	    mainPanel.setBackground(Color.BLACK);


	    ImageIcon originalIcon = new ImageIcon(getClass().getResource("Playermenu.png"));
	    Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon = new ImageIcon(scaledImage);

	    JLabel imageLabel = new JLabel(scaledIcon);
	    imageLabel.setBounds(400, 50, 400, 400); 
	    mainPanel.add(imageLabel);

	    JLabel instructions1 = new JLabel("-İf you are New user: Game > Register, then Options > Level, then Game > New");
	    instructions1.setBounds(250, 470, 800, 30);
	    instructions1.setForeground(Color.WHITE);
	    instructions1.setFont(new Font("SansSerif", Font.PLAIN, 16));
	    mainPanel.add(instructions1);

	    JLabel instructions2 = new JLabel("-İf you are Registered user: Options > Level, then Game > New");
	    instructions2.setBounds(250, 500, 700, 30);
	    instructions2.setFont(new Font("SansSerif", Font.PLAIN, 16));
	    instructions2.setForeground(Color.WHITE);
	    mainPanel.add(instructions2);

	    this.setContentPane(mainPanel);
	    this.setSize(1200, 700);
	    this.setLocationRelativeTo(null); 
	    this.setVisible(true);
	}
	
	public class QuitHandler implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        System.exit(0); 
	    }
	}
	
	public class Gamelevelhandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==novice) {
				gamelevel = "Novice";
			}
			else if (e.getSource()==intermediate) {
				gamelevel = "Intermediate";
			}
			else if (e.getSource()==advanced) {
				gamelevel ="Advanced"; 
			}	
		}
	}
	
	public class Newhandler implements ActionListener {

	    public void actionPerformed(ActionEvent e) {
	        JTextField nameField = new JTextField();
	        Object[] message = { "Enter your name:", nameField };

	        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);

	        if (option == JOptionPane.OK_OPTION) {
	            String enteredName = nameField.getText();
	            boolean found = false;

	            try (BufferedReader reader = new BufferedReader(new FileReader("usersinfo.csv"))) {
	                String line;
	                reader.readLine(); 
	                while ((line = reader.readLine()) != null) {
	                    String[] parts = line.split(",");
	                    if (parts.length > 0 && parts[0].equalsIgnoreCase(enteredName)) {
	                        found = true;
	                        break;
	                    }
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }

	            if (!found) {
	                JOptionPane.showMessageDialog(null, "User not found. Please register.");
	                new Registerhandler().actionPerformed(e);
	            } else {
	            	switch(gamelevel) {
	            		case "Novice":
	            			Novice_thread panel = new Novice_thread(enteredName);
            			    Home.this.setContentPane(panel);   
            			    Home.this.revalidate();            
            			    Home.this.repaint();               
            			    Home.this.pack();
	            			
	            			break;
	            		case "Intermediate":
	            			Intermediate_thread panel2 = new Intermediate_thread(enteredName);
            			    Home.this.setContentPane(panel2);   
            			    Home.this.revalidate();            
            			    Home.this.repaint();               
            			    Home.this.pack();
	            			
	            			break;
	            		case "Advanced":
	            			Advanced_thread panel3= new Advanced_thread(enteredName);
            			    Home.this.setContentPane(panel3);   
            			    Home.this.revalidate();            
            			    Home.this.repaint();               
            			    Home.this.pack();
            			    
	            			break;
	            	}
	            			
	            }
	        }
	    }
	}
	
	
	
}
