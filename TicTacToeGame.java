import java.util.*;
import java.io.*;

public class TicTacToeGame {

	//Played on an n x n board
	private final int NUM_ROWS = 3;
	private final int NUM_COLS = 3;
	private char[][] board;
	private Scanner input;
	private char currentPlayer;
	private int row, col;    //input read from a user
	private boolean win;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public TicTacToeGame() {
		// TODO Auto-generated constructor stub
		board = new char[NUM_ROWS][NUM_COLS];
		input = new Scanner(System.in);
		currentPlayer = 'X';
		win = false;
		System.out.println("Welcome to the Tic-Tac-Toe Game!");
		System.out.print("Would you like to load a previous game? (Y/N)");
		String load = input.next();
		if (load.equalsIgnoreCase("y")) {
			loadGame();
		} else {
			initializeBoard();
		}
		System.out.println("It is " + currentPlayer + "'s turn!");
		//Let's play!
		//Play until winner or draw
		do {   //end of game?
			//print the board
			printBoard();  //System.out.println(this);
			//Get input to push game forward
			//Ask if user wants to save.
			System.out.println("Do you want to Save the game?");
			String response = input.next();
			if (response.equalsIgnoreCase("y")) {
				saveGame();
				System.out.println("Game has been saved.");
			}
			do { //is it occupied?
				do {  //getting a valid row
					try {
						getRow();
					} catch(TicTacToeException ttte) {
						ttte.getMessage();
						ttte.printStackTrace();
					}
				} while(!isValid());
				//column time!
				do {  //getting a valid row
					try {
						getCol();
					} catch(TicTacToeException ttte) {
						ttte.getMessage();
						ttte.printStackTrace();
					}
				} while(!isValid());
				//Check to see if it is occupied
				if (isOccupied()) {
					System.out.println("That space is occupied already!");
				}
			} while (isOccupied());
			//If we've made it here, we have a valid row and column
			board[row][col] = currentPlayer;
			//Check if there is a winner
			win = isWinner();
			if (!win) {
				//Switch the player
				if (currentPlayer == 'X') {
					currentPlayer = 'O';
				} else {
					currentPlayer = 'X';
				}
			}
		} while (!win && !isDraw());
		//Game is over!
		printBoard();
		if (win) {
			System.out.println(currentPlayer + " wins!");
		} else {  //draw
			System.out.println("The game has ended in a draw. Would you like to play a game of chess?");
		}
	}
	
	private void initializeBoard() {
		for(int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				board[r][c] = ' ';
			}
		}
	}

	/**
	 * Prints the board in the following fashion:
	 * 
	 *  x | o | o 
	 *  o | x | x
	 *    | x | o
	 */
	private void printBoard() {
		for(int r = 0; r < NUM_ROWS; r++) {
			for(int c = 0; c < NUM_COLS-1; c++) {
				System.out.print(board[r][c] + " | ");
			}
			//Do last value
			System.out.println(board[r][NUM_COLS-1]);
		}
	}
	
	private void getRow() throws TicTacToeException {
		System.out.println("Enter a valid row: ");
		row = input.nextInt();
		if ((row < 0) || (row >= NUM_ROWS)) {
			throw new TicTacToeException(row + " is not a valid row.");
		}		
	}
	
	private void getCol() throws TicTacToeException {
		System.out.println("Enter a valid column: ");
		col = input.nextInt();
		if ((col < 0) || (col >= NUM_COLS)) {
			throw new TicTacToeException(col + " is not a valid column.");
		}		
	}
	
	private boolean isValid() {
		if (row < 0 || row >= NUM_ROWS) return false;
		else if (col < 0 || col >= NUM_COLS) return false;
		else return true;
	}
	
	private boolean isOccupied() {
		return board[row][col] != ' ';
	}
	
	private boolean isWinner() {
		//Check rows!
		//keep running sum of characters
		int sum = -1; //dummy value (we should at least have 3 columns in TTT)
		for(int r = 0; r < NUM_ROWS; r++) {
			sum = 0;
			for(int c = 0; c < NUM_COLS; c++) {
				if (board[r][c] == currentPlayer) {
					sum++;
				}
			}
			if (sum == NUM_ROWS) {
				return true;
			}	
		}
		//Check columns
		for(int c = 0; c < NUM_COLS; c++) {  //board[0].length
			sum = 0;
			for(int r = 0; r < NUM_ROWS; r++) {
				if (board[r][c] == currentPlayer) {
					sum++;
				}
			}
			if (sum == NUM_COLS) {
				return true;
			}
		}	
		//Check major diagonal
		sum = 0;
		for(int r = 0; r < NUM_ROWS; r++) {  
			if (board[r][r] == currentPlayer) {
				sum++;
			}
		}
		if (sum == NUM_ROWS) {
			return true;
		}
		//Check minor diagonal
		sum = 0;
		for(int r = 0; r < NUM_ROWS; r++) {
			if (board[r][NUM_ROWS-r-1] == currentPlayer) {
				sum++;
			}
		}
		//last check
		return (sum == NUM_ROWS);
	}
	
	private boolean isDraw() {
		for(int r = 0; r < NUM_ROWS; r++) {
			for(int c = 0; c < NUM_COLS; c++) {
				if (board[r][c] == ' ') return false;
			}
		}
		return true;
	}
	
	private void saveGame() {
		try {
			out = new ObjectOutputStream(new FileOutputStream("Board.dat"));
			out.writeObject(currentPlayer);
			out.writeObject(board);
			out.close();
		} catch (Exception e) {
			System.out.println("Could not open Board.dat!");
			e.printStackTrace();
		}		
	}
	
	private void loadGame() {
		try {
			in = new ObjectInputStream(new FileInputStream("Board.dat"));
			currentPlayer = (char)in.readObject();
			board = (char[][])in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println("Could not open Board.dat!");
			e.printStackTrace();
		}	
	}
	
}
