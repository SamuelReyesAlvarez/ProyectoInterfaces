package vista.ventana;

import com.sun.awt.AWTUtilities;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Samuel Reyes
 *
 */
public class VentanaEntrada extends JFrame {

    public VentanaEntrada(String rutaIcono) {
        try {
            setIconImage(ImageIO.read(new File(rutaIcono)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSize(600, 400);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setVisible(true);

        Shape forma = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 15, 15);

        AWTUtilities.setWindowShape(this, forma);
    }
}
