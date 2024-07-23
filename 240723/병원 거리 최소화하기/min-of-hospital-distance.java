import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static int n, m;
	static int[][] map;
	static List<int[]> patient = new ArrayList<>();
	static List<int[]> hospital = new ArrayList<>();
	static boolean[] selected;
	static int minDistance = Integer.MAX_VALUE;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();

		map = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = sc.nextInt();
				if (map[i][j] == 1)
					patient.add(new int[] { i, j });
				else if (map[i][j] == 2)
					hospital.add(new int[] { i, j });
			}
		}
		selected = new boolean[hospital.size()];
		hospitalCombi(selected, 0, 0);
		System.out.println(minDistance);
	}

	// hospital select combination
	private static void hospitalCombi(boolean[] selected, int start, int cnt) {
		// after selection, calculate distance between hospital and patient
		if (cnt == m) {
			calculateDist(selected);
			return;
		}
		// hospital select
		for (int i = start; i < hospital.size(); i++) {
			selected[i] = true;
			hospitalCombi(selected, i+1, cnt+1);
			selected[i] = false;
		}

	}

	private static void calculateDist(boolean[] selected) {
		int total = 0;
		for (int[] person : patient) {
			int minDist = Integer.MAX_VALUE;
			for (int i = 0; i < hospital.size(); i++) {
				if (selected[i]) {
					int[] h = hospital.get(i);
					int tempDist = Math.abs(person[0]- h[0]) + Math.abs(person[1]- h[1]);
					minDist = Math.min(minDist, tempDist);
				}
			}
			total += minDist;
		}
		minDistance = Math.min(minDistance, total);
		
	}

}