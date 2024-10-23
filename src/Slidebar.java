import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Slidebar implements Serializable{
    public boolean hoveredSlider = false;
    public boolean hoveredName = false;
	public boolean overTrash = false;
	public int percent;//SAVE THIS DATA
	public Color colorTop;//SAVE THIS DATA
	public int boxWidth;//SAVE THIS DATA
	public int lineWidth;//SAVE THIS DATA
	public String name;//SAVE THIS DATA
	
    
	public int x, y;//relative to the round rect //SAVE THIS DATA 
    public int sliderX;//SAVE THIS DATA
	
	public Slidebar(Color c, int width, int x, int y) {
		percent = 50;
		colorTop = c;
		boxWidth = width;
		lineWidth = boxWidth - 40;
		name = "N/A";
		this.x = x;
		this.y = y;
        sliderX = x+20+(int)(lineWidth*((double)percent*.01))-10;

	}
	public Slidebar(){
		//nothing goes in here, just for when loading up data
	}
	public void setPercent() {
        double p = ((double)sliderX-645+12)/410;
		if(p > 9.5)
            p = .1;
        percent = (int)(p * 100);
        
        
	}
	
	public void draw(Graphics2D g2) {
        //645 - 1055
        //System.out.println((x+20 )+" " +( x+20+lineWidth) );
       
		//draws the gray background for the slidebar
		g2.setColor(new Color(115,115,115));
		g2.fillRoundRect(x, y, boxWidth, 50, 50, 50);

        //draws the light gray lines
		g2.setColor(new Color(217,217,217));
		g2.setStroke(new BasicStroke(7));
		g2.drawLine(x+20, y+15, x+20, y+35); //left line
		g2.drawLine(x+20, y+25, x+20+lineWidth, y+25);//horiz line 
		g2.drawLine(x+20+lineWidth, y+15, x+20+lineWidth, y+35);//right line
		g2.setStroke(new BasicStroke(1));
		
		//draws the colored lines
		g2.setColor(colorTop);
		g2.setStroke(new BasicStroke(7));
		g2.drawLine(x+20, y+15, x+20, y+35); //left line
		g2.drawLine(x+20, y+25, ((x+20+lineWidth)-((x+20+lineWidth)-sliderX))+10, y+25);//horiz line
		if(percent == 100) {
			g2.drawLine(x+20+lineWidth, y+15, x+20+lineWidth, y+35);//right line
		}
       
		//draws the circle for slidebar
		g2.fillOval(sliderX, y+13, 25, 25);
		g2.setColor(Color.black);     
        g2.setStroke(new BasicStroke(3));
		g2.drawOval(sliderX, y+13, 25, 25);
		g2.setStroke(new BasicStroke(1));

		//highlights the circle if mouse is hovered over the circle
        if(hoveredSlider){
            g2.setColor(new Color(255,255,255,120));
            g2.fillOval(sliderX, y+13, 25, 25);
        }
		
		//draws percent and name of weight
		g2.setColor(Color.black);
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		g2.drawString(percent+"%", x-75, y+35);
		g2.drawString(name, x+boxWidth+25, y+35);

		//draws a box around the name of the weight being editied
        if(hoveredName){
            g2.drawRect(x+boxWidth+20, y+10, 40+10+260, 20+10);
        }
	}
}
