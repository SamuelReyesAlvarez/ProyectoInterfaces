package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JOptionPane;
import modelo.FlujoJuego;
import modelo.JuegoException;
import modelo.Jugador;
import vista.panel.PanelDuelo;
import vista.panel.PanelEstado;
import vista.panel.PanelJuego;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 * Esta clase gestiona las funciones disponibles en el panel de Duelo
 *
 */
public class ControladorDuelo implements ActionListener {

    // Objetos necesario para la gestion del panel
    private VentanaJuego vJuego;
    private PanelJuego pJuego;
    private PanelDuelo pDuelo;
    private FlujoJuego flujoJuego;
    private ControladorJuego ctrJuego;
    private Jugador[] duelistas;

    // Constructor
    ControladorDuelo(VentanaJuego vJ, PanelJuego pJ, PanelDuelo pD, FlujoJuego flujo, ControladorJuego ctrJ) {
        vJuego = vJ;
        pJuego = pJ;
        flujoJuego = flujo;
        ctrJuego = ctrJ;
        pDuelo = pD;

        actualizarPDuelo();
    }

    // Controla las acciones sobre los componentes que poseen un escuchador de eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        String reporte;

        // Comprueba si puede participar en combates
        if (flujoJuego.getJugador().getMisionActiva() != null) {
            JOptionPane.showMessageDialog(vJuego, "Completa la mision para poder realizar duelos");
        } else {
            try {
                // Ejecuta la accion de atacar sobre el jugador seleccionado y
                // muestra un informe con el resumen de la batalla
                switch (e.getActionCommand()) {
                    case "atacar1":
                        reporte = flujoJuego.combate(flujoJuego.getJugador(), duelistas[0]);
                        JOptionPane.showMessageDialog(vJuego, reporte, "Reporte de batalla", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "atacar2":
                        reporte = flujoJuego.combate(flujoJuego.getJugador(), duelistas[1]);
                        JOptionPane.showMessageDialog(vJuego, reporte, "Reporte de batalla", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "atacar3":
                        reporte = flujoJuego.combate(flujoJuego.getJugador(), duelistas[2]);
                        JOptionPane.showMessageDialog(vJuego, reporte, "Reporte de batalla", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "atacar4":
                        reporte = flujoJuego.combate(flujoJuego.getJugador(), duelistas[3]);
                        JOptionPane.showMessageDialog(vJuego, reporte, "Reporte de batalla", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }

                // Al cerrar el informe crea una nueva seleccion de duelistas
                // y actualiza los datos del jugador en las ventanas
                flujoJuego.crearDuelistas(flujoJuego.getJugador());
                PanelEstado pEstado = new PanelEstado();
                ControladorEstado ctrEstado = new ControladorEstado(vJuego, pJuego, pEstado, flujoJuego, ctrJuego);
                pEstado.addControlador(ctrEstado);
                pJuego.setPdCentro(pEstado);
                ctrJuego.actualizarPJuego();
            } catch (JuegoException ex) {
                JOptionPane.showMessageDialog(vJuego, ex.getMessage());
            }
        }
    }

    // Actualiza los datos de la ventana con los valores del flujo de juego
    private void actualizarPDuelo() {
        duelistas = new Jugador[4];

        int i = 0;
        for (Iterator<Jugador> it = flujoJuego.getJugador().getPosiblesDuelistas().iterator(); it.hasNext();) {
            Jugador posibleDuelista = it.next();
            duelistas[i] = posibleDuelista;
            i++;
        }

        if (duelistas[0] != null) {
            pDuelo.setDuelista1(duelistas[0].getNombre(), null, String.valueOf(duelistas[0].getNivel()),
                    String.valueOf(duelistas[0].getFuerza()), String.valueOf(duelistas[0].getArmadura()),
                    String.valueOf(duelistas[0].getDestreza()), String.valueOf(duelistas[0].getConstitucion()));
        } else {
            pDuelo.setDuelista1(null, null, null, null, null, null, null);
        }

        if (duelistas[1] != null) {
            pDuelo.setDuelista2(duelistas[1].getNombre(), null, String.valueOf(duelistas[1].getNivel()),
                    String.valueOf(duelistas[1].getFuerza()), String.valueOf(duelistas[1].getArmadura()),
                    String.valueOf(duelistas[1].getDestreza()), String.valueOf(duelistas[1].getConstitucion()));
        } else {
            pDuelo.setDuelista2(null, null, null, null, null, null, null);
        }

        if (duelistas[2] != null) {
            pDuelo.setDuelista3(duelistas[2].getNombre(), null, String.valueOf(duelistas[2].getNivel()),
                    String.valueOf(duelistas[2].getFuerza()), String.valueOf(duelistas[2].getArmadura()),
                    String.valueOf(duelistas[2].getDestreza()), String.valueOf(duelistas[2].getConstitucion()));
        } else {
            pDuelo.setDuelista3(null, null, null, null, null, null, null);
        }

        if (duelistas[3] != null) {
            pDuelo.setDuelista4(duelistas[3].getNombre(), null, String.valueOf(duelistas[3].getNivel()),
                    String.valueOf(duelistas[3].getFuerza()), String.valueOf(duelistas[3].getArmadura()),
                    String.valueOf(duelistas[3].getDestreza()), String.valueOf(duelistas[3].getConstitucion()));
        } else {
            pDuelo.setDuelista4(null, null, null, null, null, null, null);
        }
    }
}
