package vista.panel;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Samuel Reyes
 *
 */
public class MenuJuego extends JMenuBar {

    private JMenu opciones, acciones;
    private JMenuItem ayuda, guardar, salirPartida, salirJuego, estado, duelo, mision, bazar, clasificacion;

    public MenuJuego() {
        opciones = new JMenu("Opciones");
        acciones = new JMenu("Acciones");
        ayuda = new JMenuItem("Ayuda de KnightFight");
        guardar = new JMenuItem("Guardar partida");
        salirPartida = new JMenuItem("Salir de la partida");
        salirJuego = new JMenuItem("Salir del juego");
        estado = new JMenuItem("Estado del jugador");
        duelo = new JMenuItem("Duelo");
        mision = new JMenuItem("Mision");
        bazar = new JMenuItem("Bazar");
        clasificacion = new JMenuItem("Clasificacion");

        acciones.setMnemonic('a');
        bazar.setMnemonic('b');
        clasificacion.setMnemonic('c');
        duelo.setMnemonic('d');
        estado.setMnemonic('e');
        guardar.setMnemonic('g');
        ayuda.setMnemonic('h');
        salirJuego.setMnemonic('j');
        mision.setMnemonic('m');
        opciones.setMnemonic('o');
        salirPartida.setMnemonic('p');

        opciones.add(ayuda);
        opciones.add(guardar);
        opciones.add(salirPartida);
        opciones.add(salirJuego);
        acciones.add(estado);
        acciones.add(duelo);
        acciones.add(mision);
        acciones.add(bazar);
        acciones.add(clasificacion);

        this.add(opciones);
        this.add(acciones);
    }

    public void addControlador(ActionListener al) {
        guardar.addActionListener(al);
        guardar.setActionCommand("guardar");
        salirPartida.addActionListener(al);
        salirPartida.setActionCommand("salirPartida");
        salirJuego.addActionListener(al);
        salirJuego.setActionCommand("salirJuego");
        estado.addActionListener(al);
        estado.setActionCommand("estado");
        duelo.addActionListener(al);
        duelo.setActionCommand("duelo");
        mision.addActionListener(al);
        mision.setActionCommand("mision");
        bazar.addActionListener(al);
        bazar.setActionCommand("bazar");
        clasificacion.addActionListener(al);
        clasificacion.setActionCommand("clasificacion");
    }

    public JMenuItem getAyuda() {
        return ayuda;
    }
}
