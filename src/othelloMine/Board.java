package othelloMine;

import java.util.LinkedList;

public class Board {
	String[][] board = new String[4][4];
	String player = "";
	String position = "";
	int boardLevel = 0;
	int nbrOfTakenX = 0;
	int nbrOfTakenO = 0;

	public Board() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = ".";
			}
		}
	}

	public Board(String[][] b) {
		this.board = b;
	}

	/**
	 * puts a pice on the board if possible otherwise returns false
	 * 
	 * @param row
	 * @param col
	 * @param player
	 * @return
	 */
	public boolean insertValue(int row, int col, String player) {
		try {
			if (player.equals("X") || player.equals("O")) {
				board[row][col] = player;
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	// benefit
	public void checkNbrOfTaken() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if(this.board[i][j].equals("O")) {
					this.nbrOfTakenO++;
				}
				if(this.board[i][j].equals("X")) {
					this.nbrOfTakenX++;
				}
			}
		}
	}

	public String checkWhoWinns() {
		String temp = "";
		if (this.isBoardFull()) {
			int counter = 0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					if (board[i][j] == "X") {
						counter++;
					}
				}
			}
			if (counter - 16 < 0) {
				temp = "Computer wins";
			} else if (counter - 16 > 0) {
				temp = "User wins";
			} else if (counter - 16 == 0) {
				temp = "Its a draw";
			}
		}
		return temp;
	}

	
	
	public boolean isBoardFull() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == ".") {
					return false;
				}
			}
		}
		return true;
	}

	public void setBoardLevel(int bl) {
		this.boardLevel = bl;
	}

	public String toString() {
		String temp = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				temp += "|" + board[i][j] + "|";
			}
			temp += "\n";
		}
		this.checkNbrOfTaken();
		
		
		temp += "boardLevel:" + this.boardLevel + "\n";
		temp += "numberOfTakenX:" + this.nbrOfTakenX + "\n";
		temp += "numberOfTakenY:" + this.nbrOfTakenO+ "\n";
		
		return temp;
	}
}
