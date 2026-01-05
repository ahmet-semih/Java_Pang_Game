import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class Helphandler implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		String info = "Name: Ahmet Semih\n"
                + "Surname: Gümüş\n"
                + "Student No: 20220702049\n"
                + "Email: ahmetsemih.gumus@std.yeditepe.edu.tr";
		
		JOptionPane.showMessageDialog(null, info);
		
	}

}
