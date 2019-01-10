package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Equipamiento;
import modelo.FlujoJuego;
import modelo.JuegoException;
import modelo.Jugador;
import vista.panel.PanelBazar;
import vista.panel.PanelClasificacion;
import vista.panel.PanelMision;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 */
public class ControladorTareas extends MouseAdapter implements ActionListener {

    private VentanaJuego vJuego;
    private PanelMision pMision;
    private PanelBazar pBazar;
    private PanelClasificacion pClasificacion;
    private ControladorJuego ctrJuego;
    private FlujoJuego flujoJuego;

    public ControladorTareas(VentanaJuego vJ, JPanel panelActivo, FlujoJuego flujo, ControladorJuego ctrJ) {
        vJuego = vJ;
        ctrJuego = ctrJ;
        flujoJuego = flujo;

        if (panelActivo instanceof PanelMision) {
            pMision = (PanelMision) panelActivo;
            actualizarPMision();
        } else if (panelActivo instanceof PanelBazar) {
            pBazar = (PanelBazar) panelActivo;
            actualizarPBazar();
        } else if (panelActivo instanceof PanelClasificacion) {
            pClasificacion = (PanelClasificacion) panelActivo;
            actualizarPClasificacion();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case "aceptarMision":
                    if (pMision.getSeleccionado() < 0) {
                        throw new JuegoException("Primero debes seleccionar una mision");
                    }
                    if (flujoJuego.getJugador().getMisionActiva() != null) {
                        JOptionPane.showMessageDialog(vJuego, "Ya tienes una mision activa");
                    } else {
                        flujoJuego.getJugador().setMisionActiva(flujoJuego.getMisiones().get(pMision.getSeleccionado()));
                        flujoJuego.getMisiones().remove(pMision.getSeleccionado());
                        actualizarPMision();
                        ctrJuego.actualizarPJuego();
                    }
                    break;
                case "aceptarCompra":
                    if (pBazar.getSeleccionado() < 0) {
                        throw new JuegoException("Primero debes seleccionar un articulo");
                    }
                    Equipamiento equipo = flujoJuego.getMercado().get(pBazar.getSeleccionado());
                    if (JOptionPane.showConfirmDialog(vJuego, "¿Deseas comprar " + equipo.toString() + " por " + equipo.getPrecio() + " monedas de oro?") == JOptionPane.YES_OPTION) {
                        flujoJuego.compraEnMercado(flujoJuego.getJugador(), equipo);
                        JOptionPane.showMessageDialog(vJuego, "Has comprado " + equipo.toString() + " por " + equipo.getPrecio() + " monedas de oro");
                    }
                    actualizarPBazar();
                    ctrJuego.actualizarPJuego();
                    break;
            }
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(vJuego, ex.getMessage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //StringBuilder estado = new StringBuilder();
        Jugador seleccion;

        if (pClasificacion.getTabla().getSelectedRow() > -1) {
            seleccion = flujoJuego.crearClasificacion().get(pClasificacion.getTabla().getSelectedRow());
            /* // Datos basicos del jugador
            estado.append(seleccion.getNombre());
            estado.append("\nVida: " + seleccion.getVidaActual() + "/" + seleccion.getVidaMax());
            estado.append("\nExperiencia: " + seleccion.getExpAcumulada() + "/" + seleccion.getExpSigNivel());
            estado.append("\nNivel: " + seleccion.getNivel());
            estado.append("\nFuerza: " + seleccion.getFuerza());
            estado.append("\nArmadura: " + seleccion.getArmadura());
            estado.append("\nDestreza: " + seleccion.getDestreza());
            estado.append("\nConstitucion: " + seleccion.getConstitucion());
            JOptionPane.showMessageDialog(vJuego, estado);
             */

            // Datos completos del jugador
            JOptionPane.showMessageDialog(vJuego, seleccion.toString()); // <<para debug>>
        }
    }

    private void actualizarPMision() {
        String[] misiones;

        if (flujoJuego.getJugador().getMisionActiva() == null) {
            misiones = new String[flujoJuego.getMisiones().size()];

            for (int i = 0; i < misiones.length; i++) {
                misiones[i] = "Duracion: " + flujoJuego.getMisiones().get(i).getDuracion()
                        + " Recompensa: " + (flujoJuego.getMisiones().get(i).getRecompensa() * flujoJuego.getJugador().getNivel())
                        + " " + flujoJuego.getMisiones().get(i).getTitulo();
            }

            pMision.setEtTitulo("Misiones disponibles");
            pMision.setBtnDisponible(true);
        } else {
            misiones = new String[1];
            misiones[0] = "Duracion: " + flujoJuego.getJugador().getMisionActiva().getDuracion()
                    + " Recompensa: " + (flujoJuego.getJugador().getMisionActiva().getRecompensa() * flujoJuego.getJugador().getNivel())
                    + " " + flujoJuego.getJugador().getMisionActiva().getTitulo();

            pMision.setEtTitulo("Mision en curso");
            pMision.setBtnDisponible(false);
        }

        pMision.setMisiones(misiones);
    }

    private void actualizarPBazar() {
        String[] articulos = new String[flujoJuego.getMercado().size()];

        for (int i = 0; i < articulos.length; i++) {
            articulos[i] = flujoJuego.getMercado().get(i).getTipo().toString()
                    + "\nNivel: " + flujoJuego.getMercado().get(i).getNivel()
                    + "\tPotenciado: " + flujoJuego.getMercado().get(i).getPotenciado()
                    + "\tPrecio: " + flujoJuego.getMercado().get(i).getPrecio();
        }

        pBazar.setArticulos(articulos);
    }

    private void actualizarPClasificacion() {
        LinkedList<Jugador> clasificacion = flujoJuego.crearClasificacion();
        Object[][] datos = new Object[clasificacion.size()][pClasificacion.getTabla().getModel().getColumnCount()];

        int fila = 0;
        int columna;

        while (fila < clasificacion.size()) {
            columna = 0;
            datos[fila][columna] = fila + 1;

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getNombre();

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getNivel();

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getCombates()[0]
                    + clasificacion.get(fila).getCombates()[1]
                    + clasificacion.get(fila).getCombates()[2];

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getCombates()[0];

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getCombates()[1];

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getCombates()[2];

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getMisiones()[0];

            columna++;
            datos[fila][columna] = clasificacion.get(fila).getMisiones()[1];

            fila++;
        }

        pClasificacion.setDatosTabla(datos, flujoJuego.getJugador().getNombre());
    }
}
