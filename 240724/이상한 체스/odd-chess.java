import java.util.*;

public class Main {
    static int n, m;
    static int[][] board;
    static boolean[][] visited;
    static List<int[]> mine = new ArrayList<>();
    static int[] dx = {-1, 1, 0, 0}; // 상하좌우
    static int[] dy = {0, 0, -1, 1}; // 상하좌우
    static int minBlocked = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        m = sc.nextInt();

        board = new int[n][m];
        visited = new boolean[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = sc.nextInt();
                if (board[i][j] >= 1 && board[i][j] <= 5) {
                    mine.add(new int[] {board[i][j], i, j});
                }
            }
        }

        // 각 말들의 이동 방향 설정
        setDirections(0);
        
        System.out.println(minBlocked);
    }

    // 각 말들의 이동 방향을 설정하는 함수
    static void setDirections(int index) {
        if (index == mine.size()) {
            // 모든 말의 방향을 설정한 후, 이동 범위를 계산
            calculateBlockedCells();
            return;
        }

        int[] piece = mine.get(index);
        int type = piece[0];
        int x = piece[1];
        int y = piece[2];

        if (type == 1) {
            // Type 1: 한 방향 (상하좌우 중 하나)
            for (int dir = 0; dir < 4; dir++) {
                move(x, y, dir);
                setDirections(index + 1);
                resetVisited(x, y, dir);
            }
        } else if (type == 2) {
            // Type 2: 두 방향 (상하 or 좌우)
            for (int i = 0; i < 2; i++) {
                move(x, y, i);
                move(x, y, i + 2);
                setDirections(index + 1);
                resetVisited(x, y, i);
                resetVisited(x, y, i + 2);
            }
        } else if (type == 3) {
            // Type 3: 직각 방향 (상좌, 상우, 하좌, 하우)
            move(x, y, 0); move(x, y, 2);
            setDirections(index + 1);
            resetVisited(x, y, 0); resetVisited(x, y, 2);

            move(x, y, 0); move(x, y, 3);
            setDirections(index + 1);
            resetVisited(x, y, 0); resetVisited(x, y, 3);

            move(x, y, 1); move(x, y, 2);
            setDirections(index + 1);
            resetVisited(x, y, 1); resetVisited(x, y, 2);

            move(x, y, 1); move(x, y, 3);
            setDirections(index + 1);
            resetVisited(x, y, 1); resetVisited(x, y, 3);
        } else if (type == 4) {
            // Type 4: 세 방향 (상하좌, 상하우, 상좌우, 하좌우)
            for (int i = 0; i < 4; i++) {
                move(x, y, i);
                move(x, y, (i + 1) % 4);
                move(x, y, (i + 2) % 4);
                setDirections(index + 1);
                resetVisited(x, y, i);
                resetVisited(x, y, (i + 1) % 4);
                resetVisited(x, y, (i + 2) % 4);
            }
        } else if (type == 5) {
            // Type 5: 네 방향 (상하좌우)
            for (int dir = 0; dir < 4; dir++) {
                move(x, y, dir);
            }
            setDirections(index + 1);
            for (int dir = 0; dir < 4; dir++) {
                resetVisited(x, y, dir);
            }
        }
    }

    // 말의 이동 범위를 설정하는 함수
    static void move(int x, int y, int dir) {
        int nx = x, ny = y;
        while (true) {
            nx += dx[dir];
            ny += dy[dir];
            if (!isValid(nx, ny) || board[nx][ny] == 6) break;
            visited[nx][ny] = true;
        }
    }

    // 방문 기록을 초기화하는 함수
    static void resetVisited(int x, int y, int dir) {
        int nx = x, ny = y;
        while (true) {
            nx += dx[dir];
            ny += dy[dir];
            if (!isValid(nx, ny) || board[nx][ny] == 6) break;
            visited[nx][ny] = false;
        }
    }

    // 유효한 위치인지 확인하는 함수
    static boolean isValid(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }

    // 갈 수 없는 영역을 계산하는 함수
    static void calculateBlockedCells() {
        boolean[][] tempVisited = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (visited[i][j]) {
                    tempVisited[i][j] = true;
                }
            }
        }

        int blockedCells = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 0 && !tempVisited[i][j]) {
                    blockedCells++;
                }
            }
        }

        minBlocked = Math.min(minBlocked, blockedCells);
    }
}