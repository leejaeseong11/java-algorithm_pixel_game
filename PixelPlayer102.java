import java.awt.*;
import java.util.ArrayList;

public class PixelPlayer102 extends Player {
    static final int DEPTH = 5;		// ���ĺ�Ÿ �˰����� ��� Ž�� ����
    static int originPlayer;
    static int onePattern2X2[][] = {{0,1,-1,1},{1,0,-1,1},{1,1,-1,0},
                                    {1,-1,1,0},{1,-1,0,1},{0,-1,1,1}};

    static int onePattern3X3[][] = {{1,-1,-1,-1,1,-1,-1,-1,0},
                                    {0,-1,-1,-1,1,-1,-1,-1,1},
                                    {1,-1,-1,-1,0,-1,-1,-1,1}};

	PixelPlayer102(int[][] map) { super(map); }
	public Point nextPosition(Point lastPosition) {
		originPlayer = map[(int)currentPosition.getX()][(int)currentPosition.getY()];	// ���� �÷��̾ 1�� ������ 2�� ������ �Ǵ�

		return AlphaBetaSearch(DEPTH, map, lastPosition, originPlayer);
	}

	// �־��� ���¿��� ������ ������ ������ �����ش�.
	private Point[] Actions(Point lastPosition, int[][] map) {
		ArrayList<Point> actionsPoint = new ArrayList<Point>();
		// ���� ���� ���� �� �ִ�(map�� ���� 0��) �� �˻�
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
        Point[] actions = Actions(lastPosition, map);	// ���� ��ġ���� �� �� �ִ� ��ġ�� ����
        for (Point i : actions) {
            int [][] myMap = ArrayCopy(map);
            myMap[(int)i.getX()][(int)i.getY()] = player;	// �� i�� �� ���ο� �� ����
            int result = MaxValue(depth, myMap, i, player, -100, 100);	// MaxValue ȣ��
            if (result > max) {
                max = result;
                nextDolPosition.setLocation(i.getX(), i.getY());
            }
        }
        return nextDolPosition;
    }
	// Max�� ��ȯ���ִ� �޼ҵ�
	private int MaxValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
	    if (TerminalTest(lastPosition, player, map)) return 100;	// player�� �̰��� ��� 100 ��ȯ
	    if (depth == 0) return Result(map, originPlayer);					// leaf ��忡 �����ϸ� ���Լ� ȣ��
        int max = -100;

        Point[] actions = Actions(lastPosition, map);	// ���� ��ġ���� �� �� �ִ� ��ġ�� ����
		// ��� ����
        for (Point i : actions) {
            int [][] myMap = ArrayCopy(map);
            myMap[(int)i.getX()][(int)i.getY()] = NotPlayer(player);	// �� i�� �� ���ο� �� ����
            int result = MinValue(depth - 1, myMap, i, NotPlayer(player), alpha, beta);	// MinValue ȣ��

            if (result > max) max = result;		// max�� ����
            if (max >= beta) return max;		// max�� beta���� ũ�� �������� �����Ƿ� �˻� ����
            if (max > alpha) alpha = max;		// alpha�� ���� ū ������ ����
        }
        return max;
	}
	// Min�� ��ȯ���ִ� �޼ҵ�
	private int MinValue(int depth, int[][] map, Point lastPosition, int player, int alpha, int beta) {
		if (TerminalTest(lastPosition, player, map)) return -100;	// player�� �̰��� ��� 100 ��ȯ
		if (depth == 0) return Result(map, originPlayer);		// leaf ��忡 �����ϸ� ���Լ� ȣ��
		int min = 100;

		Point[] actions = Actions(lastPosition, map);	// ���� ��ġ���� �� �� �ִ� ��ġ�� ����
		for (Point i : actions) {
			int [][] myMap = ArrayCopy(map);
			myMap[(int)i.getX()][(int)i.getY()] = NotPlayer(player);	// �� i�� �� ���ο� �� ����
			int result = MaxValue(depth - 1, myMap, i, NotPlayer(player), alpha, beta);	// MaxValue ȣ��

			if (result < min) min = result;		// min�� ����
			if (min <= alpha) return min;		// min�� alpha
			if (min < beta) beta = min;
		}
		return min;
	}

    private int Result(int[][] map, int player) {
        //picture 2 -> 2*2 ������� �̱� �� �ִ� ������
        //picture 3 -> 3*3 ������� �̱� �� �ִ� ������
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
	// 2���� �迭 ���� �޼ҵ�
	// ����: arrayDest = ArrayCopy(arraySrc)
	private int[][] ArrayCopy(int[][] src) {
		if (src == null) return null;
		int[][] dest = new int[src.length][src[0].length];

		for (int i = 0; i < src.length; i++) {
			System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
		}

		return dest;
	}
	// ��� �÷��̾� ��ȣ�� ����ϴ� �޼ҵ�
	private int NotPlayer(int player) { return player == 1 ? 2 : 1;	}
	// �̱� �������� Ȯ�� �ϴ� �޼ҵ�
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
