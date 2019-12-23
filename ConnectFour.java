import java.util.Scanner;
/**
 * CS312 Assignment 10.
 *
 * On my honor, <Min Ho Lee>, this programming assignment is my own work and I have
 * not shared my solution with any other student in the class.
 *
 *  email address:mlee7555@utexas.edu
 *  UTEID:ml45895
 *  Section 5 digit ID:51460
 *  Grader name:Bri
 *  Number of slip days used on this assignment:2
 *
 * Program that allows two people to play Connect Four.
 */

public class ConnectFour {
    public static final int NUM_ROWS = 6;
    public static final int NUM_COL = 7;
    public static final int ZERO_INDEX = -1;
    public static final int DUMMY_EDGE = 3;

    public static void main(String[] args) {
        intro();
        Scanner keyboard = new Scanner(System.in);
        game(keyboard);
    }
    
    //main game which loops until a player wins or is a draw
    public static void game(Scanner keyboard){
        final int player1 = 1;
        final int player2 = 2; 
        final char p1Checker = 'r';
        final char p2Checker = 'b';
        final String name1 = getNames(keyboard, player1);
        final String name2 = getNames(keyboard, player2);
        char[][] board = initialBoard();
        String stage = "Current";
        printBoard(board, stage);
        boolean gameWin = false;
        while(!gameWin){
            gameWin = playerTurn(keyboard, board, name1, p1Checker);
            if(!gameWin){
                printBoard(board, stage);
                gameWin = playerTurn(keyboard, board, name2, p2Checker);
                if(gameWin){
                    stage = "Final";
                    System.out.println(name2 + " wins!!\n");
                }
                if(gameDraw(board)){
                    stage = "Final";
                    System.out.println("The game is a draw.\n");
                    gameWin = true;
                }
                printBoard(board, stage);
            }else {
                stage = "Final";
                System.out.println(name1 + " wins!!\n");
                printBoard(board, stage);
            } 
        }
    }
    
    //returns name of player 
    public static String getNames(Scanner keyboard, int playerNum){
        System.out.print("Player " + playerNum+ " enter your name: ");
        String name = keyboard.nextLine();
        System.out.println();
        return name;
    }
    
    //returns 2D array of the initial board with a dummy zone around the edges 
    public static char[][] initialBoard(){
        final int DUMMY_ROWS = NUM_ROWS + DUMMY_EDGE + DUMMY_EDGE;
        final int DUMMY_COL = NUM_COL + DUMMY_EDGE + DUMMY_EDGE;
        char[][] initialBoard = new char[DUMMY_ROWS][DUMMY_COL];
        for(int r = 0; r < DUMMY_ROWS; r++){
            for(int c = 0; c < DUMMY_COL; c++){
                initialBoard[r][c] = '.';
            }
        }
        return initialBoard;
    }
    
    //returns true if a player's turn resulted in a winning move 
    public static boolean playerTurn(Scanner keyboard, char[][] board, String name, char checker){
        String prompt = name + ", enter the column to drop your checker: ";
        turnPrompt(name, checker);
        int playerCol = validUserInput(keyboard, board, name, prompt);
        playerCol += DUMMY_EDGE + ZERO_INDEX;
        int playerRow = dropChecker(board, checker, playerCol);
        return gameWin(board, checker, playerRow, playerCol);
    }

