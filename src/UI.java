import javax.imageio.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class UI implements Serializable{
	private Panel panel;
	
	//User Interface trackers
	public int currentScreen;
	public int previousScreen = currentScreen;
	public int guiState = -1;
	public int guiGrade = 1;
	public int listIndex = -1;
	public boolean editing = false;
	public boolean changing = false;
	public boolean typable = true;
	public boolean rgbChanging = false; //SAVE THIS DATA
	public boolean rgbHover = false; //SAVE THIS DATA
	public int rgbCounter = 0; //SAVE THIS DATA
	public int r, g, b = 0; //SAVE THIS DATA
	public double angle = 0; //SAVE THIS DATA
	public String highlight = ".";

	
	public ArrayList<TextBox> homeBoxes = new ArrayList<TextBox>();//SAVE THIS DATA
	public ArrayList<TextBox> classBoxes= new ArrayList<TextBox>();//SAVE THIS DATA
	public ArrayList<TextBox> optionsBoxes= new ArrayList<TextBox>();//SAVE THIS DATA
	public ArrayList<TextBox> howToUseBoxes= new ArrayList<TextBox>();//SAVE THIS DATA
	
	public transient BufferedImage editImage;
	public transient BufferedImage colorWheel;
	public transient BufferedImage checkMark;
	public transient BufferedImage backArrow;
	public transient BufferedImage howToScreen;
	public transient BufferedImage trashcan;
	public transient BufferedImage trashOpen;

	public int bgCX;//SAVE THIS DATA
	public int bgCY;//SAVE THIS DATA
	public Color bgcolor;//SAVE THIS DATA
	public int darkRGB;//SAVE THIS DATA
	public int cX, cY, cR;//SAVE THIS DATA
	public int offsetX, offsetY;//SAVE THIS DATA
	public int sliderX, sliderY;//SAVE THIS DATA
	public int scrollY = 0;
	public int scrollSpeed = 50;
	
	public UI(Panel p) {
		panel = p;
		
		bgCX = 350;
		bgCY = (Panel.SCREEN_HEIGHT/2-200)+290;
		previousScreen = currentScreen = 0;
		darkRGB = 190;
		
		cX = 350;
		cY = (Panel.SCREEN_HEIGHT/2-200)+290;
		cR = 15;
		sliderX = 715;
		sliderY = 320+122;
		
		try {
			Image image = ImageIO.read(new File("color_wheel.png"));
			colorWheel = (BufferedImage) image;
			
			image = ImageIO.read(new File("pencil_edit.png"));
			editImage = (BufferedImage)image;
			
			image = ImageIO.read(new File("check_mark.png"));
			checkMark = (BufferedImage)image;
			
			image = ImageIO.read(new File("back_arrow.jpg"));
			backArrow = (BufferedImage)image;
			
			image = ImageIO.read(new File("how_to_screen.png"));
			howToScreen = (BufferedImage)image;

			image = ImageIO.read(new File("trash.jpg"));
			trashcan = (BufferedImage)image;

			image = ImageIO.read(new File("trash_open.png"));
			trashOpen = (BufferedImage)image;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		offsetX = (int)((cX-50)/(8.0/3));
		offsetY = (int)((cY-(Panel.SCREEN_HEIGHT/2-200))/(8.0/3));
	}
	public void setUI(UI other) {
		this.homeBoxes = other.homeBoxes;
		this.classBoxes = other.classBoxes;
		this.optionsBoxes = other.optionsBoxes;
		this.howToUseBoxes = other.howToUseBoxes;
		
		this.bgCX = other.bgCX;
		this.bgCY = other.bgCY;
		this.bgcolor = other.bgcolor;
		this.darkRGB = other.darkRGB;
		this.cX = other.cX;
		this.cY = other.cY;
		this.cR = other.cR;
		this.offsetX = other.offsetX;
		this.offsetY = other.offsetY;
		this.sliderX = other.sliderX;
		this.sliderY = other.sliderY;
		
	}
	
	
	/**
	 *	@param box specified box to be added
	 *	@param screen 0 = home / 1 = classes / 2 = options / 3 = how it works
	 *	/ 4 = save and exit
	*/
	public void addTextBox(TextBox box, int screen) {
		switch(screen) {
			case 0 : homeBoxes.add(box);break;
			case 1 : classBoxes.add(box);break;
			case 2 : optionsBoxes.add(box);break;
			case 3 : howToUseBoxes.add(box);break;
		}
	}
	public ArrayList<TextBox> getBoxes(String array) {
		switch(array) {
			case "home" : return homeBoxes;
			case "class" : return classBoxes;
			case "options" : return optionsBoxes;
			case "how to use" : return howToUseBoxes;
		}
		return null;
	}
	public void switchScreen(String text) {
		int temp = previousScreen;
		previousScreen = currentScreen;
		switch(text) {
			case "Your Classes" : currentScreen = 1; break;
			case "Options" : currentScreen = 2; break;
			case "How It Works" : currentScreen = 3; break;
			case "Save and Exit" : 
				try {
					panel.savedData.setUIComponents(this);
	    			FileOutputStream fileOut = new FileOutputStream("Data.ser");
		    		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    		out.writeObject(panel.savedData);
		    		out.close();
		    		fileOut.close();
	    		}catch(IOException ex) {
	    			System.out.println("can't write");
	    			ex.printStackTrace();
	    		}
	        	System.exit(0);
	        	break;
			case "Back" : currentScreen = temp; break;
		}
	}
	public void exitEditing(int option) {
		//each stops from changing name of something and unhighlights
		if(option == 1) {//exits out of classes gui and stops editing
			guiState = -1;
			editing = false;
			changing = false;
			highlight = ".";
		}else if(option == 2){//line 145
			changing = false;
			highlight = ".";
		}else if(option == 3) {//sets default Name to whatever was being changed
			changing = false;
			highlight = ".";
			if(classBoxes.get(4).gradeList[listIndex].className.length() == 0)
				classBoxes.get(4).gradeList[listIndex].className = "N/A";
		}
		panel.getInfo().sliderHoverIndex = -1;
		panel.getInfo().gradeHoverIndex = -1;
		
	}
	public boolean ifGradeNumberEditing(){
		return highlight.equals("percent") || highlight.equals("part") || highlight.equals("whole");
	} 

	public void updateUI() {
        //does the rgb effect
		if(rgbChanging){
			rgbCounter++;
			

			if(rgbCounter == 2){
				if(cX-bgCX > 0)
					angle = Math.atan((double)(cY-bgCY)/(cX-bgCX));
				else if(cX-bgCX<0)
					angle = Math.PI+Math.atan((double)(cY-bgCY)/(cX-bgCX));
				else
					if(cY-bgCY > 0)
						angle = Math.PI/2;
					else
						angle = Math.PI + Math.PI/2;

				if(angle < Math.PI*2)
					angle -= Math.PI/600;
				else
					angle = 0;
				
				cX = bgCX+(int)(200*Math.cos(angle));
				cY = bgCY+(int)(200*Math.sin(angle));
				offsetX = (int)((cX-50)/(8.0/3));
				offsetY = (int)((cY-(Panel.SCREEN_HEIGHT/2-200))/(8.0/3));
				Color c = new Color(panel.ui.colorWheel.getRGB(panel.ui.offsetX, panel.ui.offsetY));
				r = c.getRed();
				g = c.getGreen();
				b = c.getBlue();
				panel.setBGColor(new Color(r,g,b));
				rgbCounter=0;
			}
			
			
		}
		
	}
	
	public void drawUI(Graphics2D g2) {
		bgcolor = new Color(colorWheel.getRGB(offsetX, offsetY));
		panel.savedData.bgcolor = bgcolor;
		switch(currentScreen) {
			case 0 : //home screen
				for(TextBox tb : homeBoxes)
					tb.drawTextBox(g2);
				break;
			case 1 : //classes screen
				drawClassesUI(g2);
				
				break;
			case 2 : //options screen
				drawOptionsUI(g2);
				
				break;
			case 3 : //how to use screen
				for(TextBox tb : howToUseBoxes)
					tb.drawTextBox(g2);
				
				g2.drawImage(howToScreen, 0, scrollY, null);
				break;
		}
	}
	
	private void drawClassesUI(Graphics2D g2) {
		//draws the text box / list
		for(TextBox tb : classBoxes){
			if(tb.text == null)
				tb.drawTextListBox(g2);
			else {
				tb.drawTextBox(g2);
			}
		}
		
		//displaying grade information
		if(guiState == 1) {
			//the outter text box
			g2.setColor(Color.black);
			g2.fillRect(470, 180, 1000, 720);
			//draws the outline color grade box
			ClassInformation gradeClass = panel.getInfo();
			g2.setColor(gradeClass.color);
			g2.fillRect(429, 236+72*listIndex, 125, 45);
			g2.fillRect(475, 185, 990, 710);
			g2.setColor(Color.black);
			g2.fillRect(485, 195, 970, 690);
			//draws the white bg
			g2.setColor(Color.white);
			g2.fillRect(490, 200, 960, 680);
			
			
			
			if(guiGrade == 1) {//if viewing grade						
				
				gradeClass.drawPieChart(g2);
				
				//writes the name of the class
				g2.setColor(Color.black);
				g2.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
				g2.drawString(gradeClass.className, 970 - g2.getFontMetrics().stringWidth(gradeClass.className)/2, 190+g2.getFontMetrics().getHeight());
				g2.drawString("Weights", 500, 860); //g2.drawRect(500, 805, 240, 65);
				g2.drawString("Grades", 1240, 860); //g2.drawRect(1240, 805, 200, 65);
				
				
			}else if(guiGrade == 2) { //if viewing weights

				//checks if the name is valid while editing a weight
				if(gradeClass.sliderHoverIndex != -1){
					Slidebar sb = gradeClass.weights.get(gradeClass.sliderHoverIndex);
					if(g2.getFontMetrics().stringWidth(sb.name) < 330){
						typable = true;
					}else{
						sb.name = sb.name.substring(0,sb.name.length()-1);
						typable = false;
					}
				}
				//draws the sliders for weights
				gradeClass.drawSliders(g2);
								
				//draws the back arrow
				g2.drawImage(backArrow, 495, 803, null);
				g2.setColor(Color.black);
				//draws the title
				g2.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
				g2.drawString("Weights", 970 - g2.getFontMetrics().stringWidth("Weights")/2, 190+g2.getFontMetrics().getHeight());
			
				//draws scroll bar
				g2.setColor(new Color(217,217,217));
				g2.fillRect(1430, 200, 20, 680);
				
				
			}else if(guiGrade == 3) {//if viewing the grades
				//checks if the number entered for grade input is valid while editing grade
				if(gradeClass.gradeHoverIndex != -1){
					Grade grade = gradeClass.grades.get(gradeClass.gradeHoverIndex);
					
					//max range for the name of the grade
					if(g2.getFontMetrics().stringWidth(grade.gradeName) < 200){
						typable = true;
					}else{
						grade.gradeName = grade.gradeName.substring(0,grade.gradeName.length()-1);
						typable = false;
					}

					//max range for the number of the grade
					if(!grade.number.equals("#") || !grade.numberPart.equals("#")  || !grade.numberWhole.equals("#")){
						if(highlight.equals("percent") && grade.number.length() != 0 && Integer.parseInt(grade.number) > 999999){
							grade.number = grade.number.substring(0, grade.number.length()-1);

						}else if(highlight.equals("part") && grade.numberPart.length() != 0 && Integer.parseInt(grade.numberPart) > 999999){
							grade.numberPart = grade.numberPart.substring(0, grade.numberPart.length()-1);

						}else if(highlight.equals("whole") && grade.numberWhole.length() != 0 && Integer.parseInt(grade.numberWhole) > 999999){
							grade.numberWhole = grade.numberWhole.substring(0, grade.numberWhole.length()-1);
						
						}
						
					}
						
					

					//auto clears the box when pressed with no number
					if(grade.number.equals("#")){
						grade.number = "";
					}else if(grade.numberPart.equals("#")){
						grade.numberPart = "";
					}else if(grade.numberWhole.equals("#")){
						grade.numberWhole = "";
					}
				}


				gradeClass.drawGrades(g2, bgcolor);
				
				//draws the title
				g2.setColor(Color.black);
				g2.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
				g2.drawString("Grades", 970 - g2.getFontMetrics().stringWidth("Grades")/2, 190+g2.getFontMetrics().getHeight());
				
				//draws the back arrow
				g2.drawImage(backArrow, 495, 803, null);
			}
			
			//draws the exit box
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f));
			g2.setColor(Color.gray);
			g2.fillRect(1380, 100, 80, 80);
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.black);
			g2.drawRect(1380,100,80,80);
			g2.drawLine(1390, 110, 1450, 170);
			g2.drawLine(1450, 110, 1390, 170);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2.setStroke(new BasicStroke(1));
			
			//debugging: highligh boxes
			//g2.drawRect(620, 220, 700, 70);
			
			//draws the editing image
			g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
			if(!editing) {
				//if NOT editing
				//edit items symbol
				g2.drawString("edit items", 540, 227);
				g2.drawImage(editImage, 495, 205, null);
			}else {
				//if ARE editing

				//save changes symbol
				g2.drawString("save changes", 540, 227);
				g2.drawImage(checkMark, 495, 205, null);
				
				//hovering vs pressed while editing over name editing thing
				if(changing && guiGrade == 1) {
					//System.out.println("hehehehe: "+g2.getFontMetrics().stringWidth(gradeClass.className));
					if(g2.getFontMetrics().stringWidth(gradeClass.className)<165) {
						typable = true;
					}else {
						gradeClass.className = gradeClass.className.substring(0,gradeClass.className.length()-1);
						
						typable = false;
					}
					g2.setColor(Color.black);
				}else {
					g2.setColor(gradeClass.color);
				}

				//draws the trash can icon weight 
				if(guiGrade == 2){
					for(Slidebar sb : panel.getInfo().weights){
						
						if(sb.y > 250 && sb.y < 800-55)
							if(!sb.overTrash){
								g2.drawImage(trashcan, sb.x-130, sb.y-7, null);
							}else{
								g2.drawImage(trashOpen, sb.x-130, sb.y-12, null);
								g2.setColor(new Color(255,0,0,50));
								g2.fillRect(sb.x-135, sb.y-35,930+10,120);
								
							}
					}
				}

				//draws the trash can icon in grades
				if(guiGrade == 3){
					//loops through each grade
					for(int i = 0; i < gradeClass.grades.size(); i++){
						//draws the trash can open/closed in the given bounds of the ui
						if(gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){
							if(!gradeClass.grades.get(i).overTrash){
								g2.drawImage(trashcan, 505+20, gradeClass.startGradeY+160*i-30, null);
							}else{
								g2.drawImage(trashOpen, 505+20, gradeClass.startGradeY+160*i-5-30, null);
								g2.setColor(new Color(255,0,0,50));
								g2.fillRect(505, panel.getInfo().startGradeY-150+160*i+100+5, 930, 70+90+8);
							}
							
						}

						
					}
				}

				//highlights the certain component to edit
				g2.setStroke(new BasicStroke(4));
				int i = panel.getInfo().gradeHoverIndex;
				//System.out.println(highlight);
				switch(highlight) {
					case "name" : 
						g2.drawRect(620, 220, 700, 70);
						break;
					case "weight" :
						if(panel.getInfo().sliderHoverIndex != -1){
							Slidebar sb = panel.getInfo().weights.get(panel.getInfo().sliderHoverIndex);
							if(sb.y > 200 && sb.y < 700)
								g2.drawRect(sb.x+sb.boxWidth+20, sb.y+10, 310, 30);
						}
						break;
					case "percent" :
						//finds the grade that is being hovered over the number
						if(i != -1 && gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){
							g2.setColor(new Color(bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue(), 100));
							g2.fillRect(490+15+85+15+95+3, (panel.getInfo().startGradeY-150+160*i)+165+20-1+3, 130-6, 60-6);
							
						}
						panel.getInfo().grades.get(i).number = panel.getInfo().grades.get(i).correctNumber(panel.getInfo().grades.get(i).number);
						break;
					case "part" :
						//finds the grade that is being hovered over the number
						if(i != -1 && gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){
							g2.setColor(new Color(bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue(), 100));
							g2.fillRect(490+15+85+15+3, (panel.getInfo().startGradeY-150+160*i)+165+20-1+3, 130-6, 60-6);
						}
						panel.getInfo().grades.get(i).numberPart = panel.getInfo().grades.get(i).correctNumber(panel.getInfo().grades.get(i).numberPart);
						break;
					case "whole" :
						//finds the grade that is being hovered over the number
						if(i != -1 && gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){
							g2.setColor(new Color(bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue(), 100));
							g2.fillRect(490+15+85+15+190+3, (panel.getInfo().startGradeY-150+160*i)+165+20-1+3, 130-6, 60-6);
						}
						panel.getInfo().grades.get(i).numberWhole = panel.getInfo().grades.get(i).correctNumber(panel.getInfo().grades.get(i).numberWhole);
						break;
					
					case "grade" : 
						if(i != -1 && gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){
							g2.setStroke(new BasicStroke(2));
							g2.setColor(Color.black);
							g2.drawRect(490+15+85+10, panel.getInfo().startGradeY-150+160*i+165-36, 350,40);
							g2.setStroke(new BasicStroke(1));
						}
						break;
				
					case "menu" :
						if(i != -1 && gradeClass.startGradeY+160*i > 250 && gradeClass.startGradeY+160*i < 800-55){ 
							//start y value for the box of menu to be drawn
							int yMenu = gradeClass.startGradeY-150+160*i;

							//max amount of weights allowrd to be displayed in the drop down menu
							int amount = gradeClass.weights.size() > 4 ? 4 : gradeClass.weights.size();
							amount = amount == 0 ? 1 : amount;

							//draws the outside of the box
							g2.setColor(Color.BLACK);
							g2.fillRect(490+15+85+15+190+130+35+15, yMenu+130+20, 450, 90+65*amount);
							g2.setColor(new Color(bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue(), 200));
							g2.fillRect(490+15+85+15+190+130+35+15+5, yMenu+130+20+5, 450-10, 90-10+65*amount);
							
							//draws the individual boxes
							//g2.setColor(Color.white);
							//g2.setColor(Color.red);
							for(int j = 0; j < gradeClass.weights.size(); j++){
								//TODO: DRAW THE BOXES AND INCLUDE THE BOUNDS, THEN ADD ABILITY TO SCROLL
								int movingMenuY = gradeClass.startMenuY-150+160*i;
								
								//makes the weights from the drop box appear from a specific range
								if((movingMenuY+130+20+5+10+70*j+20 >= yMenu+130+20)&&(movingMenuY+130+20+5+10+70*j+20+60 <= yMenu+130+20+90+65*amount)){
									//changes colors if mouse over selected weight
									if(gradeClass.menuBlockHoverIndex == j){
										g2.setColor(new Color(230,230,230));
									}else{
										g2.setColor(Color.white);
									}
									//draws the white background/shaded box for the name and weight
									g2.fillRect(490+15+85+15+190+130+35+15+5+10, movingMenuY+130+20+5+10+70*j+20, 450-10-20, 90-10-20);
									g2.setColor(Color.black);
									//writes the name and weight %
									g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
									g2.drawString(gradeClass.weights.get(j).percent+"%", 490+15+85+15+190+130+35+35, movingMenuY+165+20+9-1+60-17-12-20+70*j+20);
									g2.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
									g2.drawString(gradeClass.weights.get(j).name, 490+15+85+15+190+130+35+35+85, movingMenuY+165+20+9-1+60-17-14-20+70*j+20);
									g2.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
									g2.drawString((j+1)+"/"+gradeClass.weights.size(), 490+15+85+15+190+130+35+35, movingMenuY+165+20+9-1+60-17-12-20+70*j+20+15);
								}
								
							}
						}
						break;
				}
			}
		}
	}
	

	private void drawOptionsUI(Graphics2D g2) {
		//draws the text boxes
		for(TextBox tb : optionsBoxes) {
			tb.drawTextBox(g2);
		}
		//draws the colorwheel
		g2.drawImage(colorWheel, 50,Panel.SCREEN_HEIGHT/2-200,600,600,null);
		g2.setColor(Color.black);
    	g2.setStroke(new BasicStroke(35));
		g2.drawOval(112,(Panel.SCREEN_HEIGHT/2-200)+57,485,485);

		//draws the color wheel circle color selector 
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(4));
		g2.drawOval(cX,cY,cR,cR);
		
		//draws the brightness controller slider
		int r = bgcolor.getRed();
		int g = bgcolor.getGreen();
		int b = bgcolor.getBlue();
		for(int i = 0; i < 255; i++) {
			g2.setColor(new Color(r,g,b));
			g2.drawLine(730,320+i+i,880,320+i+i);
			g2.setColor(new Color(0,0,0,255-i));
			g2.drawLine(730,320+i+i,880,320+i+i);
		}
		
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.white);
		g2.drawRect(730, 320, 150, 510);
		
		//draws the sliding slider handle
		g2.setStroke(new BasicStroke(4));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
		g2.setColor(Color.white);
		g2.fillRect(sliderX, sliderY, 180, 20);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.setColor(Color.black);
		g2.drawRect(sliderX, sliderY, 180, 20);
		
		//draws the option button for rgb background
		g2.setStroke(new BasicStroke(5));
		g2.setColor(Color.white);
		g2.fillRect(1210, 320, 55, 55);
		Color c = rgbHover ? new Color(128, 128, 128) : new Color(66,66,66);
		g2.setColor(c);
		g2.drawRect(1210, 320, 55, 55); 
		g2.setStroke(new BasicStroke(1));

		//draws the lock for the color wheel and slider
		g2.setColor(Color.black);
		g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		if(rgbChanging){
			//draws the locks
			g2.drawString("on", 1222, 357);
			g2.setStroke(new BasicStroke(30));
			g2.fillRect(260, 560, 180,115);
			g2.drawOval(275, 490, 150, 150);

		}else{
			g2.drawString("off", 1216, 358);
		}
		g2.setStroke(new BasicStroke(1));
	}
}
