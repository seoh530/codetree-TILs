import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Pair {
	int x, y;

	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class Main {
	public static final int INT_MAX = Integer.MAX_VALUE;
	public static final int MAX_N = 50;
	public static final int DIR_NUM = 4;

	// 전역 변수 선언:
	public static int n, m;

	public static int[][] a = new int[MAX_N][MAX_N];

	public static ArrayList<Pair> hospitals = new ArrayList<>();
	public static ArrayList<Pair> selectedHos = new ArrayList<>();
	// bfs에 필요한 변수들 입니다.
	public static Queue<Pair> q = new LinkedList<>();
	public static boolean[][] visited = new boolean[MAX_N][MAX_N];
	public static int[][] step = new int[MAX_N][MAX_N];

	public static int ans = INT_MAX;

	// 범위가 격자 안에 들어가는지 확인합니다.
	public static boolean inRange(int x, int y) {
		return 0 <= x && x < n && 0 <= y && y < n;
	}

	// 범위를 벗어나지 않으면서 벽이 아니고, 방문한적이
	// 없어야 갈 수 있습니다.
	public static boolean canGo(int x, int y) {
		return inRange(x, y) && a[x][y] != 1 && !visited[x][y];
	}

	// queue에 새로운 위치를 추가하고
	// 방문 여부를 표시해줍니다.
	// 시작점으로 부터의 최단거리 값도 갱신해줍니다.
	public static void push(int x, int y, int newStep) {
		q.add(new Pair(x, y));
		visited[x][y] = true;
		step[x][y] = newStep;
	}

	// visited, step 배열을 초기화합니다.
	public static void initialize() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				visited[i][j] = false;
				step[i][j] = 0;
			}
	}

	// BFS를 통해
	// 선택된 병원들로부터
	// 가장 거리가 먼 바이러스까지의 거리를 구합니다.
	public static int findMaxDist() {

		while (!q.isEmpty()) {
			// queue에서 가장 먼저 들어온 원소를 뺍니다.
			Pair currPos = q.poll();
			int x = currPos.x;
			int y = currPos.y;

			int[] dx = new int[] { 1, -1, 0, 0 };
			int[] dy = new int[] { 0, 0, 1, -1 };

			// queue에서 뺀 원소의 위치를 기준으로 4 방향을 확인합니다.
			for (int dir = 0; dir < DIR_NUM; dir++) {
				int nx = x + dx[dir];
				int ny = y + dy[dir];

				if (canGo(nx, ny))
					push(nx, ny, step[x][y] + 1);
			}
		}

		int maxDist = 0;

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				// 바이러스인 경우에만 거리를 갱신합니다.
				if (a[i][j] == 0) {
					if (visited[i][j])
						maxDist = Math.max(maxDist, step[i][j]);
					else
						maxDist = -1;
				}
			}

		return maxDist;
	}

	// 선택된 병원으로부터 모든 바이러스를 없애기 위해
	// 걸리는 시간을 계산합니다.
	public static int elapsedTimeToKillAllVirus(ArrayList<Pair> selectedHos) {
		// BFS를 다시 돌리기 전에
		// visited, step 배열을 초기화합니다.
		initialize();

		// 선택된 병원들을 시작으로 하여 BFS를 1번 돌립니다.
		for (int i = 0; i < selectedHos.size(); i++)
			push(selectedHos.get(i).x, selectedHos.get(i).y, 0);

		int maxElapsedTime = findMaxDist();
		return maxElapsedTime;
	}

	// 모든 병원 조합을 생성하여 최소 시간을 찾습니다.
	public static void generateCombinations(int start, int depth, ArrayList<Pair> selectedHos) {
		if (depth == m) {
			ans = Math.min(ans, elapsedTimeToKillAllVirus(selectedHos));
			return;
		}

		for (int i = start; i < hospitals.size(); i++) {
			selectedHos.add(hospitals.get(i));
			generateCombinations(i + 1, depth + 1, selectedHos);
			selectedHos.remove(selectedHos.size() - 1);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 입력:
		n = sc.nextInt();
		m = sc.nextInt();

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				a[i][j] = sc.nextInt();

				// Backtracking을 선택할 병원 index 기준으로
				// 쉽게 돌리기 위해 병원 위치만 따로 저장합니다.
				if (a[i][j] == 2)
					hospitals.add(new Pair(i, j));
			}

		// 최소 시간을 구합니다.
		generateCombinations(0, 0, selectedHos);

		System.out.print(ans);
	}
}