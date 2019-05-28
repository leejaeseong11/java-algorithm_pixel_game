import java.awt.*;
import java.util.ArrayList;

public class PixelPlayer102 extends Player {
    static final int DEPTH = 5;		// 알파베타 알고리즘의 노드 탐색 깊이
    static int originPlayer;
    static int onePattern2X2[][] = {{0,1,-1,1},{1,0,-1,1},{1,1,-1,0},
                                    {1,-1,1,0},{1,-1,0,1},{0,-1,1,1}};

    static int onePattern3X3[][] = {{1,-1,-1,-1,1,-1,-1,-1,0},
                                    {0,-1,-1,-1,1,-1,-1,-1,1},
                                    {1,-1,-1,-1,0,-1,-1,-1,1}};

	PixelPlayer102(int[][] map) { super(map); }
	public Point nextPosition(Point lastPosition) {
		originPlayer = map[(int)currentPosition.getX()][(int)currentPosition.getY()];	// 현재 플레이어가 1번 돌인지 2번 돌인지 판단

		return AlphaBetaSearch(DEPTH, map, lastPosition, originPlayer);
	}

	// 주어진 상태에서 가능한 수들의 집합을 돌려준다.
	private Point[] Actions(Point lastPosition, int[][] map) {
		ArrayList<Point> actionsPoint = new ArrayList<Point>();
		// 다음 돌이 놓을 수 있는(map의 값이 0인) 곳 검색
		for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
			if (map[(int)lastPosition.getX()][i] == 0) {
				actionsPoint.add(new Point((int) lastPosition.getX(), i));
			}
		}
		for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
			if (map[i][(int)lastPosition.getY()] == 0) {
				actionsPoint.add(new Point(i,(int)lastPosition.getY()));
			}
		}
		return actionsPoint.toArray(new Point[actionsPoint.size()]);  // arraylist -> array
	}
	private Point AlphaBetaSearch(int depth, int[][] map, Point lastPosition, int player) {
        int max = -100;
        Point nextDolPosition = new Point(0, 0);
        Point[] actions = Actions(lastPosition, map);	// 현재 위치에서 둘 수 있는 위치들 생성
        for (Point i : actions) {
            int [][] myMap = ArrayCopy(map);
            myMap[(int)i.getX()][(int)i.getY()] = player;	// 돌 i를 둔 새로운 맵 생성
            int result = MaxValue(depth, myMap, i, player, -100, 100);	// MaxValue 호출
            if (result > max) {
                max = result;
                nextDolPosition.setLocation(i.getX(), i.getY());
            }
        }
        return nextDolPosition;
    }
	// Max값 반환해주는 메소드
	private int MaxValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
	    if (TerminalTest(lastPosition, player, map)) return 100;	// player가 이겼을 경우 100 반환
	    if (depth == 0) return Result(map, originPlayer);					// leaf 노드에 도달하면 평가함수 호출
        int max = -100;

        Point[] actions = Actions(lastPosition, map);	// 현재 위치에서 둘 수 있는 위치들 생성
		// 노드 생성
        for (Point i : actions) {
            int [][] myMap = ArrayCopy(map);
            myMap[(int)i.getX()][(int)i.getY()] = NotPlayer(player);	// 돌 i를 둔 새로운 맵 생성
            int result = MinValue(depth - 1, myMap, i, NotPlayer(player), alpha, beta);	// MinValue 호출

            if (result > max) max = result;		// max값 갱신
            if (max >= beta) return max;		// max가 beta보다 크면 유망하지 않으므로 검사 중지
            if (max > alpha) alpha = max;		// alpha를 가장 큰 값으로 갱신
        }
        return max;
	}
	// Min값 반환해주는 메소드
	private int MinValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
		if (TerminalTest(lastPosition, player, map)) return -100;	// player가 이겼을 경우 100 반환
		if (depth == 0) return Result(map, originPlayer);		// leaf 노드에 도달하면 평가함수 호출
		int min = 100;

		Point[] actions = Actions(lastPosition, map);	// 현재 위치에서 둘 수 있는 위치들 생성
		for (Point i : actions) {
			int [][] myMap = ArrayCopy(map);
			myMap[(int)i.getX()][(int)i.getY()] = NotPlayer(player);	// 돌 i를 둔 새로운 맵 생성
			int result = MaxValue(depth - 1, myMap, i, NotPlayer(player), alpha, beta);	// MaxValue 호출

			if (result < min) min = result;		// min값 갱신
			if (min <= alpha) return min;		// min이 alpha
			if (min < beta) beta = min;
		}
		return min;
	}

    private int Result(int[][] map, int player) {
        //picture 2 -> 2*2 사이즈에서 이길 수 있는 모형들
        //picture 3 -> 3*3 사이즈에서 이길 수 있는 모형들
        //count means counting the model which could win
        int myCount = 0;
        int oppoCount = 0;
        int result = 0;
        int temp[] = null;
        for(int i=0; i<7; i++){
            for(int j=0; j<7 ; j++){
                temp = copyPattern(2,i,j,map);
                myCount += arrayCompare(player, temp, onePattern2X2);
                oppoCount += arrayCompare(NotPlayer(player), temp, onePattern2X2);
            }
        }
        for(int i=0; i<6; i++){
            for (int j = 0; j < 6; j++) {
                temp = copyPattern(3,i,j,map);
                myCount += arrayCompare(player, temp, onePattern3X3);
                oppoCount += arrayCompare(NotPlayer(player), temp, onePattern3X3);
            }
        }
        result = myCount - oppoCount;
        return result;
    }
    //Function used for comparing array
    private int[] copyPattern(int size,int row, int col, int[][] map){
        int[] temp = new int[size * size];
        int cut=0;
        for(int i = row; i < row + size; i++) {
            for (int j = col; j < col + size; j++) {
                temp[cut] = map[i][j];
                cut++;
            }
        }
        return temp;
    }
    private int arrayCompare(int player, int[] temp, int[][] pattern){
        int cnt = 0, midCnt = 0;

        for (int i = 0; i < pattern.length; i++) {
            midCnt = 0;
            for (int j = 0; j < pattern[0].length; j++) {
                if (pattern[i][j] == -1) midCnt++;
                else if (pattern[i][j] == 1 && temp[j] == player) midCnt++;
                else if (pattern[i][j] == 0 && temp[j] == 0) midCnt++;
                else break;
            }
            if (midCnt == pattern[0].length) cnt++;
        }
        return cnt;
    }
	// 2차원 배열 복사 메소드
	// 사용법: arrayDest = ArrayCopy(arraySrc)
	private int[][] ArrayCopy(int[][] src) {
		if (src == null) return null;
		int[][] dest = new int[src.length][src[0].length];

		for (int i = 0; i < src.length; i++) {
			System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
		}

		return dest;
	}
	// 상대 플레이어 번호를 출력하는 메소드
	private int NotPlayer(int player) { return player == 1 ? 2 : 1;	}
	// 이긴 상태인지 확인 하는 메소드
	private boolean TerminalTest(Point lastPosition, int player, int[][] map) {
		int x = (int) lastPosition.getY();
		int y = (int) lastPosition.getX();

		for (int i = 0; i < 9; i++) {
			int count = 0;
			Point[] newPoint = new Point[3];
			for (int j = 0; j < 3; j++) {
				newPoint[j] = new Point();
				newPoint[j].setLocation(PixelTester.inspectionList[i][j][1] + x, PixelTester.inspectionList[i][j][0] + y);
				if (newPoint[j].y >= 0 && newPoint[j].y < PixelTester.SIZE_OF_BOARD && newPoint[j].x >= 0 && newPoint[j].x < PixelTester.SIZE_OF_BOARD) {
					if (map[newPoint[j].y][newPoint[j].x] == player) {
						count += 1;
					}
				}
			}
			if (count == 3) return true;
		}
		return false;
	}
}
