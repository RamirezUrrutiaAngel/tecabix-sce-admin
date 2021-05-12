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
package mx.tecabix.view.turno;

import java.awt.Frame;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.Turno;
import mx.tecabix.db.entity.TurnoDia;
import mx.tecabix.service.Auth;
import mx.tecabix.service.controller.TurnoController;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.Proceso;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class JDialogTurnoNuevo extends javax.swing.JDialog {

    private final String[] horas = new String[60 * 24];
    private Auth auth = new Auth();
    private JDialog THIS = this;
    public JDialogTurnoNuevo(java.awt.Frame parent) {
        super(parent, true);
        setLocationRelativeTo(parent);
        int index = 0;
        for (short hora = 0; hora < 24; hora++) {
            for (short minuto = 0; minuto < 60; minuto++, index++) {
                horas[index] = ((hora / 10 == 0) ? "0" + hora : "" + hora) + ":" + ((minuto / 10 == 0) ? "0" + minuto : "" + minuto);
            }
        }
        initComponents();
        setVisible(true);
    }

    private void checkAction(JSpinner inicio, JSpinner fin, boolean check) {
        inicio.setEnabled(check);
        fin.setEnabled(check);
        if (inicio.getValue().equals(horas[0]) && check) {
            inicio.setValue(horas[60 * 9]);
        }
        if (fin.getValue().equals(horas[0]) && check) {
            fin.setValue(horas[60 * 18]);
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
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldDescripcion = new javax.swing.JTextField();
        jSpinnerDomIni = new javax.swing.JSpinner();
        jCheckBoxDom = new javax.swing.JCheckBox();
        jSpinnerDomFin = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerLunFin = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerLunIni = new javax.swing.JSpinner();
        jCheckBoxLun = new javax.swing.JCheckBox();
        jSpinnerMarFin = new javax.swing.JSpinner();
        jCheckBoxMar = new javax.swing.JCheckBox();
        jSpinnerMarIni = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerMieFin = new javax.swing.JSpinner();
        jCheckBoxMie = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jSpinnerMieIni = new javax.swing.JSpinner();
        jSpinnerJueIni = new javax.swing.JSpinner();
        jCheckBoxJue = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerJueFin = new javax.swing.JSpinner();
        jSpinnerVieIni = new javax.swing.JSpinner();
        jSpinnerVieFin = new javax.swing.JSpinner();
        jCheckBoxVie = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jCheckBoxSab = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jSpinnerSabFin = new javax.swing.JSpinner();
        jSpinnerSabIni = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jButtonNuevo = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo turno");
        setMaximumSize(new java.awt.Dimension(313, 365));
        setMinimumSize(new java.awt.Dimension(313, 365));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre");

        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyTyped(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Descripcion");

        jTextFieldDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDescripcionKeyTyped(evt);
            }
        });

        jSpinnerDomIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerDomIni.setEnabled(false);

        jCheckBoxDom.setText("Domingo");
        jCheckBoxDom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxDomStateChanged(evt);
            }
        });

        jSpinnerDomFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerDomFin.setEnabled(false);

        jLabel4.setText("-");

        jSpinnerLunFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerLunFin.setEnabled(false);

        jLabel5.setText("-");

        jSpinnerLunIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerLunIni.setEnabled(false);

        jCheckBoxLun.setText("Lunes");
        jCheckBoxLun.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxLunStateChanged(evt);
            }
        });

        jSpinnerMarFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerMarFin.setEnabled(false);

        jCheckBoxMar.setText("Martes");
        jCheckBoxMar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxMarStateChanged(evt);
            }
        });

        jSpinnerMarIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerMarIni.setEnabled(false);

        jLabel6.setText("-");

        jSpinnerMieFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerMieFin.setEnabled(false);

        jCheckBoxMie.setText("Miercoles");
        jCheckBoxMie.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxMieStateChanged(evt);
            }
        });

        jLabel7.setText("-");

        jSpinnerMieIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerMieIni.setEnabled(false);

        jSpinnerJueIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerJueIni.setEnabled(false);

        jCheckBoxJue.setText("Jueves");
        jCheckBoxJue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxJueStateChanged(evt);
            }
        });

        jLabel8.setText("-");

        jSpinnerJueFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerJueFin.setEnabled(false);

        jSpinnerVieIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerVieIni.setEnabled(false);

        jSpinnerVieFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerVieFin.setEnabled(false);

        jCheckBoxVie.setText("Viernes");
        jCheckBoxVie.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxVieStateChanged(evt);
            }
        });

        jLabel9.setText("-");

        jCheckBoxSab.setText("Sabado");
        jCheckBoxSab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxSabStateChanged(evt);
            }
        });

        jLabel10.setText("-");

        jSpinnerSabFin.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerSabFin.setEnabled(false);

        jSpinnerSabIni.setModel(new javax.swing.SpinnerListModel(horas));
        jSpinnerSabIni.setEnabled(false);

        jButtonNuevo.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/disquete.png"))); // NOI18N
        jButtonNuevo.setText("Guardar");
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoActionPerformed(evt);
            }
        });

        jButtonCancelar.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/cancelar.png"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNuevo)
                    .addComponent(jButtonCancelar))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBoxLun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxDom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxMar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxMie, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jCheckBoxJue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxVie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxSab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerDomIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerDomFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerLunIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerLunFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerMarIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerMarFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerMieIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerMieFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerJueIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerJueFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerVieIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerVieFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSpinnerSabIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinnerSabFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNombre)
                            .addComponent(jTextFieldDescripcion))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerDomIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxDom)
                    .addComponent(jLabel4)
                    .addComponent(jSpinnerDomFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerLunIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxLun)
                    .addComponent(jLabel5)
                    .addComponent(jSpinnerLunFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerMarIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxMar)
                    .addComponent(jLabel6)
                    .addComponent(jSpinnerMarFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerMieIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxMie)
                    .addComponent(jLabel7)
                    .addComponent(jSpinnerMieFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerJueIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxJue)
                    .addComponent(jLabel8)
                    .addComponent(jSpinnerJueFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerVieIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxVie)
                    .addComponent(jLabel9)
                    .addComponent(jSpinnerVieFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerSabIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxSab)
                    .addComponent(jLabel10)
                    .addComponent(jSpinnerSabFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void jCheckBoxDomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxDomStateChanged
        checkAction(jSpinnerDomIni, jSpinnerDomFin, jCheckBoxDom.isSelected());
    }//GEN-LAST:event_jCheckBoxDomStateChanged

    private void jCheckBoxLunStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxLunStateChanged
        checkAction(jSpinnerLunIni, jSpinnerLunFin, jCheckBoxLun.isSelected());
    }//GEN-LAST:event_jCheckBoxLunStateChanged

    private void jCheckBoxMarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxMarStateChanged
        checkAction(jSpinnerMarIni, jSpinnerMarFin, jCheckBoxMar.isSelected());
    }//GEN-LAST:event_jCheckBoxMarStateChanged

    private void jCheckBoxMieStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxMieStateChanged
        checkAction(jSpinnerMieIni, jSpinnerMieFin, jCheckBoxMie.isSelected());
    }//GEN-LAST:event_jCheckBoxMieStateChanged

    private void jCheckBoxJueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxJueStateChanged
        checkAction(jSpinnerJueIni, jSpinnerJueFin, jCheckBoxJue.isSelected());
    }//GEN-LAST:event_jCheckBoxJueStateChanged

    private void jCheckBoxVieStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxVieStateChanged
        checkAction(jSpinnerVieIni, jSpinnerVieFin, jCheckBoxVie.isSelected());
    }//GEN-LAST:event_jCheckBoxVieStateChanged

    private void jCheckBoxSabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxSabStateChanged
        checkAction(jSpinnerSabIni, jSpinnerSabFin, jCheckBoxSab.isSelected());
    }//GEN-LAST:event_jCheckBoxSabStateChanged

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jTextFieldNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyTyped
        String aux = jTextFieldNombre.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Turno.SIZE_NOMBRE, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyTyped

    private void jTextFieldDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescripcionKeyTyped
        String aux = jTextFieldDescripcion.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Turno.SIZE_DESCRIPCION, aux)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldDescripcionKeyTyped

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        String aux = jTextFieldNombre.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Turno.SIZE_NOMBRE, aux)) {
            JOptionPane.showMessageDialog(this, "El campo nombre no es valido", "Warnnig", JOptionPane.WARNING_MESSAGE);
            return;
        }
        aux = jTextFieldDescripcion.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Turno.SIZE_DESCRIPCION, aux)) {
            JOptionPane.showMessageDialog(this, "El campo descripcion no es valido", "Warnnig", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new Proceso(this) {
            @Override
            public void proceso() {
                try {
                    getjProgressBar().setValue(20);
                    Turno turno = new Turno();
                    turno.setNombre(jTextFieldNombre.getText());
                    turno.setDescripcion(jTextFieldDescripcion.getText());
                    turno.setTurnoDias(new ArrayList<>());
                    TurnoDia dia;
                    if (jCheckBoxDom.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("DOMINGO");
                        dia.setInicio(LocalTime.parse(jSpinnerDomIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerDomFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxLun.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("LUNES");
                        dia.setInicio(LocalTime.parse(jSpinnerLunIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerLunFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxMar.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("MARTES");
                        dia.setInicio(LocalTime.parse(jSpinnerMarIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerMarFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxMie.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("MIERCOLES");
                        dia.setInicio(LocalTime.parse(jSpinnerMieIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerMieFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxJue.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("JUEVES");
                        dia.setInicio(LocalTime.parse(jSpinnerJueIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerJueFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxVie.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("VIERNES");
                        dia.setInicio(LocalTime.parse(jSpinnerVieIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerVieFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    if (jCheckBoxSab.isSelected()) {
                        dia = new TurnoDia();
                        dia.setDia(new Catalogo());
                        dia.getDia().setNombre("SABADO");
                        dia.setInicio(LocalTime.parse(jSpinnerSabIni.getValue().toString()));
                        dia.setFin(LocalTime.parse(jSpinnerSabFin.getValue().toString()));
                        turno.getTurnoDias().add(dia);
                    }
                    TurnoController turnoController = new TurnoController();
                    turnoController.save(turno);
                    THIS.dispose();
                } catch (Exception ex) {
                    Logger.getLogger(JDialogTurnoNuevo.class.getName()).log(Level.SEVERE, null, ex);
                    setVisible(false);
                    ErrorLog err= new ErrorLog(THIS, ex);
                }
            }
        };
    }//GEN-LAST:event_jButtonNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JCheckBox jCheckBoxDom;
    private javax.swing.JCheckBox jCheckBoxJue;
    private javax.swing.JCheckBox jCheckBoxLun;
    private javax.swing.JCheckBox jCheckBoxMar;
    private javax.swing.JCheckBox jCheckBoxMie;
    private javax.swing.JCheckBox jCheckBoxSab;
    private javax.swing.JCheckBox jCheckBoxVie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinnerDomFin;
    private javax.swing.JSpinner jSpinnerDomIni;
    private javax.swing.JSpinner jSpinnerJueFin;
    private javax.swing.JSpinner jSpinnerJueIni;
    private javax.swing.JSpinner jSpinnerLunFin;
    private javax.swing.JSpinner jSpinnerLunIni;
    private javax.swing.JSpinner jSpinnerMarFin;
    private javax.swing.JSpinner jSpinnerMarIni;
    private javax.swing.JSpinner jSpinnerMieFin;
    private javax.swing.JSpinner jSpinnerMieIni;
    private javax.swing.JSpinner jSpinnerSabFin;
    private javax.swing.JSpinner jSpinnerSabIni;
    private javax.swing.JSpinner jSpinnerVieFin;
    private javax.swing.JSpinner jSpinnerVieIni;
    private javax.swing.JTextField jTextFieldDescripcion;
    private javax.swing.JTextField jTextFieldNombre;
    // End of variables declaration//GEN-END:variables
}
