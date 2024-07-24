import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static final int N_large = 5; // 고대 문명 전체 격자 크기
    static final int N_small = 3; // 회전시킬 격자의 크기

    static class Board {
        int[][] a = new int[N_large][N_large];

        public Board() {
            for (int i = 0; i < N_large; i++) {
                for (int j = 0; j < N_large; j++) {
                    a[i][j] = 0;
                }
            }
        }

        private boolean inRange(int y, int x) {
            return 0 <= y && y < N_large && 0 <= x && x < N_large;
        }

        public Board rotate(int sy, int sx, int cnt) {
            Board result = new Board();
            for (int i = 0; i < N_large; i++) {
                for (int j = 0; j < N_large; j++) {
                    result.a[i][j] = this.a[i][j];
                }
            }
            for (int k = 0; k < cnt; k++) {
                int tmp = result.a[sy + 0][sx + 2];
                result.a[sy + 0][sx + 2] = result.a[sy + 0][sx + 0];
                result.a[sy + 0][sx + 0] = result.a[sy + 2][sx + 0];
                result.a[sy + 2][sx + 0] = result.a[sy + 2][sx + 2];
                result.a[sy + 2][sx + 2] = tmp;
                tmp = result.a[sy + 1][sx + 2];
                result.a[sy + 1][sx + 2] = result.a[sy + 0][sx + 1];
                result.a[sy + 0][sx + 1] = result.a[sy + 1][sx + 0];
                result.a[sy + 1][sx + 0] = result.a[sy + 2][sx + 1];
                result.a[sy + 2][sx + 1] = tmp;
            }
            return result;
        }

        public int calScore() {
            int score = 0;
            boolean[][] visit = new boolean[N_large][N_large];
            int[] dy = {0, 1, 0, -1}, dx = {1, 0, -1, 0};

            for (int i = 0; i < N_large; i++) {
                for (int j = 0; j < N_large; j++) {
                    if (!visit[i][j]) {
                        Queue<int[]> q = new LinkedList<>();
                        Queue<int[]> trace = new LinkedList<>();
                        q.offer(new int[]{i, j});
                        trace.offer(new int[]{i, j});
                        visit[i][j] = true;
                        while (!q.isEmpty()) {
                            int[] cur = q.poll();
                            for (int k = 0; k < 4; k++) {
                                int ny = cur[0] + dy[k], nx = cur[1] + dx[k];
                                if (inRange(ny, nx) && a[ny][nx] == a[cur[0]][cur[1]] && !visit[ny][nx]) {
                                    q.offer(new int[]{ny, nx});
                                    trace.offer(new int[]{ny, nx});
                                    visit[ny][nx] = true;
                                }
                            }
                        }
                        if (trace.size() >= 3) {
                            score += trace.size();
                            while (!trace.isEmpty()) {
                                int[] t = trace.poll();
                                a[t[0]][t[1]] = 0;
                            }
                        }
                    }
                }
            }
            return score;
        }

        public void fill(Queue<Integer> que) {
            for (int j = 0; j < N_large; j++) {
                for (int i = N_large - 1; i >= 0; i--) {
                    if (a[i][j] == 0 && !que.isEmpty()) {
                        a[i][j] = que.poll();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int K = sc.nextInt();
        int M = sc.nextInt();
        Queue<Integer> q = new LinkedList<>();
        Board board = new Board();

        for (int i = 0; i < N_large; i++) {
            for (int j = 0; j < N_large; j++) {
                board.a[i][j] = sc.nextInt();
            }
        }
        for (int i = 0; i < M; i++) {
            q.offer(sc.nextInt());
        }

        while (K-- > 0) {
            int maxScore = 0;
            Board maxScoreBoard = null;
            for (int cnt = 1; cnt <= 3; cnt++) {
                for (int sx = 0; sx <= N_large - N_small; sx++) {
                    for (int sy = 0; sy <= N_large - N_small; sy++) {
                        Board rotated = board.rotate(sy, sx, cnt);
                        int score = rotated.calScore();
                        if (maxScore < score) {
                            maxScore = score;
                            maxScoreBoard = rotated;
                        }
                    }
                }
            }
            if (maxScoreBoard == null) {
                break;
            }
            board = maxScoreBoard;
            while (true) {
                board.fill(q);
                int newScore = board.calScore();
                if (newScore == 0) break;
                maxScore += newScore;
            }
            System.out.print(maxScore + " ");
        }
    }
}