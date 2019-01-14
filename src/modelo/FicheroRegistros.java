package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Samuel Reyes
 *
 * Gestiona el tratamiento del fichero que guarda los datos basicos de cada
 * partida
 *
 */
public class FicheroRegistros {

    // Localizacion del fichero
    private static final String FICHERO_REGISTROS = "archivos/registros.dat";
    // Formato para cambiar de Date a String
    private static final String FORMATO_FECHA = "MM-dd HH:mm";
    // Formato para longitud de nombre
    private static final String FORMATO_NOMBRE = "%15s";
    // Formato para longitud de posicion y total participantes
    private static final String FORMATO_POSICION_TOTAL = "%7s";
    // (UNICODE) 22 de fecha + 30 de nombre + 4 de turno + 14 de posicion/totalParticipantes
    private static final int LONGITUD_REGISTRO = 70;
    // Valores necesarios para tratar el fichero
    private static final int LONGITUD_FECHA = 11;
    private static final int LONGITUD_NOMBRE = 15;
    private static final int LONGITUD_POSICION_TOTAL = 7;
    private static final int CAMPOS_REGISTRO = 4;

    // Constructor
    public FicheroRegistros() {
    }

    // Actualiza o incluye registros
    protected void guardarRegistros(FlujoJuego flujo) throws IOException {
        // Datos a guardar (en el orden: fecha, nombre, turno, posTotal)
        String fecha = new SimpleDateFormat(FORMATO_FECHA).format(new Date());
        String nombre = String.format(FORMATO_NOMBRE, flujo.getJugador().getNombre());
        int turno = flujo.getTurnoDeJuego();
        int posicion = flujo.crearClasificacion().indexOf(flujo.getJugador()) + 1;
        int totalParticipantes = flujo.getCompetencia().size() + 1;
        String posTotal = String.format(FORMATO_POSICION_TOTAL, (posicion + "/" + totalParticipantes));
        int registro = 0;

        // Comprueba si ya existe un registro con el nombre del jugador
        registro = comprobarRegistros(registro, nombre);

        // Escribe los datos del registro
        escribirRegistro(registro, fecha, nombre, turno, posTotal);
    }

    // Ayuda en la gestion de los registros
    private int comprobarRegistros(int registro, String nombre) throws IOException {
        // Abre el fichero con acceso aleatorio en modo lectura
        RandomAccessFile acceso = new RandomAccessFile(FICHERO_REGISTROS, "r");

        // Variables locales para tratar el fichero
        StringBuilder sb;
        boolean encontrado = false;
        int totalRegistros = (int) acceso.length() / LONGITUD_REGISTRO;

        // Comprueba si ya existe un registro con el mismo nombre
        while (!encontrado && registro < totalRegistros) {
            sb = new StringBuilder();
            // Situa el cursor de lectura al comienzo del campo de Nombre en cada registro
            acceso.seek((registro * LONGITUD_REGISTRO) + (LONGITUD_FECHA * 2));

            // Obtiene los caracteres que componen el nombre
            for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                sb.append(acceso.readChar());
            }

            // Si existe, devolvera la posicion del registro encontrado
            if ((sb.toString().trim()).equalsIgnoreCase(nombre.trim())) {
                encontrado = true;
            } else {
                // Si no lo encuentra, devolvera la posicion para el nuevo registro
                registro++;
            }
        }

        // Cierra el fichero de los registros
        acceso.close();

