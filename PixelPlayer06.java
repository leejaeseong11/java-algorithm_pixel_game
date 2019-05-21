import java.awt.*;

public class PixelPlayer06 extends Player {
	PixelPlayer06(int[][] map) {
		super(map);
	}

	int order;
	int enemy;
	int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // 가중치를 저장할 나의 맵
	int lastRow;
	int lastCol;
	int myRow = -1; // 다음 수가 이기거나 지는 경우 바로 반환할 값을 저장
	int myCol = -1;

	Point nextPosition(Point lastPosition) {

		// 함수 순서
		// 1. 전체 가중치 초기화
		// 2. 무조건 이기는 경우 가중치 1순위 1000
		// 3. 적이 이기는 경우 가중치 2순위
		// 4. 2번과 3번이 아닐 경우 둔 3번함수 호출하여 3순위

		// 기능 구현 개요
		// 1. 현재 무조건 이길 수 있는 돌의 좌표 계산 기능 (아직 구현 안 함)
		// 2. 적이 다음 턴 놓게되면 지게되는 돌의 좌표 계산 기능 (했는데 최적화 안 함)
		// 3. 이길확률이 가장 높은 돌의 좌표 계산 기능(대각선으로 2 개의 돌일 경우 이길확률이 올라감)
		// 4. 그 돌을 놓을 경우 적이 이기는 경우 계산 (아직 구현 안 함)
		// 가중치 값이 같을 때 처리하는 것
		// 현재 탐색된 돌이 다음에 위험 할 경우 가중치를 낮게 둠

		// 행과 열의 인덱스를 0번부터 시작, 전체 바둑판 크기는 8*8(0~7 * 0~7)
		// 1번째 턴이 파란색이며, 선공이고 바둑판에 1로 표기됨, 4행3열 부터 시작
		// 2번째 턴이 빨간색이며, 후공이고 바둑판에 2로 표기됨, 3행4열 부터 시작

		lastRow = (int) lastPosition.getX();
		lastCol = (int) lastPosition.getY();

		order = PixelTester.turn;

		if (order == 1)
			enemy = 2;
		else
			enemy = 1;
		Point nextPosition = null;
		System.out.println(lastRow + " " + lastCol);

		for (int i = 0; i < 8; i++) // 초기 가중치 설정
			for (int j = 0; j < 8; j++)
				myMap[i][j] = 0;

		if (this.isWin()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}

		// 무조건 이기는 경우
		// 이기는 메소드
		// 무조건 지는 경우
		// 상대가 돌을 한개면 더 두면 이기는 경우를 막는 메소드(그 남은 한자리에 대한 가중치 설정)
		if (this.isLose()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}
		// --- 가중치 탐색 후 행렬인덱스 리턴 from ---
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1) { // 내턴에 따라 달리해줘야 함
					// 가중치 부여 (모든 조건식 재검토할 필요 >> 돌이 많아지는 경우 어떤 돌에게는 대각선이고 어떤 돌에게는 왼쪽임)
					if (i != 0)
						myMap[i - 1][j] += 2; // row 아래쪽 가중치 2 부여
					if (i != 7)
						myMap[i + 1][j] += 2; // row 위쪽 가중치 2 부여
					if (j != 0)
						myMap[i][j - 1] += 2; // col 왼쪽 가중치 2 부여
					if (j != 7)
						myMap[i][j + 1] += 2; // col 오른쪽 가중치 2 부여
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 1; // 오른쪽위 가중치 1 부여
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 1; // 왼쪽아래 가중치 1 부여
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 3; // 왼쪽위 가중치 3 부여
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 3; // 오른쪽아래 가중치 3 부여
				}
			}
		}

		// 가중치 재설정 부분
		// 1. 놓인 위치 가중치 제거
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1 || map[i][j] == 2)
					myMap[i][j] = -1;
			}
		}

		// 2. 판 이외 부분의 가중치 제거
		myMap[0][0] = -1;
		myMap[0][7] = -1;
		myMap[7][0] = -1;
		myMap[7][7] = -1;

		// row : 행(x로 변수 지정), col : 열(y로 변수 지정)
		int best_row = -1, best_col = -1; // 행렬에 대한 가중치가 최대인 값을 저장하기 위한 변수
		int row_index = -1, col_index = -1; // 행렬에 대한 가중치가 최대인 인덱스를 저장하기 위한 변수

		// 겹치는 경우 생각해봐야 함
		// row에 대한 가중치 탐색
		for (int i = 0; i < 8; i++) {
			if (best_row <= myMap[i][lastCol]) {
				best_row = myMap[i][lastCol];
				row_index = i;
			}
		}

		// col에 대한 가중치 탐색
		for (int i = 0; i < 8; i++) {
			if (best_col <= myMap[lastRow][i]) {
				best_col = myMap[lastRow][i];
				col_index = i;
			}
		}

		System.out.println("열고정 최고 인덱스 " + lastRow + " " + col_index + " " + myMap[lastRow][col_index]);
		System.out.println("행고정 최고 인덱스 " + row_index + " " + lastCol + " " + myMap[row_index][lastCol]);

		// row또는 col에 대한 가중치를 비교 후, 인덱스값 리턴 ( 똑같을 경우에도 어디가 더 유리한지)
		if (best_row >= best_col) // 행이동에 대한 가중치값이 열이동에 대한 가중치 값보다 높을 경우
			nextPosition = new Point(row_index, lastCol); // 좌표는 반대로 리턴
		else // 열이동에 대한 가중치값이 행이동에 대한 가중치 값보다 높을 경우
			nextPosition = new Point(lastRow, col_index); // 좌표는 반대로 리턴
		// --- 가중치 탐색 후 행렬인덱스 리턴 to ---

