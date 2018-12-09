package othelloMine;

import java.util.Scanner;

/**
 * Othello game working kind of.
 * 
 * @author Lukas Kurasinski
 *
 */
public class Driver {
	private Board board = new Board();
	private int evalDepth = 4; // must be even for evaluation of computers moves!!!!!!!!!!!!!!!!!!!!!!
	private int move = 0;
	private Board optimalMinimalTerminalBoard = board; // optimal board for a given node depth 
	private boolean gameIsRunning = true;
	private long startTime;// for time testing
	private int boardsCount = 0;
	int boardDepth = -1;
	private boolean debug;

	public void runGame(boolean prunning, boolean debug, int evalDepth) {
		this.evalDepth = evalDepth;
		this.debug = debug;
		System.out.println("\n      Welcome\n\n----- OTHELLO LUKAS KURASINSKI VER 1.0 ----\n\nFor full experience resize the console window after first move,\nto accommodate the text between two dashed lines\n\n");
		System.out.println(board.toString());

		while (gameIsRunning) {
			try {
				System.out.println("----------------------------------------------------------------------");
				// first move player X
				// get input from console
				Scanner scanner = new Scanner(System.in);
				System.out.println("enter [row,column] --if bad input nothing happens!-- (enter x to exit)");

				String[] input = scanner.nextLine().split(",");
				if (input[0].equals("x"))
					System.exit(0);
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
					System.out.println(board.toString());
				}
				// for time testing
				startTime = System.currentTimeMillis();

				// computer move player O
				// send to minmax or pruning for evaluation
				if (prunning == true) {
					optimalMinimalTerminalBoard = this.pruning(board, board.getLevel() + evalDepth, Integer.MIN_VALUE,
							Integer.MAX_VALUE);
				} else if (prunning == false) {
					optimalMinimalTerminalBoard = this.minmax(board, board.getLevel() + evalDepth);
				}
				if (prunning == true) {
					System.out.println("Evaluation WITH prunning");
				} else if (prunning == false) {
					System.out.println("Evaluation WITHOUT prunning");
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
							System.out.println("********OPONENT MOVE************* \n" + board.toString() + "\n");
							move++;
							if (board.isBoardFull()) {
								System.out.println(board.checkWhoWinns());
								break;
							}
						}
					}
				} catch (Exception e) {
				}
				
				// check if theres a winner or a full board 
				// this board is a base board for next turn
				optimalMinimalTerminalBoard = new Board();
				optimalMinimalTerminalBoard.setValue((int) Math.pow(evalDepth, Integer.MAX_VALUE));
				boardDepth = -1;
				// this is just for JVM and OS threading 
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

			// minimizer
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
			if (debug)
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
			if (debug)
				System.out.println(maxBoard);
			return maxBoard;
		}

		return null;
	}

	public Board pruning(Board initialStateBoard, int depth, int alpha, int beta) {

		boardsCount++;
		// terminal test
		if (initialStateBoard.isBoardFull() || initialStateBoard.getLevel() >= depth) {
			initialStateBoard.sumUpBoard(); // Heuristic val of this board utility func
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
						Board tempReturnTerminalBoard = pruning(tempCloneBoard, depth, a, b);
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
			if (debug)
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
						Board tempReturnTerminalBoard = pruning(tempCloneBoard, depth, a, b);

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
			if (debug)
				System.out.println(maxBoard);
			return maxBoard;
		}

		return null;

	}

	public static void main(String[] args) {
		String preRunInfo="\n----- OTHELLO LUKAS KURASINSKI VER 1.0 ----\n"
				+ "\n Start it with three arguments arg1 arg2 arg3 " + "\n arg1 = [ true | false ] for pruning "
				+ "\n arg2 = [ true | false ] for debugger " + "\n arg3 = [ 2 | 4 | 6 | .. ] (even nbr) for cut-off depth"
				+ "\n DEPTH HIGHER THEN 4 TAKES A REALLY LONG TIME TO EVAL!";
		
		if (args.length != 3) {
			System.out.println(preRunInfo);
			System.exit(0);
		}
		int depth = 4;
		boolean prun = true;
		boolean debug = false;
		if (args.length > 0) {
			if (args[0].equals("true")) {
				prun = true;
			} else if (args[0].equals("false")) {
				prun = false;

			} else {
				System.out.println(preRunInfo);
				System.exit(0);
			}
			if (args[1].equals("true")) {
				debug = true;
			} else if (args[1].equals("false")) {
				debug = false;

			} else {
				System.out.println(preRunInfo);
				System.exit(0);
			}
			if (Integer.parseInt(args[2]) % 2 == 0) {
				depth = Integer.parseInt(args[2]);
			} else {
				System.out.println(preRunInfo);
				System.exit(0);
			}
		}

		Driver o = new Driver();
		o.runGame(prun, debug, depth); // must be true for pruning and true for debug
	}
}