        return registro;
    }

    // Ayuda en la actualizacion o escritura de registros
    private void escribirRegistro(int registro, String fecha, String nombre, int turno, String posTotal) throws IOException {
        // Abre el fichero con acceso aleatorio en modo escritura
        RandomAccessFile acceso = new RandomAccessFile(FICHERO_REGISTROS, "rw");

        // Coloca el cursor al comienzo del registro para escribir los datos
        acceso.seek(registro * LONGITUD_REGISTRO);

        // Escribe la fecha
        acceso.writeChars(fecha);
        // Escribe el nombre
        acceso.writeChars(nombre);
        // Escribe el turno
        acceso.writeInt(turno);
        // Escribe la posicion y el total de participantes
        acceso.writeChars(posTotal);

        // Comprueba si la longitud total del fichero es multiplo de la longitud
        // de registro definida
        if (acceso.length() % LONGITUD_REGISTRO != 0) {
            JOptionPane.showMessageDialog(null, "El registro no se ha guardado correctamente");
        }

        // Cierra el fichero de los registros
        acceso.close();
    }

    // Almacena todos los datos del fichero en una matriz para rellenar la tabla
    // de partidas guardadas
    public Object[][] cargarRegistros() throws IOException {
        File fichero = new File(FICHERO_REGISTROS);

        // Crea el fichero binario si no existe
        if (!fichero.isFile()) {
            fichero.createNewFile();
        }

        // Almacena los datos de los registros en una matriz que será enviada al
        // Model de JTable
        Object[][] registros = new Object[(int) fichero.length() / LONGITUD_REGISTRO][CAMPOS_REGISTRO];
        int fila = 0;
        int columna;

        // Abrir fichero en modo secuencial (leer todo)
        DataInputStream dis = new DataInputStream(new FileInputStream(FICHERO_REGISTROS));
        StringBuilder sb = new StringBuilder();
        boolean finFichero = false;

        // Lee y carga los datos del fichero en la matriz
        while (!finFichero) {
            try {
                columna = 0;
                // Lee la fecha
                for (int j = 0; j < LONGITUD_FECHA; j++) {
                    sb.append(dis.readChar());
                }
                registros[fila][columna] = sb.toString().trim();
                sb = new StringBuilder();
                columna++;
                // Lee el nombre
                for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                    sb.append(dis.readChar());
                }
                registros[fila][columna] = sb.toString().trim();
                sb = new StringBuilder();
                columna++;
                // Lee el turno
                registros[fila][columna] = dis.readInt();
                columna++;
                // Lee la posicion y el total de participantes
                for (int i = 0; i < LONGITUD_POSICION_TOTAL; i++) {
                    sb.append(dis.readChar());
                }
                registros[fila][columna] = sb.toString().trim();
                sb = new StringBuilder();
                fila++;
            } catch (EOFException e) {
                finFichero = true;
            }
        }

        // Cierra el fichero de registros
        dis.close();

        return registros;
    }

    // Ayuda a borrar un registro concreto
    // Para ello traspasa los datos del fichero original a una copia sin el
    // registro seleccionado paraa borrar
    protected void eliminarRegistro(String nombre) throws IOException, JuegoException {
        // Para evitar problemas de nombres de fichero, se obtienen todos los
        // datos del original, se elimina el fichero y se crea uno identico con
        // los nuevos datos
        Object[][] registros = cargarRegistros();
        int numReg = comprobarRegistros(0, nombre);

        // Controla las excepciones al borrar y crear ficheros
        File ficheroReg = new File(FICHERO_REGISTROS);
        if (!ficheroReg.delete()) {
            throw new JuegoException("Error al borrar fichero de registros");
        }
        if (!ficheroReg.createNewFile()) {
            throw new JuegoException("Error al crear nuevo fichero de registros");
        }

        // Se abre el fichero binario para escritura secuencial
        DataOutputStream escribirFichero = new DataOutputStream(new FileOutputStream(FICHERO_REGISTROS));

        // Escribe los registros anteriores al que se quiere borrar
        for (int i = 0; i < numReg; i++) {
            escribirRegistro(i, (String) registros[i][0], String.format(FORMATO_NOMBRE, (String) registros[i][1]), (int) registros[i][2], String.format(FORMATO_POSICION_TOTAL, registros[i][3]));
        }

        // Saltamos el registro a eliminar y continuamos escribiendo los posteriores
        for (int i = numReg + 1; i < registros.length; i++) {
            escribirRegistro(i - 1, (String) registros[i][0], String.format(FORMATO_NOMBRE, (String) registros[i][1]), (int) registros[i][2], String.format(FORMATO_POSICION_TOTAL, registros[i][3]));
        }

        // Cierra el fichero de registros
        escribirFichero.close();
    }
}
