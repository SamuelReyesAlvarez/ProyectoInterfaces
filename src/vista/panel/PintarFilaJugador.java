package vista.panel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Samuel Reyes
 *
 */
public class PintarFilaJugador extends DefaultTableCellRenderer {

    private String nombre;
    private int columna;

    public PintarFilaJugador(String nombre, int colorPatron) {
        this.nombre = nombre;
        this.columna = colorPatron;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.getValueAt(row, columna).equals(nombre)) {
            this.setOpaque(true);
            this.setBackground(Color.LIGHT_GRAY);
        } else {
            this.setOpaque(false);
        }

        return this;
    }
}
