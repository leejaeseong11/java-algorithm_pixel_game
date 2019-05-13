import java.awt.Point;

public class PixelPlayer06 extends Player{
	PixelPlayer06(int[][] map) {
		super(map);
	}

	Point nextPosition(Point lastPosition) {
		
		int x = (int)lastPosition.getX();
		int y = (int)lastPosition.getY();
		Point nextPosition = null;
		
		int[][] myMap = new int [8][8]; // ����ġ�� ������ ���� ��	
		
		for(int i = 0; i < 8; i++) // �ʱ� ����ġ ����
			for(int j = 0; j < 8; j++)
				myMap[i][j] = 0;

//		myMap[0][0] = -1; // ���� ����� ��ġ
//		myMap[0][7] = -1;
//		myMap[7][0] = -1;
//		myMap[7][7] = -1;
//		myMap[3][4] = -1; // �ʱ� �������� ���� ��ġ
//		myMap[4][3] = -1;		
//		myMap[x][y] = -1;
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(map[i][j] == 1) {
					if(i != 0) // ����ġ �ο�
						myMap[i-1][j] += 2;
					if(i != 7)
						myMap[i+1][j] += 2;
					if(j != 0)
						myMap[i][j-1] += 2;
					if(j != 7)
						myMap[i][j+1] += 2;
					if(i != 7 && j != 7)
						myMap[i+1][j+1] += 3;
					if(i != 0 && j != 0)
						myMap[i-1][j-1] += 3;
					if(i != 7 && j != 0)
						myMap[i+1][j-1] += 1;
					if(i != 0 && j != 7)
						myMap[i-1][j+1] += 1;
				}
			}
		}

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(map[i][j] != 0)
			myMap[i][j] = -1; // ���� ��ġ ����ġ ����
			}
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) 
				System.out.print(myMap[i][j] + " ");
			System.out.println();
		}
		System.out.println(x + " c " + y);
		
		int best_col = -1, best_wid = -1; // ����ġ�� �ִ��� ��ġ�� ����
		int col_index = -1, wid_index = -1; // ����ġ�� �ִ��� ��ġ�� �ε���
		
		for(int i = 0; i < 8; i++) {
			if(best_col <= myMap[x][i]) {
				best_col = myMap[x][i];
				col_index = i;
			}
		}
		
		for(int i = 0; i < 8; i++) {
			if(best_wid <= myMap[i][y]) {
				best_wid = myMap[i][y];
				wid_index = i;
			}
		}
		
		if(best_col >= best_wid)
			nextPosition = new Point(x, col_index);
		else
			nextPosition = new Point(wid_index, y);
		
		return nextPosition;
	}
}
