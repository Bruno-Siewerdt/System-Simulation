package Simulation;

import java.awt.Graphics;

public class Dots {
	
	Vector xy;

	public Dots(Vector xy) {
		this.xy = xy;
	}
	
	public void render(Graphics g) {
		g.fillRect((int)xy.x, (int)(xy.y)-Simulation.Y_translate, 1, 1);
	}
}
