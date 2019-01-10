package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Samuel Reyes
 *
 */
public class FlujoJuego {

    private static final String FICHERO_NOMBRES = "archivos/nombres.txt";
    private static final String FICHERO_MISIONES = "archivos/misiones.txt";
    private static final int CANTIDAD_COMPETENCIA = 99;
    private static final int CANTIDAD_MISIONES = 30;
    private static final int MAXIMO_DUELISTAS = 4;
    private static final int RANGO_MAXIMO_DUELO = 5;
    private static final int MAX_VUELTAS_BUSCAR_DUELISTA = 15;
    private static final int RANGO_FREC_MISION = 15;
    private static final int FREC_MAQUINA_HACE_MISION = 3;
    private static final int MAX_TURNOS_COMBATE = 5;
    private static final int PORCENTAJE_VIDA_MIN_COMBATIR = 20;
    private static final int RANGO_PORCENTAJE_COMBATIR = 30;
    private static final int TASA_MERCADER = 10;
    private static final int RANGO_FREC_VENTA = 5;

    private Jugador jugador;
    private HashSet<Jugador> competencia;
    private LinkedList<Mision> misiones;
    private LinkedList<Equipamiento> mercado;
    private int turnoDeJuego;

    // constructor base
    public FlujoJuego() {
    }

    // constructor para patron
    public FlujoJuego(String nombre) throws JuegoException {
        jugador = new Jugador(nombre);
    }

    public Jugador getJugador() {
        return jugador;
    }

    public HashSet<Jugador> getCompetencia() {
        return competencia;
    }

    public LinkedList<Mision> getMisiones() {
        return misiones;
    }

    public LinkedList<Equipamiento> getMercado() {
        return mercado;
    }

    public int getTurnoDeJuego() {
        return turnoDeJuego;
    }

    public void iniciarDatos(String nombre) throws IOException, JuegoException {
        LinkedList<String> nombresComp = crearListaDesdeFichero(FICHERO_NOMBRES, nombre);
        jugador = new Jugador(nombre);
        competencia = new HashSet<>();
        mercado = new LinkedList<>();

        for (int i = 0; i < CANTIDAD_COMPETENCIA; i++) {
            if (!nombresComp.get(i).equalsIgnoreCase(nombre)) {
                competencia.add(new Jugador(nombresComp.get(i)));
            }
        }

        crearMisiones();
        turnoDeJuego = 1;
    }

    private void crearMisiones() throws IOException {
        LinkedList<String> textoMisiones = crearListaDesdeFichero(FICHERO_MISIONES, "");

        misiones = new LinkedList<>();

        for (int i = 0; i < CANTIDAD_MISIONES; i++) {
            misiones.add(new Mision(textoMisiones.get(i)));
        }
    }

