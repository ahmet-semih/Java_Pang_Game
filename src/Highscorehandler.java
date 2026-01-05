import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Highscorehandler extends JFrame implements ActionListener {
	
	HashMap<String,Integer> username_score_map = new HashMap<>();
	List<Integer> userscores = new ArrayList<>();
	JTextArea result;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		File folder = new File("Users/");
		File[] files = folder.listFiles();
		
		
		
		if (files == null || files.length == 0) {
		    JOptionPane.showMessageDialog(null, "No user files found in Users/ directory.");
		    return;
		}
		
		
		
		for(File file : files) {
			
			int novicescore=0;
			int intermediatescore=0;
			int advancedscore=0;
			
			try(BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				String line;
				boolean isfirstline = true;
				
				while ((line = br.readLine()) != null) {
					
					if (isfirstline) {
						
						isfirstline = false; 
				        continue;
				    }
					
					
					
					String[] parts = line.split(",");
					String level = parts[2];
					
					if(level.equals("Novice") && Integer.parseInt(parts[3]) >novicescore) {
						novicescore=Integer.parseInt(parts[3]);
					}
					if(level.equals("Intermediate") && Integer.parseInt(parts[3])>intermediatescore) {
						intermediatescore=Integer.parseInt(parts[3]);
					}
					if(level.equals("Advanced") && Integer.parseInt(parts[3])>advancedscore) {
						advancedscore=Integer.parseInt(parts[3]);
					}
				}
				
				int total_score = novicescore+intermediatescore+advancedscore;
				
				 String filename = file.getName();  
	             String username = filename.split("\\.")[0];
				
				username_score_map.put(username,total_score);
				
			}
			catch(Exception ee) {
				System.out.println(ee);
			}
		}
		
		
		for(int score : username_score_map.values()) {
			userscores.add(score);
		}
		
		
		
		Collections.sort(userscores);
		Collections.reverse(userscores);
		
		
		result=new JTextArea(10,30);
		result.setEditable(false);
		result.setFont(new Font("Monospaced",Font.PLAIN, 12));
		
		List<String> processed_names = new ArrayList<>();
		
		result.append(String.format("%-3s %-20s %11s\n","#","Username","Score"));
		result.append("===========================================\n");
		
		int index=0;
		while(index < userscores.size() && index <= 9) {
			
			for(String a : username_score_map.keySet()) {
				
				if(username_score_map.get(a).equals(userscores.get(index)) && !processed_names.contains(a)) {
					String line = String.format("%-3d %-20s %10d", index + 1, a, userscores.get(index));
		            result.append(line + "\n");
					processed_names.add(a);
					break;
				}
			}
			index++;
		}
		
		
        add(result); 
        setTitle("High Scores");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        revalidate();
        repaint();
		
	}

}
