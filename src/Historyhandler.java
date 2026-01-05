import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Historyhandler implements ActionListener{
	private JTextField userinfo;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		JPanel lblPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lbl = new JLabel("Name:");
		lblPanel.add(lbl);
		main.add(lblPanel);
        userinfo = new JTextField(8);
        main.add(userinfo);
        

        JButton infoButton = new JButton("Get info");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(infoButton);
        main.add(buttonPanel);

        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        main.add(resultArea);

        infoButton.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                String name = userinfo.getText();
                
                try (BufferedReader br = new BufferedReader(new FileReader("Users/" + name + ".csv"))) {
                	
                    String line;
                    String allData = "";
                    
                    boolean firstline = true;
                    
                    while ((line = br.readLine()) != null) {
                    	
                    	String[] parts = line.split(",");

                    	if (firstline) {
                            allData+=(String.format("%-12s                   %-10s      %-10s           %-10s\n", parts[0], parts[1], parts[2], parts[3]));
                            firstline = false;
                        } else {
                            allData+=(String.format("%-12s     %-10s    %-10s          %-10s\n", parts[0], parts[1], parts[2], parts[3]));
                        }
                    	
                    }
                    resultArea.setText(allData);
                } 
                catch (Exception ex) {
                	resultArea.setText("error");
                }
            }
        	
        });
        JOptionPane.showMessageDialog(null, main, "Enter Username For History", JOptionPane.PLAIN_MESSAGE);
		
	}
	
	

}
