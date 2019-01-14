package modelo;

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
 * Clase que define y asigna las funciones propias de cada participante en la
 * partida
 *
 */
public class Jugador implements Comparable<Jugador> {

    // Valores predefinidos para la gestion de la clase
    private static final int LONG_MIN_NOMBRE = 3;
    private static final int LONG_MAX_NOMBRE = 15;
    //private static final String FORMATO_NOMBRE = "[a-zA-Z]{3,15}"; // No funciona
    private static final int NIVEL_INICIAL = 1;
    private static final int EXPERIENCIA_INICIAL = 0;
    private static final int EXPERIENCIA_BASE = 2;
    private static final int VIDA_BASE = 200;
    private static final int ATAQUE_MIN_BASE = 10;
    private static final int ATAQUE_MAX_BASE = 15;
    private static final int DEFENSA_MIN_BASE = 0;
    private static final int DEFENSA_MAX_BASE = 0;
    private static final int PUNTOS_ATR_NUEVO_NIVEL = 3;
    private static final int VALOR_FUERZA = 2;
    private static final int VALOR_ARMADURA = 2;
    private static final int VALOR_DESTREZA = 1;
    private static final int VALOR_CONSTITUCION = 20;
    private static final int ORO_INICIAL = 0;
    private static final int TAMANIO_MAX_INVENTARIO = 20;
    private static final int PROBABILIDAD_CREAR_EQUIPO = 5;
    private static final int TASA_VENTA_DIRECTA = 2;

    // Atributos que definen y componen al jugador
    private String nombre;
    private int nivel;
    private int expAcumulada;
    private int expSigNivel;
    private int puntosAtrSinUsar;
    private int[] atributosJugador = new int[4];
    private int[] atributosEquipo = new int[4];
    private int vidaMax;
    private int vidaActual;
    private int ataqueMin;
    private int ataqueMax;
    private int defensaMin;
    private int defensaMax;
    private int oroActual;
    private int[] combates = new int[3];
    private int[] totalAtaDef = new int[2];
    private int[] misiones = new int[2];
    private Mision misionActiva;
    private HashSet<Jugador> posiblesDuelistas;
    private LinkedList<Equipamiento> inventario;
    private Equipamiento[] equipado;

    // Constructor
    public Jugador(String nombre) throws JuegoException {
        setNombre(nombre);
        setNivel(NIVEL_INICIAL);
        setExpAcumulada(EXPERIENCIA_INICIAL);
        setExpSigNivel();
        setPuntosAtrSinUsar(0);
        setAtributosJugador(new int[]{5, 5, 5, 5});
        setAtributosEquipo(new int[]{0, 0, 0, 0});
        setVidaActual(getVidaMax());
        setOroActual(ORO_INICIAL);
        setCombates(new int[]{0, 0, 0});
        setTotalAtaDef(new int[]{0, 0});
        setMisiones(new int[]{0, 0});
        setMisionActiva(null);
        setPosiblesDuelistas(null);
        setInventario(new LinkedList<>());
        equipado = new Equipamiento[TipoEquipamiento.values().length];
    }

    // Metodos que devuelven los valores definidos en el jugador
    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExpAcumulada() {
        return expAcumulada;
    }

    public int getExpSigNivel() {
        return expSigNivel;
    }

    public int getPuntosAtrSinUsar() {
        return puntosAtrSinUsar;
    }

    public int[] getAtributosJugador() {
        return atributosJugador;
    }

    public int[] getAtributosEquipo() {
        return atributosEquipo;
    }

    public int getFuerza() {
        return atributosJugador[0] + atributosEquipo[0];
    }

    public int getArmadura() {
        return atributosJugador[1] + atributosEquipo[1];
    }

    public int getDestreza() {
        return atributosJugador[2] + atributosEquipo[2];
    }

