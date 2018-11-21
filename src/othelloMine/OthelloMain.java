package othelloMine;

import java.util.Scanner;

public class OthelloMain {
	Board board = new Board();
	
	
	public void evalBoard(Board board) {
		
	}
	
	
	
	public void runGame() {
		System.out.println(board.toString());
		while (true) {
			try {
				//first move player X
				Scanner scanner = new Scanner(System.in);
				System.out.println("enter cell x,y");
				String[] input = scanner.nextLine().split(",");
				board.insertValue(Integer.parseInt(input[0]), Integer.parseInt(input[1]), "X");
			//	String[] oponent= checkGame(board).split(",");
			//	board.insertValue(Integer.parseInt(oponent[0]), Integer.parseInt(oponent[1]), "O");
				
				
				System.out.println(board.toString());
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
	
	
	
	public void eval(Board board) {
		
		//1. termina state(check before move from scanner)
		//2. loop throu possible moves choose best one for a give player + recursive
		
	}
	

	
	
	
	public static void main(String[] args) {
		OthelloMain om = new OthelloMain();
		om.runGame();
	}
}
