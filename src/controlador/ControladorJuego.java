package controlador;

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
 */
public class ControladorJuego implements ActionListener {

    private static final String RUTA_AYUDA = "help/help_set.hs";

    private FlujoJuego flujoJuego;
    private VentanaJuego vJuego;
    private MenuJuego menu;
    private PanelJuego pJuego;
    private PanelEstado pEstado;
    private BasesDeDatos bDatos;
    private ControladorEstado ctrEstado;

    public ControladorJuego(BasesDeDatos bd, VentanaJuego vJ, FlujoJuego flujo) {
        flujoJuego = flujo;
        vJuego = vJ;
        menu = new MenuJuego();
        pJuego = new PanelJuego();
        pEstado = new PanelEstado();
        ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
        bDatos = bd;

        actualizarPJuego();

        pEstado.addControlador(ctrEstado);
        menu.addControlador(this);
        pJuego.addControlador(this);
        vJuego.setJMenuBar(menu);
        vJuego.setContentPane(pJuego);
        pJuego.setPdCentro(pEstado);

        activarAyuda();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ControladorDuelo ctrDuelo;
        ControladorTareas ctrTareas;
        int opcion;

        try {
            switch (e.getActionCommand()) {
                case "terminarTurno":
                    // deshabilita el boton Terminar Turno mientras se realiza la simulacion
                    pJuego.getBotonTerminar().setEnabled(false);
                    // simula los jugadores de la maquina
                    String mensaje = flujoJuego.pasarTurno();
                    if (mensaje != null && mensaje.length() > 0) {
                        JOptionPane.showMessageDialog(vJuego, mensaje);
                    }
                    // activa de nuevo el boton Terminar Turno y actualiza la pantalla de juego
                    pJuego.getBotonTerminar().setEnabled(true);
                    pEstado = new PanelEstado();
                    ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
                    pEstado.addControlador(ctrEstado);
                    pJuego.setPdCentro(pEstado);
                    JOptionPane.showMessageDialog(vJuego, "Comienza el turno " + flujoJuego.getTurnoDeJuego());
                    break;
                case "guardar":
                    bDatos.guardarDatos(flujoJuego);
                    JOptionPane.showMessageDialog(pEstado, "Partida guardada");
                    break;
                case "salirPartida":
                    opcion = JOptionPane.showConfirmDialog(vJuego, "Vas a salir de la partida, ¿deseas guardarla?");
                    if (opcion == JOptionPane.YES_OPTION) {
                        bDatos.guardarDatos(flujoJuego);
                        vJuego.dispose();
                    } else if (opcion == JOptionPane.NO_OPTION) {
                        vJuego.dispose();
                    }
                    bDatos.cerrarConexion();
                    bDatos = null;
                    break;
                case "salirJuego":
                    opcion = JOptionPane.showConfirmDialog(vJuego, "Vas a salir del juego, ¿deseas guardar la partida?");
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
                    pEstado = new PanelEstado();
                    ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, this);
                    pEstado.addControlador(ctrEstado);
                    pJuego.setPdCentro(pEstado);
                    break;
                case "duelo":
                    if (flujoJuego.getJugador().getPosiblesDuelistas() == null) {
                        flujoJuego.crearDuelistas(flujoJuego.getJugador());
                    }
                    PanelDuelo pDuelo = new PanelDuelo();
                    ctrDuelo = new ControladorDuelo(vJuego, pJuego, pDuelo, flujoJuego, this);
                    pDuelo.addControlador(ctrDuelo);
                    pJuego.setPdCentro(pDuelo);
                    break;
                case "mision":
                    PanelMision pMision = new PanelMision();
                    ctrTareas = new ControladorTareas(vJuego, pMision, flujoJuego, this);
                    pMision.addControlador(ctrTareas);
                    pJuego.setPdCentro(pMision);
                    break;
                case "bazar":
                    PanelBazar pBazar = new PanelBazar();
                    ctrTareas = new ControladorTareas(vJuego, pBazar, flujoJuego, this);
                    pBazar.addControlador(ctrTareas);
                    pJuego.setPdCentro(pBazar);
                    break;
                case "clasificacion":
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

    public BasesDeDatos getBD() {
        return bDatos;
    }

    public void activarAyuda() {
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