    public int getConstitucion() {
        return atributosJugador[3] + atributosEquipo[3];
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getAtaqueMin() {
        return ataqueMin;
    }

    public int getAtaqueMax() {
        return ataqueMax;
    }

    public int getDefensaMin() {
        return defensaMin;
    }

    public int getDefensaMax() {
        return defensaMax;
    }

    public int getOroActual() {
        return oroActual;
    }

    public int[] getCombates() {
        return combates;
    }

    public int[] getTotalAtaDef() {
        return totalAtaDef;
    }

    public int[] getMisiones() {
        return misiones;
    }

    public Mision getMisionActiva() {
        return misionActiva;
    }

    public HashSet<Jugador> getPosiblesDuelistas() {
        return posiblesDuelistas;
    }

    public int getTamanioMaxInventario() {
        return TAMANIO_MAX_INVENTARIO;
    }

    public LinkedList<Equipamiento> getInventario() {
        return inventario;
    }

    public Equipamiento[] getEquipado() {
        return equipado;
    }

    public int getTasaVentaDirecta() {
        return TASA_VENTA_DIRECTA;
    }

    // Metodos que asignan valores para definir al jugador
    private void setNombre(String nombre) throws JuegoException {
        // Controla el formato de nombre para el jugador
        if (nombre.length() < LONG_MIN_NOMBRE || nombre.length() > LONG_MAX_NOMBRE) {
            throw new JuegoException("El nombre debe contener entre " + LONG_MIN_NOMBRE + " y " + LONG_MAX_NOMBRE + " letras");
        }
        for (int i = 0; i < nombre.length(); i++) {
            if (!Character.isLetter(nombre.charAt(i))) {
                throw new JuegoException("El nombre debe contener solo letras");
            }
        }
        this.nombre = nombre;
    }

    /*
    // No funciona String.matches(regexp)
    private void setNombre(String nombre) throws JuegoException {
        if (nombre.matches(FORMATO_NOMBRE)) {
            throw new JuegoException("El nombre debe contener entre 3 y 15 letras,\nsin espacios ni numeros ni caracteres especiales");
        }
        this.nombre = nombre;
    }
     */
    protected void setExpAcumulada(int expGanada) throws JuegoException {
        this.expAcumulada += expGanada;

        // Controla el aumento de nivel
        while (this.expAcumulada >= expSigNivel) {
            setNivel(nivel + 1);
            setPuntosAtrSinUsar(puntosAtrSinUsar + PUNTOS_ATR_NUEVO_NIVEL);
        }
    }

    private void setNivel(int nivel) {
        this.nivel = nivel;
        setExpSigNivel();
    }

    private void setExpSigNivel() {
        // Calcula la experiencia necesaria para alcanzar el siguiente nivel
        expSigNivel = (int) Math.pow(EXPERIENCIA_BASE, nivel);
    }

    private void setPuntosAtrSinUsar(int puntosAtrSinUsar) throws JuegoException {
        // Controla que no se usen mas puntos de los disponibles
        if (puntosAtrSinUsar < 0) {
            throw new JuegoException("Atributos sin usar no deben ser negativos");
        }
        this.puntosAtrSinUsar = puntosAtrSinUsar;
    }

    private void setAtributosJugador(int[] atributos) {
        // Al modificarse los atributos se actualizan los valores de estado del personaje
        atributosJugador = atributos;
        setVidaMax();
        setAtaqueMin();
        setAtaqueMax();
        setDefensaMin();
        setDefensaMax();
    }

    private void setAtributosEquipo(int[] atributos) {
        // Al modificarse los atributos se actualizan los valores de estado del personaje
        atributosEquipo = atributos;
        setVidaMax();
        setAtaqueMin();
        setAtaqueMax();
        setDefensaMin();
        setDefensaMax();
    }

    private void setVidaMax() {
        // Calcula el valor de vida maxima segun los atributos del jugador
        vidaMax = VIDA_BASE + ((atributosJugador[3] + atributosEquipo[3]) * VALOR_CONSTITUCION);
        //setVidaActual(vidaMax);
    }

    public void setVidaActual(int vidaActual) {
        if (vidaActual < 0) {
            vidaActual = 0;
        }
        this.vidaActual = vidaActual;
    }

    private void setAtaqueMin() {
        // Calcula el valor de ataque minimo segun los atributos del jugador
        ataqueMin = ATAQUE_MIN_BASE + ((atributosJugador[0] + atributosEquipo[0]) * VALOR_FUERZA);
    }

    private void setAtaqueMax() {
        // Calcula el valor de ataque maximo segun los atributos del jugador
        ataqueMax = ATAQUE_MAX_BASE + ((atributosJugador[0] + atributosEquipo[0]) * VALOR_FUERZA) + ((atributosJugador[2] + atributosEquipo[2]) * VALOR_DESTREZA);
    }

    private void setDefensaMin() {
        // Calcula el valor de defensa minima segun los atributos del jugador
        defensaMin = DEFENSA_MIN_BASE + ((atributosJugador[1] + atributosEquipo[1]) * VALOR_ARMADURA);
    }

    private void setDefensaMax() {
        // Calcula el valor de defensa maxima segun los atributos del jugador
        defensaMax = DEFENSA_MAX_BASE + ((atributosJugador[1] + atributosEquipo[1]) * VALOR_ARMADURA) + ((atributosJugador[2] + atributosEquipo[2]) * VALOR_DESTREZA);
    }

    protected void setOroActual(int nuevoOroActual) throws JuegoException {
        if (nuevoOroActual < 0) {
            throw new JuegoException("No puedes tener oro en negativo");
        }
        this.oroActual = nuevoOroActual;
    }

    protected void setCombates(int[] combates) {
        this.combates = combates;
    }

    protected void setTotalAtaDef(int[] totalAtaDef) {
        this.totalAtaDef = totalAtaDef;
    }

    protected void setMisiones(int[] misiones) {
        this.misiones = misiones;
    }

    public void setMisionActiva(Mision nuevaMision) throws JuegoException {
        // Establece una nueva mision en curso para el jugador si es posible
        if (vidaActual < vidaMax && nuevaMision != null) {
            throw new JuegoException("No puedes ir a una mision estando herido");
        }
        this.misionActiva = nuevaMision;
    }

    protected void setPosiblesDuelistas(HashSet<Jugador> posiblesDuelistas) {
        this.posiblesDuelistas = posiblesDuelistas;
    }

    protected void setInventario(LinkedList<Equipamiento> inventario) throws JuegoException {
        this.inventario = inventario;

        // Comprueba los objetos del inventario para equipar los que se especifiquen
        for (Equipamiento equipo : this.inventario) {
            if (equipo.isEquipado()) {
                Equipamiento aEquipar = inventario.get(inventario.indexOf(equipo));
                inventario.remove(equipo);
                setEquipado(aEquipar);
            }
        }

        actualizarInventario(null, false);
    }

    // Incluye en la tupla de objetos equipados un nuevo objeto
    private void setEquipado(Equipamiento nuevoEquipado) throws JuegoException {
        if (nuevoEquipado != null) {
            // Comprueba si hay otro objeto del mismo Tipo para intercambiarlos
            if (equipado[nuevoEquipado.getTipo().ordinal()] != null) {
                Equipamiento viejoEquipado = equipado[nuevoEquipado.getTipo().ordinal()];
                equipado[nuevoEquipado.getTipo().ordinal()] = nuevoEquipado;
                viejoEquipado.setEquipado(false);
                inventario.add(viejoEquipado);
                setInventario(inventario);
            } else {
                // Si no es asi, establece el nuevo objeto en la posicion que le
                // corresponde en la tupla
                equipado[nuevoEquipado.getTipo().ordinal()] = nuevoEquipado;
            }
        }
    }

    // Actualiza el valor total de cada atributo en funcion del objeto equipado
    private void actualizarAtributosEquipar() {
        // Para evitar suma continua de valores se crea un contador desde 0
        atributosEquipo = new int[]{0, 0, 0, 0};
        // Se recorre la tupla de Equipado para calcular la suma total de valores
        for (Equipamiento equipamiento : equipado) {
            if (equipamiento != null) {
                switch (equipamiento.getTipo()) {
                    case Casco:
                        atributosEquipo[2] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                    case Coraza:
                        atributosEquipo[3] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                    case Pantalon:
                        atributosEquipo[3] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                    case Botas:
                        atributosEquipo[2] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                    case Escudo:
                        atributosEquipo[1] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                    case Arma:
                        atributosEquipo[0] += equipamiento.getNivel() * equipamiento.getPotenciado();
                        break;
                }
            }
        }
        // Al finalizar la actualizacion de valores se actualizan los datos de
        // estado del jugador
        setAtributosEquipo(atributosEquipo);
    }

    // Actualiza el valor del atributo afectado al desequipar un objeto
    private void actualizarAtributosDesequipar(Equipamiento equipamiento) {
        if (equipamiento != null) {
            // Comprueba el objeto desequipado
            switch (equipamiento.getTipo()) {
                case Casco:
                    atributosEquipo[2] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
                case Coraza:
                    atributosEquipo[3] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
                case Pantalon:
                    atributosEquipo[3] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
                case Botas:
                    atributosEquipo[2] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
                case Escudo:
                    atributosEquipo[1] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
                case Arma:
                    atributosEquipo[0] -= equipamiento.getNivel() * equipamiento.getPotenciado();
                    break;
            }
        }
        // Al finalizar la actualizacion se actializan los datos de estado del
        // jugador
        setAtributosEquipo(atributosEquipo);
    }

    // Controla la generacion de nuevos objetos al finalizar misiones
    private Equipamiento crearEquipo() throws JuegoException {
        Random r = new Random();

        // No siempre se obtienen objetos
        if (r.nextInt(PROBABILIDAD_CREAR_EQUIPO) == 0) {
            // El tipo de objeto es aleatorio
            TipoEquipamiento tipo = TipoEquipamiento.values()[r.nextInt(TipoEquipamiento.values().length)];
            // El nivel del objeto depende del nivel de jugador
            int nivel = (this.nivel / 10) + 1;
            // El potenciado inicial del objeto depende del nivel de este
            int potenciado = r.nextInt(nivel);

            // Devuelve el objeto creado asignandole como propietario el jugador
            // que completo la mision
            return new Equipamiento(this, tipo, nivel, potenciado, false);
        } else {
            return null;
        }
    }

    // Metodos para el flujo de juego propios del jugador
    // Mejora un atributo concreto usando puntos ganados al subir de nivel
    public void subirAtributo(int atributo) throws JuegoException {
        setPuntosAtrSinUsar(puntosAtrSinUsar - 1);
        atributosJugador[atributo]++;
        setAtributosJugador(atributosJugador);
    }

    public void subirVictoria() {
        combates[0]++;
    }

    public void subirEmpate() {
        combates[1]++;
    }

    public void subirDerrota() {
        combates[2]++;
    }

    // Actualiza el total de misiones completadas y el oro total obtenido por recompensas
    public String subirMisiones(int recompensa) throws JuegoException {
        misiones[0]++;
        misiones[1] += recompensa * nivel;
        setOroActual(oroActual + (recompensa * nivel));
        return String.valueOf(recompensa * nivel);
    }

    // Gestiona la mision en curso
    public String avanzarMision() throws JuegoException {
        Equipamiento equipo;
        StringBuilder mensaje = new StringBuilder();
        try {
            // Al pasar de turno se reduce el tiempo restante
            misionActiva.disminuirDuracion();
        } catch (JuegoException e) {
            // Si la mision se completa se informa al jugador
            mensaje.append(e.getMessage());
            mensaje.append("\nRecibe: " + subirMisiones(misionActiva.getRecompensa()) + " monedas de oro");
            // Genera la posibilidad de obtener un objeto nuevo
            equipo = crearEquipo();
            if (equipo != null) {
                // Si hay objeto nuevo se informa al jugador y se incluye en su
                // inventario
                mensaje.append(" y un objeto nuevo");
                mensaje.append(actualizarInventario(equipo, true));
            }
            // Libera al jugador para poder realizar nuevas misiones
            setMisionActiva(null);
            // Devuelve el mensaje de fin de mision
            return mensaje.toString();
        }
        // No devuelve mensaje al no completarse la mision
        return "";
    }

    // Gestiona los objetos en el inventario
    // <<Metodo pendiente de actualizar>>
    public String actualizarInventario(Equipamiento equipamiento, boolean aniadir) {
        String mensaje = "";
        if (equipamiento != null) {
            // Comprueba si se quiere incluir o eliminar el objeto especificado
            if (aniadir) {
                if (inventario.size() < TAMANIO_MAX_INVENTARIO) {
                    inventario.add(equipamiento);
                } else {
                    mensaje = "\nInventario lleno, se ha perdido el objeto";
                }
            } else {
                inventario.remove(equipamiento);
            }
        }

        // Ordena los objetos en el inventario por el criterio definido en el
        // metodo compareTo() de Equipamiento
        Collections.sort(inventario); // <<Metodo pendiente de actualizar>>
        return mensaje;
    }

    // Gestiona los objetos que circulan entre el inventario y el set equipado
    public void equiparDesequipar(Equipamiento equipo, boolean equipar) throws JuegoException {
        Equipamiento miEquipo;

        if (equipo != null) {
            // Comprueba el sentido de circulacion del objeto
            if (equipar) {
                // Mueve el objeto del inventario al set equipado
                miEquipo = inventario.get(inventario.indexOf(equipo));
                miEquipo.setEquipado(equipar);
                setInventario(inventario);
                actualizarAtributosEquipar();
            } else {
                // Mueve el objeto del set equipado al inventario
                miEquipo = equipado[equipo.getTipo().ordinal()];
                equipado[equipo.getTipo().ordinal()] = null;
                miEquipo.setEquipado(equipar);
                actualizarInventario(miEquipo, true);
                actualizarAtributosDesequipar(equipo);
            }
        }
    }

    // Aumenta el nivel de pontenciado del objeto
    public void mejorarEquipo(Equipamiento equipo) throws JuegoException {
        // Comprueba si el jugador tiene monedas suficientes para la accion
        if (getOroActual() > equipo.getValorPotenciar()) {
            // Reduce del oro actual del jugador el coste de la accion
            oroActual -= equipo.getValorPotenciar();
            // Actualiza el valor del atributo al que afecta el objeto
            equipo.aumentarPotenciado();
            if (equipo.isEquipado()) {
                // Si el objeto esta equipado, actualiza los datos de estado del
                // jugador
                actualizarAtributosEquipar();
            }
        } else {
            throw new JuegoException("No dispones de oro suficiente");
        }
    }

    // Gestiona la venta de un objeto del inventario del jugador
    // <<Metodo pendiente de actualizar>>
    public void venderEquipo(Equipamiento equipo) throws JuegoException {
        // Comprueba si el objeto a vender esta en el inventario y pudo ser vendido
        if (inventario.remove(equipo)) {
            // Suma el valor de venta del objeto al oro actual del jugador
            setOroActual((int) (oroActual + (equipo.getValor() / TASA_VENTA_DIRECTA)));
            // Informa al jugador del exito de la accion
            throw new JuegoException("Has vendido el objeto por " + (equipo.getValor() / TASA_VENTA_DIRECTA));
        } else if (equipado[equipo.getTipo().ordinal()].equals(equipo)) {
            // Si el objeto estaba equipado lo elimina del set
            equipado[equipo.getTipo().ordinal()] = null; // <<Codigo pendiente de actualizar>>
            // Suma el valor de venta del objeto al oro actual del jugador
            setOroActual((int) (oroActual + (equipo.getValor() / TASA_VENTA_DIRECTA)));
            throw new JuegoException("Has vendido el objeto por " + (equipo.getValor() / TASA_VENTA_DIRECTA));
        } else {
            throw new JuegoException("No se ha encontrado el objeto:\n" + equipo);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.nombre);
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
        final Jugador other = (Jugador) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Jugador o) {
        if (this.combates[0] > o.combates[0]) {
            return -1;
        } else if (this.combates[0] < o.combates[0]) {
            return 1;
        } else {
            if (this.combates[1] > o.combates[1]) {
                return -1;
            } else if (this.combates[1] < o.combates[1]) {
                return 1;
            } else {
                if (this.combates[2] > o.combates[2]) {
                    return -1;
                } else if (this.combates[2] < o.combates[2]) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder verInventario = new StringBuilder();
        StringBuilder verEquipado = new StringBuilder();

        for (Iterator<Equipamiento> iterator = inventario.iterator(); iterator.hasNext();) {
            Equipamiento next = iterator.next();
            verInventario.append(next.toString());
        }

        for (Equipamiento equipamiento : equipado) {
            if (equipamiento != null) {
                verEquipado.append(equipamiento);
            }
        }

        return "nombre= " + getNombre() + " nivel= " + getNivel() + " experiencia= " + getExpAcumulada() + "/" + getExpSigNivel() + " puntosAtrSinUsar= " + getPuntosAtrSinUsar() + "\nfuerza= " + getFuerza() + " armadura= " + getArmadura() + " destreza= " + getDestreza() + " constitucion= " + getConstitucion() + "\nvida= " + getVidaActual() + "/" + getVidaMax() + " ataque= " + getAtaqueMin() + "-" + getAtaqueMax() + " defensa= " + getDefensaMin() + "-" + getDefensaMax() + "\noroActual= " + getOroActual() + " vitorias= " + getCombates()[0] + " empates= " + getCombates()[1] + " derrotas= " + getCombates()[2] + "\ntotalAta= " + getTotalAtaDef()[0] + " totalDef= " + getTotalAtaDef()[1] + " misiones= " + getMisiones()[0] + " oroTotal= " + getMisiones()[1] + "\nmisionActiva=\n" + getMisionActiva() + "\ninventario=\n" + verInventario + "\nequipado=\n" + verEquipado;
    }
}
