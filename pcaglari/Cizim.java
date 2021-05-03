package pcaglari;

import javax.swing.JOptionPane;

public class Cizim {

    public void kazanan(MainFrame frame, int selectwin) { //kazanan kaybeden tahta çizimleri
        
        if (selectwin == 1) {
            JOptionPane.showMessageDialog(frame, "Siyah Kazandı", "Puan", 1);
        }
        if (selectwin == 2) {
            JOptionPane.showMessageDialog(frame, "Beyaz Kazandı", "Puan", 1);
        }
    }

    public void yenilen(MainFrame frame, int x1, int y1, int x2, int y2) {
        frame.getGraphics().drawLine((x1 + 1) * 40 + 16, (y1 + 1) * 40 + 40, (x2 + 1) * 40 + 16, (y2 + 1) * 40 + 40);
        frame.getGraphics().drawLine((x1 + 1) * 40 + 16, (y1 + 1) * 40 + 41, (x2 + 1) * 40 + 16, (y2 + 1) * 40 + 41);
        frame.getGraphics().drawLine((x1 + 1) * 40 + 17, (y1 + 1) * 40 + 40, (x2 + 1) * 40 + 17, (y2 + 1) * 40 + 40);
        frame.getGraphics().drawLine((x1 + 1) * 40 + 16, (y1 + 1) * 40 + 42, (x2 + 1) * 40 + 16, (y2 + 1) * 40 + 42);
        frame.getGraphics().drawLine((x1 + 1) * 40 + 18, (y1 + 1) * 40 + 40, (x2 + 1) * 40 + 18, (y2 + 1) * 40 + 40);
    }
}
