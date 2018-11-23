package othelloMine;

import java.util.LinkedList;

public class Board implements Comparable {
	private String[][] board	  ={{".",".",".",".",".",".",".","."},
									{".",".",".",".",".",".",".","."},
									{".",".",".",".",".",".",".","."},
									{".",".",".","X","O",".",".","."},
									{".",".",".","O","X",".",".","."},
									{".",".",".",".",".",".",".","."},
									{".",".",".",".",".",".",".","."},
									{".",".",".",".",".",".",".","."}};	// = new String[4][4];
									
		
	public void setValue(int value) {
		this.value = value;
	}

	private String player = "";
	private String position = "";
	private int level = 0;
	private int nbrOfTakenX = 0;
	private int nbrOfTakenO = 0;
	private int value = 0;
	private int boardLength = board.length;
	private LinkedList<String> movehistory = new LinkedList<String>();

	public Board() {
//		for (int i = 0; i < board.length; i++) {
//			for (int j = 0; j < board.length; j++) {
//				board[i][j] = ".";
//			}
//		}
	}

	public Board(String[][] b) {
		this.board = b;
	}

	public Board(String[][] board,String player, String position, int boardLevel,int value, LinkedList<String> moveHist) {
		this.board = board;
		this.player = player;
		this.position = position;
		this.level = boardLevel;
		this.value=value;
		this.movehistory = moveHist;
	}

	public int getNbrOfTakenX() {
		return nbrOfTakenX;
	}

