package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		// JFrame is the frame/window in which the game runs
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// closes the window properly when user clicks "x" button
		window.setResizable(false);
		// so we cannot resize the window
		window.setTitle("2D Adventure");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);//GamePanel is JPanel with extra functions
		
		window.pack(); //causes the window to be sized to fit the preferred size and layouts of its subcomponents(gamePanel)
		
		window.setLocationRelativeTo(null);
		//window is at center of screen
		window.setVisible(true);
		
		gamePanel.setupGame();
		gamePanel.startGameThread();
	}

}
