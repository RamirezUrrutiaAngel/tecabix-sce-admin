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
package mx.tecabix.view.plantel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mx.tecabix.db.entity.Direccion;
import mx.tecabix.db.entity.Estado;
import mx.tecabix.db.entity.Municipio;
import mx.tecabix.db.entity.Plantel;
import mx.tecabix.db.entity.Trabajador;
import mx.tecabix.service.Auth;
import mx.tecabix.service.controller.EstadoController;
import mx.tecabix.service.controller.MunicipioController;
import mx.tecabix.service.controller.PlantelController;
import mx.tecabix.service.page.EstadoPage;
import mx.tecabix.service.page.MunicipioPage;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.Proceso;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class JDialogPlantelNuevo extends javax.swing.JDialog {

    private Auth auth = new Auth();
    private List<Estado> estados;
    private List<Municipio> municipios;
    private Trabajador trabajador;
    private JFrame parent;
    private JDialog THIS;
    private JPanelPlantel jPanelPlantel;
    public JDialogPlantelNuevo(java.awt.Frame parent, JPanelPlantel jPanelPlantel) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        this.jPanelPlantel = jPanelPlantel;
        init();
    }

    private void init(){
        THIS = this;
        Proceso p = new Proceso(parent) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(30);
                Exception ex = cargarEstado();
                if(ex != null){
                    this.setVisible(false);
                    ErrorLog err = new ErrorLog(parent, ex);
                    return;
                }
                getjProgressBar().setValue(70);
                ex = cargarMunicipio();
                if(ex != null){
                    this.setVisible(false);
                    ErrorLog err = new ErrorLog(parent, ex);
                    return;
                }
                this.setVisible(false);
                THIS.setVisible(true);
            }
        };
        
    }
    private Exception cargarEstado(){
        try {
            EstadoController ec = new EstadoController();
            byte max=Byte.MAX_VALUE;
            short zero = 0;
            EstadoPage page =  ec.find(new String(), 0, max , zero);
            estados = page.getData();
            if(estados == null){
                estados = new ArrayList<>();
            }
            estados.sort(new Comparator<Estado>() {
                @Override
                public int compare(Estado o1, Estado o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            for (Estado estado : estados) {
                jComboBoxEstado.addItem(estado.getNombre());
            }
            jComboBoxEstado.setSelectedIndex(-1);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(JDialogPlantelNuevo.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }
    
    private Exception cargarMunicipio(){
        try {
            MunicipioController mc = new MunicipioController();
            int select = jComboBoxEstado.getSelectedIndex();
            if(select < 0 || estados.size() <= 0){
                return null;
            }
            Estado estado = estados.get(select);
            MunicipioPage page = mc.findByEstadoClave(estado.getClave());
            municipios = page.getData();
            municipios.sort(new Comparator<Municipio>() {
                @Override
                public int compare(Municipio o1, Municipio o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            jComboBoxMunicipio.removeAllItems();
            for (Municipio municipio : municipios) {
                jComboBoxMunicipio.addItem(municipio.getNombre());
            }
            jComboBoxMunicipio.setSelectedIndex(-1);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(JDialogPlantelNuevo.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldGerente = new javax.swing.JTextField();
        jButtonBuscarGerente = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCalle = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldNumInt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldNumExt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxEstado = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxMunicipio = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldCodigoPostal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldAsentamiento = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneReferencia = new javax.swing.JTextPane();
        jButtonGuardar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo Plantel");
        setMaximumSize(new java.awt.Dimension(394, 524));
        setMinimumSize(new java.awt.Dimension(394, 524));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Entidad"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre:");

        jTextFieldNombre.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyTyped(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Gerente:");

        jTextFieldGerente.setEditable(false);
        jTextFieldGerente.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldGerente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldGerenteMouseClicked(evt);
            }
        });

        jButtonBuscarGerente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/lupa.png"))); // NOI18N
        jButtonBuscarGerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarGerenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNombre))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBuscarGerente, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldGerente)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldGerente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarGerente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Direccion"));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Calle:");

        jTextFieldCalle.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCalleKeyTyped(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Num Int:");

        jTextFieldNumInt.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNumInt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumIntKeyTyped(evt);
            }
        });

        jLabel6.setText("Num Ext:");

        jTextFieldNumExt.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNumExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumExtKeyTyped(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Estado:");

        jComboBoxEstado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxEstadoItemStateChanged(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Municipio:");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Codigo Postal:");

        jTextFieldCodigoPostal.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCodigoPostal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoPostalKeyTyped(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Asentamiento:");

        jTextFieldAsentamiento.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldAsentamiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldAsentamientoKeyTyped(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Referencia:");

        jTextPaneReferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPaneReferenciaKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPaneReferencia);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCalle))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNumInt, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNumExt, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxMunicipio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCodigoPostal))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldAsentamiento))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBoxMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldNumInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldNumExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldAsentamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButtonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/disquete.png"))); // NOI18N
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/cancelar.png"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonCancelar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyTyped
        String aux = jTextFieldNombre.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Plantel.SIZE_NOMBRE, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyTyped

    private void jComboBoxEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxEstadoItemStateChanged
        Proceso p = new Proceso(this) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(70);
                Exception ex = cargarMunicipio();
                if(ex != null){
                    this.setVisible(false);
                    THIS.setVisible(false);
                    THIS.dispose();
                    ErrorLog err = new ErrorLog(parent, ex);
                    return;
                }
