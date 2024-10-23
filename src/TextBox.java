import java.awt.*;
import java.io.*;
import java.util.*;
public class TextBox implements Serializable{
	
	public String text; //SAVE THIS DATA 
	public ClassInformation[] gradeList = new ClassInformation[7]; //SAVE THIS DATA 
	
	public boolean round;//SAVE THIS DATA 
	public int x, y, textWidth, textHeight;//SAVE THIS DATA 
	public Color boxColor;//SAVE THIS DATA 
	public Font font;//SAVE THIS DATA 
	public FontMetrics fm;//SAVE THIS DATA 
	public Panel p;//SAVE THIS DATA 
	
	public int boxX, boxY, boxWidth, boxHeight;//SAVE THIS DATA 
	public boolean button, hovered;
	
	public TextBox(Panel p, String t, boolean r, boolean b, int x, int y, Font f, Color c) {
		this.p = p;
		for(int i = 0; i < 7; i++) {
			gradeList[i] = new ClassInformation();
		}
		text = t;
		round = r;
		hovered = false;
		button = b;
		this.x = x;
		this.y = y;
		textWidth = 0;
		textHeight = 0;
		boxColor = c;
		font = f;
		
	}
	public TextBox(Panel p, boolean r, int x, int y, Font f, Color c) {
		this.p = p;
		gradeList = new ClassInformation[7];
		for(int i = 0; i < 7; i++) {
			gradeList[i] = new ClassInformation();
		}		
		
		round = r;
		hovered = false;
		button = false;
		this.x = x;
		this.y = y;
		textWidth = 0;
		textHeight = 0;
		boxColor = c;
		font = f;
		
	}
	public TextBox(){
		//nothing goes in here, just for when loading up data
		for(int i = 0; i < 7; i++) {
			gradeList[i] = new ClassInformation();
		}	
		font = new Font("Comic Sans MS", Font.PLAIN, 80);
		text = "";
		
	}
	
	public void drawTextBox(Graphics2D g2) {
		fm = g2.getFontMetrics(font);
		textWidth = fm.stringWidth(text);
		textHeight = fm.getHeight();
		
		int percent = 25;
		int spacingX = (int)(textWidth*(percent*.005));
		int spacingY = (int)(textHeight*(percent*.01));
		boxX = x-spacingX;
		boxY = y-spacingY;
		boxWidth = textWidth+spacingX*2;
		boxHeight = textHeight+spacingY*2;
		
		g2.setColor(boxColor);
		if(round) {
			g2.fillRoundRect(boxX,boxY,boxWidth,boxHeight, 100, 100);
		}else {
			g2.fillRect(boxX,boxY,boxWidth,boxHeight);
		}
		
		if(hovered) {
			Color c = p.savedData.bgcolor;
			g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(),100));
			g2.fillRect(boxX,boxY,boxWidth,boxHeight);
		}
		g2.setColor(Color.black);
		g2.setFont(font);
		g2.drawString(text,x,y+3*textHeight/4);
		
		
		//~~~~~~~~~ debugging ~~~~~~~~~
		/*
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
		g2.drawString("width: "+textWidth +"   height: "+textHeight+"   centerX: "+textWidth/2, x, y);
		*/
	}

	public void drawTextListBox(Graphics2D g2) {
		fm = g2.getFontMetrics(font);

		textWidth = 135;
		textHeight = fm.getHeight();
		int percent = 45;
		int spacingX = (int)(textWidth*(percent*.005));
		int spacingY = (int)(textHeight*(percent*.01));
		boxX = x-spacingX;
		boxY = y-spacingY;
		boxWidth = textWidth+spacingX*2;
		boxHeight = textHeight+spacingY*2;
		
		//left text box with names
		g2.setColor(boxColor);
		g2.fillRect(120, 223, 195, 72*7);
		
		//right text box with grades
		g2.setColor(new Color(217,217,217));
		g2.fillRect(315, boxY+10, 155, boxHeight*7-20);
				
		//grades
		for(int i = 0; i < 7; i++) {
			gradeList[i].update();
			//the color depending on the grade
			if(gradeList[i].color == new Color(161, 70, 255)) {
				g2.setColor(new Color(161, 70, 255));
			}else {
				g2.setColor(gradeList[i].color);
			}
			g2.fillRoundRect(329, 236+72*i, 125, 45, 40, 40);
			
			//writes the name of the class
			g2.setColor(Color.black);
			String name = gradeList[i].className;
			textWidth = fm.stringWidth(name);
			if(textWidth > 135) {
				textWidth = 135;
				name = name.substring(0,5)+"...";
				textWidth = fm.stringWidth(name);
			}
			g2.drawString(name, (boxX+(boxWidth/2))-(textWidth/2), (y+spacingY+10)+72*i);
			
			//writes the grade
			String grade = "";
			if(gradeList[i].numericalGrade == -1)
				grade = "###";
			else
				grade = gradeList[i].numericalGrade+"";
				textWidth = fm.stringWidth(grade);
			g2.drawString(grade, (315+(155/2))-(textWidth/2), (y+spacingY+12)+72*i);
			
			
			
		}	
		//~~~~~~~~~ debugging ~~~~~~~~~
		/*	
		g2.setColor((Color.black));
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
		g2.drawString("width: "+boxWidth+"   height: "+boxHeight, x, y);
		*/
			
	}
}
