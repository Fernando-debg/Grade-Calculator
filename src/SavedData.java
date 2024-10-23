import java.awt.*;
import java.io.*;
import java.util.*;

public class SavedData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8767937047180573819L;
	/*
	 * UI saved components
	*/
	public ArrayList<ArrayList<TextBox>> boxes = new ArrayList<ArrayList<TextBox>>();
	
	public Color bgcolor;
	public int darkRGB;
	public int cX, cY, cR;
	public int offsetX, offsetY;
	public int sliderX, sliderY;
	public int bgCX;
	public int bgCY;	

	public void setUIComponents(UI other) {
		boxes.add(other.homeBoxes);
		boxes.add(other.classBoxes);
		boxes.add(other.optionsBoxes);
		boxes.add(other.howToUseBoxes);

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
	public UI getUIComponents(Panel panel) {
		UI newUI = new UI(panel);
		newUI.homeBoxes = boxes.get(0);//getTextBoxData(boxes.get(0));
		newUI.classBoxes = boxes.get(1);//getTextBoxData(boxes.get(1));
		newUI.optionsBoxes = boxes.get(2);//getTextBoxData(boxes.get(2));
		newUI.howToUseBoxes = boxes.get(3);//getTextBoxData(boxes.get(3));

		newUI.bgCX = bgCX;
		newUI.bgCY = bgCY;
		newUI.bgcolor = bgcolor;
		newUI.darkRGB = darkRGB;
		newUI.cX = cX;
		newUI.cY = cY;
		newUI.cR = cR;
		newUI.offsetX = offsetX;
		newUI.offsetY = offsetY;
		newUI.sliderX = sliderX;
		newUI.sliderY = sliderY;
		
		return newUI;
	}

}
