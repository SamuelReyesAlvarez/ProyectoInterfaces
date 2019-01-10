package principal;

import controlador.ControladorEntrada;
import controlador.ControladorInicio;
import controlador.ControladorJuego;
import modelo.BasesDeDatos;
import modelo.FlujoJuego;
import vista.panel.PanelEntrada;
import vista.ventana.VentanaCarga;
import vista.ventana.VentanaEntrada;
import vista.ventana.VentanaInicio;
import vista.ventana.VentanaJuego;

/**
 *
 * @author Samuel Reyes
 *
 */
public class Principal {

    private static final String RUTA_ICONO = "src/imagenes/KnightFightLogoAplicacion.png";

    private static FlujoJuego flujo;
    private static BasesDeDatos bd;
    private static String nombre;
    private static boolean nueva;

    public static void main(String[] args) {
        // Video presentacion
        VentanaInicio vInicio = new VentanaInicio();
        ControladorInicio ctrInicio = new ControladorInicio(vInicio);
        vInicio.addControlador(ctrInicio);

        do {
            // Menu de entrada
            VentanaEntrada vEntrada = new VentanaEntrada(RUTA_ICONO);
            PanelEntrada pEntrada = new PanelEntrada();
            ControladorEntrada ctrEntrada = new ControladorEntrada(vEntrada, pEntrada);
            pEntrada.addControlador(ctrEntrada);
            vEntrada.setContentPane(pEntrada);

            while (ctrEntrada.getBD() == null || ctrEntrada.getNombre() == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    // espera
                }
            }

            nombre = ctrEntrada.getNombre();
            bd = ctrEntrada.getBD();
            nueva = ctrEntrada.getNueva();

            // Carga de datos e inicio del juego
            VentanaCarga vCarga = new VentanaCarga(RUTA_ICONO);
            VentanaJuego vJuego = new VentanaJuego(RUTA_ICONO);

            ctrEntrada.iniciarCarga(vCarga, vJuego, bd, nombre, nueva);
            flujo = ctrEntrada.getFlujo();

            ControladorJuego ctrJuego = new ControladorJuego(bd, vJuego, flujo);

            while (bd != null) {
                try {
                    bd = ctrJuego.getBD();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    // espera
                }
            }
        } while (true);
    }
}
