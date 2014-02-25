package racing;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Racing extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1169331112688629681L;
	private Car raceCar;
	private Human driver;
	private Human target;
	private int[] keys;
	private Dimension screenSize;
	private Timer timer;
	private BufferedImage background;
	private BufferedImage raceTrack;

	public Racing() {
		init();
	}

	private void init() {
		screenSize = new Dimension(1920, 1200);
		setPreferredSize(screenSize);

		raceCar = new Car();
		raceCar.move(300, 300);
		
		driver = new Human();
		driver.move(-35, -35);
		
		
		target = new Human();
		target.move(50, 50);

		registerKeyListener();
		timer = new Timer(15, this);
		timer.start();
		//loadTarget();
		loadTrack();
	}
	
	private void loadTarget() {
	
		System.out.println("Creating new human");	
		Human h = new Human();
		h.move(50, 50);
		add(h);
	}
	
	private void loadTrack() {
		Paint texture = null;
		try {
			raceTrack = ImageIO.read(Car.class
		               .getResource("/images/racetrack.png"));
			texture = new TexturePaint(ImageIO.read(Car.class
		               .getResource("/images/rocks.jpg")),new Rectangle(512, 512));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		background = new BufferedImage(screenSize.width, screenSize.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = background.createGraphics();
		g2d.setPaint(texture);
		g2d.fillRect(0, 0, screenSize.width, screenSize.height);

		g2d.drawImage(raceTrack, 0, 0, null);

	}

	private void registerKeyListener() {
		keys = new int[256];
		Arrays.fill(keys, 0); // 0 = key is up

		KeyboardFocusManager kfm = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				switch (e.getID()) {
				case KeyEvent.KEY_PRESSED:
					keys[e.getKeyCode()] = 1;
					break;
				case KeyEvent.KEY_RELEASED:
					keys[e.getKeyCode()] = 0;
					break;
				default:
					break;
				}
				return false;
			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, 0, 0, this);
		raceCar.paintComponent(g2d);
		driver.paintComponent(g2d);
	}
	
	public void paintHuman(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, 0, 0, this);
		target.paintComponent(g2d);
	}

	private void moveCar() {
		keyProcessing();
		raceCar.move();
		driver.move();
		controlRaceCarPosition();
	}

	private void controlRaceCarPosition() {
		
		//Moves car to other side of screen.
		int x = raceCar.getX(), y = raceCar.getY();
		if (x>getWidth()+5) {
			raceCar.setX(0);	
		}
		else if (x<-5) {
			raceCar.setX(getWidth());
		}
		if (y>getHeight()+5) {
			raceCar.setY(0);
		}
		else if (y<-5) {
			raceCar.setY(getHeight());
		}
		
		int color = raceTrack.getRGB(x, y);
		if (color > 0) {
			// stop on white
			raceCar.stop();
		} else if (color < 0 && color > 16000000) {
			// slowdown on gray
			raceCar.slowdown();
		}
	}

	private void keyProcessing() {

		if (keys[KeyEvent.VK_UP] == 1) {
			raceCar.accelerate();
		}
		if (keys[KeyEvent.VK_DOWN] == 1) {
			raceCar.slowdown();
		}
		if (keys[KeyEvent.VK_RIGHT] == 1) {
			raceCar.turnRight();
		}
		if (keys[KeyEvent.VK_LEFT] == 1) {
			raceCar.turnLeft();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		moveCar();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		JFrame f = new JFrame("Racing");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new Racing());
		f.pack();
		f.setVisible(true);
	}

}
