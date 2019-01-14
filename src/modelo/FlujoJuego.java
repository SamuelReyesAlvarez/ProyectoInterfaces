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
 * Esta clase gestiona todas las acciones comunes de los jugadores y dirige la
 * ejecucion principal del juego
 *
 * Es la clase central del modelo y el jugador es una pieza mas
 *
 */
public class FlujoJuego {

    // Localizacion de los ficheros necesarios para la construccion de los datos
    // menos relevantes del juego
    private static final String FICHERO_NOMBRES = "archivos/nombres.txt";
    private static final String FICHERO_MISIONES = "archivos/misiones.txt";
    // Valores predefinidos para la gestion del proceso de juego
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

    // Objetos necesarios para la construccion del juego
    private Jugador jugador;
    private HashSet<Jugador> competencia;
    private LinkedList<Mision> misiones;
    private LinkedList<Equipamiento> mercado;
    private int turnoDeJuego;

    // Constructor principal
    public FlujoJuego() {
    }

    // Constructor para patron
    public FlujoJuego(String nombre) throws JuegoException {
        jugador = new Jugador(nombre);
    }

    // Metodos que devuelven los valores asignados a los objetos que componen
    // la clase
    public int getTasaMercader() {
        return TASA_MERCADER;
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

    // Este metodo se encarga de crear los datos para las nuevas partidas
    public void iniciarDatos(String nombre) throws IOException, JuegoException {
        // Obtiene nombres por defecto para los oponentes del usuario
        LinkedList<String> nombresComp = crearListaDesdeFichero(FICHERO_NOMBRES, nombre);
        // Inicia los datos del personaje del usuario de la aplicacion
        jugador = new Jugador(nombre);

        competencia = new HashSet<>();
        mercado = new LinkedList<>();

        // Crea a los jugadores oponentes que seran controlador por la maquina
        for (int i = 0; i < CANTIDAD_COMPETENCIA; i++) {
            if (!nombresComp.get(i).equalsIgnoreCase(nombre)) {
                competencia.add(new Jugador(nombresComp.get(i)));
            }
        }

        // Crea una nueva lista de misiones
        crearMisiones();
        // Activa el primer turno de la partida
        turnoDeJuego = 1;
    }

    // Genera de forma aleatoria las misiones para los jugadores
    private void crearMisiones() throws IOException {
        LinkedList<String> textoMisiones = crearListaDesdeFichero(FICHERO_MISIONES, "");

        misiones = new LinkedList<>();

        for (int i = 0; i < CANTIDAD_MISIONES; i++) {
            misiones.add(new Mision(textoMisiones.get(i)));
        }
    }

    // Ayuda en la obtencion de datos estandar almacenados en ficheros de texto
    // como son los nombres de jugadores o definicion de misiones
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
        // Desordena la coleccion para que la asignacion sea aleatoria
        Collections.shuffle(listado);
        return listado;
    }

    // Junta al jugador con los demas participantes y los ordena por el criterio
    // definido en el metodo compareTo() de Jugador
    public LinkedList<Jugador> crearClasificacion() {
        // Se necesita un tipo de coleccion que aporte un indice a los objetos
        // para ser enumerados
        LinkedList<Jugador> clasificacion = new LinkedList<>();

        // Copia los jugadores de la coleccion original en la coleccion comodin
        for (Iterator<Jugador> iterator = competencia.iterator(); iterator.hasNext();) {
            Jugador next = iterator.next();
            clasificacion.add(next);
        }

        // Incluye al usuario para completar la coleccion
        clasificacion.add(jugador);

        // Ordena la coleccion
        Collections.sort(clasificacion);

        // Devuelve la coleccion ordenada para mostrarla en la Clasificacion o
        // para crear la seleccion de duelistas de cada jugador
        return clasificacion;
    }

    // Crea para el jugador actual una seleccion de oponentes cercanos en la
    // clasificacion para participar en combates
    public void crearDuelistas(Jugador jugadorActual) {
        // Obtiene la lista ordenada de todos los participantes, incluyendo al
        // jugador
        LinkedList<Jugador> clasificacion = crearClasificacion();
        // Lista para guardar los duelistas sin que haya repetidos
        HashSet<Jugador> duelistas = new HashSet<>();

        Random r = new Random();
        int posJugador = clasificacion.indexOf(jugadorActual);
        int posDuelista;

        // Controla que no se realicen mas de 15 vueltas en el bucle (seguridad)
        // *Si gran cantidad de jugadores luchan, se quedaran sin vida y no
        // podran ser elegidos como duelistas*
        int vueltasRestantes = MAX_VUELTAS_BUSCAR_DUELISTA;

        // En cada vuelta cambia la seleccion del duelista entre superior e
        // inferior en la clasificacion
        int mayorMenor = 1;
        do {
            posDuelista = r.nextInt(RANGO_MAXIMO_DUELO) + 1;

            if (mayorMenor < 0) {
                mayorMenor = 1;
            } else {
                mayorMenor = -1;
            }

            try {
                // Selecciona al duelista por su posicion en la clasificacion
                // controlando la seleccion fuera del rango de la coleccion
                Jugador duelista = clasificacion.get(posJugador + (posDuelista * mayorMenor));
                if (duelista.getVidaActual() > 0) {
                    duelistas.add(duelista);
                }
            } catch (IndexOutOfBoundsException e) {
                // No hacer nada y continuar
            }

            vueltasRestantes--;
        } while (duelistas.size() < MAXIMO_DUELISTAS && vueltasRestantes > 0);

        // Guarda la lista de duelistas elegidos para el jugador actual
        jugadorActual.setPosiblesDuelistas(duelistas);
    }

