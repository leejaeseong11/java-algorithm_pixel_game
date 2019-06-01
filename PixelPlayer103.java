import java.awt.*;

public class PixelPlayer103 extends Player {
    PixelPlayer103(int[][] map) {
        super(map);
    }

    // ���� �ʵ�
    int order; // ���� �� �� ���� ������ ����
    int enemy; // ����� �� �� ���� ������ ����
    int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // ����ġ�� �����ϱ� ���� ��
    int lastRow; // ������ �������� ���� �� �ε����� ����
    int lastCol; // ������ �������� ���� �� �ε����� ����
    int myRow = -1; // �̹� �� ���ų� �̱�� ��츦 ���� ���� �Ǵ� ���� �� �ε���
    int myCol = -1; // �̹� �� ���ų� �̱�� ��츦 ���� ���� �Ǵ� ���� �� �ε���
    int lose_cnt_row = 0;
    int lose_cnt_col = 0;

    Point nextPosition(Point lastPosition) {

        // ��� ���� �ε����� 0 ���� ����, ��ü �ٵ��� ũ��� 8*8(0~7 * 0~7)
        // 1��° ���� �Ķ����̸�, �����̰� �ٵ��ǿ� 1�� ǥ���, 4��3�� ���� ����
        // 2��° ���� �������̸�, �İ��̰� �ٵ��ǿ� 2�� ǥ���, 3��4�� ���� ����
        // ȭ��ǥ ��ġ�� �Ķ�����, 4��3������ ����

        lastRow = (int) lastPosition.getX(); // ���� �Ͽ� ���� ������� �� �ε��� �ʱ�ȭ
        lastCol = (int) lastPosition.getY(); // ���� �Ͽ� ���� ������� �� �ε��� �ʱ�ȭ

        // ���� �����÷��̾� ����, �İ� �÷��̾� ���� �����ϴ� ������ �ʱ�ȭ
        order = PixelTester.turn;

        // ���� �����̶�� order�� 1�� �����ϰ� �İ��̶�� 2�� ����
        // enemy �������� �� �ݴ�� 2�� 1�� ����
        if (order == 1)
            enemy = 2;
        else
            enemy = 1;

        // ������ ����Ʈ ��ü�� null �� �ʱ�ȭ
        Point nextPosition = null;

        // ����ġ���� ����ġ�� 0���� �ʱ�ȭ
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                myMap[i][j] = 1;

        // isWin() �Լ��� ȣ���Ͽ� ������ �̱�� ���� �ִ��� �Ǵ�
        if (this.isWin()) {
            // ���� ������ �̱�� ���� ������ �� �ε����� ����
            nextPosition = new Point(myRow, myCol);
            System.out.print("win");
            return nextPosition;
        }

        // islose() �Լ��� ȣ���Ͽ� ������ ���� ���� �ִ��� �Ǵ�
        if (isLose() == true) {
            // ���� ������ ���� ���� �ϳ� ������ ����
            nextPosition = new Point(myRow, myCol);
            myMap[myRow][myCol] = 100;
            System.out.print("lose");
            //return nextPosition;
        }

        // --- ���� 0 ���� �ʱ�ȭ�� ����ġ�ʿ� ����ġ �Ҵ� from ---
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // ���� �����̸�, �� ���� ��ġ�� �������� ���� ����ġ�� �Ҵ�
                if (map[i][j] == order) {
                    if (i != 0)
                        myMap[i - 1][j] += 4; // ���� ����ġ 4 �ο�
                    if (i != 7)
                        myMap[i + 1][j] += 4; // �Ʒ��� ����ġ 4 �ο�
                    if (j != 0)
                        myMap[i][j - 1] += 4; // ���� ����ġ 4 �ο�
                    if (j != 7)
                        myMap[i][j + 1] += 4; // ������ ����ġ 4 �ο�
                    if (i != 7 && j != 7)
                        myMap[i + 1][j + 1] += 5; // �����ʾƷ� ����ġ 5 �ο�
                    if (i != 0 && j != 0)
                        myMap[i - 1][j - 1] += 5; // ������ ����ġ 5 �ο�
                    if (i != 7 && j != 0)
                        myMap[i + 1][j - 1] += 1; // ���ʾƷ� ����ġ 1 �ο�
                    if (i != 0 && j != 7)
                        myMap[i - 1][j + 1] += 1; // �������� ����ġ 1 �ο�
                    if (i < 6 && j < 6)
                        myMap[i + 2][j + 2] += 1; // �밢��*2 ����ġ �ο�
                    if (i > 1 && j > 1)
                        myMap[i - 2][j - 2] += 1;
                    if (i < 6 && j < 7)
                        myMap[i + 2][j + 1] += 3; // �밢��*2 �� ����ġ �ο�
                    if (i > 1 && j > 0)
                        myMap[i - 2][j - 1] += 3;
                    if (i < 7 && j < 6)
                        myMap[i + 1][j + 2] += 3; // �밢��*2 �� ����ġ �ο�
                    if (i > 0 && j > 1)
                        myMap[i - 1][j - 2] += 3;
                    if (j < 6)
                        myMap[i][j + 2] += 2; // ���� ������*2 ����ġ �ο�
                    if (j > 1)
                        myMap[i][j - 2] += 2;
                    if (i < 6)
                        myMap[i + 2][j] += 2; // �� �Ʒ�*2 ����ġ �ο�
                    if (i > 1)
                        myMap[i - 2][j] += 2;
                }
            }
        }

        // ����ġ �缳��
        // 1. ������ ���̳� ���� ���� ���� ��ġ�� ����ġ�� -1�� ����
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map[i][j] == 1 || map[i][j] == 2)
                    myMap[i][j] = -1;
            }
        }

        // 2. �� �̿��� �κ��� ����ġ�� -1�� ����
        myMap[0][0] = -1;
        myMap[0][7] = -1;
        myMap[7][0] = -1;
        myMap[7][7] = -1;

        int best_row = -1, best_col = -1; // ��Ŀ� ���� ����ġ�� �ִ��� ���� �����ϱ� ���� �ʵ�
        int row_index = -1, col_index = -1; // ��Ŀ� ���� ����ġ�� �ִ��� �ε����� �����ϱ� ���� �ʵ�
        boolean check = true; // ���� ���� ���� �����ϱ� ���� while�� ���� �Ҹ� �ʵ�

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

            if (best_col == 0 && best_row == 0) { // ���� ���� ���� ������ ��� = �ּ��� ���� ����ġ�� 0�� ���
                nextPosition = new Point(row_index, lastCol);
                check = false;

            }

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

    boolean isPossible(int row, int col) { // ���� ���Ƶ� �Ǵ��� �˻��ϴ� �޼ҵ�
        if (map[row][col] == 0 && (lastRow == row || lastCol == col)) { // �������� ��������鼭 ���γ� ���θ� ������ �� ����
            return true;
        }

        return false;
    }

    boolean isLose() { // ���� ������ ���� ���
        // ��� ��쿡 ���ؼ� ���� �� ���� row�� col�̶�� ����
        int cnt = 0; // ������ ���� �� �������� Ȯ��
        // �� ���� �м�
        // --- '��' ��� �м� from ---
        for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
            if (isPossible(lastRow, col)) {
                // ��뵹�� �߰��� ��� '��'��� �м�
                // ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
                // �� �� == 1 2
                // �� == 3
                if (col > 0 && lastRow < 7) {
                    if (map[lastRow][col - 1] == enemy && map[lastRow + 1][col] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col < 7 && lastRow < 7) {
                    if (map[lastRow][col + 1] == enemy && map[lastRow + 1][col + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0) {
                    if (map[lastRow - 1][col] == enemy && map[lastRow - 1][col - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }

                // --- '��' ��� �м� to ---

                // --- '��' ��� �м� from ---
                // ��뵹�� �߰��� ��� '��'��� �м�
                // ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
                // �� == 1
                // �� �� == 2 3
                if (col < 7 && lastRow < 7) {
                    if (map[lastRow + 1][col] == enemy && map[lastRow + 1][col + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow][col - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col < 7 && lastRow > 0) {
                    if (map[lastRow - 1][col] == enemy && map[lastRow][col + 1] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }

                // --- '��' ��� �м� to ---

                // --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
                // ��뵹�� �߰��� ��� '\'��� �м�
                // ������ ���� '\' ����翡 ��ȣ�� �ű�ٸ�
                // �� == 1
                // �� == 2
                // �� == 3
                if (col < 6 && lastRow < 6) {
                    if (map[lastRow + 1][col + 1] == enemy && map[lastRow + 2][col + 2] == enemy) { // ����� ���� 1���� 2���� �ִ�
                        // ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0 && lastRow < 7 && col < 7) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow + 1][col + 1] == enemy) { // ����� ���� 1���� 3���� �ִ�
                        // ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 1 && lastRow > 1) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow - 2][col - 2] == enemy) { // ����� ���� 2���� 3���� �ִ�
                        // ���
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                // --- '\' ��� �м� to ---

            }
        }
        if (cnt == 1) // �� ������ ������ ���� �� �����̸� true
            return true;

        lose_cnt_col = cnt;

        cnt = 0; // ������ �� �� �ʱ�ȭ

        // �� ���� �м�
        // --- '��' ��� �м� from ---
        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
            if (isPossible(row, lastCol)) {
                // ��뵹�� �߰��� ��� '��'��� �м�
                // ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
                // �� �� == 1 2
                // �� == 3
                if (lastCol > 0 && row < 7) {
                    if (map[row][lastCol - 1] == enemy && map[row + 1][lastCol] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol < 7 && row < 7) {
                    if (map[row][lastCol + 1] == enemy && map[row + 1][lastCol + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0) {
                    if (map[row - 1][lastCol] == enemy && map[row - 1][lastCol - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                // --- '��' ��� �м� to ---

                // --- '��' ��� �м� from ---
                // ��뵹�� �߰��� ��� '��'��� �м�
                // ������ ���� '��' ����翡 ��ȣ�� �ű�ٸ�
                // �� == 1
                // �� �� == 2 3
                if (lastCol < 7 && row < 7) {
                    if (map[row + 1][lastCol] == enemy && map[row + 1][lastCol + 1] == enemy) { // ����� ���� 2���� 3���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row][lastCol - 1] == enemy) { // ����� ���� 1���� 2���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol < 7 && row > 0) {
                    if (map[row - 1][lastCol] == enemy && map[row][lastCol + 1] == enemy) { // ����� ���� 1���� 3���� �ִ� ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }

                // --- '��' ��� �м� to ---

                // --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
                // ��뵹�� �߰��� ��� '\'��� �м�
                // ������ ���� '\' ����翡 ��ȣ�� �ű�ٸ�
                // �� == 1
                // �� == 2
                // �� == 3
                if (lastCol < 6 && row < 6) {
                    if (map[row + 1][lastCol + 1] == enemy && map[row + 2][lastCol + 2] == enemy) { // ����� ���� 1���� 2���� �ִ�
                        // ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0 && row < 7 && lastCol < 7) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row + 1][lastCol + 1] == enemy) { // ����� ���� 1���� 3���� �ִ�
                        // ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 1 && row > 1) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row - 2][lastCol - 2] == enemy) { // ����� ���� 2���� 3���� �ִ�
                        // ���
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
            }
        }
        // --- '\' ��� �м� to ---
        if (cnt == 1) // ������ ���� �� �����̸� true
            return true;

        lose_cnt_row = cnt;

        return false; // ������ ���� ���ų� �������̸� false
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
                    // �� == 3
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
                    // �� == 1
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
                    // �� == 1
                    // �� == 2
                    // �� == 3
                    if (col < 6 && row < 6) {
                        if (map[row + 1][col + 1] == order && map[row + 2][col + 2] == order) { // ����� ���� 1���� 2���� �ִ� ���
                            myRow = row;
                            myCol = col;
                            return true;
                        }
                    }
                    if (col > 0 && row > 0 && row < 7 && col < 7) {
                        if (map[row - 1][col - 1] == order && map[row + 1][col + 1] == order) { // ����� ���� 1���� 3���� �ִ� ���
                            myRow = row;
                            myCol = col;
                            return true;
                        }
                    }
                    if (col > 1 && row > 1) {
                        if (map[row - 1][col - 1] == order && map[row - 2][col - 2] == order) { // ����� ���� 2���� 3���� �ִ� ���
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

    // ������ �̱�� ����� ���� ����, �ȵθ� ���� ����� ���� ���� ��� ���� ���� ���������� �� �� �� �м��ϴ� �޼ҵ�
    boolean OneMoreAnalysis(int inputRow, int inputCol, String opt) {

        // --- �޼ҵ� ���� ---
        // 1. �ش� ��ġ�� ���� ������ ���, ���� ���� �������� �Ǵ�
        // 2. �����ϸ�, �� ���� ���� ��ġ�� ����ġ�� 0���� ���� �� true �� ����
        // 3. true �� �����ϰ� �Ǹ�, ���� ����ġ�� ������ �ٸ� ��ġ�� Ž��
        // 4. ���� ���� ��ġ�� �������� ������ false �� ����

        // ���� �̹� �� row �� �� ���� �ξ��� ���, �� row�� ���� ��� col�� ���� ���輺 �м�(�Ű������� "row" �� �ԷµǾ���
        // ���)
        if (opt.equals("row")) {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
                // �ش� ��ġ�� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
                if (lose_cnt_row >= 2) {
                        for(int j = 0 ; j < PixelTester.SIZE_OF_BOARD; j ++) { myMap[inputRow][j] = 0; }
                    return true;
                }
                if (i == inputCol)
                    continue;

                // --- '��' �� 3 ���� ��� Ž�� from (�⺻��� ���� + ����� ������� �� ���� �κ��� �����ϴ� ����) ---
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����
                if (inputRow != 0 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
                        return true; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
                    }
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                if (inputRow != 7 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                if (inputRow != 7 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '��' �� 3 ���� ��� Ž�� to ---

                // --- '��' �� 3 ���� ��� Ž�� from ---
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����
                if (inputRow != 0 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                if (inputRow != 0 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                if (inputRow != 7 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '��' �� 3 ���� ��� Ž�� from ---

                // --- '\' �� 3 ���� ��� Ž�� from ---
                // '\' �� �� ������ �Ʒ� ���� ������� ���
                if (inputRow != 0 && inputRow != 1 && i != 0 && i != 1) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
                            && map[inputRow - 2][i - 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' �� �� �߾� ���� ������� ��� ����
                if (inputRow != 0 && inputRow != 7 && i != 0 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
                            && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' �� �� ���� �� ���� ������� ��� ����
                if (inputRow != 6 && inputRow != 7 && i != 6 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i + 1] == enemy
                            && map[inputRow + 2][i + 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '\' �� 3 ���� ��� Ž�� to ---
            }
        }

        // ���� �̹� �� col �� �� ���� �ξ��� ���, �� col�� ���� ��� row�� ���� ���輺 �м�(�Ű������� "row" �� �ƴ� �ٸ�
        // ������ �ԷµǾ��� ���)
        else {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
                // �ش� ��ġ�� ���� ���� �� ����ϴ� ���Ǻ� �˻� �̱� ������, ���� ���� �˻� ����
                if (lose_cnt_col >= 2) {
                    for(int j = 0 ; j < PixelTester.SIZE_OF_BOARD; j ++) { myMap[inputRow][j] = 0; }
                    return true;
                }
                if (i == inputRow)
                    continue;

                // --- '��' �� 3 ���� ��� Ž�� from (�⺻��� ���� + ����� ������� �� ���� �κ��� �����ϴ� ����) ---
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����
                if (i != 0 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0; // ������ �̱�� ����� ���� ��� �η��� �ߴ� ���� ����ġ�� 0���� ����
                        return true; // �� ���� ã�Ƶ� ���� ���� �� �̱�� �ǹǷ� �� �̻��� �м��� �� �ʿ䰡 ����
                    }
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                if (i != 7 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                if (i != 7 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '��' �� 3 ���� ��� Ž�� to ---

                // --- '��' �� 3 ���� ��� Ž�� from ---
                // '��' �� �� ������ �Ʒ� ���� ������� ��� ����
                if (i != 0 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� �߾� ���� ������� ��� ����
                if (i != 0 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '��' �� �� ���� �� ���� ������� ��� ����
                if (i != 7 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '��' �� 3 ���� ��� Ž�� to ---

                // --- '\' �� 3 ���� ��� Ž�� from ---
                // '\' �� �� ������ �Ʒ� ���� ������� ���
                if (i != 0 && i != 1 && inputCol != 0 && inputCol != 1) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
                            && map[i - 2][inputCol - 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' �� �� �߾� ���� ������� ��� ����
                if (i != 0 && i != 7 && inputCol != 0 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
                            && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' �� �� ���� �� ���� ������� ��� ����
                if (i != 6 && i != 7 && inputCol != 6 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol + 1] == enemy
                            && map[i + 2][inputCol + 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '\' �� 3 ���� ��� Ž�� to ---
            }

        }
        // ���� �ش� ��ġ�� ���� ���Ƶ�, ���� ���� �������� �ʴٸ� false ����
        return false;
    }
}