package modelo;

import java.util.Random;

/**
 *
 * @author Samuel Reyes
 *
 */
public class Mision {

    private static final int RECOMPENSA_POR_TURNO = 100;
    private static final int DURACION_MAXIMA = 5;
    private static final int DURACION_MINIMA = 2;

    private String titulo;
    private int duracion;
    private int recompensa;

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

    private void setRecompensaYDuracion() {
        Random r = new Random();

        duracion = r.nextInt(DURACION_MAXIMA) + DURACION_MINIMA;
        recompensa = (duracion * RECOMPENSA_POR_TURNO) + (((int) Math.pow(duracion, 2) * RECOMPENSA_POR_TURNO) / 10);
    }

    protected void disminuirDuracion() throws JuegoException {
        duracion--;

        if (duracion < 1) {
            throw new JuegoException("La mision se ha completado");
        }
    }

    @Override
    public String toString() {
        return titulo + "\nduracion=" + duracion + " recompensa=" + recompensa + '}';
    }
}
