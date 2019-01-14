package modelo;

/**
 *
 * @author Samuel Reyes
 *
 * Se necesita de un lanzador de excepciones propio para la transimision de
 * mensajes importantes entre el modelo y el controlador
 *
 */
public class JuegoException extends Exception {

    public JuegoException(String mensaje) {
        super(mensaje);
    }
}
