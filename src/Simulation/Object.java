package Simulation;

import java.awt.Graphics;

public class Object {
	
	Vector p, v;
	double length = 30;
	int width;
	public boolean MouseOn;
	
	public Object() {
		p = new Vector(0, 0);
		v = new Vector(0, 0);
		width = Simulation.WIDTH;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if(this.MouseOn) {
			g.drawRect((int)p.x-3, (int)p.y-3-Simulation.Y_translate, width+5, (int)length+5);
		}
	}
	
	public double force(double yi, Object ob) {
		return 0;
	}

}
