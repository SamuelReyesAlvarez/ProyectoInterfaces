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
 */
public class FicheroRegistros {

    private static final String FICHERO_REGISTROS = "archivos/registros.dat"; // fichero para registro de datos clave
    private static final String FORMATO_FECHA = "MM-dd HH:mm"; // formato para cambiar de Date a String
    private static final String FORMATO_NOMBRE = "%15s"; // formato para longitud de nombre
    private static final String FORMATO_POSICION_TOTAL = "%7s"; // formato para longitud de posicion y total participantes
    private static final int LONGITUD_REGISTRO = 70; // (UNICODE) 22 de fecha + 30 de nombre + 4 de turno + 14 de posicion/totalParticipantes
    private static final int LONGITUD_FECHA = 11;
    private static final int LONGITUD_NOMBRE = 15;
    private static final int LONGITUD_POSICION_TOTAL = 7;
    private static final int CAMPOS_REGISTRO = 4;

    public FicheroRegistros() {
    }

    protected void guardarRegistros(FlujoJuego flujo) throws IOException {
        // datos a guardar (en el orden: fecha, nombre, turno, posTotal)
        String fecha = new SimpleDateFormat(FORMATO_FECHA).format(new Date());
        String nombre = String.format(FORMATO_NOMBRE, flujo.getJugador().getNombre());
        int turno = flujo.getTurnoDeJuego();
        int posicion = flujo.crearClasificacion().indexOf(flujo.getJugador()) + 1;
        int totalParticipantes = flujo.getCompetencia().size() + 1;
        String posTotal = String.format(FORMATO_POSICION_TOTAL, (posicion + "/" + totalParticipantes));
        int registro = 0;

        // comprueba si ya existe un registro con el nombre del jugador
        registro = comprobarRegistros(registro, nombre);

        // escribe los datos del registro
        escribirRegistro(registro, fecha, nombre, turno, posTotal);
    }

    private int comprobarRegistros(int registro, String nombre) throws IOException {
        // abre el fichero con acceso aleatorio en modo lectura
        RandomAccessFile acceso = new RandomAccessFile(FICHERO_REGISTROS, "r");

        // para tratar el fichero
        StringBuilder sb;
        boolean encontrado = false;
        int totalRegistros = (int) acceso.length() / LONGITUD_REGISTRO;

        // comprueba si ya existe un registro con el mismo nombre
        while (!encontrado && registro < totalRegistros) {
            sb = new StringBuilder();
            acceso.seek((registro * LONGITUD_REGISTRO) + (LONGITUD_FECHA * 2));

            for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                sb.append(acceso.readChar());
            }

            // si existe, devolvera la posicion del registro encontrado
            if ((sb.toString().trim()).equalsIgnoreCase(nombre.trim())) {
                encontrado = true;
            } else {
                // si no lo encuentra, devolvera la posicion para el nuevo registro
                registro++;
            }
        }

        // cierra el fichero de los registros
        acceso.close();

        return registro;
    }

    private void escribirRegistro(int registro, String fecha, String nombre, int turno, String posTotal) throws IOException {
        // abre el fichero con acceso aleatorio en modo escritura
        RandomAccessFile acceso = new RandomAccessFile(FICHERO_REGISTROS, "rw");

        // coloca el puntero para escribir los datos
        acceso.seek(registro * LONGITUD_REGISTRO);

        // escribe la fecha
        acceso.writeChars(fecha);
        // escribe el nombre
        acceso.writeChars(nombre);
        // escribe el turno
        acceso.writeInt(turno);
        // escribe la posicion y el total de participantes
        acceso.writeChars(posTotal);

        if (acceso.length() % LONGITUD_REGISTRO != 0) {
            JOptionPane.showMessageDialog(null, "El registro no se ha guardado correctamente");
        }

        // cierra el fichero de los registros
        acceso.close();
    }

    public Object[][] cargarRegistros() throws IOException {
        File fichero = new File(FICHERO_REGISTROS);

        // crea el fichero binario si no existe
        if (!fichero.isFile()) {
            fichero.createNewFile();
        }

        // almacena los datos de los registros en una matriz que será enviada al Model de JTable
        Object[][] registros = new Object[(int) fichero.length() / LONGITUD_REGISTRO][CAMPOS_REGISTRO];
        int fila = 0;
        int columna;

        // abrir fichero en modo secuencial (leer todo)
        DataInputStream dis = new DataInputStream(new FileInputStream(FICHERO_REGISTROS));
        StringBuilder sb = new StringBuilder();
        boolean finFichero = false;

        // lee y carga los datos del fichero en la matriz
        while (!finFichero) {
            try {
                columna = 0;
                // lee la fecha
                for (int j = 0; j < LONGITUD_FECHA; j++) {
                    sb.append(dis.readChar());
                }
                registros[fila][columna] = sb.toString().trim();
                sb = new StringBuilder();
                columna++;
                // lee el nombre
                for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                    sb.append(dis.readChar());
                }
                registros[fila][columna] = sb.toString().trim();
                sb = new StringBuilder();
                columna++;
                // lee el turno
                registros[fila][columna] = dis.readInt();
                columna++;
                // lee la posicion y el total de participantes
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

        // cerrar fichero de registros
        dis.close();

        return registros;
    }

    protected void eliminarRegistro(String nombre) throws IOException, JuegoException {
        // como se no puede eliminar un registro de un fichero binario,
        // se creara una copia del original sin el registro a eliminar
        Object[][] registros = cargarRegistros();
        int numReg = comprobarRegistros(0, nombre);

        // control de excepciones al borrar y crear ficheros
        File ficheroReg = new File(FICHERO_REGISTROS);
        if (!ficheroReg.delete()) {
            throw new JuegoException("Error al borrar fichero de registros");
        }
        if (!ficheroReg.createNewFile()) {
            throw new JuegoException("Error al crear nuevo fichero de registros");
        }

        // se abre el fichero binario para escritura secuencial
        DataOutputStream escribirFichero = new DataOutputStream(new FileOutputStream(FICHERO_REGISTROS));

        // escribe los registros anteriores al que se quiere borrar
        for (int i = 0; i < numReg; i++) {
            escribirRegistro(i, (String) registros[i][0], String.format(FORMATO_NOMBRE, (String) registros[i][1]), (int) registros[i][2], String.format(FORMATO_POSICION_TOTAL, registros[i][3]));
        }

        // saltamos el registro a eliminar y continuamos escribiendo los posteriores
        for (int i = numReg + 1; i < registros.length; i++) {
            escribirRegistro(i - 1, (String) registros[i][0], String.format(FORMATO_NOMBRE, (String) registros[i][1]), (int) registros[i][2], String.format(FORMATO_POSICION_TOTAL, registros[i][3]));
        }

        // cerrar fichero registros
        escribirFichero.close();
    }
}
