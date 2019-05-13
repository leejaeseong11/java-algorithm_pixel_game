import java.awt.*;

public class PixelPlayer06 extends Player{
    PixelPlayer06(int[][] map) {
        super(map);
    }

    int order;
    int[][] myMap = new int [PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // ����ġ�� ������ ���� ��
    int countFind = 0; // �ش� ����� ����� ���� ���� ��� �𿴴��� �ľ��ϴ� ����
    int rowDef = -1; // ���� ���� ���ƾ��ϴ� ��ġ�� ��Ÿ���� row��ǥ
    int colDef = -1; // ���� ���� ���ƾ��ϴ� ��ġ�� ��Ÿ���� col��ǥ

    Point nextPosition(Point lastPosition) {

        // ��� ���� ����
        // 1. ���� ������ �̱� �� �ִ� ���� ��ǥ ��� ���
        // 2. ���� ���� �� ���ԵǸ� ���ԵǴ� ���� ��ǥ ��� ���
        // 3. �̱�Ȯ���� ���� ���� ���� ��ǥ ��� ���(�밢������ 2 ���� ���� ��� �̱�Ȯ���� �ö�)


        // ��� ���� �ε����� 0������ ����, ��ü �ٵ��� ũ��� 8*8(0~7 * 0~7)
        // 1��° ���� �Ķ����̸�, �����̰� �ٵ��ǿ� 1�� ǥ���, 4��3�� ���� ����
        // 2��° ���� �������̸�, �İ��̰� �ٵ��ǿ� 2�� ǥ���, 3��4�� ���� ����
        int lastRow = (int)lastPosition.getX();
        int lastCol = (int)lastPosition.getY();
//        int lastRow = 8 - (int)lastPosition.getY();
//        int lastCol = (int)lastPosition.getX();
        order = PixelTester.turn;
        Point nextPosition = null;
        System.out.println(lastRow + " " + lastCol);

        for(int i = 0; i < 8; i++) // �ʱ� ����ġ ����
            for(int j = 0; j < 8; j++)
                myMap[i][j] = 0;

//      myMap[0][0] = -1; // ���� ����� ��ġ
//      myMap[0][7] = -1;
//      myMap[7][0] = -1;
//      myMap[7][7] = -1;
//      myMap[3][4] = -1; // �ʱ� �������� ���� ��ġ
//      myMap[4][3] = -1;
//      myMap[x][y] = turn;

        // --- ����ġ Ž�� �� ����ε��� ���� from ---
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(map[i][j] == 1) { // ���Ͽ� ���� �޸������ ��
                    // ����ġ �ο� (��� ���ǽ� ������� �ʿ� >> ���� �������� ��� � �����Դ� �밢���̰� � �����Դ� ������)
                    if(i != 0)
                        myMap[i-1][j] = 2; // row �Ʒ��� ����ġ 2 �ο�
                    if(i != 7)
                        myMap[i+1][j] = 2; // row ���� ����ġ 2 �ο�
                    if(j != 0)
                        myMap[i][j-1] = 2; // col ���� ����ġ 2 �ο�
                    if(j != 7)
                        myMap[i][j+1] = 2; // col ������ ����ġ 2 �ο�
                    if(i != 7 && j != 7)
                        myMap[i+1][j+1] = 1; // �������� ����ġ 1 �ο�
                    if(i != 0 && j != 0)
                        myMap[i-1][j-1] = 1; // ���ʾƷ� ����ġ 1 �ο�
                    if(i != 7 && j != 0)
                        myMap[i+1][j-1] = 3; // ������ ����ġ 3 �ο�
                    if(i != 0 && j != 7)
                        myMap[i-1][j+1] = 3; // �����ʾƷ� ����ġ 3 �ο�
                }
            }
        }

        // ��밡 ���� �Ѱ��� �� �θ� �̱�� ��츦 ���� �޼ҵ�(�� ���� ���ڸ��� ���� ����ġ ����)
        this.SetWeightForDefense();

