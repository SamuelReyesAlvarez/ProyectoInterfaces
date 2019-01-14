package modelo;

import java.util.Random;

/**
 *
 * @author Samuel Reyes
 *
 * Clase que define cada mision creada
 *
 */
public class Mision {

    // Valores predefinidos para la gestion de la clase
    private static final int RECOMPENSA_POR_TURNO = 100;
    private static final int DURACION_MAXIMA = 5;
    private static final int DURACION_MINIMA = 2;

    // Atributos que definen cada mision
    private String titulo;
    private int duracion;
    private int recompensa;

    // Constructor
    public Mision(String titulo) {
        this.titulo = titulo;
        setRecompensaYDuracion();
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getRecompensa() {
        return recompensa;
    }

    // Establece la duracion y recompensa para la mision creada
    private void setRecompensaYDuracion() {
        Random r = new Random();

        // La duracion se establece de forma aleatoria dentro de un rango
        duracion = r.nextInt(DURACION_MAXIMA) + DURACION_MINIMA;
        // La recompensa se establece en base a la duracion asignada
        // La recompensa real es igual a la recompensa establecida multiplicada
        // por el nivel del jugador que la completa
        recompensa = (duracion * RECOMPENSA_POR_TURNO) + (((int) Math.pow(duracion, 2) * RECOMPENSA_POR_TURNO) / 10);
    }

    // Controla el tiempo de duracion de la mision
    protected void disminuirDuracion() throws JuegoException {
        duracion--;

        // Cuando la mision finalice se informa al jugador
        if (duracion < 1) {
            throw new JuegoException("La mision se ha completado");
        }
    }

    @Override
    public String toString() {
        return titulo + "\nduracion=" + duracion + " recompensa=" + recompensa + '}';
    }
}
