import java.awt.*;

public class PixelPlayer06 extends Player {
	PixelPlayer06(int[][] map) {
		super(map);
	}

	int order; // 나의 돌 번호
	int enemy; // 상대의 돌 번호
	int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // 가중치를 저장할 나의 맵
	int lastRow;
	int lastCol;
	int myRow = -1;
	int myCol = -1;

	Point nextPosition(Point lastPosition) {

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
		// System.out.println(lastRow + " " + lastCol);

		for (int i = 0; i < 8; i++) // 초기 가중치 설정
			for (int j = 0; j < 8; j++)
				myMap[i][j] = 0;

		if (this.isWin()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}

		if (this.isLose()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}

		// 내가 선공이면
		// --- 가중치 탐색 후 행렬인덱스 리턴 from ---
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == order && order == 1) {
					// 가중치 부여
					if (i != 0)
						myMap[i - 1][j] += 2; // 위쪽 가중치 2 부여
					if (i != 7)
						myMap[i + 1][j] += 2; // 아래쪽 가중치 2 부여
					if (j != 0)
						myMap[i][j - 1] += 2; // 왼쪽 가중치 2 부여
					if (j != 7)
						myMap[i][j + 1] += 2; // 오른쪽 가중치 2 부여
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // 오른쪽아래 가중치 3 부여
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // 왼쪽위 가중치 3 부여
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // 왼쪽아래 가중치 1 부여
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // 오른쪽위 가중치 1 부여
				} else if (map[i][j] == enemy && order == 2 && PixelTester.dolCount <= 15) {
					// 가중치 부여
					if (i != 0)
						myMap[i - 1][j] += 2; // 위쪽 가중치 2 부여
					if (i != 7)
						myMap[i + 1][j] += 2; // 아래쪽 가중치 2 부여
					if (j != 0)
						myMap[i][j - 1] += 2; // 왼쪽 가중치 2 부여
					if (j != 7)
						myMap[i][j + 1] += 2; // 오른쪽 가중치 2 부여
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // 오른쪽아래 가중치 3 부여
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // 왼쪽위 가중치 3 부여
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // 왼쪽아래 가중치 1 부여
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // 오른쪽위 가중치 1 부여
				} else if (map[i][j] == order && order == 2) {
					// 가중치 부여
					if (i != 0)
						myMap[i - 1][j] += 2; // 위쪽 가중치 2 부여
					if (i != 7)
						myMap[i + 1][j] += 2; // 아래쪽 가중치 2 부여
					if (j != 0)
						myMap[i][j - 1] += 2; // 왼쪽 가중치 2 부여
					if (j != 7)
						myMap[i][j + 1] += 2; // 오른쪽 가중치 2 부여
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // 오른쪽아래 가중치 3 부여
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // 왼쪽위 가중치 3 부여
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // 왼쪽아래 가중치 1 부여
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // 오른쪽위 가중치 1 부여
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

		// 무조건 이기거나 지는 경우가 없는 경우
		// row : 행(x로 변수 지정), col : 열(y로 변수 지정)
		int best_row = -1, best_col = -1; // 행렬에 대한 가중치가 최대인 값을 저장하기 위한 변수
		int row_index = -1, col_index = -1; // 행렬에 대한 가중치가 최대인 인덱스를 저장하기 위한 변수
		boolean check = true;

		while (check) { // 나의 다음 수가 위험한지 확인하기 위한 반복문

			// if(lastRow == 5 && lastCol == 5) System.out.println("%");
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

			if (best_col == 0 || best_row == 0) { // 나의 다음 수가 위험한 경우 = 최선의 수가 가중치가 0인 경우
				nextPosition = new Point(row_index, lastCol);
				check = false;

			}

			// if(Math.abs(best_row - best_col) <= 3) {
			// best_row = ValueSplit(row_index, lastCol);
			// best_col = ValueSplit(lastRow, col_index);
			// }

			if (best_row >= best_col) { // 행이동에 대한 가중치값이 열이동에 대한 가중치 값보다 높을 경우
				if (OneMoreAnalysis(row_index, lastCol, "row")) {
					best_row = -1;
					best_col = -1;
					check = true;
				} else {
					check = false;
					nextPosition = new Point(row_index, lastCol);
				}
			} else { // 열이동에 대한 가중치값이 행이동에 대한 가중치 값보다 높을 경우
				if (OneMoreAnalysis(lastRow, col_index, "col")) {
					best_row = -1;
					best_col = -1;
					check = true;
				} else {
					check = false;
					nextPosition = new Point(lastRow, col_index);
				}
			}
		}

		return nextPosition;
	}

