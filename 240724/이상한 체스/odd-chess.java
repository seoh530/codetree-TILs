import java.util.Scanner;

import java.util.ArrayList;

class Pair {
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {
    public static final int INT_MAX = Integer.MAX_VALUE;
    public static final int DIR_NUM = 4;
    public static final int CHESS_TYPE_NUM = 5;
    public static final int MAX_M = 8;
    public static final int MAX_N = 8;
    
    public static int n, m;
    public static int minArea = INT_MAX;
    public static ArrayList<Pair> chessPieces = new ArrayList<>();
    public static int[][] board = new int[MAX_N][MAX_M];
    
    // 말들의 방향을 표시합니다.
    public static int[][] pieceDir = new int[MAX_N][MAX_M];
    
    // 자신의 말로 갈 수 있는 영역을 표시합니다.
    public static boolean[][] visited = new boolean[MAX_N][MAX_M];
    
    // 입력으로 주어진 방향에 대해
    // 말의 종류마다 북동남서 방향으로
    // 이동이 가능한지 표시합니다.
    public static int[][] canMove = new int[][]{
        {},
        {1, 0, 0, 0},
        {0, 1, 0, 1},
        {1, 1, 0, 0},
        {1, 1, 0, 1},
        {1, 1, 1, 1}
    };
    
    public static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }
    
    // 움직일 수 있는 곳인지 여부를 확인합니다.
    public static boolean canGo(int x, int y) {
        return inRange(x, y) && board[x][y] != 6;
    }
    
    // (startX, startY)에서 해당 타입의 말이 특정 방향을 
    // 바라보고 있을 때 갈 수 있는 곳들을 전부 표시합니다.
    public static void fill(int startX, int startY, int pieceNum, int faceDir) {
        // 북동남서 순으로 방향을 설정합니다.
        int[] dx = new int[]{-1, 0, 1, 0};
        int[] dy = new int[]{0, 1, 0, -1};
        
        for(int i = 0; i < 4; i++) {
            // 해당 말이 움직일 수 있는 방향인지를 확인합니다.
            // 움직일 수 없다면 pass합니다.
            if(canMove[pieceNum][i] == 0)
                continue;
            
            // 갈 수 있다면, 끝날때까지 계속 진행합니다.
            // 방향은 faceDir만큼 시계방향으로 
            // 회전했을 때를 기준으로 움직입니다.
            int x = startX, y = startY;
            int moveDir = (i + faceDir) % 4;
            while(true) {
                visited[x][y] = true;
                int nx = x + dx[moveDir], ny = y + dy[moveDir];
                if(canGo(nx, ny)) {
                    x = nx; y = ny;
                }
                else
                    break;
            }
        }
    }
    
    // n 개의 체스 말의 방향이 정해졌을 때 이동할 수 없는 영역의 넓이를 반환합니다.
    public static int getArea() {
        // visited 배열을 초기화합니다.
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                visited[i][j] = false;
    
        for(int i = 0; i < chessPieces.size(); i++) {
            int x = chessPieces.get(i).x;
            int y = chessPieces.get(i).y;
            
            // 해당 말이 정해진 방향에 있을 때 갈 수 있는 곳들을 전부 표시합니다.
            fill(x, y, board[x][y], pieceDir[x][y]);
        }
        
        int area = 0;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                if(!visited[i][j] && board[i][j] != 6)
                    area++;
    
        return area;
    }
    
    public static void searchMinArea(int cnt) {
        // 자신의 말들의 방향이 전부 결정되었을 때, 갈 수 없는 넓이를 계산하여 갱신합니다.
        if(cnt == chessPieces.size())  {
            minArea = Math.min(minArea, getArea());
            return;
        }
    
        // cnt 말의 방향을 설정합니다. 
        int pieceX = chessPieces.get(cnt).x;
        int pieceY = chessPieces.get(cnt).y;
        
        for(int i = 0; i < 4; i++) {
            pieceDir[pieceX][pieceY] = i;
            searchMinArea(cnt + 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) {
                board[i][j] = sc.nextInt();
                if(1 <= board[i][j] && board[i][j] <= 5)
                    chessPieces.add(new Pair(i, j));
            }

        searchMinArea(0);
        System.out.print(minArea);
    }
}