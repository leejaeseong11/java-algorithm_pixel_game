import java.awt.*;

public class PixelPlayer06 extends Player{
    PixelPlayer06(int[][] map) {
        super(map);
    }

    int order;
    int enermy;
    int[][] myMap = new int [PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // 가중치를 저장할 나의 맵
    int lastRow;
    int lastCol;


    Point nextPosition(Point lastPosition) {

        //함수 순서
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


        lastRow = (int)lastPosition.getX();
        lastCol = (int)lastPosition.getY();

        order = PixelTester.turn;

        if(order == 1) enermy = 2;
        else enermy = 1;
        Point nextPosition = null;
        System.out.println(lastRow + " " + lastCol);



        for(int i = 0; i < 8; i++) // 초기 가중치 설정
            for(int j = 0; j < 8; j++)
                myMap[i][j] = 0;

        this.SetWeightForAttack();
        // 무조건 이기는 경우
        // 이기는 메소드
        // 무조건 지는 경우
        // 상대가 돌을 한개면 더 두면 이기는 경우를 막는 메소드(그 남은 한자리에 대한 가중치 설정)
        this.SetWeightForDefense();

        // --- 가중치 탐색 후 행렬인덱스 리턴 from ---
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(map[i][j] == 1) { // 내턴에 따라 달리해줘야 함
                    // 가중치 부여 (모든 조건식 재검토할 필요 >> 돌이 많아지는 경우 어떤 돌에게는 대각선이고 어떤 돌에게는 왼쪽임)
                    if(i != 0)
                        myMap[i-1][j] += 2; // row 아래쪽 가중치 2 부여
                    if(i != 7)
                        myMap[i+1][j] += 2; // row 위쪽 가중치 2 부여
                    if(j != 0)
                        myMap[i][j-1] += 2; // col 왼쪽 가중치 2 부여
                    if(j != 7)
                        myMap[i][j+1] += 2; // col 오른쪽 가중치 2 부여
                    if(i != 7 && j != 7)
                        myMap[i+1][j+1] += 1; // 오른쪽위 가중치 1 부여
                    if(i != 0 && j != 0)
                        myMap[i-1][j-1] += 1; // 왼쪽아래 가중치 1 부여
                    if(i != 7 && j != 0)
                        myMap[i+1][j-1] += 3; // 왼쪽위 가중치 3 부여
                    if(i != 0 && j != 7)
                        myMap[i-1][j+1] += 3; // 오른쪽아래 가중치 3 부여
                }
            }
        }

        
        // 가중치 재설정 부분
        // 1. 놓인 위치 가중치 제거
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(map[i][j] == 1 || map[i][j] == 2)
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
        for(int i = 0; i < 8; i++) {
            if(best_row <= myMap[i][lastCol]) {
                best_row = myMap[i][lastCol];
                row_index = i;
            }
        }

        // col에 대한 가중치 탐색
        for(int i = 0; i < 8; i++) {
            if(best_col <= myMap[lastRow][i]) {
                best_col = myMap[lastRow][i];
                col_index = i;
            }
        }

        System.out.println("열고정 최고 인덱스 " +lastRow + " " +col_index +" "+ myMap[lastRow][col_index]);
        System.out.println("행고정 최고 인덱스 " +row_index + " " + lastCol +" "+ myMap[row_index][lastCol]);

        // row또는 col에 대한 가중치를 비교 후, 인덱스값 리턴 ( 똑같을 경우에도 어디가 더 유리한지)
        if( best_row >= best_col ) // 행이동에 대한 가중치값이 열이동에 대한 가중치 값보다 높을 경우
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

    void SetWeightForDefense() {

        // 1번 플레이어, 선공일 경우
        if (order == 1) {
            // --- 'ㄱ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if (row == 7) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if(col == 0) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

                    // 'ㄱ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 오른쪽위, 오른쪽아래)
                    if((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7)) continue;

                    // 상대돌을 발견할 경우 'ㄱ'모양 분석
                    // 'ㄱ' 모양이 중심점을 기준으로 함
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
            // --- 'ㄱ' 모양 분석 to ---

            // --- 'ㄴ' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if(row == 0) continue; // 'ㄴ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if (col == 7) continue; // 'ㄴ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

                    // 'ㄴ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 왼쪽아래, 오른쪽아래)
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
            // --- 'ㄴ' 모양 분석 to ---

            // --- '\' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
            for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
                if(row == 0 || row == 7) continue; // '\' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
                for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                    if (col == 0 || col == 7) continue; // '\' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
                    
                    boolean check = false;
                    // '\' 모양이 절대 만들어지지 않는 특정 좌표 건너뛰기
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
            // --- '\' 모양 분석 to ---
        }

    // 2번 플레이어, 후공일 경우
    else{}
    }

    void OneMoreAnalysis(int inputRow, int inputCol) {

        // inputRow에 대한 반복문 9가지 경우
        // 행 분석
        if (inputRow != 7) {
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                // 'ㄱ' 자 중 첫번째(왼쪽)
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
                // 'ㄱ' 자 중 두번째(가운데)
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
                // 'ㄱ' 자 중 세번째
                if (map[inputRow][col] == 0 && map[inputRow][col - 1] == enermy && map[inputRow + 1][col] == enermy) {
                    myMap[inputRow][inputCol] = 1;
                    break;
                }
            }
        }
            // 'ㄱ' 자 중 세번째
        }


    boolean SetWeightForAttack() { // 무조건 이기는 경우
        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // 'ㄱ'자가 완성되는 경우
            if (row == 7) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if(col == 0) continue; // 'ㄱ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

                // 'ㄱ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 오른쪽위, 오른쪽아래)
                if((row == 0 && col == 1) || (row == 0 && col == 7) || (row == 6 && col == 7)) continue;

                // 'ㄱ' 모양이 중심점을 기준으로 함
				if (isPossible(row, col)) {
					if (map[row][col] == 0 && map[row][col - 1] == order && map[row + 1][col] == order) {
							return true;
					}
				}
			}
            
        }

        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // 'ㄴ'자가 완성되는 경우
            if(row == 0) continue; // 'ㄴ' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if (col == 7) continue; // 'ㄴ' 모양이 절대 만들어지지 않는 열 범위 건너뛰기

                // 'ㄴ'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 왼쪽아래, 오른쪽아래)
                if((row == 1 && col == 0) || (row == 7 && col == 0) || (row == 7 && col == 6)) continue;

                // 'ㄴ' 모양이 중심점을 기준으로 함
                if (map[row][col] == 0 && map[row][col+1] == order && map[row-1][col] == order) {
                    myMap[row][col] = 1000;
                }
            }
        }

        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) { // '\'자가 완성되는 경우
            if(row == 0 || row == 7) continue; // '\' 모양이 절대 만들어지지 않는 행 범위 건너뛰기
            for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
                if (col == 0 || col == 7) continue; // '\' 모양이 절대 만들어지지 않는 열 범위 건너뛰기
                // '\'모양이 절대 만들어지지 않는 특정 좌표 건너뛰기(왼쪽위, 오른쪽위, 오른쪽아래)
                if((row == 1 && col == 1) || (row == 6 && col == 6)) continue;

                // '\' 모양이 중심점을 기준으로 함
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

