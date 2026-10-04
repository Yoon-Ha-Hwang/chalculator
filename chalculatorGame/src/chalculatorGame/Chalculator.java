package chalculatorGame;

import java.util.ArrayList;
import java.util.Scanner;

public class Chalculator {
	/*
	 * NOTE:
	 * Numbers are a single digit, but values are not necessarily single digit.
	 * The code accepts multiple valid answers, and this is intentional. 
	 */
	
	public static ArrayList<Integer> questionNumbers = new ArrayList<>(); //The arrayList containing the numbers used in the solution
	public static ArrayList<String> questionOperators = new ArrayList<>(); //The arrayList containing the operators used in the solution
	
	//Change these values for a harder/easier challenge
	public static int numNumbers = 6; 
	public static int numOperators = 2; 
	
	//There will be numOperators + 1 values that need to be assembled from numNumbers numbers because an operator needs to be in between two numbers
	public static int[] questionValues = new int[numOperators + 1]; //The array containing the values used in the equation
	
	static Scanner read = new Scanner(System.in);
	
	public static String equation = ""; //The answer question
	
	public static void main(String args[]) {
		System.out.println("Welcome to Chalculator!\n This is a game where you have find the equation based on the numbers and operators used as well as the final answer.");
		System.out.println("For example, if you see \"2\", \"3\", and \"+\" and the answer is 5, you type 2+3!");
		System.out.println("Note that Chalculator works without brackets and works strictly left-to-right. For example, 2+3*5 will be 25, not the usual 17.");
		while(true) {
			questionGenerator();
			System.out.println("What do you think was the original equation?"); 
			String guess = read.next();
			if(answerVerify(guess)) {
				System.out.println("WOW! That was the correct equation!");
			} else { //The answer was wrong
				System.out.println("So close! The correct equation was actually " + equation);
			}
			System.out.println("I will generate the next question!");
		}
	}
	
	public static void questionGenerator() {
		//Field reset
		questionNumbers.clear();
		questionOperators.clear();
		
		for(int i = 0; i < numNumbers; i++) {
			Integer appendA = (int)(Math.random() * 9 + 1); //Random integer from 1~9, not including 0s because I don't want something times 0 as the answer
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
		ArrayList<Integer> questionNumbersCloneA = new ArrayList<>();
		ArrayList<Integer> questionNumbersCloneB = new ArrayList<>();
		for(int i = 0; i < questionNumbers.size(); i++) {
			questionNumbersCloneA.add(questionNumbers.get(i));
			questionNumbersCloneB.add(questionNumbers.get(i));
		}
		ArrayList<String> questionOperatorsCloneA = new ArrayList<>();
		ArrayList<String> questionOperatorsCloneB = new ArrayList<>();
		for(int i = 0; i < questionNumbers.size(); i++) {
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
		Integer answer = 0; //The result of the equation, displayed to the player
		answer = questionValues[0]; //Start with the very first value as the initial value
		equation = answer.toString(); //The correct equation. Initialise with the very first value
		for(int i = 1; i < questionValues.length; i++) { //Alternate between operator and value
			String operator = questionOperatorsCloneA.remove(0);
			equation += operator; //Append operators to the solution
			int value = questionValues[i];
			equation += operator.toString(); //Append values to the solution
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
		int[] presentNumbers = new int[numNumbers];
		String[] presentOperators = new String[numOperators];
		while(!questionNumbersCloneB.isEmpty()) { //Sort numbers
			int minValue = Integer.MAX_VALUE;
			int minIndex = Integer.MAX_VALUE;
			for(int i = 0; i < questionNumbersCloneB.size(); i++) { //Find the smallest number
				if(minValue > questionNumbersCloneB.get(i)) {
					minValue = questionNumbersCloneB.get(i);
					minIndex = i;
				}
			}
			int addTo = 0;
			while(presentNumbers[addTo] != 0) { //Find a place in presentNumbers that is a zero, so we can add the minimum number there
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
		System.out.println("NUMBERS: ");
		for(int i = 0; i < numNumbers; i++) {
			System.out.print(presentNumbers[i]);
			System.out.print(" "); //This is important to ensure that the numbers don't appear next to each other, they are genuinely separate values
		}
		System.out.println("OPERATORS: ");
		for(int i = 0; i < numOperators; i++) {
			System.out.print(presentOperators[i]);
			System.out.print(" ");
		}
		System.out.println("ANSWER: " + answer);
	}
	
	public static boolean answerVerify(String submission) {
		//To be implemented later
	}
}
