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
 * Gestiona las funciones disponibles en los paneles de Mision, Bazar y
 * Clasificacion
 *
 */
public class ControladorTareas extends MouseAdapter implements ActionListener {

    // Objetos necesarios para la gestion de la ventana
    private VentanaJuego vJuego;
    private PanelMision pMision;
    private PanelBazar pBazar;
    private PanelClasificacion pClasificacion;
    private ControladorJuego ctrJuego;
    private FlujoJuego flujoJuego;

    // Constructor
    public ControladorTareas(VentanaJuego vJ, JPanel panelActivo, FlujoJuego flujo, ControladorJuego ctrJ) {
        vJuego = vJ;
        ctrJuego = ctrJ;
        flujoJuego = flujo;

        // Comprueba que panel se esta activando
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

    // Controla las funciones definidas en los componentes que poseen un
    // escuchador de eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Comprueba que componente recibio el evento y ejecuta su funcion
            switch (e.getActionCommand()) {
                case "aceptarMision":
                    // Controla que el usuario haya seleccionado una mision y la
                    // establece como "en curso"
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
                    // Controla que el usuario haya seleccionado un objeto del
                    // bazar y pregunta si acepta comprarlo tras mostrar datos
                    // referentes al mismo
                    if (pBazar.getSeleccionado() < 0) {
                        throw new JuegoException("Primero debes seleccionar un articulo");
                    }
                    Equipamiento equipo = flujoJuego.getMercado().get(pBazar.getSeleccionado());
                    if (JOptionPane.showConfirmDialog(vJuego, "¿Deseas comprar " + equipo.toString() + " por " + (equipo.getPrecio() + (equipo.getPrecio() / flujoJuego.getTasaMercader())) + " monedas de oro?") == JOptionPane.YES_OPTION) {
                        flujoJuego.compraEnMercado(flujoJuego.getJugador(), equipo);
                        JOptionPane.showMessageDialog(vJuego, "Has comprado " + equipo.toString() + " por " + (equipo.getPrecio() + (equipo.getPrecio() / flujoJuego.getTasaMercader())) + " monedas de oro");
                    }
                    actualizarPBazar();
                    ctrJuego.actualizarPJuego();
                    break;
            }
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(vJuego, ex.getMessage());
        }
    }

    // Muestra informacion del jugador seleccionado en la tabla de clasificacion
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

    // Actualiza los datos mostrados en el panel de mision
    private void actualizarPMision() {
        String[] misiones;

        // Cambia el aspecto del panel Mision segun tenga mision activa o no el jugador
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

    // Actualiza la informacion mostrada en el panel de Bazar
    private void actualizarPBazar() {
        String[] articulos = new String[flujoJuego.getMercado().size()];

        for (int i = 0; i < articulos.length; i++) {
            articulos[i] = flujoJuego.getMercado().get(i).getTipo().toString()
                    + " Nivel: " + flujoJuego.getMercado().get(i).getNivel()
                    + " Potenciado: " + flujoJuego.getMercado().get(i).getPotenciado()
                    + " Precio: " + flujoJuego.getMercado().get(i).getPrecio();
        }

        pBazar.setArticulos(articulos);
    }

    // Actualiza los datos que contiene la tabla del panel de Clasificacion
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
