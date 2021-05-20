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
import mx.tecabix.db.entity.Banco;
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
import mx.tecabix.db.entity.Salario;
import mx.tecabix.db.entity.SeguroSocial;
import mx.tecabix.db.entity.Trabajador;
import mx.tecabix.db.entity.Turno;
import mx.tecabix.db.entity.Usuario;
import mx.tecabix.db.entity.UsuarioPersona;
import mx.tecabix.service.Auth;
import mx.tecabix.service.controller.BancoController;
import mx.tecabix.service.controller.CatalogoController;
import mx.tecabix.service.controller.DepartamentoController;
import mx.tecabix.service.controller.EstadoController;
import mx.tecabix.service.controller.MunicipioController;
import mx.tecabix.service.controller.PerfilController;
import mx.tecabix.service.controller.PlantelController;
import mx.tecabix.service.controller.PuestoController;
import mx.tecabix.service.controller.TrabajadorController;
import mx.tecabix.service.controller.TurnoController;
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
    private List<Turno> turnos;
    private List<Catalogo> tipoPeriodos;
    private List<Catalogo> tipoPagos;
    private List<Banco> bancos;

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
        trabajador.setSeguroSocial(new SeguroSocial());
        trabajador.getSeguroSocial().setEntidadFederativa(new Estado());
        trabajador.setSalario(new Salario());
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelPersona, "Persona");
        contactos = trabajador.getPersonaFisica().getPersona().getContactos();
        new Proceso(THIS) {
            @Override
            public void proceso() {
                EstadoController estadoController = new EstadoController();
                try {

                    byte max = Byte.MAX_VALUE;
                    short zero = 0;

                    estados = estadoController.find("", zero, max, zero).getData();
                    getjProgressBar().setValue(30);

                    estados.sort(new Comparator<Estado>() {
                        @Override
                        public int compare(Estado o1, Estado o2) {
                            return o1.getNombre().compareTo(o2.getNombre());
                        }
                    });
                    for (int i = 0; i < estados.size(); i++) {
                        jComboBoxNacimientoEstado.addItem(estados.get(i).getNombre());
                    }
                    getjProgressBar().setValue(50);
                    jComboBoxNacimientoEstado.setSelectedIndex(-1);
                    BancoController bc = new BancoController();
                    bancos = bc.find("", zero, max, zero).getData();
                    bancos.sort(new Comparator<Banco>() {
                        @Override
                        public int compare(Banco o1, Banco o2) {
                            return o1.getNombre().compareTo(o2.getNombre());
                        }
                    });
                    for (int i = 0; i < bancos.size(); i++) {
                        jComboBoxBanco.addItem(bancos.get(i).getNombre());
                    }
                    jComboBoxBanco.setSelectedIndex(-1);
                    getjProgressBar().setValue(60);
                    CatalogoController cc = new CatalogoController();
                    tipoPagos = cc.findByTipoNombre("PAGO_SALARIO").getCatalogos();
                    tipoPagos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                    tipoPagos.stream().forEach(x -> jComboBoxTipoPago.addItem(x.getNombre()));

                    getjProgressBar().setValue(80);

                    tipoPeriodos = cc.findByTipoNombre("PERIODO_SALARIO").getCatalogos();
                    tipoPeriodos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                    tipoPeriodos.stream().forEach(x -> jComboBoxTipoPeriodo.addItem(x.getNombre()));

                    setVisible(false);
                    THIS.setVisible(true);
                } catch (Exception ex) {
                    setVisible(false);
                    Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, ex);
                    ErrorLog e = new ErrorLog(THIS, ex);
                }

            }

        };
        jComboBoxTipoPago.setSelectedIndex(-1);
        jComboBoxTipoPeriodo.setSelectedIndex(-1);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxSexo = new javax.swing.JComboBox<>();
        jButtonSiguiente00 = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jComboBoxNacimientoEstado = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        jFormattedTextNacimientoFcha = new javax.swing.JFormattedTextField();
        jTextFieldNacimientoCity = new javax.swing.JTextField();
        jPanelSeguroSocial = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldCURP = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldRfc = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldAlta = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldBaja = new javax.swing.JTextField();
        jButtonSiguiente01 = new javax.swing.JButton();
        jButtonAtras01 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldObservacionBaja = new javax.swing.JTextField();
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
        jButtonSiguiente02 = new javax.swing.JButton();
        jButtonAtras02 = new javax.swing.JButton();
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
        jButtonSiguiente03 = new javax.swing.JButton();
        jButtonAtras03 = new javax.swing.JButton();
        jPanelUsuario = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jCheckBoxAgregarUsuario = new javax.swing.JCheckBox();
        jLabeCorreo = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabelPerfil = new javax.swing.JLabel();
        jComboBoxPerfil = new javax.swing.JComboBox<>();
        jButtonSiguiente04 = new javax.swing.JButton();
        jButtonAtras04 = new javax.swing.JButton();
        jPanelTrabajador = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButtonSiguiente05 = new javax.swing.JButton();
        jButtonAtras05 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxPlantel = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jComboBoxDepartamento = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxPuesto = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jButtonJefe = new javax.swing.JButton();
        jTextFieldJefe = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jComboBoxTurno = new javax.swing.JComboBox<>();
        jPanelSalario = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jButtonSiguiente07 = new javax.swing.JButton();
        jButtonAtras06 = new javax.swing.JButton();
        jFormattedTextPagoPorPeriodo = new javax.swing.JFormattedTextField();
        jLabel35 = new javax.swing.JLabel();
        jComboBoxTipoPeriodo = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jFormattedTextDiaPorPeriodo = new javax.swing.JFormattedTextField();
        jLabel40 = new javax.swing.JLabel();
        jFormattedTextPagoPorDia = new javax.swing.JFormattedTextField();
        jLabel41 = new javax.swing.JLabel();
        jFormattedTextPagoPorHoa = new javax.swing.JFormattedTextField();
        jLabel42 = new javax.swing.JLabel();
        jFormattedTextHoraPorDia = new javax.swing.JFormattedTextField();
        jPanelPago = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jButtonSiguiente3 = new javax.swing.JButton();
        jButtonAtras07 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jComboBoxTipoPago = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jComboBoxBanco = new javax.swing.JComboBox<>();
        jTextFieldCLAVE = new javax.swing.JTextField();
        jTextFieldNumCuenta = new javax.swing.JTextField();
        jTextFieldScucursal = new javax.swing.JTextField();

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

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nacimiento (City):");

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

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Nacimiento (Est.):");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Nacimiento (Fch):");

        jFormattedTextNacimientoFcha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        jFormattedTextNacimientoFcha.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jFormattedTextNacimientoFcha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextNacimientoFchaKeyTyped(evt);
            }
        });

        jTextFieldNacimientoCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNacimientoCityKeyTyped(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente00, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNombre)
                            .addComponent(jTextFieldPaterno)
                            .addComponent(jTextFieldMaterno, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                            .addComponent(jComboBoxSexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxNacimientoEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldNacimientoCity, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextNacimientoFcha, javax.swing.GroupLayout.Alignment.TRAILING))))
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
                    .addComponent(jLabel28)
                    .addComponent(jComboBoxNacimientoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldNacimientoCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jFormattedTextNacimientoFcha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
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

        jTabbedPane.addTab("tab00", jPanelPersona);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Seguro Social"));

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Numero:");

        jTextFieldNumero.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyTyped(evt);
            }
        });

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("CURP:");

        jTextFieldCURP.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldCURP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCURPKeyTyped(evt);
            }
        });

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("RFC:");

        jTextFieldRfc.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldRfc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldRfcKeyTyped(evt);
            }
        });

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Fecha de Alta:");

        jTextFieldAlta.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldAlta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldAltaKeyTyped(evt);
            }
        });

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Fecha de baja:");
        jLabel24.setEnabled(false);

        jTextFieldBaja.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldBaja.setEnabled(false);
        jTextFieldBaja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBajaKeyTyped(evt);
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

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Observacion (Baja):");
        jLabel27.setEnabled(false);

        jTextFieldObservacionBaja.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTextFieldObservacionBaja.setEnabled(false);
        jTextFieldObservacionBaja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldObservacionBajaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNumero))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCURP, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAlta, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldBaja, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldRfc, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAtras01, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente01, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldObservacionBaja, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldCURP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextFieldRfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextFieldAlta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextFieldBaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jTextFieldObservacionBaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente01)
                    .addComponent(jButtonAtras01)))
        );

        javax.swing.GroupLayout jPanelSeguroSocialLayout = new javax.swing.GroupLayout(jPanelSeguroSocial);
        jPanelSeguroSocial.setLayout(jPanelSeguroSocialLayout);
        jPanelSeguroSocialLayout.setHorizontalGroup(
            jPanelSeguroSocialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSeguroSocialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelSeguroSocialLayout.setVerticalGroup(
            jPanelSeguroSocialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSeguroSocialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab01", jPanelSeguroSocial);

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

        jButtonSiguiente02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente02.setText("Siguiente");
        jButtonSiguiente02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente02ActionPerformed(evt);
            }
        });

        jButtonAtras02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras02.setText("Atras");
        jButtonAtras02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras02ActionPerformed(evt);
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
                        .addGap(0, 276, Short.MAX_VALUE)
                        .addComponent(jButtonAtras02, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente02, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jButtonSiguiente02)
                    .addComponent(jButtonAtras02)))
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

        jTabbedPane.addTab("tab02", jPanelDireccion);

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
                        .addGap(0, 276, Short.MAX_VALUE)
                        .addComponent(jButtonAtras03, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente03, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jButtonSiguiente03)
                    .addComponent(jButtonAtras03)))
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

        jTabbedPane.addTab("tab03", jPanelContacto);

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

        jButtonSiguiente04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente04.setText("Siguiente");
        jButtonSiguiente04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente04ActionPerformed(evt);
            }
        });

        jButtonAtras04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras04.setText("Atras");
        jButtonAtras04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras04ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxAgregarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
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
                        .addComponent(jButtonAtras04, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente04, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jButtonSiguiente04)
                    .addComponent(jButtonAtras04)))
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

        jTabbedPane.addTab("tab04", jPanelUsuario);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Trabajador"));

        jButtonSiguiente05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente05.setText("Siguiente");
        jButtonSiguiente05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente05ActionPerformed(evt);
            }
        });

        jButtonAtras05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras05.setText("Atras");
        jButtonAtras05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras05ActionPerformed(evt);
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

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Turno");

        jComboBoxTurno.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 276, Short.MAX_VALUE)
                        .addComponent(jButtonAtras05, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente05, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jButtonJefe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldJefe))
                            .addComponent(jComboBoxPuesto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxTurno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jComboBoxTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonJefe))
                    .addComponent(jTextFieldJefe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente05)
                    .addComponent(jButtonAtras05)))
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

        jTabbedPane.addTab("tab05", jPanelTrabajador);

        jPanelSalario.setBorder(javax.swing.BorderFactory.createTitledBorder("Salario y Periodo"));

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Pago por periodo");

        jButtonSiguiente07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-correcta.png"))); // NOI18N
        jButtonSiguiente07.setText("Siguiente");
        jButtonSiguiente07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente07ActionPerformed(evt);
            }
        });

        jButtonAtras06.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras06.setText("Atras");
        jButtonAtras06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras06ActionPerformed(evt);
            }
        });

        jFormattedTextPagoPorPeriodo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextPagoPorPeriodo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Tipo periodo");

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Dias por periodo");

        jFormattedTextDiaPorPeriodo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextDiaPorPeriodo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Pago por dia");

        jFormattedTextPagoPorDia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextPagoPorDia.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Pago por hora");

        jFormattedTextPagoPorHoa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextPagoPorHoa.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText(" Horas por dias");

        jFormattedTextHoraPorDia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextHoraPorDia.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxTipoPeriodo, 0, 408, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 277, Short.MAX_VALUE)
                        .addComponent(jButtonAtras06, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente07, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextPagoPorPeriodo))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextDiaPorPeriodo))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextPagoPorDia))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextPagoPorHoa))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextHoraPorDia)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jComboBoxTipoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jFormattedTextPagoPorPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jFormattedTextPagoPorDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jFormattedTextPagoPorHoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jFormattedTextDiaPorPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jFormattedTextHoraPorDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente07)
                    .addComponent(jButtonAtras06)))
        );

        javax.swing.GroupLayout jPanelSalarioLayout = new javax.swing.GroupLayout(jPanelSalario);
        jPanelSalario.setLayout(jPanelSalarioLayout);
        jPanelSalarioLayout.setHorizontalGroup(
            jPanelSalarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSalarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelSalarioLayout.setVerticalGroup(
            jPanelSalarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSalarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab06", jPanelSalario);

        jPanelPago.setBorder(javax.swing.BorderFactory.createTitledBorder("Salario y Pago"));

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("CLAVE");

        jButtonSiguiente3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/disquete.png"))); // NOI18N
        jButtonSiguiente3.setText("Guardar");
        jButtonSiguiente3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiguiente3ActionPerformed(evt);
            }
        });

        jButtonAtras07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/flecha-izquierda.png"))); // NOI18N
        jButtonAtras07.setText("Atras");
        jButtonAtras07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtras07ActionPerformed(evt);
            }
        });

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Tipo pago");

        jComboBoxTipoPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTipoPagoItemStateChanged(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Num. Cuenta");

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Sucursal");

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Banco");

        jComboBoxBanco.setEnabled(false);

        jTextFieldCLAVE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCLAVEKeyTyped(evt);
            }
        });

        jTextFieldNumCuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNumCuentaKeyTyped(evt);
            }
        });

        jTextFieldScucursal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldScucursalKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxTipoPago, 0, 408, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(0, 277, Short.MAX_VALUE)
                        .addComponent(jButtonAtras07, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSiguiente3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldCLAVE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNumCuenta))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldScucursal)))
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxBanco, 0, 408, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jComboBoxTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jComboBoxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jTextFieldCLAVE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jTextFieldNumCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jTextFieldScucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSiguiente3)
                    .addComponent(jButtonAtras07)))
        );

        javax.swing.GroupLayout jPanelPagoLayout = new javax.swing.GroupLayout(jPanelPago);
        jPanelPago.setLayout(jPanelPagoLayout);
        jPanelPagoLayout.setHorizontalGroup(
            jPanelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelPagoLayout.setVerticalGroup(
            jPanelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("tab07", jPanelPago);

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

        if (jComboBoxNacimientoEstado.getSelectedIndex() != -1) {
            personaFisica.getSexo().setNombre(jComboBoxSexo.getSelectedItem().toString());
            trabajador.getSeguroSocial().getEntidadFederativa().setClave(estados.get(jComboBoxNacimientoEstado.getSelectedIndex()).getClave());
        } else {
            JOptionPane.showMessageDialog(this, "No se a seleccionado el estado donde nacio", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        text = jTextFieldNacimientoCity.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, SeguroSocial.SIZE_CIUDAD, text)) {
            JOptionPane.showMessageDialog(this, "El formato de la ciudad de nacimiento es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getSeguroSocial().setCiudad(text);

        text = jFormattedTextNacimientoFcha.getText();
        try {
            LocalDate fecha = LocalDate.parse(text);
            if (fecha == null) {
                JOptionPane.showMessageDialog(this, "El formato de la fecha de nacimiento es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getPersonaFisica().setFechaNacimiento(fecha);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato de la fecha de nacimiento es incorrecto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jComboBoxSexo.getSelectedIndex() != -1) {
            personaFisica.getSexo().setNombre(jComboBoxSexo.getSelectedItem().toString());
        }
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelSeguroSocial, "Seguro Social");
    }//GEN-LAST:event_jButtonSiguiente00ActionPerformed

    private void jButtonAtras02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras02ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelSeguroSocial, "Seguro Social");
    }//GEN-LAST:event_jButtonAtras02ActionPerformed

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

    private void jButtonSiguiente02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente02ActionPerformed
        int select = jComboBoxEstados.getSelectedIndex();
        if (select < 0) {
            JOptionPane.showMessageDialog(this, "No se a seleccionado un estado", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        select = jComboBoxMunicipio.getSelectedIndex();
        if (select < 0) {
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
    }//GEN-LAST:event_jButtonSiguiente02ActionPerformed

    private void jButtonAtras03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras03ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelDireccion, "Direccion");
    }//GEN-LAST:event_jButtonAtras03ActionPerformed

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

    private void jButtonSiguiente03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente03ActionPerformed

        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelUsuario, "Usuario");
    }//GEN-LAST:event_jButtonSiguiente03ActionPerformed

    private void jButtonAtras04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras04ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelContacto, "Contacto");
    }//GEN-LAST:event_jButtonAtras04ActionPerformed

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

    private void jButtonSiguiente04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente04ActionPerformed
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
                                getjProgressBar().setValue(25);
                                if (planteles == null) {
                                    PlantelController pc = new PlantelController();
                                    planteles = pc.find(new String(), 0, max, zero).getData();
                                    planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                    planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                                    jComboBoxPlantel.setSelectedIndex(-1);
                                }
                                getjProgressBar().setValue(50);
                                if (departamentos == null) {
                                    DepartamentoController dc = new DepartamentoController();
                                    departamentos = dc.find(new String(), 0, max, zero).getData();
                                    departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                    departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                                    jComboBoxDepartamento.setSelectedIndex(-1);
                                }
                                getjProgressBar().setValue(75);
                                if (turnos == null) {
                                    TurnoController tc = new TurnoController();
                                    turnos = tc.find(new String(), 0, max, zero).getData();
                                    turnos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                    turnos.stream().forEach(x -> jComboBoxTurno.addItem(x.getNombre()));
                                    jComboBoxTurno.setSelectedIndex(-1);
                                }
                                getjProgressBar().setValue(90);
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
                            getjProgressBar().setValue(25);
                            if (planteles == null) {
                                PlantelController pc = new PlantelController();
                                planteles = pc.find(new String(), 0, max, zero).getData();
                                planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                                jComboBoxPlantel.setSelectedIndex(-1);
                            }
                            getjProgressBar().setValue(50);
                            if (departamentos == null) {
                                DepartamentoController dc = new DepartamentoController();
                                departamentos = dc.find(new String(), 0, max, zero).getData();
                                departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                                jComboBoxDepartamento.setSelectedIndex(-1);
                            }
                            getjProgressBar().setValue(75);
                            if (turnos == null) {
                                TurnoController tc = new TurnoController();
                                turnos = tc.find(new String(), 0, max, zero).getData();
                                turnos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                                turnos.stream().forEach(x -> jComboBoxTurno.addItem(x.getNombre()));
                                jComboBoxTurno.setSelectedIndex(-1);
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
                        getjProgressBar().setValue(25);
                        if (planteles == null) {
                            PlantelController pc = new PlantelController();
                            planteles = pc.find(new String(), 0, max, zero).getData();
                            planteles.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            planteles.stream().forEach(x -> jComboBoxPlantel.addItem(x.getNombre()));
                            jComboBoxPlantel.setSelectedIndex(-1);
                        }
                        getjProgressBar().setValue(50);
                        if (departamentos == null) {
                            DepartamentoController dc = new DepartamentoController();
                            departamentos = dc.find(new String(), 0, max, zero).getData();
                            departamentos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            departamentos.stream().forEach(x -> jComboBoxDepartamento.addItem(x.getNombre()));
                            jComboBoxDepartamento.setSelectedIndex(-1);
                        }
                        getjProgressBar().setValue(75);
                        if (turnos == null) {
                            TurnoController tc = new TurnoController();
                            turnos = tc.find(new String(), 0, max, zero).getData();
                            turnos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            turnos.stream().forEach(x -> jComboBoxTurno.addItem(x.getNombre()));
                            jComboBoxTurno.setSelectedIndex(-1);
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
    }//GEN-LAST:event_jButtonSiguiente04ActionPerformed

    private void jTextFieldUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioKeyReleased
        isAceptUsuario = false;
    }//GEN-LAST:event_jTextFieldUsuarioKeyReleased

    private void jComboBoxDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxDepartamentoItemStateChanged
        int select = jComboBoxDepartamento.getSelectedIndex();
        if (select > -1) {
            Proceso p = new Proceso(THIS) {
                @Override
                public void proceso() {
                    byte max = Byte.MAX_VALUE;
                    short zero = 0;
                    Departamento departamento = departamentos.get(select);
                    if (departamento != null) {
                        try {
                            jComboBoxPuesto.removeAllItems();
                            PuestoController pc = new PuestoController();
                            puestos = pc.findByDepartamentoClave(departamento.getClave(), max, zero).getData();
                            puestos.sort((x, y) -> x.getNombre().compareToIgnoreCase(y.getNombre()));
                            puestos.stream().forEach(x -> jComboBoxPuesto.addItem(x.getNombre()));
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
        if (jdtst.getTrabajador() != null) {
            trabajador.setJefe(jdtst.getTrabajador());
            jTextFieldJefe.setText(trabajador.getJefe().getSeguroSocial().getCURP());
        }
    }//GEN-LAST:event_jButtonJefeActionPerformed

    private void jButtonAtras05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras05ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelUsuario, "Usuario");
    }//GEN-LAST:event_jButtonAtras05ActionPerformed

    private void jButtonSiguiente05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente05ActionPerformed
        Proceso p = new Proceso(THIS) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(30);
                int select = jComboBoxPlantel.getSelectedIndex();
                boolean isNotError = true;
                if (isNotError && select > -1) {
                    Plantel plantel = planteles.get(select);
                    trabajador.setPlantel(plantel);
                } else {
                    JOptionPane.showMessageDialog(this, "No se selecciono el plantel", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                select = jComboBoxDepartamento.getSelectedIndex();
                if (isNotError && select > -1) {
                    select = jComboBoxPuesto.getSelectedIndex();
                    if (isNotError && select > -1) {
                        Puesto puesto = puestos.get(select);
                        trabajador.setPuesto(puesto);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se selecciono el puesto", "Warning", JOptionPane.WARNING_MESSAGE);
                        isNotError = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se selecciono el departamento", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                if (isNotError && trabajador.getJefe() == null) {
                    JOptionPane.showMessageDialog(this, "No se selecciono el Jefe directo", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                select = jComboBoxTurno.getSelectedIndex();
                if (isNotError && select > -1) {
                    trabajador.setTurno(turnos.get(select));
                } else {
                    JOptionPane.showMessageDialog(this, "No se selecciono el Turno", "Warning", JOptionPane.WARNING_MESSAGE);
                    isNotError = false;
                }
                getjProgressBar().setValue(80);

                if (isNotError) {
                    jTabbedPane.removeAll();
                    jTabbedPane.add(jPanelSalario, "Salario y periodo");
                }
            }
        };
    }//GEN-LAST:event_jButtonSiguiente05ActionPerformed

    private void jTextFieldNumeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyTyped
        String text = jTextFieldNumero.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_NUMERIC, SeguroSocial.SIZE_NUMERO, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumeroKeyTyped

    private void jTextFieldCURPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCURPKeyTyped
        String text = jTextFieldCURP.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, SeguroSocial.SIZE_CURP, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCURPKeyTyped

    private void jTextFieldRfcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRfcKeyTyped
        String text = jTextFieldRfc.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, SeguroSocial.SIZE_RFC, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldRfcKeyTyped

    private void jTextFieldAltaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAltaKeyTyped
        String fecha = jTextFieldAlta.getText();
        int size = fecha.length();
        char input = evt.getKeyChar();
        char pre = ' ';
        if (size > 0) {
            pre = fecha.charAt(size - 1);
            if (pre == '-') {
                pre = fecha.charAt(size - 2);
            }
        }
        if (size > 9) {
            evt.consume();
            return;
        }
        switch (size) {
            case 0:
                if (input != '1' && input != '2') {
                    evt.consume();
                }
                return;
            case 1:
                if (pre == '1') {
                    if (input != '9') {
                        evt.consume();
                    }
                } else {
                    if (input != '0') {
                        evt.consume();
                    }
                }
                return;

            case 2:
                if (pre == '0') {
                    if (input != '0' && input != '1' && input != '2' && input != '3') {
                        evt.consume();
                    }
                }
                return;
            case 3:
                if (input < '0' || input > '9') {
                    evt.consume();
                }
                return;
            case 4:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 5:
                if (input != '0' && input != '1') {
                    evt.consume();
                }
                return;
            case 6:
                if (pre == '1') {
                    if (input < '0' || input > '2') {
                        evt.consume();
                    }
                } else {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }
                }
                return;

            case 7:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 8:
                if (input < '0' || input > '3') {
                    evt.consume();
                }
                return;
            case 9:
                if (pre == '3') {
                    if (input < '0' || input > '1') {
                        evt.consume();
                    }

                } else if (pre == '0') {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }

                } else {
                    if (input < '0' || input > '9') {
                        evt.consume();
                    }
                }
                return;
        }
    }//GEN-LAST:event_jTextFieldAltaKeyTyped

    private void jTextFieldBajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBajaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBajaKeyTyped

    private void jButtonSiguiente01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente01ActionPerformed
        String text = jTextFieldNumero.getText();
        if (auth.isNotValid(Auth.TIPO_NUMERIC, SeguroSocial.SIZE_NUMERO, text)) {
            JOptionPane.showMessageDialog(this, "El formato del numero no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getSeguroSocial().setNumero(text);
        text = jTextFieldCURP.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, SeguroSocial.SIZE_CURP, text)) {
            JOptionPane.showMessageDialog(this, "El formato del CURP no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getSeguroSocial().setCURP(text);
        text = jTextFieldRfc.getText();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, SeguroSocial.SIZE_RFC, text)) {
            JOptionPane.showMessageDialog(this, "El formato del RFC no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        trabajador.getSeguroSocial().setRfc(text);
        try {
            text = jTextFieldAlta.getText();
            trabajador.getSeguroSocial().setAlta(LocalDate.parse(text));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato de la fecha no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jComboBoxEstados.getSelectedIndex() < 0) {
            jComboBoxEstados.removeAllItems();
            for (int i = 0; i < estados.size(); i++) {
                jComboBoxEstados.addItem(estados.get(i).getNombre());
            }
            jComboBoxEstados.setSelectedIndex(-1);
        }

        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelDireccion, "Direccion");
    }//GEN-LAST:event_jButtonSiguiente01ActionPerformed

    private void jButtonAtras01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras01ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelPersona, "Persona");
    }//GEN-LAST:event_jButtonAtras01ActionPerformed

    private void jTextFieldObservacionBajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldObservacionBajaKeyTyped
        String fecha = jFormattedTextNacimientoFcha.getText();
        int size = fecha.length();
        char input = evt.getKeyChar();
        char pre = ' ';
        if (size > 0) {
            pre = fecha.charAt(size - 1);
            if (pre == '-') {
                pre = fecha.charAt(size - 2);
            }
        }
        if (size > 9) {
            evt.consume();
            return;
        }
        switch (size) {
            case 0:
                if (input != '1' && input != '2') {
                    evt.consume();
                }
                return;
            case 1:
                if (pre == '1') {
                    if (input != '9') {
                        evt.consume();
                    }
                } else {
                    if (input != '0') {
                        evt.consume();
                    }
                }
                return;

            case 2:
                if (pre == '0') {
                    if (input != '0' && input != '1' && input != '2' && input != '3') {
                        evt.consume();
                    }
                }
                return;
            case 3:
                if (input < '0' || input > '9') {
                    evt.consume();
                }
                return;
            case 4:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 5:
                if (input != '0' && input != '1') {
                    evt.consume();
                }
                return;
            case 6:
                if (pre == '1') {
                    if (input < '0' || input > '2') {
                        evt.consume();
                    }
                } else {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }
                }
                return;

            case 7:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 8:
                if (input < '0' || input > '3') {
                    evt.consume();
                }
                return;
            case 9:
                if (pre == '3') {
                    if (input < '0' || input > '1') {
                        evt.consume();
                    }

                } else if (pre == '0') {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }

                } else {
                    if (input < '0' || input > '9') {
                        evt.consume();
                    }
                }
                return;
        }
    }//GEN-LAST:event_jTextFieldObservacionBajaKeyTyped

    private void jFormattedTextNacimientoFchaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextNacimientoFchaKeyTyped
        String fecha = jFormattedTextNacimientoFcha.getText();
        int size = fecha.length();
        char input = evt.getKeyChar();
        char pre = ' ';
        if (size > 0) {
            pre = fecha.charAt(size - 1);
            if (pre == '-') {
                pre = fecha.charAt(size - 2);
            }
        }
        if (size > 9) {
            evt.consume();
            return;
        }
        switch (size) {
            case 0:
                if (input != '1' && input != '2') {
                    evt.consume();
                }
                return;
            case 1:
                if (pre == '1') {
                    if (input != '9') {
                        evt.consume();
                    }
                } else {
                    if (input != '0') {
                        evt.consume();
                    }
                }
                return;

            case 2:
                if (pre == '0') {
                    if (input != '0' && input != '1' && input != '2' && input != '3') {
                        evt.consume();
                    }
                }
                return;
            case 3:
                if (input < '0' || input > '9') {
                    evt.consume();
                }
                return;
            case 4:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 5:
                if (input != '0' && input != '1') {
                    evt.consume();
                }
                return;
            case 6:
                if (pre == '1') {
                    if (input < '0' || input > '2') {
                        evt.consume();
                    }
                } else {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }
                }
                return;

            case 7:
                if (input != '-') {
                    evt.consume();
                }
                return;

            case 8:
                if (input < '0' || input > '3') {
                    evt.consume();
                }
                return;
            case 9:
                if (pre == '3') {
                    if (input < '0' || input > '1') {
                        evt.consume();
                    }

                } else if (pre == '0') {
                    if (input < '1' || input > '9') {
                        evt.consume();
                    }

                } else {
                    if (input < '0' || input > '9') {
                        evt.consume();
                    }
                }
                return;
        }
    }//GEN-LAST:event_jFormattedTextNacimientoFchaKeyTyped

    private void jButtonSiguiente07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente07ActionPerformed
        int select = jComboBoxTipoPeriodo.getSelectedIndex();
        boolean isNotError = true;
        if (isNotError && select > -1) {
            trabajador.getSalario().setTipoPeriodo(tipoPeriodos.get(select));
        } else {
            JOptionPane.showMessageDialog(this, "No se selecciono el tipo de periodo", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = jFormattedTextPagoPorPeriodo.getText();
        try {
            Float moneda = Float.parseFloat(text);
            if (moneda <= 0) {
                JOptionPane.showMessageDialog(this, "El valor del pago por periodo debe ser mayor a 0", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getSalario().setPeriodo((int) (moneda * 100));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato del pago por periodo no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        text = jFormattedTextPagoPorDia.getText();
        try {
            Float moneda = Float.parseFloat(text);
            if (moneda <= 0) {
                JOptionPane.showMessageDialog(this, "El valor del pago por dia debe ser mayor a 0", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getSalario().setDia((int) (moneda * 100));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato del pago por diar no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        text = jFormattedTextPagoPorHoa.getText();
        try {
            Float moneda = Float.parseFloat(text);
            if (moneda <= 0) {
                JOptionPane.showMessageDialog(this, "El valor del pago por hora debe ser mayor a 0", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getSalario().setHora((int) (moneda * 100));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato del pago por hora no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        text = jFormattedTextDiaPorPeriodo.getText();
        try {
            Integer dias = Integer.parseInt(text);
            if (dias <= 0) {
                JOptionPane.showMessageDialog(this, "El valor de dias por periodo debe ser mayor a 0", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getSalario().setDiaPorPeriodo(dias);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato de dias por periodo no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        text = jFormattedTextHoraPorDia.getText();
        try {
            Integer horas = Integer.parseInt(text);
            if (horas <= 0) {
                JOptionPane.showMessageDialog(this, "El valor de horas por dia debe ser mayor a 0", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            trabajador.getSalario().setHoraPorDia(horas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El formato de horas por dias no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelPago, "Salario Pago");
    }//GEN-LAST:event_jButtonSiguiente07ActionPerformed

    private void jButtonAtras06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras06ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelTrabajador, "Trabajador");
    }//GEN-LAST:event_jButtonAtras06ActionPerformed

    private void jButtonSiguiente3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiguiente3ActionPerformed
        int select = jComboBoxTipoPago.getSelectedIndex();
        String text;
        if (select > -1) {
            trabajador.getSalario().setTipoPago(tipoPagos.get(select));
        } else {
            JOptionPane.showMessageDialog(this, "No se selecciono el tipo de pago", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jComboBoxBanco.isEnabled()) {
            select = jComboBoxBanco.getSelectedIndex();
            if (select > -1) {
                trabajador.getSalario().setBanco(bancos.get(select));
            } else {
                JOptionPane.showMessageDialog(this, "No se selecciono el banco de pago", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            trabajador.getSalario().setBanco(null);
        }
        if (jTextFieldCLAVE.isEnabled()) {
            text = jTextFieldCLAVE.getText();
            if (!text.isEmpty()) {
                if (auth.isNotValid(Auth.TIPO_NUMERIC, Salario.SIZE_CLAVE_INTERBANCARIA, text)) {
                    JOptionPane.showMessageDialog(this, "El formato de la clave no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                text = null;
            }
            trabajador.getSalario().setClaveInterBancaria(text);
        }
        if (jTextFieldNumCuenta.isEnabled()) {
            text = jTextFieldNumCuenta.getText();
            if (!text.isEmpty()) {
                if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, Salario.SIZE_NUMERO_CUENTA, text)) {
                    JOptionPane.showMessageDialog(this, "El formato del numero de la cuenta no es correcto", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                text = null;
            }
            trabajador.getSalario().setNumeroCuenta(text);
        }
        if (jTextFieldScucursal.isEnabled()) {
            text = jTextFieldScucursal.getText();
            if (!text.isEmpty()) {
                if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Salario.SIZE_SUCURSAL, text)) {
                    JOptionPane.showMessageDialog(this, "El formato de la sucursal no es correcta", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                text = null;
            }
            trabajador.getSalario().setSucursal(text);
        }
        Proceso p = new Proceso(THIS) {
            @Override
            public void proceso() {
                try {
                    getjProgressBar().setValue(80);
                    TrabajadorController tc = new TrabajadorController();
                    tc.save(trabajador);
                    
                    setVisible(false);
                    repaint();
                    THIS.dispose();
                    THIS.repaint();
                    JOptionPane.showMessageDialog(this, "Se guarado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception e) {
                    setVisible(false);
                    Logger.getLogger(JDialogTrabajadorNuevo.class.getName()).log(Level.SEVERE, null, e);
                    ErrorLog err = new ErrorLog(THIS, e);
                }
            }
        };
    }//GEN-LAST:event_jButtonSiguiente3ActionPerformed

    private void jButtonAtras07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtras07ActionPerformed
        jTabbedPane.removeAll();
        jTabbedPane.add(jPanelSalario, "Salario y periodo");
    }//GEN-LAST:event_jButtonAtras07ActionPerformed

    private void jTextFieldNacimientoCityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNacimientoCityKeyTyped
        String text = jTextFieldNacimientoCity.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE, SeguroSocial.SIZE_CIUDAD, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNacimientoCityKeyTyped

    private void jComboBoxTipoPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTipoPagoItemStateChanged
        boolean isNotEfectivo = !"EFECTIVO".equals(jComboBoxTipoPago.getSelectedItem().toString());
        jComboBoxBanco.setEnabled(isNotEfectivo);
        jTextFieldCLAVE.setEnabled(isNotEfectivo);
        jTextFieldNumCuenta.setEnabled(isNotEfectivo);
        jTextFieldScucursal.setEnabled(isNotEfectivo);
        if (!isNotEfectivo) {
            jTextFieldCLAVE.setText(new String());
            jTextFieldNumCuenta.setText(new String());
            jTextFieldScucursal.setText(new String());
            jComboBoxBanco.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_jComboBoxTipoPagoItemStateChanged

    private void jTextFieldCLAVEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCLAVEKeyTyped
        String text = jTextFieldCLAVE.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_NUMERIC, Salario.SIZE_CLAVE_INTERBANCARIA, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldCLAVEKeyTyped

    private void jTextFieldNumCuentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumCuentaKeyTyped
        String text = jTextFieldNumCuenta.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC, Salario.SIZE_NUMERO_CUENTA, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldNumCuentaKeyTyped

    private void jTextFieldScucursalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldScucursalKeyTyped
        String text = jTextFieldScucursal.getText() + evt.getKeyChar();
        if (auth.isNotValid(Auth.TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, Salario.SIZE_SUCURSAL, text)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldScucursalKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAniadirContacto;
    private javax.swing.JButton jButtonAtras01;
    private javax.swing.JButton jButtonAtras02;
    private javax.swing.JButton jButtonAtras03;
    private javax.swing.JButton jButtonAtras04;
    private javax.swing.JButton jButtonAtras05;
    private javax.swing.JButton jButtonAtras06;
    private javax.swing.JButton jButtonAtras07;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonJefe;
    private javax.swing.JButton jButtonSiguiente00;
    private javax.swing.JButton jButtonSiguiente01;
    private javax.swing.JButton jButtonSiguiente02;
    private javax.swing.JButton jButtonSiguiente03;
    private javax.swing.JButton jButtonSiguiente04;
    private javax.swing.JButton jButtonSiguiente05;
    private javax.swing.JButton jButtonSiguiente07;
    private javax.swing.JButton jButtonSiguiente3;
    private javax.swing.JCheckBox jCheckBoxAgregarUsuario;
    private javax.swing.JComboBox<String> jComboBoxBanco;
    private javax.swing.JComboBox<String> jComboBoxDepartamento;
    private javax.swing.JComboBox<String> jComboBoxEstados;
    private javax.swing.JComboBox<String> jComboBoxMunicipio;
    private javax.swing.JComboBox<String> jComboBoxNacimientoEstado;
    private javax.swing.JComboBox<String> jComboBoxPerfil;
    private javax.swing.JComboBox<String> jComboBoxPlantel;
    private javax.swing.JComboBox<String> jComboBoxPuesto;
    private javax.swing.JComboBox<String> jComboBoxSexo;
    private javax.swing.JComboBox<String> jComboBoxTipoContacto;
    private javax.swing.JComboBox<String> jComboBoxTipoPago;
    private javax.swing.JComboBox<String> jComboBoxTipoPeriodo;
    private javax.swing.JComboBox<String> jComboBoxTurno;
    private javax.swing.JFormattedTextField jFormattedTextDiaPorPeriodo;
    private javax.swing.JFormattedTextField jFormattedTextHoraPorDia;
    private javax.swing.JFormattedTextField jFormattedTextNacimientoFcha;
    private javax.swing.JFormattedTextField jFormattedTextPagoPorDia;
    private javax.swing.JFormattedTextField jFormattedTextPagoPorHoa;
    private javax.swing.JFormattedTextField jFormattedTextPagoPorPeriodo;
    private javax.swing.JLabel jLabeCorreo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPerfil;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JMenuItem jMenuItemEliminar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelContacto;
    private javax.swing.JPanel jPanelDireccion;
    private javax.swing.JPanel jPanelPago;
    private javax.swing.JPanel jPanelPersona;
    private javax.swing.JPanel jPanelSalario;
    private javax.swing.JPanel jPanelSeguroSocial;
    private javax.swing.JPanel jPanelTrabajador;
    private javax.swing.JPanel jPanelUsuario;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldAlta;
    private javax.swing.JTextField jTextFieldAsentamiento;
    private javax.swing.JTextField jTextFieldBaja;
    private javax.swing.JTextField jTextFieldCLAVE;
    private javax.swing.JTextField jTextFieldCURP;
    private javax.swing.JTextField jTextFieldCalle;
    private javax.swing.JTextField jTextFieldCodigoPostal;
    private javax.swing.JTextField jTextFieldContacto;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldJefe;
    private javax.swing.JTextField jTextFieldMaterno;
    private javax.swing.JTextField jTextFieldNacimientoCity;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldNumCuenta;
    private javax.swing.JTextField jTextFieldNumExt;
    private javax.swing.JTextField jTextFieldNumInt;
    private javax.swing.JTextField jTextFieldNumero;
    private javax.swing.JTextField jTextFieldObservacionBaja;
    private javax.swing.JTextField jTextFieldPaterno;
    private javax.swing.JTextField jTextFieldReferencia;
    private javax.swing.JTextField jTextFieldRfc;
    private javax.swing.JTextField jTextFieldScucursal;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
