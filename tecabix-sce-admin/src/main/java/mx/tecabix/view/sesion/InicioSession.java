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
package mx.tecabix.view.sesion;

import java.util.UUID;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mx.tecabix.ConfiguracionConexion;
import mx.tecabix.ConfiguracionPropertie;
import mx.tecabix.service.controller.SesionConreoller;
import mx.tecabix.db.entity.Sesion;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.Proceso;

/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class InicioSession extends javax.swing.JDialog {

    private JFrame parent;
    private boolean autentificado;
    private boolean exit;
    private Sesion sessionResponse;

    public InicioSession(JFrame parent) {
        super(parent, true);
        initComponents();
        this.parent = parent;
    }

    private void evento() {
        JDialog THIS = this;
        Proceso proceso = new Proceso(this) {
            @Override
            public void proceso() {
                
                this.getjProgressBar().setValue(30);
                try {
                    ConfiguracionPropertie configuracion = ConfiguracionPropertie.getConfiguracionPropertie();
                    ConfiguracionConexion conexion = ConfiguracionConexion.getConfiguracionConexion();
                    SesionConreoller sessionController = new SesionConreoller();
                    String usuario = jTextFieldUsuario.getText();
                    String pass = String.valueOf(jPasswordFieldPsw.getPassword());
                    String hashEquipo = configuracion.getHashEquipo();
                    String http = configuracion.getHttp();
                    String ip = configuracion.getIP();
                    String puerto = configuracion.getPuerto();
                    conexion.setHttp(http);
                    conexion.setIp(ip);
                    conexion.setPuerto(puerto);
                    conexion.setUsuario(usuario);
                    conexion.setPassword(pass);
                    this.getjProgressBar().setValue(80);
                    sessionResponse = sessionController.post(hashEquipo);
                    UUID token = sessionResponse.getClave();
                    conexion.setToken(token);
                    conexion.setSesion(sessionResponse);

                    autentificado = true;
                    exit = false;
                    THIS.dispose();
                } catch (Exception ex) {
                    this.setVisible(false);
                    String msj_401 = "<html><h3>Acceso denegado</h3>"
                            + "<h4>Posibles causas:</h4>"
                            + "<ul>"
                            + "<li>El Usuario y/o Contraseña es incorrecta</li>"
                            + "<li>El Usuario se encuentra bloqueado</li>"
                            + "<li>La licencia no es valida o está inactiva</li>"
                            + "</ul></html>";
                    String msj_404 = "<html><h3>Acceso denegado</h3>"
                            + "<h4>Posibles causas:</h4>"
                            + "<ul>"
                            + "<li>La licencia no es valida</li>"
                            + "</ul></html>";
                    String msj_406 = "<html><h3>Acceso denegado</h3>"
                            + "<h4>Posibles causas:</h4>"
                            + "<ul>"
                            + "<li>El usuario esta logeado en otro equipo</li>"
                            + "<li>Otro equipo esta usando la misma licencia</li>"
                            + "</ul></html>";
                    String msj_409 = "<html><h3>Acceso denegado</h3>"
                            + "<h4>Posibles causas:</h4>"
                            + "<ul>"
                            + "<li>Los datos del usuario esta corrompidos o bloqueados</li>"
                            + "</ul></html>";
                    String msj_423 = "<html><h3>Sistema empresarial suspendido por falta de pago</h3>"
                            + "</html>";
                    String msj_refused = "<html><h3>Conexión denegada</h3>"
                            + "<h4>Posibles causas:</h4>"
                            + "<ul>"
                            + "<li>El servidor está fuera de alcance </li>"
                            + "<li>El servidor está fuera de servicio </li>"
                            + "</ul></html>";
                    if (ex.getMessage().equalsIgnoreCase("401 Unauthorized")) {
                        JOptionPane.showMessageDialog(THIS, msj_401, "Acceso no Authorizado", JOptionPane.WARNING_MESSAGE);
                    } else if (ex.getMessage().equalsIgnoreCase("404 Not Found")) {
                        JOptionPane.showMessageDialog(THIS, msj_404, "Acceso no Authorizado", JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getMessage().equalsIgnoreCase("406 Not Acceptable")) {
                        JOptionPane.showMessageDialog(THIS, msj_406, "Acceso no Authorizado", JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getMessage().equalsIgnoreCase("409 Conflict")) {
                        JOptionPane.showMessageDialog(THIS, msj_409, "Acceso no Authorizado", JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getMessage().equalsIgnoreCase("423 Locked")) {
                        JOptionPane.showMessageDialog(THIS, msj_423, "Sistema suspendido", JOptionPane.ERROR_MESSAGE);
                    } else if (ex.getMessage().contains("Connection refused")) {
                        JOptionPane.showMessageDialog(THIS, msj_refused, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ErrorLog errorLog = new ErrorLog(THIS, ex);
                    }
                }
                this.getjProgressBar().setValue(100);
            }
        };
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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPasswordFieldPsw = new javax.swing.JPasswordField();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tecabix");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Inicia sesión");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Usuario");

        jTextFieldUsuario.setText("root");
        jTextFieldUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUsuarioActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Contraseña");

        jPasswordFieldPsw.setText("admin");
        jPasswordFieldPsw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldPswActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldPsw, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldUsuario)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPasswordFieldPsw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jButton1.setText("Inicia sesión");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioActionPerformed
        evento();
    }//GEN-LAST:event_jTextFieldUsuarioActionPerformed

    private void jPasswordFieldPswActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldPswActionPerformed
        evento();
    }//GEN-LAST:event_jPasswordFieldPswActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        evento();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
        exit = true;
    }//GEN-LAST:event_jButton2ActionPerformed

    public boolean isAutentificado() {
        return autentificado;
    }

    public boolean isExit() {
        return exit;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordFieldPsw;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
