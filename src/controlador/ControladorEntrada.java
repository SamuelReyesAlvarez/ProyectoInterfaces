package controlador;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JOptionPane;
import modelo.BasesDeDatos;
import modelo.FicheroRegistros;
import modelo.FlujoJuego;
import modelo.JuegoException;
import modelo.Jugador;
import vista.panel.PanelEntrada;
import vista.ventana.VentanaCarga;
import vista.ventana.VentanaEntrada;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 */
public class ControladorEntrada implements ActionListener {

    private static final String FICHERO_TUTORIAL = "archivos/Tutorial.pdf";
    private static final String RUTA_AYUDA = "help/help_set.hs";

    private VentanaEntrada vEntrada;
    private PanelEntrada pEntrada;
    private FlujoJuego flujoJuego;
    private BasesDeDatos bd;
    private FicheroRegistros fRegistros;
    private Random r;
    private String nombre;
    private boolean nueva;

    public ControladorEntrada(VentanaEntrada vE, PanelEntrada pE) {
        vEntrada = vE;
        pEntrada = pE;

        pEntrada.setSize(600, 400);

        fRegistros = new FicheroRegistros();
        try {
            pEntrada.setDatosTabla(fRegistros.cargarRegistros());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(pE, ex.getMessage());
        }

        activarAyuda();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case "tutorial":
                    // Funcion temporal del boton Tutorial para acceder al documento
                    try {
                        Desktop.getDesktop().open(new File(FICHERO_TUTORIAL));
                    } catch (IOException ex) {
                        Logger.getLogger(ControladorEntrada.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "nueva":
                    // obtiene el nombre escrito por el usuario en la etiqueta
                    nombre = pEntrada.getTxtNombre().getText();
                    // comprueba si ya hay una partida con ese nombre
                    if (!pEntrada.getNombresTabla().contains(nombre.toUpperCase())) {
                        // comprueba si el nombre tiene el formato correcto
                        new Jugador(nombre);
                        bd = new BasesDeDatos(nombre);
                        nueva = true;
                        // si todo bien, comienza una nueva partida
                    } else {
                        JOptionPane.showMessageDialog(vEntrada, "Ya existe una partida guardada con ese nombre");
                    }
                    break;
                case "cargar":
                    try {
                        nombre = pEntrada.getTabla().getValueAt(pEntrada.getTabla().getSelectedRow(), 1).toString();
                        bd = new BasesDeDatos(nombre);
                        nueva = false;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(vEntrada, "Debe seleccionar una partida para cargar");
                    }
                    break;
                case "borrar":
                    try {
                        String partida = pEntrada.getTabla().getValueAt(pEntrada.getTabla().getSelectedRow(), 1).toString();
                        bd = new BasesDeDatos(partida);
                        bd.eliminarDatos(partida);
                        pEntrada.setDatosTabla(fRegistros.cargarRegistros());
                        bd.cerrarConexion();
                        nombre = null;
                        bd = null;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(vEntrada, "Debe seleccionar una partida a borrar");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(vEntrada, ex.getMessage());
                        bd.cerrarConexion();
                        bd = null;
                    }

                    vEntrada.revalidate();
                    vEntrada.repaint();
                    break;
                case "salir":
                    System.exit(0);
                    break;
            }
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(vEntrada, ex.getMessage());
            bd.cerrarConexion();
            bd = null;
        }
    }

    public FlujoJuego iniciarCarga(VentanaCarga vCarga, VentanaJuego vJuego, BasesDeDatos bd, String nombre, boolean nuevaPartida) {
        vEntrada.dispose();

        vCarga.getBarraProgreso().setVentanaPadre(vCarga);
        vCarga.getBarraProgreso().setVentanaNueva(vJuego);
        vCarga.setVisible(true);

        r = new Random();

        flujoJuego = new FlujoJuego();

        try {
            if (nuevaPartida) {
                flujoJuego.iniciarDatos(nombre);
            } else {
                flujoJuego = bd.cargarDatos(nombre);
            }
        } catch (IOException | JuegoException ex) {
            JOptionPane.showMessageDialog(vCarga, ex);
        }

        for (int i = 0; i <= 100; i++) {
            vCarga.getBarraProgreso().setProgreso(i);
            vCarga.getBarraProgreso().setBordeYTexto(null, "Cargando " + i + "%");

            try {
                int retardoCarga = r.nextInt(50);
                Thread.sleep(retardoCarga);
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(vCarga, ex);
            }

        }

        vCarga.getBarraProgreso().cambioDeVentana();

        return flujoJuego;
    }

    public String getNombre() {
        return nombre;
    }

    public BasesDeDatos getBD() {
        return bd;
    }

    public boolean getNueva() {
        return nueva;
    }

    public FlujoJuego getFlujo() {
        return flujoJuego;
    }

    public void activarAyuda() {
        try {
            // Carga el fichero de ayuda
            File fichero = new File(RUTA_AYUDA);
            URL hsURL = fichero.toURI().toURL();

            // Crea el HelpSet y el HelpBroker
            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            HelpBroker hb = helpset.createHelpBroker();

            // Activa Help y Tutorial en botones
            // Asigna la tecla F1 para la ventana actual (no fundiona)
            hb.enableHelpOnButton(pEntrada.getAyuda(), "aplicacion", helpset);
            //hb.enableHelpOnButton(pEntrada.getTutorial(), "tutorial", helpset);
            hb.enableHelpKey(vEntrada.getContentPane(), "ventana_entrada", helpset);
        } catch (MalformedURLException | HelpSetException ex) {
            JOptionPane.showMessageDialog(vEntrada, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
