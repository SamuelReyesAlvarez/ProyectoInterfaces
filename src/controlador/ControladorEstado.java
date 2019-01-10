package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import modelo.Equipamiento;
import modelo.FlujoJuego;
import modelo.JuegoException;
import vista.panel.PanelEstado;
import vista.panel.PanelJuego;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 */
public class ControladorEstado implements ActionListener {

    private FlujoJuego flujoJuego;
    private VentanaJuego vJuego;
    private PanelJuego pJuego;
    private PanelEstado pEstado;
    private ControladorJuego ctrJuego;

    public ControladorEstado(VentanaJuego vJ, PanelJuego pJ, PanelEstado pE, FlujoJuego flujo, ControladorJuego ctrJ) {
        flujoJuego = flujo;
        vJuego = vJ;
        pJuego = pJ;
        pEstado = pE;
        ctrJuego = ctrJ;
        actualizarPEstado();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Equipamiento equipo = null;
        Equipamiento patronEquipo;
        JToggleButton equipoSeleccionado = null;

        if (pEstado.getSeleccionEquipado() != null) {
            equipoSeleccionado = pEstado.getSeleccionEquipado();
        } else if (pEstado.getSeleccionInventario() != null) {
            equipoSeleccionado = pEstado.getSeleccionInventario();
        }

        try {
            switch (e.getActionCommand()) {
                case "subirFuerza":
                    flujoJuego.getJugador().subirAtributo(0);
                    break;
                case "subirArmadura":
                    flujoJuego.getJugador().subirAtributo(1);
                    break;
                case "subirDestreza":
                    flujoJuego.getJugador().subirAtributo(2);
                    break;
                case "subirConstitucion":
                    flujoJuego.getJugador().subirAtributo(3);
                    break;
                case "equipar":
                    if (equipoSeleccionado.getToolTipText() != null) {
                        patronEquipo = new Equipamiento(Integer.parseInt(equipoSeleccionado.getToolTipText().toString().trim()));
                        equipo = flujoJuego.getJugador().getInventario().get(flujoJuego.getJugador().getInventario().indexOf(patronEquipo));
                        flujoJuego.getJugador().equiparDesequipar(equipo, true);
                    } else {
                        JOptionPane.showMessageDialog(vJuego, "Selecciona un objeto a equipar");
                    }
                    break;
                case "desequipar":
                    if (equipoSeleccionado.getToolTipText() != null) {
                        patronEquipo = new Equipamiento(Integer.parseInt(equipoSeleccionado.getToolTipText().toString().trim()));
                        for (Equipamiento equipado : flujoJuego.getJugador().getEquipado()) {
                            if (equipado != null) {
                                if (equipado.equals(patronEquipo)) {
                                    flujoJuego.getJugador().equiparDesequipar(equipado, false);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(vJuego, "Selecciona un objeto a desequipar");
                    }
                    break;
                case "potenciar":
                    if (equipoSeleccionado.getToolTipText() != null) {
                        patronEquipo = new Equipamiento(Integer.parseInt(equipoSeleccionado.getToolTipText().toString().trim()));
                        for (Equipamiento equipado : flujoJuego.getJugador().getEquipado()) {
                            if (equipado != null) {
                                if (equipado.equals(patronEquipo)) {
                                    equipo = equipado;
                                }
                            }
                        }
                        if (equipo == null) {
                            equipo = flujoJuego.getJugador().getInventario().get(flujoJuego.getJugador().getInventario().indexOf(patronEquipo));
                        }
                        if (JOptionPane.showConfirmDialog(vJuego, "¿Deseas potenciar " + equipo.toString() + " por " + equipo.getValorPotenciar() + " monedas de oro?", "Mensaje", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                            flujoJuego.getJugador().mejorarEquipo(equipo);
                        }
                    } else {
                        JOptionPane.showMessageDialog(vJuego, "Selecciona un objeto a potenciar");
                    }
                    break;
                case "vender":
                    if (equipoSeleccionado.getToolTipText() != null) {
                        patronEquipo = new Equipamiento(Integer.parseInt(equipoSeleccionado.getToolTipText().toString().trim()));
                        equipo = flujoJuego.getJugador().getInventario().get(flujoJuego.getJugador().getInventario().indexOf(patronEquipo));
                        if (JOptionPane.showConfirmDialog(vJuego, "¿Deseas vender " + equipo.toString() + " por " + (equipo.getValor() / flujoJuego.getJugador().getTasaVentaDirecta()) + " monedas de oro?", "Mensaje", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                            flujoJuego.getJugador().venderEquipo(equipo);
                        }
                    } else {
                        JOptionPane.showMessageDialog(vJuego, "Selecciona un objeto a vender");
                    }
                    break;
            }

            pEstado = new PanelEstado();
            pEstado.addControlador(this);
            pJuego.setPdCentro(pEstado);
            actualizarPEstado();
            ctrJuego.actualizarPJuego();
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(vJuego, ex.getMessage());
        }
    }

    protected void actualizarPEstado() {
        pEstado.setAtaqueOcasionado(String.valueOf(flujoJuego.getJugador().getTotalAtaDef()[0]));
        pEstado.setCombatesTotal(calcularTotal(flujoJuego.getJugador().getCombates()));
        pEstado.setAtaqueRecibido(String.valueOf(flujoJuego.getJugador().getTotalAtaDef()[1]));
        pEstado.setDerrotasTotal(String.valueOf(flujoJuego.getJugador().getCombates()[2]));
        pEstado.setEmpatesTotal(String.valueOf(flujoJuego.getJugador().getCombates()[1]));
        pEstado.setMisionesTotal(String.valueOf(flujoJuego.getJugador().getMisiones()[0]));
        pEstado.setOroTotal(String.valueOf(flujoJuego.getJugador().getMisiones()[1]));
        pEstado.setPbArmadura(comprobarMaximo(), flujoJuego.getJugador().getArmadura());
        pEstado.setPbConstitucion(comprobarMaximo(), flujoJuego.getJugador().getConstitucion());
        pEstado.setPbDestreza(comprobarMaximo(), flujoJuego.getJugador().getDestreza());
        pEstado.setPbFuerza(comprobarMaximo(), flujoJuego.getJugador().getFuerza());
        pEstado.setVictoriasTotal(String.valueOf(flujoJuego.getJugador().getCombates()[0]));
        pEstado.setAtributosSinUsar(String.valueOf(flujoJuego.getJugador().getPuntosAtrSinUsar()));
        rellenarEquipado();
        rellenarInventario();
    }

    private String calcularTotal(int[] array) {
        int total = 0;
        for (int cantidad : array) {
            total += cantidad;
        }
        return String.valueOf(total);
    }

    private int comprobarMaximo() {
        int maximo = Integer.MIN_VALUE;
        for (int indice = 0; indice < flujoJuego.getJugador().getAtributosJugador().length; indice++) {
            maximo = Math.max(maximo, (flujoJuego.getJugador().getAtributosJugador()[indice] + flujoJuego.getJugador().getAtributosEquipo()[indice]));
        }
        return maximo;
    }

    private void rellenarEquipado() {
        for (int indice = 0; indice < flujoJuego.getJugador().getEquipado().length; indice++) {
            if (flujoJuego.getJugador().getEquipado()[indice] != null) {
                pEstado.setEquipado(indice, flujoJuego.getJugador().getEquipado()[indice].textoBoton(),
                        String.valueOf(flujoJuego.getJugador().getEquipado()[indice].getIdentificador()));
            } else {
                pEstado.setEquipado(indice, null, null);
            }
        }
    }

    private void rellenarInventario() {
        int indice = 0;
        while (indice < flujoJuego.getJugador().getInventario().size()) {
            pEstado.setInventario(indice, flujoJuego.getJugador().getInventario().get(indice).textoBoton(),
                    String.valueOf(flujoJuego.getJugador().getInventario().get(indice).getIdentificador()));
            indice++;
        }
        while (indice < flujoJuego.getJugador().getTamanioMaxInventario()) {
            pEstado.setInventario(indice, null, null);
            indice++;
        }
    }
}
