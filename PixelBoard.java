import java.io.*;
import java.awt.*;

import javax.swing.*;

import java.awt.image.*;

import javax.imageio.*;

public class PixelBoard extends JComponent{
	private int [][] map;
	public static boolean isendb;
	private int winner;

	public PixelBoard(int m[][]) {
		map = m;
		isendb = false;
		/*JButton startBtn = new JButton("Start!");
		startBtn.setLocation(00, 00);
		startBtn.setSize(100,	100);
		add(startBtn);*/
	}

	public void isend(int win) {
		isendb = true;
		winner = win;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		BufferedImage bgimg = null, redDolimg = null, blueDolimg = null, player1img = null, player2img = null, tableimg = null, arrow = null, arrow2 = null;
		int numberOfblueDol = 0, numberOfredDol = 0;
		String snumberOfblueDol = new String();
		String snumberOfredDol = new String();
		String p1TimeString;// = new String();
		String p2TimeString;// = new String();
		Font fn = new Font("Helvetica", Font.CENTER_BASELINE, 16);
		Font cr = new Font("Helvetica", Font.ITALIC, 8);

		try {
			bgimg = ImageIO.read(new File("src/board.png"));
			redDolimg = ImageIO.read(new File("src/redDol.png"));
			blueDolimg = ImageIO.read(new File("src/blueDol.png"));
			player1img = ImageIO.read(new File("src/player1.jpg"));
			player2img = ImageIO.read(new File("src/player2.jpg"));
			arrow = ImageIO.read(new File("src/arrow.png"));
			arrow2 = ImageIO.read(new File("src/arrow2.png"));
		} catch( Exception e ) {
			System.out.println("IO Error");
		}

		g2.drawImage(bgimg, 0, 0, 590, 590, this);	
		g2.setColor(Color.blue);
		g2.drawRect(605, 10, 160, 130);
		g2.drawImage(player1img, 625, 20, 120, 80, this);
		g2.setFont(fn);
		g2.drawString("Player1: ", 610, 130);
		g2.setColor(Color.black);
		g2.drawImage(blueDolimg, 685, 110, 25, 25, this);

		g2.setColor(Color.red);
		g2.drawRect(605, 460, 160, 130);
		g2.drawImage(player2img, 625, 470, 120, 80, this);
		g2.setFont(fn);
		g2.drawString("Player2: ", 610, 580);
		g2.setColor(Color.black);
		g2.drawImage(redDolimg, 685, 560, 25, 25, this);

		g2.drawImage(arrow, 20 , (int)(PixelTester.dolPosition.getX() * 54) + 80 , this);
		g2.drawImage(arrow2, (int)(PixelTester.dolPosition.getY() * 54) + 80 , 530 , this);

		for ( int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
			for ( int j = 0; j < PixelTester.SIZE_OF_BOARD; j++) {
				if (map[i][j] == 1 ) { 				
					g2.drawImage(blueDolimg, 85 +(54 * j) - 3, 85+ (54 * i) - 1, this);
					numberOfblueDol++;
				} else if (map[i][j] == 2 ) { 
					g2.drawImage(redDolimg, 85+ (54 * j) - 3, 85+ (54 * i) - 1, this);
					numberOfredDol++;
				}
				//
				if (PixelTester.dolSequence[i][j] != 0) {
					g2.drawString(PixelTester.dolSequence[i][j] + "", 85 + (54 * j) - 3, 85 + (54 * i) + 10);
				}
				if (map[i][j] != -1) {
					g2.setColor(Color.gray);
					g2.setFont(cr);
					g2.drawString(j + "" + i + "", 85 + (54 * (j + 1)) - 20, 85 + (54 * (i + 1)) - 10);
					g2.setColor(Color.black);
					g2.setFont(fn);
				}
			}
		}
		snumberOfblueDol = numberOfblueDol + " ";
		snumberOfredDol = numberOfredDol + " ";
		g2.drawString(snumberOfblueDol, 725, 130);
		g2.drawString(snumberOfredDol, 725, 580);

		p1TimeString = String.format("%.3f MILLISEC", PixelTester.p1Time/(double)(numberOfblueDol) );
		p2TimeString = String.format("%.3f MILLISEC", PixelTester.p2Time/(double)(numberOfredDol) );
		g2.drawString("수행시간 : ", 610, 168);
		g2.drawString(p1TimeString, 610, 183);
		g2.drawString("수행시간 : ", 610, 615);
		g2.drawString(p2TimeString, 610, 630);

		if ( isendb ) {
			g2.setFont(fn);
			switch(winner)
			{
				case 1:
					g2.drawString("[ Player "+winner+" Win! ]", 628, 250);
					g2.drawImage(player1img, 625, 265, 120, 80, this);  
					break;
				case 2:
					g2.drawString("[ Player "+winner+" Win! ]", 628, 250);
					g2.drawImage(player2img, 625, 265, 120, 80, this);	
					break;
				case 3:
					if( PixelTester.p1Time/(double)(numberOfblueDol) <  PixelTester.p2Time/(double)(numberOfredDol))
					{
						g2.drawString("[ Draw! \n Player 2 Draw Win! ]", 628, 250);
						g2.drawImage(player1img, 625, 265, 120, 80, this);  
					}
					else
					{
						g2.drawString("[ Draw! \n Player 1 Draw Win! ]", 628, 250);
						g2.drawImage(player2img, 625, 265, 120, 80, this);  
					}
					break;
			}
		}
	}
}