import java.awt.Point;
//https://docs.oracle.com/javase/8/docs/api/index.html?java/awt/Point.html

public abstract class Player {
	public int[][] map;
	public Point currentPosition;
  
  	protected Player() {
  	}
  
	protected Player(int[][] map) {
		this.map = map;
		this.currentPosition = new Point();
		this.currentPosition.setLocation(0, 0);
	}
	
	void setCurrentPosition(Point currentPosition) {
		this.currentPosition.setLocation(currentPosition);
	}
	// abstract methord!!!
	abstract Point nextPosition(Point lastPosition);
}
