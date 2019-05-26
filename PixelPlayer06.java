import java.awt.*;

public class PixelPlayer06 extends Player {
	PixelPlayer06(int[][] map) {
		super(map);
	}

	int order; // ���� �� ��ȣ
	int enemy; // ����� �� ��ȣ
	int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // ����ġ�� ������ ���� ��
	int lastRow;
	int lastCol;
	int myRow = -1;
	int myCol = -1;

	Point nextPosition(Point lastPosition) {

		// ��� ���� �ε����� 0������ ����, ��ü �ٵ��� ũ��� 8*8(0~7 * 0~7)
		// 1��° ���� �Ķ����̸�, �����̰� �ٵ��ǿ� 1�� ǥ���, 4��3�� ���� ����
		// 2��° ���� �������̸�, �İ��̰� �ٵ��ǿ� 2�� ǥ���, 3��4�� ���� ����

		lastRow = (int) lastPosition.getX();
		lastCol = (int) lastPosition.getY();

		order = PixelTester.turn;

		if (order == 1)
			enemy = 2;
		else
			enemy = 1;
		Point nextPosition = null;
		// System.out.println(lastRow + " " + lastCol);

		for (int i = 0; i < 8; i++) // �ʱ� ����ġ ����
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

		// ���� �����̸�
		// --- ����ġ Ž�� �� ����ε��� ���� from ---
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == order && order == 1) {
					// ����ġ �ο�
					if (i != 0)
						myMap[i - 1][j] += 2; // ���� ����ġ 2 �ο�
					if (i != 7)
						myMap[i + 1][j] += 2; // �Ʒ��� ����ġ 2 �ο�
					if (j != 0)
						myMap[i][j - 1] += 2; // ���� ����ġ 2 �ο�
					if (j != 7)
						myMap[i][j + 1] += 2; // ������ ����ġ 2 �ο�
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // �����ʾƷ� ����ġ 3 �ο�
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // ������ ����ġ 3 �ο�
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // ���ʾƷ� ����ġ 1 �ο�
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // �������� ����ġ 1 �ο�
				} else if (map[i][j] == enemy && order == 2 && PixelTester.dolCount <= 15) {
					// ����ġ �ο�
					if (i != 0)
						myMap[i - 1][j] += 2; // ���� ����ġ 2 �ο�
					if (i != 7)
						myMap[i + 1][j] += 2; // �Ʒ��� ����ġ 2 �ο�
					if (j != 0)
						myMap[i][j - 1] += 2; // ���� ����ġ 2 �ο�
					if (j != 7)
						myMap[i][j + 1] += 2; // ������ ����ġ 2 �ο�
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // �����ʾƷ� ����ġ 3 �ο�
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // ������ ����ġ 3 �ο�
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // ���ʾƷ� ����ġ 1 �ο�
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // �������� ����ġ 1 �ο�
				} else if (map[i][j] == order && order == 2) {
					// ����ġ �ο�
					if (i != 0)
						myMap[i - 1][j] += 2; // ���� ����ġ 2 �ο�
					if (i != 7)
						myMap[i + 1][j] += 2; // �Ʒ��� ����ġ 2 �ο�
					if (j != 0)
						myMap[i][j - 1] += 2; // ���� ����ġ 2 �ο�
					if (j != 7)
						myMap[i][j + 1] += 2; // ������ ����ġ 2 �ο�
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 3; // �����ʾƷ� ����ġ 3 �ο�
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 3; // ������ ����ġ 3 �ο�
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 1; // ���ʾƷ� ����ġ 1 �ο�
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 1; // �������� ����ġ 1 �ο�
				}
			}
		}

		// ����ġ �缳�� �κ�
		// 1. ���� ��ġ ����ġ ����
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1 || map[i][j] == 2)
					myMap[i][j] = -1;
			}
		}

		// 2. �� �̿� �κ��� ����ġ ����
		myMap[0][0] = -1;
		myMap[0][7] = -1;
		myMap[7][0] = -1;
		myMap[7][7] = -1;

		// ������ �̱�ų� ���� ��찡 ���� ���
		// row : ��(x�� ���� ����), col : ��(y�� ���� ����)
		int best_row = -1, best_col = -1; // ��Ŀ� ���� ����ġ�� �ִ��� ���� �����ϱ� ���� ����
		int row_index = -1, col_index = -1; // ��Ŀ� ���� ����ġ�� �ִ��� �ε����� �����ϱ� ���� ����
		boolean check = true;

		while (check) { // ���� ���� ���� �������� Ȯ���ϱ� ���� �ݺ���

			// if(lastRow == 5 && lastCol == 5) System.out.println("%");
			// row�� ���� ����ġ Ž��
			for (int i = 0; i < 8; i++) {
				if (best_row <= myMap[i][lastCol]) {
					best_row = myMap[i][lastCol];
					row_index = i;
				}
			}

			// col�� ���� ����ġ Ž��
			for (int i = 0; i < 8; i++) {
				if (best_col <= myMap[lastRow][i]) {
					best_col = myMap[lastRow][i];
					col_index = i;
				}
			}

			if (best_col == 0 || best_row == 0) { // ���� ���� ���� ������ ��� = �ּ��� ���� ����ġ�� 0�� ���
				nextPosition = new Point(row_index, lastCol);
				check = false;

			}

			// if(Math.abs(best_row - best_col) <= 3) {
			// best_row = ValueSplit(row_index, lastCol);
			// best_col = ValueSplit(lastRow, col_index);
			// }

			if (best_row >= best_col) { // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
				if (OneMoreAnalysis(row_index, lastCol, "row")) {
					best_row = -1;
					best_col = -1;
					check = true;
				} else {
					check = false;
					nextPosition = new Point(row_index, lastCol);
				}
			} else { // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
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

	boolean isPossible(int row, int col) { // ���� ���Ƶ� �Ǵ��� �˻��ϴ� �޼ҵ�
		if (map[row][col] == 0 && (lastRow == row || lastCol == col)) {// �������� ��������鼭 ���γ� ���θ� ������ �� ����
			return true;
		}

		return false;
	}

	boolean isLose() { // ���� ������ ���� ���
		// ��� ��쿡 ���ؼ� ���� �� ���� row�� col�̶�� ����
		// --- '��' ��� �м� from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '��'��� �м�
					// ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
					// �� �� == 1 2
					//   �� == 3
					if (col > 0 && row < 7) {
						if (map[row][col - 1] == enemy && map[row + 1][col] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row < 7) {
						if (map[row][col + 1] == enemy && map[row + 1][col + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col] == enemy && map[row - 1][col - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}

		// --- '��' ��� �м� to ---

		// --- '��' ��� �м� from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '��'��� �м�
					// ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
					// �� == 1
					// �� �� == 2 3
					if (col < 7 && row < 7) {
						if (map[row + 1][col] == enemy && map[row + 1][col + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col - 1] == enemy && map[row][col - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row > 0) {
						if (map[row - 1][col] == enemy && map[row][col + 1] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}
		// --- '��' ��� �м� to ---

		// --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '\'��� �м�
					// ������ ���� '\' ����翡 ��ȣ�� �ű�ٸ�
					// ��              == 1
					//   ��          == 2 
					//     ��      == 3
					if (col < 6 && row < 6) {
						if (map[row + 1][col + 1] == enemy && map[row + 2][col + 2] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0 && row < 7 && col < 7) {
						if (map[row - 1][col - 1] == enemy && map[row + 1][col + 1] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 1 && row > 1) {
						if (map[row - 1][col - 1] == enemy && map[row - 2][col - 2] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}

			}
		}
		// --- '\' ��� �м� to ---

		return false;
	}

	boolean isWin() { // ������ �̱�� ���
		// ��� ��쿡 ���ؼ� ���� �� ���� row�� col�̶�� ����
		// --- '��' ��� �м� from ---
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
				if (isPossible(row, col)) {
					// ���� ���� �߰��� ��� '��'��� �м�
					// ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
					// �� �� == 1 2
					//   �� == 3
					if (col > 0 && row < 7) {
						if (map[row][col - 1] == order && map[row + 1][col] == order) { // ����� ���� 1���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row < 7) {
						if (map[row][col + 1] == order && map[row + 1][col + 1] == order) { // ����� ���� 2���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) {
						if (map[row - 1][col] == order && map[row - 1][col - 1] == order) { // ����� ���� 1���� 2���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}

		// --- '��' ��� �м� to ---

		// --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {

				if (isPossible(row, col)) {
					// ���� ���� �߰��� ��� '��'��� �м�
					// ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
					// ��     == 1
					// �� �� == 2 3
					if (col < 7 && row < 7) { // ����� ���� 2���� 3���� �ִ� ���
						if (map[row + 1][col] == order && map[row + 1][col + 1] == order) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0) { // ����� ���� 1���� 2���� �ִ� ���	
						if (map[row - 1][col - 1] == order && map[row][col - 1] == order) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col < 7 && row > 0) { // ����� ���� 1���� 3���� �ִ� ���
						if (map[row - 1][col] == order && map[row][col + 1] == order) {
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}
			}
		}
		// --- '��' ��� �м� to ---

		// --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {

				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '\'��� �м�
					// ������ ���� '\' ����翡 ��ȣ�� �ű�ٸ�
					// ��              == 1
					//   ��          == 2 
					//     ��      == 3
					if (col < 6 && row < 6) {
						if (map[row + 1][col + 1] == order && map[row + 2][col + 2] == order) {  // ����� ���� 1���� 2���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 0 && row > 0 && row < 7 && col < 7) {
						if (map[row - 1][col - 1] == order && map[row + 1][col + 1] == order) {  // ����� ���� 1���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
					if (col > 1 && row > 1) {
						if (map[row - 1][col - 1] == order && map[row - 2][col - 2] == order) {  // ����� ���� 2���� 3���� �ִ� ���
							myRow = row;
							myCol = col;
							return true;
						}
					}
				}

			}
		}
		// --- '\' ��� �м� to ---

		return false;
	}

	boolean OneMoreAnalysis(int inputRow, int inputCol, String opt) {

		// ������ �̱�� ����� ���� ����, �ȵθ� ���� ����� ���� ���� ��� ���� ���� ����� Ž��
		// ���� ���� �� ���� Ž��(���� ù��° �õ� �� �̹� �˻�)
		// �࿡ ���� �� ���� Ž��(���� ù��° �õ� �� �̹� �˻�

		// ���� �̹� �� row �� �� ���� �ξ��� ��� ���� ���� Ž��
		if (opt.equals("row")) {
			// ���� �����ʰ� �� �� �� ���� �ξ��� ���(�� �� ���� ���� Ž��)
			for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

				// ���� �� ���� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
				if (i == inputCol)
					continue;

				// '��' �� 3 ���� ��� Ž��
				// '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
				if (inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 1 && i != 7)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
						return true; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
					}
				}

				// '��' �� �� �߾� ���� ������� ��� ����
				if (inputRow != 7 && i != 0 && (inputRow != 0 && i != 1) && (inputRow != 0 && i != 7)
						&& (inputRow != 6 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� ���� �� ���� ������� ��� ����
				if (inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 0 && i != 6)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '��' �� 3 ���� ��� Ž��
				// '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
				if (inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 7 && i != 1)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� �߾� ���� ������� ��� ����
				if (inputRow != 0 && i != 7 && (inputRow != 1 && i != 0) && (inputRow != 7 && i != 0)
						&& (inputRow != 7 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� ���� �� ���� ������� ��� ����
				if (inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 6 && i != 0)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '\' �� 3 ���� ��� Ž��
				// '\' �� �� ������ �Ʒ� ���� ������� ���
				if ((inputRow != 0 && inputRow != 1) && (i != 0 && i != 1) && (inputRow != 2 && i != 2)
						&& (inputRow != 7 && i != 7)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
							&& map[inputRow - 2][i - 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' �� �� �߾� ���� ������� ��� ����
				if ((inputRow != 0 && inputRow != 7) && (i != 0 && i != 7) && (inputRow != 1 && i != 1)
						&& (inputRow != 6 && i != 6)) {
					if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
							&& map[inputRow + 1][i + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' �� �� ���� �� ���� ������� ��� ����
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
		// ���� �̹� �� col �� �� ���� �ξ��� ��� �̿� ���� ���� Ž��
		else {
			for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

				// ���� �� ���� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
				if (i == inputRow)
					continue;
				if (lastRow == 5 && lastCol == 5)
					System.out.println("%");
				// '��' �� 3 ���� ��� Ž��
				// '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
				if (i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 1 && inputCol != 7)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
						return true; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
					}
				}

				// '��' �� �� �߾� ���� ������� ��� ����
				if (i != 7 && inputCol != 0 && (i != 0 && inputCol != 1) && (i != 0 && inputCol != 7)
						&& (i != 6 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� ���� �� ���� ������� ��� ����
				if (i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 0 && inputCol != 6)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '��' �� 3 ���� ��� Ž��
				// '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
				if (i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 7 && inputCol != 1)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� �߾� ���� ������� ��� ����
				if (i != 0 && inputCol != 7 && (i != 1 && inputCol != 0) && (i != 7 && inputCol != 0)
						&& (i != 7 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '��' �� �� ���� �� ���� ������� ��� ����
				if (i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 6 && inputCol != 0)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}

				// '\' �� 3 ���� ��� Ž��
				// '\' �� �� ������ �Ʒ� ���� ������� ���
				if ((i != 0 && i != 1) && (inputCol != 0 && inputCol != 1) && (i != 2 && inputCol != 2)
						&& (i != 7 && inputCol != 7)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
							&& map[i - 2][inputCol - 2] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' �� �� �߾� ���� ������� ��� ����
				if ((i != 0 && i != 7) && (inputCol != 0 && inputCol != 7) && (i != 1 && inputCol != 1)
						&& (i != 6 && inputCol != 6)) {
					if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
							&& map[i + 1][inputCol + 1] == enemy) {
						myMap[inputRow][inputCol] = 0;
						return true;
					}
				}
				// '\' �� �� ���� �� ���� ������� ��� ����
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