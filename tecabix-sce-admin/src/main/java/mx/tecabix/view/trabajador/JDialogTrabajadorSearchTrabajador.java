/*
 *   This file is part of Foobar.
 *
 *   Foobar is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Foobar is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package mx.tecabix.view.trabajador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import mx.tecabix.db.entity.PersonaFisica;
import mx.tecabix.db.entity.Trabajador;
import mx.tecabix.service.PageGeneric;
import mx.tecabix.service.controller.TrabajadorController;
import mx.tecabix.service.page.TrabajadorPage;
import mx.tecabix.view.BotonPagina;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.ModelT;
import mx.tecabix.view.Proceso;
import mx.tecabix.view.sesion.JPanelSesion;

/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class JDialogTrabajadorSearchTrabajador extends javax.swing.JDialog {

    private Trabajador trabajador;
    private String search;
    private JDialog parent;
    private ModelT defaultTableModel;
    private List<Trabajador> trabajadores;
    private short pagina;
    
    private final short NOMBRE = 0;
    private final short CURP = 1;
    private final short NUM_COLUMNAS = 2;
    
    public JDialogTrabajadorSearchTrabajador(JDialog parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        this.parent = parent;
        init();
        setVisible(true);
    }
    
    private void paginacion(PageGeneric page, short pagina) {
        jPanelPaginacion.removeAll();

        short inicio = 0, fin = 0;
        inicio = (pagina < 3) ? 0 : (short) (pagina - 2);
        fin = pagina;
        if (pagina > 0) {
            BotonPagina botonIzquierdaTotal = new BotonPagina((short) (0), BotonPagina.IZQUIERDA_TOTAL) {
                @Override
                public void action(short x) {
                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            BotonPagina botonIzquierda = new BotonPagina((short) (pagina - 1), BotonPagina.IZQUIERDA) {
                @Override
                public void action(short x) {
                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            jPanelPaginacion.add(botonIzquierdaTotal);
            jPanelPaginacion.add(botonIzquierda);
        } else {
            jPanelPaginacion.add(new JButton("<<"));
            jPanelPaginacion.add(new JButton("<"));
        }
        for (short i = inicio; i < fin; i++) {
            BotonPagina botonPagina = new BotonPagina(i) {
                @Override
                public void action(short x) {
                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            jPanelPaginacion.add(botonPagina);
        }
        jPanelPaginacion.add(new JButton("-" + (pagina + 1) + "-"));
        inicio = (short) (pagina + 1);
        fin = (short) ((page.getTotalPages() - pagina >= 3) ? pagina + 3 : page.getTotalPages());
        for (short i = inicio; i < fin; i++) {
            BotonPagina botonPagina = new BotonPagina(i) {
                @Override
                public void action(short x) {
                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            jPanelPaginacion.add(botonPagina);
        }
        if (pagina == page.getTotalPages() - 1) {
            jPanelPaginacion.add(new JButton(">"));
            jPanelPaginacion.add(new JButton(">>"));
        } else {
            BotonPagina botonDerechaTotal = new BotonPagina((short) (page.getTotalPages() - 1), BotonPagina.DERECHA_TOTAL) {
                @Override
                public void action(short x) {

                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            BotonPagina botonDerecha = new BotonPagina((short) (pagina + 1), BotonPagina.DERECHA) {
                @Override
                public void action(short x) {

                    Proceso proceso = new Proceso(parent) {
                        @Override
                        public void proceso() {
                            getjProgressBar().setValue(80);
                            cargarDatos(x);
                            counstrurTabla();
                        }
                    };
                }
            };
            jPanelPaginacion.add(botonDerecha);
            jPanelPaginacion.add(botonDerechaTotal);
        }

    }
    
    private void cargarDatos(short page) {
        try {
            this.pagina = page;
            TrabajadorController trabajadorController = new TrabajadorController();
            byte elements = 25;
           
            int by = jComboBoxBy.getSelectedIndex();
            if (search == null) {
                search = new String();
            }
            TrabajadorPage response = trabajadorController.find(search, by, elements, page);
            if (response != null) {
                this.trabajadores = response.getData();
            }
            paginacion(response, pagina);
        } catch (Exception ex) {
            Logger.getLogger(JPanelSesion.class.getName()).log(Level.SEVERE, null, ex);
            ErrorLog errorLog = new ErrorLog(parent, ex);
        }
    }
    
    public void init(){
        new Proceso(parent) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(80);
                cargarDatos(pagina);
                counstrurTabla();
            }
        };
    }
    
    private void counstrurTabla(){
        cargarTabla();
        formatearTabla();
    }
    
    private void cargarTabla(){
        try {
            String[] header = new String[NUM_COLUMNAS];
            header[NOMBRE]      = "NOMBRE";
            header[CURP]      = "CURP";
            Class[] headerType = new Class[NUM_COLUMNAS];

            headerType[NOMBRE] = java.lang.String.class;
            headerType[CURP] = java.lang.String.class;
            defaultTableModel = new ModelT(header,headerType);
            for (int i = 0; i < trabajadores.size(); i++) {
                Trabajador trabajador = trabajadores.get(i);
                PersonaFisica persona = trabajador.getPersonaFisica();
                StringBuilder nombre =  new StringBuilder(persona.getNombre());
                if(persona.getApellidoPaterno() != null){
                    nombre.append(" ").append(persona.getApellidoPaterno());
                }
                if(persona.getApellidoMaterno() != null){
                    nombre.append(" ").append(persona.getApellidoMaterno());
                }
                
                
                defaultTableModel.addRow(new java.util.Vector());
                defaultTableModel.setValueAt(nombre.toString(),  i, 0);
                defaultTableModel.setValueAt(trabajador.getSeguroSocial().getCURP(),  i, 1);
            }
            jTable.setModel(defaultTableModel);
            if(!this.trabajadores.isEmpty()){
                jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jTable.setRowSelectionInterval(0, 0);
            }
            
        } catch (Exception e) {
            ErrorLog error = new ErrorLog(parent, e);
        }
    }

    private void formatearTabla(){
        try {
            jTable.setFont(new Font("Arial",Font.PLAIN,16));
            jTable.setRowHeight(25);
            jTable.getColumnModel().getColumn(NOMBRE).setMinWidth(250);
            jTable.getColumnModel().getColumn(CURP).setMaxWidth(300);
            jTable.getColumnModel().getColumn(CURP).setMinWidth(220);
            jTable.setShowHorizontalLines(false);
            jTable.setShowVerticalLines(false);
            jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                    if(row%2==0){
                        component.setBackground(Color.decode("#f9f9f9"));

                    }else{
                        component.setBackground(Color.WHITE);
                    }
                    if(isSelected){
                        component.setBackground(Color.decode("#0048ff"));
                    }
                    return component;
                }
            });
            jTable.setDefaultRenderer(Float.class, new DefaultTableCellRenderer(){
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                    this.setHorizontalAlignment(RIGHT);
                    if(row%2==0){
                        component.setBackground(Color.decode("#f9f9f9"));
                    }else{
                        component.setBackground(Color.WHITE);
                    }
                    if(column==2){
                        component.setBackground(Color.decode("#e1ffd8"));
                    }
                    if(isSelected){
                        component.setBackground(Color.decode("#0048ff"));
                    }
                    return component;
                }
            });
            jTable.getTableHeader().setReorderingAllowed(false) ;
            jTable.add(this.jPopupMenuSeleccionar);
        } catch (Exception e) {
             ErrorLog error = new ErrorLog(parent, e);
        }
    }
    
    private void select(){
        int select = jTable.getSelectedRow();
        if(select >= 0 && trabajadores.size() > 0 ){
            trabajador = trabajadores.get(select);
        }
        dispose();
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuSeleccionar = new javax.swing.JPopupMenu();
        jMenuItemSeleccionar = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBoxBy = new javax.swing.JComboBox<>();
        jTextFieldSearch = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanelPaginacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        jMenuItemSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/cheque.png"))); // NOI18N
        jMenuItemSeleccionar.setText("Seleccionar");
        jPopupMenuSeleccionar.add(jMenuItemSeleccionar);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleccionar Trabajador");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/cheque.png"))); // NOI18N
        jButton1.setText("Seleccionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/cancelar.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/buscar.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DES" }));
        jComboBox2.setEnabled(false);

        jComboBoxBy.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jComboBoxBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "A. Paterno", "A. Materno", "CURP" }));

        jTextFieldSearch.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldSearch)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addComponent(jComboBox2)
                    .addComponent(jComboBoxBy))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelPaginacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 569, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        init();
    }//GEN-LAST:event_jButton4ActionPerformed

    private int clic = 0;
    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        init();
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        trabajador = null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        select();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        Thread h = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clic+=1;
                    if(clic > 1){
                        select();
                    }
                    Thread.sleep(500);
                    clic=0;
                } catch (InterruptedException ex) {
                    Logger.getLogger(JDialogTrabajadorSearchTrabajador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        h.start();
    }//GEN-LAST:event_jTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBoxBy;
    private javax.swing.JMenuItem jMenuItemSeleccionar;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelPaginacion;
    private javax.swing.JPopupMenu jPopupMenuSeleccionar;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
