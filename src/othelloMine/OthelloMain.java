package othelloMine;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Othello game working kind of.....
 * 
 * @author Lukas Kurasinski
 *
 */
public class OthelloMain {
	private Board board = new Board();
	private int evalDepth = 4; // must be even for evaluation of computers moves!!!!!!!!!!!!!!!!!!!!!!
	private int move = 0;
	private Board optimalMinimalTerminalBoard = board; // optimal board for a given node depth / player min val
	private boolean gameIsRunning = true;
	private long startTime;// for time testing
	private int boardsCount = 0;
	int boardDepth = -1;
	private boolean debug;
	public void runGame(boolean prunning,boolean debug) {
		this.debug=debug;
		System.out.println(board.toString());

		while (gameIsRunning) {
			try {

				// first move player X
				// get input from console
				Scanner scanner = new Scanner(System.in);
				System.out.println("enter cell x,y --if bad input game crashes!--");
				String[] input = scanner.nextLine().split(",");
				// try to put a move on a board if possible
				try {
					if (board.insertValue(Integer.parseInt(input[0]), Integer.parseInt(input[1]), "X")) {
						// board.addMovehistory(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
						System.out.println("********YOUR MOVE************* \n" + board.toString());
						move++;
						if (board.isBoardFull()) {
							System.out.println(board.checkWhoWinns());
							break;
						}
					}
				} catch (Exception e) {
				}
				// for time testing
				startTime = System.currentTimeMillis();

				// computer move player O
				if (prunning) {
					optimalMinimalTerminalBoard = this.prunning(board, board.getLevel() + evalDepth, Integer.MIN_VALUE,
							Integer.MAX_VALUE);
				} else {
					optimalMinimalTerminalBoard = this.minmax(board, board.getLevel() + evalDepth);
				}
				System.out.println("Number of boards checked: " + boardsCount);
				boardsCount = 0;
				// for time testing
				System.out.println("evaluation with depth " + evalDepth + " took:"
						+ (System.currentTimeMillis() - startTime) + " millis.");

				// try to use the move from evaluation
				try {
					String[] oponent = optimalMinimalTerminalBoard.getMovehistory().get(move).split(",");
					if (oponent.length != 0) {
						if (board.insertValue(Integer.parseInt(oponent[0]), Integer.parseInt(oponent[1]), "O")) {
							System.out.println("oponent choose position: " + oponent[0] + "," + oponent[1]);
							System.out.println("********OPONENT MOVE************* \n" + board.toString());
							move++;
							if (board.isBoardFull()) {
								System.out.println(board.checkWhoWinns());
								break;
							}
						}
					}
				} catch (Exception e) {
				}
				/*
				 * // check if theres a winner (fulll board) if (board.isBoardFull()) {
				 * System.out.println(board.checkWhoWinns()); gameIsRunning = false; }
				 */
				// this board is going to be replaced under evaluation
				optimalMinimalTerminalBoard = new Board();
				optimalMinimalTerminalBoard.setValue((int) Math.pow(evalDepth, Integer.MAX_VALUE));
				boardDepth = -1;
				// this is just for JVM and OS
				Thread.sleep(50);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public Board minmax(Board initialStateBoard, int depth) {
		boardsCount++;
		// terminal test 
		if (initialStateBoard.isBoardFull() || initialStateBoard.getLevel() >= depth) {
			initialStateBoard.sumUpBoard(); // Heuristic val of this board, utility func
			return initialStateBoard.cloneThisBoardObj();

			//minimizer
		} else if (initialStateBoard.getLastActivePlayer().equals("X")) {
			Board minBoard = new Board();
			minBoard.setValue(Integer.MAX_VALUE);// Worst max value
			for (int i = 0; i < initialStateBoard.getBoardLength(); i++) {
				for (int j = 0; j < initialStateBoard.getBoardLength(); j++) {
					Board tempCloneBoard = initialStateBoard.cloneThisBoardObj();
					if (tempCloneBoard.makeBoard(i, j, "O")) {
						Board tempReturnTerminalBoard = minmax(tempCloneBoard, depth);
						try {
							if (tempReturnTerminalBoard.getValue() <= minBoard.getValue()) {
								minBoard = tempReturnTerminalBoard.cloneThisBoardObj();

							}

						} catch (Exception e) {
						}
					}
				}
			}
			if(debug)
			System.out.println(minBoard);
			return minBoard;
			// maximizer
		} else if (initialStateBoard.getLastActivePlayer().equals("O")) {
			Board maxBoard = new Board();
			maxBoard.setValue(Integer.MIN_VALUE); // Worst min value
			for (int i = 0; i < initialStateBoard.getBoardLength(); i++) {
				for (int j = 0; j < initialStateBoard.getBoardLength(); j++) {
					Board tempCloneBoard = initialStateBoard.cloneThisBoardObj();
					if (tempCloneBoard.makeBoard(i, j, "X")) {
						Board tempReturnTerminalBoard = minmax(tempCloneBoard, depth);
						try {
							if (tempReturnTerminalBoard.getValue() >= maxBoard.getValue()) {
								maxBoard = tempReturnTerminalBoard.cloneThisBoardObj();

							}

						} catch (Exception e) {
						}
					}
				}
			}
			if(debug)
			System.out.println(maxBoard);
			return maxBoard;
		}

		return null;
	}

	public Board prunning(Board initialStateBoard, int depth, int alpha, int beta) {

		boardsCount++;
		// terminal test
		if (initialStateBoard.isBoardFull() || initialStateBoard.getLevel() >= depth) {
			initialStateBoard.sumUpBoard(); // Heuristic val of this board  utility func
			return initialStateBoard.cloneThisBoardObj();
			// minimizer
		} else if (initialStateBoard.getLastActivePlayer().equals("X")) {
			int a = alpha;
			int b = beta;
			Board minBoard = new Board();
			minBoard.setValue(Integer.MAX_VALUE);// Worst max value
			loop: for (int i = 0; i < initialStateBoard.getBoardLength(); i++) {
				for (int j = 0; j < initialStateBoard.getBoardLength(); j++) {
					Board tempCloneBoard = initialStateBoard.cloneThisBoardObj();
					if (tempCloneBoard.makeBoard(i, j, "O")) {
						Board tempReturnTerminalBoard = prunning(tempCloneBoard, depth, a, b);
						try {

							if (tempReturnTerminalBoard.getValue() <= minBoard.getValue()) {
								minBoard = tempReturnTerminalBoard.cloneThisBoardObj();
								b = minBoard.getValue();
							}

							if (minBoard.getValue() < a) {
								break loop;
							}
						} catch (Exception e) {
						}
					}

				}

			}
			if(debug)
			System.out.println(minBoard);
			return minBoard;
			// maximizer
		} else if (initialStateBoard.getLastActivePlayer().equals("O")) {
			int a = alpha;
			int b = beta;
			Board maxBoard = new Board();
			maxBoard.setValue(Integer.MIN_VALUE); // Worst min value
			loop: for (int i = 0; i < initialStateBoard.getBoardLength(); i++) {
				for (int j = 0; j < initialStateBoard.getBoardLength(); j++) {
					Board tempCloneBoard = initialStateBoard.cloneThisBoardObj();
					if (tempCloneBoard.makeBoard(i, j, "X")) {
						Board tempReturnTerminalBoard = prunning(tempCloneBoard, depth, a, b);

						try {

							if (tempReturnTerminalBoard.getValue() >= maxBoard.getValue()) {
								maxBoard = tempReturnTerminalBoard.cloneThisBoardObj();
								a = maxBoard.getValue();

							}
							if (maxBoard.getValue() > b) {
								break loop;
							}

						} catch (Exception e) {
						}
					}
				}

			}
			if(debug)
			System.out.println(maxBoard);
			return maxBoard;
		}

		return null;

	}

	public static void main(String[] args) {
		OthelloMain o = new OthelloMain();
		o.runGame(true,false);	// must be true for pruning and true for debug 
	}
}
