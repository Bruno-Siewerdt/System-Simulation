package Simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Mola extends Object{

	public Vector pf, pf0, pi0;
	double k;
	public double l0;
	Object[] objects = {null, null};
	
	public Mola(double k, double x, double y, double yf, Object[] lig) {
		p = new Vector(x, y);
		pf = new Vector(x+20, yf);
		pf0 = new Vector(x+20, yf);
		pi0 = new Vector(x, y);
		length = yf-y;
		l0 = length;
		this.k = k;
		objects[0] = lig[0];
		objects[1] = lig[1];
		width = 20;
	}
	
	public void tick() {
		if(p.x <= Simulation.WIDTH-320+pi0.x) {
			p.x += 1;
		}
	}
	
	double round(double x) {
		return (double)(Math.round(x*100))/100d;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawLine((int)p.x+9, (int)p.y-Simulation.Y_translate, (int)p.x+9, (int)p.y-Simulation.Y_translate+10);
		g.drawImage(Simulation.S_Mola, (int)(p.x), (int)(p.y)-Simulation.Y_translate+10, 20, (int)(length)-20, null);
		g.drawLine((int)p.x+9, (int)pf.y-Simulation.Y_translate-10, (int)p.x+9, (int)pf.y-Simulation.Y_translate);
		if(this.MouseOn) {
			g.drawRect((int)p.x-3, (int)p.y-3-Simulation.Y_translate, width+5, (int)length+5);
		}
		if(Simulation.Change_Value_Object == this) {
			g.fillRect((int)this.p.x+25, (int)this.p.y+40-Simulation.Y_translate, (int)((Math.floor(Math.log10(k))*18))+115, 40);
			g.setColor(Color.black);
		}
		g.setFont(new Font("SansSerif", Font.PLAIN, 30));
		g.drawString(round(k)+"N/m", (int)this.p.x+25, (int)this.p.y+70-Simulation.Y_translate);
	}
	
	public double force(double yi, Object ob) {
		if(ob instanceof Massa) {
			if(ob == objects[1]) {
				pf.y = pf0.y + yi*Simulation.SCALE;
			} else if(ob == objects[0]) {
				p.y = pi0.y + yi*Simulation.SCALE;
			}
			length = pf.y-p.y;
			if(ob == objects[1]) {
				return -k*(length-l0)/Simulation.SCALE;
			} else if(ob == objects[0]) {
				return k*(length-l0)/Simulation.SCALE;
			}
		}
		return 0;
	}
}
