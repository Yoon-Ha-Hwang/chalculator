package chalculatorGame;

import java.util.ArrayList;
import java.util.Scanner;

public class Chalculator {
	/*
	 * NOTE:
	 * Numbers are a single digit, but values are not necessarily single digit.
	 * The code accepts multiple valid answers, and this is intentional. 
	 */
	
	public static ArrayList<Double> questionNumbers = new ArrayList<>(); //The arrayList containing the numbers used in the solution
	public static ArrayList<String> questionOperators = new ArrayList<>(); //The arrayList containing the operators used in the solution
	public static Double answer = 0.0; //The result of the equation, displayed to the player
	
	//Change these values for a harder/easier challenge
	//Note that there's no fear of integer overflow because there's not enough digits to multiply and get Integer.MAX_VALUE (at least, I think)
	public static int numNumbers = 5; 
	public static int numOperators = 2; 
	
	//There will be numOperators + 1 values that need to be assembled from numNumbers numbers because an operator needs to be in between two numbers
	public static Double[] questionValues = new Double[numOperators + 1]; //The array containing the values used in the equation (used internally)
	
	static Scanner read = new Scanner(System.in);
	
	public static String equation = ""; //The answer question
	
	public static void main(String args[]) {
		System.out.println("Welcome to Chalculator!\nThis is a game where you have find the equation based on the numbers and operators used as well as the final answer.");
		System.out.println("For example, if you see \"2\", \"3\", and \"+\" and the answer is 5, you type 2+3!");
		System.out.println("Note that Chalculator works without brackets and works strictly left-to-right. For example, 2+3*5 will be 25, not the usual 17.");
		System.out.println("Also, please note that you need to use all operators and numbers.");
		System.out.println("Do not add the \"equals something \" or spaces in your equation, i.e. enter 2+3 and not 2+3=5 or 2 + 3.");
		System.out.println("Also note that if you put two numbers in a row, they get combined, so a 1 next to a 4 becomes a 14.\nOrder doesn't matter, so it can become a 41.");
		while(true) {
			questionGenerator();
			System.out.println("What do you think was the original equation?"); 
			String guess = read.next();
			if(answerVerify(guess)) {
				System.out.println("WOW! That was the correct equation!");
			} else { //The answer was wrong
				System.out.println("The correct equation was " + equation + ".");
			}
			System.out.println("I will generate the next question!");
		}
	}
	
	public static void questionGenerator() {
		//Field reset
		questionNumbers.clear();
		questionOperators.clear();
		
		for(int i = 0; i < numNumbers; i++) {
			Double appendA = (double) ((int) (Math.random() * 9 + 1)); //Random integer from 1~9, not including 0s because I don't want something times 0 as the answer
			questionNumbers.add(appendA);
		}
		for(int i = 0; i < numOperators; i++) {
			String appendB = null;
			int RNG = (int)(Math.random() * 4);
			if(RNG == 0) {
				appendB = "+";
			} else if(RNG == 1) {
				appendB = "-";
			} else if(RNG == 2) {
				appendB = "*";
			} else if(RNG == 3) {
				appendB = "/";
			}
			questionOperators.add(appendB);
		}
		//Clone A is used internally to build the question. Clone B is used to display the question to the user.
		ArrayList<Double> questionNumbersCloneA = new ArrayList<>();
		ArrayList<Double> questionNumbersCloneB = new ArrayList<>();
		for(int i = 0; i < questionNumbers.size(); i++) {
			questionNumbersCloneA.add(questionNumbers.get(i));
			questionNumbersCloneB.add(questionNumbers.get(i));
		}
		ArrayList<String> questionOperatorsCloneA = new ArrayList<>();
		ArrayList<String> questionOperatorsCloneB = new ArrayList<>();
		for(int i = 0; i < questionOperators.size(); i++) {
			questionOperatorsCloneA.add(questionOperators.get(i));
			questionOperatorsCloneB.add(questionOperators.get(i));
		}
		for(int i = 0; i <= numOperators; i++) { //Ensure there's no null in any of them by giving all of them at least one value
			questionValues[i] = questionNumbersCloneA.remove(0);
		}
		while(questionNumbersCloneA.size() != 0) { //Randomly assign all remaining numbers to values
			int selectedIndex = (int)(Math.random() * (numOperators+1)); //Possible ranges: 0 to numOperators (numOperators+1 total values)
			questionValues[selectedIndex] = questionValues[(int)(selectedIndex)] * 10 + questionNumbersCloneA.remove(0); //This will squeeze in the number at the end
		}
		/* 
		 * At this point it should have assembled the necessary values using cloneA's numbers
		 * Now it's time to use the numbers to assemble the equation (and calculate the answer)
		 * Clone B, which will be displayed to the user, will have numbers arranged in ascending order
		 * The operators will be in random order
		 */
		answer = questionValues[0]; //Start with the very first value as the initial value
		equation = answer.toString(); //The correct equation. Initialise with the very first value
		for(int i = 1; i < questionValues.length; i++) { //Alternate between operator and value
			String operator = questionOperatorsCloneA.remove(0);
			equation += operator; //Append operators to the solution
			Double value = questionValues[i];
			equation += value.toString(); //Append values to the solution
			if(operator.equals("+")) {
				answer += value;
			} else if(operator.equals("-")) {
				answer -= value;
			} else if(operator.equals("*")) {
				answer *= value;
			} else { //Operator is divide (/). Note that there's no fear of division by zero since a number of zero cannot be generated in the first place
				answer /= value;
			}
		}
		//It now has an answer and the solution equation. Now I just need to present the question to the player.
		Double[] presentNumbers = new Double[numNumbers];
		String[] presentOperators = new String[numOperators];
		while(!questionNumbersCloneB.isEmpty()) { //Sort numbers
			Double minValue = (double) Integer.MAX_VALUE;
			int minIndex = Integer.MAX_VALUE;
			for(int i = 0; i < questionNumbersCloneB.size(); i++) { //Find the smallest number
				if(minValue > questionNumbersCloneB.get(i)) {
					minValue = questionNumbersCloneB.get(i);
					minIndex = i;
				}
			}
			int addTo = 0;
			while(presentNumbers[addTo] != null) { //Find a place in presentNumbers that is a zero, so we can add the minimum number there
				addTo++;
			} //By exiting the while loop, it means the spot is occupied by a zero
			presentNumbers[addTo] = questionNumbersCloneB.remove(minIndex);
		}
		while(!questionOperatorsCloneB.isEmpty()) {
			String toAdd = questionOperatorsCloneB.remove((int)(Math.random() * questionOperatorsCloneB.size()));
			int whereTo = 0;
			while(presentOperators[whereTo] != null) {
				whereTo++;
			}
			presentOperators[whereTo] = toAdd;
		}
		//Values and operators successfully randomised. Now present that to the player.
		System.out.println("\nHere is the question!");
		System.out.print("NUMBERS: ");
		for(int i = 0; i < numNumbers; i++) {
			System.out.print(presentNumbers[i].intValue());
			System.out.print(" "); //This is important to ensure that the numbers don't appear next to each other, they are genuinely separate values
		}
		System.out.print("\nOPERATORS: ");
		for(int i = 0; i < numOperators; i++) {
			System.out.print(presentOperators[i]);
			System.out.print(" ");
		}
		//Round answers to 6 decimal places
		answer = (double)((long)(answer * 100000) / 100000);
		System.out.println("\nANSWER: " + answer);
	}
	