	// void ValueSplit(int row, int col) {
	// int count = 0;

	// if(order == 1) {
	// if(map[row-1][col-1] == enemy) { count ++; }
	// else if (map[row+1][col-1] == enemy) { count ++; }
	// }

	// else {}
	// }

	boolean isPossible(int row, int col) { // 돌을 놓아도 되는지 검사하는 메소드
		if (map[row][col] == 0 && (lastRow == row || lastCol == col)) {// 게임판이 비어있으면서 가로나 세로만 움직일 수 있음
			return true;
		}

		return false;
	}

	boolean isLose() { // 막지 않으면 지는 경우
		// 모든 경우에 대해서 내가 둘 곳이 row와 col이라고 생각
		// --- 'ㄱ' 모양 분석 from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 'ㄱ'모양 분석
					// 다음과 같은 'ㄱ' 돌모양에 번호를 매긴다면
					// ㅇ ㅇ == 1 2
					//   ㅇ == 3
					if (col > 0 && row < 7) {
						if (map[row][col - 1] == enemy && map[row + 1][col] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row < 7) {
						if (map[row][col + 1] == enemy && map[row + 1][col + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col] == enemy && map[row - 1][col - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}

		// --- 'ㄱ' 모양 분석 to ---

		// --- 'ㄴ' 모양 분석 from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 'ㄴ'모양 분석
					// 다음과 같은 'ㄴ' 돌모양에 번호를 매긴다면
					// ㅇ == 1
					// ㅇ ㅇ == 2 3
					if (col < 7 && row < 7) {
						if (map[row + 1][col] == enemy && map[row + 1][col + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col - 1] == enemy && map[row][col - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row > 0) {
						if (map[row - 1][col] == enemy && map[row][col + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
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
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 '\'모양 분석
					// 다음과 같은 '\' 돌모양에 번호를 매긴다면
					// ㅇ              == 1
					//   ㅇ          == 2 
					//     ㅇ      == 3
					if (col < 6 && row < 6) {
						if (map[row + 1][col + 1] == enemy && map[row + 2][col + 2] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0 && row < 7 && col < 7) {
						if (map[row - 1][col - 1] == enemy && map[row + 1][col + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 1 && row > 1) {
						if (map[row - 1][col - 1] == enemy && map[row - 2][col - 2] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
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
		// 모든 경우에 대해서 내가 둘 곳이 row와 col이라고 생각
		// --- 'ㄱ' 모양 분석 from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// 나의 돌을 발견할 경우 'ㄱ'모양 분석
					// 다음과 같은 'ㄱ' 돌모양에 번호를 매긴다면
					// ㅇ ㅇ == 1 2
					//   ㅇ == 3
					if (col > 0 && row < 7) {
						if (map[row][col - 1] == order && map[row + 1][col] == order) { // 상대의 돌이 1번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row < 7) {
						if (map[row][col + 1] == order && map[row + 1][col + 1] == order) { // 상대의 돌이 2번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col] == order && map[row - 1][col - 1] == order) { // 상대의 돌이 1번과 2번에 있는 경우
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
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {

				if (isPossible(row, col)) {
					// 나의 돌을 발견할 경우 'ㄴ'모양 분석
					// 다음과 같은 'ㄴ' 돌모양에 번호를 매긴다면
					// ㅇ     == 1
					// ㅇ ㅇ == 2 3
					if (col < 7 && row < 7) { // 상대의 돌이 2번과 3번에 있는 경우
						if (map[row + 1][col] == order && map[row + 1][col + 1] == order) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) { // 상대의 돌이 1번과 2번에 있는 경우	
						if (map[row - 1][col - 1] == order && map[row][col - 1] == order) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row > 0) { // 상대의 돌이 1번과 3번에 있는 경우
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
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {

				if (isPossible(row, col)) {
					// 상대돌을 발견할 경우 '\'모양 분석
					// 다음과 같은 '\' 돌모양에 번호를 매긴다면
					// ㅇ              == 1
					//   ㅇ          == 2 
					//     ㅇ      == 3
					if (col < 6 && row < 6) {
						if (map[row + 1][col + 1] == order && map[row + 2][col + 2] == order) {  // 상대의 돌이 1번과 2번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0 && row < 7 && col < 7) {
						if (map[row - 1][col - 1] == order && map[row + 1][col + 1] == order) {  // 상대의 돌이 1번과 3번에 있는 경우
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 1 && row > 1) {
						if (map[row - 1][col - 1] == order && map[row - 2][col - 2] == order) {  // 상대의 돌이 2번과 3번에 있는 경우
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

	boolean OneMoreAnalysis(int inputRow, int inputCol, String opt) {

		// 무조건 이기는 경우의 돌도 없고, 안두면 지는 경우의 돌도 없는 경우 다음 수를 고려한 탐색
		// 열에 대한 각 열을 탐색(행은 첫번째 시도 때 이미 검사)
		// 행에 대한 각 행을 탐색(열은 첫번째 시도 때 이미 검사

		// 내가 이번 턴 row 중 한 곳에 두었을 경우 열에 대한 탐색
		if (opt.equals("row")) {
			// 열에 두지않고 행 중 한 곳에 두었을 경우(그 둔 곳의 열을 탐색)
			for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

				// 내가 그 곳에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
				if (i == inputCol)
					continue;

				// 'ㄱ' 자 3 가지 모양 탐색
				// 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
				if (inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 1 && i != 7)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
						return true; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
					}
				}

				// 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
				if (inputRow != 7 && i != 0 && (inputRow != 0 && i != 1) && (inputRow != 0 && i != 7)
						&& (inputRow != 6 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if (inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 0 && i != 6)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// 'ㄴ' 자 3 가지 모양 탐색
				// 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
				if (inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 7 && i != 1)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
				if (inputRow != 0 && i != 7 && (inputRow != 1 && i != 0) && (inputRow != 7 && i != 0)
						&& (inputRow != 7 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if (inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 6 && i != 0)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '\' 자 3 가지 모양 탐색
				// '\' 자 중 오른쪽 아래 돌이 비어있을 경우
				if ((inputRow != 0 && inputRow != 1) && (i != 0 && i != 1) && (inputRow != 2 && i != 2)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
							&& map[inputRow - 2][i - 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' 자 중 중앙 돌이 비어있을 경우 기준
				if ((inputRow != 0 && inputRow != 7) && (i != 0 && i != 7) && (inputRow != 1 && i != 1)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
							&& map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if ((inputRow != 6 && inputRow != 7) && (i != 6 && i != 7) && (inputRow != 5 && i != 5)
						&& (inputRow != 0 && i != 0)) {
					if (map[inputRow][i] == 0 && map[inputRow + 1][i + 1] == enemy
							&& map[inputRow + 2][i + 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
			}
		}
		// 내가 이번 턴 col 중 한 곳에 두었을 경우 이에 대한 행을 탐색
		else {
			for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

				// 내가 그 곳에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
				if (i == inputRow)
					continue;
				if (lastRow == 5 && lastCol == 5)
					System.out.println("%");
				// 'ㄱ' 자 3 가지 모양 탐색
				// 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
				if (i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 1 && inputCol != 7)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
						return true; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
					}
				}

				// 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
				if (i != 7 && inputCol != 0 && (i != 0 && inputCol != 1) && (i != 0 && inputCol != 7)
						&& (i != 6 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if (i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 0 && inputCol != 6)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// 'ㄴ' 자 3 가지 모양 탐색
				// 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준(기본모양 조건 + 만들어지지않는 모양 조건)
				if (i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 7 && inputCol != 1)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
				if (i != 0 && inputCol != 7 && (i != 1 && inputCol != 0) && (i != 7 && inputCol != 0)
						&& (i != 7 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if (i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 6 && inputCol != 0)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '\' 자 3 가지 모양 탐색
				// '\' 자 중 오른쪽 아래 돌이 비어있을 경우
				if ((i != 0 && i != 1) && (inputCol != 0 && inputCol != 1) && (i != 2 && inputCol != 2)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
							&& map[i - 2][inputCol - 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' 자 중 중앙 돌이 비어있을 경우 기준
				if ((i != 0 && i != 7) && (inputCol != 0 && inputCol != 7) && (i != 1 && inputCol != 1)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
							&& map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
				if ((i != 6 && i != 7) && (inputCol != 6 && inputCol != 7) && (i != 5 && inputCol != 5)
						&& (i != 0 && inputCol != 0)) {
					if (map[i][inputCol] == 0 && map[i + 1][inputCol + 1] == enemy
							&& map[i + 2][inputCol + 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
			}

		}
		return false;
	}
}