package pcaglari;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Hakem { //oyunu yöneten biri olmalı

    Tahta tahta = new Tahta();
    MainFrame frame = null;
    TavlaOyunu tavla = null;

    public void lokasyon(MainFrame frame) { //çerçevede yerleşimi

        if (frame.x >= 36 && frame.x <= 638 && frame.y >= 60 && frame.y <= 658) {
            frame.koordinatx = (frame.x - 16) / 40;
            frame.koordinaty = (frame.y - 40) / 40;
            frame.tempx = (frame.koordinatx + 1) * 40 + 16;
            frame.tempy = (frame.koordinaty + 1) * 40 + 40;
            System.out.println("tempx: " + frame.tempx);
            System.out.println("tempy: " + frame.tempy);
            System.out.println("koordinatx: " + frame.koordinatx);
            System.out.println("koordinaty: " + frame.koordinaty);

            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) < 20 && (frame.tempx - frame.x) > 0
                    && (frame.tempy - frame.y) > 0) {
                System.out.println(+(frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                frame.koordinaty = frame.koordinaty;
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) >= 20) {
                System.out.println((frame.x - frame.tempx));

                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) >= 20 && (frame.tempx - frame.x) > 0) {
                System.out.println((frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) < 20 && (frame.tempy - frame.y) > 0) {
                System.out.println((frame.x - frame.tempx));
                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                frame.koordinaty = frame.koordinaty;
            }

            if (frame.backgammon[frame.koordinatx][frame.koordinaty] == 0) {
                if (frame.flag == 1) {
                    frame.backgammon[frame.koordinatx][frame.koordinaty] = 1;
                    frame.store.push(new Taslar(frame.koordinatx, frame.koordinaty, 1));
                    frame.flag = 2;
                } else {
                    frame.backgammon[frame.koordinatx][frame.koordinaty] = 2;
                    frame.store.push(new Taslar(frame.koordinatx, frame.koordinaty, 2));
                    frame.flag = 1;
                }
            }
			
    
    

    

    public void pc(MainFrame frame) { //insan-pc 
        if (frame.x > 734 && frame.y > 223 && frame.x < 855 && frame.y < 268) {
            frame.SelectResult[1] = JOptionPane.showConfirmDialog(frame, "İnsan-Pc Modu", "Mod Seçimi", 2) + 1;
            if (frame.SelectResult[1] == 1) {
                frame.pcfirst = JOptionPane.showConfirmDialog(frame,
                        "          " + "Önce Pc" + "\n" + "          " + "Önce Oyuncu", "İpuçları", 2) + 1;
                frame.store.clear();
            }
        }
    }

    public void network(final MainFrame frame) { //online 
        if (frame.x > 734 && frame.y > 358 && frame.x < 855 && frame.y < 408) {
            frame.SelectResult[2] = JOptionPane.showConfirmDialog(frame, "Online Oyun", "Mod Seçimi", 2) + 1;
            if (frame.SelectResult[2] == 1) {

                final JDialog jd = new JDialog(frame, "Online Seçeneği");
                FlowLayout a = new FlowLayout(FlowLayout.CENTER, 40, 40);
                jd.setVisible(true);
                jd.setResizable(false);
                Container jd1 = jd.getContentPane();
                jd1.setBackground(Color.red);
                jd.setBounds(500, 150, 200, 240);
                jd.setLayout(a);

                final JButton but1 = new JButton("Online Oluştur");
                final JButton but2 = new JButton("Online Katıl");

                but1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        new Thread(new Runnable() { // Sunucu Kurulumu için Thread Oluşumu
                            public void run() {
                                try {
                                    frame.server = new ServerSocket(8888);
                                    System.out.println("Sunucu Soket Başarıyla Oluşturuldu ");
                                    InetAddress ip = InetAddress.getLocalHost();
                                    String localip = ip.getHostAddress();
                                    System.out.println(localip);

                                    System.out.println("Bağlantı Bekleniliyor... ");
                                    frame.socket = frame.server.accept();
                                    System.out.println("Sunucu-İstemci Arasındaki Bağlantı");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    frame.din = new DataInputStream(frame.socket.getInputStream());
                                    System.out.println("İletim");
                                    frame.dout = new DataOutputStream(frame.socket.getOutputStream());
                                    frame.isStart = true;
                                    frame.flag = 1;
                                    frame.isYourTurn = true;
                                    frame.isServer = true;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (frame.SelectResult[0] == 1 && frame.SelectResult[1] == 1
                                        && frame.SelectResult[3] == 1) {
                                    try {
                                        frame.server.close();
                                        frame.din.close();
                                        frame.dout.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();
                        but2.setEnabled(false);
                    }
                });

                but2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() { // İstemci Kurulumu
                            public void run() {
                                String ip = JOptionPane.showInputDialog(frame, "İpAdresi");
                                System.out.println("Oyuncu" + ip);
                                if (ip != null) {
                                    try {
                                        frame.client = new Socket(ip, 8888);
                                        System.out.println("Oyuncu Oluşturuldu");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        frame.din = new DataInputStream(frame.client.getInputStream());
                                        frame.dout = new DataOutputStream(frame.client.getOutputStream());
                                        System.out.println("İletim");
                                        frame.isStart = true;
                                        frame.isYourTurn = true;
                                        frame.isClient = true;
                                        frame.flag = 2;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (frame.SelectResult[0] == 1 && frame.SelectResult[1] == 1
                                        && frame.SelectResult[3] == 1) {
                                    try {
                                        frame.client.close();
                                        frame.din.close();
                                        frame.dout.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();
                        but1.setEnabled(false);
                    }
                });

                jd.add(but1);
                jd.add(but2);
                frame.store.clear();
            }
        }
    }

    public void newgame(MainFrame frame) {
        if (frame.x > 734 && frame.y > 430 && frame.x < 855 && frame.y < 479) {
            frame.SelectResult[3] = JOptionPane.showConfirmDialog(frame, "Yeniden Başlat", "Mod Seçimi", 2) + 1;

            
            frame.SelectResult[1] = 0; //menü seçimleri
            frame.SelectResult[2] = 0;
            frame.Clickfirst = false;
            frame.Isfirst = true;
            frame.Iskazanan = false;
            frame.repaint();
            frame.koordinatx = 0;
            frame.koordinaty = 0;
            frame.backgammoncount = 0;
            frame.store.clear(); //temizle 

        }
    }

    public void regretbackgamon(MainFrame frame) {
        int result = 0;
        if (frame.x > 734 && frame.y > 502 && frame.x < 855 && frame.y < 550) {
            result = JOptionPane.showConfirmDialog(frame, "Pes Etmek İstiyor Musunuz？", "Mod Seçimi", 2) + 1;

            if (result == 1) {
                result = 0;
                Taslar xoy = frame.store.pop();
                int x = xoy.getx();
                int y = xoy.gety();
                frame.flag = xoy.getflag();
                frame.backgammon[x][y] = 0;
                frame.repaint();
            }
            frame.regretcount = result + frame.regretcount;
        }

    }

    public int menu(final MainFrame frame) {
        int result = 0;
        if (frame.x > 734 && frame.y > 154 && frame.x < 855 && frame.y < 203) {
            System.out.println(result);
            final JDialog jd = new JDialog(frame, "Menü Seçenekleri");
            FlowLayout buju = new FlowLayout(FlowLayout.CENTER, 20, 40);
            jd.setVisible(true);
            jd.setResizable(false);
            Container jd1 = jd.getContentPane();
            jd1.setBackground(Color.green);
            jd.setBounds(500, 150, 200, 350);
            jd.setLayout(buju);
            JButton but1 = new JButton("Diğer Ayarlar");
            JButton but2 = new JButton("Yenilgi");
            JButton but4 = new JButton("Çık");
            but1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.flag = JOptionPane.showConfirmDialog(jd, "Siyah Öncelikli" + "\n" + "Yanlış,Beyaz Öncelikli", "Mod Seçimi", 2) + 1;
                    if (frame.flag != 1) {
                        frame.flag = 2;
                    }
                }
            });
            but2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (frame.flag == 1) {
                        JOptionPane.showMessageDialog(jd, "Beyaz Kazandı");
                    }
                    if (frame.flag == 2) {
                        JOptionPane.showMessageDialog(jd, "Siyah Kazandı");
                    }
                }
            });

            but4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(jd, "Oyundan Çıktınız", "İpuçları", 2);
                    if (result == 0) {
                        System.exit(0);
                    }
                }
            });
            jd.add(but1);
            jd.add(but2);
            jd.add(but4);
            return result;
        }
        return 1;
    }

    public void PClokasyon(MainFrame frame) {

        if (frame.x >= 36 && frame.x <= 638 && frame.y >= 60 && frame.y <= 658) {
            frame.koordinatx = (frame.x - 16) / 40;
            frame.koordinaty = (frame.y - 40) / 40;
            frame.tempx = (frame.koordinatx + 1) * 40 + 16;
            frame.tempy = (frame.koordinaty + 1) * 40 + 40;

            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) < 20 && (frame.tempx - frame.x) > 0
                    && (frame.tempy - frame.y) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                frame.koordinaty = frame.koordinaty;
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) >= 20) {
                System.out.println("tempx: " + (frame.x - frame.tempx));

                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) >= 20 && (frame.tempx - frame.x) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) < 20 && (frame.tempy - frame.y) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                frame.koordinaty = frame.koordinaty;
            }

            if (frame.backgammon[frame.koordinatx][frame.koordinaty] == 0) {
                frame.backgammon[frame.koordinatx][frame.koordinaty] = 2;

                frame.store.push(new Taslar(frame.koordinatx, frame.koordinaty, 2));
                frame.Isfirst = true;
                frame.Clickfirst = true;
                frame.backgammoncount++;
                frame.repaint();
                System.out.println("Beyaz Taşlar" + frame.backgammoncount);
            }

        }
    }

    public void Connectlokasyon(MainFrame frame) { //client-server bağlantı

        if (frame.x >= 36 && frame.x <= 638 && frame.y >= 60 && frame.y <= 658) {
            frame.koordinatx = (frame.x - 16) / 40;
            frame.koordinaty = (frame.y - 40) / 40;
            frame.tempx = (frame.koordinatx + 1) * 40 + 16;
            frame.tempy = (frame.koordinaty + 1) * 40 + 40;
            System.out.println("tempx: " + frame.tempx);
            System.out.println("tempy: " + frame.tempy);
            System.out.println("coox: " + frame.koordinatx);
            System.out.println("cooy: " + frame.koordinaty);

            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) < 20 && (frame.tempx - frame.x) > 0
                    && (frame.tempy - frame.y) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                frame.koordinaty = frame.koordinaty;
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) >= 20) {
                System.out.println("tempx: " + (frame.x - frame.tempx));

                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) < 20 && (frame.tempy - frame.y) >= 20 && (frame.tempx - frame.x) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                frame.koordinatx = frame.koordinatx;
                if (frame.koordinaty == 0) {
                    frame.koordinaty = frame.koordinaty;
                } else {
                    frame.koordinaty = frame.koordinaty - 1;
                }
            }
            if ((frame.tempx - frame.x) >= 20 && (frame.tempy - frame.y) < 20 && (frame.tempy - frame.y) > 0) {
                System.out.println("tempx: " + (frame.x - frame.tempx));
                if (frame.koordinatx == 0) {
                    frame.koordinatx = frame.koordinatx;
                } else {
                    frame.koordinatx = frame.koordinatx - 1;
                }
                frame.koordinaty = frame.koordinaty;
            }

            if (frame.backgammon[frame.koordinatx][frame.koordinaty] == 0) {
                if (frame.isServer) {
                    frame.backgammon[frame.koordinatx][frame.koordinaty] = 1;
                    frame.repaint();
                    try {
                        frame.dout.writeInt(frame.koordinatx);
                        frame.dout.writeInt(frame.koordinaty);
                        frame.dout.writeInt(1);
                        System.out.println("İstemciye");

                        if (!frame.Iskazanan) {
                            frame.isYourTurn = !frame.isYourTurn;
                        }

                        frame.swapServer = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    frame.store.push(new Taslar(frame.koordinatx, frame.koordinaty, 1));
                }
                if (frame.isClient) {
                    frame.backgammon[frame.koordinatx][frame.koordinaty] = 2;
                    frame.repaint();
                    try {
                        frame.dout.writeInt(frame.koordinatx);
                        frame.dout.writeInt(frame.koordinaty);
                        frame.dout.writeInt(2);
                        System.out.println("Sunucuya、");

                        if (!frame.Iskazanan) {
                            frame.isYourTurn = !frame.isYourTurn;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        frame.store.push(new Taslar(frame.koordinatx, frame.koordinaty, 2));
                    }
                }
            }
        }
    }

    public void LounchThread() {
        if (frame.SelectResult[2] == 1) {

            new Thread(new Runnable() {

                public void run() {

                    while (true) {
                        while (frame.isStart && !frame.isYourTurn) {
                            try {

                                frame.koordinatx = frame.din.readInt();

                                frame.koordinaty = frame.din.readInt();
                                frame.backgammon[frame.koordinatx][frame.koordinaty] = frame.din.readInt();
                                System.out.println("koordinatx = din.readInt();    Diğerinin X koordinatı" + frame.din.readInt());
                                frame.repaint();

                                frame.isYourTurn = !frame.isYourTurn;

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();

            if (frame.isServer && frame.swapServer) {
                frame.swapServer = false;
                frame.isYourTurn = !frame.isYourTurn;
            }

            //frame.pp.lounchpp(frame);
        }
    }

    void regretbackgammon(MainFrame aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
