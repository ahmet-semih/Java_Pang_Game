import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Balloon {
	ImageIcon balloon_image;
	int diameter;
	int x;
	int y;
	int velocity_x;
	int velocity_y;
	int max_height;
	int gravity=2;
	
	
    public Balloon(ImageIcon balloon_image,int diameter,int x, int y, int velocity_x, int velocity_y, int max_height) {
        this.diameter=diameter;
    	this.balloon_image=balloon_image;
    	this.x = x;
        this.y = y;
        this.velocity_x = velocity_x;
        this.velocity_y = velocity_y;
        this.max_height = max_height;
    }
	
	void move_balloon(){
		x+=velocity_x;
		y+=velocity_y;
		
		velocity_y+=gravity;
		
		if(y+diameter*5>=569) {
			y = 569 - diameter * 5;
			velocity_y = -velocity_y;
		}
		if(y<=max_height) {
			 y = max_height;
	        if (velocity_y < 0) {      
	            velocity_y = 0;
	        }
		}
		if(x <=15 || x + diameter * 5  >=1150) {
			velocity_x=-velocity_x;
		};
	}
}
