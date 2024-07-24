public class Main {
    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.
    }
import java.util.*;

public class ArtifactExploration {
    static int[][] grid = new int[5][5];
    static List<Integer> artifactPieces;
    static int artifactIndex = 0;
    static int[] dRow = {0, 1, 0, -1};
    static int[] dCol = {1, 0, -1, 0};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int K = sc.nextInt();
        int M = sc.nextInt();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = sc.nextInt();
            }
        }

        artifactPieces = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            artifactPieces.add(sc.nextInt());
        }

        List<Integer> results = new ArrayList<>();
        for (int turn = 0; turn < K; turn++) {
            int bestRow = -1, bestCol = -1, bestRotation = -1;
            int maxArtifactValue = -1;

            // Check all possible 3x3 sub-grids and rotations
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    for (int rotation = 0; rotation < 3; rotation++) {
                        int[][] tempGrid = deepCopyGrid(grid);
                        rotate3x3(tempGrid, i, j, rotation);
                        int value = calculateArtifactValue(tempGrid);

                        if (value > maxArtifactValue) {
                            bestRow = i;
                            bestCol = j;
                            bestRotation = rotation;
                            maxArtifactValue = value;
                        }
                    }
                }
            }

            if (maxArtifactValue == 0) {
                break;
            }

            rotate3x3(grid, bestRow, bestCol, bestRotation);
            int turnValue = removeAndRefillArtifacts();
            results.add(turnValue);
        }

        for (int result : results) {
            System.out.println(result);
        }

        sc.close();
    }

    private static int[][] deepCopyGrid(int[][] original) {
        int[][] copy = new int[5][5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 5);
        }
        return copy;
    }

    private static void rotate3x3(int[][] grid, int startRow, int startCol, int rotation) {
        int[][] temp = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[i][j] = grid[startRow + i][startCol + j];
            }
        }

        for (int r = 0; r < rotation; r++) {
            int[][] rotated = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotated[j][2 - i] = temp[i][j];
                }
            }
            temp = rotated;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[startRow + i][startCol + j] = temp[i][j];
            }
        }
    }

    private static int calculateArtifactValue(int[][] grid) {
        boolean[][] visited = new boolean[5][5];
        int totalValue = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    int value = bfs(grid, visited, i, j);
                    if (value >= 3) {
                        totalValue += value;
                    }
                }
            }
        }

        return totalValue;
    }

    private static int bfs(int[][] grid, boolean[][] visited, int row, int col) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        visited[row][col] = true;
        int count = 0;
        int type = grid[row][col];

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            count++;

            for (int d = 0; d < 4; d++) {
                int newRow = current[0] + dRow[d];
                int newCol = current[1] + dCol[d];

                if (newRow >= 0 && newRow < 5 && newCol >= 0 && newCol < 5 && !visited[newRow][newCol] && grid[newRow][newCol] == type) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }

        return count;
    }

    private static int removeAndRefillArtifacts() {
        boolean[][] visited = new boolean[5][5];
        int totalValue = 0;

        while (true) {
            boolean foundArtifact = false;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!visited[i][j]) {
                        List<int[]> artifact = new ArrayList<>();
                        if (collectArtifact(grid, visited, i, j, artifact)) {
                            totalValue += artifact.size();
                            foundArtifact = true;
                            for (int[] pos : artifact) {
                                grid[pos[0]][pos[1]] = 0;
                            }
                        }
                    }
                }
            }

            if (!foundArtifact) {
                break;
            }

            refillGrid();
        }

        return totalValue;
    }

    private static boolean collectArtifact(int[][] grid, boolean[][] visited, int row, int col, List<int[]> artifact) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        visited[row][col] = true;
        int type = grid[row][col];
        artifact.add(new int[]{row, col});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            for (int d = 0; d < 4; d++) {
                int newRow = current[0] + dRow[d];
                int newCol = current[1] + dCol[d];

                if (newRow >= 0 && newRow < 5 && newCol >= 0 && newCol < 5 && !visited[newRow][newCol] && grid[newRow][newCol] == type) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                    artifact.add(new int[]{newRow, newCol});
                }
            }
        }

        return artifact.size() >= 3;
    }

    private static void refillGrid() {
        for (int col = 0; col < 5; col++) {
            List<Integer> newCol = new ArrayList<>();

            for (int row = 4; row >= 0; row--) {
                if (grid[row][col] != 0) {
                    newCol.add(grid[row][col]);
                }
            }

            int newSize = newCol.size();
            for (int row = 4; row >= 0; row--) {
                if (row >= 5 - newSize) {
                    grid[row][col] = newCol.get(4 - row);
                } else {
                    grid[row][col] = artifactPieces.get(artifactIndex);
                    artifactIndex = (artifactIndex + 1) % artifactPieces.size();
                }
            }
        }
    }
}