	public int getNbrOfTakenO() {
		return nbrOfTakenO;
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
			if ((player.equals("X") || player.equals("O")) && board[row][col].equals(".")&&this.isValidMove(row, col, player)) {
				board[row][col] = player;
				this.player = player;
				level++;
				movehistory.add(row + "," + col);
				this.flipLeft(row, col, player, row, col);
				this.flipRight(row, col, player, row, col);
				this.flipUp(row, col, player, row, col);
				this.flipDown(row, col, player, row, col);
				this.flipUpLeft(row, col, player, row, col);
				this.flipUpRight(row, col, player, row, col);
				
				this.sumUpBoard();
	//			System.out.println(this.toString());
				return true;
			
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private void sumUpBoard() {
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
		this.nbrOfTakenO = tempO;
		this.nbrOfTakenX = tempX;
		this.value = this.nbrOfTakenX - this.nbrOfTakenO;
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
			if (this.nbrOfTakenO>this.nbrOfTakenX) {
				temp = "O wins," + this.nbrOfTakenO;
			} else if (this.nbrOfTakenO<this.nbrOfTakenX) {
				temp = "X wins," + this.nbrOfTakenX;
			} else {
				temp = "Its a draw";
			}
		}
		return temp;
	}

	public boolean hasPlayerValidMove(String player) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				return isValidMove(i,j,player);
			}
		}
		return false;
	}

	
	public void addMovehistory(int row,int col) {
		this.movehistory.add(row+","+col);
	}
	
	//Neighbor
	private boolean isValidMove(int row,int col,String player) {
		if(player.equals("X")) {
			try {
				if(this.board[row-1][col-1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row-1][col].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row-1][col+1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row][col-1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row][col+1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col-1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col+1].equals("O")) {
					return true;
				}
			}catch(Exception e) {}
			
		}else if(player.equals("O")) {
			try {
				if(this.board[row-1][col-1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row-1][col].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row-1][col+1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row][col-1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row][col+1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col-1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			try {
				if(this.board[row+1][col+1].equals("X")) {
					return true;
				}
			}catch(Exception e) {}
			
		}
		return false;
		
	}
	

	
	private void flipLeft(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow][endCol - 1].equals("O")) {
					flipLeft(row, col, player, endRow, endCol - 1);
				} else if (this.board[endRow][endCol - 1].equals("X")) {
					for (int i = endCol-1; i <= col; i++) {
						board[row][i] = "X";
					}
				} else if (this.board[row][col - 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow][endCol - 1].equals("X")) {
					flipLeft(row, col, player, endRow, endCol - 1);
				} else if (this.board[endRow][endCol - 1].equals("O")) {
					for (int i = endCol-1; i <= col; i++) {
						board[row][i] = "O";
					}
				} else if (this.board[row][col - 1].equals(".")) {
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
					for (int i = col; i <= endCol+1; i++) {
						board[row][i] = "X";
					}
				} else if (this.board[row][col + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow][endCol + 1].equals("X")) {
					flipRight(row, col, player, endRow, endCol + 1);
				} else if (this.board[endRow][endCol + 1].equals("O")) {
					for (int i = col; i <= endCol+1; i++) {
						board[row][i] = "O";
					}
				} else if (this.board[row][col + 1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}
	
	private void flipUp(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow-1][endCol].equals("O")) {
					flipUp(row, col, player, endRow-1, endCol);
				} else if (this.board[endRow-1][endCol].equals("X")) {
					for (int i = endRow-1; i <= row; i++) {
						board[i][col] = "X";
					}
				} else if (this.board[endRow-1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow-1][endCol].equals("X")) {
					flipUp(row, col, player, endRow-1, endCol);
				} else if (this.board[endRow-1][endCol].equals("O")) {
					for (int i = endRow-1; i <= row; i++) {
						board[i][col] = "O";
					}
				} else if (this.board[endRow-1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}

	private void flipDown(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow+1][endCol].equals("O")) {
					flipDown(row, col, player, endRow+1, endCol);
				} else if (this.board[endRow+1][endCol].equals("X")) {
					for (int i = row; i <= endRow+1; i++) {
						board[i][col] = "X";
					}
				} else if (this.board[endRow+1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow+1][endCol].equals("X")) {
					flipDown(row, col, player, endRow+1, endCol);
				} else if (this.board[endRow+1][endCol].equals("O")) {
					for (int i = row; i <= endRow+1; i++) {
						board[i][col] = "O";
					}
				} else if (this.board[endRow+1][endCol].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}
	
	
	private void flipUpLeft(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow-1][endCol-1].equals("O")) {
					flipUpLeft(row, col, player, endRow-1, endCol-1);
				} else if (this.board[endRow-1][endCol-1].equals("X")) {
					for (int i = row; i >= endRow-1; i--) {
						board[i][col] = "X";
						col--;
					}
				} else if (this.board[endRow-1][endCol-1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow-1][endCol-1].equals("X")) {
					flipUpLeft(row, col, player, endRow-1, endCol-1);
				} else if (this.board[endRow-1][endCol-1].equals("O")) {
					for (int i = row; i >= endRow-1; i++) {
						board[i][col] = "O";
						col--;
					}
				} else if (this.board[endRow-1][endCol-1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		}
	}
	private void flipUpRight(int row, int col, String player, int endRow, int endCol) {
		if (player.equals("X")) {
			try {
				if (this.board[endRow-1][endCol+1].equals("O")) {
					flipUpRight(row, col, player, endRow-1, endCol+1);
				} else if (this.board[endRow-1][endCol+1].equals("X")) {
					for (int i = row; i >= endRow-1; i--) {
						board[i][col] = "X";
						col++;
					}
				} else if (this.board[endRow-1][endCol+1].equals(".")) {
					return;
				}
			} catch (Exception e) {
			}
		} else if (player.equals("O")) {
			try {
				if (this.board[endRow-1][endCol+1].equals("X")) {
					flipUpRight(row, col, player, endRow-1, endCol+1);
				} else if (this.board[endRow-1][endCol+1].equals("O")) {
					for (int i = row+1; i >= endRow-1; i--) {
						board[i][col] = "O";
						col++;
					}
				} else if (this.board[endRow-1][endCol+1].equals(".")) {
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

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
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
		return new Board(this.getBoardCopy(),this.player,this.position,this.level,this.value,this.getMoveHistoryCopy());
	}

	public LinkedList<String> getMovehistory() {
		return movehistory;
	}

	private String showMoveHistory() {
		String temp="";
		for (int i = 0; i < this.movehistory.size(); i++) {
			temp+=this.movehistory.get(i)+";";
		}
		return temp;
	}
	
	public String toString() {
		String temp = "";
		for (int i = 0; i < board.length; i++) {
			temp+=i+"";
			for (int j = 0; j < board.length; j++) {
				
				temp += "|" + board[i][j] + "|";
			}
			temp += "\n";
			if(i==board.length-1) {
				for (int j = 0; j < board.length; j++) {
					temp+="  "+j;
				}
				temp+="\n";
			}
		}
		temp += "boardLevel:" + this.level + "\n";
		temp += "boardValue:" + this.value + "\n";
		temp += "boardPlayer:" + this.player + "\n";
		temp += "numberOfTakenX:" + this.nbrOfTakenX + "\n";
		temp += "numberOfTakenO:" + this.nbrOfTakenO + "\n";
		temp += "move history:" + this.showMoveHistory() + "\n";
		

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
		return this.getValue()-b.getValue();

	}

	

}
