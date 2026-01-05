import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Registerhandler implements ActionListener{
	
	JTextField username ;
	JTextField psw ; 
	JDialog dialog;  
	
	public void actionPerformed(ActionEvent event) {
		username = new JTextField(10);
		psw = new JTextField(10); 
		JButton login = new JButton("Login"); 
		JButton help = new JButton("Help");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(240, 150));
		
		JPanel userpanel = new JPanel(); 
        userpanel.add(new JLabel("Name:"));
        userpanel.add(username);


        JPanel passpanel = new JPanel(); 
        passpanel.add(new JLabel("Password:"));
        passpanel.add(psw);

        
        JPanel buttonpanel = new JPanel();
        buttonpanel.add(login);
        buttonpanel.add(help);
        
        panel.add(userpanel);
        panel.add(passpanel);
        panel.add(buttonpanel);
        
        JOptionPane optionpane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        dialog = optionpane.createDialog("Register");
        
        Loginhandler tfh = new Loginhandler();
        login.addActionListener(tfh);
        
        Helphandler hph = new Helphandler();
        help.addActionListener(hph);
        

        dialog.setVisible(true);
        	
	}
	
	public class Loginhandler implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			String name = username.getText();
            String password = psw.getText();
            File file = new File("usersinfo.csv");
            boolean isNewFile = !file.exists();

            try (FileWriter writer = new FileWriter("usersinfo.csv", true)) {
            	if (isNewFile) {
                    writer.write("name,password\n"); 
                }
            	
            	writer.write(name + "," + password +"\n");
            	
            	
            	File userDir = new File("Users");
            	if (!userDir.exists()) {
            	    userDir.mkdirs();
            	}
            	
            	File user_file = new File("Users/" + name + ".csv");
            	try (FileWriter userWriter = new FileWriter(user_file)) {
                    userWriter.write("date,time,level,score\n");
                }
            	
                JOptionPane.showMessageDialog(null, "User registered");
                dialog.dispose();
            } 
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing to file.");
                e.printStackTrace();
            }
            
		}
	}
}
