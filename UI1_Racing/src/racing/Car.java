package racing;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Car extends JComponent {
		
	private static final long serialVersionUID = -8147987130820080891L;
	private double x = 0,y = 0;
	private float angle = 0;
	private double speed = 0.0;
	
	public Car() {
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void move() {
		double newX = x + Math.sin(angle) * speed;
		double newY = y - Math.cos(angle) * speed;
		move(newX, newY);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		/*
		 *  Verschieben des Zeichenrasters um x und y
		 *  drehen der Fl�che um den Winkle angle
		 */
		g2d.translate(x, y);
		g2d.rotate(angle);
		
		// Beispielzeichnung ersetzten durch ihre eigene
		 Image carAvatar = Toolkit.getDefaultToolkit().getImage(Car.class
	               .getResource("/images/buick_topDown.png"));
		    g2d.drawImage(carAvatar, -35, -35, 70, 90, this);
		    g2d.finalize();
		
	}
	
	private int doubleToInt(double doubleValue) {
		int baseInt = (int)doubleValue;
		if(doubleValue - baseInt >= 0.5) {
			return baseInt+1;
		} else {
			return baseInt;
		}
	}

	public void accelerate() {
		speed += 0.1;
	}

	public void slowdown() {
		speed -= 0.2;
		if(speed < 0) {
			speed = 0;
		}
	}

	public void stop() {
		speed = 0;
	}

	public void turnRight() {
		angle += Math.PI / 40;
	}

	public void turnLeft() {
		angle -= Math.PI / 40;
	}

	public int getX() {
		return doubleToInt(x);
	}

	public int getY() {
		return doubleToInt(y);
	}

	public float getAngle() {
		return angle;
	}

	public double getSpeed() {
		return speed;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void setX(double newposX) {
		x = newposX;
	}

	public void  setY(double newposY) {
		y = newposY;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Racing");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Car c = new Car();
		c.move(50, 50);
		f.add(c);
		f.setSize(200, 240);
		f.setVisible(true);

	}

}
