package modelo;

/**
 *
 * @author Samuel Reyes
 *
 * Define las caracteristicas y funciones basicas de los objetos equipables por
 * el jugador
 *
 */
public class Equipamiento implements Comparable<Equipamiento> {

    // Valores predefinidos para el objeto
    private static final int VALOR_BASE = 500;
    private static final int VALOR_POTENCIADO = 150;

    // Valor de identificacion unica para cada objeto
    private static int idProximo = 1;

    // Atributos que definen al objeto
    private Jugador propietario;
    private TipoEquipamiento tipo;
    private int identificador;
    private int nivel;
    private int potenciado;
    private int precio;
    private boolean equipado;
    private boolean enVenta;

    // Constructor principal
    public Equipamiento(Jugador propietario, TipoEquipamiento tipo, int nivel, int potenciado, boolean equipado) throws JuegoException {
        setPropietario(propietario);
        this.tipo = tipo;
        this.identificador = idProximo;
        this.nivel = nivel;
        this.potenciado = potenciado;
        setEquipado(equipado);
        idProximo++;
    }

    // Constructor para patron de busqueda
    public Equipamiento(int id) {
        identificador = id;
    }

    // Metodos que devuelven los valores definidos en el objetos
    public int getIdProximo() {
        return idProximo;
    }

    public Jugador getPropietario() {
        return propietario;
    }

    public TipoEquipamiento getTipo() {
        return tipo;
    }

    public int getIdentificador() {
        return identificador;
    }

    public int getNivel() {
        return nivel;
    }

    public int getPotenciado() {
        return potenciado;
    }

    public int getValor() {
        return (VALOR_BASE * nivel) + (VALOR_POTENCIADO * potenciado * nivel);
    }

    public int getValorPotenciar() {
        return nivel * (potenciado + 1) * VALOR_POTENCIADO;
    }

    public int getPrecio() {
        return precio;
    }

    public boolean isEquipado() {
        return equipado;
    }

    public boolean isEnVenta() {
        return enVenta;
    }

    // Metodos que establecen los valores que definen al objeto
    protected void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    public void setPrecio(int precio) throws JuegoException {
        if (precio > 0) {
            this.precio = precio;
        } else {
            throw new JuegoException("El precio debe ser positivo");
        }
    }

    public void setEquipado(boolean equipado) throws JuegoException {
        this.equipado = equipado;
        if (equipado) {
            setEnVenta(false, precio);
        }
    }

    public void setEnVenta(boolean enVenta, int precio) throws JuegoException {
        this.enVenta = enVenta;
        if (enVenta) {
            equipado = false;
            setPrecio(precio);
        }
    }

    // Permite subir el nivel de mejora de un objeto
    public void aumentarPotenciado() throws JuegoException {
        if (potenciado == nivel * 10) {
            throw new JuegoException("Limite de potenciado alcanzado");
        }
        potenciado++;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.identificador;
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
        final Equipamiento other = (Equipamiento) obj;
        if (this.identificador != other.identificador) {
            return false;
        }
        return true;
    }

    // Ya no es necesario comparar por "equipado"
    // Pendiente de revisión
    @Override
    public int compareTo(Equipamiento o) {
        if (this.equipado && !o.equipado) {
            return 1;
        } else if (this.equipado && o.equipado) {
            return 0;
        } else if (!this.equipado && !o.equipado) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return getTipo() + " nvl " + getNivel() + " +" + getPotenciado() + " valor " + getValor();
    }

    // Formato para los botones de la ventana Estado
    public String textoBoton() {
        return "<html><p>" + getTipo() + "<br/>nvl " + getNivel() + " + " + getPotenciado() + "<br/>valor " + getValor() + "</p></html>";
    }
}
