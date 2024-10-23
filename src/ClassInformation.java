import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class ClassInformation implements Serializable {
	public String className;//SAVE THIS DATA 
	
	public String letterGrade = "#";//SAVE THIS DATA 
	public int numericalGrade = -1;//SAVE THIS DATA
	public Color color = new Color(161, 70, 255);//for grade bg color //SAVE THIS DATA
	public boolean hoveredAdd = false;
	public int hoveredMode = -1;
    public int sliderHoverIndex = -1;
	public int gradeHoverIndex = -1;
	public int menuBlockHoverIndex = -1;
	public int gradeIndex = -1;
	public Random rand = new Random();
	public ArrayList<Grade> grades = new ArrayList<Grade>();//SAVE THIS DATA
	
	
	//information for the slider
	public boolean scrollable = false;
	public int startWeightY = 350;
	public int startGradeY = 350;
	public int startMenuY = 350;
	//used to store the weights of the slider
	public LinkedList<Slidebar> weights = new LinkedList<Slidebar>();//SAVE THIS DATA
	
	
	public ClassInformation() {
		className = "N/A";
	}
	public ClassInformation(String name) {
		className = name;
		
	}
	
	//adds a weight to the slidebar and then the grades
	public void addWeight(int size) {
		//the random color of the weight circle for slider
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		Color c = new Color(r,g,b);
		if(size == 0) {
			weights.add(new Slidebar(c, 450, 625, 350));
		}else {
			weights.add(new Slidebar(c, 450, 625, weights.getLast().y+125));
		}
	}
	public void addGrade(){
		Grade grade = new Grade();
		grades.add(grade);
	}
	
	public void setNumericalGrade() {
		//where the grade is calculated to get the letter grade of the course
		double sumGrade = 0;
		double sumWeight = 0;
		 
		double[] subNumGrades = new double[weights.size()];
		double[] subDenGrades = new double[weights.size()];
		double[] subGrade = new double[weights.size()];

		double sumNum = 0;
		double sumDen = 0;

		//looks through each grade
		Grade g = null;
		for(int i = 0; i < grades.size(); i++){
			g = grades.get(i);

			if(g.weightIndexTracker != -1){
				if(g.mode == '/' && !g.numberWhole.equals("0")){
					double p = Integer.parseInt(g.numberPart);
					double w = Integer.parseInt(g.numberWhole);
					sumNum = p;
					sumDen = w;
				}else if(g.mode == '%'){
					sumNum = Integer.parseInt(g.number);
					sumDen = 100;
				}

				subNumGrades[g.weightIndexTracker] += sumNum;
				subDenGrades[g.weightIndexTracker] += sumDen;
			}
			sumNum = 0;
			sumDen = 0;
		}

		for(int i = 0; i < subGrade.length; i++){
			if(subDenGrades[i] != 0){
				subGrade[i] = subNumGrades[i]/subDenGrades[i];
				subGrade[i] = subGrade[i]*(weights.get(i).percent/100.0);
				sumWeight += weights.get(i).percent;
			}
			sumGrade += subGrade[i];			
		}
		
		numericalGrade = (int)((sumGrade/(sumWeight/100.0))*100);
	}
	
	public void update() {
		//sets the numerical grade
		if(numericalGrade >= 90) {
			color = new Color(110, 255, 130);
			if(numericalGrade >= 97) {
				letterGrade = "A+";
			}else if(numericalGrade >=94) {
				letterGrade = "A";
			}else {
				letterGrade = "A-";
			}
		}else if(numericalGrade >= 80){
			color = new Color(255, 255, 95);
			if(numericalGrade >= 87) {
				letterGrade = "B+";
			}else if(numericalGrade >=84) {
				letterGrade = "B";
			}else {
				letterGrade = "B-";
			}
			
		}else if(numericalGrade >= 70){
			color = new Color(255, 200, 111);
			if(numericalGrade >= 77) {
				letterGrade = "C+";
			}else if(numericalGrade >=74) {
				letterGrade = "C";
			}else {
				letterGrade = "C-";
			}
			
		}else if(numericalGrade >= 60){
			color = new Color(255, 114, 114);
			if(numericalGrade >= 67) {
				letterGrade = "D+";
			}else if(numericalGrade >=64) {
				letterGrade = "D";
			}else {
				letterGrade = "D-";
			}
			
		}else if(numericalGrade >=0){
			color = new Color(255, 40, 40);
			letterGrade = "F";
		}else {
			color = new Color(161, 70, 255);
			letterGrade = "#";
		}
		
        for(int i = 0; i < weights.size(); i++){
            weights.get(i).setPercent();
        }
	}

	public void drawPieChart(Graphics2D g2) {
		//draws the piechart
		int pcX = 970;
		int pcY = 540;
		int rad = 500;
		//draws slices for weights
		if(weights.size() == 0) {
			g2.setColor(Color.black);
			g2.fillOval(pcX - rad/2, pcY-rad/2, rad,rad);
			
			
		}else {
			
			//draws each part of the pie chart
			double currentAngle = 0;
			for(int i = 0; i < weights.size(); i++) {
				g2.setColor(weights.get(i).colorTop);				
				g2.fillArc(pcX - rad/2, pcY-rad/2, rad, rad, (int)currentAngle, (int)((360.0)/weights.size()));
				
				currentAngle += (360.0)/weights.size();
			}
		}
		//inner white for letter grade
		g2.setColor(Color.white);
		g2.fillOval(pcX - rad/3, pcY - rad/3, (int)(rad/1.5),(int)(rad/1.5));
		
		//writes letter grade
		g2.setColor(Color.black);
		g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 150));
		g2.drawString(letterGrade, pcX- g2.getFontMetrics().stringWidth(letterGrade)/2, pcY+g2.getFontMetrics().getHeight()/5);
		
	}
	public void drawSliders(Graphics2D g2) {
		//draws the add sign
		if(hoveredAdd) {
			g2.setColor(new Color(217,217,217));
		}else {
			g2.setColor(new Color(150,150,150));
		}
		if(weights.size() == 0 && (startWeightY > 250 && startWeightY < 800)) {
			g2.fillRect(925, startWeightY, 50, 50);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(5));
			g2.drawLine(950, startWeightY+10, 950, startWeightY+40);
			g2.drawLine(935, startWeightY+25, 965, startWeightY+25);
		}else {
			int sy = weights.getLast().y+150;
			if(sy > 250 && sy < 800) {
				g2.fillRect(925, sy, 50, 50);
				g2.setColor(Color.black);
				g2.setStroke(new BasicStroke(5));
				g2.drawLine(950, sy+10, 950, sy+40);
				g2.drawLine(935, sy+25, 965, sy+25);
			}
			
		}
		
		//draws each individual slide bar
		for(int i = 0; i < weights.size(); i++) {
			weights.get(i).y = startWeightY + 150*i;
			if(weights.get(i).y > 250 && weights.get(i).y < 750){
				weights.get(i).draw(g2);
				g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
				g2.drawString((i+1)+"/"+weights.size(), 837,weights.get(i).y+75);
			
			}
		}
	}
	public void drawGrades(Graphics2D g2, Color color){
		
		//draws the add sign
		if(hoveredAdd) {
			g2.setColor(new Color(217,217,217));
		}else {
			g2.setColor(new Color(150,150,150));
		}
		if(grades.size() == 0 && (startGradeY > 250 && startGradeY < 750)) {
			g2.fillRect(925, startGradeY, 50, 50);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(5));
			g2.drawLine(950, startGradeY+10, 950, startGradeY+40);
			g2.drawLine(935, startGradeY+25, 965, startGradeY+25);
		}else {
			int sy = startGradeY+160*grades.size();
			if(sy > 250 && sy < 750) {
				g2.fillRect(925, sy, 50, 50);
				g2.setColor(Color.black);
				g2.setStroke(new BasicStroke(5));
				g2.drawLine(950, sy+10, 950, sy+40);
				g2.drawLine(935, sy+25, 965, sy+25);
			}
		}
		
		//draws the grade information format
		for(int i = 0; i < grades.size(); i++){
			int sy = startGradeY+160*i;
			if(sy > 250 && sy < 750){
				gradeFormat(g2, color, grades.get(i), 160*i);

				//highlights the circle of the mode button if being hovered over by mouse
				if(hoveredMode == i){
					g2.setColor(new Color(255,255,255, 150));
					if(grades.get(i).mode == '/')
						g2.fillOval(509, startGradeY+40+4+160*i, 42, 42);
					else if(grades.get(i).mode == '%')
						g2.fillOval(548, startGradeY+40+4+160*i, 42, 42);
				}
				g2.setColor(Color.black);
				g2.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
				g2.drawString((i+1)+"/"+grades.size(),490+15+5+240, startGradeY+40+4+160*i+75);
			}
		}
	
	}
	public void gradeFormat(Graphics2D g2, Color bgcolor, Grade grade, int space){
		
		//test UI for a single grade entry
		int rX = 490;
		int rY = startGradeY-150+space;
		int rWidth = 960;
		int rHeight = 680;
		
		//upper and lower bound for visibility of grade text
		g2.setColor(Color.black);
		//g2.drawLine(rX, rY+110, rX+rWidth, rY+110);
		//g2.drawLine(rX, rY+580, rX+rWidth, rY+580);

		//g2.drawLine(rX+50, rY+110, rX+50, rY+110+200);

		//drawing the background options button for part/whole and percent 
		g2.setColor(Color.black);
		g2.fillOval(rX+15, rY+190, 50, 50);
		g2.fillOval(rX+15+40, rY+190, 50, 50);
		g2.fillRect(rX+15+30, rY+191, 40, 49);
		
		//the circle for switching modes
		g2.setColor(bgcolor);
		if(grade.mode == '/'){
			g2.fillOval(rX+15+4, rY+190+4, 42, 42);//LEFT - PART/WHOLE
			
			//the borders for the part/whole numbers
			//left
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(rX+15+85+15, rY+165+20-1, 130, 60);
			g2.setColor(Color.white);
			g2.fillRect(rX+15+85+10, rY+165+20+9-1, 150, 60-17);
			//highlights the left box if mouse is over it
			if(grade.overNumber == 1){
				g2.setColor(bgcolor);
				g2.fillRect(rX+15+85+15+3, rY+165+20-1+3, 130-6, 60-6);
				g2.setColor(new Color(255,255,255,200));
				g2.fillRect(rX+15+85+15+3, rY+165+20-1+3, 130-6, 60-6);
			}

			//right
			g2.setColor(Color.black);
			g2.drawRect(rX+15+85+15+190, rY+165+20-1, 130, 60);
			g2.setColor(Color.white);
			g2.fillRect(rX+15+85+10+190, rY+165+20+9-1, 150, 60-17);
			//highlights the right box if mouse is over it
			if(grade.overNumber == 2){
				g2.setColor(bgcolor);
				g2.fillRect(rX+15+85+15+190+3, rY+165+20-1+3, 130-6, 60-6);
				g2.setColor(new Color(255,255,255,200));
				g2.fillRect(rX+15+85+15+190+3, rY+165+20-1+3, 130-6, 60-6);
			}

			//draws the dividing line for part / whole
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(6));
			g2.drawLine(rX+15+85+15+130+20, rY+165+20+9-1+60-17, rX+15+85+15+130+10+30, rY+165+20+9-1);

			//draws the numbers for part/whole
			g2.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
			String num1, num2;
			if(grade.number.equals("#")){
				num1 = "#";
				num2 = "#";
			}else{
				num1 = grade.numberPart;
				num2 = grade.numberWhole;
			}
			g2.drawString(num1, rX+15+85+15+130/2-(stringWidth(g2, num1)/2), rY+165+20-1+43);
			g2.drawString(num2, rX+15+85+15+190+130/2-(stringWidth(g2, num2)/2), rY+165+20-1+43);

			//draws the name of which mode it is in
			g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
			g2.drawString("Part/Whole",rX+15+5, rY+190+50+20);
			

		}else if(grade.mode == '%'){
			g2.fillOval(rX+15+4+39, rY+190+4, 42, 42); //RIGHT - PERCENT

			//the black box with the number inside it
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(rX+15+85+15+95, rY+165+20-1, 130, 60);
			g2.setColor(Color.white);
			g2.fillRect(rX+15+85+10+95, rY+165+20+9-1, 150, 60-17);
			if(grade.overNumber == 0){
				g2.setColor(bgcolor);
				g2.fillRect(rX+15+85+15+95+3, rY+165+20-1+3, 130-6, 60-6);
				g2.setColor(new Color(255,255,255,200));
				g2.fillRect(rX+15+85+15+95+3, rY+165+20-1+3, 130-6, 60-6);
			}

			//the big percent sign
			g2.setColor(Color.black);
			g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
			g2.drawString("%", rX+15+85+15+95+130+25, rY+165+20-1+50);

			//draws the numbers for part/whole
			g2.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
			if(grade.number.equals("#")){
				g2.drawString("#", rX+15+85+15+95+130/2-(stringWidth(g2, "#")/2), rY+165+20-1+43);
			}else{
				g2.drawString(grade.number, rX+15+85+15+95+130/2-(stringWidth(g2, grade.number)/2), rY+165+20-1+43);
			}
			

			//draws the name of which mode it is in
			g2.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
			g2.drawString("Percent",rX+15+18, rY+190+50+20);
		}

		//the name of the grade assignments
		g2.setColor(Color.black);
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
		g2.drawString(grade.gradeName, rX+15+85+15, rY+165);
		if(grade.overName){
			g2.setStroke(new BasicStroke(2));
			g2.setColor(bgcolor);
			g2.drawRect(rX+15+85+10, rY+165-36, 350,40);
		}

		//draws the dotted line 
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(5));
		for(int i = 0; i < 6; i++){
			g2.drawLine(rX+15+85+15+190+130+35, rY+130+(6*i*4), rX+15+85+15+190+130+35, rY+130+6+(6*i*4));
		}
		g2.setStroke(new BasicStroke(1));

		//draws the drop down menu box
		g2.fillRect(rX+15+85+15+190+130+35+15, rY+130+20, 450, 90);
		g2.setColor(new Color(240,240,240));
		g2.fillRect(rX+15+85+15+190+130+35+15+5, rY+130+20+5, 450-10, 90-10);
		g2.setColor(Color.white);
		g2.fillRect(rX+15+85+15+190+130+35+15+5+10, rY+130+20+5+10, 450-10-20, 90-10-20);
				

		//draws string for the percent weightage of the grade
		g2.setColor(Color.black);
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		g2.drawString(grade.weight+"%", rX+15+85+15+190+130+35+35, rY+165+20+9-1+60-17-12-20);

		//draws string for the name of the weight of the grade
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		g2.drawString(grade.weightName, rX+15+85+15+190+130+35+35+85, rY+165+20+9-1+60-17-14-20);

		//draws which grade out of the total it is
		g2.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		//g2.drawString(grade.weightName, rX+15+85+15+190+130+35+35+85, rY+165+20+9-1+60-17-14-20);
		
		//draws the drop down arrow
		int[] xp = {rX+15+85+15+190+130+35+15+200, rX+15+85+15+190+130+35+15+250, rX+15+85+15+190+130+35+15+225};
		int[] yp = {rY+130+20+90, rY+130+20+90, rY+130+20+90+25};
		g2.fillPolygon(new Polygon(xp, yp, 3));

		//highlight of the drop down if hovered
		g2.setColor(new Color(240,240,240));
		int[] xp2 = {rX+15+85+15+190+130+35+15+200, rX+15+85+15+190+130+35+15+250, rX+15+85+15+190+130+35+15+225};
		int[] yp2 = {rY+130+20+90-5-2, rY+130+20+90-5-2, rY+130+20+90+20-2};
		if(grade.overDropMenu)
			g2.fillPolygon(new Polygon(xp2, yp2, 3));
	}

	public int stringWidth(Graphics2D g2, String str){
		return g2.getFontMetrics().stringWidth(str);
	}
}
