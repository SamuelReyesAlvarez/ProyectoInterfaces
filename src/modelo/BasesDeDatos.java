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
 */
public class BasesDeDatos {

    private static final String CARPETA_PARTIDAS = "partidas/"; // carpeta
    private static final String TERMINACION_FICHERO_BD = ".oo"; // extensión base de datos

    private ObjectContainer conexionBD;

    public BasesDeDatos(String nombre) {
        // abre la conexion con la base de datos estableciendo la configuracion previa de guardado en cascada
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(FlujoJuego.class).cascadeOnUpdate(true);
        conexionBD = Db4oEmbedded.openFile(config, CARPETA_PARTIDAS + nombre.toUpperCase() + TERMINACION_FICHERO_BD);
    }

    /**
     * BASE DE DATOS
     */
    public FlujoJuego guardarDatos(FlujoJuego flujo) throws IOException, JuegoException {
        // // uso esto porque no se sobrescribe el objeto, guarda como nuevo
        // conexionBD.delete(new FlujoJuego());

        // guarda la partida
        conexionBD.store(flujo);

        // guarda en un fichero los datos para la tabla de partidas
        new FicheroRegistros().guardarRegistros(flujo);

        return cargarDatos(flujo.getJugador().getNombre());
    }

    public FlujoJuego cargarDatos(String nombre) throws JuegoException {
        // busca la partida almacenada en la base de datos
        ObjectSet<FlujoJuego> cargado = conexionBD.queryByExample(new FlujoJuego());
        FlujoJuego flujoCargado = null;

        // se crea una base de datos para cada partida para mejor acceso a un Flujo de Juego
        if (cargado.size() != 1) {
            throw new JuegoException("No hay datos o están duplicados");
        }

        // obtiene los datos de la partida
        flujoCargado = cargado.next();

        // devuelve la partida para continuarla
        return flujoCargado;
    }

    public void eliminarDatos(String nombre) throws JuegoException, IOException {
        File ficheroBD = new File(CARPETA_PARTIDAS + nombre.toUpperCase() + TERMINACION_FICHERO_BD);

        // elimina la base de datos que contiene la partida
        if (!ficheroBD.delete()) {
            throw new JuegoException("No se pudo borrar la partida");
        }

        // elimina el registro de los datos clave del fichero binario
        new FicheroRegistros().eliminarRegistro(nombre);
    }

    public void cerrarConexion() {
        // cierra la conexion con la base de datos
        conexionBD.close();
    }
}
