import java.awt.*;

public class PixelPlayer06 extends Player{
    PixelPlayer06(int[][] map) {
        super(map);
    }

    int order;
    int enermy;
    int[][] myMap = new int [PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // ����ġ�� ������ ���� ��
    int lastRow;
    int lastCol;


    Point nextPosition(Point lastPosition) {

        //�Լ� ����
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


        lastRow = (int)lastPosition.getX();
        lastCol = (int)lastPosition.getY();

        order = PixelTester.turn;

        if(order == 1) enermy = 2;
        else enermy = 1;
        Point nextPosition = null;
        System.out.println(lastRow + " " + lastCol);



        for(int i = 0; i < 8; i++) // �ʱ� ����ġ ����
            for(int j = 0; j < 8; j++)
                myMap[i][j] = 0;

        this.SetWeightForAttack();
        // ������ �̱�� ���
        // �̱�� �޼ҵ�
        // ������ ���� ���
        // ��밡 ���� �Ѱ��� �� �θ� �̱�� ��츦 ���� �޼ҵ�(�� ���� ���ڸ��� ���� ����ġ ����)
        this.SetWeightForDefense();

        // --- ����ġ Ž�� �� ����ε��� ���� from ---
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(map[i][j] == 1) { // ���Ͽ� ���� �޸������ ��
                    // ����ġ �ο� (��� ���ǽ� ������� �ʿ� >> ���� �������� ��� � �����Դ� �밢���̰� � �����Դ� ������)
                    if(i != 0)
                        myMap[i-1][j] += 2; // row �Ʒ��� ����ġ 2 �ο�
                    if(i != 7)
                        myMap[i+1][j] += 2; // row ���� ����ġ 2 �ο�
                    if(j != 0)
                        myMap[i][j-1] += 2; // col ���� ����ġ 2 �ο�
                    if(j != 7)
                        myMap[i][j+1] += 2; // col ������ ����ġ 2 �ο�
                    if(i != 7 && j != 7)
                        myMap[i+1][j+1] += 1; // �������� ����ġ 1 �ο�
                    if(i != 0 && j != 0)
                        myMap[i-1][j-1] += 1; // ���ʾƷ� ����ġ 1 �ο�
                    if(i != 7 && j != 0)
                        myMap[i+1][j-1] += 3; // ������ ����ġ 3 �ο�
                    if(i != 0 && j != 7)
                        myMap[i-1][j+1] += 3; // �����ʾƷ� ����ġ 3 �ο�
                }
            }
        }

        
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

        // row : ��(x�� ���� ����), col : ��(y�� ���� ����)
        int best_row = -1, best_col = -1; // ��Ŀ� ���� ����ġ�� �ִ��� ���� �����ϱ� ���� ����
        int row_index = -1, col_index = -1; // ��Ŀ� ���� ����ġ�� �ִ��� �ε����� �����ϱ� ���� ����

        // ��ġ�� ��� �����غ��� ��
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

        // row�Ǵ� col�� ���� ����ġ�� �� ��, �ε����� ���� ( �Ȱ��� ��쿡�� ��� �� ��������)
        if( best_row >= best_col ) // ���̵��� ���� ����ġ���� ���̵��� ���� ����ġ ������ ���� ���
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

    void SetWeightForDefense() {

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
                }
            }
            // --- '��' ��� �м� to ---

            // --- '\' ��� �м� from --- (�ǳ� �� ���� ���� ���� ���� �м� �� �ʿ� ���µ� �ű���� �� ��)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if(row == 0 || row == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if (col == 0 || col == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                    
                    boolean check = false;
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
                    
                    if (isPossible(row, col)) 
                    	
                }
            }
            // --- '\' ��� �м� to ---
        }

    // 2�� �÷��̾�, �İ��� ���
    else{}
    }

    void OneMoreAnalysis(int inputRow, int inputCol) {

        // inputRow�� ���� �ݺ��� 9���� ���
        // �� �м�
        if (inputRow != 7) {
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                // '��' �� �� ù��°(����)
                if(col == 7) continue;
                if(inputRow == 0 && (col == 0 || col == 6)) continue;
                if(inputRow == 6 && col == 6) continue;

                if (map[inputRow][col] == 0 && map[inputRow][col + 1] == enermy && map[inputRow + 1][col + 1] == enermy) {
                    myMap[inputRow][inputCol] = 1;
                }
            }
        }

        if (inputRow != 7) {
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                // '��' �� �� �ι�°(���)
                if (col == 0) continue;
                if (inputRow == 0 && (col == 1 || col == 7)) continue;
                if (inputRow == 6 && col == 7) continue;

                if (map[inputRow][col] == 0 && map[inputRow][col - 1] == enermy && map[inputRow + 1][col] == enermy) {
                    myMap[inputRow][inputCol] = 1;
                    break;
                }
            }
        }

        if (inputRow !=0) {
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                // '��' �� �� ����°
                if (map[inputRow][col] == 0 && map[inputRow][col - 1] == enermy && map[inputRow + 1][col] == enermy) {
                    myMap[inputRow][inputCol] = 1;
                    break;
                }
            }
        }
            // '��' �� �� ����°
        }


    boolean SetWeightForAttack() { // ������ �̱�� ���
        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // '��'�ڰ� �ϼ��Ǵ� ���
            if (row == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if(col == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

                // '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ��������, �����ʾƷ�)
                if((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7)) continue;

                // '��' ����� �߽����� �������� ��
				if (isPossible(row, col)) {
					if (map[row][col] == 0 && map[row][col - 1] == order && map[row + 1][col] == order) {
							return true;
					}
				}
			}
            
        }

        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // '��'�ڰ� �ϼ��Ǵ� ���
            if(row == 0) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if (col == 7) continue; // '��' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�

                // '��'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ���ʾƷ�, �����ʾƷ�)
                if((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6)) continue;

                // '��' ����� �߽����� �������� ��
                if (map[row][col] == 0 && map[row][col+1] == order && map[row-1][col] == order) {
                    myMap[row][col] = 1000;
                }
            }
        }

        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // '\'�ڰ� �ϼ��Ǵ� ���
            if(row == 0 || row == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if (col == 0 || col == 7) continue; // '\' ����� ���� ��������� �ʴ� �� ���� �ǳʶٱ�
                // '\'����� ���� ��������� �ʴ� Ư�� ��ǥ �ǳʶٱ�(������, ��������, �����ʾƷ�)
                if((row == 1 && col == 1) || (row == 6 && col == 6)) continue;

                // '\' ����� �߽����� �������� ��
                if (map[row][col] == 0 && map[row+1][col+1] == order && map[row-1][col-1] == order) {
                    myMap[row][col] = 1000;
                }
            }
        }
        return false;
    }
    
    boolean isPossible(int row, int col) {
    	if(map[row][col] == 0 && (lastRow == row || lastCol == col))
    		return true;
    	
    	return false;
    }

}

