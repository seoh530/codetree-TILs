package samsung;

import java.util.*;

public class Main {

	static final int N_large = 5;
	static final int N_small = 3;
	static int[][] board;
	static int[][] tempBoard;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int K = sc.nextInt(); // explore count
		int M = sc.nextInt(); // artifact count
		board = new int[N_large][N_large];
		tempBoard = new int[N_large][N_large];

		for (int i = 0; i < N_large; i++) {
			for (int j = 0; j < N_large; j++) {
				board[i][j] = sc.nextInt();
			}
		}
		for (int i = 1; i <= N_small; i++) {
			for (int j = 1; j <= N_small; j++) {
				rotation90(i, j);
				System.out.println("\ni: "+i+" j: "+j+" val: "+board[i][j]);
				System.out.println("\n================");
			}

		}

	}

	// 90 degree rotation
	private static void rotation90(int i, int j) {
		// start with top left block
		int[] dx = { -1, -1, -1, 0, 1, 1, 1, 0 };
		int[] dy = { -1, 0, 1, 1, 1, 0, -1, -1 };
		int idx = 0;

		for (int k = 0; k < N_large; k++) {
			for (int l = 0; l < N_large; l++) {
				tempBoard[k][l] = board[k][l];
			}
		}

		for (int n = 0; n < 8; n++) {
			idx = (n + 2) % 8;
			int oriX = i + dx[n]; // 0
			int oriY = j + dy[n];// 0
			int newX = i + dx[idx];
			int newY = j + dy[idx];

			tempBoard[oriX][oriY] = board[newX][newY];

		}

		for (int u = 0; u < N_large; u++) {
			System.out.println();
			for (int w = 0; w < N_large; w++) {
				System.out.print(tempBoard[u][w] + " ");
			}
		}
	}
}