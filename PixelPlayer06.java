import java.awt.*;

public class PixelPlayer06 extends Player {
	PixelPlayer06(int[][] map) {
		super(map);
	}

	int order;
	int enemy;
	int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // ����ġ�� ������ ���� ��
	int lastRow;
	int lastCol;
	int myRow = -1; // ���� ���� �̱�ų� ���� ��� �ٷ� ��ȯ�� ���� ����
	int myCol = -1;

	Point nextPosition(Point lastPosition) {

		// �Լ� ����
		// 1. ��ü ����ġ �ʱ�ȭ
		// 2. ������ �̱�� ��� ����ġ 1���� 1000
		// 3. ���� �̱�� ��� ����ġ 2����
		// 4. 2���� 3���� �ƴ� ��� �� 3���Լ� ȣ���Ͽ� 3����

		// ��� ���� ����
		// 1. ���� ������ �̱� �� �ִ� ���� ��ǥ ��� ��� (���� ���� �� ��)
		// 2. ���� ���� �� ���ԵǸ� ���ԵǴ� ���� ��ǥ ��� ��� (�ߴµ� ����ȭ �� ��)
		// 3. �̱�Ȯ���� ���� ���� ���� ��ǥ ��� ���(�밢������ 2 ���� ���� ��� �̱�Ȯ���� �ö�)
		// 4. �� ���� ���� ��� ���� �̱�� ��� ��� (���� ���� �� ��)
		// ����ġ ���� ���� �� ó���ϴ� ��
		// ���� Ž���� ���� ������ ���� �� ��� ����ġ�� ���� ��

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
		System.out.println(lastRow + " " + lastCol);

		for (int i = 0; i < 8; i++) // �ʱ� ����ġ ����
			for (int j = 0; j < 8; j++)
				myMap[i][j] = 0;

		if (this.isWin()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}

		// ������ �̱�� ���
		// �̱�� �޼ҵ�
		// ������ ���� ���
		// ��밡 ���� �Ѱ��� �� �θ� �̱�� ��츦 ���� �޼ҵ�(�� ���� ���ڸ��� ���� ����ġ ����)
		if (this.isLose()) {
			nextPosition = new Point(myRow, myCol);
			return nextPosition;
		}
		// --- ����ġ Ž�� �� ����ε��� ���� from ---
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1) { // ���Ͽ� ���� �޸������ ��
					// ����ġ �ο� (��� ���ǽ� ������� �ʿ� >> ���� �������� ��� � �����Դ� �밢���̰� � �����Դ� ������)
					if (i != 0)
						myMap[i - 1][j] += 2; // row �Ʒ��� ����ġ 2 �ο�
					if (i != 7)
						myMap[i + 1][j] += 2; // row ���� ����ġ 2 �ο�
					if (j != 0)
						myMap[i][j - 1] += 2; // col ���� ����ġ 2 �ο�
					if (j != 7)
						myMap[i][j + 1] += 2; // col ������ ����ġ 2 �ο�
					if (i != 7 && j != 7)
						myMap[i + 1][j + 1] += 1; // �������� ����ġ 1 �ο�
					if (i != 0 && j != 0)
						myMap[i - 1][j - 1] += 1; // ���ʾƷ� ����ġ 1 �ο�
					if (i != 7 && j != 0)
						myMap[i + 1][j - 1] += 3; // ������ ����ġ 3 �ο�
					if (i != 0 && j != 7)
						myMap[i - 1][j + 1] += 3; // �����ʾƷ� ����ġ 3 �ο�
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

		// row : ��(x�� ���� ����), col : ��(y�� ���� ����)
		int best_row = -1, best_col = -1; // ��Ŀ� ���� ����ġ�� �ִ��� ���� �����ϱ� ���� ����
		int row_index = -1, col_index = -1; // ��Ŀ� ���� ����ġ�� �ִ��� �ε����� �����ϱ� ���� ����

		// ��ġ�� ��� �����غ��� ��
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

		System.out.println("������ �ְ� �ε��� " + lastRow + " " + col_index + " " + myMap[lastRow][col_index]);
		System.out.println("����� �ְ� �ε��� " + row_index + " " + lastCol + " " + myMap[row_index][lastCol]);

		// row�Ǵ� col�� ���� ����ġ�� �� ��, �ε����� ���� ( �Ȱ��� ��쿡�� ��� �� ��������)
		if (best_row >= best_col) // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
			nextPosition = new Point(row_index, lastCol); // ��ǥ�� �ݴ�� ����
		else // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
			nextPosition = new Point(lastRow, col_index); // ��ǥ�� �ݴ�� ����
		// --- ����ġ Ž�� �� ����ε��� ���� to ---

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

	boolean isPossible(int row, int col) { // ���� ���Ƶ� �Ǵ��� �˻��ϴ� �޼ҵ�
		if (map[row][col] == 0 && (lastRow == row || lastCol == col)) {// �������� ��������鼭 ���γ� ���θ� ������ �� ����
			return true;
		}

		return false;
	}

	boolean isLose() { // ���� ������ ���� ���

		// --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//                if (row == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//                    if(col == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
				// '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ��������, �����ʾƷ�)
//				if ((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7))
//					continue;
				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '��'��� �м�
					// '��' ����� �߽����� �������� ��
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

		// --- '��' ��� �м� to ---

		// --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//			if (row == 0)
//				continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//				if (col == 7)
//					continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

				// '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ���ʾƷ�, �����ʾƷ�)
//				if ((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6))
//					continue;

				if (isPossible(row, col)) {

					// ��뵹�� �߰��� ��� '��'��� �м�
					// '��' ����� �߽����� �������� ��
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
		// --- '��' ��� �м� to ---

		// --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
		for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//			if (row == 0 || row == 7)
//				continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
			for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//				if (col == 0 || col == 7)
//					continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
				
				// '\' ����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�
//				if ((row == 1 && col == 1) || (row == 6 && col == 6))
//					continue;

				if (isPossible(row, col)) {
					// ��뵹�� �߰��� ��� '\'��� �м�
					// '\' ����� �߽����� �������� ��
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
		// --- '\' ��� �м� to ---

		return false;
	}

	boolean isWin() { // ������ �̱�� ���
		// --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//		                if (row == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//		                    if(col == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
						// '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ��������, �����ʾƷ�)
//						if ((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7))
//							continue;
						if (isPossible(row, col)) {
							// ��뵹�� �߰��� ��� '��'��� �м�
							// '��' ����� �߽����� �������� ��
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

				// --- '��' ��� �м� to ---

				// --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//					if (row == 0)
//						continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//						if (col == 7)
//							continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

						// '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ���ʾƷ�, �����ʾƷ�)
//						if ((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6))
//							continue;

						if (isPossible(row, col)) {

							// ��뵹�� �߰��� ��� '��'��� �м�
							// '��' ����� �߽����� �������� ��
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
				// --- '��' ��� �м� to ---

				// --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
				for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
//					if (row == 0 || row == 7)
//						continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
					for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
//						if (col == 0 || col == 7)
//							continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
						
						// '\' ����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�
//						if ((row == 1 && col == 1) || (row == 6 && col == 6))
//							continue;

						if (isPossible(row, col)) {
							// ��뵹�� �߰��� ��� '\'��� �м�
							// '\' ����� �߽����� �������� ��
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
				// --- '\' ��� �м� to ---

				return false;
			}

	void OneMoreAnalysis(int inputRow, int inputCol, String opt) {

        // ������ �̱�� ����� ���� ����, �ȵθ� ���� ����� ���� ���� ��� ���� ���� ����� Ž��
        // ���� ���� �� ���� Ž��(���� ù��° �õ� �� �̹� �˻�)
        // �࿡ ���� �� ���� Ž��(���� ù��° �õ� �� �̹� �˻�

        // �࿡ �ξ��� ��쿡 ���� ���Ǻ� �� ��Ž��
        // 3��° �Ķ���� : row�� ������� �̹� �� ���� row�� �ξ��� ��� �� ���� Ž������
        //this.OneMoreAnalysis(row_index, lastCol, new String("row")); << �䷸�� ���� ������� ��

        // ���� �̹� �� row �� �� ���� �ξ��� ��� ���� ���� Ž��
        if(opt.equals("row")) {
            // ���� �����ʰ� �� �� �� ���� �ξ��� ���(�� �� ���� ���� Ž��)
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

                // ���� �� ���� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
                if (i == inputCol) continue;

                // '��' �� 3 ���� ��� Ž��
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
                if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy
                        && inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 1 && i != 7) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
                    break; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy
                        && inputRow != 7 && i != 0 && (inputRow != 0 && i != 1) && (inputRow != 0 && i != 7) && (inputRow != 6 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 0 && i != 6) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }

                // '��' �� 3 ���� ��� Ž��
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
                if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy
                        && inputRow != 0 && i != 0 && (inputRow != 1 && i != 1) && (inputRow != 7 && i != 1) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy
                        && inputRow != 0 && i != 7 && (inputRow != 1 && i != 0) && (inputRow != 7 && i != 0) && (inputRow != 7 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && inputRow != 7 && i != 7 && (inputRow != 0 && i != 0) && (inputRow != 6 && i != 0) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }


                // '\' �� 3 ���� ��� Ž��
                // '\' �� �� ������ �Ʒ� ���� ������� ���
                if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy && map[inputRow - 2][i - 2] == enemy
                        && (inputRow != 0 && inputRow != 1) && (i != 0 && i != 1) && (inputRow != 2 && i != 2) && (inputRow != 7 && i != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' �� �� �߾� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy && map[inputRow + 1][i + 1] == enemy
                        && (inputRow != 0 && inputRow != 7) && (i != 0 && i != 7) && (inputRow != 1 && i != 1) && (inputRow != 6 && i != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' �� �� ���� �� ���� ������� ��� ����
                else if (map[inputRow][i] == 0 && map[inputRow + 1][i + 1] == enemy && map[inputRow + 2][i + 2] == enemy
                        && (inputRow != 6 && inputRow != 7) && (i != 6 && i != 7) && (inputRow != 5 && i != 5) && (inputRow != 0 && i != 0)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
            }
        }
        // ���� �̹� �� col �� �� ���� �ξ��� ��� �̿� ���� ���� Ž��
        else {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {

                // ���� �� ���� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
                if (i == inputRow) continue;

                // '��' �� 3 ���� ��� Ž��
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
                if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy
                        && i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 1 && inputCol != 7) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
                    break; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy
                        && i != 7 && inputCol != 0 && (i != 0 && inputCol != 1) && (i != 0 && inputCol != 7) && (i != 6 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 0 && inputCol != 6) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }

                // '��' �� 3 ���� ��� Ž��
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����(�⺻��� ���� + ����������ʴ� ��� ����)
                if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy
                        && i != 0 && inputCol != 0 && (i != 1 && inputCol != 1) && (i != 7 && inputCol != 1) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy
                        && i != 0 && inputCol != 7 && (i != 1 && inputCol != 0) && (i != 7 && inputCol != 0) && (i != 7 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && i != 7 && inputCol != 7 && (i != 0 && inputCol != 0) && (i != 6 && inputCol != 0) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }


                // '\' �� 3 ���� ��� Ž��
                // '\' �� �� ������ �Ʒ� ���� ������� ���
                if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy && map[i - 2][inputCol - 2] == enemy
                        && (i != 0 && i != 1) && (inputCol != 0 && inputCol != 1) && (i != 2 && inputCol != 2) && (i != 7 && inputCol != 7)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' �� �� �߾� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy && map[i + 1][inputCol + 1] == enemy
                        && (i != 0 && i != 7) && (inputCol != 0 && inputCol != 7) && (i != 1 && inputCol != 1) && (i != 6 && inputCol != 6)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
                // '\' �� �� ���� �� ���� ������� ��� ����
                else if (map[i][inputCol] == 0 && map[i + 1][inputCol + 1] == enemy && map[i + 2][inputCol + 2] == enemy
                        && (i != 6 && i != 7) && (inputCol != 6 && inputCol != 7) && (i != 5 && inputCol != 5) && (i != 0 && inputCol != 0)) {
                    myMap[inputRow][inputCol] = 0;
                    break;
                }
            }
        }

    }
}