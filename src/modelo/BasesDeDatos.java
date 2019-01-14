package modelo;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Samuel Reyes
 *
 * Gestiona el tratamiento de la base de datos DB4O
 *
 */
public class BasesDeDatos {

    // Datos necesarios para la base de datos
    private static final String CARPETA_PARTIDAS = "partidas/"; // carpeta
    private static final String TERMINACION_FICHERO_BD = ".oo"; // extensión base de datos

    // Objeto que ayuda en la gestion de la base de datos
    private ObjectContainer conexionBD;

    // Constructor
    public BasesDeDatos(String nombre) {
        // Abre la conexion con la base de datos estableciendo la configuracion previa de guardado en cascada
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(FlujoJuego.class).cascadeOnUpdate(true);
        conexionBD = Db4oEmbedded.openFile(config, CARPETA_PARTIDAS + nombre.toUpperCase() + TERMINACION_FICHERO_BD);
    }

    /**
     * BASE DE DATOS
     */
    public FlujoJuego guardarDatos(FlujoJuego flujo) throws IOException, JuegoException {
        // Guarda la partida en la base de datos
        conexionBD.store(flujo);

        // Guarda en un fichero los datos para la tabla de partidas
        new FicheroRegistros().guardarRegistros(flujo);

        // Devuelve la referencia de la partida en la base de datos para
        // evitar duplicar datos
        return cargarDatos(flujo.getJugador().getNombre());
    }

    public FlujoJuego cargarDatos(String nombre) throws JuegoException {
        // Busca la partida almacenada en la base de datos (necesita la referencia)
        ObjectSet<FlujoJuego> cargado = conexionBD.queryByExample(new FlujoJuego());
        FlujoJuego flujoCargado = null;

        // Se crea una base de datos para cada partida para mejor acceso a un
        // Flujo de Juego concreto
        if (cargado.size() != 1) {
            throw new JuegoException("No hay datos o están duplicados");
        }

        // Obtiene los datos de la partida
        flujoCargado = cargado.next();

        // Devuelve la partida al Controlador del Juego para continuarla
        return flujoCargado;
    }

    public void eliminarDatos(String nombre) throws JuegoException, IOException {
        // Se necesita un objeto de tipo Filo para borrar la base de datos
        File ficheroBD = new File(CARPETA_PARTIDAS + nombre.toUpperCase() + TERMINACION_FICHERO_BD);

        // Elimina la base de datos que contiene la partida
        if (!ficheroBD.delete()) {
            throw new JuegoException("No se pudo borrar la partida");
        }

        // Elimina el registro de los datos clave en el fichero binario de
        // partidas guardadas
        new FicheroRegistros().eliminarRegistro(nombre);
    }

    public void cerrarConexion() {
        // Cierra la conexion con la base de datos actual
        conexionBD.close();
    }
}