//        if (best_row > best_col) {
//            this.OneMoreAnalysis(row_index, lastCol);
//        }
//        else if (best_row < best_col) {
//            this.OneMoreAnalysis(row_index, lastCol);
//        }
//        else {
//            this.OneMoreAnalysis(row_index, lastCol);
//        }

		return nextPosition;
	}

	boolean isPossible(int row, int col) { // 돌을 놓아도 되는지 검사하는 메소드
		if (map[row][col] == 0 && (lastRow == row || lastCol == col)) {// 게임판이 비어있으면서 가로나 세로만 움직일 수 있음
			return true;
		}

		return false;
	}

	boolean isLose() { // 막지 않으면 지는 경우

		// --- 'ㄱ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//                if (row == 7) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//                    if(col == 0) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
				// 'ㄱ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 오른쪽위, 오른쪽아래)
//				if ((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7))
//					continue;
				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 'ㄱ'모양 분석
					// 'ㄱ' 모양이 중심점을 기준으로 함
					if (col > 0 && row < 7) {
						if (map[row][col - 1] == enemy && map[row + 1][col] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row < 7) {
						if (map[row][col + 1] == enemy && map[row + 1][col + 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col] == enemy && map[row - 1][col - 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}

		// --- 'ㄱ' 모양 분석 to ---

		// --- 'ㄴ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//			if (row == 0)
//				continue; // 'ㄴ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//				if (col == 7)
//					continue; // 'ㄴ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

				// 'ㄴ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 왼쪽아래, 오른쪽아래)
//				if ((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6))
//					continue;

				if (isPossible(row, col)) {

					// 상대돌을 발견할 경우 'ㄴ'모양 분석
					// 'ㄴ' 모양이 중심점을 기준으로 함
					if (col < 7 && row < 7) {
						if (map[row + 1][col] == enemy && map[row + 1][col + 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col - 1] == enemy && map[row][col - 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row > 0) {
						if (map[row - 1][col] == enemy && map[row][col + 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}
		// --- 'ㄴ' 모양 분석 to ---

		// --- '\' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//			if (row == 0 || row == 7)
//				continue; // '\' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//				if (col == 0 || col == 7)
//					continue; // '\' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
				
				// '\' 모양이 절대 만들어지지 않는 특정 좌표 건너뛰기
//				if ((row == 1 && col == 1) || (row == 6 && col == 6))
//					continue;

				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 '\'모양 분석
					// '\' 모양이 중심점을 기준으로 함
					if (col < 6 && row < 6) {
						if (map[row + 1][col + 1] == enemy && map[row + 2][col + 2] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0 && row < 7 && col < 7) {
						if (map[row - 1][col - 1] == enemy && map[row + 1][col + 1] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 1 && row > 1) {
						if (map[row - 1][col - 1] == enemy && map[row - 2][col - 2] == enemy) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}

			}
		}
		// --- '\' 모양 분석 to ---

		return false;
	}

	boolean isWin() { // 무조건 이기는 경우
		// --- 'ㄱ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//		                if (row == 7) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//		                    if(col == 0) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
						// 'ㄱ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 오른쪽위, 오른쪽아래)
//						if ((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7))
//							continue;
						if (isPossible(row, col)) {
							// 상대돌을 발견할 경우 'ㄱ'모양 분석
							// 'ㄱ' 모양이 중심점을 기준으로 함
							if (col > 0 && row < 7) {
								if (map[row][col - 1] == order && map[row + 1][col] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col < 7 && row < 7) {
								if (map[row][col + 1] == order && map[row + 1][col + 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col > 0 && row > 0) {
								if (map[row - 1][col] == order && map[row - 1][col - 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
						}
					}
				}

				// --- 'ㄱ' 모양 분석 to ---

				// --- 'ㄴ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//					if (row == 0)
//						continue; // 'ㄴ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//						if (col == 7)
//							continue; // 'ㄴ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

						// 'ㄴ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 왼쪽아래, 오른쪽아래)
//						if ((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6))
//							continue;

						if (isPossible(row, col)) {

							// 상대돌을 발견할 경우 'ㄴ'모양 분석
							// 'ㄴ' 모양이 중심점을 기준으로 함
							if (col < 7 && row < 7) {
								if (map[row + 1][col] == order && map[row + 1][col + 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col > 0 && row > 0) {
								if (map[row - 1][col - 1] == order && map[row][col - 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col < 7 && row > 0) {
								if (map[row - 1][col] == order && map[row][col + 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
						}
					}
				}
				// --- 'ㄴ' 모양 분석 to ---

				// --- '\' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//					if (row == 0 || row == 7)
//						continue; // '\' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//						if (col == 0 || col == 7)
//							continue; // '\' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
						
						// '\' 모양이 절대 만들어지지 않는 특정 좌표 건너뛰기
//						if ((row == 1 && col == 1) || (row == 6 && col == 6))
//							continue;

						if (isPossible(row, col)) {
							// 상대돌을 발견할 경우 '\'모양 분석
							// '\' 모양이 중심점을 기준으로 함
							if (col < 6 && row < 6) {
								if (map[row + 1][col + 1] == order && map[row + 2][col + 2] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col > 0 && row > 0 && row < 7 && col < 7) {
								if (map[row - 1][col - 1] == order && map[row + 1][col + 1] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
							if (col > 1 && row > 1) {
								if (map[row - 1][col - 1] == order && map[row - 2][col - 2] == order) {
									myRow = row;
									myCol = col;
									return true;
								}
							}
						}

					}
				}
				// --- '\' 모양 분석 to ---

				return false;
			}

	void OneMoreAnalysis(int inputRow, int inputCol, String opt) {

        // 무조건 이기는 경우의 돌도 없고, 안두면 지는 경우의 돌도 없는 경우 다음 수를 고려한 탐색
        // 열에 대한 각 열을 탐색(행은 첫번째 시도 때 이미 검사)
        // 행에 대한 각 행을 탐색(열은 첫번째 시도 때 이미 검사

        // 행에 두었을 경우에 대한 조건부 값 재탐색
        // 3번째 파라미터 : row로 넣을경우 이번 턴 내가 row에 두었을 경우 에 대한 탐색시작
        //this.OneMoreAnalysis(row_index, lastCol, new String("row")); << 요렇게 실행 시켜줘야 함

        // 내가 이번 턴 row 중 한 곳에 두었을 경우 열에 대한 탐색
        if(opt.equals("row")) {
            // 열에 두지않고 행 중 한 곳에 두었을 경우(그 둔 곳의 열을 탐색)
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

                // 내가 그 곳에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
                if (i == inputCol) continue;

                // 'ㄱ' 자 3 가지 모양 탐색
                // 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
                if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy
                        && inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 1 && i != 7) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
                    break; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
                }
                // 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy
                        && inputRow != 7 && i != 0 && (inputRow != 0 && i != 1) && (inputRow != 0 && i != 7) && (inputRow != 6 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 0 && i != 6) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }

                // 'ㄴ' 자 3 가지 모양 탐색
                // 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
                if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy
                        && inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 7 && i != 1) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy
                        && inputRow != 0 && i != 7 && (inputRow != 1 && i != 0) && (inputRow != 7 && i != 0) && (inputRow != 7 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 6 && i != 0) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }


                // '\' 자 3 가지 모양 탐색
                // '\' 자 중 오른쪽 아래 돌이 비어있을 경우
                if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy && map[inputRow - 2][i - 2] == enemy
                        && (inputRow != 0 && inputRow != 1) && (i != 0 && i != 1) && (inputRow != 2 && i != 2) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && (inputRow != 0 && inputRow != 7) && (i != 0 && i != 7) && (inputRow != 1 && i != 1) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i + 1] == enemy && map[inputRow + 2][i + 2] == enemy
                        && (inputRow != 6 && inputRow != 7) && (i != 6 && i != 7) && (inputRow != 5 && i != 5) && (inputRow != 0 && i != 0)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
            }
        }
        // 내가 이번 턴 col 중 한 곳에 두었을 경우 이에 대한 행을 탐색
        else {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

                // 내가 그 곳에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
                if (i == inputRow) continue;

                // 'ㄱ' 자 3 가지 모양 탐색
                // 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
                if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy
                        && i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 1 && inputCol != 7) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
                    break; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
                }
                // 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy
                        && i != 7 && inputCol != 0 && (i != 0 && inputCol != 1) && (i != 0 && inputCol != 7) && (i != 6 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 0 && inputCol != 6) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }

                // 'ㄴ' 자 3 가지 모양 탐색
                // 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
                if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy
                        && i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 7 && inputCol != 1) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy
                        && i != 0 && inputCol != 7 && (i != 1 && inputCol != 0) && (i != 7 && inputCol != 0) && (i != 7 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 6 && inputCol != 0) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }


                // '\' 자 3 가지 모양 탐색
                // '\' 자 중 오른쪽 아래 돌이 비어있을 경우
                if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy && map[i - 2][inputCol - 2] == enemy
                        && (i != 0 && i != 1) && (inputCol != 0 && inputCol != 1) && (i != 2 && inputCol != 2) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' 자 중 중앙 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && (i != 0 && i != 7) && (inputCol != 0 && inputCol != 7) && (i != 1 && inputCol != 1) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol + 1] == enemy && map[i + 2][inputCol + 2] == enemy
                        && (i != 6 && i != 7) && (inputCol != 6 && inputCol != 7) && (i != 5 && inputCol != 5) && (i != 0 && inputCol != 0)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
            }
        }

    }
}