	public static boolean answerVerify(String submission) {
		//I will first need to verify that they used only the numbers and operations provided
		ArrayList<Integer> usedNumbersList = new ArrayList<>();
		ArrayList<String> usedOperatorsList = new ArrayList<>();
		for(int i = 0; i < submission.length(); i++) { //Collect information on what they entered + verify they used correct operators only
			String detect = submission.substring(i, i+1);
			if(detect.equals("1")) {
				usedNumbersList.add(1);
			} else if(detect.equals("2")) {
				usedNumbersList.add(2);
			} else if(detect.equals("3")) {
				usedNumbersList.add(3);
			} else if(detect.equals("4")) {
				usedNumbersList.add(4);
			} else if(detect.equals("5")) {
				usedNumbersList.add(5);
			} else if(detect.equals("6")) {
				usedNumbersList.add(6);
			} else if(detect.equals("7")) {
				usedNumbersList.add(7);
			} else if(detect.equals("8")) {
				usedNumbersList.add(8);
			} else if(detect.equals("9")) {
				usedNumbersList.add(9);
			} else if(detect.equals("+")) {
				usedOperatorsList.add("+");
			} else if(detect.equals("-")) {
				usedOperatorsList.add("-");
			} else if(detect.equals("*")) {
				usedOperatorsList.add("*");
			} else if(detect.equals("/")) {
				usedOperatorsList.add("/");
			} else { //They entered something that shouldn't be entered (e.g. a letter)
				System.out.println(detect + " is not a valid operator or number.");
				return false;
			}
		}
		
		//Now I need to check if they used numbers and operators correctly
		if(usedNumbersList.size() > questionNumbers.size()) {
			System.out.println("Your equation contains too many numbers.");
			return false;
		} else if(usedNumbersList.size() < questionNumbers.size()) {
			System.out.println("Your equation contains too few numbers (remember, you need to use every number!).");
		} else { //They passed the number count check, now checking number of operators
			if(usedOperatorsList.size() > questionOperators.size()) {
				System.out.println("Your equation contains too many operators.");
				return false;
			} else if(usedOperatorsList.size() < questionOperators.size()) {
				System.out.println("Your equation contains too few operators (remember, you need to use every operator!).");
				return false;
			} else {
				//They have the correct number of numbers and operators in the equation
			}
		}
		
		//Now I need to check if they have the correct count for each (i.e. use approved numbers and operators)
		ArrayList<Integer> questionNumbersClone = new ArrayList<>();
		for(int i = 0; i < questionNumbers.size(); i++) {
			questionNumbersClone.add(questionNumbers.get(i).intValue());
		}
		for(int i = 0; i < usedNumbersList.size(); i++) {
			if(questionNumbersClone.indexOf(usedNumbersList.get(i)) != -1) { //Meaning, they were able to find
				int location = questionNumbersClone.indexOf(usedNumbersList.get(i));
				questionNumbersClone.remove(location);
			} else {
				System.out.println("Your equation contains too many " + usedNumbersList.get(i) + "s.");
				return false;
			}
		} 
		ArrayList<String> questionOperatorsClone = new ArrayList<>();
		for(int i = 0; i < questionOperators.size(); i++) {
			questionOperatorsClone.add(questionOperators.get(i));
		}
		for(int i = 0; i < usedOperatorsList.size(); i++) {
			if(questionOperatorsClone.indexOf(usedOperatorsList.get(i)) != -1) {
				int location = questionOperatorsClone.indexOf(usedOperatorsList.get(i)); //Not sure why I am allowed to define location here when I already defined it earlier
				questionOperatorsClone.remove(location);
			} else {
				System.out.println("Your equation contains too many " + usedOperatorsList.get(i) + "s.");
				return false;
			}
		}
		
		//At this point they have the correct number of operators and numbers in the equation. Now all that's needed is check the answers of their equation.
		if((submission.substring(0, 1)).equals("+") || (submission.substring(0, 1)).equals("-") || (submission.substring(0, 1)).equals("*") || (submission.substring(0, 1)).equals("/")) {
			System.out.println("Equation may not begin with an operator.");
			return false;
		}
		if((submission.substring(submission.length()-1, submission.length())).equals("+") || (submission.substring(submission.length()-1, submission.length())).equals("-") || (submission.substring(submission.length()-1, submission.length())).equals("*") || (submission.substring(submission.length()-1, submission.length())).equals("/")) {
			System.out.println("Equation may not end with an operator.");
			return false;
		}
		
		//Calculate answer
		double letsCheck = 0.0; //Answer of the user's equation
		int valueUsed = 0;
		int x = 0;
		
		//Initialise letsCheck
		while(!((submission.substring(x, x+1).equals("+")) || (submission.substring(x, x+1).equals("-")) || (submission.substring(x, x+1).equals("*")) || (submission.substring(x, x+1).equals("/")))) {
			if(x != 0) {
				valueUsed *= 10;
			}
			if((submission.substring(x, x+1)).equals("1")) {
				valueUsed += 1;
			} else if((submission.substring(x, x+1)).equals("2")) {
				valueUsed += 2;
			} else if((submission.substring(x, x+1)).equals("3")) {
				valueUsed += 3;
			} else if((submission.substring(x, x+1)).equals("4")) {
				valueUsed += 4;
			} else if((submission.substring(x, x+1)).equals("5")) {
				valueUsed += 5;
			} else if((submission.substring(x, x+1)).equals("6")) {
				valueUsed += 6;
			} else if((submission.substring(x, x+1)).equals("7")) {
				valueUsed += 7;
			} else if((submission.substring(x, x+1)).equals("8")) {
				valueUsed += 8;
			} else if((submission.substring(x, x+1)).equals("9")) {
				valueUsed += 9;
			} else { 
				//It's an operator
			}
			x++;
		}
		for(int i = x; i < submission.length(); i++) {
			if((submission.substring(i, i+1)).equals("+")) {
				letsCheck += valueUsed;
				valueUsed = 0;
			} else if((submission.substring(i, i+1)).equals("-")) {
				letsCheck -= valueUsed;
				valueUsed = 0;
			} else if((submission.substring(i, i+1)).equals("*")) {
				letsCheck *= valueUsed;
				valueUsed = 0;
			} else if((submission.substring(i, i+1)).equals("/")) { //There's no threat of division by zero since zero is not a valid number and would have been caught in an earlier filter
				letsCheck /= valueUsed;
				valueUsed = 0;
			} else { //A number was detected
				valueUsed *= 10; //Move up a digit
				if((submission.substring(i, i+1)).equals("1")) {
					valueUsed += 1;
				} else if((submission.substring(i, i+1)).equals("2")) {
					valueUsed += 2;
				} else if((submission.substring(i, i+1)).equals("3")) {
					valueUsed += 3;
				} else if((submission.substring(i, i+1)).equals("4")) {
					valueUsed += 4;
				} else if((submission.substring(i, i+1)).equals("5")) {
					valueUsed += 5;
				} else if((submission.substring(i, i+1)).equals("6")) {
					valueUsed += 6;
				} else if((submission.substring(i, i+1)).equals("7")) {
					valueUsed += 7;
				} else if((submission.substring(i, i+1)).equals("8")) {
					valueUsed += 8;
				} else if((submission.substring(i, i+1)).equals("9")) {
					valueUsed += 9;
				}
			}
		}
		
		//Now for the ultimate check: DO THEY EQUAL??? (within an error bound)
		double errorBound = 0.001;
		letsCheck = (double)((long)(letsCheck * 100000) / 100000);
		if(answer < (letsCheck - errorBound)) {
			System.out.println("Your answer is too big. It evaluates to " + letsCheck + ".");
			return false;
		} else if(answer > (letsCheck + errorBound)) {
			System.out.println("Your answer is too small. It evaluates to " + letsCheck + ".");
			return false;
		} else {
			return true;
		}
	}
}
