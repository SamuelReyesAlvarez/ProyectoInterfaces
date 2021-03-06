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
 * Gestiona la ejecucion del video de presentacion de la empresa
 *
 */
public class ControladorInicio extends KeyAdapter {

    // Localicacion del video
    private static final String RUTA_VIDEO = "videos/iPlayGames480.mp4";
    // Valores predefinidos para reproducir el video
    private static final int VOLUMEN_VIDEO = 1;
    private static final int TOTAL_CICLOS = 20;

    // Objetos necesarios para la gestion de la ventana
    private VentanaInicio vInicio;
    private JFXPanel pVideo;

    // Constructor
    public ControladorInicio(VentanaInicio vInicio) {
        this.vInicio = vInicio;
        pVideo = new JFXPanel();
        pVideo.addKeyListener(this);

        this.vInicio.add(pVideo);
        cargarVideoEmpresa();
        this.vInicio.dispose();
    }

    // Crea un nuevo hilo donde carga y ejecuta el video
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

            // Pausa la ejecucion del proceso principal hasta que el video termine
            // o sea interrumpido
            int ciclos = 0;
            do {
                Thread.sleep(500);
                ciclos++;
            } while (vInicio.isActive() && ciclos < TOTAL_CICLOS);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(pVideo, "Se ha interrumpido el video");
        }
    }

    // Cierra la ventana y fuerza la interrupcion del video al pulsa la tecla Esc
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            vInicio.dispose();
        }
    }
}
