/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcaglari;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


@SuppressWarnings("...")//birden fazla uyarı aynı anda olmasın diye
public class MainFrame extends JFrame implements MouseListener {
	int x; 
	int y; 
	int koordinatx; 
	int koordinaty; 
	int tempx;
	int tempy;
	int flag = 1; 
	int pcfirst = 1; 
	int regretcount = 0; 
	int backgammoncount = 0; 
	boolean Iskazanan = false;
	boolean Isfirst = true;
	boolean Issecond = false;
	boolean Clickfirst = false; 
	int SelectResult[] = new int[4];
	int menubutton[] = new int[4];
	Hareket store = new Hareket();
	
	ServerSocket server = null;
	Socket socket = null;
	Socket client = null;
	String IP = null;
	DataInputStream din = null;
	DataOutputStream dout = null;
	boolean isStart = false; // Online oyun
	boolean isYourTurn = false; // Oyun sırası 
	boolean isClient = false; // Client bağlı mı?
	boolean isServer = false; // Server bağlı mı?
	boolean swapServer;

	int[][] backgammon = new int[15][15];
	Hakem hakem = new Hakem();
	İnsanPc pc = new İnsanPc();
	Tahta tahta = new Tahta();
    //Object pp;
    private Object pp;

	
	@SuppressWarnings("....") //çerçeve 
	public MainFrame() {
		this.setSize(900, 700);
		this.setTitle("Oyun");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width = Toolkit.getDefaultToolkit().getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getDefaultToolkit().getScreenSize().height;
		this.setLocation((width - 900) / 2, (height - 700) / 2);
		this.addMouseListener(this);
	}

	
	public void paint(Graphics g) { //çizimler

		BufferedImage background = null;
		BufferedImage qipan = null;
		BufferedImage blackbackgammon = null;
		BufferedImage whitebackgammon = null;
		try {
			background = ImageIO.read(new File("arkaplan.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(background, 0, 0, this);

		
		try {
			qipan = ImageIO.read(new File("arkaplan.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			whitebackgammon = ImageIO.read(new File("beyaz.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			blackbackgammon = ImageIO.read(new File("siyah.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(qipan, 0, 10, this);
		
		tahta.setqipantbase(this);
		
		for (int i = 1; i <= 15; i++) {
			for (int j = 1; j <= 15; j++) {
				g.drawLine(17 + 40 * i, 40, 17 + 40 * i, 678);
				g.drawLine(17, 40 + j * 40, 658, 40 + j * 40);
			}
		}
		
		if (SelectResult[3] == 1) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					backgammon[i][j] = 0;
					SelectResult[3]--;
				}
			}
		}
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (backgammon[i][j] == 1) { 
					int m = (i + 1) * 40 + 16;
					int n = (j + 1) * 40 + 40;
					g.drawImage(blackbackgammon, m - 15, n - 15, 30, 30, this);
				}
				if (backgammon[i][j] == 2) { 
					int m = (i + 1) * 40 + 16;
					int n = (j + 1) * 40 + 40;
					g.drawImage(whitebackgammon, m - 15, n - 15, 30, 30, this);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) { //mouse hareketleri
		this.x = e.getX();
		this.y = e.getY();
		System.out.println("x: " + e.getX());
		System.out.println("y: " + e.getY());
		hakem.pp(this);
		hakem.pc(this);
		hakem.network(this);
		hakem.newgame(this);
		hakem.regretbackgammon(this);
		hakem.menu(this);
		
		if (SelectResult[1] == 1) {
			SelectResult[0] = 0;
			
			hakem.PClokasyon(this);
			
			pp.lounchpp(this);
			if (Iskazanan) {
				return;
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (Isfirst) {
				Isfirst = false;
				pc.lounchpc(this);
				this.repaint();
				pp.lounchpp(this);
			}
			System.out.println("İnsan-Pc Oyunu");
		}
		
		if (SelectResult[2] == 1) {
			if (isStart && isYourTurn) {
				
				hakem.Connectlokasyon(this);
				repaint();
			}
			
			new Thread(new Runnable() {
				public void run() {
					
					while (true) {
						while (isStart && !isYourTurn) {
							try {
								
								koordinatx = din.readInt();
								
								koordinaty = din.readInt();
								backgammon[koordinatx][koordinaty] = din.readInt();
								repaint();
								
								isYourTurn = !isYourTurn;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
			
			pp.lounchpp(this);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

    
}

