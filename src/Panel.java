import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Panel extends JPanel implements Runnable{
	
	public static final int SCREEN_WIDTH = 1500, SCREEN_HEIGHT = 950;
	private transient Thread progamThread;
	private KeyInput ki  = new KeyInput(this);
	private  MouseInput mi= new MouseInput(this);
	public UI ui;
	public SavedData savedData;
	
	int sY = 300;
	int scroll = 10;
	
	public Panel(){
        savedData = new SavedData();
        ui = new UI(this);
		
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.addKeyListener(ki);
        this.addMouseListener(mi);
        this.addMouseMotionListener(mi);
        this.addMouseWheelListener(mi);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }
	public void startProgram() {
		try {
    		FileInputStream fileIn = new FileInputStream("Data.ser");
    		ObjectInputStream in = new ObjectInputStream(fileIn);
    		savedData = (SavedData)in.readObject();
    		in.close();
    		fileIn.close();
    		ui = savedData.getUIComponents(this);
    		System.out.println("data loaded successfully");
    		
    		
    	}catch(IOException ex) {
    		System.out.println("################# ERROR LOADING DATA #################");
    		ui = new UI(this);
    		setHomeUI();
			setClassesUI();
			setOptionsUI();
			setHowToUseUI();
    	}catch(ClassNotFoundException ex) {
    		System.out.println("no class");
    	}
		Color c = new Color(ui.colorWheel.getRGB(ui.offsetX, ui.offsetY));
		int r = c.getRed();// - ui.darkRGB < 0 ? 0 : c.getRed()- ui.darkRGB;
		int g = c.getGreen();// - ui.darkRGB < 0 ? 0 : c.getGreen()- ui.darkRGB;
		int b = c.getBlue();// - ui.darkRGB < 0 ? 0 : c.getBlue()- ui.darkRGB;
		

        this.setBackground(new Color(r,g,b));
        
        progamThread = new Thread(this);
		progamThread.start();
	}
	
	//sets up UI for each screen
	private void setHomeUI() {
		Font font = new Font("Comic Sans MS", Font.PLAIN, 80);
		TextBox title = new TextBox(this, "Grade Calculator", true, false, SCREEN_WIDTH/2 - 310, 40, font, Color.white);
		ui.addTextBox(title, 0);
		font = new Font("Comic Sans MS", Font.PLAIN, 40);
		
		int y = 330;
		TextBox classes = new TextBox(this, "Your Classes", false, true, SCREEN_WIDTH/2 - 116, y, font, Color.white);
		TextBox options = new TextBox(this, "Options", false, true, SCREEN_WIDTH/2 - 72, y+120, font, Color.white);
		TextBox help = new TextBox(this, "How It Works", false, true, SCREEN_WIDTH/2 - 134, y+120*2, font, Color.white);
		TextBox save = new TextBox(this, "Save and Exit", false, true, SCREEN_WIDTH/2 - 128, y+120*3, font, Color.white);
		ui.addTextBox(classes, 0);
		ui.addTextBox(options, 0);
		ui.addTextBox(help, 0);
		ui.addTextBox(save, 0);
	}
	private void setClassesUI() {
		Font font = new Font("Comic Sans MS", Font.PLAIN, 80);
		TextBox title = new TextBox(this, "Classes", true, false, SCREEN_WIDTH/2 - 136, 40, font, Color.white);
		ui.addTextBox(title, 1);
		
		font = new Font("Comic Sans MS", Font.PLAIN, 27);
		TextBox back = new TextBox(this, "Back", false, true, 45,SCREEN_HEIGHT-80, font, Color.white);
		TextBox classes = new TextBox(this, "Name", false, false, 185, 150, font, Color.white);
		TextBox grade = new TextBox(this, "Grade", false, false, 355, 150, font, Color.white);
		
		TextBox list = new TextBox(this, false, 150,240,font,Color.white);
		//TextBox list = new TextBox(this, s, false, 150,240,font,Color.white);
		
		ui.addTextBox(back, 1);
		ui.addTextBox(classes, 1);
		ui.addTextBox(grade, 1);
		ui.addTextBox(list, 1);
	}
	private void setOptionsUI() {
		Font font = new Font("Comic Sans MS", Font.PLAIN, 80);
		TextBox title = new TextBox(this, "Options", true, false, SCREEN_WIDTH/2 - 145, 40, font, Color.white);
		ui.addTextBox(title, 2);
		
		font = new Font("Comic Sans MS", Font.PLAIN, 30);
		TextBox back = new TextBox(this, "Back", false, true, 45,SCREEN_HEIGHT-80, font, Color.white);
		TextBox bgc = new TextBox(this, "Background Color", false, false, 240, SCREEN_HEIGHT/2-250, font, Color.white);
		TextBox darkness = new TextBox(this, "Color Darkness", false, false, 700, SCREEN_HEIGHT/2-250, font, Color.white);
		TextBox rgbColor = new TextBox(this, "RGB Background", false, false, 1120, SCREEN_HEIGHT/2-250, font, Color.white);
		ui.addTextBox(bgc, 2);
		ui.addTextBox(back, 2);
		ui.addTextBox(darkness, 2);
		ui.addTextBox(rgbColor, 2);
	}
	private void setHowToUseUI() {		
		Font font = new Font("Comic Sans MS", Font.PLAIN, 30);
		TextBox back = new TextBox(this, "Back", false, true, 45,SCREEN_HEIGHT-80, font, Color.white);
		ui.addTextBox(back, 3);
	}
	
	public void run() {
		try {
			while(true) {
				repaint();
				update();
				Thread.sleep(10);
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void update() {
		ui.updateUI();
		mi.mouseHover();
		//controls smooth scrolling for mouse
		if(mi.moving) {
			float travel = (ui.scrollSpeed/mi.speed);
	    	mi.dis = (int)(travel);
	    	
	    	mi.movingY += mi.direction*mi.dis;

	    	//checks if the upper or lower bounds have been hit
	    	if(mi.movingY >= mi.scrollMin) {
	    		mi.movingY = mi.scrollMin;
	    		mi.moving = false;
    			mi.speed = 1f;
	    	}else if(mi.movingY <= mi.scrollMax) {
	    		mi.movingY = mi.scrollMax;
    			mi.moving = false;
    			mi.speed = 1f;
	    	}
	    	
	    	//adjusts the speed when needed
	    	if(mi.speed > 10f) {
		    	mi.moving = false;
		    	mi.speed = 1f;
		    }else {
		    	mi.speed+=1f;
		    }
	    	
	    	if(ui.currentScreen == 3){
	    		ui.scrollY = mi.movingY;
			}else if(ui.guiGrade == 2){//System.out.println(ui.classBoxes.get(4).gradeList[ui.listIndex].startY);
	    		ui.classBoxes.get(4).gradeList[ui.listIndex].startWeightY = mi.movingY;
	    	}else if(ui.guiGrade == 3){
				if(ui.highlight.equals("menu")){
					ui.classBoxes.get(4).gradeList[ui.listIndex].startMenuY = mi.movingY;
				}else{
					ui.classBoxes.get(4).gradeList[ui.listIndex].startGradeY = mi.movingY;
				}
				
			}
		}
	}
	
	public ClassInformation getInfo(){
		return ui.classBoxes.get(4).gradeList[ui.listIndex];
	}

	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
        Graphics2D g2 = (Graphics2D) gr;
	    g2.setColor(new Color(0,0,0,ui.darkRGB));
	    g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	    ui.drawUI(g2);
        
        
	}
	public void setBGColor(Color c) {
		this.setBackground(c);
	}
	
	
};