    //prints the board excluding the dummy areas 
    public static void printBoard(char[][] board, String stage){
        System.out.println(stage + " Board");
        System.out.println("1 2 3 4 5 6 7  column numbers");
        for(int r = DUMMY_EDGE; r < NUM_ROWS + DUMMY_EDGE; r++){
            for(int c = DUMMY_EDGE; c < NUM_COL + DUMMY_EDGE; c++){
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    //drops user's checker if user enters a valid column, and returns index of row
    public static int dropChecker(char[][] board, char checker, int arrayCol){
        int r = NUM_ROWS + ZERO_INDEX + DUMMY_EDGE;;
        while(board[r][arrayCol] != '.'){
            r--;
        }
        board[r][arrayCol] = checker;
        return r++;
    }

    //prints the prompt when it's a player's turn
    public static void turnPrompt(String name, char checker){
        System.out.println(name + " it is your turn.");
        System.out.println("Your pieces are the " + checker + "'s.");
        System.out.print(name + ", enter the column to drop your checker: ");
    }

    //returns a valid user input - within 1 to 7, and column is not full.
    public static int validUserInput(Scanner keyboard, char[][] board, String name, String prompt){
        //initializing userCol to a number
        int userCol = 0;
        boolean outOfRange = true; 
        boolean fullColumn = true;
        while(outOfRange || fullColumn){
            userCol = getInt(keyboard, prompt);
            System.out.println();
            outOfRange = userCol > 7 || userCol < 1;
            if(outOfRange){
                System.out.println(userCol + " is not a valid column.");
                System.out.print(prompt);
            } else if(!outOfRange){
                int arrayCol = userCol + ZERO_INDEX + DUMMY_EDGE;
                int topRow =DUMMY_EDGE;
                fullColumn = board[topRow][arrayCol] != '.';
                if(fullColumn){
                    System.out.println(userCol + " is not a legal column. That column is full");
                    System.out.print(prompt);
                }
            }
        }
        return userCol;
    }

    //returns true if 4-in-a-row is found in, in either direction 
    public static boolean fourInARow(char[][] board, char checker, int r, int c, int rDelta, int cDelta){
        int sum = 0; 
        int leftRow = r - rDelta; 
        int leftCol = c - cDelta;
        while(board[leftRow][leftCol] == checker){
            sum++;
            leftRow -= rDelta;
            leftCol -= cDelta; 
            if(sum == 4){
                return true;
            }
        }
        while(board[r][c] == checker){
            sum++;
            r += rDelta;
            c += cDelta;
            if(sum == 4){
                return true;
            }
        }
        return false;
    }

    //returns true if any win is found 
    public static boolean gameWin(char[][] board, char checker, int r, int c){
        boolean verticalWin = fourInARow(board, checker, r, c, 1, 0);
        boolean horizontalWin = fourInARow(board, checker, r, c, 0, 1);
        boolean diagonalWin1 = fourInARow(board, checker, r, c, 1, 1);
        boolean diagonalWin2 = fourInARow(board, checker, r, c, -1, 1);
        return verticalWin || horizontalWin|| diagonalWin1||diagonalWin2; 
    }

    //returns true if all uppermost row is full, hence a draw
    public static boolean gameDraw(char[][] board){
        for(int c = DUMMY_EDGE; c <= NUM_COL + DUMMY_EDGE -1; c++){
            if(board[DUMMY_EDGE][c] == '.'){
                return false;
            }
        }
        return true;
    }

    // show the intro
    public static void intro() {
        System.out.println("This program allows two people to play the");
        System.out.println("game of Connect four. Each player takes turns");
        System.out.println("dropping a checker in one of the open columns");
        System.out.println("on the board. The columns are numbered 1 to 7.");
        System.out.println("The first player to get four checkers in a row");
        System.out.println("horizontally, vertically, or diagonally wins");
        System.out.println("the game. If no player gets fours in a row and");
        System.out.println("and all spots are taken the game is a draw.");
        System.out.println("Player one's checkers will appear as r's and");
        System.out.println("player two's checkers will appear as b's.");
        System.out.println("Open spaces on the board will appear as .'s.\n");
    }

    // prompt the user for an int. The String prompt will
    // be printed out. I expect key is connected to System.in.
    public static int getInt(Scanner key, String prompt) {
        while(!key.hasNextInt()) {
            String notAnInt = key.nextLine();
            System.out.println();
            System.out.println(notAnInt + " is not an integer.");
            System.out.print(prompt);
        }
        int result = key.nextInt();
        key.nextLine();
        return result;
    }
}