//                this.setVisible(false);
            }
        };
    }//GEN-LAST:event_jComboBoxEstadoItemStateChanged

    private void jButtonBuscarGerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarGerenteActionPerformed
        JDialogPlantelSearchTrabajador searchGerente = new JDialogPlantelSearchTrabajador(this);
        if(searchGerente.getTrabajador() != null){
            trabajador = searchGerente.getTrabajador();
            jTextFieldGerente.setText(trabajador.getSeguroSocial().getCURP());
        }
    }//GEN-LAST:event_jButtonBuscarGerenteActionPerformed

    private int clic = 0;
    private void jTextFieldGerenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldGerenteMouseClicked
        Thread h = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clic+=1;
                    if(clic > 1){
                        JDialogPlantelSearchTrabajador searchGerente = new JDialogPlantelSearchTrabajador(THIS);
                        if(searchGerente.getTrabajador() != null){
                            trabajador = searchGerente.getTrabajador();
                            jTextFieldGerente.setText(trabajador.getSeguroSocial().getCURP());
                        }
                    }
                    Thread.sleep(500);
                    clic=0;
                } catch (InterruptedException ex) {
                    Logger.getLogger(JDialogPlantelSearchTrabajador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        h.start();
    }//GEN-LAST:event_jTextFieldGerenteMouseClicked

    private void jTextFieldCalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCalleKeyTyped
        String aux = jTextFieldCalle.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_CALLE, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCalleKeyTyped

    private void jTextFieldNumIntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumIntKeyTyped
        String aux = jTextFieldNumInt.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_NUM_INT, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumIntKeyTyped

    private void jTextFieldNumExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumExtKeyTyped
        String aux = jTextFieldNumExt.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_NUM_EXT, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumExtKeyTyped

    private void jTextFieldCodigoPostalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoPostalKeyTyped
        String aux = jTextFieldCodigoPostal.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_NUMERIC, Direccion.SIZE_CODIGO_POSTAL, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCodigoPostalKeyTyped

    private void jTextFieldAsentamientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAsentamientoKeyTyped
        String aux = jTextFieldAsentamiento.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_ASENTAMIENTO, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldAsentamientoKeyTyped

    private void jTextPaneReferenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPaneReferenciaKeyTyped
        String aux = jTextPaneReferencia.getText()+evt.getKeyChar();
        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_REFERENCIA, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextPaneReferenciaKeyTyped

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        Proceso p = new Proceso(THIS) {
            @Override
            public void proceso() {
                try {
                    getjProgressBar().setValue(40);
                    Direccion direccion = new Direccion();
                    String calle = jTextFieldCalle.getText();
                    String numInt = jTextFieldNumInt.getText();
                    String numExt = jTextFieldNumExt.getText();
                    String cp = jTextFieldCodigoPostal.getText();
                    String asentamiento = jTextFieldAsentamiento.getText();
                    String referencia =jTextPaneReferencia.getText();
                    if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_CALLE, calle)) {
                        JOptionPane.showMessageDialog(this, "La calle no es valida", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setCalle(calle);
                    if(numInt != null && !numInt.trim().isEmpty()){
                        if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_NUM_INT,  numInt)) {
                            JOptionPane.showMessageDialog(this, "El Num Int no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        direccion.setNumInt(numInt);
                    }
                    if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_NUM_EXT, numExt)) {
                        JOptionPane.showMessageDialog(this, "El Num Ext no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setNumExt(numExt);
                    if(auth.isNotValid(Auth.TIPO_NUMERIC, Direccion.SIZE_CODIGO_POSTAL, cp)) {
                        JOptionPane.showMessageDialog(this, "El C.P. no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setCodigoPostal(cp);
                    if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_ASENTAMIENTO, asentamiento)) {
                        JOptionPane.showMessageDialog(this, "El Asentamiento no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setAsentamiento(asentamiento);
                    if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_REFERENCIA, referencia)) {
                        JOptionPane.showMessageDialog(this, "La Referencia no es valida", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setReferencia(referencia);
                    int select  = jComboBoxMunicipio.getSelectedIndex();
                    if( select < 0){
                        JOptionPane.showMessageDialog(this, "El municipio no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    direccion.setMunicipio(municipios.get(select));
                    Plantel plantel = new Plantel();
                    plantel.setDireccion(direccion);
                    String nombre = jTextFieldNombre.getText();
                    if(auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Plantel.SIZE_NOMBRE, nombre)) {
                        JOptionPane.showMessageDialog(this, "El nombre no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    plantel.setNombre(nombre);
                    if(trabajador == null){
                        JOptionPane.showMessageDialog(this, "El Gerente no es valido", "warnig", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    plantel.setGerente(trabajador);
                    getjProgressBar().setValue(80);
                    PlantelController plantelController = new PlantelController();
                    plantelController.save(plantel);
                    THIS.dispose();
                    jPanelPlantel.init();
                } catch (Exception ex) {
                    Logger.getLogger(JDialogPlantelNuevo.class.getName()).log(Level.SEVERE, null, ex);
                    setVisible(false);
                    ErrorLog err = new ErrorLog(THIS, ex);
                }
            }
        };
    }//GEN-LAST:event_jButtonGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarGerente;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JComboBox<String> jComboBoxEstado;
    private javax.swing.JComboBox<String> jComboBoxMunicipio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldAsentamiento;
    private javax.swing.JTextField jTextFieldCalle;
    private javax.swing.JTextField jTextFieldCodigoPostal;
    private javax.swing.JTextField jTextFieldGerente;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldNumExt;
    private javax.swing.JTextField jTextFieldNumInt;
    private javax.swing.JTextPane jTextPaneReferencia;
    // End of variables declaration//GEN-END:variables
}
