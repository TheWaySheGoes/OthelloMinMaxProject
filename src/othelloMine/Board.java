package othelloMine;

import java.util.LinkedList;

public class Board implements Comparable {
	private String[][] board = { { ".", ".", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", "." },
			{ ".", ".", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", "X", "O", ".", ".", "." },
			{ ".", ".", ".", "O", "X", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", "." },
			{ ".", ".", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", "." } }; // = new
																										// String[4][4];

	public void setValue(int value) {
		this.value = value;
	}

	private String lastActivePlayer = "";
	private String position = "";
	private int level = 0;
	private int numberOfX = 0;
	private int numberOfO = 0;
	private int value = 0;
	private int boardLength = board.length;
	private LinkedList<String> movehistory = new LinkedList<String>();
	private Board parent;
	private boolean isVisited = false;
	private LinkedList<Board> children; // this is null on this object creation
	private int alpha = Integer.MIN_VALUE;
	private int beta =Integer.MAX_VALUE;
	
	
	
	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public Board getParent() {
		return parent;
	}

	public void setParent(Board parent) {
		this.parent = parent;
	}

	public Board getChilde(int i) {
		return children.get(i);
	}

	public LinkedList<Board> getChilderen() {
		return children; 
	}

	public void addChilde(Board childe) {
		if (children == null) {
			children = new LinkedList<Board>();
		} else {
			this.children.add(childe);

		}
	}

	public Board() {

	}

	public Board(String[][] b) {
		this.board = b;
	}

	public Board(String[][] board, String lastActivePlayer, String position, int boardLevel, int value,
			int alpha, int beta, LinkedList<String> moveHist) {
		this.board = board;
		this.lastActivePlayer = lastActivePlayer;
		this.position = position;
		this.level = boardLevel;
		this.value = value;
		this.movehistory = moveHist;
		this.alpha=alpha;
		this.beta=beta;
		
	}

	public int getAlpha() {
		return alpha;
	}

	public int getBeta() {
		return beta;
	}

	/**
	 * puts a piece on the board if possible otherwise returns false
	 * 
	 * @param row
	 * @param col
	 * @param player
	 *            "X" or "O"
	 * @return
	 */
	public boolean insertValue(int row, int col, String player) {
		try {
			if ((player.equals("X") || player.equals("O")) && board[row][col].equals(".")
					&& this.isValidMove(row, col, player)) {
				board[row][col] = player;
				this.lastActivePlayer = player;
				level++;
				movehistory.add(row + "," + col);
				this.flipLeft(row, col, player, row, col);
				this.flipRight(row, col, player, row, col);
				this.flipUp(row, col, player, row, col);
				this.flipDown(row, col, player, row, col);
				this.flipUpLeft(row, col, player, row, col);
				this.flipUpRight(row, col, player, row, col);
				this.flipDownLeft(row, col, player, row, col);
				this.flipDownRight(row, col, player, row, col);
				this.sumUpBoard();
				// System.out.println(this.toString());
				return true;

			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// *** thisOneIsForalternativeSolution*** (no sumUp() )
	public boolean makeBoard(int row, int col, String player) {
		try {
			if ((player.equals("X") || player.equals("O")) && board[row][col].equals(".")
					&& this.isValidMove(row, col, player)) {
				board[row][col] = player;
				this.lastActivePlayer = player;
				level++;
				movehistory.add(row + "," + col);
				this.flipLeft(row, col, player, row, col);
				this.flipRight(row, col, player, row, col);
				this.flipUp(row, col, player, row, col);
				this.flipDown(row, col, player, row, col);
				this.flipUpLeft(row, col, player, row, col);
				this.flipUpRight(row, col, player, row, col);
				this.flipDownLeft(row, col, player, row, col);
				this.flipDownRight(row, col, player, row, col);
				// ****
				// System.out.println(this.toString());
				return true;

			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void sumUpBoard() {
		int tempX = 0;
		int tempO = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (this.board[i][j].equals("O")) {
					tempO++;
				}
				if (this.board[i][j].equals("X")) {
					tempX++;
				}
			}
		}
		this.numberOfO = tempO;
		this.numberOfX = tempX;
		this.value = this.numberOfX - this.numberOfO;
	}

	/**
	 * 
	 * @return
	 */
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

	public String checkWhoWinns() {
		String temp = "";
		if (this.isBoardFull()) {
			if (this.numberOfO > this.numberOfX) {
				temp = "O wins," + this.numberOfO;
			} else if (this.numberOfO < this.numberOfX) {
				temp = "X wins," + this.numberOfX;
			} else {
				temp = "Its a draw";
			}
		}
		return temp;
	}

	public boolean hasPlayerValidMove(String player) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				return isValidMove(i, j, player);
			}
		}
		return false;
	}

	public void addMovehistory(int row, int col) {
		this.movehistory.add(row + "," + col);
	}

	// Neighbor
	private boolean isValidMove(int row, int col, String player) {
		if (player.equals("X")) {
			try {
				if (this.board[row - 1][col - 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row - 1][col].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row - 1][col + 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row][col - 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row][col + 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col - 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col + 1].equals("O")) {
					return true;
				}
			} catch (Exception e) {
			}

		} else if (player.equals("O")) {
			try {
				if (this.board[row - 1][col - 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row - 1][col].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row - 1][col + 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row][col - 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row][col + 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col - 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}
			try {
				if (this.board[row + 1][col + 1].equals("X")) {
					return true;
				}
			} catch (Exception e) {
			}

		}
		return false;

	}

	private void flipLeft(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow][endCol - 1].equals("O")) {
					flipLeft(row, col, player, endRow, endCol - 1);
				} else if (this.board[endRow][endCol - 1].equals("X")) {
					for (int i = col; i >= endCol - 1; i--) {
						board[row][i] = "X";
					}
				} else if (this.board[endRow][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow][endCol - 1].equals("X")) {
					flipLeft(row, col, player, endRow, endCol - 1);
				} else if (this.board[endRow][endCol - 1].equals("O")) {
					for (int i = col; i >= col; i--) {
						board[row][i] = "O";
					}
				} else if (this.board[endRow][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipRight(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow][endCol + 1].equals("O")) {
					flipRight(row, col, player, endRow, endCol + 1);
				} else if (this.board[endRow][endCol + 1].equals("X")) {
					for (int i = col; i <= endCol + 1; i++) {
						board[row][i] = "X";
					}
				} else if (this.board[endRow][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow][endCol + 1].equals("X")) {
					flipRight(row, col, player, endRow, endCol + 1);
				} else if (this.board[endRow][endCol + 1].equals("O")) {
					for (int i = col; i <= endCol + 1; i++) {
						board[row][i] = "O";
					}
				} else if (this.board[endRow][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipUp(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow - 1][endCol].equals("O")) {
					flipUp(row, col, player, endRow - 1, endCol);
				} else if (this.board[endRow - 1][endCol].equals("X")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "X";
					}
				} else if (this.board[endRow - 1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow - 1][endCol].equals("X")) {
					flipUp(row, col, player, endRow - 1, endCol);
				} else if (this.board[endRow - 1][endCol].equals("O")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "O";
					}
				} else if (this.board[endRow - 1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipDown(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow + 1][endCol].equals("O")) {
					flipDown(row, col, player, endRow + 1, endCol);
				} else if (this.board[endRow + 1][endCol].equals("X")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "X";
					}
				} else if (this.board[endRow + 1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow + 1][endCol].equals("X")) {
					flipDown(row, col, player, endRow + 1, endCol);
				} else if (this.board[endRow + 1][endCol].equals("O")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "O";
					}
				} else if (this.board[endRow + 1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipUpLeft(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow - 1][endCol - 1].equals("O")) {
					flipUpLeft(row, col, player, endRow - 1, endCol - 1);
				} else if (this.board[endRow - 1][endCol - 1].equals("X")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "X";
						col--;
					}
				} else if (this.board[endRow - 1][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow - 1][endCol - 1].equals("X")) {
					flipUpLeft(row, col, player, endRow - 1, endCol - 1);
				} else if (this.board[endRow - 1][endCol - 1].equals("O")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "O";
						col--;
					}
				} else if (this.board[endRow - 1][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipUpRight(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow - 1][endCol + 1].equals("O")) {
					flipUpRight(row, col, player, endRow - 1, endCol + 1);
				} else if (this.board[endRow - 1][endCol + 1].equals("X")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "X";
						col++;
					}
				} else if (this.board[endRow - 1][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow - 1][endCol + 1].equals("X")) {
					flipUpRight(row, col, player, endRow - 1, endCol + 1);
				} else if (this.board[endRow - 1][endCol + 1].equals("O")) {
					for (int i = row; i >= endRow - 1; i--) {
						board[i][col] = "O";
						col++;
					}
				} else if (this.board[endRow - 1][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipDownLeft(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow + 1][endCol - 1].equals("O")) {
					flipDownLeft(row, col, player, endRow + 1, endCol - 1);
				} else if (this.board[endRow + 1][endCol - 1].equals("X")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "X";
						col--;
					}
				} else if (this.board[endRow + 1][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow + 1][endCol - 1].equals("X")) {
					flipDownLeft(row, col, player, endRow + 1, endCol - 1);
				} else if (this.board[endRow + 1][endCol - 1].equals("O")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "O";
						col--;
					}
				} else if (this.board[endRow + 1][endCol - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipDownRight(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow + 1][endCol + 1].equals("O")) {
					flipDownRight(row, col, player, endRow + 1, endCol + 1);
				} else if (this.board[endRow + 1][endCol + 1].equals("X")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "X";
						col++;
					}
				} else if (this.board[endRow + 1][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow + 1][endCol + 1].equals("X")) {
					flipDownRight(row, col, player, endRow + 1, endCol + 1);
				} else if (this.board[endRow + 1][endCol + 1].equals("O")) {
					for (int i = row; i <= endRow + 1; i++) {
						board[i][col] = "O";
						col++;
					}
				} else if (this.board[endRow + 1][endCol + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * value tested at terminal node.
	 * 
	 * @return
	 */
	public int getValue() {
		return this.value;
	}

	public String getLastActivePlayer() {
		return lastActivePlayer;
	}

	public void setPlayer(String player) {
		this.lastActivePlayer = player;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getLevel() {
		return level;
	}

	public void setBoardLevel(int bl) {
		this.level = bl;
	}

	public int getBoardLength() {
		return boardLength;
	}

	public String[][] getBoard() {
		return this.board;
	}

	public String[][] getBoardCopy() {
		String[][] temp = new String[this.boardLength][this.boardLength];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp.length; j++) {
				temp[i][j] = this.board[i][j];
			}
		}
		return temp;
	}

	public LinkedList<String> getMoveHistoryCopy() {
		LinkedList<String> temp = new LinkedList<String>();
		for (int i = 0; i < movehistory.size(); i++) {
			temp.add(this.movehistory.get(i));
		}
		return temp;
	}

	public Board cloneThisBoardObj() {
		return new Board(this.getBoardCopy(), this.lastActivePlayer, this.position, this.level, this.value,
				this.alpha,this.beta, this.getMoveHistoryCopy());
	}

	public LinkedList<String> getMovehistory() {
		return movehistory;
	}

	private String showMoveHistory() {
		String temp = "";
		for (int i = 0; i < this.movehistory.size(); i++) {
			temp += this.movehistory.get(i) + ";";
		}
		return temp;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}

	public String toString() {
		String temp = "";
		for (int i = 0; i < board.length; i++) {
			temp += i + "";
			for (int j = 0; j < board.length; j++) {

				temp += "|" + board[i][j] + "|";
			}
			temp += "\n";
			if (i == board.length - 1) {
				for (int j = 0; j < board.length; j++) {
					temp += "  " + j;
				}
				temp += "\n";
			}
		}
		temp += "boardLevel:" + this.level + "\n";
		temp += "boardValue:" + this.value + "\n";
		temp += "boardPlayer:" + this.lastActivePlayer + "\n";
		temp += "numberOfTakenX:" + this.numberOfX + "\n";
		temp += "numberOfTakenO:" + this.numberOfO + "\n";
		temp += "move history:" + this.showMoveHistory();

		return temp;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Sorts according to board overall value (utilitie)
	 */
	@Override
	public int compareTo(Object obj) {
		Board b = (Board) obj;
		return this.getValue() - b.getValue();

	}

}
