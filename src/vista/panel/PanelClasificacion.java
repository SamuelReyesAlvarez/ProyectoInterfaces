/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.panel;

import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class PanelClasificacion extends javax.swing.JPanel {

    /**
     * Creates new form PanelClasificacion
     */
    public PanelClasificacion() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTitulo = new javax.swing.JPanel();
        etTitulo = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        clasificacion = new javax.swing.JTable();

        setEnabled(false);
        setMaximumSize(new java.awt.Dimension(700, 550));
        setMinimumSize(new java.awt.Dimension(700, 550));
        setPreferredSize(new java.awt.Dimension(700, 550));
        setLayout(new java.awt.BorderLayout());

        etTitulo.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        etTitulo.setText("Clasificacion general");
        panelTitulo.add(etTitulo);

        add(panelTitulo, java.awt.BorderLayout.NORTH);

        clasificacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Puesto", "Nombre", "Nivel", "Combates", "Victorias", "Empates", "Derrotas", "Misiones", "Oro total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        clasificacion.setFocusable(false);
        clasificacion.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(clasificacion);
        if (clasificacion.getColumnModel().getColumnCount() > 0) {
            clasificacion.getColumnModel().getColumn(0).setResizable(false);
            clasificacion.getColumnModel().getColumn(0).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(1).setResizable(false);
            clasificacion.getColumnModel().getColumn(1).setPreferredWidth(120);
            clasificacion.getColumnModel().getColumn(2).setResizable(false);
            clasificacion.getColumnModel().getColumn(2).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(3).setResizable(false);
            clasificacion.getColumnModel().getColumn(3).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(4).setResizable(false);
            clasificacion.getColumnModel().getColumn(4).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(5).setResizable(false);
            clasificacion.getColumnModel().getColumn(5).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(6).setResizable(false);
            clasificacion.getColumnModel().getColumn(6).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(7).setResizable(false);
            clasificacion.getColumnModel().getColumn(7).setPreferredWidth(50);
            clasificacion.getColumnModel().getColumn(8).setResizable(false);
            clasificacion.getColumnModel().getColumn(8).setPreferredWidth(100);
        }

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable clasificacion;
    private javax.swing.JLabel etTitulo;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables

    public void addControlador(MouseListener ml) {
        clasificacion.addMouseListener(ml);
    }

    public void setDatosTabla(Object[][] datos, String nombre) {
        String[] cabecera = new String[clasificacion.getColumnCount()];

        for (int i = 0; i < cabecera.length; i++) {
            cabecera[i] = clasificacion.getColumnName(i);
        }

        clasificacion.setModel(new DefaultTableModel(datos, cabecera));
        clasificacion.setDefaultRenderer(Object.class, new PintarFilaJugador(nombre, 1));
    }

    public JTable getTabla() {
        return clasificacion;
    }
}
