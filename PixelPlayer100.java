import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class PixelPlayer100 extends Player {
	PixelPlayer100(int[][] map) { super(map); }

	/*Scanner scan = new Scanner (System.in);

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
		int count = 0, find = 0;
		Point nextPosition;
		Random random = new Random();
		int seed = random.nextInt(2);

		if( seed == 0)
		{
			while(true)
			{
				x = x + 1;
				count++;
				if( x >= PixelTester.SIZE_OF_BOARD )
				{
					x = 0;
				}
				if(map[x][y] == 0 )
				{
					find = 1;
					break;
				}
				if( count > PixelTester.SIZE_OF_BOARD )
				{
					x = (int)lastPosition.getX();
					count = 0;
					break;
				}
			}
			while(find == 0)
			{
				y = y + 1;
				count++;
				if( y >= PixelTester.SIZE_OF_BOARD){
					y = 0;
				}

				if(map[x][y] == 0 )
				{
					break;
				}
				if( count > PixelTester.SIZE_OF_BOARD )
					break;
			}
		}
		else
		{
			while(true)
			{
				y = y + 1;
				count++;
				if( y >= PixelTester.SIZE_OF_BOARD){
					y = 0;
				}

				if(map[x][y] == 0 )
				{
					find = 1;
					break;
				}
				if( count > PixelTester.SIZE_OF_BOARD )
				{
					y = (int)lastPosition.getY();
					count = 0;
					break;
				}
			}
			while(find == 0)
			{
				x = x + 1;
				count++;
				if( x >= PixelTester.SIZE_OF_BOARD )
				{
					x = 0;
				}
				if(map[x][y] == 0 )
				{
					break;
				}
				if( count > PixelTester.SIZE_OF_BOARD )
					break;
			}

		}

		nextPosition = new Point(x, y);
		return nextPosition;
	}

}