    // Gestiona el cambio de turno en la partida
    public String pasarTurno() throws IOException {
        // Se usara para devolver informacion relevante al jugador
        String mensaje = null;

        // Se simula la actividad de todos los jugadores que participan en el juego
        int i = 1;
        for (Iterator<Jugador> iterator = competencia.iterator(); iterator.hasNext();) {
            Jugador next = iterator.next();

            // <<Para debug>>
            System.out.println("Simulando: " + i + "-" + next.getNombre());

            simularTurno(next);

            i++;
        }

        // Crea una nueva lista de misiones
        crearMisiones();
        // Avanza de turno
        turnoDeJuego++;

        // Inicia el turno del jugador y comprueba la mision activa
        try {
            if (jugador.getMisionActiva() != null) {
                mensaje = jugador.avanzarMision();
            }
        } catch (JuegoException ex) {
            // Esta excepcion no es relevante y la simulacion debe continuar
        }

        // Al comienzo de turno se recupera toda la vida
        jugador.setVidaActual(jugador.getVidaMax());
        // Genera una nueva seleccion de duelistas
        crearDuelistas(jugador);

        // <<Para debug>>
        System.out.println("Fin simulacion");

        // Mensaje informativo para el jugador
        return mensaje;
    }

    // Gestiona las acciones establecidas para cada jugador controlado por la maquina
    private void simularTurno(Jugador jugadorSimulado) {
        Random r = new Random();
        int numAleatorio;

        // Al comienzo de turno se recupera toda la vida
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
                    // A la maquina no le interesa esta informacion y la
                    // simulacion debe continuar
                }
            }
        }

        // Comprueba si tiene mejor equipo para equiparse
        try {
            accionesInventario(jugadorSimulado, r);
        } catch (JuegoException ex) {
            // A la maquina no le interesa esta informacion y la
            // simulacion debe continuar
        }

        // Mejorar equipamiento equipado
        for (Equipamiento equipamiento : jugadorSimulado.getEquipado()) {
            if (equipamiento != null) {
                try {
                    jugadorSimulado.mejorarEquipo(equipamiento);
                } catch (JuegoException ex) {
                    // A la maquina no le interesa esta informacion y la
                    // simulacion debe continuar
                }
            }
        }

        // Mejorar equipamiento del inventario
        if (jugadorSimulado.getInventario().size() > 0) {
            for (Equipamiento equipamiento : jugadorSimulado.getInventario()) {
                try {
                    jugadorSimulado.mejorarEquipo(equipamiento);
                } catch (JuegoException ex) {
                    // A la maquina no le interesa esta informacion y la
                    // simulacion debe continuar
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
            // A la maquina no le interesa esta informacion y la
            // simulacion debe continuar
        }
    }

    // Gestiona el inventario
    private void accionesInventario(Jugador jugadorSimulado, Random r) throws JuegoException {
        for (Equipamiento equipamiento : jugadorSimulado.getInventario()) {
            if (jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()] != null) {
                if ((equipamiento.getNivel() > jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()].getNivel())
                        || (equipamiento.getValor() > jugadorSimulado.getEquipado()[equipamiento.getTipo().ordinal()].getValor())) {
                    // Cambiar equipado si tiene mejor objeto en el inventario
                    jugadorSimulado.equiparDesequipar(equipamiento, true);
                } else {
                    if (r.nextInt(RANGO_FREC_VENTA) == 0) {
                        // Vender equipamiento que sobra
                        jugadorSimulado.actualizarInventario(equipamiento, false);
                        equipamiento.setPrecio(r.nextInt(equipamiento.getValor()) + (equipamiento.getValor() / 2));
                        mercado.add(equipamiento);
                    }
                }
            } else {
                // Equipar objetos nuevos si hay hueco
                jugadorSimulado.equiparDesequipar(equipamiento, true);
            }
        }
    }

    // Gestiona los combates contra otros jugadores
    private void combatirDuelo(Jugador jugadorSimulado, Random r) throws JuegoException {
        do {
            crearDuelistas(jugadorSimulado);
            // Si no encuentra ningun duelista no luchara
            if (jugadorSimulado.getPosiblesDuelistas().size() > 0) {
                int duelista = r.nextInt(jugadorSimulado.getPosiblesDuelistas().size());
                combate(jugadorSimulado, (Jugador) jugadorSimulado.getPosiblesDuelistas().toArray()[duelista]);
            }
        } while (((jugadorSimulado.getVidaActual() * 100) / jugadorSimulado.getVidaMax()) > (r.nextInt(RANGO_PORCENTAJE_COMBATIR) + PORCENTAJE_VIDA_MIN_COMBATIR) && (jugadorSimulado.getPosiblesDuelistas().size() > 0));
    }

    // Ejecuta las acciones de un combate
    public String combate(Jugador jugadorActual, Jugador duelista) throws JuegoException {
        // Se usara para recoger el resumen del combate
        StringBuilder resumenCombate = new StringBuilder();

        // Variables locales para la gestion del combate
        Random r = new Random();
        int danioJugador;
        int danioDuelista = 0;
        int totalDanioJugador = 0;
        int totalDanioDuelista = 0;
        int turnoCombateActual = 0;

        // Comprueba que ninguno de los combatientes participe sin vida
        if (jugadorActual.getVidaActual() < (jugadorActual.getVidaMax() / 10)) {
            throw new JuegoException("No puedes combatir estando tan herido. Debes recuperar vida");
        }

        do {
            turnoCombateActual++;

            resumenCombate.append("Ronda del combate: " + turnoCombateActual + "\n");

            // Jugador actual lanza golpe sobre duelista
            danioJugador = (r.nextInt(jugadorActual.getAtaqueMax() - jugadorActual.getAtaqueMin()) + jugadorActual.getAtaqueMin()) - (r.nextInt(duelista.getDefensaMax() - duelista.getDefensaMin()) + duelista.getDefensaMin());
            if (danioJugador < 0) {
                danioJugador = 0;
            }
            duelista.setVidaActual(duelista.getVidaActual() - danioJugador);
            totalDanioJugador += danioJugador;

            resumenCombate.append(jugadorActual.getNombre() + " causa " + danioJugador + " de daño a " + duelista.getNombre() + "\n");

            // Si duelista no ha muerto lanza golpe sobre jugador actual
            if (duelista.getVidaActual() > 0) {
                danioDuelista = (r.nextInt(duelista.getAtaqueMax() - duelista.getAtaqueMin()) + duelista.getAtaqueMin()) - (r.nextInt(jugadorActual.getDefensaMax() - jugadorActual.getDefensaMin()) + jugadorActual.getDefensaMin());
                if (danioDuelista < 0) {
                    danioDuelista = 0;
                }
                jugadorActual.setVidaActual(jugadorActual.getVidaActual() - danioDuelista);
                totalDanioDuelista += danioDuelista;

                resumenCombate.append(duelista.getNombre() + " causa " + danioDuelista + " de daño a " + jugadorActual.getNombre() + "\n");
            }

            // Controla que ambos siguan vivos y aun queden rondas de combate
        } while (jugadorActual.getVidaActual() > 0 && duelista.getVidaActual() > 0 && MAX_TURNOS_COMBATE > turnoCombateActual);

        // Comprueba el resultado final
        resumenCombate.append(comprobarVencedor(jugadorActual, duelista, totalDanioJugador, totalDanioDuelista));
        // Mensaje de resultado final del combate
        resumenCombate.append("\nTotal de daño causado:" + "\n\t" + totalDanioJugador + " " + jugadorActual.getNombre() + "\n\t" + totalDanioDuelista + " " + duelista.getNombre());

        // Incluye los valores del resultado en las estadisticas de cada combatiente
        jugadorActual.setTotalAtaDef(new int[]{jugadorActual.getTotalAtaDef()[0] + totalDanioJugador, jugadorActual.getTotalAtaDef()[1] + totalDanioDuelista});
        duelista.setTotalAtaDef(new int[]{duelista.getTotalAtaDef()[0] + totalDanioDuelista, duelista.getTotalAtaDef()[1] + totalDanioJugador});

        // Devuelve toda la informacion del combate
        return resumenCombate.toString();
    }

    // Analiza el resultado para asignar premios y mensaje final
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

    // Gestiona la compra de objetos en el Bazar
    public void compraEnMercado(Jugador jugadorActual, Equipamiento equipoElegido) throws JuegoException {
        // Establece el precio total del objeto incluyendo la Tasa del Mercader
        int precioTotal = equipoElegido.getPrecio() + (equipoElegido.getPrecio() / TASA_MERCADER);

        // Comprueba si el jugador dispone de monedas suficientes para la compra
        if (jugadorActual.getOroActual() >= precioTotal) {
            // Resta al comprador el coste total de la compra
            jugadorActual.setOroActual(jugadorActual.getOroActual() - precioTotal);

            // <<Para debug>>
            System.out.println("precio del equipo a la venta: " + equipoElegido.getPrecio());
            System.out.println("antiguo propietario del equipo: " + equipoElegido.getPropietario());
            System.out.println("oro del antiguo propietario: " + equipoElegido.getPropietario().getOroActual());

            // Suma el precio de venta al jugador que ofrecia el objeto
            equipoElegido.getPropietario().setOroActual(equipoElegido.getPropietario().getOroActual() + equipoElegido.getPrecio());
            // Establece al comprador como nuevo propietario del objeto
            equipoElegido.setPropietario(jugadorActual);
            // Incluye el objeto en el inventario del comprador
            jugadorActual.actualizarInventario(equipoElegido, true);
            // Elimina el objeto de la lista del Bazar
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
