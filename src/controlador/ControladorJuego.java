package controlador;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JOptionPane;
import modelo.BasesDeDatos;
import modelo.FlujoJuego;
import modelo.JuegoException;
import vista.panel.MenuJuego;
import vista.panel.PanelBazar;
import vista.panel.PanelClasificacion;
import vista.panel.PanelDuelo;
import vista.panel.PanelEstado;
import vista.panel.PanelJuego;
import vista.panel.PanelMision;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 * Gestiona las funciones disponibles en el panel de juego
 *
 */
public class ControladorJuego implements ActionListener {

    // Localizacion de los archivos necesarios para las funciones de la ventana
    private static final String FICHERO_TUTORIAL = "archivos/Tutorial.pdf";
    private static final String FICHERO_MANUAL = "archivos/ManualUsuario.pdf";
    private static final String RUTA_AYUDA = "help/help_set.hs";

    // Objetos necesarios para gestionar la ventana
    private FlujoJuego flujoJuego;
    private VentanaJuego vJuego;
    private MenuJuego menu;
    private PanelJuego pJuego;
    private PanelEstado pEstado;
    private BasesDeDatos bDatos;
    private ControladorEstado ctrEstado;

    // Controlador
    public ControladorJuego(BasesDeDatos bd, VentanaJuego vJ, FlujoJuego flujo) {
        flujoJuego = flujo;
        vJuego = vJ;
        menu = new MenuJuego();
        pJuego = new PanelJuego();
        pEstado = new PanelEstado();
        ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
        bDatos = bd;

        actualizarPJuego();

        // Carga el panel interno por defecto en la zona dinamica de la ventana
        pEstado.addControlador(ctrEstado);
        menu.addControlador(this);
        pJuego.addControlador(this);
        vJuego.setJMenuBar(menu);
        vJuego.setContentPane(pJuego);
        pJuego.setPdCentro(pEstado);

        activarAyuda();
    }

    // Controla la ejecucion de las funciones definidas en los componentes de la
    // ventana que poseen escucha de eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        ControladorDuelo ctrDuelo;
        ControladorTareas ctrTareas;
        int opcion;

