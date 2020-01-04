import java.awt.Graphics;
import java.awt.Font;
import java.util.Scanner;
import java.util.Arrays;

public class Board {
	private int[][] board;
	private int empty;

	public Board(){
		board = new int[9][9];
		empty = 9 * 9;
	}

	//Use Graphics to draw the board outline
	public void drawBoard(Graphics g){
		g.drawRect(0, 0, 450, 450);
		// vertical lines
		for(int row = 50; row < 450; row += 50){
			g.drawLine(row, 0, row, 450);
		}
		//horizontal lines
		for(int col = 50; col < 450; col += 50){
			g.drawLine(0, col, 450, col);
		}
	}
	
	public int getEmpty(){
		return empty;
	}

	// Insert user's input onto the board
	public void insertNum(Graphics g, int num, int x, int y){
		board[y - 1][x - 1] = num;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
		g.drawString("" + +num, (x - 1) * 50, y * 50);
		empty--;
	}
	
	// Checks if it is a legal move
	public boolean check(int num, int x, int y){
		//check row
		for(int c = 0; c < 9; c++){
			if(board[y - 1][c] == num){
				return false;
			}
		}
		// check col
		for(int r = 0; r < 9; r++){
			if(board[r][x - 1] == num){
				return false;
			}
		}
		//check subsquare
		return subsquare(num, x, y);
	}
	
	//check if legal move within subsquare
	private boolean subsquare(int num, int x, int y){
		if(x <= 3){
			if (y <= 3){
				return subsq(num, 0, 0);
			}else if(y <= 6){
				return subsq(num, 0, 3);
			}else{
				return subsq(num, 0, 6);
			}
		}else if(x <= 6){
			if (y <= 3){
				return subsq(num, 3, 0);
			}else if(y <= 6){
				return subsq(num, 3, 3);
			}else{
				return subsq(num, 3, 6);
			}
		}else{
			if (y <= 3){
				return subsq(num, 6, 0);
			}else if(y <= 6){
				return subsq(num, 6, 3);
			}else{
				return subsq(num, 6, 6);
			}
		}
	}
	
	// iterate within subsquare to find repetition
	private boolean subsq(int num, int row, int col) {
		for(int r = row; r < row + 3; r++){
			for(int c = col; c < col + 3; c++){
				if(board[r][c] == num){
					return false;
				}	
			}
		}
		return true;
	}

	public static void main(String[] args){
		Scanner keyboard = new Scanner(System.in);
		
		//intro 
		System.out.print("Welcome to Sudoku. Please enter your name. ");
		String playerName = keyboard.nextLine();
		DrawingPanel dp = new DrawingPanel(500,500);
		Graphics g = dp.getGraphics();
		Board b = new Board();
		b.drawBoard(g);
		
		//game
		boolean continueGame = true;
		while(continueGame){
			String prompt = "Submit the number you wish to enter. ";
			System.out.println(prompt);
			int num = getNum(keyboard, prompt);
			while(num > 9 || num < 1){
				System.out.println(prompt);
				num = getNum(keyboard, prompt);
			}
			prompt = "Enter the column you wish this number to be at. ";
			System.out.println(prompt);
			int col = getNum(keyboard, prompt);
			while(col > 9 || col < 1){
				System.out.println(prompt);
				col = getNum(keyboard, prompt);
			}
			prompt = "Enter the row you wish this number to be at. ";
			System.out.println(prompt);
			int row = getNum(keyboard, prompt);
			while(row > 9 || row < 1){
				System.out.println(prompt);
				row = getNum(keyboard, prompt);
			}
			if(!b.check(num, col, row)){
				continueGame = false;
				System.out.println("Invalid option. Game has ended. ");
			}else{
				b.insertNum(g, num, col, row);
				if(b.getEmpty() == 0){
					continueGame = false;
					System.out.println("Game complete! Congratulations");
				}
			}
			
		}
		
		
		
	}
	
	public static int getNum(Scanner key, String prompt){
		while(!key.hasNextInt()){
			String notAnInt = key.nextLine();
			System.out.println("\n" + notAnInt + " is not a number");
			System.out.println(prompt);
		}
		int res = key.nextInt();
		key.nextLine();
		return res;
	}




}
