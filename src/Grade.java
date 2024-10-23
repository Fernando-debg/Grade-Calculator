import java.io.*;
public class Grade implements Serializable{
    /** The number representing the grade of the assignment
     * @value PERCENT MODE: "#"
     * @value MODE: "# #"
     */
    public String number;    //SAVE THIS DATA 
    public String numberPart;//SAVE THIS DATA 
    public String numberWhole;//SAVE THIS DATA 
    public String weightName; //SAVE THIS DATA 
    public String gradeName;//SAVE THIS DATA 
    public int weight;//SAVE THIS DATA 
    public char mode;//SAVE THIS DATA 

    public boolean overTrash = false;
    public boolean overName = false;
    public boolean pressedNumber = false;
    public boolean overDropMenu = false;
    /**
     *-1 = no number hovered
     * 0 = hovering over on percent mode
     * 1 = hovering over left num on P/W mode
     * 2 = hovering over right num on P/W mode
     */
    public int overNumber = -1;
    public int weightIndexTracker = -1;

    public Grade(String n, String num, String wn, int w, char m){
        gradeName = n;
        number = num;
        weightName = wn;
        weight = w;
        mode = m;
    }

    public Grade(){
        number = numberPart = numberWhole = "0";
        weightName = "No Weight Selected";
        gradeName = "N/A";
        weight = 0;
        mode = '/';
    }

    public void switchMode(){
        //clears the fields
        number = "0";
        numberPart = "0";
        numberWhole = "0";

        //actually switches the modes
        mode = mode == '/' ? '%' : '/';
        
    }

    public boolean validNumber(){
        if(!number.contains("#")){
            if(mode == '/'){
                if(overNumber == 1){
                    if(numberPart.length()==0)
                        numberPart = "0";
                    return Integer.parseInt(numberPart) < 999999;
                }
                    
                else if(overNumber == 2){
                    if(numberWhole.length() == 0)
                        numberWhole = "0";
                    return Integer.parseInt(numberWhole) < 999999;
                }
            }else if(mode == '%'){
                if(overNumber == 0){
                    if(number.length() == 0)
                        number = "0";
                    return Integer.parseInt(number) <= 999999;
                }
            }
        }else{
            return true;
        }
        
        return false;
    }

    public String correctNumber(String num){
        //checks if the number typed has any leading zeros
		boolean leadingZero = num.length() == 0 ? false : num.charAt(0) == '0';
		//erases any of the leading zeros
		for(int i = 0; i < num.length(); i++){
			if(leadingZero && num.charAt(i) != '0'){
				num = num.substring(i);
				break;
			}
		}
        return num;
    }

    public void clear(){
        weightName = "No Weight Selected";
        weight = 0;
    }
}