        // ����ġ �缳�� �κ�
        // 1. ���� ��ġ ����ġ ����
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(map[i][j] == 1 || map[i][j] == 2)
                    myMap[i][j] = -1;
            }
        }
        // 2. �� �̿� �κ��� ����ġ ����
        myMap[0][0] = -1;
        myMap[0][7] = -1;
        myMap[7][0] = -1;
        myMap[7][7] = -1;

//        for(int i = 0; i < 8; i++) {
//            for(int j = 0; j < 8; j++)
//                System.out.print("("+i +","+ j+")"+myMap[i][j] + "\t");
//            System.out.println();
//        }
//        System.out.println(lastRow + " c " + lastCol);

        // row : ��(x�� ���� ����), col : ��(y�� ���� ����)
        int best_row = -1, best_col = -1; // ��Ŀ� ���� ����ġ�� �ִ��� ���� �����ϱ� ���� ����
        int row_index = -1, col_index = -1; // ��Ŀ� ���� ����ġ�� �ִ��� �ε����� �����ϱ� ���� ����

        // row�� ���� ����ġ Ž��
        for(int i = 0; i < 8; i++) {
            if(best_row <= myMap[i][lastCol]) {
                best_row = myMap[i][lastCol];
                row_index = i;
            }
        }

        // col�� ���� ����ġ Ž��
        for(int i = 0; i < 8; i++) {
            if(best_col <= myMap[lastRow][i]) {
                best_col = myMap[lastRow][i];
                col_index = i;
            }
        }

        System.out.println("������ �ְ� �ε��� " +lastRow + " " +col_index +" "+ myMap[lastRow][col_index]);
        System.out.println("����� �ְ� �ε��� " +row_index + " " + lastCol +" "+ myMap[row_index][lastCol]);

       // for (int i = 0; i < 8 ; i++)

        //}

        // row�Ǵ� col�� ���� ����ġ�� �� ��, �ε����� ����
        if( best_row >= best_col ) // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
            nextPosition = new Point(row_index, lastCol); // ��ǥ�� �ݴ�� ����
        else // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
            nextPosition = new Point(lastRow, col_index); // ��ǥ�� �ݴ�� ����
        // --- ����ġ Ž�� �� ����ε��� ���� to ---

        return nextPosition;
    }

    void SetWeightForDefense() {

//        int countFind = 0; // �ش� ����� ����� ���� ���� ��� �𿴴��� �ľ��ϴ� ����
//        int rowDef = -1; // ���� ���� ���ƾ��ϴ� ��ġ�� ��Ÿ���� row��ǥ
//        int colDef = -1; // ���� ���� ���ƾ��ϴ� ��ġ�� ��Ÿ���� col��ǥ
//        int testcountFind = 0; // �׽�Ʈ�� ����

        // 1�� �÷��̾�, ������ ���
        if (order == 1) {
            // --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)

            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if (row == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if(col == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

                    // '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ��������, �����ʾƷ�)
                    if((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7)) continue;

                    // ��뵹�� �߰��� ��� '��'��� �м�
                    // '��' ����� �߽����� �������� ��
                    if (map[row][col] == 2 && map[row][col-1] == 2 && map[row+1][col] == 0) {
                        myMap[row+1][col] = 10;
                    }
                    else if (map[row][col] == 2 && map[row][col-1] == 0 && map[row+1][col] == 2)  {
                        myMap[row][col-1] = 10;
                    }
                    else if (map[row][col] == 0 && map[row][col-1] == 2 && map[row+1][col] == 2) {
                        myMap[row][col] = 10;
                    }
                        // �� �𿩾��ϴ� 3���� ���� �� �߰ߵ� ���� ������ ������ 2���� �� �� 1���� �𿴴ٸ� �̿� ���� �ε��� ����
//                        if (map[row + 1][col] == 2) { countFind++; }
//                        else if(map[row + 1][col] == 0 ) { rowDef = row + 1; colDef = col; }
//
//                        if (map[row][col - 1] == 2) { countFind++;}
//                        else if(map[row][col - 1] == 0) { rowDef = row; colDef = col - 1; }
//
//                        // ���� ���� 1���� �θ� �̱�� ��Ȳ�̶��, �� ��ǥ�� ����ġ ������ 10���� ����
//                        if(countFind >= 1) { this.myMap[rowDef][colDef] = 10; }
//                        // �� ������ ����ġ ������ ���� ������ ���� �ʱ�ȭ
//                        countFind = 0;
//                        rowDef = -1;
//                        colDef = -1;
                }
            }
            // --- '��' ��� �м� to ---

            // --- '��' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if(row == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if (col == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

                    // '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ���ʾƷ�, �����ʾƷ�)
                    if((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6)) continue;

                    if (map[row][col] == 2 && map[row][col+1] == 2 && map[row-1][col] == 0) {
                        myMap[row-1][col] = 10;
                    }
                    else if (map[row][col] == 2 && map[row][col+1] == 0 && map[row-1][col] == 2)  {
                        myMap[row][col+1] = 10;
                    }
                    else if (map[row][col] == 0 && map[row][col+1] == 2 && map[row-1][col] == 2) {
                        myMap[row][col] = 10;
                    }

//                    // ��뵹�� �߰��� ��� '��'��� �м�
//                    if (map[row][col] == 2) {
//
//                        // �� �𿩾��ϴ� 3���� ���� �� �߰ߵ� ���� ������ ������ 2���� �� �� 1���� �𿴴ٸ� �̿� ���� �ε��� ����
//                        if (map[row - 1][col] == 2) { countFind++; }
//                        else if(map[row - 1][col] == 0 ) { rowDef = row - 1; colDef = col; }
//
//                        if (map[row][col + 1] == 2) { countFind++; }
//                        else if(map[row][col + 1] == 0) { rowDef = row; colDef = col + 1; }
//
//                        // ���� ���� 1���� �θ� �̱�� ��Ȳ�̶��, �� ��ǥ�� ����ġ ������ 10���� ����
//                        if(countFind >= 1) { this.myMap[rowDef][colDef] = 10; }
//                        // �� ������ ����ġ ������ ���� ������ ���� �ʱ�ȭ
//                        countFind = 0;
//                        rowDef = -1;
//                        colDef = -1;
//                    }
//                    else if (map[row][col] == 0 && map[row-1][col] == 2 && map[row][col+1] == 2) {
//                        this.myMap[row][col] = 10;
//                    }

                }
            }
            // --- '��' ��� �м� to ---

            // --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if(row == 0 || row == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if (col == 0 || col == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

                    // '\' ����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�
                    if((row == 1 && col == 1) || (row == 6 && col == 6)) continue;

                    if (map[row][col] == 2 && map[row+1][col+1] == 2 && map[row-1][col-1] == 0) {
                        myMap[row-1][col-1] = 10;
                    }
                    else if (map[row][col] == 2 && map[row+1][col+1] == 0 && map[row-1][col-1] == 2)  {
                        myMap[row+1][col+1] = 10;
                    }
                    else if (map[row][col] == 0 && map[row+1][col+1] == 2 && map[row-1][col-1] == 2) {
                        myMap[row][col] = 10;
                    }
//                    if(map[row][col] == 2) {
//
//                        if(map[row + 1][col + 1] == 2) { countFind++; }
//                        else if(map[row + 1][col + 1] == 0) { rowDef = row + 1; colDef = col + 1; }
//
//                        if(map[row - 1][col - 1] == 2) { countFind++; }
//                        else if(map[row - 1][col - 1] == 0) { rowDef = row - 1; colDef = col - 1; }
//
//                        // ���� ���� 1���� �θ� �̱�� ��Ȳ�̶��, �� ��ǥ�� ����ġ ������ 10���� ����
//                        if(countFind >= 1) { this.myMap[rowDef][colDef] = 10; }
//                        // �� ������ ����ġ ������ ���� ������ ���� �ʱ�ȭ
//                        countFind = 0;
//                        rowDef = -1;
//                        colDef = -1;
//                    }
//                    else if (map[row][col] == 0 && map[row-1][col-1] == 2 && map[row+1][col+1] == 2) {
//                        this.myMap[row][col] = 10;
//                    }

                }
            }
            // --- '\' ��� �м� to ---
        }

    // 2�� �÷��̾�, �İ��� ���
    else{}
    }
}
