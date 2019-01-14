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
 * Gestiona las acciones realizadas y los datos visibles sobre la ventana de
 * entrada
 *
 */
public class ControladorEntrada implements ActionListener {

    // Localizacion de los ficheros necesarios en la clase
    private static final String FICHERO_TUTORIAL = "archivos/Tutorial.pdf";
    private static final String RUTA_AYUDA = "help/help_set.hs";

    // Objetos necesarios para la gestion de la ventana
    private VentanaEntrada vEntrada;
    private PanelEntrada pEntrada;
    private FlujoJuego flujoJuego;
    private BasesDeDatos bd;
    private FicheroRegistros fRegistros;
    private Random r;
    private String nombre;
    private boolean nueva;

    // Constructor
    public ControladorEntrada(VentanaEntrada vE, PanelEntrada pE) {
        vEntrada = vE;
        pEntrada = pE;

        pEntrada.setSize(600, 400);

        fRegistros = new FicheroRegistros();

        // Rellena la tabla con los datos requeridos
        try {
            pEntrada.setDatosTabla(fRegistros.cargarRegistros());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(pE, ex.getMessage());
        }

        activarAyuda();
    }

    // Controla las acciones realizadas por el usuario sobre los componentes que
    // poseen escuchadores de eventos
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
                    // Obtiene el nombre escrito por el usuario en la etiqueta
                    nombre = pEntrada.getTxtNombre().getText();
                    // Comprueba si ya hay una partida con ese nombre
                    if (!pEntrada.getNombresTabla().contains(nombre.toUpperCase())) {
                        // Comprueba si el nombre tiene el formato correcto
                        new Jugador(nombre);
                        bd = new BasesDeDatos(nombre);
                        nueva = true;
                        // Si todo bien, comienza una nueva partida
                    } else {
                        JOptionPane.showMessageDialog(vEntrada, "Ya existe una partida guardada con ese nombre");
                    }
                    break;
                case "cargar":
                    try {
                        // Obtiene el nombre de la partida seleccionada en la tabla
                        nombre = pEntrada.getTabla().getValueAt(pEntrada.getTabla().getSelectedRow(), 1).toString();
                        bd = new BasesDeDatos(nombre);
                        nueva = false;
                        // Si todo va bien, carga la partida guardada
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(vEntrada, "Debe seleccionar una partida para cargar");
                    }
                    break;
                case "borrar":
                    try {
                        // Obtiene el nombre de la partida seleccionada en la tabla
                        String partida = pEntrada.getTabla().getValueAt(pEntrada.getTabla().getSelectedRow(), 1).toString();
                        bd = new BasesDeDatos(partida);
                        // Elimina la partida de la base de datos y el registro
                        // del fichero que alimenta la tabla
                        bd.eliminarDatos(partida);
                        // Actualiza los datos en la tabla
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

                    // Actualiza la ventana
                    vEntrada.revalidate();
                    vEntrada.repaint();
                    break;
                case "salir":
                    // Sale por completo de la aplicacion
                    System.exit(0);
                    break;
            }
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(vEntrada, ex.getMessage());
            if (bd != null) {
                bd.cerrarConexion();
            }
            bd = null;
        }
    }

    // Inicia o carga todos los datos de la partida y simula la carga de la barra
    // de progreso
    public FlujoJuego iniciarCarga(VentanaCarga vCarga, VentanaJuego vJuego, BasesDeDatos bd, String nombre, boolean nuevaPartida) {
        vEntrada.dispose();

        // Indica al bean personalizado las ventanas a gestionar
        vCarga.getBarraProgreso().setVentanaPadre(vCarga);
        vCarga.getBarraProgreso().setVentanaNueva(vJuego);
        vCarga.setVisible(true);

        r = new Random();

        flujoJuego = new FlujoJuego();

        // Crea o carga los datos de la partida
        try {
            if (nuevaPartida) {
                flujoJuego.iniciarDatos(nombre);
            } else {
                flujoJuego = bd.cargarDatos(nombre);
            }
        } catch (IOException | JuegoException ex) {
            JOptionPane.showMessageDialog(vCarga, ex);
        }

        // Simula el progreso de la barra de carga
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

        // Activa la funcion especial del bean
        vCarga.getBarraProgreso().cambioDeVentana();

        return flujoJuego;
    }

    // Metodos que devuelven al Main los datos necesarios para continuar la ejecucion
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

    //
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
            hb.enableHelpKey(vEntrada.getContentPane(), "ventana_entrada", helpset);
        } catch (MalformedURLException | HelpSetException ex) {
            JOptionPane.showMessageDialog(vEntrada, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
