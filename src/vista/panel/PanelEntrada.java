/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.panel;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Samuel Reyes
 *
 */
public class PanelEntrada extends javax.swing.JPanel {

    /**
     * Creates new form PanelEntrada
     */
    public PanelEntrada() {
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

        separador = new javax.swing.JSplitPane();
        panelIzquierdo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        etNuevaPartida = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        etNombre = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        btnNuevaPartida = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        ayuda = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        panelDerecho = new javax.swing.JPanel();
        panelDerechoNorte = new javax.swing.JPanel();
        etGuardadas = new javax.swing.JLabel();
        panelDerechoCentro = new javax.swing.JPanel();
        scrollTabla = new javax.swing.JScrollPane();
        tblGuardadas = new javax.swing.JTable();
        panelDerechoSur = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(550, 350));
        setLayout(new java.awt.BorderLayout());

        separador.setDividerLocation(150);
        separador.setDividerSize(1);
        separador.setEnabled(false);
        separador.setOpaque(false);

        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new java.awt.GridLayout(9, 1));

        etNuevaPartida.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        etNuevaPartida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etNuevaPartida.setText("Nueva partida");
        etNuevaPartida.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etNuevaPartida, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etNuevaPartida, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelIzquierdo.add(jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        panelIzquierdo.add(jPanel2);

        etNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etNombre.setText("Nombre de jugador:");
        etNombre.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelIzquierdo.add(jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelIzquierdo.add(jPanel4);

        btnNuevaPartida.setText("Crear partida");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevaPartida, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnNuevaPartida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelIzquierdo.add(jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 149, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        panelIzquierdo.add(jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        panelIzquierdo.add(jPanel7);

        ayuda.setText("?");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ayuda)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ayuda)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelIzquierdo.add(jPanel8);

        btnSalir.setText("Salir del juego");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelIzquierdo.add(jPanel9);

        separador.setLeftComponent(panelIzquierdo);

        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new java.awt.BorderLayout());

        etGuardadas.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        etGuardadas.setText("Partidas Guardadas");
        panelDerechoNorte.add(etGuardadas);

        panelDerecho.add(panelDerechoNorte, java.awt.BorderLayout.PAGE_START);

        tblGuardadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Jugador", "Turno", "Posicion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGuardadas.setOpaque(false);
        tblGuardadas.setShowHorizontalLines(false);
        tblGuardadas.setShowVerticalLines(false);
        tblGuardadas.getTableHeader().setResizingAllowed(false);
        tblGuardadas.getTableHeader().setReorderingAllowed(false);
        scrollTabla.setViewportView(tblGuardadas);
        if (tblGuardadas.getColumnModel().getColumnCount() > 0) {
            tblGuardadas.getColumnModel().getColumn(0).setResizable(false);
            tblGuardadas.getColumnModel().getColumn(1).setResizable(false);
            tblGuardadas.getColumnModel().getColumn(2).setResizable(false);
            tblGuardadas.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout panelDerechoCentroLayout = new javax.swing.GroupLayout(panelDerechoCentro);
        panelDerechoCentro.setLayout(panelDerechoCentroLayout);
        panelDerechoCentroLayout.setHorizontalGroup(
            panelDerechoCentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDerechoCentroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelDerechoCentroLayout.setVerticalGroup(
            panelDerechoCentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        panelDerecho.add(panelDerechoCentro, java.awt.BorderLayout.CENTER);

        btnCargar.setText("Cargar partida");
        panelDerechoSur.add(btnCargar);

        btnBorrar.setText("Borrar partida");
        panelDerechoSur.add(btnBorrar);

        panelDerecho.add(panelDerechoSur, java.awt.BorderLayout.PAGE_END);

        separador.setRightComponent(panelDerecho);

        add(separador, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ayuda;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnNuevaPartida;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel etGuardadas;
    private javax.swing.JLabel etNombre;
    private javax.swing.JLabel etNuevaPartida;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel panelDerecho;
    private javax.swing.JPanel panelDerechoCentro;
    private javax.swing.JPanel panelDerechoNorte;
    private javax.swing.JPanel panelDerechoSur;
    private javax.swing.JPanel panelIzquierdo;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JSplitPane separador;
    private javax.swing.JTable tblGuardadas;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

    public void addControlador(ActionListener al) {
        btnBorrar.addActionListener(al);
        btnBorrar.setActionCommand("borrar");
        btnCargar.addActionListener(al);
        btnCargar.setActionCommand("cargar");
        btnNuevaPartida.addActionListener(al);
        btnNuevaPartida.setActionCommand("nueva");
        btnSalir.addActionListener(al);
        btnSalir.setActionCommand("salir");
    }

    public ArrayList<String> getNombresTabla() {
        ArrayList<String> lista = new ArrayList<>();

        for (int i = 0; i < tblGuardadas.getRowCount(); i++) {
            lista.add(tblGuardadas.getValueAt(i, 1).toString());
        }

        return lista;
    }

    public void setDatosTabla(Object[][] datos) {
        String[] cabecera = new String[tblGuardadas.getColumnCount()];

        for (int i = 0; i < cabecera.length; i++) {
            cabecera[i] = tblGuardadas.getColumnName(i);
        }

        tblGuardadas.setModel(new DefaultTableModel(datos, cabecera) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public JTable getTabla() {
        return tblGuardadas;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JButton getAyuda() {
        return ayuda;
    }
}
