import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class KeyInput implements Serializable, KeyListener{
	Panel panel;
	public boolean shift = false;
	public boolean shiftReleased = true;
	
	public KeyInput(Panel p) {
		this.panel = p;
	}
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void keyPressed(KeyEvent e) {
    	int code = e.getKeyCode();
    	String name = "";
    	
    	shift = false;
    	if(code == KeyEvent.VK_SHIFT) {
    		shift = true;
    		shiftReleased = false;
    		panel.ui.scrollSpeed = 100;
    	}
		//if(panel.getInfo().sliderHoverIndex != -1 || panel.getInfo().gradeHoverIndex != -1)
			
    	//System.out.println(!panel.ui.highlight.equals(".") +" "+ shiftReleased);
    	if(!panel.ui.highlight.equals(".") && panel.ui.changing) {
			//System.out.println("changing");
    		name = getNameType();
    		String s = "";
    		if(panel.ui.typable) {
    			if(!panel.ui.ifGradeNumberEditing()){
					if(shiftReleased) {
					
						switch(code) {
							case KeyEvent.VK_A : s = "a"; break;
							case KeyEvent.VK_B : s = "b"; break;
							case KeyEvent.VK_C : s = "c"; break;
							case KeyEvent.VK_D : s = "d"; break;
							case KeyEvent.VK_E : s = "e"; break;
							case KeyEvent.VK_F : s = "f"; break;
							case KeyEvent.VK_G : s = "g"; break;
							case KeyEvent.VK_H : s = "h"; break;
							case KeyEvent.VK_I : s = "i"; break;
							case KeyEvent.VK_J : s = "j"; break;
							case KeyEvent.VK_K : s = "k"; break;
							case KeyEvent.VK_L : s = "l"; break;
							case KeyEvent.VK_M : s = "m"; break;
							case KeyEvent.VK_N : s = "n"; break;
							case KeyEvent.VK_O : s = "o"; break;
							case KeyEvent.VK_P : s = "p"; break;
							case KeyEvent.VK_Q : s = "q"; break;
							case KeyEvent.VK_R : s = "r"; break;
							case KeyEvent.VK_S : s = "s"; break;
							case KeyEvent.VK_T : s = "t"; break;
							case KeyEvent.VK_U : s = "u"; break;
							case KeyEvent.VK_V : s = "v"; break;
							case KeyEvent.VK_W : s = "w"; break;
							case KeyEvent.VK_X : s = "x"; break;
							case KeyEvent.VK_Y : s = "y"; break;
							case KeyEvent.VK_Z : s = "z"; break;
								
							case KeyEvent.VK_0 : s = "0"; break;
							case KeyEvent.VK_1 : s = "1"; break;
							case KeyEvent.VK_2 : s = "2"; break;
							case KeyEvent.VK_3 : s = "3"; break;
							case KeyEvent.VK_4 : s = "4"; break;
							case KeyEvent.VK_5 : s = "5"; break;
							case KeyEvent.VK_6 : s = "6"; break;
							case KeyEvent.VK_7 : s = "7"; break;
							case KeyEvent.VK_8 : s = "8"; break;
							case KeyEvent.VK_9 : s = "9"; break;
								
							case KeyEvent.VK_BACK_QUOTE : s = "`"; break;
							case KeyEvent.VK_MINUS : s = "-"; break;
							case KeyEvent.VK_EQUALS : s = "="; break;
							case KeyEvent.VK_OPEN_BRACKET : s = "["; break;
							case KeyEvent.VK_CLOSE_BRACKET : s = "]"; break;
							case KeyEvent.VK_BACK_SLASH : s = "\\"; break;
							case KeyEvent.VK_SEMICOLON : s = ";"; break;
							case KeyEvent.VK_QUOTE : s = "'"; break;
							case KeyEvent.VK_COMMA : s = ","; break;
							case KeyEvent.VK_PERIOD : s = "."; break;
							case KeyEvent.VK_SLASH : s = "/"; break;
														
							case KeyEvent.VK_SPACE : s = " "; break;
							case KeyEvent.VK_BACK_SPACE : if(name.length()>0) {s = name.substring(0,name.length()-1); name = "";} break;
							case KeyEvent.VK_ENTER : if(name.length()==0) {name = "N/A";} panel.ui.changing = false; panel.ui.highlight = "."; break;
						}
					}else {
						switch(code) {
							case KeyEvent.VK_A : s = "A"; break;
							case KeyEvent.VK_B : s = "B"; break;
							case KeyEvent.VK_C : s = "C"; break;
							case KeyEvent.VK_D : s = "D"; break;
							case KeyEvent.VK_E : s = "E"; break;
							case KeyEvent.VK_F : s = "F"; break;
							case KeyEvent.VK_G : s = "G"; break;
							case KeyEvent.VK_H : s = "H"; break;
							case KeyEvent.VK_I : s = "I"; break;
							case KeyEvent.VK_J : s = "J"; break;
							case KeyEvent.VK_K : s = "K"; break;
							case KeyEvent.VK_L : s = "L"; break;
							case KeyEvent.VK_M : s = "M"; break;
							case KeyEvent.VK_N : s = "N"; break;
							case KeyEvent.VK_O : s = "O"; break;
							case KeyEvent.VK_P : s = "P"; break;
							case KeyEvent.VK_Q : s = "Q"; break;
							case KeyEvent.VK_R : s = "R"; break;
							case KeyEvent.VK_S : s = "S"; break;
							case KeyEvent.VK_T : s = "T"; break;
							case KeyEvent.VK_U : s = "U"; break;
							case KeyEvent.VK_V : s = "V"; break;
							case KeyEvent.VK_W : s = "W"; break;
							case KeyEvent.VK_X : s = "X"; break;
							case KeyEvent.VK_Y : s = "Y"; break;
							case KeyEvent.VK_Z : s = "Z"; break;
							
							case KeyEvent.VK_0 : s = ")"; break;
							case KeyEvent.VK_1 : s = "!"; break;
							case KeyEvent.VK_2 : s = "@"; break;
							case KeyEvent.VK_3 : s = "#"; break;
							case KeyEvent.VK_4 : s = "$"; break;
							case KeyEvent.VK_5 : s = "%"; break;
							case KeyEvent.VK_6 : s = "^"; break;
							case KeyEvent.VK_7 : s = "&"; break;
							case KeyEvent.VK_8 : s = "*"; break;
							case KeyEvent.VK_9 : s = "("; break;
							
							case KeyEvent.VK_BACK_QUOTE : s = "~"; break;
							case KeyEvent.VK_MINUS : s = "_"; break;
							case KeyEvent.VK_EQUALS : s = "+"; break;
							case KeyEvent.VK_OPEN_BRACKET : s = "{"; break;
							case KeyEvent.VK_CLOSE_BRACKET : s = "}"; break;
							case KeyEvent.VK_BACK_SLASH : s = "|"; break;
							case KeyEvent.VK_SEMICOLON : s = ":"; break;
							case KeyEvent.VK_QUOTE : s = "\""; break;
							case KeyEvent.VK_COMMA : s = "<"; break;
							case KeyEvent.VK_PERIOD : s = ">"; break;
							case KeyEvent.VK_SLASH : s = "?"; break;
							
							case KeyEvent.VK_SPACE : s = " "; break;
							case KeyEvent.VK_BACK_SPACE : if(name.length()>0) {s = name.substring(0,name.length()-1); name = "";} break;
							case KeyEvent.VK_ENTER : if(name.length()==0) {name = "N/A";} panel.ui.changing = false; panel.ui.highlight = "."; break;
						}
					}
				}else{
					//TODO: WRITE THE 0-9 CODE SWITCH FOR ENTERING NUMBERS FOR GRADE SPACE
					
					switch(code){
						case KeyEvent.VK_0 : s = "0"; break;
						case KeyEvent.VK_1 : s = "1"; break;
						case KeyEvent.VK_2 : s = "2"; break;
						case KeyEvent.VK_3 : s = "3"; break;
						case KeyEvent.VK_4 : s = "4"; break;
						case KeyEvent.VK_5 : s = "5"; break;
						case KeyEvent.VK_6 : s = "6"; break;
						case KeyEvent.VK_7 : s = "7"; break;
						case KeyEvent.VK_8 : s = "8"; break;
						case KeyEvent.VK_9 : s = "9"; break;

						case KeyEvent.VK_BACK_SPACE : if(name.length()>0) {s = name.substring(0,name.length()-1); name = "";} break;
						case KeyEvent.VK_ENTER : if(name.length()==0) {name = "0";} panel.ui.changing = false; panel.ui.highlight = "."; break;
					}	
					
				}
	    		
				
    		}

			//sets the name equal to whatever it was editing
    		switch(panel.ui.highlight){
				case "name" : 
					panel.getInfo().className = name+s;
					break;
				case "weight" : 
					panel.getInfo().weights.get(panel.getInfo().sliderHoverIndex).name = name+s;
					break;
				case "percent" :
					panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex).number = name+s;
					break;
				case "part" :
					panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex).numberPart = name+s;
					break;
				case "whole" :
					panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex).numberWhole = name+s;
					break;
				case "grade" :
					panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex).gradeName = name+s;
					break;
			}
		}
    	
    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_SHIFT) {
    		shift = false;
    		shiftReleased = true;
    		panel.ui.scrollSpeed = 50;
    	}
        
		//########### DEBUG ############
        if(code == KeyEvent.VK_ESCAPE) {
        	//closes program
        	System.exit(0);
        }

        
    }
    
	/**
	 * @returns the name of whatever is being highlighted, "." 
	 * if nothing is being highlighted
	 */
	private String getNameType(){
		switch(panel.ui.highlight){
			case "name" : 
				return panel.ui.classBoxes.get(4).gradeList[panel.ui.listIndex].className;
			case "weight" : 
				Slidebar sb = panel.getInfo().weights.get(panel.getInfo().sliderHoverIndex);
				return sb.name;

			case "percent" :
				Grade g = panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex);
				return g.number;

			case "part" :
				Grade gr = panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex);
				return gr.numberPart;

			case "whole" :
				Grade g2 = panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex);
				return g2.numberWhole;
			
			case "grade" :
				Grade grr = panel.getInfo().grades.get(panel.getInfo().gradeHoverIndex);
				return grr.gradeName;
		}
		return ".";
	}
}
