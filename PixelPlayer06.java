import java.awt.Point;

public class PixelPlayer06 extends Player{
	PixelPlayer06(int[][] map) {
		super(map);
	}

	Point nextPosition(Point lastPosition) {
		
		int x = (int)lastPosition.getX();
		int y = (int)lastPosition.getY();
		Point nextPosition = null;
		
		int[][] myMap = new int [8][8]; // 가중치를 저장할 나의 맵	
		
		for(int i = 0; i < 8; i++) // 초기 가중치 설정
			for(int j = 0; j < 8; j++)
				myMap[i][j] = 0;

//		myMap[0][0] = -1; // 맵을 벗어나는 위치
//		myMap[0][7] = -1;
//		myMap[7][0] = -1;
//		myMap[7][7] = -1;
//		myMap[3][4] = -1; // 초기 설정으로 놓인 위치
//		myMap[4][3] = -1;		
//		myMap[x][y] = -1;
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(map[i][j] == 1) {
					if(i != 0) // 가중치 부여
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
			myMap[i][j] = -1; // 놓인 위치 가중치 제거
			}
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) 
				System.out.print(myMap[i][j] + " ");
			System.out.println();
		}
		System.out.println(x + " c " + y);
		
		int best_col = -1, best_wid = -1; // 가중치가 최대인 위치를 저장
		int col_index = -1, wid_index = -1; // 가중치가 최대인 위치의 인덱스
		
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
