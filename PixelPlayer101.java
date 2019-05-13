import java.awt.*;
import java.util.Scanner;

public class PixelPlayer101 extends Player {
	PixelPlayer101(int[][] map) { super(map); }
/*
	Scanner scan = new Scanner (System.in);

	public Point nextPosition(Point lastPosition) {
		Point nextPosition;

		String num = scan.nextLine();
		int x = Integer.parseInt(num.substring(0, 1));
		int y = Integer.parseInt(num.substring(1));

		nextPosition = new Point(y, x);
		return nextPosition;
	}
*/
	public Point nextPosition(Point lastPosition) {
		int x = (int)lastPosition.getX(), y = (int)lastPosition.getY();
		int myNum = map[(int)currentPosition.getX()][(int)currentPosition.getY()];
		Point nextPosition;

		for( int n = 0; n < 8; n++ )
		{
			for( int m = 0; m < 8; m++ )
			{
				if( myNum == map[n][m] )
				{
					for(int i = 0; i < PixelTester.SIZE_OF_BOARD; i++)
					{
						if(map[i][y] == 0 && ( i == n || y == m ))
						{
							nextPosition = new Point(i, y);
							System.out.println(nextPosition);
							return nextPosition;
						}
					}
					for(int i = 0; i < PixelTester.SIZE_OF_BOARD; i++)
					{
						if(map[x][i] == 0 && ( x == n || i == m ))
						{
							nextPosition = new Point(x, i);
							System.out.println(nextPosition);
							return nextPosition;
						}
					}
				}
			}
		}

		nextPosition = new Point(x, y);
		return nextPosition;
	}

}