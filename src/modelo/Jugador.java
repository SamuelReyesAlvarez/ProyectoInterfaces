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
 */
public class Jugador implements Comparable<Jugador> {

    // Constantes
    private static final int LONG_MIN_NOMBRE = 3;
    private static final int LONG_MAX_NOMBRE = 15;
    //private static final String FORMATO_NOMBRE = "[a-zA-Z]{3,15}";
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

    // Atributos
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

    // Getters
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

    // Setters
    private void setNombre(String nombre) throws JuegoException {
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
    // No funciona el String.matches(regexp)
    private void setNombre(String nombre) throws JuegoException {
        if (nombre.matches(FORMATO_NOMBRE)) {
            throw new JuegoException("El nombre debe contener entre 3 y 15 letras,\nsin espacios ni numeros ni caracteres especiales");
        }
        this.nombre = nombre;
    }
     */
    protected void setExpAcumulada(int expGanada) throws JuegoException {
        this.expAcumulada += expGanada;

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
        expSigNivel = (int) Math.pow(EXPERIENCIA_BASE, nivel);
    }

    private void setPuntosAtrSinUsar(int puntosAtrSinUsar) throws JuegoException {
        if (puntosAtrSinUsar < 0) {
            throw new JuegoException("Atributos sin usar no deben ser negativos");
        }
        this.puntosAtrSinUsar = puntosAtrSinUsar;
    }

    private void setAtributosJugador(int[] atributos) {
        atributosJugador = atributos;
        setVidaMax();
        setAtaqueMin();
        setAtaqueMax();
        setDefensaMin();
        setDefensaMax();
    }

    private void setAtributosEquipo(int[] atributos) {
        atributosEquipo = atributos;
        setVidaMax();
        setAtaqueMin();
        setAtaqueMax();
        setDefensaMin();
        setDefensaMax();
    }

    private void setVidaMax() {
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
        ataqueMin = ATAQUE_MIN_BASE + ((atributosJugador[0] + atributosEquipo[0]) * VALOR_FUERZA);
    }

    private void setAtaqueMax() {
        ataqueMax = ATAQUE_MAX_BASE + ((atributosJugador[0] + atributosEquipo[0]) * VALOR_FUERZA) + ((atributosJugador[2] + atributosEquipo[2]) * VALOR_DESTREZA);
    }

    private void setDefensaMin() {
        defensaMin = DEFENSA_MIN_BASE + ((atributosJugador[1] + atributosEquipo[1]) * VALOR_ARMADURA);
    }

    private void setDefensaMax() {
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

        for (Equipamiento equipo : this.inventario) {
            if (equipo.isEquipado()) {
                Equipamiento aEquipar = inventario.get(inventario.indexOf(equipo));
                inventario.remove(equipo);
                setEquipado(aEquipar);
            }
        }

        actualizarInventario(null, false);
    }

    private void setEquipado(Equipamiento nuevoEquipado) throws JuegoException {
        if (nuevoEquipado != null) {
            if (equipado[nuevoEquipado.getTipo().ordinal()] != null) {
                Equipamiento viejoEquipado = equipado[nuevoEquipado.getTipo().ordinal()];
                equipado[nuevoEquipado.getTipo().ordinal()] = nuevoEquipado;
                viejoEquipado.setEquipado(false);
                inventario.add(viejoEquipado);
                setInventario(inventario);
            } else {
                equipado[nuevoEquipado.getTipo().ordinal()] = nuevoEquipado;
            }
        }
    }

    private void actualizarAtributosEquipar() {
        atributosEquipo = new int[]{0, 0, 0, 0};
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
        setAtributosEquipo(atributosEquipo);
    }

    private void actualizarAtributosDesequipar(Equipamiento equipamiento) {
        if (equipamiento != null) {
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
        setAtributosEquipo(atributosEquipo);
    }

    private Equipamiento crearEquipo() throws JuegoException {
        Random r = new Random();

        if (r.nextInt(PROBABILIDAD_CREAR_EQUIPO) == 0) {
            TipoEquipamiento tipo = TipoEquipamiento.values()[r.nextInt(TipoEquipamiento.values().length)];
            String nombre = tipo.name().toString();
            int nivel = (this.nivel / 10) + 1;
            int potenciado = r.nextInt(nivel);

            return new Equipamiento(this, tipo, nivel, potenciado, false);
        } else {
            return null;
        }
    }

    // Metodos de flujo de juego
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

    public String subirMisiones(int recompensa) throws JuegoException {
        misiones[0]++;
        misiones[1] += recompensa * nivel;
        setOroActual(oroActual + (recompensa * nivel));
        return String.valueOf(recompensa * nivel);
    }

    public String avanzarMision() throws JuegoException {
        Equipamiento equipo;
        StringBuilder mensaje = new StringBuilder();
        try {
            misionActiva.disminuirDuracion();
        } catch (JuegoException e) {
            mensaje.append(e.getMessage());
            mensaje.append("\nRecibe: " + subirMisiones(misionActiva.getRecompensa()) + " monedas de oro");
            equipo = crearEquipo();
            if (equipo != null) {
                mensaje.append(" y un objeto nuevo");
                mensaje.append(actualizarInventario(equipo, true));
            }
            setMisionActiva(null);
            return mensaje.toString();
        }
        return "";
    }

    public String actualizarInventario(Equipamiento equipamiento, boolean aniadir) {
        String mensaje = "";
        if (equipamiento != null) {
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
        Collections.sort(inventario);
        return mensaje;
    }

    public void equiparDesequipar(Equipamiento equipo, boolean equipar) throws JuegoException {
        Equipamiento miEquipo;

        if (equipo != null) {
            if (equipar) {
                miEquipo = inventario.get(inventario.indexOf(equipo));
                miEquipo.setEquipado(equipar);
                setInventario(inventario);
                actualizarAtributosEquipar();
            } else {
                miEquipo = equipado[equipo.getTipo().ordinal()];
                equipado[equipo.getTipo().ordinal()] = null;
                miEquipo.setEquipado(equipar);
                actualizarInventario(miEquipo, true);
                actualizarAtributosDesequipar(equipo);
            }
        }
    }

    public void mejorarEquipo(Equipamiento equipo) throws JuegoException {
        if (getOroActual() > equipo.getValorPotenciar()) {
            oroActual -= equipo.getValorPotenciar();
            equipo.aumentarPotenciado();
            if (equipo.isEquipado()) {
                actualizarAtributosEquipar();
            }
        } else {
            throw new JuegoException("No dispones de oro suficiente");
        }
    }

    public void venderEquipo(Equipamiento equipo) throws JuegoException {
        if (inventario.remove(equipo)) {
            setOroActual((int) (oroActual + (equipo.getValor() / TASA_VENTA_DIRECTA)));
            throw new JuegoException("Has vendido el objeto por " + (equipo.getValor() / TASA_VENTA_DIRECTA));
        } else if (equipado[equipo.getTipo().ordinal()].equals(equipo)) {
            equipado[equipo.getTipo().ordinal()] = null;
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
