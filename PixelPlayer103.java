import java.awt.*;

public class PixelPlayer103 extends Player {
    PixelPlayer103(int[][] map) {
        super(map);
    }

    // 전역 필드
    int order; // 나의 턴 및 돌의 순서를 저장
    int enemy; // 상대의 턴 및 돌의 순서를 저장
    int[][] myMap = new int[PixelTester.SIZE_OF_BOARD][PixelTester.SIZE_OF_BOARD]; // 가중치를 저장하기 위한 맵
    int lastRow; // 상대방이 마지막에 놓은 행 인덱스를 저장
    int lastCol; // 상대방이 마지막에 놓은 열 인덱스를 저장
    int myRow = -1; // 이번 턴 지거나 이기는 경우를 위해 놓게 되는 돌의 행 인덱스
    int myCol = -1; // 이번 턴 지거나 이기는 경우를 위해 놓게 되는 돌의 열 인덱스
    int lose_cnt_row = 0;
    int lose_cnt_col = 0;

    Point nextPosition(Point lastPosition) {

        // 행과 열의 인덱스는 0 부터 시작, 전체 바둑판 크기는 8*8(0~7 * 0~7)
        // 1번째 턴이 파란색이며, 선공이고 바둑판에 1로 표기됨, 4행3열 부터 시작
        // 2번째 턴이 빨간색이며, 후공이고 바둑판에 2로 표기됨, 3행4열 부터 시작
        // 화살표 위치는 파란색돌, 4행3열부터 시작

        lastRow = (int) lastPosition.getX(); // 이전 턴에 놓은 상대방아의 행 인덱스 초기화
        lastCol = (int) lastPosition.getY(); // 이전 턴에 놓은 상대방아의 열 인덱스 초기화

        // 내가 선공플레이어 인지, 후공 플레이어 인지 저장하는 변수를 초기화
        order = PixelTester.turn;

        // 내가 선공이라면 order에 1을 저장하고 후공이라면 2를 저장
        // enemy 변수에는 그 반대로 2와 1을 저장
        if (order == 1)
            enemy = 2;
        else
            enemy = 1;

        // 리턴할 포인트 객체를 null 값 초기화
        Point nextPosition = null;

        // 가중치맵의 가중치를 0으로 초기화
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                myMap[i][j] = 1;

        // isWin() 함수를 호출하여 무조건 이기는 수가 있는지 판단
        if (this.isWin()) {
            // 만약 무조건 이기는 수가 있으면 그 인덱스를 리턴
            nextPosition = new Point(myRow, myCol);
            System.out.print("win");
            return nextPosition;
        }

        // islose() 함수를 호출하여 무조건 지는 수가 있는지 판단
        if (isLose() == true) {
            // 만약 무조건 지는 수가 하나 있으면 막음
            nextPosition = new Point(myRow, myCol);
            myMap[myRow][myCol] = 100;
            System.out.print("lose");
            //return nextPosition;
        }

        // --- 위에 0 으로 초기화한 가중치맵에 가중치 할당 from ---
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // 내가 선공이면, 내 돌의 위치를 기준으로 맵의 가중치를 할당
                if (map[i][j] == order) {
                    if (i != 0)
                        myMap[i - 1][j] += 4; // 위쪽 가중치 4 부여
                    if (i != 7)
                        myMap[i + 1][j] += 4; // 아래쪽 가중치 4 부여
                    if (j != 0)
                        myMap[i][j - 1] += 4; // 왼쪽 가중치 4 부여
                    if (j != 7)
                        myMap[i][j + 1] += 4; // 오른쪽 가중치 4 부여
                    if (i != 7 && j != 7)
                        myMap[i + 1][j + 1] += 5; // 오른쪽아래 가중치 5 부여
                    if (i != 0 && j != 0)
                        myMap[i - 1][j - 1] += 5; // 왼쪽위 가중치 5 부여
                    if (i != 7 && j != 0)
                        myMap[i + 1][j - 1] += 1; // 왼쪽아래 가중치 1 부여
                    if (i != 0 && j != 7)
                        myMap[i - 1][j + 1] += 1; // 오른쪽위 가중치 1 부여
                    if (i < 6 && j < 6)
                        myMap[i + 2][j + 2] += 1; // 대각선*2 가중치 부여
                    if (i > 1 && j > 1)
                        myMap[i - 2][j - 2] += 1;
                    if (i < 6 && j < 7)
                        myMap[i + 2][j + 1] += 3; // 대각선*2 옆 가중치 부여
                    if (i > 1 && j > 0)
                        myMap[i - 2][j - 1] += 3;
                    if (i < 7 && j < 6)
                        myMap[i + 1][j + 2] += 3; // 대각선*2 옆 가중치 부여
                    if (i > 0 && j > 1)
                        myMap[i - 1][j - 2] += 3;
                    if (j < 6)
                        myMap[i][j + 2] += 2; // 왼쪽 오른쪽*2 가중치 부여
                    if (j > 1)
                        myMap[i][j - 2] += 2;
                    if (i < 6)
                        myMap[i + 2][j] += 2; // 위 아래*2 가중치 부여
                    if (i > 1)
                        myMap[i - 2][j] += 2;
                }
            }
        }

        // 가중치 재설정
        // 1. 상대방의 돌이나 나의 돌이 놓인 위치에 가중치를 -1로 설정
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (map[i][j] == 1 || map[i][j] == 2)
                    myMap[i][j] = -1;
            }
        }

        // 2. 판 이외의 부분의 가중치를 -1로 설정
        myMap[0][0] = -1;
        myMap[0][7] = -1;
        myMap[7][0] = -1;
        myMap[7][7] = -1;

        int best_row = -1, best_col = -1; // 행렬에 대한 가중치가 최대인 값을 저장하기 위한 필드
        int row_index = -1, col_index = -1; // 행렬에 대한 가중치가 최대인 인덱스를 저장하기 위한 필드
        boolean check = true; // 나의 다음 수를 결정하기 위해 while에 쓰일 불린 필드

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

            if (best_col == 0 && best_row == 0) { // 나의 다음 수가 위험한 경우 = 최선의 수가 가중치가 0인 경우
                nextPosition = new Point(row_index, lastCol);
                check = false;

            }

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

    boolean isPossible(int row, int col) { // 돌을 놓아도 되는지 검사하는 메소드
        if (map[row][col] == 0 && (lastRow == row || lastCol == col)) { // 게임판이 비어있으면서 가로나 세로만 움직일 수 있음
            return true;
        }

        return false;
    }

    boolean isLose() { // 막지 않으면 지는 경우
        // 모든 경우에 대해서 내가 둘 곳이 row와 col이라고 생각
        int cnt = 0; // 위험한 곳이 몇 군데인지 확인
        // 행 고정 분석
        // --- 'ㄱ' 모양 분석 from ---
        for (int col = 0; col < PixelTester.SIZE_OF_BOARD; col++) {
            if (isPossible(lastRow, col)) {
                // 상대돌을 발견할 경우 'ㄱ'모양 분석
                // 다음과 같은 'ㄱ' 돌모양에 번호를 매긴다면
                // ㅇ ㅇ == 1 2
                // ㅇ == 3
                if (col > 0 && lastRow < 7) {
                    if (map[lastRow][col - 1] == enemy && map[lastRow + 1][col] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col < 7 && lastRow < 7) {
                    if (map[lastRow][col + 1] == enemy && map[lastRow + 1][col + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0) {
                    if (map[lastRow - 1][col] == enemy && map[lastRow - 1][col - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }

                // --- 'ㄱ' 모양 분석 to ---

                // --- 'ㄴ' 모양 분석 from ---
                // 상대돌을 발견할 경우 'ㄴ'모양 분석
                // 다음과 같은 'ㄴ' 돌모양에 번호를 매긴다면
                // ㅇ == 1
                // ㅇ ㅇ == 2 3
                if (col < 7 && lastRow < 7) {
                    if (map[lastRow + 1][col] == enemy && map[lastRow + 1][col + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow][col - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col < 7 && lastRow > 0) {
                    if (map[lastRow - 1][col] == enemy && map[lastRow][col + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }

                // --- 'ㄴ' 모양 분석 to ---

                // --- '\' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
                // 상대돌을 발견할 경우 '\'모양 분석
                // 다음과 같은 '\' 돌모양에 번호를 매긴다면
                // ㅇ == 1
                // ㅇ == 2
                // ㅇ == 3
                if (col < 6 && lastRow < 6) {
                    if (map[lastRow + 1][col + 1] == enemy && map[lastRow + 2][col + 2] == enemy) { // 상대의 돌이 1번과 2번에 있는
                        // 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 0 && lastRow > 0 && lastRow < 7 && col < 7) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow + 1][col + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는
                        // 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                if (col > 1 && lastRow > 1) {
                    if (map[lastRow - 1][col - 1] == enemy && map[lastRow - 2][col - 2] == enemy) { // 상대의 돌이 2번과 3번에 있는
                        // 경우
                        myRow = lastRow;
                        myCol = col;
                        cnt++;
                        continue;
                    }
                }
                // --- '\' 모양 분석 to ---

            }
        }
        if (cnt == 1) // 행 고정시 위험한 곳이 한 군데이면 true
            return true;

        lose_cnt_col = cnt;

        cnt = 0; // 위험한 곳 수 초기화

        // 열 고정 분석
        // --- 'ㄱ' 모양 분석 from ---
        for (int row = 0; row < PixelTester.SIZE_OF_BOARD; row++) {
            if (isPossible(row, lastCol)) {
                // 상대돌을 발견할 경우 'ㄱ'모양 분석
                // 다음과 같은 'ㄱ' 돌모양에 번호를 매긴다면
                // ㅇ ㅇ == 1 2
                // ㅇ == 3
                if (lastCol > 0 && row < 7) {
                    if (map[row][lastCol - 1] == enemy && map[row + 1][lastCol] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol < 7 && row < 7) {
                    if (map[row][lastCol + 1] == enemy && map[row + 1][lastCol + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0) {
                    if (map[row - 1][lastCol] == enemy && map[row - 1][lastCol - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                // --- 'ㄱ' 모양 분석 to ---

                // --- 'ㄴ' 모양 분석 from ---
                // 상대돌을 발견할 경우 'ㄴ'모양 분석
                // 다음과 같은 'ㄴ' 돌모양에 번호를 매긴다면
                // ㅇ == 1
                // ㅇ ㅇ == 2 3
                if (lastCol < 7 && row < 7) {
                    if (map[row + 1][lastCol] == enemy && map[row + 1][lastCol + 1] == enemy) { // 상대의 돌이 2번과 3번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row][lastCol - 1] == enemy) { // 상대의 돌이 1번과 2번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol < 7 && row > 0) {
                    if (map[row - 1][lastCol] == enemy && map[row][lastCol + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }

                // --- 'ㄴ' 모양 분석 to ---

                // --- '\' 모양 분석 from --- (건너 뛴 돌이 있을 때는 구지 분석 할 필요 없는데 거기까진 안 함)
                // 상대돌을 발견할 경우 '\'모양 분석
                // 다음과 같은 '\' 돌모양에 번호를 매긴다면
                // ㅇ == 1
                // ㅇ == 2
                // ㅇ == 3
                if (lastCol < 6 && row < 6) {
                    if (map[row + 1][lastCol + 1] == enemy && map[row + 2][lastCol + 2] == enemy) { // 상대의 돌이 1번과 2번에 있는
                        // 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 0 && row > 0 && row < 7 && lastCol < 7) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row + 1][lastCol + 1] == enemy) { // 상대의 돌이 1번과 3번에 있는
                        // 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
                if (lastCol > 1 && row > 1) {
                    if (map[row - 1][lastCol - 1] == enemy && map[row - 2][lastCol - 2] == enemy) { // 상대의 돌이 2번과 3번에 있는
                        // 경우
                        myRow = row;
                        myCol = lastCol;
                        cnt++;
                        continue;
                    }
                }
            }
        }
        // --- '\' 모양 분석 to ---
        if (cnt == 1) // 위험한 곳이 한 군데이면 true
            return true;

        lose_cnt_row = cnt;

        return false; // 위험한 곳이 없거나 여러곳이면 false
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
                    // ㅇ == 3
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
                    // ㅇ == 1
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
                    // ㅇ == 1
                    // ㅇ == 2
                    // ㅇ == 3
                    if (col < 6 && row < 6) {
                        if (map[row + 1][col + 1] == order && map[row + 2][col + 2] == order) { // 상대의 돌이 1번과 2번에 있는 경우
                            myRow = row;
                            myCol = col;
                            return true;
                        }
                    }
                    if (col > 0 && row > 0 && row < 7 && col < 7) {
                        if (map[row - 1][col - 1] == order && map[row + 1][col + 1] == order) { // 상대의 돌이 1번과 3번에 있는 경우
                            myRow = row;
                            myCol = col;
                            return true;
                        }
                    }
                    if (col > 1 && row > 1) {
                        if (map[row - 1][col - 1] == order && map[row - 2][col - 2] == order) { // 상대의 돌이 2번과 3번에 있는 경우
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

    // 무조건 이기는 경우의 돌도 없고, 안두면 지는 경우의 돌도 없는 경우 다음 수가 위험한지를 한 번 더 분석하는 메소드
    boolean OneMoreAnalysis(int inputRow, int inputCol, String opt) {

        // --- 메소드 설명 ---
        // 1. 해당 위치에 돌을 놓았을 경우, 다음 수가 위험한지 판단
        // 2. 위험하면, 그 돌을 놓을 위치의 가중치를 0으로 설정 후 true 를 리턴
        // 3. true 를 리턴하게 되면, 높은 가중치를 가지는 다른 위치를 탐색
        // 4. 돌을 놓을 위치가 위험하지 않으면 false 를 리턴

        // 내가 이번 턴 row 중 한 곳에 두었을 경우, 그 row에 대한 모든 col에 대해 위험성 분석(매개변수가 "row" 로 입력되었을
        // 경우)
        if (opt.equals("row")) {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
                // 해당 위치에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
                if (lose_cnt_row >= 2) {
                        for(int j = 0 ; j < PixelTester.SIZE_OF_BOARD; j ++) { myMap[inputRow][j] = 0; }
                    return true;
                }
                if (i == inputCol)
                    continue;

                // --- 'ㄱ' 자 3 가지 모양 탐색 from (기본모양 조건 + 모양이 만들어질 수 없는 부분을 제외하는 조건) ---
                // 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준
                if (inputRow != 0 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow - 1][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
                        return true; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
                    }
                }
                // 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
                if (inputRow != 7 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (inputRow != 7 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow][i + 1] == enemy && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- 'ㄱ' 자 3 가지 모양 탐색 to ---

                // --- 'ㄴ' 자 3 가지 모양 탐색 from ---
                // 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준
                if (inputRow != 0 && i != 0) {
                    if (map[inputRow][i] == 0 && map[inputRow][i - 1] == enemy && map[inputRow - 1][i - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
                if (inputRow != 0 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i] == enemy && map[inputRow][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (inputRow != 7 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i] == enemy && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- 'ㄴ' 자 3 가지 모양 탐색 from ---

                // --- '\' 자 3 가지 모양 탐색 from ---
                // '\' 자 중 오른쪽 아래 돌이 비어있을 경우
                if (inputRow != 0 && inputRow != 1 && i != 0 && i != 1) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
                            && map[inputRow - 2][i - 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' 자 중 중앙 돌이 비어있을 경우 기준
                if (inputRow != 0 && inputRow != 7 && i != 0 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow - 1][i - 1] == enemy
                            && map[inputRow + 1][i + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (inputRow != 6 && inputRow != 7 && i != 6 && i != 7) {
                    if (map[inputRow][i] == 0 && map[inputRow + 1][i + 1] == enemy
                            && map[inputRow + 2][i + 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '\' 자 3 가지 모양 탐색 to ---
            }
        }

        // 내가 이번 턴 col 중 한 곳에 두었을 경우, 그 col에 대한 모든 row에 대해 위험성 분석(매개변수가 "row" 가 아닌 다른
        // 것으로 입력되었을 경우)
        else {
            for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
                // 해당 위치에 돌을 놓은 후 고려하는 조건부 검색 이기 때문에, 놓을 곳은 검색 제외
                if (lose_cnt_col >= 2) {
                    for(int j = 0 ; j < PixelTester.SIZE_OF_BOARD; j ++) { myMap[inputRow][j] = 0; }
                    return true;
                }
                if (i == inputRow)
                    continue;

                // --- 'ㄱ' 자 3 가지 모양 탐색 from (기본모양 조건 + 모양이 만들어질 수 없는 부분을 제외하는 조건) ---
                // 'ㄱ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준
                if (i != 0 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i - 1][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0; // 상대방이 이기는 모양이 나올 경우 두려고 했던 곳의 가중치를 0으로 감소
                        return true; // 한 곳만 찾아도 적이 다음 턴 이기게 되므로 더 이상의 분석을 할 필요가 없음
                    }
                }
                // 'ㄱ' 자 중 중앙 돌이 비어있을 경우 기준
                if (i != 7 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄱ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (i != 7 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i][inputCol + 1] == enemy && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- 'ㄱ' 자 3 가지 모양 탐색 to ---

                // --- 'ㄴ' 자 3 가지 모양 탐색 from ---
                // 'ㄴ' 자 중 오른쪽 아래 돌이 비어있을 경우 기준
                if (i != 0 && inputCol != 0) {
                    if (map[i][inputCol] == 0 && map[i][inputCol - 1] == enemy && map[i - 1][inputCol - 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄴ' 자 중 중앙 돌이 비어있을 경우 기준
                if (i != 0 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol] == enemy && map[i][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // 'ㄴ' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (i != 7 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol] == enemy && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- 'ㄴ' 자 3 가지 모양 탐색 to ---

                // --- '\' 자 3 가지 모양 탐색 from ---
                // '\' 자 중 오른쪽 아래 돌이 비어있을 경우
                if (i != 0 && i != 1 && inputCol != 0 && inputCol != 1) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
                            && map[i - 2][inputCol - 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' 자 중 중앙 돌이 비어있을 경우 기준
                if (i != 0 && i != 7 && inputCol != 0 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i - 1][inputCol - 1] == enemy
                            && map[i + 1][inputCol + 1] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // '\' 자 중 왼쪽 위 돌이 비어있을 경우 기준
                if (i != 6 && i != 7 && inputCol != 6 && inputCol != 7) {
                    if (map[i][inputCol] == 0 && map[i + 1][inputCol + 1] == enemy
                            && map[i + 2][inputCol + 2] == enemy) {
                        myMap[inputRow][inputCol] = 0;
                        return true;
                    }
                }
                // --- '\' 자 3 가지 모양 탐색 to ---
            }

        }
        // 만약 해당 위치에 돌을 놓아도, 다음 수가 위험하지 않다면 false 리턴
        return false;
    }
}