        try {
            // Comprueba que componente recibio el evento
            switch (e.getActionCommand()) {
                case "terminarTurno":
                    // Deshabilita el boton Terminar Turno mientras se realiza
                    // la simulacion
                    pJuego.getBotonTerminar().setEnabled(false);
                    // Simula los jugadores de la maquina
                    String mensaje = flujoJuego.pasarTurno();
                    if (mensaje != null && mensaje.length() > 0) {
                        JOptionPane.showMessageDialog(vJuego, mensaje);
                    }
                    // Activa de nuevo el boton Terminar Turno y actualiza la
                    // pantalla de juego
                    pJuego.getBotonTerminar().setEnabled(true);
                    pEstado = new PanelEstado();
                    ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
                    pEstado.addControlador(ctrEstado);
                    pJuego.setPdCentro(pEstado);
                    JOptionPane.showMessageDialog(vJuego, "Comienza el turno " + flujoJuego.getTurnoDeJuego());
                    break;
                case "tutorial":
                    // Abre el pdf con el Tutorial paso a paso de tareas en la aplicacion
                    Desktop.getDesktop().open(new File(FICHERO_TUTORIAL));
                    break;
                case "manual":
                    // Abre el Manual de usuario
                    Desktop.getDesktop().open(new File(FICHERO_MANUAL));
                    break;
                case "guardar":
                    // Almacena los datos de la partida en la base de datos y
                    // actualiza la informacion de la partida en el fichero de
                    // partidas guardadas
                    bDatos.guardarDatos(flujoJuego);
                    JOptionPane.showMessageDialog(pEstado, "Partida guardada");
                    break;
                case "salirPartida":
                    // Pregunta al usuario si desea guardar la partida antes de
                    // salir al menu principal y controla su respuesta
                    opcion = JOptionPane.showConfirmDialog(vJuego, "Vas a salir de la partida, ¿deseas guardarla?", "Salir de la partida", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        bDatos.guardarDatos(flujoJuego);
                        vJuego.dispose();
                        bDatos.cerrarConexion();
                        bDatos = null;
                    } else if (opcion == JOptionPane.NO_OPTION) {
                        vJuego.dispose();
                        bDatos.cerrarConexion();
                        bDatos = null;
                    }
                    break;
                case "salirJuego":
                    // Pregunta al usuario si desea guardar la partida antes de
                    // salir al menu principal y controla su respuesta
                    opcion = JOptionPane.showConfirmDialog(vJuego, "Vas a salir del juego, ¿deseas guardar la partida?", "Salir del juego", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        bDatos.guardarDatos(flujoJuego);
                        bDatos.cerrarConexion();
                        System.exit(1);
                    } else if (opcion == JOptionPane.NO_OPTION) {
                        bDatos.cerrarConexion();
                        System.exit(0);
                    }
                    break;
                case "estado":
                    // Muestra la el panel de Estado en la zona dinamica de la ventana
                    pEstado = new PanelEstado();
                    ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
                    pEstado.addControlador(ctrEstado);
                    pJuego.setPdCentro(pEstado);
                    break;
                case "duelo":
                    // Muestra la el panel de Duelo en la zona dinamica de la ventana
                    if (flujoJuego.getJugador().getPosiblesDuelistas() == null) {
                        flujoJuego.crearDuelistas(flujoJuego.getJugador());
                    }
                    PanelDuelo pDuelo = new PanelDuelo();
                    ctrDuelo = new ControladorDuelo(vJuego, pJuego, pDuelo, flujoJuego, this);
                    pDuelo.addControlador(ctrDuelo);
                    pJuego.setPdCentro(pDuelo);
                    break;
                case "mision":
                    // Muestra la el panel de Mision en la zona dinamica de la ventana
                    PanelMision pMision = new PanelMision();
                    ctrTareas = new ControladorTareas(vJuego, pMision, flujoJuego, this);
                    pMision.addControlador(ctrTareas);
                    pJuego.setPdCentro(pMision);
                    break;
                case "bazar":
                    // Muestra la el panel de Bazar en la zona dinamica de la ventana
                    PanelBazar pBazar = new PanelBazar();
                    ctrTareas = new ControladorTareas(vJuego, pBazar, flujoJuego, this);
                    pBazar.addControlador(ctrTareas);
                    pJuego.setPdCentro(pBazar);
                    break;
                case "clasificacion":
                    // Muestra la el panel de Clasificacion en la zona dinamica de la ventana
                    PanelClasificacion pClasificacion = new PanelClasificacion();
                    ctrTareas = new ControladorTareas(vJuego, pClasificacion, flujoJuego, this);
                    pClasificacion.addControlador(ctrTareas);
                    pJuego.setPdCentro(pClasificacion);
                    break;
            }

            actualizarPJuego();
        } catch (IOException | JuegoException ex) {
            JOptionPane.showMessageDialog(vJuego, ex.getMessage());
        }
    }

    // Actualiza la informacion de la ventana con los datos de la partida requeridos
    protected void actualizarPJuego() {
        pJuego.setAtaqueMinMax(String.valueOf(flujoJuego.getJugador().getAtaqueMin()) + " - " + String.valueOf(flujoJuego.getJugador().getAtaqueMax()));
        pJuego.setDefensaMinMax(String.valueOf(flujoJuego.getJugador().getDefensaMin()) + " - " + String.valueOf(flujoJuego.getJugador().getDefensaMax()));
        pJuego.setEtFoto(null);
        pJuego.setEtNivel(String.valueOf(flujoJuego.getJugador().getNivel()));
        pJuego.setEtNombre(String.valueOf(flujoJuego.getJugador().getNombre()));
        pJuego.setEtOroAcumulado(String.valueOf(flujoJuego.getJugador().getOroActual()));
        pJuego.setEtTurnoActual(String.valueOf(flujoJuego.getTurnoDeJuego()));
        pJuego.setPbExperiencia(flujoJuego.getJugador().getExpSigNivel(), flujoJuego.getJugador().getExpAcumulada());
        pJuego.setPbVida(flujoJuego.getJugador().getVidaMax(), flujoJuego.getJugador().getVidaActual());
        vJuego.revalidate();
        vJuego.repaint();
    }

    // Devuelve el estado de la conexion con la base de datos para el control de
    // la ejecucion desde el Main
    public BasesDeDatos getBD() {
        return bDatos;
    }

    // Muestra la ayuda de la aplicacion
    public void activarAyuda() {
        // JavaHelp
        try {
            // Carga el fichero de ayuda
            File fichero = new File(RUTA_AYUDA);
            URL hsURL = fichero.toURI().toURL();

            // Crea el HelpSet y el HelpBroker
            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            HelpBroker hb = helpset.createHelpBroker();

            // Activa Help en boton y asigna la tecla F1 para la ventana actual
            hb.enableHelpOnButton(menu.getAyuda(), "aplicacion", helpset);
            hb.enableHelpKey(vJuego.getContentPane(), "ventana_juego", helpset);
        } catch (MalformedURLException | HelpSetException ex) {
            JOptionPane.showMessageDialog(vJuego, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
