import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Advanced_thread extends JPanel{
	
	private Thread playerThread, gameThread;
	private volatile boolean playerThreadRunning = true;
	private volatile boolean gameThreadRunning = true;
	
	private boolean showingRestartMessage = false;
	private int countdown = 3;
	
	private String username;
	private int userscore=0;
	
	private int userlife =7;
	
	private int timeLeft = 120;

    private int characterX;
    private int characterY = 483;

    private ImageIcon background;

    private List<ImageIcon> rights = new ArrayList<>();
    private List<ImageIcon> lefts = new ArrayList<>();
    private List<ImageIcon> baloons = new ArrayList<>();
    private List<BufferedImage> arrows = new ArrayList<>();
    private List<Balloon> current_baloons = new ArrayList<>();
    

    private ImageIcon playerstop;
    private ImageIcon playershooting;
    private ImageIcon currentImage;

    
    private int frameIndex = 0;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean shooting = false;
    
    int curx;
    int cury;
    int arrowFrameIndex;
    private BufferedImage cur_arrow;
    
    ImageIcon biggest_balloon = new ImageIcon(getClass().getResource("/Baloons/baloons1.png"));
    ImageIcon biggest_balloon2 = new ImageIcon(getClass().getResource("/Baloons/baloons2.png"));
    ImageIcon biggest_balloon3 = new ImageIcon(getClass().getResource("/Baloons/baloons3.png"));
    ImageIcon biggest_balloon4 = new ImageIcon(getClass().getResource("/Baloons/baloons4.png"));
    
    ImageIcon life_icon = new ImageIcon(getClass().getResource("Playerlife.png"));
    
    int arrowlogic = 68;
    private boolean gameFinished = false;
    
    private Timer timeTimer;
    
    
    private Rectangle left = new Rectangle(225,228,101,21);
    private Rectangle middleup = new Rectangle(552,228,101,21);
    private Rectangle right = new Rectangle(875,228,101,21);
    private Rectangle middledown = new Rectangle(552,363,101,21);
    private List<Rectangle> blockslist = new ArrayList<>();
    
    
    
    
    public Advanced_thread(String username) {
    	this.username=username;
    	
    	blockslist.add(left);
    	blockslist.add(middleup);
    	blockslist.add(right);
    	blockslist.add(middledown);
    	
        rights.add(new ImageIcon(getClass().getResource("/Player_right/Player.png")));
        rights.add(new ImageIcon(getClass().getResource("/Player_right/Player copy.png")));
        rights.add(new ImageIcon(getClass().getResource("/Player_right/Player copy 2.png")));
        rights.add(new ImageIcon(getClass().getResource("/Player_right/Player copy 3.png")));
        rights.add(new ImageIcon(getClass().getResource("/Player_right/Player copy 4.png")));
        

        lefts.add(new ImageIcon(getClass().getResource("/Player_left/Player copy 5.png")));
        lefts.add(new ImageIcon(getClass().getResource("/Player_left/Player copy 6.png")));
        lefts.add(new ImageIcon(getClass().getResource("/Player_left/Player copy 7.png")));
        lefts.add(new ImageIcon(getClass().getResource("/Player_left/Player copy 8.png")));
        lefts.add(new ImageIcon(getClass().getResource("/Player_left/Player copy 9.png")));
        
        baloons.add(new ImageIcon(getClass().getResource("/Baloons/baloons1.png")));
        baloons.add(new ImageIcon(getClass().getResource("/Baloons/baloons2.png")));
        baloons.add(new ImageIcon(getClass().getResource("/Baloons/baloons3.png")));
        baloons.add(new ImageIcon(getClass().getResource("/Baloons/baloons4.png")));
        
        background = new ImageIcon(getClass().getResource("Advanced_Backgrounds.png"));
        
        playerstop = new ImageIcon(getClass().getResource("Playerstop.png"));
        playershooting = new ImageIcon(getClass().getResource("Playershooting.png"));
        currentImage = playerstop;
           
        current_baloons.add(new Balloon(biggest_balloon,22,60,60,4,4,50));
        current_baloons.add(new Balloon(biggest_balloon,22,800,60,4,4,50));
        
        try {
            BufferedImage[] fullImages = {
                ImageIO.read(Novice_thread.class.getResource("/Arrows/Arrows1.png")),
                ImageIO.read(Novice_thread.class.getResource("/Arrows/Arrows2.png")),
                ImageIO.read(Novice_thread.class.getResource("/Arrows/Arrows3.png"))
                
            };
            

            int[] startHeights = {33, 86, 138};
            int[] lastheights = {83,135,189};
            double step; 

            for (int imgIndex = 0; imgIndex < 3; imgIndex++) {
            	
            	step = 50.0 / (22+imgIndex); 
            	
                BufferedImage fullImage = fullImages[imgIndex];

                for (int i = 0; i < 23; i++) {
                    int width = 9;
                    int height = (int)Math.round(startHeights[imgIndex] + i * step);
                    int x = 1 + i * 17;
                    int y = lastheights[imgIndex] - height;

                    if (i == 22) {
                    	y=0;
                        x = fullImage.getWidth() - width;
                    }

                    BufferedImage arrow = fullImage.getSubimage(x, y, width, height);
                    arrows.add(arrow);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading arrows: " + e.getMessage());
        }
        
        setPreferredSize(new Dimension(1200, 700));
        characterX = (getPreferredSize().width - 86) / 2;

        setFocusable(true);
        addKeyListener(new Character_movement());
        
        startPlayerThread();
        startGameThread();
        
        
        timeTimer = new Timer(1000, new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft <= 0) {
                	timeTimer.stop();
                    game_over("Time's up");
                }
                repaint(); 
            }
        });
        timeTimer.start();  
    }
    
    private void startPlayerThread() {
        playerThread = new Thread(() -> {
            while (playerThreadRunning) {
                if (movingRight) {
                    if (characterX + 25 <= 1130) characterX += 27;
                    frameIndex = (frameIndex + 1) % rights.size();
                    currentImage = rights.get(frameIndex);
                    
                } 
                else if (movingLeft) {
                    if (characterX - 25 >= 5) characterX -= 27;
                    frameIndex = (frameIndex + 1) % lefts.size();
                    currentImage = lefts.get(frameIndex);
                }
                
                
                SwingUtilities.invokeLater(this::repaint);
                try { 
                	Thread.sleep(40); 
                } 
                catch (InterruptedException e) {
                	break; 
                }
            }
        });
        playerThread.start();
    }
    
    public void finish_game_succesful() {
    	timeTimer.stop();
    	record_user_score_to_csv();
    	
    	int finalscore = (userscore)*3 + userlife*14 + timeLeft*8;
    	
        JOptionPane.showMessageDialog(this,"Congratulations, " + username + "!\nYour score from this level is: " + finalscore,"Game Completed!",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void record_user_score_to_csv() {
    	
    	 LocalDateTime cur_date = LocalDateTime.now();
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	     String formattedDate = cur_date.format(formatter);
	     
	     int finalscore = (userscore)*3 + userlife*14 + timeLeft*8;

	     try (FileWriter write_user_score = new FileWriter("Users/" + username + ".csv", true)) {
    	        write_user_score.write(formattedDate + "," + (100 - timeLeft) + "," + "Advanced" + "," + finalscore + "\n");
    	 } catch(Exception e) {
    	        System.out.println(e);
    	 }
    	
    } 
        
    private void startGameThread() {
    	
    	gameThread = new Thread(() -> {
    		while (gameThreadRunning) {
                
                if (!gameFinished && current_baloons.isEmpty()) {
                    gameFinished = true;
                    
                    stopThreads();
                    
                    finish_game_succesful();
                }
                if(shooting) {
                	
                	if (arrowFrameIndex < arrows.size()){
                    	
                        cur_arrow = arrows.get(arrowFrameIndex);
                        
                        Rectangle cur_arrow_rect = new Rectangle(curx + 35,characterY - (cur_arrow.getHeight() * 3) + 86,12,cur_arrow.getHeight() * 3);
                        boolean arrow_block_collision=false;
                        
                        for(Rectangle blockk : blockslist) {
                        	if(cur_arrow_rect.intersects(blockk)) {
                                arrowlogic = 68;
                                shooting = false;
                                currentImage = playerstop;
                                arrow_block_collision=true;
                            } 
                        	
                        }
                        
                        if(!arrow_block_collision){
                        	arrowFrameIndex += 1;
                            
                            arrowlogic=arrowFrameIndex-1;
                            
                            handle_arrow_balloon_collision();
                        }   
                    } 
                    else {
                    	arrowlogic = 68;
                        shooting = false;
                        currentImage = playerstop;
                    }
                }

                boolean collisionOccurred = false;

                for (Balloon b : current_baloons) {
                	
                    b.move_balloon();
                    Rectangle balloonRect = new Rectangle(b.x, b.y, b.diameter * 5, b.diameter * 5);
                    
                    for(Rectangle rg : blockslist) {
                    	if(rg.intersects(balloonRect)) {
                    		handle_block_balloon_colision(b,rg);
                    		break;
                    	}
                    }
                    
                    
                    Rectangle charRect = new Rectangle(characterX, characterY, 60, 65);
                    
                    if (charRect.intersects(balloonRect)) {
                        collisionOccurred = true;
                    }
                }

                if (collisionOccurred) {
                    userlife -= 1;
                    
                    if (userlife <= -0) {
                        game_over("You lost all your lives.");
                        stopThreads();
                    } 
                    else {
                    	stopThreads();
                        
                        start_new_game(); 
                    }
                }
                
                SwingUtilities.invokeLater(this::repaint);
                try { 
                	Thread.sleep(30); 
                } 
                catch (InterruptedException e) {
                	break; 
                }
                
    		}    
    		
    	});
    	
    	gameThread.start();
    }
    
    private void handle_block_balloon_colision(Balloon b,Rectangle r) {
    	
    	Rectangle balloonRect = new Rectangle(b.x, b.y, b.diameter * 5, b.diameter * 5);
        
    	int balloonRight = balloonRect.x + balloonRect.width;
        int balloonBottom = balloonRect.y + balloonRect.height;

        int recRight = r.x + r.width;
        int recBottom = r.y + r.height;

        int overlapLeft = Math.abs(balloonRight - r.x);
        int overlapRight = Math.abs(recRight - balloonRect.x);
        int overlapTop = Math.abs(balloonBottom - r.y);
        int overlapBottom = Math.abs(recBottom - balloonRect.y);

        int minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (minOverlap == overlapLeft) {
        	
            b.velocity_x *= -1;
            b.x = r.x - balloonRect.width; 
        } 
        else if (minOverlap == overlapRight) {
        	
            b.velocity_x *= -1;
            b.x = recRight; 
        } 
        else if (minOverlap == overlapTop) {
        	
            b.velocity_y *= -1;
            b.y = r.y - balloonRect.height; 
        } 
        else { 
            b.velocity_y *= -1;
            b.y = recBottom; 
        }
    
    }
    
    private void stopThreads() {
        playerThreadRunning = false;
        gameThreadRunning = false;

        if (playerThread != null && playerThread.isAlive()) {
            playerThread.interrupt();
        }
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
    }
    
    public void handle_arrow_balloon_collision() {
        Rectangle cur_arrow_rectangle = new Rectangle(curx + 35,characterY - (cur_arrow.getHeight() * 3) + 86,12,cur_arrow.getHeight() * 3);

        boolean hit = false;
        List<Balloon> toAdd = new ArrayList<>();
        List<Balloon> toRemove = new ArrayList<>();

        for (Balloon curbln : current_baloons) {
            Rectangle curblnrect = new Rectangle(curbln.x,curbln.y,curbln.diameter * 5,curbln.diameter * 5);

            if (curblnrect.intersects(cur_arrow_rectangle)) {
            	
                hit = true;
                toRemove.add(curbln);

                if (curbln.diameter == 22){
                	userscore+=50;
                    toAdd.add(new Balloon(biggest_balloon2, 15, curbln.x - 5, curbln.y + 10, -4, 4, curbln.y));
                    toAdd.add(new Balloon(biggest_balloon2, 15, curbln.x + 5, curbln.y + 10, 4, 4, curbln.y));
                } 
                else if (curbln.diameter == 15){
                	userscore+=100;
                    toAdd.add(new Balloon(biggest_balloon3, 8, curbln.x - 5, curbln.y + 10, -4, 4, curbln.y));
                    toAdd.add(new Balloon(biggest_balloon3, 8, curbln.x + 5, curbln.y + 10, 4, 4, curbln.y));
                } 
                else if (curbln.diameter == 8){
                	userscore+=200;
                    toAdd.add(new Balloon(biggest_balloon4, 4, curbln.x - 5, curbln.y + 10, -4, 4, curbln.y));
                    toAdd.add(new Balloon(biggest_balloon4, 4, curbln.x + 5, curbln.y + 10, 4, 4, curbln.y));
                }
                else if (curbln.diameter == 4){
                	userscore += 300;
                }
            }
        }

        current_baloons.removeAll(toRemove);
        current_baloons.addAll(toAdd);

        if (hit) {
            arrowlogic = 68;
            shooting = false;
            currentImage = playerstop;
        }

        repaint();
    }
    
    public void start_new_game() {
    	
        showingRestartMessage = true;
        countdown = 3;
        repaint();

        Timer countdownTimer = new Timer(1000, null); // 1 second 
        countdownTimer.addActionListener(e -> {
            countdown--;
            repaint();

            if (countdown == 0) {
                countdownTimer.stop();
                showingRestartMessage = false;

                // Reset character position
                characterX = (getPreferredSize().width - 86) / 2;
                currentImage = playerstop;

                // Reset arrow state
                shooting = false;
                arrowlogic = 68;

                // Reset timer
                timeLeft = 100;
                
                userscore=0;

                // Reset balloons
                current_baloons.clear();
                current_baloons.add(new Balloon(biggest_balloon, 22, 60, 60, 4, 4, 50));
                current_baloons.add(new Balloon(biggest_balloon,22,800,60,4,4,50));
                
                playerThreadRunning = true;
                gameThreadRunning = true;
                startPlayerThread();
                startGameThread(); 

                repaint();
            }
        });

        countdownTimer.start();
    }
    
    public void game_over(String reason) {
    	record_user_score_to_csv();
    	timeTimer.stop();
        JOptionPane.showMessageDialog(this,"Game Over due to: " + reason + "\nYour score is: " + userscore,"Game Over",JOptionPane.WARNING_MESSAGE);
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        
        
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        
        g.drawImage(currentImage.getImage(), characterX, characterY, 86, 86, this);
        
        for(Balloon b: current_baloons) {
			g.drawImage(b.balloon_image.getImage(),b.x,b.y,(b.diameter)*5,(b.diameter)*5,this);
		}
        
        
        if (shooting && cur_arrow != null) {
            g.drawImage(cur_arrow, curx + 35, characterY - (cur_arrow.getHeight() * 3) + 86, 12, cur_arrow.getHeight() * 3, this);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String timeText = "Time: " + timeLeft;
        
        g.drawString(timeText, getWidth() - 210, 51);
        
        if (showingRestartMessage) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            g.drawString("Restarting in " + countdown, getWidth() / 2 - 120, getHeight() / 2);
        }
        
        for(int i=0;i<userlife-1;i++) {
        	g.drawImage(life_icon.getImage(),i*70+3,600,70,70,this);
        }
        
        String userText = "User: " + username;
        int x = getWidth()/2;
        int y = getHeight() - 50;

        g.drawString(userText, x, y);
        
    }
    
    public class Character_movement implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT && !movingRight) {
                movingRight = true;
                movingLeft = false;
            } else if (key == KeyEvent.VK_LEFT && !movingLeft) {
                movingLeft = true;
                movingRight = false;
            }
            else if(key == KeyEvent.VK_SPACE && arrowlogic==68) {
            	movingLeft = false;
                movingRight = false;
                currentImage = playershooting;
                shooting = true;
                curx = characterX;
                cury = characterY;
                arrowFrameIndex = 0;
                repaint();
            }   
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        	
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT) {
                movingRight = false;
                currentImage = playerstop;
            } 
            else if (key == KeyEvent.VK_LEFT) {
                movingLeft = false;
                currentImage = playerstop;
            }
        }
}
}
