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
package mx.tecabix.view.departamento;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mx.tecabix.db.entity.Departamento;
import mx.tecabix.service.Auth;
import mx.tecabix.service.controller.DepartamentoController;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.Proceso;
import mx.tecabix.view.perfil.JDialogPerfilNuevo;
/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class JDialogDepartamentoEditar extends javax.swing.JDialog {

    private Auth auth = new Auth();
    private Departamento departamento;

    public JDialogDepartamentoEditar(java.awt.Frame parent, Departamento departamento) {
        super(parent, true);
        initComponents();
        this.departamento = departamento;
        jTextFieldNombre.setText(departamento.getNombre());
        jTextPaneDescripcion.setText(departamento.getDescripcion());
        setLocationRelativeTo(parent);
        setVisible(true);
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
        jButtonGuardar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneDescripcion = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Departamento");
        setMinimumSize(new java.awt.Dimension(472, 284));

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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre:");

        jTextFieldNombre.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyTyped(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Descripcion:");

        jTextPaneDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextPaneDescripcionKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPaneDescripcion);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(171, Short.MAX_VALUE)
                .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldNombre))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
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

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private javax.swing.JDialog THIS = this;
    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        Proceso p = new Proceso(this) {
            @Override
            public void proceso() {
                String nombre = jTextFieldNombre.getText();
                String descripcion = jTextPaneDescripcion.getText();
                if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Departamento.SIZE_NOMBRE, nombre)) {
                    setVisible(false);
                    JOptionPane.showMessageDialog(THIS, "El nombre no es valido", "Warnig", JOptionPane.WARNING_MESSAGE);
                }
                if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Departamento.SIZE_DESCRIPCION, descripcion)) {
                    setVisible(false);
                    JOptionPane.showMessageDialog(THIS, "La descripcion no es valida", "Warnig", JOptionPane.WARNING_MESSAGE);
                }else{
                    try {
                        DepartamentoController departamentoController = new DepartamentoController();
                        departamento.setNombre(nombre);
                        departamento.setDescripcion(descripcion);
                        departamentoController.update(departamento);
                        THIS.dispose();
                    } catch (Exception e) {
                        setVisible(false);
                        ErrorLog err = new ErrorLog(THIS, e);
                        Logger.getLogger(JDialogPerfilNuevo.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        };

    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jTextFieldNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyTyped
        String text = jTextFieldNombre.getText()+evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Departamento.SIZE_NOMBRE, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyTyped

    private void jTextPaneDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPaneDescripcionKeyTyped
        String text = jTextPaneDescripcion.getText()+evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Departamento.SIZE_DESCRIPCION, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextPaneDescripcionKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextPane jTextPaneDescripcion;
    // End of variables declaration//GEN-END:variables
}
