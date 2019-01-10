package vista.ventana;

import com.sun.awt.AWTUtilities;
import java.awt.Shape;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JDialog;

/**
 *
 * @author Samuel Reyes
 *
 */
public class VentanaInicio extends JDialog {

    public VentanaInicio() {
        setSize(854, 480);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setVisible(true);

        Shape forma = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 15, 15);

        AWTUtilities.setWindowShape(this, forma);
    }

    public void addControlador(KeyListener kl) {
        addKeyListener(kl);
    }
}
