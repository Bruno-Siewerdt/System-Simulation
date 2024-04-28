package Simulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Simulation extends Canvas implements Runnable, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{
		
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 1200;
	public static int HEIGHT = 700;
	static double B = 3d;
	static double k = 3d;
	static double m = 4d;
	static double g = 9.81;
	static double v0 = 0;
	public static double SCALE = 10;
	public static int xSCALE = 10;
	public static Massa mass;
	static Mola mola;
	static Amortecedor amort;
	public static Object statc;
	public static double f = 20;
	public static boolean gravity = true;
	public static List<Object> objects;
	public boolean Pause = true;
	boolean MC = false, KC = false, BC = false, Clicked = false, Delete = true, FC = false;
	boolean started = false;
	Object O_aux;
	public static Object Change_Value_Object; 
	boolean changing_value = false;
	public static BufferedImage S_Mola, S_Amort1, S_Amort2;
	boolean decimal = false, firstDigit = false;
	double e_dec = 1, y0Moving;
	boolean movedMass;
	Object ob_Moving;
	public static boolean ForceEditing = false;
	
	int Yinit, Y_translate_init;
	public static int Y_translate;
	
	public BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public Simulation() {
		try {
			S_Mola = ImageIO.read(getClass().getResource("/Mola.png"));
			S_Amort1 = ImageIO.read(getClass().getResource("/Amort1.png"));
			S_Amort2 = ImageIO.read(getClass().getResource("/Amort2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
	}
	
	public static void main(String[] args) {
		statc = new Object();
		mass = new Massa(m, 100, 100, 0, 0, 0);
		Object[] aux = {statc, (Object)mass};
		mola = new Mola(k, 180, 0, 100, aux);
		amort = new Amortecedor(B, 120, 0, 100, aux);
		objects = new ArrayList<Object>();
		mass.objects.add(mola);
		mass.objects.add(amort);
		objects.add(mass);
		objects.add(mola);
		objects.add(amort);
		objects.add(statc);
		
		Simulation game = new Simulation();
		JFrame frame = new JFrame("Massa-Mola-Amortecedor");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		new Thread(game).start();
	}
	
	public void tick() {
		if(!Pause) {
			for(int i = 0; i < objects.size(); i++) {
				Object ob = objects.get(i);
				ob.tick();
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = layer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.drawLine(101, 0, 101, 800);
		for(int i = 0; i < objects.size(); i++) {
			Object ob = objects.get(i);
			ob.render(g);
		}
		g.setColor(Color.white);
		if(Pause) {
			g.setFont(new Font("SansSerif", Font.PLAIN, 40));
			if(MC) {
				g.fillRect(11, 250, 50, 50);
				g.setColor(Color.black);
				g.drawString("M", 20, 290);
				g.setColor(Color.white);
				g.drawString("B", 22, 350);
				g.drawString("K", 22, 410);
				g.drawString("F", 22, 470);
				g.drawString("D", 22, 230);
				g.drawRect(11, 310, 50, 50);
				g.drawRect(11, 370, 50, 50);
				g.drawRect(11, 190, 50, 50);
				g.drawRect(11, 430, 50, 50);
			} else if(BC){
				g.fillRect(11, 310, 50, 50);
				g.setColor(Color.black);
				g.drawString("B", 22, 350);
				g.setColor(Color.white);
				g.drawString("M", 20, 290);
				g.drawString("K", 22, 410);
				g.drawString("D", 22, 230);
				g.drawString("F", 22, 470);
				g.drawRect(11, 250, 50, 50);
				g.drawRect(11, 370, 50, 50);
				g.drawRect(11, 190, 50, 50);
				g.drawRect(11, 430, 50, 50);
			} else if(KC) {
				g.fillRect(11, 370, 50, 50);
				g.setColor(Color.black);
				g.drawString("K", 22, 410);
				g.setColor(Color.white);
				g.drawString("B", 22, 350);
				g.drawString("M", 20, 290);
				g.drawString("F", 22, 470);
				g.drawString("D", 22, 230);
				g.drawRect(11, 310, 50, 50);
				g.drawRect(11, 250, 50, 50);
				g.drawRect(11, 190, 50, 50);
				g.drawRect(11, 430, 50, 50);
			} else if(Delete) {
				g.fillRect(11, 190, 50, 50);
				g.setColor(Color.black);
				g.drawString("D", 22, 230);
				g.setColor(Color.white);
				g.drawString("K", 22, 410);
				g.drawString("B", 22, 350);
				g.drawString("M", 20, 290);
				g.drawString("F", 22, 470);
				g.drawRect(11, 310, 50, 50);
				g.drawRect(11, 250, 50, 50);
				g.drawRect(11, 370, 50, 50);
				g.drawRect(11, 430, 50, 50);
			} else if(FC) {
				g.fillRect(11, 430, 50, 50);
				g.setColor(Color.black);
				g.drawString("F", 22, 470);
				g.setColor(Color.white);
				g.drawString("D", 22, 230);
				g.drawString("K", 22, 410);
				g.drawString("B", 22, 350);
				g.drawString("M", 20, 290);
				g.drawRect(11, 190, 50, 50);
				g.drawRect(11, 310, 50, 50);
				g.drawRect(11, 250, 50, 50);
				g.drawRect(11, 370, 50, 50);
			} else {
				g.drawString("D", 22, 230);
				g.drawString("M", 20, 290);
				g.drawString("B", 22, 350);
				g.drawString("K", 22, 410);
				g.drawString("F", 22, 470);
				g.drawRect(11, 190, 50, 50);
				g.drawRect(11, 250, 50, 50);
				g.drawRect(11, 310, 50, 50);
				g.drawRect(11, 370, 50, 50);
				g.drawRect(11, 430, 50, 50);
			}
		}
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}
	
	double round(double x) {
		return Math.round(x*100)/100;
	}
	
	@Override
	public void run() {
		requestFocus();
		while(true) {
			render();
			tick();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void restart() {
		for(int i = 0; i < objects.size(); i++) {
			Object ob = objects.get(i);
			if(ob instanceof Massa) {
				ob.p.x = 0;
				ob.p.y = 0;
				((Massa)ob).v.y = 0;
				((Massa)ob).dots.clear();
			} else if(ob instanceof Mola) {
				ob.p.x = ((Mola)ob).pi0.x;
				ob.p.y = ((Mola)ob).pi0.y;
				((Mola)ob).pf.x = ((Mola)ob).pf0.x;
				((Mola)ob).pf.y = ((Mola)ob).pf0.y;
				ob.length = ((Mola)ob).pf0.y - ((Mola)ob).pi0.y;
			} else if(ob instanceof Amortecedor) {
				ob.p.x = ((Amortecedor)ob).pi0.x;
				ob.p.y = ((Amortecedor)ob).pi0.y;
				((Amortecedor)ob).pf.x = ((Amortecedor)ob).pf0.x;
				((Amortecedor)ob).pf.y = ((Amortecedor)ob).pf0.y;
				ob.length = ((Amortecedor)ob).pf0.y - ((Amortecedor)ob).pi0.y;
			}
		}
	}

	public void CheckLigs() {
		for(int i = 0; i < objects.size(); i++) {
			Object ob = objects.get(i);
			if(ob instanceof Massa) {
				for(int c = 0; c < ((Massa)ob).objects.size(); c++) {
					if(objects.indexOf(((Massa)ob).objects.get(c)) < 0) {
						((Massa)ob).objects.remove(c);
					}
				}
			}
		}
	}
	
	public void CreateMola(Object ob1, Object ob2) {
		if(ob2 instanceof Massa) {
			if(ob1 instanceof Massa) {
				Object obs[] = {null, null};
				Mola m;
				if(((Massa) ob1).xy0.y < ((Massa)ob2).xy0.y) {
					obs[0] = (Massa)objects.get(objects.indexOf(ob1));
					obs[1] = ob2;
					m = new Mola(1, 180, ((Massa)ob1).xy0.y+120, ((Massa)ob2).xy0.y, obs);
				} else {
					obs[1] = objects.get(objects.indexOf(ob1));
					obs[0] = ob2;
					m = new Mola(1, 180, ((Massa)ob2).xy0.y+120, ((Massa)ob1).xy0.y, obs);
				}
				Massa ma = (Massa)ob2;
				Massa ma2 = (Massa)objects.get(objects.indexOf(ob1));
				ma.objects.add(m);
				ma2.objects.add(m);
				objects.add(m);
			} else {
				Object[] obs = {ob1, ob2};
				Mola m = new Mola(1, 180, 0, ((Massa)ob2).xy0.y, obs);
				Massa ma = (Massa)ob2;
				ma.objects.add(m);
				objects.add(m);
			}
		} else if(!(ob2 instanceof Mola) && !(ob2 instanceof Amortecedor)) {
			Object[] obs = {ob2, ob1};
			Mola m = new Mola(1, 180, 0, ((Massa)ob1).xy0.y, obs);
			Massa ma = (Massa)ob1;
			ma.objects.add(m);
			objects.add(m);
		}
	}
	
	public void CreateAmort(Object ob1, Object ob2) {
		if(ob2 instanceof Massa) {
			if(ob1 instanceof Massa) {
				Object obs[] = {null, null};
				Amortecedor m;
				if(((Massa) ob1).xy0.y < ((Massa)ob2).xy0.y) {
					obs[0] = objects.get(objects.indexOf(ob1));
					obs[1] = ob2;
					m = new Amortecedor(1, 120, ((Massa)ob1).xy0.y+120, ((Massa)ob2).xy0.y, obs);
				} else {
					obs[1] = objects.get(objects.indexOf(ob1));
					obs[0] = ob2;
					m = new Amortecedor(1, 120, ((Massa)ob2).xy0.y+120, ((Massa)ob1).xy0.y, obs);
				}
				Massa ma = (Massa)ob2;
				Massa ma2 = (Massa)objects.get(objects.indexOf(ob1));
				ma.objects.add(m);
				ma2.objects.add(m);
				objects.add(m);
			} else {
				Object[] obs = {ob1, ob2};
				Amortecedor m = new Amortecedor(1, 120, 0, ((Massa)ob2).xy0.y, obs);
				Massa ma = (Massa)ob2;
				ma.objects.add(m);
				objects.add(m);
			}
		} else if(!(ob2 instanceof Mola) && !(ob2 instanceof Amortecedor)) {
			Object[] obs = {ob2, ob1};
			Amortecedor m = new Amortecedor(1, 120, 0, ((Massa)ob1).xy0.y, obs);
			Massa ma = (Massa)ob1;
			ma.objects.add(m);
			objects.add(m);
		}
	}
	
	public double Module(double x) {
		if(x < 0) {
			return -x;
		}
		return x;
	}
	
	public void CheckLengths() {
		for(int i = 0; i < objects.size(); i++) {
			Object ob = objects.get(i);
			if(ob instanceof Mola) {
				if(((Mola)ob).objects[0] instanceof Massa) {
					((Mola)ob).pi0.y = ((Massa)(((Mola)ob).objects[0])).xy0.y+120;
					((Mola)ob).pf0.y = ((Massa)(((Mola)ob).objects[1])).xy0.y;
					((Mola)ob).p.y = ((Massa)(((Mola)ob).objects[0])).xy0.y+120;
					((Mola)ob).pf.y = ((Massa)(((Mola)ob).objects[1])).xy0.y;
				} else {
					((Mola)ob).pf0.y = ((Massa)(((Mola)ob).objects[1])).xy0.y;
					((Mola)ob).pf.y = ((Massa)(((Mola)ob).objects[1])).xy0.y;
				}
				((Mola)ob).length = ((Mola)ob).pf0.y - ((Mola)ob).pi0.y ;
				((Mola)ob).l0 = ((Mola)ob).length;
			} else if(ob instanceof Amortecedor) {
				if(((Amortecedor)ob).objects[0] instanceof Massa) {
					((Amortecedor)ob).pi0.y = ((Massa)(((Amortecedor)ob).objects[0])).xy0.y+120;
					((Amortecedor)ob).pf0.y = ((Massa)(((Amortecedor)ob).objects[1])).xy0.y;
					((Amortecedor)ob).p.y = ((Massa)(((Amortecedor)ob).objects[0])).xy0.y+120;
					((Amortecedor)ob).pf.y = ((Massa)(((Amortecedor)ob).objects[1])).xy0.y;
				} else {
					((Amortecedor)ob).pf0.y = ((Massa)(((Amortecedor)ob).objects[1])).xy0.y;
					((Amortecedor)ob).pf.y = ((Massa)(((Amortecedor)ob).objects[1])).xy0.y;
				}
				((Amortecedor)ob).length = ((Amortecedor)ob).pf0.y - ((Amortecedor)ob).pi0.y;
				((Amortecedor)ob).l_ant = ((Amortecedor)ob).length;
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(Module(e.getY() - Yinit) > 5) {
			if(Change_Value_Object instanceof Massa) {
				((Massa)Change_Value_Object).ticks = 0;
			}
			changing_value = false;
			Change_Value_Object = null;
			firstDigit = false;
			started = false;
		}
		if(movedMass) {
			if(!KC && !BC && !FC) {
				if(y0Moving + e.getY()-Yinit >= 100) {
					((Massa)ob_Moving).xy0.y = y0Moving + e.getY()-Yinit;
					CheckLengths();
				} else {
					((Massa)ob_Moving).xy0.y = 100;
				}
			}
			for(int i = 0; i < objects.size(); i++) {
				Object ob = objects.get(i);
				int x = e.getX();
				int y = e.getY();
				if(ob instanceof Massa) {
					if(x >= ((Massa)ob).xy0.x+(ob.p.x*Simulation.xSCALE) && x <= ((Massa)ob).xy0.x+(ob.p.x*Simulation.xSCALE)+ob.width && y >= ((Massa)ob).xy0.y+(ob.p.y*Simulation.SCALE)-Simulation.Y_translate && y <= ((Massa)ob).xy0.y+(ob.p.y*Simulation.SCALE)+ob.length-Simulation.Y_translate) {
						ob.MouseOn = true;
					}
				} else {
					if(x >= ob.p.x && x <= ob.p.x+ob.width && y >= ob.p.y-Simulation.Y_translate && y <= ob.p.y+ob.length-Simulation.Y_translate) {
						ob.MouseOn = true;
					}
				}
			}
		} else {
			if(Y_translate_init - e.getY()+Yinit >= 0) {
				Y_translate = Y_translate_init - e.getY()+Yinit;
			} else {
				Y_translate = 0;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(Pause) {
			for(int i = 0; i < objects.size(); i++) {
				Object ob = objects.get(i);
				ob.MouseOn = false;
				int x = e.getX();
				int y = e.getY();
				if(ob instanceof Massa) {
					if(x >= ((Massa)ob).xy0.x+(ob.p.x*Simulation.xSCALE) && x <= ((Massa)ob).xy0.x+(ob.p.x*Simulation.xSCALE)+ob.width && y >= ((Massa)ob).xy0.y+(ob.p.y*Simulation.SCALE)-Simulation.Y_translate && y <= ((Massa)ob).xy0.y+(ob.p.y*Simulation.SCALE)+ob.length-Simulation.Y_translate) {
						ob.MouseOn = true;
					}
				} else {
					if(x >= ob.p.x && x <= ob.p.x+ob.width && y >= ob.p.y-Simulation.Y_translate && y <= ob.p.y+ob.length-Simulation.Y_translate) {
						ob.MouseOn = true;
					}
				}
			}
			if(!Clicked) {
				MC = false;
				BC = false;
				KC = false;
				Delete = false;
				FC = false;
				if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 250 && e.getY() <= 300) {
					MC = true;
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 310 && e.getY() <= 360) {
					BC = true;
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 370 && e.getY() <= 420) {
					KC = true;
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 190 && e.getY() <= 240) {
					Delete = true;
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 430 && e.getY() <= 480) {
					FC = true;
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Yinit = e.getY();
		Y_translate_init = Y_translate;
		if(Pause) {
			movedMass = false;
			for(int i = 0; i < objects.size(); i++) {
				if(objects.get(i).MouseOn && Pause && objects.get(i) instanceof Massa) {
					movedMass = true;
					ob_Moving = objects.get(i);
					y0Moving = ((Massa)objects.get(i)).xy0.y;
				}
			}
			if(e.getX() <= 100) {
				started = false;
				if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 250 && e.getY() <= 300) {
					if(MC && Clicked) {
						Clicked = false;
					} else {
						Clicked = true;
						Delete = false;
						MC = true;
						KC = false;
						BC = false;
						FC = false;
					}
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 310 && e.getY() <= 360) {
					if(BC && Clicked) {
						Clicked = false;
					} else {
						Clicked = true;
						BC = true;
						Delete = false;
						KC = false;
						MC = false;
						FC = false;
					}
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 370 && e.getY() <= 420) {
					if(KC && Clicked) {
						Clicked = false;
					} else {
						Clicked = true;
						KC = true;
						Delete = false;
						MC = false;
						BC = false;
						FC = false;
					}
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 190 && e.getY() <= 240) {
					if(Delete && Clicked) {
						Clicked = false;
					} else {
						Clicked = true;
						KC = false;
						MC = false;
						BC = false;
						Delete = true;
						FC = false;
					}
				} else if(e.getX() >= 11 && e.getX() <= 61 && e.getY() >= 430 && e.getY() <= 480) {
					if(FC && Clicked) {
						Clicked = false;
					} else {
						Clicked = true;
						KC = false;
						MC = false;
						BC = false;
						Delete = false;
						FC = true;
					}
				} else {
					KC = false;
					MC = false;
					BC = false;
					Delete = false;
					FC = false;
				}
			}
			if(e.getX() > 100) {
				if(MC) {
					started = false;
					boolean canCreate = true;
					if(e.getY()+Y_translate < 160) {
						canCreate = false;
					}
					for(int i = 0; i < objects.size(); i++) {
						Object ob = objects.get(i);
						if(ob instanceof Massa && e.getY()+Y_translate >= ((Massa)ob).xy0.y-60 && e.getY()+Y_translate <= ((Massa)ob).xy0.y+180) {
							canCreate = false;
						}
					}
					if(canCreate) {
						Massa m = new Massa(1d, 100, e.getY()+Y_translate-60, 0, 0, 0);
						objects.add(m);
					}
				} else if(KC) {
					if(!started) {
						for(int i = 0; i < objects.size(); i++) {
							Object ob = objects.get(i);
							if(!(ob instanceof Mola) && !(ob instanceof Amortecedor) && ob.MouseOn) {
								started = true;
								O_aux = ob;
							}
						}
					} else {
						for(int i = 0; i < objects.size(); i++) {
							Object ob = objects.get(i);
							if(ob.MouseOn && ob != O_aux) {
								CreateMola(O_aux, ob);
								i = objects.size();
							}
						}
						started = false;
					}
				} else if(BC) {
					if(!started) {
						for(int i = 0; i < objects.size(); i++) {
							Object ob = objects.get(i);
							if(!(ob instanceof Mola) && !(ob instanceof Amortecedor) && ob.MouseOn) {
								started = true;
								O_aux = ob;
							}
						}
					} else {
						for(int i = 0; i < objects.size(); i++) {
							Object ob = objects.get(i);
							if(ob.MouseOn && ob != O_aux) {
								CreateAmort(O_aux, ob);
								i = objects.size();
							}
						}
						started = false;
					}
				} else if(Delete) {
					for(int i = 0; i < objects.size(); i++) {
						Object ob = objects.get(i);
						if(ob.MouseOn && ob instanceof Massa) {
							objects.remove(ob);
							for(int c = 0; c < ((Massa)ob).objects.size(); c++) {
								objects.remove(((Massa)ob).objects.get(c));
							}
							CheckLigs();
						} else if(ob.MouseOn && (ob instanceof Mola || ob instanceof Amortecedor)) {
							objects.remove(ob);
							CheckLigs();
						}
					}
					started = false;
				} else {
					started = false;
					for(int i = 0; i < objects.size(); i++) {
						Object ob = objects.get(i);
						if(ob.MouseOn && (ob instanceof Mola || ob instanceof Massa || ob instanceof Amortecedor)) {
							if(e.getButton() == MouseEvent.BUTTON1) {
								ForceEditing = false;
							} else if(e.getButton() == MouseEvent.BUTTON3 && ob instanceof Massa) {
								ForceEditing = true;
							}
							Change_Value_Object = ob;
							firstDigit = true;
							changing_value = true;
							i = objects.size();
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(Module(e.getY() - Yinit) > 5) {
			changing_value = false;
			if(Change_Value_Object instanceof Massa) {
				((Massa)Change_Value_Object).ticks = 0;
			}
			Change_Value_Object = null;
			firstDigit = false;
			
			if(movedMass) {
				if(FC) {
					int value = e.getY() - Yinit;
					double force;
					if(value < 0) {
						force = -1d;
					} else {
						force = 1d;
					}
					Massa m = (Massa)ob_Moving;
					m.forces += force;
				} else if(KC) {
					for(int i = 0; i < objects.size(); i++) {
						Object ob = objects.get(i);
						if(ob.MouseOn && !(ob instanceof Mola) && !(ob instanceof Amortecedor) && ob != ob_Moving) {
							CreateMola(ob_Moving, ob);
						}
					}
					started = false;
				} else if(BC) {
					for(int i = 0; i < objects.size(); i++) {
						Object ob = objects.get(i);
						if(ob.MouseOn && !(ob instanceof Mola) && !(ob instanceof Amortecedor) && ob != ob_Moving) {
							CreateAmort(ob_Moving, ob);
						}
					}
					started = false;
				} else {
					CheckLengths();
				}
				movedMass = false;
			}
		}
			
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(changing_value && e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
			int num = e.getKeyCode()-48;
			if(Change_Value_Object instanceof Massa) {
				if(ForceEditing) {
					if(((Massa)Change_Value_Object).forces >= 0) {
						if(firstDigit) {
							((Massa)Change_Value_Object).forces = (double)num;
							firstDigit = false;
						} else {
							if(decimal) {
								((Massa)Change_Value_Object).forces += e_dec*(double)num;
								e_dec = e_dec/10;
							} else {
								((Massa)Change_Value_Object).forces *= 10;
								((Massa)Change_Value_Object).forces += (double)num;
							}
						}
					} else {
						if(firstDigit) {
							((Massa)Change_Value_Object).forces = -(double)num;
							firstDigit = false;
						} else {
							if(decimal) {
								((Massa)Change_Value_Object).forces -= e_dec*(double)num;
								e_dec = e_dec/10;
							} else {
								((Massa)Change_Value_Object).forces *= 10;
								((Massa)Change_Value_Object).forces -= (double)num;
							}
						}
					}
				} else {
					if(firstDigit) {
						((Massa)Change_Value_Object).m = num;
						firstDigit = false;
					} else {
						if(decimal) {
							((Massa)Change_Value_Object).m += e_dec*num;
							e_dec = e_dec/10;
						} else {
							((Massa)Change_Value_Object).m *= 10;
							((Massa)Change_Value_Object).m += num;
						}
					}
				}
			} else if(Change_Value_Object instanceof Mola) {
				if(firstDigit) {
					((Mola)Change_Value_Object).k = num;
					firstDigit = false;
				} else {
					if(decimal) {
						((Mola)Change_Value_Object).k += e_dec*num;
						e_dec = e_dec/10;
					} else {
						((Mola)Change_Value_Object).k *= 10;
						((Mola)Change_Value_Object).k += num;
					}
				}
			} else if(Change_Value_Object instanceof Amortecedor) {
				if(firstDigit) {
					((Amortecedor)Change_Value_Object).B = num;
					firstDigit = false;
				} else {
					if(decimal) {
						((Amortecedor)Change_Value_Object).B += e_dec*num;
						e_dec = e_dec/10;
					} else {
						((Amortecedor)Change_Value_Object).B *= 10;
						((Amortecedor)Change_Value_Object).B += num;
					}
				}
			}
		}
		if(changing_value && e.getKeyCode() == 46) {
			if(!firstDigit) {
				decimal = true;
				e_dec = 0.1;
			}
		} else if(changing_value && e.getKeyCode() == KeyEvent.VK_ENTER) {
			changing_value = false;
			Change_Value_Object = null;
			e_dec = 1;
			decimal = false;
		} else if(changing_value && e.getKeyCode() == KeyEvent.VK_BACK_SPACE)  {
			if(Change_Value_Object instanceof Massa) {
				if(ForceEditing) {
					if(decimal) {
						if(e_dec < 0.1) {
							e_dec *= 10;
							((Massa)Change_Value_Object).forces = Math.floor((((Massa)Change_Value_Object).forces/e_dec)/10);
							((Massa)Change_Value_Object).forces *= e_dec*10;
							if(e_dec == 0.1) {
								decimal = false;
							}
						}
					} else {
						((Massa)Change_Value_Object).forces = Math.floor(((Massa)Change_Value_Object).forces/10);
					}
				} else {
					if(decimal) {
						if(e_dec < 0.1) {
							e_dec *= 10;
							((Massa)Change_Value_Object).m = Math.floor((((Massa)Change_Value_Object).m/e_dec)/10);
							((Massa)Change_Value_Object).m *= e_dec*10;
							if(e_dec == 0.1) {
								decimal = false;
							}
						}
					} else {
						((Massa)Change_Value_Object).m = Math.floor(((Massa)Change_Value_Object).m/10);
					}
				}
			} else if(Change_Value_Object instanceof Mola) {
				if(decimal) {
					if(e_dec < 0.1) {
						e_dec *= 10;
						((Mola)Change_Value_Object).k = Math.floor((((Mola)Change_Value_Object).k/e_dec)/10);
						((Mola)Change_Value_Object).k *= e_dec*10;
						if(e_dec == 0.1) {
							decimal = false;
						}
					}
				} else {
					((Mola)Change_Value_Object).k = Math.floor(((Mola)Change_Value_Object).k/10);
				}
			} else if(Change_Value_Object instanceof Amortecedor) {
				if(decimal) {
					if(e_dec < 0.1) {
						e_dec *= 10;
						((Amortecedor)Change_Value_Object).B = Math.floor((((Amortecedor)Change_Value_Object).B/e_dec)/10);
						((Amortecedor)Change_Value_Object).B *= e_dec*10;
						if(e_dec == 0.1) {
							decimal = false;
						}
					}
				} else {
					((Amortecedor)Change_Value_Object).B = Math.floor(((Amortecedor)Change_Value_Object).B/10);
				}
			}
		}
		if(changing_value && e.getKeyCode() == KeyEvent.VK_T && ForceEditing) {
			((Massa)Change_Value_Object).forces *= -1;
		} else if(e.getKeyCode() == KeyEvent.VK_R) {
			restart();
			Pause = true;
		} else if(e.getKeyCode() == KeyEvent.VK_P) {
			Pause = !Pause;
			if(Pause == false) {
				for(int i = 0; i < objects.size(); i++) {
					objects.get(i).MouseOn = false;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}
}
