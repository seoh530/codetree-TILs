import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static int n, m;
	public static List<int[]> patient = new ArrayList<>();
	public static List<int[]> hospital = new ArrayList<>();
	public static int minDistance = Integer.MAX_VALUE;
	public static int[][] map;
	public static boolean[] selected;

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

	public static void hospitalCombi(boolean[] selected, int start, int cnt) {
		if (cnt == m) {
			calculateDist(selected);
			return;
		}
		// hospital combination
		for (int i = start; i < hospital.size(); i++) {
			selected[i] = true;
			hospitalCombi(selected, i + 1, cnt + 1);
			selected[i] = false;
		}

	}

	public static void calculateDist(boolean[] selected) {
		int sumDist = 0;
		for (int[] person : patient) {
			int minDist = Integer.MAX_VALUE;
			for (int i = 0; i < hospital.size(); i++) {
				if (selected[i]) {
					int[] hospitals = hospital.get(i);
					int dist = Math.abs(person[0] - hospitals[0]) + Math.abs(person[1] - hospitals[1]);
					minDist = Math.min(minDist, dist);
				}
			}
			sumDist += minDist;
            if(sumDist >= minDistance) return;
		}
		minDistance = Math.min(minDistance, sumDist);
	}

}