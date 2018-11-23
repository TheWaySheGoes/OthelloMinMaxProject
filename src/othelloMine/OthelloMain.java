package othelloMine;

import java.util.LinkedList;
import java.util.Scanner;

public class OthelloMain {
	private Board board = new Board();
	private LinkedList<Board> terminalStateBoards = new LinkedList<Board>();
	private int evalDepth = 5;
	private int move = 0;
	private Board optimalTerminalBoard = board;
	private boolean gameIsRunning = true;
	private long startTime;// for time testing
	
	public void runGame() {
		
		System.out.println(board.toString());
		
		while (gameIsRunning) {
			try {
				
				// first move player X
				// get input from console
				Scanner scanner = new Scanner(System.in);
				System.out.println("enter cell x,y --if bad input game crashes!--");
				String[] input = scanner.nextLine().split(",");
				// try to put a move on a board if possible
				if (board.insertValue(Integer.parseInt(input[0]), Integer.parseInt(input[1]), "X")) {
					// board.addMovehistory(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
					System.out.println("********YOUR MOVE************* \n" + board.toString());
					move++;
				}
				//for time testing
				startTime=System.currentTimeMillis();
				
				// computer move player O
				// evaluate all of the possible moves up to depth +3
				this.evaluate(board, board.getLevel() + evalDepth);
				
				//for time testing
				System.out.println("evaluation with depth "+evalDepth+" took:"+(System.currentTimeMillis()-startTime)+" millis.");
				
				// try to use the move from evaluation
				try {
					String[] oponent = optimalTerminalBoard.getMovehistory().get(move).split(",");
					if (oponent.length != 0) {
						if (board.insertValue(Integer.parseInt(oponent[0]), Integer.parseInt(oponent[1]), "O")) {
							System.out.println("oponent choose position: " + oponent[0] + "," + oponent[1]);
							System.out.println("********OPONENT MOVE************* \n" + board.toString());
							move++;
						}
					}
				} catch (Exception e) {
				}

				// check if theres a winner (fulll board)
				if (board.isBoardFull()) {
					System.out.println(board.checkWhoWinns());
					gameIsRunning = false;
				}

				// this board is going to be replaced under evaluation
				optimalTerminalBoard = new Board();
				optimalTerminalBoard.setValue((int) Math.pow(evalDepth, Integer.MAX_VALUE));

				// this is just for JVM and OS
				Thread.sleep(50);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void evaluate(Board initialStateBoard, int depth) {

		// terminal test
		if (initialStateBoard.isBoardFull() || initialStateBoard.getLevel() >= depth) {
			// utility function replacing optimal board if this better
			if (initialStateBoard.getValue() <= optimalTerminalBoard.getValue()) {
				optimalTerminalBoard = initialStateBoard.cloneThisBoardObj();
				// System.out.println("newest optimal terminal board: \n" +
				// optimalTerminalBoard.toString());
			}
		} else {

			// generating actions
			LinkedList<Board> actionsOflegalMovesInTheState = new LinkedList<Board>();
			for (int i = 0; i < initialStateBoard.getBoardLength(); i++) {
				for (int j = 0; j < initialStateBoard.getBoardLength(); j++) {
					Board temp = initialStateBoard.cloneThisBoardObj();
					// adding possible actions to list
					if (initialStateBoard.getPlayer().equals("X")) {
						if (temp.insertValue(i, j, "O")) {
							actionsOflegalMovesInTheState.add(temp.cloneThisBoardObj());
						}
					} else if (initialStateBoard.getPlayer().equals("O")) {
						if (temp.insertValue(i, j, "X")) {
							actionsOflegalMovesInTheState.add(temp.cloneThisBoardObj());
						}
					}
				}
			}
			// recursing all legal actions for evaluation again
			// System.out.println("legal actions list size:
			// "+actionsOflegalMovesInTheState.size());
			for (int i = 0; i < actionsOflegalMovesInTheState.size(); i++) {
				// System.out.println("legal action board \n" +
				// actionsOflegalMovesInTheState.get(i).toString());
				evaluate(actionsOflegalMovesInTheState.get(i), depth);
			}
		}
	}

	public static void main(String[] args) {
		OthelloMain o = new OthelloMain();
		o.runGame();
	}
}
