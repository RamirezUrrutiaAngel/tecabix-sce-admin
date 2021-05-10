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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.Contacto;
import mx.tecabix.db.entity.Departamento;
import mx.tecabix.db.entity.Direccion;
import mx.tecabix.db.entity.Estado;
import mx.tecabix.db.entity.Municipio;
import mx.tecabix.db.entity.Perfil;
import mx.tecabix.db.entity.Persona;
import mx.tecabix.db.entity.PersonaFisica;
import mx.tecabix.db.entity.Plantel;
import mx.tecabix.db.entity.Puesto;
import mx.tecabix.db.entity.Trabajador;
import mx.tecabix.db.entity.Usuario;
import mx.tecabix.db.entity.UsuarioPersona;
import mx.tecabix.service.Auth;
import mx.tecabix.service.controller.DepartamentoController;
import mx.tecabix.service.controller.EstadoController;
import mx.tecabix.service.controller.MunicipioController;
import mx.tecabix.service.controller.PerfilController;
import mx.tecabix.service.controller.PlantelController;
import mx.tecabix.service.controller.PuestoController;
import mx.tecabix.service.controller.TrabajadorController;
import mx.tecabix.service.controller.UsuarioController;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.ModelT;
import mx.tecabix.view.Proceso;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class JDialogTrabajadorNuevo extends javax.swing.JDialog {

    private Trabajador trabajador;
    private List<Estado> estados;
    private List<Municipio> municipios;
    private List<Contacto> contactos;
    private List<Perfil> perfiles;
    private List<Plantel> planteles;
    private List<Departamento> departamentos;
    private List<Puesto> puestos;

    private JDialog THIS = this;
    private boolean isAceptUsuario;

    private ModelT defaultTableModel;

    private final short TIPO = 0;
    private final short VALOR = 1;
    private final short NUM_COLUMNAS = 2;

    private Auth auth;

    public JDialogTrabajadorNuevo(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        auth = new Auth();
        trabajador = new Trabajador();
        trabajador.setPersonaFisica(new PersonaFisica());
        trabajador.getPersonaFisica().setDireccion(new Direccion());
        trabajador.getPersonaFisica().getDireccion().setMunicipio(new Municipio());
        trabajador.getPersonaFisica().setSexo(new Catalogo());
        trabajador.getPersonaFisica().setPersona(new Persona());
        trabajador.getPersonaFisica().getPersona().setContactos(new ArrayList<>());
        trabajador.getPersonaFisica().getPersona().setUsuarioPersona(new UsuarioPersona());
        trabajador.getPersonaFisica().getPersona().getUsuarioPersona().setUsuario(new Usuario());
        trabajador.getPersonaFisica().getPersona().getUsuarioPersona().getUsuario().setPerfil(new Perfil());
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelPersona, "Persona");
        contactos = trabajador.getPersonaFisica().getPersona().getContactos();
        setVisible(true);
    }

    private void counstrurTabla() {
        cargarTabla();
        formatearTabla();
    }

    private void cargarTabla() {
        try {
            String[] header = new String[NUM_COLUMNAS];
            header[TIPO] = "TIPO";
            header[VALOR] = "VALOR";
            Class[] headerType = new Class[NUM_COLUMNAS];

            headerType[TIPO] = java.lang.String.class;
            headerType[VALOR] = java.lang.String.class;
            defaultTableModel = new ModelT(header, headerType);
            for (int i = 0; i < contactos.size(); i++) {
                Contacto contacto = contactos.get(i);

                defaultTableModel.addRow(new java.util.Vector());
                defaultTableModel.setValueAt(contacto.getTipo().getNombre(), i, 0);
                defaultTableModel.setValueAt(contacto.getValor(), i, 1);
            }
            jTable.setModel(defaultTableModel);
            if (!this.contactos.isEmpty()) {
                jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jTable.setRowSelectionInterval(0, 0);
            }

        } catch (Exception e) {
            ErrorLog error = new ErrorLog(THIS, e);
        }
    }

    private void formatearTabla() {
        try {
            jTable.setFont(new Font("Arial", Font.PLAIN, 16));
            jTable.setRowHeight(25);
            jTable.getColumnModel().getColumn(TIPO).setMinWidth(150);
            jTable.getColumnModel().getColumn(TIPO).setMaxWidth(200);
            jTable.getColumnModel().getColumn(VALOR).setMinWidth(200);
            jTable.setShowHorizontalLines(false);
            jTable.setShowVerticalLines(false);
            jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row % 2 == 0) {
                        component.setBackground(Color.decode("#f9f9f9"));

                    } else {
                        component.setBackground(Color.WHITE);
                    }
                    if (isSelected) {
                        component.setBackground(Color.decode("#0048ff"));
                    }
                    return component;
                }
            });
            jTable.setDefaultRenderer(Float.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    this.setHorizontalAlignment(RIGHT);
                    if (row % 2 == 0) {
                        component.setBackground(Color.decode("#f9f9f9"));
                    } else {
                        component.setBackground(Color.WHITE);
                    }
                    if (column == 2) {
                        component.setBackground(Color.decode("#e1ffd8"));
                    }
                    if (isSelected) {
                        component.setBackground(Color.decode("#0048ff"));
                    }
                    return component;
                }
            });
            jTable.getTableHeader().setReorderingAllowed(false);
            jTable.setComponentPopupMenu(jPopupMenu);
        } catch (Exception e) {
            ErrorLog error = new ErrorLog(THIS, e);
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

        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemEliminar = new javax.swing.JMenuItem();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelPersona = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPaterno = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldMaterno = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCURP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldRFC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxSexo = new javax.swing.JComboBox<>();
        jButtonSiguiente00 = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jFormattedTextNacimiento = new javax.swing.JFormattedTextField();
        jPanelDireccion = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldCalle = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldNumInt = new javax.swing.JTextField();
        jTextFieldNumExt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldCodigoPostal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldAsentamiento = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldReferencia = new javax.swing.JTextField();
        jButtonSiguiente01 = new javax.swing.JButton();
        jButtonAtras01 = new javax.swing.JButton();
        jComboBoxEstados = new javax.swing.JComboBox<>();
        jComboBoxMunicipio = new javax.swing.JComboBox<>();
        jPanelContacto = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jComboBoxTipoContacto = new javax.swing.JComboBox<>();
        jButtonAniadirContacto = new javax.swing.JButton();
        jTextFieldContacto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jButtonSiguiente02 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanelUsuario = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jCheckBoxAgregarUsuario = new javax.swing.JCheckBox();
        jLabeCorreo = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabelPerfil = new javax.swing.JLabel();
        jComboBoxPerfil = new javax.swing.JComboBox<>();
        jButtonSiguiente03 = new javax.swing.JButton();
        jButtonAtras03 = new javax.swing.JButton();
        jPanelTrabajador = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonAtras04 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxPlantel = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jComboBoxDepartamento = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxPuesto = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jButtonJefe = new javax.swing.JButton();
        jTextFieldJefe = new javax.swing.JTextField();

        jMenuItemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/basura.png"))); // NOI18N
        jMenuItemEliminar.setText("Eliminar");
        jMenuItemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEliminarActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemEliminar);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelPersona.setBorder(javax.swing.BorderFactory.createTitledBorder("Persona"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre:");

        jTextFieldNombre.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyTyped(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Apellido paterno:");

        jTextFieldPaterno.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldPaterno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPaternoKeyTyped(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Apellido materno:");

        jTextFieldMaterno.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldMaterno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldMaternoKeyTyped(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("CURP:");

        jTextFieldCURP.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCURP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCURPKeyTyped(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("RFC:");

        jTextFieldRFC.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldRFC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldRFCKeyTyped(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nacimiento:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Sexo:");

        jComboBoxSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MASCULINO", "FEMENINO" }));
        jComboBoxSexo.setSelectedIndex(-1);

        jButtonSiguiente00.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente00.setText("Siguiente");
        jButtonSiguiente00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente00ActionPerformed(evt);
            }
        });

        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/cancelar.png"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jFormattedTextNacimiento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        jFormattedTextNacimiento.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jFormattedTextNacimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextNacimientoKeyTyped(evt);
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNombre))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPaterno, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCURP, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldRFC, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextNacimiento))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldMaterno, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                            .addComponent(jComboBoxSexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente00, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jFormattedTextNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldCURP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente00)
                    .addComponent(jButtonCancelar)))
        );

        javax.swing.GroupLayout jPanelPersonaLayout = new javax.swing.GroupLayout(jPanelPersona);
        jPanelPersona.setLayout(jPanelPersonaLayout);
        jPanelPersonaLayout.setHorizontalGroup(
            jPanelPersonaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPersonaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelPersonaLayout.setVerticalGroup(
            jPanelPersonaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPersonaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("Persona", jPanelPersona);

        jPanelDireccion.setBorder(javax.swing.BorderFactory.createTitledBorder("Direccion"));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Estado:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Municipio:");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Calle:");

        jTextFieldCalle.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCalleKeyTyped(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Num. Interior:");

        jTextFieldNumInt.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNumInt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumIntKeyTyped(evt);
            }
        });

        jTextFieldNumExt.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNumExt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumExtKeyTyped(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Num. Exterior:");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Codigo Postal:");

        jTextFieldCodigoPostal.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCodigoPostal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoPostalKeyTyped(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Asentamiento:");

        jTextFieldAsentamiento.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldAsentamiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldAsentamientoKeyTyped(evt);
            }
        });

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Referencia:");

        jTextFieldReferencia.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldReferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldReferenciaKeyTyped(evt);
            }
        });

        jButtonSiguiente01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente01.setText("Siguiente");
        jButtonSiguiente01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente01ActionPerformed(evt);
            }
        });

        jButtonAtras01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras01.setText("Atras");
        jButtonAtras01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras01ActionPerformed(evt);
            }
        });

        jComboBoxEstados.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxEstadosItemStateChanged(evt);
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
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxEstados, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldReferencia))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 207, Short.MAX_VALUE)
                        .addComponent(jButtonAtras01, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente01, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxMunicipio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCalle))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNumInt))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNumExt))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCodigoPostal))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAsentamiento)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBoxEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBoxMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldNumInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldNumExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldAsentamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente01)
                    .addComponent(jButtonAtras01)))
        );

        javax.swing.GroupLayout jPanelDireccionLayout = new javax.swing.GroupLayout(jPanelDireccion);
        jPanelDireccion.setLayout(jPanelDireccionLayout);
        jPanelDireccionLayout.setHorizontalGroup(
            jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelDireccionLayout.setVerticalGroup(
            jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab2", jPanelDireccion);

        jPanelContacto.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacto"));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBoxTipoContacto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "E-MAIL", "TEL-CASA", "TEL-MOVIL", "TEL-OFICINA", "SITIO-WEB" }));
        jComboBoxTipoContacto.setSelectedIndex(-1);

        jButtonAniadirContacto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/plus.png"))); // NOI18N
        jButtonAniadirContacto.setText("Añadir");
        jButtonAniadirContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAniadirContactoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxTipoContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldContacto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAniadirContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAniadirContacto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxTipoContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButtonSiguiente02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente02.setText("Siguiente");
        jButtonSiguiente02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente02ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButton7.setText("Atras");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 207, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente02, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente02)
                    .addComponent(jButton7)))
        );

        javax.swing.GroupLayout jPanelContactoLayout = new javax.swing.GroupLayout(jPanelContacto);
        jPanelContacto.setLayout(jPanelContactoLayout);
        jPanelContactoLayout.setHorizontalGroup(
            jPanelContactoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContactoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelContactoLayout.setVerticalGroup(
            jPanelContactoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContactoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab3", jPanelContacto);

        jPanelUsuario.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuario"));

        jCheckBoxAgregarUsuario.setText("Agregar cuenta de usuario al trabajador");
        jCheckBoxAgregarUsuario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxAgregarUsuarioItemStateChanged(evt);
            }
        });

        jLabeCorreo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabeCorreo.setText("Correo:");
        jLabeCorreo.setEnabled(false);

        jTextFieldCorreo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCorreo.setEnabled(false);
        jTextFieldCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCorreoKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCorreoKeyReleased(evt);
            }
        });

        jLabelUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelUsuario.setText("Usuario:");
        jLabelUsuario.setEnabled(false);

        jTextFieldUsuario.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldUsuario.setEnabled(false);
        jTextFieldUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldUsuarioKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldUsuarioKeyReleased(evt);
            }
        });

        jLabelPerfil.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelPerfil.setText("Perfil:");
        jLabelPerfil.setEnabled(false);

        jComboBoxPerfil.setEnabled(false);

        jButtonSiguiente03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente03.setText("Siguiente");
        jButtonSiguiente03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente03ActionPerformed(evt);
            }
        });

        jButtonAtras03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras03.setText("Atras");
        jButtonAtras03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras03ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxAgregarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabeCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCorreo))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabelUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldUsuario))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabelPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxPerfil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAtras03, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente03, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxAgregarUsuario)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabeCorreo)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuario)
                    .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPerfil)
                    .addComponent(jComboBoxPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente03)
                    .addComponent(jButtonAtras03)))
        );

        javax.swing.GroupLayout jPanelUsuarioLayout = new javax.swing.GroupLayout(jPanelUsuario);
        jPanelUsuario.setLayout(jPanelUsuarioLayout);
        jPanelUsuarioLayout.setHorizontalGroup(
            jPanelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelUsuarioLayout.setVerticalGroup(
            jPanelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab4", jPanelUsuario);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Trabajador"));

        jButtonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/disquete.png"))); // NOI18N
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jButtonAtras04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras04.setText("Atras");
        jButtonAtras04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras04ActionPerformed(evt);
            }
        });

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Plantel:");

        jComboBoxPlantel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Departamento:");

        jComboBoxDepartamento.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jComboBoxDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxDepartamentoItemStateChanged(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Puesto:");

        jComboBoxPuesto.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Jefe directo:");

        jButtonJefe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/lupa.png"))); // NOI18N
        jButtonJefe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJefeActionPerformed(evt);
            }
        });

        jTextFieldJefe.setEditable(false);
        jTextFieldJefe.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 207, Short.MAX_VALUE)
                        .addComponent(jButtonAtras04, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxPlantel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxDepartamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jButtonJefe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldJefe))
                            .addComponent(jComboBoxPuesto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jComboBoxPlantel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jComboBoxDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jComboBoxPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonJefe))
                    .addComponent(jTextFieldJefe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonAtras04)))
        );

        javax.swing.GroupLayout jPanelTrabajadorLayout = new javax.swing.GroupLayout(jPanelTrabajador);
        jPanelTrabajador.setLayout(jPanelTrabajadorLayout);
        jPanelTrabajadorLayout.setHorizontalGroup(
            jPanelTrabajadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrabajadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTrabajadorLayout.setVerticalGroup(
            jPanelTrabajadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrabajadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab2", jPanelTrabajador);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyTyped
        String text = jTextFieldNombre.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, PersonaFisica.SIZE_NOMBRE, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyTyped

    private void jTextFieldPaternoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPaternoKeyTyped
        String text = jTextFieldPaterno.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA, PersonaFisica.SIZE_APELLIDO_PATERNO, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPaternoKeyTyped

    private void jTextFieldMaternoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMaternoKeyTyped
        String text = jTextFieldMaterno.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA, PersonaFisica.SIZE_APELLIDO_MATERNO, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldMaternoKeyTyped

    private void jTextFieldCURPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCURPKeyTyped
        String text = jTextFieldCURP.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, Trabajador.SIZE_CURP, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCURPKeyTyped

    private void jTextFieldRFCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRFCKeyTyped
        String text = jTextFieldRFC.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, Trabajador.SIZE_CURP, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldRFCKeyTyped

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonSiguiente00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente00ActionPerformed
        PersonaFisica personaFisica = trabajador.getPersonaFisica();
        String text = jTextFieldNombre.getText();

        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, PersonaFisica.SIZE_NOMBRE, text)) {
            JOptionPane.showMessageDialog(this, "El formato del nombre es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        personaFisica.setNombre(text);
        text = jTextFieldPaterno.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA, PersonaFisica.SIZE_APELLIDO_PATERNO, text)) {
            JOptionPane.showMessageDialog(this, "El formato del apellido paterno es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        personaFisica.setApellidoPaterno(text);
        text = jTextFieldMaterno.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA, PersonaFisica.SIZE_APELLIDO_MATERNO, text)) {
            JOptionPane.showMessageDialog(this, "El formato del apelldo materno es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        personaFisica.setApellidoMaterno(text);
        text = jTextFieldCURP.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, Trabajador.SIZE_CURP, text)) {
            JOptionPane.showMessageDialog(this, "El formato del CURP es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
//        trabajador.setCURP(text);
        if (jComboBoxSexo.getSelectedIndex() != -1) {
            personaFisica.getSexo().setNombre(jComboBoxSexo.getSelectedItem().toString());
        }
        try {
            if (!jFormattedTextNacimiento.getText().isEmpty()) {
                personaFisica.setFechaNacimiento(
                        LocalDate.parse(jFormattedTextNacimiento.getText(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        ));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato del nacimiento es invalido", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (estados == null) {
            Proceso p;
            p = new Proceso(this) {
                @Override
                public void proceso() {
                    try {
                        EstadoController estadoController = new EstadoController();
                        byte max = Byte.MAX_VALUE;
                        short zero = 0;
                        getjProgressBar().setValue(30);
                        estados = estadoController.find("", zero, max, zero).getData();
                        getjProgressBar().setValue(80);
                        estados.sort(new Comparator<Estado>() {
                            @Override
                            public int compare(Estado o1, Estado o2) {
                                return o1.getNombre().compareTo(o2.getNombre());
                            }
                        });
                        for (int i = 0; i < estados.size(); i++) {
                            jComboBoxEstados.addItem(estados.get(i).getNombre());
                        }
                        jComboBoxEstados.setSelectedIndex(-1);
                    } catch (Exception ex) {
                        this.setVisible(false);
                        Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                        ErrorLog e = new ErrorLog(THIS, ex);
                    }
                }
            };
        }
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelDireccion, "Direccion");
    }//GEN-LAST:event_jButtonSiguiente00ActionPerformed

    private void jButtonAtras01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras01ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelPersona, "Persona");
    }//GEN-LAST:event_jButtonAtras01ActionPerformed

    private void jComboBoxEstadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxEstadosItemStateChanged
        Proceso p = new Proceso(this) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(70);
                MunicipioController mc = new MunicipioController();
                int select = jComboBoxEstados.getSelectedIndex();
                if (select > -1) {
                    try {
                        Estado estado = estados.get(select);
                        municipios = mc.findByEstadoClave(estado.getClave()).getData();
                        municipios.sort(new Comparator<Municipio>() {
                            @Override
                            public int compare(Municipio o1, Municipio o2) {
                                return o1.getNombre().compareTo(o2.getNombre());
                            }
                        });
                        jComboBoxMunicipio.removeAllItems();

                        for (int i = 0; i < municipios.size(); i++) {
                            jComboBoxMunicipio.addItem(municipios.get(i).getNombre());
                        }
                        jComboBoxMunicipio.setSelectedIndex(-1);
                    } catch (Exception ex) {
                        setVisible(false);
                        Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                        ErrorLog err = new ErrorLog(THIS, ex);
                    }
                }

            }
        };
    }//GEN-LAST:event_jComboBoxEstadosItemStateChanged

    private void jTextFieldCalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCalleKeyTyped
        String text = jTextFieldCalle.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_CALLE, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCalleKeyTyped

    private void jTextFieldNumIntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumIntKeyTyped
        String text = jTextFieldNumInt.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_NUM_INT, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumIntKeyTyped

    private void jTextFieldNumExtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumExtKeyTyped
        String text = jTextFieldNumExt.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_NUM_EXT, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumExtKeyTyped

    private void jTextFieldCodigoPostalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoPostalKeyTyped
        String text = jTextFieldCodigoPostal.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_NUMERIC, Direccion.SIZE_CODIGO_POSTAL, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCodigoPostalKeyTyped

    private void jTextFieldAsentamientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAsentamientoKeyTyped
        String text = jTextFieldAsentamiento.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_ASENTAMIENTO, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldAsentamientoKeyTyped

    private void jTextFieldReferenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReferenciaKeyTyped
        String text = jTextFieldReferencia.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_REFERENCIA, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldReferenciaKeyTyped

    private void jButtonSiguiente01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente01ActionPerformed
        int select = jComboBoxEstados.getSelectedIndex();
        if(select < 0 ){
            JOptionPane.showMessageDialog(this, "No se a seleccionado un estado", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        select = jComboBoxMunicipio.getSelectedIndex();
        if(select < 0 ){
            JOptionPane.showMessageDialog(this, "No se a seleccionado un Municipio", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Municipio municipio = municipios.get(select);
        trabajador.getPersonaFisica().getDireccion().setMunicipio(municipio);
        String text = jTextFieldCalle.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Direccion.SIZE_CALLE, text)) {
            JOptionPane.showMessageDialog(this, "El formato de la calle es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getPersonaFisica().getDireccion().setCalle(text);
        text = jTextFieldNumInt.getText();
        if (text != null && !text.isEmpty()) {
            if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_NUM_INT, text)) {
                JOptionPane.showMessageDialog(this, "El formato del numero interior es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getPersonaFisica().getDireccion().setNumInt(text);
        }
        
        text = jTextFieldNumExt.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_NUM_EXT, text)) {
            JOptionPane.showMessageDialog(this, "El formato del numero exterior es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getPersonaFisica().getDireccion().setNumExt(text);
        text = jTextFieldCodigoPostal.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_CODIGO_POSTAL, text)) {
            JOptionPane.showMessageDialog(this, "El formato del codigo postal es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getPersonaFisica().getDireccion().setCodigoPostal(text);
        text = jTextFieldAsentamiento.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_ASENTAMIENTO, text)) {
            JOptionPane.showMessageDialog(this, "El formato del asentamiento es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getPersonaFisica().getDireccion().setAsentamiento(text);
        text = jTextFieldReferencia.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, Direccion.SIZE_REFERENCIA, text)) {
            JOptionPane.showMessageDialog(this, "El formato de la referencia es incorrecta", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getPersonaFisica().getDireccion().setReferencia(text);
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelContacto, "Contacto");
    }//GEN-LAST:event_jButtonSiguiente01ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelDireccion, "Direccion");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButtonAniadirContactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAniadirContactoActionPerformed
        int op = jComboBoxTipoContacto.getSelectedIndex();
        String value = jTextFieldContacto.getText();
        if (op != -1 && !value.isEmpty()) {
            Contacto contacto = new Contacto();
            contacto.setTipo(new Catalogo());
            contacto.getTipo().setNombre(jComboBoxTipoContacto.getSelectedItem().toString());
            contacto.setValor(value);
            contactos.add(contacto);
            jTextFieldContacto.setText("");
            counstrurTabla();
        }
    }//GEN-LAST:event_jButtonAniadirContactoActionPerformed

    private void jMenuItemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEliminarActionPerformed
        int selected = jTable.getSelectedRow();
        if (selected >= 0 && selected < contactos.size()) {
            contactos.remove(selected);
            counstrurTabla();
        }

    }//GEN-LAST:event_jMenuItemEliminarActionPerformed

    private void jButtonSiguiente02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente02ActionPerformed

        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelUsuario, "Usuario");
    }//GEN-LAST:event_jButtonSiguiente02ActionPerformed

    private void jButtonAtras03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras03ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelContacto, "Contacto");
    }//GEN-LAST:event_jButtonAtras03ActionPerformed

    private void jCheckBoxAgregarUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxAgregarUsuarioItemStateChanged
        boolean selected = jCheckBoxAgregarUsuario.isSelected();
        jLabeCorreo.setEnabled(selected);
        jLabelPerfil.setEnabled(selected);
        jLabelUsuario.setEnabled(selected);
        jTextFieldCorreo.setEnabled(selected);
        jTextFieldUsuario.setEnabled(selected);
        jComboBoxPerfil.setEnabled(selected);
        if (perfiles == null) {
            Proceso p = new Proceso(THIS) {
                @Override
                public void proceso() {
                    try {
                        PerfilController pc = new PerfilController();
                        byte max = Byte.MAX_VALUE;
                        short zero = 0;
                        getjProgressBar().setValue(30);
                        perfiles = pc.find("", 0, max, zero).getData();
                        getjProgressBar().setValue(80);
                        perfiles.sort(new Comparator<Perfil>() {
                            @Override
                            public int compare(Perfil o1, Perfil o2) {
                                return o1.getNombre().compareTo(o2.getNombre());
                            }
                        });

                        for (int i = 0; i < perfiles.size(); i++) {
                            jComboBoxPerfil.addItem(perfiles.get(i).getNombre());
                        }
                        jComboBoxPerfil.setSelectedIndex(-1);
                    } catch (Exception ex) {
                        setVisible(false);
                        Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                        ErrorLog err = new ErrorLog(THIS, ex);
                    }
                }
            };
        }
    }//GEN-LAST:event_jCheckBoxAgregarUsuarioItemStateChanged

    private void jTextFieldCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCorreoKeyTyped
        String text = jTextFieldCorreo.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Usuario.SIZE_CORREO, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCorreoKeyTyped

    private void jTextFieldUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioKeyTyped
        String text = jTextFieldUsuario.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Usuario.SIZE_NOMBRE, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldUsuarioKeyTyped

    private void jButtonSiguiente03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente03ActionPerformed
        if (jCheckBoxAgregarUsuario.isSelected()) {
            this.trabajador.getPersonaFisica().getPersona().getUsuarioPersona().setUsuario(new Usuario());
            String text = jTextFieldCorreo.getText();
            if (auth.isNotValid(Auth.TIPO_EMAIL, Usuario.SIZE_CORREO, text)) {
                JOptionPane.showMessageDialog(this, "El formato del correo es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.trabajador.getPersonaFisica().getPersona().getUsuarioPersona().getUsuario().setCorreo(text);
            text = jTextFieldUsuario.getText();
            if (auth.isNotValid(Auth.TIPO_EMAIL, Usuario.SIZE_NOMBRE, text) && auth.isNotValid(Auth.TIPO_VARIABLE, Usuario.SIZE_NOMBRE, text)) {
                JOptionPane.showMessageDialog(this, "El formato del nombre de usuario es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.trabajador.getPersonaFisica().getPersona().getUsuarioPersona().getUsuario().setNombre(text);
            if (jComboBoxPerfil.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "No se a seleccionado un perfil", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int select = jComboBoxPerfil.getSelectedIndex();
            this.trabajador.getPersonaFisica().getPersona().getUsuarioPersona().getUsuario().setPerfil(perfiles.get(select));

            Proceso p = new Proceso(THIS) {
                @Override
                public void proceso() {
                    byte max = Byte.MAX_VALUE;
                    short zero = 0;
                    if (!isAceptUsuario) {
                        getjProgressBar().setValue(30);
                        String usuario = jTextFieldUsuario.getText();
                        UsuarioController uc = new UsuarioController();

                        boolean notExist = false;
                        try {
                            uc.exists(usuario);
                            notExist = true;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "El nombre no esta disponible.", "Warning", JOptionPane.WARNING_MESSAGE);
                            Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        getjProgressBar().setValue(45);
                        if (notExist) {
                            try {
                                getjProgressBar().setValue(60);
                                if (planteles == null) {
                                    PlantelController pc = new PlantelController();
                                    planteles = pc.find(new String(), 0, max, zero).getData();
                                    planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                    planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                                    jComboBoxPlantel.setSelectedIndex(-1);
                                }
                                getjProgressBar().setValue(70);
                                if (departamentos == null) {
                                    DepartamentoController dc = new DepartamentoController();
                                    departamentos = dc.find(new String(), 0, max, zero).getData();
                                    departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                    departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                                    jComboBoxDepartamento.setSelectedIndex(-1);
                                }
                                getjProgressBar().setValue(80);
                                isAceptUsuario = true;
                                trabajador.getPersonaFisica().getPersona().getUsuarioPersona().getUsuario().setNombre(usuario);
                                jTabbedPane.removeAll();
                                jTabbedPane.add(jPanelTrabajador, "Trabajador");
                            } catch (Exception e) {
                                setVisible(false);
                                Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, e);
                                ErrorLog err = new ErrorLog(THIS, e);
                            }
                        }

                    } else {
                        try {
                            getjProgressBar().setValue(60);
                            if (planteles == null) {
                                PlantelController pc = new PlantelController();
                                planteles = pc.find(new String(), 0, max, zero).getData();
                                planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                                jComboBoxPlantel.setSelectedIndex(-1);
                            }
                            getjProgressBar().setValue(70);
                            if (departamentos == null) {
                                DepartamentoController dc = new DepartamentoController();
                                departamentos = dc.find(new String(), 0, max, zero).getData();
                                departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                                jComboBoxDepartamento.setSelectedIndex(-1);
                            }
                        } catch (Exception e) {
                            setVisible(false);
                            Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, e);
                            ErrorLog err = new ErrorLog(THIS, e);
                        }
                        jTabbedPane.removeAll();
                        jTabbedPane.add(jPanelTrabajador, "Trabajador");
                    }
                }
            };

        } else {
            this.trabajador.getPersonaFisica().getPersona().getUsuarioPersona().setUsuario(null);
            isAceptUsuario = false;
            Proceso p = new Proceso(THIS) {
                @Override
                public void proceso() {
                    try {
                        byte max = Byte.MAX_VALUE;
                        short zero = 0;
                        if (planteles == null) {
                            PlantelController pc = new PlantelController();
                            planteles = pc.find(new String(), 0, max, zero).getData();
                            planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                            jComboBoxPlantel.setSelectedIndex(-1);
                        }
                        if (departamentos == null) {
                            DepartamentoController dc = new DepartamentoController();
                            departamentos = dc.find(new String(), 0, max, zero).getData();
                            departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                            jComboBoxDepartamento.setSelectedIndex(-1);
                        }
                    } catch (Exception e) {
                        setVisible(false);
                        Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, e);
                        ErrorLog err = new ErrorLog(THIS, e);
                    }
                }
            };
            jTabbedPane.removeAll();
            jTabbedPane.add(jPanelTrabajador, "Trabajador");
        }
    }//GEN-LAST:event_jButtonSiguiente03ActionPerformed

    private void jTextFieldUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioKeyReleased
        isAceptUsuario = false;
    }//GEN-LAST:event_jTextFieldUsuarioKeyReleased

    private void jComboBoxDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxDepartamentoItemStateChanged
        int select = jComboBoxDepartamento.getSelectedIndex();
        if(select > -1){
            Proceso p = new Proceso(THIS) {
                @Override
                public void proceso() {
                    byte max = Byte.MAX_VALUE;
                    short zero = 0;
                    Departamento departamento = departamentos.get(select);
                    if(departamento != null){
                        try {
                            jComboBoxPuesto.removeAllItems();
                            PuestoController pc = new PuestoController();
                            puestos = pc.findByDepartamentoClave(departamento.getClave(), max, zero).getData();
                            puestos.sort((x,y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            puestos.stream().forEach(x->jComboBoxPuesto.addItem(x.getNombre()));
                            jComboBoxPuesto.setSelectedIndex(-1);
                        } catch (Exception ex) {
                            Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }   
                }
            };
        }
        
    }//GEN-LAST:event_jComboBoxDepartamentoItemStateChanged

    private void jTextFieldCorreoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCorreoKeyReleased
        jTextFieldUsuario.setText(jTextFieldCorreo.getText());
    }//GEN-LAST:event_jTextFieldCorreoKeyReleased

    private void jButtonJefeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJefeActionPerformed
        JDialogTrabajadorSearchTrabajador jdtst = new JDialogTrabajadorSearchTrabajador(THIS);
        if(jdtst.getTrabajador() != null){
            trabajador.setJefe(jdtst.getTrabajador());
            jTextFieldJefe.setText(trabajador.getJefe().getSeguroSocial().getCURP());
        }
    }//GEN-LAST:event_jButtonJefeActionPerformed

    private void jButtonAtras04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras04ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelUsuario, "Usuario");
    }//GEN-LAST:event_jButtonAtras04ActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        Proceso p = new Proceso(THIS) {
            @Override
            public void proceso() {
                int select = jComboBoxPlantel.getSelectedIndex();
                boolean isNotError = true;
                if(isNotError && select > -1){
                    Plantel plantel = planteles.get(select);
                    trabajador.setPlantel(plantel);
                }else{
                    JOptionPane.showMessageDialog(this, "No se selecciono el plantel", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                select = jComboBoxDepartamento.getSelectedIndex();
                if(isNotError && select > -1){
                    select = jComboBoxPuesto.getSelectedIndex();
                    if(isNotError && select > -1){
                        Puesto puesto = puestos.get(select);
                        trabajador.setPuesto(puesto);
                    }else{
                        JOptionPane.showMessageDialog(this, "No se selecciono el puesto", "Warning", JOptionPane.WARNING_MESSAGE);
                        isNotError = false;
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "No se selecciono el departamento", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                if(isNotError && trabajador.getJefe() == null){
                    JOptionPane.showMessageDialog(this, "No se selecciono el Jefe directo", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                if(isNotError){
                    try {
                        TrabajadorController tc = new TrabajadorController();
                        tc.save(trabajador);
                        THIS.dispose();
                        this.dispose();
                        setVisible(false);
                        JOptionPane.showMessageDialog(this, "Se guarado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        setVisible(false);
                        Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, e);
                        ErrorLog err = new ErrorLog(THIS, e);
                    }
                }
            }
        };
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jFormattedTextNacimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextNacimientoKeyTyped
        String fecha = jFormattedTextNacimiento.getText();
        int size = fecha.length();
        char input = evt.getKeyChar();
        char pre = ' ';
        if(size > 0){
            pre = fecha.charAt(size-1);
            if(pre == '/'){
                pre = fecha.charAt(size-2);
            }
        }
        if(size > 9){
            evt.consume();
            return;
        }
        switch(size){
            case 0:
                if(input < '0' || input > '3'){
                    evt.consume();
                }
                return;
            case 1:
                if(pre == '3'){
                    if(input < '0' || input > '1'){
                        evt.consume();
                    }
                    
                }else if(pre == '0'){
                    if(input < '1' || input > '9'){
                        evt.consume();
                    }
                    
                }else{
                    if(input < '0' || input > '9'){
                        evt.consume();
                    }
                }
                return;
            case 2:
                if(input != '/'){
                        evt.consume();
                }
                return;
            case 3:
                if(input != '0' && input != '1'){
                    evt.consume();
                }
                return;
            case 4:
                if(pre == '1'){
                    if(input < '0' || input > '2'){
                        evt.consume();
                    }
                }else{
                    if(input < '1' || input > '9'){
                        evt.consume();
                    }
                }
                return;
            case 5:
                if(input != '/'){
                        evt.consume();
                    }
                return;
            case 6:
                if(input != '1' && input != '2'){
                    evt.consume();
                }
                return;
            case 7:
                if(pre == '1'){
                    if(input != '9'){
                        evt.consume();
                    }
                }else{
                    if(input != '0'){
                        evt.consume();
                    }
                }
                return;
            
            case 8:
                if(pre == '0'){
                    if(input != '0' && input != '1' && input != '2' && input != '3'){
                        evt.consume();
                    }
                }
                return;
            case 9:
                if(input < '0' || input > '9'){
                    evt.consume();
                }
                
        }
    }//GEN-LAST:event_jFormattedTextNacimientoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonAniadirContacto;
    private javax.swing.JButton jButtonAtras01;
    private javax.swing.JButton jButtonAtras03;
    private javax.swing.JButton jButtonAtras04;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonJefe;
    private javax.swing.JButton jButtonSiguiente00;
    private javax.swing.JButton jButtonSiguiente01;
    private javax.swing.JButton jButtonSiguiente02;
    private javax.swing.JButton jButtonSiguiente03;
    private javax.swing.JCheckBox jCheckBoxAgregarUsuario;
    private javax.swing.JComboBox<String> jComboBoxDepartamento;
    private javax.swing.JComboBox<String> jComboBoxEstados;
    private javax.swing.JComboBox<String> jComboBoxMunicipio;
    private javax.swing.JComboBox<String> jComboBoxPerfil;
    private javax.swing.JComboBox<String> jComboBoxPlantel;
    private javax.swing.JComboBox<String> jComboBoxPuesto;
    private javax.swing.JComboBox<String> jComboBoxSexo;
    private javax.swing.JComboBox<String> jComboBoxTipoContacto;
    private javax.swing.JFormattedTextField jFormattedTextNacimiento;
    private javax.swing.JLabel jLabeCorreo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPerfil;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JMenuItem jMenuItemEliminar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelContacto;
    private javax.swing.JPanel jPanelDireccion;
    private javax.swing.JPanel jPanelPersona;
    private javax.swing.JPanel jPanelTrabajador;
    private javax.swing.JPanel jPanelUsuario;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldAsentamiento;
    private javax.swing.JTextField jTextFieldCURP;
    private javax.swing.JTextField jTextFieldCalle;
    private javax.swing.JTextField jTextFieldCodigoPostal;
    private javax.swing.JTextField jTextFieldContacto;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldJefe;
    private javax.swing.JTextField jTextFieldMaterno;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldNumExt;
    private javax.swing.JTextField jTextFieldNumInt;
    private javax.swing.JTextField jTextFieldPaterno;
    private javax.swing.JTextField jTextFieldRFC;
    private javax.swing.JTextField jTextFieldReferencia;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
