package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		// main screen
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Willagers' Revenge!!");
		
		// window inside screen
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack(); // causes window to be sized to fit preferred size + layouts of subcomponents
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();	

	}

}