    private LinkedList<String> crearListaDesdeFichero(String fichero, String nombre) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(fichero));
        LinkedList<String> listado = new LinkedList<>();

        String linea = lector.readLine();
        while (linea != null) {
            if (!nombre.equalsIgnoreCase(linea)) {
                listado.add(linea);
            }
            linea = lector.readLine();
        }

        lector.close();
        Collections.shuffle(listado);
        return listado;
    }

    public LinkedList<Jugador> crearClasificacion() {
        //HashSet<Jugador> participantes = competencia;
        //participantes.add(jugador);

        LinkedList<Jugador> clasificacion = new LinkedList<>();

        for (Iterator<Jugador> iterator = competencia.iterator(); iterator.hasNext();) {
            Jugador next = iterator.next();
            clasificacion.add(next);
        }

        clasificacion.add(jugador);

        Collections.sort(clasificacion);

        return clasificacion;
    }

    public void crearDuelistas(Jugador jugadorActual) {
        // recoge todos los jugadores, incluido el usuario, y los ordena por exito en combates
        LinkedList<Jugador> clasificacion = crearClasificacion();
        // lista para guardar los duelistas sin que haya repetidos
        HashSet<Jugador> duelistas = new HashSet<>();

        Random r = new Random();
        int posJugador = clasificacion.indexOf(jugadorActual);
        int posDuelista;

        // controla que no se produzcan mas de 15 vueltas en el bucle
        // si gran cantidad de jugadores luchan, se quedaran sin vida y no podran ser elegidos como duelistas
        int vueltasRestantes = MAX_VUELTAS_BUSCAR_DUELISTA;

        // en cada vuelta cambia la seleccion del duelista entre superior e inferior en la clasificacion
        int mayorMenor = 1;
        do {
            posDuelista = r.nextInt(RANGO_MAXIMO_DUELO) + 1;

            if (mayorMenor < 0) {
                mayorMenor = 1;
            } else {
                mayorMenor = -1;
            }

            try {
                // selecciona al duelista por su posicion en la clasificacion
                Jugador duelista = clasificacion.get(posJugador + (posDuelista * mayorMenor));
                if (duelista.getVidaActual() > 0) {
                    duelistas.add(duelista);
                }
            } catch (IndexOutOfBoundsException e) {
                // no hacer nada
            }

            vueltasRestantes--;
        } while (duelistas.size() < MAXIMO_DUELISTAS && vueltasRestantes > 0);

        // guarda la lista de duelistas elegidos para el jugador actual
        jugadorActual.setPosiblesDuelistas(duelistas);
    }

    public String pasarTurno() throws IOException {
        // Devuelve un mensaje con informacion relevante del jugador
        String mensaje = null;

        // Se simula la actividad de todos los jugadores que participan en el juego
        int i = 1;
        for (Iterator<Jugador> iterator = competencia.iterator(); iterator.hasNext();) {
            Jugador next = iterator.next();

            System.out.println("Simulando: " + i + "-" + next.getNombre());

            simularTurno(next);

            i++;
        }

        crearMisiones();
        turnoDeJuego++;

        try {
            if (jugador.getMisionActiva() != null) {
                mensaje = jugador.avanzarMision();
            }
        } catch (JuegoException ex) {
            // la simulacion debe continuar
        }

        jugador.setVidaActual(jugador.getVidaMax());
        crearDuelistas(jugador);

        System.out.println("Fin simulacion");

        return mensaje;
    }

    private void simularTurno(Jugador jugadorSimulado) {
        Random r = new Random();
        int numAleatorio;

        jugadorSimulado.setVidaActual(jugadorSimulado.getVidaMax());

        // Gasta los puntos de atributos sin usar
        if (jugadorSimulado.getPuntosAtrSinUsar() > 0) {
            for (int i = jugadorSimulado.getPuntosAtrSinUsar(); i > 0; i--) {
                try {
                    numAleatorio = r.nextInt(jugadorSimulado.getAtributosJugador().length);
                    jugadorSimulado.subirAtributo(numAleatorio);
                    if (numAleatorio == 3) {
                        jugadorSimulado.setVidaActual(jugadorSimulado.getVidaMax());
                    }
                } catch (JuegoException ex) {
                    // la simulacion debe continuar
                }
            }
        }

        // Comprueba si tiene mejor equipo para equiparse
        try {
            accionesInventario(jugadorSimulado, r);
        } catch (JuegoException ex) {
            // la simulacion debe continuar
        }

        // Mejorar equipamiento equipado/inventario
        for (Equipamiento equipamiento : jugadorSimulado.getEquipado()) {
            if (equipamiento != null) {
                try {
                    jugadorSimulado.mejorarEquipo(equipamiento);
                } catch (JuegoException ex) {
                    // la simulacion debe continuar
                }
            }
        }
        if (jugadorSimulado.getInventario().size() > 0) {
            for (Equipamiento equipamiento : jugadorSimulado.getInventario()) {
                try {
                    jugadorSimulado.mejorarEquipo(equipamiento);
                } catch (JuegoException ex) {
                    // la simulacion debe continuar
                }
            }
        }

        // Decide entre ir a mision o combatir contra otros jugadores
        try {
            if (jugadorSimulado.getMisionActiva() == null) {
                if (misiones.isEmpty()) {
                    combatirDuelo(jugadorSimulado, r);
                } else {
                    if (r.nextInt(RANGO_FREC_MISION) % FREC_MAQUINA_HACE_MISION == 0) {
                        jugadorSimulado.setMisionActiva(misiones.pollLast());
                    } else {
                        combatirDuelo(jugadorSimulado, r);
                    }
                }
            } else {
                jugadorSimulado.avanzarMision();
            }
        } catch (JuegoException ex) {
            // la simulacion debe continuar
        }
    }

    private void accionesInventario(Jugador jugadorSimulado, Random r) throws JuegoException {
        for (Equipamiento equipamiento : jugadorSimulado.getInventario()) {
            if (jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()] != null) {
                if ((equipamiento.getNivel() > jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()].getNivel())
                        || (equipamiento.getValor() > jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()].getValor())) {
                    // Cambiar equipado
                    jugadorSimulado.equiparDesequipar(equipamiento, true);
                } else {
                    if (r.nextInt(RANGO_FREC_VENTA) == 0) {
                        // Vender equipamiento
                        jugadorSimulado.actualizarInventario(equipamiento, false);
                        equipamiento.setPrecio(r.nextInt(equipamiento.getValor()) + (equipamiento.getValor() / 2));
                        mercado.add(equipamiento);
                    }
                }
            } else {
                // Equipar
                jugadorSimulado.equiparDesequipar(equipamiento, true);
            }
        }
    }

    private void combatirDuelo(Jugador jugadorSimulado, Random r) throws JuegoException {
        do {
            crearDuelistas(jugadorSimulado);
            // si no encuentra ningun duelista no podra luchar
            if (jugadorSimulado.getPosiblesDuelistas().size() > 0) {
                int duelista = r.nextInt(jugadorSimulado.getPosiblesDuelistas().size());
                combate(jugadorSimulado, (Jugador) jugadorSimulado.getPosiblesDuelistas().toArray()[duelista]);
            }
        } while (((jugadorSimulado.getVidaActual() * 100) / jugadorSimulado.getVidaMax()) > (r.nextInt(RANGO_PORCENTAJE_COMBATIR) + PORCENTAJE_VIDA_MIN_COMBATIR) && (jugadorSimulado.getPosiblesDuelistas().size() > 0));
    }

    public String combate(Jugador jugadorActual, Jugador duelista) throws JuegoException {
        StringBuilder resumenCombate = new StringBuilder();
        Random r = new Random();
        int danioJugador;
        int danioDuelista = 0;
        int totalDanioJugador = 0;
        int totalDanioDuelista = 0;
        int turnoCombateActual = 0;

        if (jugadorActual.getVidaActual() < (jugadorActual.getVidaMax() / 10)) {
            throw new JuegoException("No puedes combatir estando tan herido. Debes recuperar vida");
        }

        do {
            turnoCombateActual++;

            resumenCombate.append("Turno del combate: " + turnoCombateActual + "\n");

            danioJugador = (r.nextInt(jugadorActual.getAtaqueMax() - jugadorActual.getAtaqueMin()) + jugadorActual.getAtaqueMin()) - (r.nextInt(duelista.getDefensaMax() - duelista.getDefensaMin()) + duelista.getDefensaMin());
            if (danioJugador < 0) {
                danioJugador = 0;
            }
            duelista.setVidaActual(duelista.getVidaActual() - danioJugador);
            totalDanioJugador += danioJugador;

            resumenCombate.append(jugadorActual.getNombre() + " causa " + danioJugador + " de daño a " + duelista.getNombre() + "\n");

            if (duelista.getVidaActual() > 0) {
                danioDuelista = (r.nextInt(duelista.getAtaqueMax() - duelista.getAtaqueMin()) + duelista.getAtaqueMin()) - (r.nextInt(jugadorActual.getDefensaMax() - jugadorActual.getDefensaMin()) + jugadorActual.getDefensaMin());
                if (danioDuelista < 0) {
                    danioDuelista = 0;
                }
                jugadorActual.setVidaActual(jugadorActual.getVidaActual() - danioDuelista);
                totalDanioDuelista += danioDuelista;

                resumenCombate.append(duelista.getNombre() + " causa " + danioDuelista + " de daño a " + jugadorActual.getNombre() + "\n");
            }
        } while (jugadorActual.getVidaActual() > 0 && duelista.getVidaActual() > 0 && MAX_TURNOS_COMBATE > turnoCombateActual);

        resumenCombate.append(comprobarVencedor(jugadorActual, duelista, totalDanioJugador, totalDanioDuelista));

        resumenCombate.append("\nTotal de daño causado:" + "\n\t" + totalDanioJugador + " " + jugadorActual.getNombre() + "\n\t" + totalDanioDuelista + " " + duelista.getNombre());

        jugadorActual.setTotalAtaDef(new int[]{jugadorActual.getTotalAtaDef()[0] + totalDanioJugador, jugadorActual.getTotalAtaDef()[1] + totalDanioDuelista});
        duelista.setTotalAtaDef(new int[]{duelista.getTotalAtaDef()[0] + totalDanioDuelista, duelista.getTotalAtaDef()[1] + totalDanioJugador});

        return resumenCombate.toString();
    }

    private String comprobarVencedor(Jugador jugadorActual, Jugador duelista, int totalDanioJugador, int totalDanioDuelista) throws JuegoException {

        String resultado;

        if (duelista.getVidaActual() < 1) {
            jugadorActual.subirVictoria();
            jugadorActual.setExpAcumulada(duelista.getNivel());
            duelista.subirDerrota();
            resultado = ("\n" + jugadorActual.getNombre() + " a matado a " + duelista.getNombre() + "\n");
        } else if (jugadorActual.getVidaActual() < 1) {
            jugadorActual.subirDerrota();
            duelista.subirVictoria();
            duelista.setExpAcumulada(jugadorActual.getNivel());
            resultado = ("\n" + duelista.getNombre() + " a matado a " + jugadorActual.getNombre() + "\n");
        } else if (totalDanioJugador > totalDanioDuelista) {
            jugadorActual.subirVictoria();
            jugadorActual.setExpAcumulada(duelista.getNivel());
            duelista.subirDerrota();
            resultado = ("\n" + jugadorActual.getNombre() + " gana por causar más daño que " + duelista.getNombre() + "\n");
        } else if (totalDanioJugador < totalDanioDuelista) {
            jugadorActual.subirDerrota();
            duelista.subirVictoria();
            duelista.setExpAcumulada(jugadorActual.getNivel());
            resultado = ("\n" + duelista.getNombre() + " gana por causar más daño que " + jugadorActual.getNombre() + "\n");
        } else {
            jugadorActual.subirEmpate();
            jugadorActual.setExpAcumulada(duelista.getNivel() / 2);
            duelista.subirEmpate();
            duelista.setExpAcumulada(jugadorActual.getNivel() / 2);
            resultado = ("\n" + jugadorActual.getNombre() + " y " + duelista.getNombre() + " empatan por causar el mismo daño sin morir ninguno\n");
        }

        return resultado;
    }

    public void compraEnMercado(Jugador jugadorActual, Equipamiento equipoElegido) throws JuegoException {
        int precioTotal = equipoElegido.getPrecio() + (equipoElegido.getPrecio() / TASA_MERCADER);

        if (jugadorActual.getOroActual() >= precioTotal) {
            jugadorActual.setOroActual(jugadorActual.getOroActual() - precioTotal);

            // <<debug>>
            System.out.println("precio del equipo a la venta: " + equipoElegido.getPrecio());
            System.out.println("antiguo propietario del equipo: " + equipoElegido.getPropietario());
            System.out.println("oro del antiguo propietario: " + equipoElegido.getPropietario().getOroActual());

            equipoElegido.getPropietario().setOroActual(equipoElegido.getPropietario().getOroActual() + equipoElegido.getPrecio());
            equipoElegido.setPropietario(jugadorActual);
            jugadorActual.actualizarInventario(equipoElegido, true);
            mercado.remove(equipoElegido);
        } else {
            throw new JuegoException("No dispones de oro suficiente para comprar este objeto");
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.jugador);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlujoJuego other = (FlujoJuego) obj;
        if (!Objects.equals(this.jugador, other.jugador)) {
            return false;
        }
        return true;
    }
}
