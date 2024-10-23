import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
/**
 * Class that manages mouse clicks the user does and how it impacts the GUI 
 */
public class MouseInput extends MouseAdapter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8359552026505993562L;
	public Panel panel;
	public int dragging = -1;
	public float speed = 1f;
	public float dis = 0;
	public int direction = 0;
	public int movingY = 0;
	public int scrollMax = 0;
	public int scrollMin = 0;
	public boolean moving = false;
    
    public MouseInput(Panel panel) {
        this.panel=panel;
    }
    
    public void mousePressed(MouseEvent e){
    	int mouseX = e.getX();
        int mouseY = e.getY();
        
        //clicking ui buttons 
		
        ArrayList<TextBox> boxes = null;
        
        switch(panel.ui.currentScreen) {
        	case 0 : boxes = panel.ui.getBoxes("home");break;
        	case 1 : boxes = panel.ui.getBoxes("class");
        		
				//determines which class was pressed
				for(int i = 0; i < 7; i++) {
        			//shows class details
        			if(mouseOverBoxArea(mouseX, mouseY,329, 236+72*i, 125, 45)) {
        				panel.ui.guiState = 1;
        				panel.ui.listIndex = i;
						panel.ui.exitEditing(3);
        				break;
        			} 
        		}
        		
				//exits class details
        		if(panel.ui.guiState == 1 && mouseOverBoxArea(mouseX, mouseY,1380, 110, 80, 80)){
        			panel.ui.exitEditing(1);
        		}
        		
				//detects if the user pressed on a name while editing
				String highlight = panel.ui.highlight;
				panel.ui.changing = ((highlight.equals("name") || highlight.equals("weight")));
					

        		//switches between class, grade, and weights gui 
        		if(panel.ui.guiState == 1) {
        			//if editing class
        			if(mouseOverBoxArea(mouseX, mouseY, 495, 205, 40, 40)) {
        				panel.ui.editing = !panel.ui.editing;
        			}
        			//piechart to weights
        			if(panel.ui.guiGrade == 1 && mouseOverBoxArea(mouseX, mouseY, 500,805,240,65)) {
        				panel.ui.guiGrade = 2;
        				panel.ui.exitEditing(3);
					
					//piechart to grades
        			}else if(panel.ui.guiGrade == 1 && mouseOverBoxArea(mouseX, mouseY, 1240, 805, 200, 65)) {
        				panel.ui.guiGrade = 3;
        				panel.ui.exitEditing(3);
						ClassInformation g = panel.getInfo();
						for(int i = 0; i < g.weights.size(); i++){
							for(int j = 0; j < g.grades.size(); j++){
								Grade gr = g.grades.get(j);
								if(gr.weightIndexTracker == i){
									g.grades.get(j).weightName = g.weights.get(i).name;
									g.grades.get(j).weight = g.weights.get(i).percent;
								}
							}
						}
						
					//weights or grades to piechart
					}else if(panel.ui.guiGrade == 2 || panel.ui.guiGrade == 3) {
        				//going back to grade
        				if(mouseOverBoxArea(mouseX, mouseY, 495, 803, 115, 72)) {
        					panel.getInfo().setNumericalGrade();
							panel.ui.guiGrade = 1;
        					panel.ui.exitEditing(2);
        				}
        			}
        		}  		
        		
        		//handles weight pressing on components
        		if(panel.ui.guiGrade == 2) {
					//if hovering over and pressed the add button
        			ClassInformation g = panel.getInfo();
        			if(g.hoveredAdd && (mouseY > 250 && mouseY < 800)) {
        				if(g.weights.size() == 0) {
    	    				if(mouseOverBoxArea(mouseX, mouseY, 925, g.startWeightY, 50, 50))
    	    					g.addWeight(0);
    	    			}else {
    	    				if(mouseOverBoxArea(mouseX, mouseY, 925, g.weights.getLast().y+150, 50, 50))
    	    					g.addWeight(1);
    	    			}
        			}

					//if pressing and hovering over weight component
					Slidebar sb = null;
					for(int i = 0; i < g.weights.size(); i++){
						sb = g.weights.get(i);
						//if over circle or weight box
						if(sb.hoveredSlider)
							dragging = 3;

						//if editing and over the name of a weight 
						if(panel.ui.editing && mouseOverBoxArea(mouseX, mouseY, sb.x+sb.boxWidth+20, sb.y+10, 310, 30)){
							g.sliderHoverIndex = i;
							panel.ui.highlight = "weight";
							panel.ui.changing = true;
							break;
						}else{
							panel.ui.changing = false;
						}

						//if pressed delete button
						if(sb.overTrash){
							//find the grade that has the weight and resets it
							for(int j = 0; j < g.grades.size(); j++){
								Grade gr = g.grades.get(j);
								if(gr.weightIndexTracker == i){
									g.grades.get(j).clear();
									gr.weightIndexTracker = -1;
								}
							}
							g.weights.remove(i);
							if(g.weights.size() <3){
								g.startWeightY = 350;
							}

						}

						if(dragging == 3){
							g.sliderHoverIndex = i;
							break;
						}else{
							g.sliderHoverIndex = -1;
						}
					}
        		}
        		
				//handles grades pressing on components
				if(panel.ui.guiGrade == 3){
					//of hovering over and pressed the add button
					ClassInformation g = panel.getInfo();
					if(g.hoveredAdd && (mouseY > 250 && mouseY < 750)){
						g.addGrade();
					}

					//looping through each grade
					for(int i = 0; i < g.grades.size(); i++){
						if(mouseY > 250 && mouseY < 750){
							Grade grade = g.grades.get(i);
							//if pressed the delete button while editing
							if(grade.overTrash){
								g.grades.remove(i);
								if(g.grades.size() < 3){
									g.startGradeY = 350;
								}
							}

							//if pressed on the mode switch button
							if(g.hoveredMode == i){
								grade.switchMode();
							}
							
							//sets the name and weight of the grade that the user selects
							if(g.gradeIndex == i && g.menuBlockHoverIndex != -1){
								g.grades.get(i).weight = g.weights.get(g.menuBlockHoverIndex).percent;
								g.grades.get(i).weightName = g.weights.get(g.menuBlockHoverIndex).name;
								g.grades.get(i).weightIndexTracker = g.menuBlockHoverIndex;
								grade.overDropMenu = false;
								g.menuBlockHoverIndex = -1;
								g.gradeIndex = -1;
								panel.ui.highlight = ".";
								break;
							}

							//if pressed on the numbers while editing
							if(grade.overNumber != -1){
								if(grade.overNumber == 0){
									panel.ui.highlight = "percent";

								}else if(grade.overNumber == 1){
									panel.ui.highlight = "part";

								}else if(grade.overNumber == 2){
									panel.ui.highlight = "whole";

								}
								panel.ui.changing = true;
								g.gradeHoverIndex = i;
								break;
							}else{
								panel.ui.highlight = ".";
								panel.ui.changing = false;
								g.gradeHoverIndex = -1;
							}

							//if pressed on name of grade while editing
							if(grade.overName){
								panel.ui.highlight = "grade";
								g.gradeHoverIndex = i;
								panel.ui.changing = true;
								break;
							}else{
								panel.ui.highlight = ".";
								panel.ui.changing = false;
								g.gradeHoverIndex = -1;
							}

							//if pressed on dropdown menu button to appear while editing
							if(grade.overDropMenu){
								g.gradeHoverIndex = i;
								panel.ui.highlight = "menu";
								
								g.startMenuY = g.startGradeY;
								break;
							}else{
								g.gradeHoverIndex = -1;
								panel.ui.highlight = ".";
							}
		
						}
						
						
						
					}
				}


        		break;
        	case 2 : boxes = panel.ui.getBoxes("options");
				//if presssed on rgb background color switcher
				if(panel.ui.rgbHover){
					panel.ui.rgbChanging = !panel.ui.rgbChanging;
				}

        		
					
				//if clicking the color wheel/darkness slider
				if(mouseOverCircleArea(mouseX, mouseY, panel.ui.bgCX, panel.ui.bgCY, 220) && !panel.ui.rgbChanging) {
					dragging = 1;
				}else if(mouseOverBoxArea(mouseX, mouseY, 715, 310, 180,530)) {
					dragging = 2;
				}
				

				
				
        		break;
        	case 3 : boxes = panel.ui.getBoxes("how to use");break;
        }
		//checks if pressed on any back button
        for(int i = 0; i < boxes.size(); i++) {
			TextBox tb = boxes.get(i);
			if(tb.hovered) {
				//stops editing once left the "Classes" screen
				if(tb.text.equals("Back")){
					if(panel.ui.listIndex != -1)
						panel.ui.exitEditing(1);
				}
				//switches screens
				panel.ui.switchScreen(tb.text);
				break;
			}
		}
    }
    public void mouseHover() {
    	Point p = new Point(panel.getLocationOnScreen());
    	int panelX = (int)p.getX();
    	int panelY = (int)p.getY();
    	Point m = MouseInfo.getPointerInfo().getLocation();
    	int mouseX = (int)(m.getX()-panelX);
    	int mouseY = (int)(m.getY()-panelY);
    	
    	ArrayList<TextBox> boxes = null;
    	switch(panel.ui.currentScreen) {
	    	case 0 : boxes = panel.ui.getBoxes("home");break;
	    	case 1 : boxes = panel.ui.getBoxes("class");
	    		//if hovering over a name of a class
	    		if(panel.ui.guiGrade == 1) {
		    		if(panel.ui.editing) {
		    			if(mouseOverBoxArea(mouseX, mouseY, 620, 220, 700, 70)) {
		    				panel.ui.highlight = "name";
		    				
		    			}else if(!panel.ui.changing){
		    				panel.ui.highlight = ".";
		    			}
		    		}
	    		}
	    		
				//if inside the weights GUI
	    		if(panel.ui.guiGrade == 2) {
	    			ClassInformation g = panel.ui.classBoxes.get(4).gradeList[panel.ui.listIndex];
	   				
					//if over an add weight button 
	    			if(g.weights.size() == 0) {
	    				g.hoveredAdd = mouseOverBoxArea(mouseX, mouseY, 925, g.startWeightY, 50, 50) ? true : false;
	    			}else {
	    				g.hoveredAdd = mouseOverBoxArea(mouseX, mouseY, 925, g.weights.getLast().y+150, 50, 50) ? true : false;
						
						
						Slidebar sb = null;
						//looping through each slidebar option
						for(int i = 0; i < g.weights.size(); i++){
							sb = g.weights.get(i);
							//if over a circle and entire weight bar for a weight slider
							sb.hoveredSlider = mouseOverBoxArea(mouseX, mouseY,sb.x+20,sb.y+15, sb.lineWidth,20);
							if(panel.ui.editing){
								sb.hoveredName = mouseOverBoxArea(mouseX, mouseY, sb.x+sb.boxWidth+20, sb.y+10, 310, 30);
							}
							
							//if hovering over trash can icon 
							if(panel.ui.editing && mouseOverBoxArea(mouseX, mouseY, sb.x-130, sb.y-7, 50, 65)){
								sb.overTrash = true;
							}else{
								sb.overTrash = false;
							}
						}
	    			}
	   			}
	    		
				//if inside the grades GUI
				if(panel.ui.guiGrade == 3){
					//current subject the user is on
					ClassInformation g = panel.ui.classBoxes.get(4).gradeList[panel.ui.listIndex];
					
					//if hovering over the add button
					if(g.grades.size() == 0){
						g.hoveredAdd = mouseOverBoxArea(mouseX, mouseY, 925,g.startGradeY, 50, 50);
					}else{
						g.hoveredAdd = mouseOverBoxArea(mouseX, mouseY, 925, g.startGradeY+160*g.grades.size(), 50, 50);

						//looping through each grade
						for(int i = 0; i < g.grades.size(); i++){
							//if hovering over the mode switch button
							if(mouseOverBoxArea(mouseX, mouseY, 535, g.startGradeY+40+1+160*i, 40, 49)||
							   mouseOverCircleArea(mouseX, mouseY, 505+25, g.startGradeY+40+160*i+25, 25)||
							   mouseOverCircleArea(mouseX, mouseY, 545+25, g.startGradeY+40+160*i+25, 25)){
								
								g.hoveredMode = i;
								g.grades.get(i).overTrash = false;
								break;

							}else{
								g.hoveredMode = -1;
							}

							//if hovering over items while editing
							if(panel.ui.editing){
								//if hovering over trash can icon
								g.grades.get(i).overTrash = mouseOverBoxArea(mouseX, mouseY, 525, g.startGradeY+160*i-30, 50, 65);
								g.hoveredMode = -1;
								
								//if hovering over the number(s) of the grade with its respective mode
								g.grades.get(i).overNumber = -1;
								if(g.grades.get(i).mode == '/'){
									if(mouseOverBoxArea(mouseX, mouseY, 605, g.startGradeY-150+160*i+184, 130, 60)){
										g.grades.get(i).overNumber = 1;
									}else if(mouseOverBoxArea(mouseX, mouseY, 605+190, g.startGradeY-150+160*i+184, 130, 60)){
										g.grades.get(i).overNumber = 2;
									}

								}else if(g.grades.get(i).mode == '%' ){
									if(mouseOverBoxArea(mouseX, mouseY, 605+95, g.startGradeY-150+160*i+184, 130, 60)){
										g.grades.get(i).overNumber = 0;
										
									}

								}
								//if hovering over the name of the gade
								g.grades.get(i).overName = mouseOverBoxArea(mouseX, mouseY, 490+15+85+10, g.startGradeY-150+160*i+165-36, 350, 40);

								//if hovering over the weight drop down menu
								g.grades.get(i).overDropMenu = !panel.ui.highlight.equals("menu") && mouseOverTriangleArea(mouseX, mouseY, 490+15+85+15+190+130+35+15+200, g.startGradeY-150+160*i+130+20+90, 50, 25);
								
								//if hovering over box of weight
								int amount = g.weights.size() > 4 ? 4 : g.weights.size();
								amount = amount == 0 ? 1 : amount;
								if(panel.ui.highlight.equals("menu")){
									for(int w = 0; w < g.weights.size(); w++){
										int movingMenuY = g.startMenuY-150*160*g.gradeHoverIndex;
										if((movingMenuY+130+20+5+10+70*w+20 >= g.startGradeY-150*160*g.gradeHoverIndex+130+20)&&(movingMenuY+130+20+5+10+70*w+20+60 <= g.startGradeY-150*160*g.gradeHoverIndex+130+20+90+65*amount))
											if(mouseOverBoxArea(mouseX, mouseY, 990, g.startMenuY-150+160*g.gradeHoverIndex+130+20+5+10+70*w+20, 420, 60)){
												g.menuBlockHoverIndex = w;
												g.gradeIndex = g.gradeHoverIndex;
												break;
											}else{
												g.menuBlockHoverIndex = -1;
												g.gradeIndex = -1;
											}
									}
								}
							}
						}
					}
				}

	    		break;
	    	case 2 : boxes = panel.ui.getBoxes("options");
				//if over rgb background button
				panel.ui.rgbHover = mouseOverBoxArea(mouseX, mouseY, 1210, 320, 55, 55);
				
				break;			 
        	case 3 : boxes = panel.ui.getBoxes("how to use");break;
		}
    	
		for(int i = 0; i < boxes.size(); i++) {
			TextBox tb = boxes.get(i);
			if(tb.button && mouseOverBoxArea(mouseX,mouseY,tb.boxX, tb.boxY, tb.boxWidth, tb.boxHeight)) {
				tb.hovered = true;
				break;
			}else {
				tb.hovered = false;
			}
		}
    }
    
	public void mouseReleased(MouseEvent e) {
    	dragging = -1;
    }
    public void mouseDragged(MouseEvent e) {
    	int mx = (int)(e.getX());
    	int my = (int)(e.getY());
		//if dragging color wheel
    	if(dragging == 1) {
			//checks if the mouse is inside the bounds of the circle
    		if(mouseOverCircleArea(mx, my, panel.ui.bgCX, panel.ui.bgCY, 220)) {
    			int dx = mx-panel.ui.cX-5;
    			int dy = my-panel.ui.cY-5;
    			panel.ui.cX = panel.ui.cX + dx;
    			panel.ui.cY = panel.ui.cY + dy;
    		}else {
    	    	
    	    	double angle = 0;
    	    	if(mx > 350) {
    	    		angle = Math.atan((double)(my-panel.ui.bgCY)/(mx-350));
    	    	}else if(mx < 350) {
    	    		angle = Math.PI+Math.atan((double)(my-panel.ui.bgCY)/(mx-350));
    	    	}else if(mx == 350){
    	    		if(my > panel.ui.bgCY) {
    	    			angle = Math.PI/2;
    	    		}else {
    	    			angle = Math.PI+Math.PI/2;
    	    		}
    	    	}

    			panel.ui.cX = panel.ui.bgCX+(int)(220*Math.cos(angle));
    			panel.ui.cY = panel.ui.bgCY+(int)(220*Math.sin(angle));
    		}
    		
    		
    	//if dragging color wheel darkness
    	}else if (dragging == 2) {
    		if(my >= 310 && my <= 820) {
    			panel.ui.sliderY = my;
    			
    		}else {
    			panel.ui.sliderY = my < 310 ? 310 : 820;
    		}
    		panel.ui.darkRGB=255-(panel.ui.sliderY-310)/2;
    		
		//if dragging weight slider
    	}else if (dragging == 3){
			ClassInformation g = panel.ui.classBoxes.get(4).gradeList[panel.ui.listIndex];
			if(mx >= 645 && mx <= 1055){
				g.weights.get(g.sliderHoverIndex).sliderX = mx-25/2;
			}
		}


    	panel.ui.offsetX = (int)((panel.ui.cX-50)/(8.0/3));
    	panel.ui.offsetY = (int)((panel.ui.cY-(Panel.SCREEN_HEIGHT/2-200))/(8.0/3));
		Color c = new Color(panel.ui.colorWheel.getRGB(panel.ui.offsetX, panel.ui.offsetY));
		int r, g, b = 0;
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		panel.setBGColor(new Color(r,g,b));
    }
    
    
    public void mouseWheelMoved(MouseWheelEvent e) {
    	speed = 1f;
	    direction = 0-e.getWheelRotation();
		ClassInformation grade = null;
		if(panel.ui.guiGrade == 2 || panel.ui.guiGrade == 3)
	    	grade = panel.ui.classBoxes.get(4).gradeList[panel.ui.listIndex];
	    
		//scrolling through "how it works" screen
	    if(panel.ui.currentScreen == 3) {
	    	movingY = panel.ui.scrollY;
	    	scrollMax = -4049;
	    	scrollMin = 0;
	    	moving = true;	

	    //scrolling through weights screen
    	}
		if(!panel.ui.highlight.equals("menu")){
			if(panel.ui.guiGrade == 2 && grade.weights.size()>2){
				movingY = grade.startWeightY;
				scrollMax = 0 - 150*grade.weights.size()+150*4;
				scrollMin = 310;

				moving = true;	
			
			//scrolling through grades screen
			}else if(panel.ui.guiGrade == 3 && grade.grades.size() > 2){
				movingY = grade.startGradeY;
				scrollMax = 0-160*grade.grades.size()+160*4;
				scrollMin = 350;

				moving = true;
			}	
		}else{
			if(grade.weights.size() > 4){
				movingY = grade.startMenuY;
				//FIX THE MAX RANGE
				int amount = grade.weights.size() > 4 ? 4 : grade.weights.size();
				amount = amount == 0 ? 1 : amount;

				scrollMax = grade.startGradeY-grade.weights.size()*70+296;
				scrollMin = grade.startGradeY;
				moving = true;
			}
			
		}
		

    	//implementation moved to panel update method
    }
    
	private boolean mouseOverTriangleArea(int mouseX, int mouseY, int x, int y, int base, int height){
		int x1 = x;
		int y1 = y;

		int x2 = x+base/2;
		int y2 = y+height;

		int x3 = x+base;
		int y3 = y;

		float areaTriangle = areaTriangle(x1, y1, x2, y2, x3, y3);

		float area1 = areaTriangle(mouseX, mouseY, x2, y2, x3, y3);
		float area2 = areaTriangle(x1, y1, mouseX, mouseY, x3, y3);
		float area3 = areaTriangle(x1, y1, x2, y2, mouseX, mouseY);
		
		return areaTriangle == area1+area2+area3;


	}
	private float areaTriangle(int x1, int y1, int x2, int y2, int x3, int y3){
		return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0f);
	}
    private boolean mouseOverBoxArea(int mouseX, int mouseY, int x, int y, int w, int h){
        if(mouseX > x && mouseX < x+w)
            if(mouseY > y && mouseY < y+h)
                return true;
        
        return false;
    }
    private boolean mouseOverCircleArea(int mouseX, int mouseY, int x, int y, int r) {
    	return (mouseX-x)*(mouseX-x)+(mouseY-y)*(mouseY-y) <= r*r;
    }
}