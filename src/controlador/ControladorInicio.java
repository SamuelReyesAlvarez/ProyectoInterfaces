package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.JOptionPane;
import vista.ventana.VentanaInicio;

/**
 *
 * @author Samuel Reyes
 *
 */
public class ControladorInicio extends KeyAdapter {

    private static final String RUTA_VIDEO = "videos/iPlayGames480.mp4";
    private static final int VOLUMEN_VIDEO = 1;
    private static final int TOTAL_CICLOS = 20;

    private VentanaInicio vInicio;
    private JFXPanel pVideo;

    public ControladorInicio(VentanaInicio vInicio) {
        this.vInicio = vInicio;
        pVideo = new JFXPanel();
        pVideo.addKeyListener(this);

        this.vInicio.add(pVideo);
        cargarVideoEmpresa();
        this.vInicio.dispose();
    }

    public void cargarVideoEmpresa() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    File video = new File(RUTA_VIDEO);
                    MediaPlayer reproductor = new MediaPlayer(new Media(video.toURI().toString()));
                    pVideo.setScene(new Scene(new Group(new MediaView(reproductor))));
                    reproductor.setVolume(VOLUMEN_VIDEO);
                    reproductor.play();
                }
            });

            int ciclos = 0;
            do {
                Thread.sleep(500);
                ciclos++;
            } while (vInicio.isActive() && ciclos < TOTAL_CICLOS);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(pVideo, "Se ha interrumpido el video");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            vInicio.dispose();
        }
    }
}
