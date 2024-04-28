package Simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Massa extends Object{
	
	public Vector v, a, xy0;
	double time = 0;
	double lastY = -1;
	boolean statc = false;
	public List<Object> objects;
	double m;
	int ticks = 0;
	public List<Dots> dots;
	double forces;
	
	public Massa(double massa, double x, double y, double v0x, double v0y, double Forces) {
		p = new Vector(0, 0);
		v = new Vector(v0x, v0y);
		a = new Vector(0, 0);
		xy0 = new Vector(x, y);
		m = massa;
		objects = new ArrayList<Object>();
		dots = new ArrayList<Dots>();
		this.forces = Forces;
		width = 120;
		length = 120;
	}
	
	public void tick() {
		a.y = 0;
		if(Simulation.gravity) {
			a.y += Simulation.g;
		}
		a.y += forces/m;
		for(int i = 0; i < objects.size(); i++) {
			Object ob = objects.get(i);
			a.y += ob.force(p.y, this)/m;
		}
		
		v.y += a.y/Simulation.f;
		p.y += v.y/Simulation.f;
		
		if(p.x*Simulation.xSCALE <= Simulation.WIDTH-200-length) {
			p.x += 0.1;
			dots.add(new Dots(new Vector(p.x*Simulation.xSCALE + xy0.x, p.y*Simulation.SCALE + xy0.y+length/2)));
		}
	}
	
	double round(double x) {
		return (double)(Math.round(x*100))/100d;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)(xy0.x+(p.x*Simulation.xSCALE)), (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate, width, (int)length);
		if(dots.size() >= 2) {
			Dots d1 = dots.get(0);
			d1.render(g);
			for(int i = 1; i < dots.size(); i++) {
				Dots d2 = dots.get(i);
				d2.render(g);
				g.drawLine((int)d1.xy.x, (int)(d1.xy.y)-Simulation.Y_translate, (int)d2.xy.x, (int)(d2.xy.y)-Simulation.Y_translate);
				d1 = d2;
			}
		}
		if(this.MouseOn) {
			g.drawRect((int)(xy0.x+(p.x*Simulation.xSCALE))-3, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-3, width+5, (int)length+5);
		}
		g.setFont(new Font("SansSerif", Font.PLAIN, 15));
		if(forces < 0) {
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-20);
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-20, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2+5, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-15);
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-20, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2-5, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-15);
			if(Simulation.Change_Value_Object == this && Simulation.ForceEditing) {
				g.fillRect((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2 - 20 - (int)((Math.floor(Math.log10(-forces))*4)), (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-40, (int)((Math.floor(Math.log10(-forces))*8)) + 48, 20);
				g.setColor(Color.black);
			}
			g.drawString(-round(forces) + "N", (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2 - 15 -(int)((Math.floor(Math.log10(-forces))*4)), (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate-25);
		} else if(forces > 0) {
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate + (int)length, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate+20 + (int)length);
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate+20 + (int)length, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2+5, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate+ 15 + (int)length);
			g.drawLine((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate+20 + (int)length, (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2-5, (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate + 15 + (int)length);
			if(Simulation.Change_Value_Object == this && Simulation.ForceEditing) {
				g.fillRect((int)(xy0.x+(p.x*Simulation.xSCALE))+width/2 - 20 - (int)((Math.floor(Math.log10(forces))*4)), (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate + 20 + (int)length, (int)((Math.floor(Math.log10(forces))*8)) + 48, 20);
				g.setColor(Color.black);
			}
			g.drawString(round(forces) + "N", (int)(xy0.x+(p.x*Simulation.xSCALE))+width/2 - 15 -(int)((Math.floor(Math.log10(forces))*4)), (int)(xy0.y+(p.y*Simulation.SCALE))-Simulation.Y_translate+35 + (int)length);
		}
		g.setColor(Color.black);
		if(Simulation.Change_Value_Object == this && !Simulation.ForceEditing) {
			if(ticks < 10) {
				ticks++;
			} else {
				g.fillRect((int)(this.xy0.x + this.p.x*Simulation.SCALE)+15-(int)((Math.floor(Math.log10(m))*9))-5, (int)(this.xy0.y + this.p.y*Simulation.SCALE)+40 -Simulation.Y_translate, (int)((Math.floor(Math.log10(m))*18))+100, 40);
				g.setColor(Color.white);
			}
		}
		g.setFont(new Font("SansSerif", Font.PLAIN, 30));
		g.drawString(round(m)+"kg", (int)(this.xy0.x + this.p.x*Simulation.SCALE)+15-(int)((Math.floor(Math.log10(m))*9)), (int)(this.xy0.y + this.p.y*Simulation.SCALE)+70 -Simulation.Y_translate);
	}
}
