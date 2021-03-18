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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import mx.tecabix.ConfiguracionConexion;
import mx.tecabix.service.controller.SesionConreoller;
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.Licencia;
import mx.tecabix.db.entity.Servicio;
import mx.tecabix.db.entity.Sesion;
import mx.tecabix.db.entity.Usuario;
import mx.tecabix.service.Auth;
import mx.tecabix.service.PageGeneric;
import mx.tecabix.service.page.SesionPage;
import mx.tecabix.view.BotonPagina;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.ModelT;
import mx.tecabix.view.Proceso;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class JPanelSesion extends javax.swing.JPanel {

    private final Auth auth;
    private final JFrame parent;
    private ModelT defaultTableModel;
    private List<Sesion> sesiones;
    private short pagina;
    private String search;

    private final short USUARIO = 0;
    private final short LICENCIA = 1;
    private final short TIPO_DISPOSITIVO = 2;
    private final short SERVICIO = 3;
    private final short PETICIONES = 4;
    private final short NUM_COLUMNAS = 5;

    private final String ROOT_SESION_ELIMINAR = "ROOT_SESION_ELIMINAR";

    public JPanelSesion(JFrame parent) {
        initComponents();
        this.auth = new Auth();
        this.parent = parent;
        if (auth.isNotAuthorized(ROOT_SESION_ELIMINAR)) {
            jButtonEliminar.setEnabled(false);
            jMenuItemEliminar.setEnabled(false);
        }
        search = new String();
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
            SesionConreoller sesionConreoller = new SesionConreoller();
            byte elements = 25;
            int by = jComboBoxBy.getSelectedIndex();
            if (search == null) {
                search = new String();
            }
            SesionPage response = sesionConreoller.find(search, by, elements, page);
            if (response != null) {
                this.sesiones = response.getData();
                if (!this.sesiones.isEmpty()) {
                    jButtonEliminar.setEnabled(true);
                    if (auth.isAuthorized(ROOT_SESION_ELIMINAR)) {
                        jButtonEliminar.setEnabled(true);
                        jMenuItemEliminar.setEnabled(true);
                    }
                    jButtonVer.setEnabled(true);
                    response.getTotalPages();
                    paginacion(response, pagina);
                } else {
                    jButtonEliminar.setEnabled(false);
                    jButtonVer.setEnabled(false);
                }
            } else {
                this.sesiones = new ArrayList<>();
                jButtonEliminar.setEnabled(false);
                jButtonVer.setEnabled(false);
            }
        } catch (Exception ex) {
            Logger.getLogger(JPanelSesion.class.getName()).log(Level.SEVERE, null, ex);
            ErrorLog errorLog = new ErrorLog(parent, ex);
        }

    }

    public void init() {
        new Proceso(parent) {
            @Override
            public void proceso() {
                getjProgressBar().setValue(80);
                cargarDatos(pagina);
                counstrurTabla();
            }
        };
    }

    private void counstrurTabla() {
        cargarTabla();
        formatearTabla();
        this.validate();
        this.repaint();
    }

    private void cargarTabla() {
        try {
            String[] header = new String[NUM_COLUMNAS];
            header[USUARIO] = "USUARIO";
            header[LICENCIA] = "LICENCIA";
            header[TIPO_DISPOSITIVO] = "TIPO DE DISPOSITIVO";
            header[SERVICIO] = "SERVICIO";
            header[PETICIONES] = "PETICIONES";
            Class[] headerType = new Class[NUM_COLUMNAS];

            headerType[USUARIO] = java.lang.String.class;
            headerType[LICENCIA] = java.lang.String.class;
            headerType[TIPO_DISPOSITIVO] = java.lang.String.class;
            headerType[SERVICIO] = java.lang.String.class;
            headerType[PETICIONES] = java.lang.Integer.class;
            defaultTableModel = new ModelT(header, headerType);
            for (int i = 0; i < sesiones.size(); i++) {
                Sesion sesion = sesiones.get(i);
                defaultTableModel.addRow(new java.util.Vector());
                Usuario usuario = sesion.getUsuario();
                Licencia licencia = sesion.getLicencia();
                Catalogo tipo_dispo = licencia.getServicio().getTipo();
                Servicio servicio = licencia.getServicio();
                defaultTableModel.setValueAt(usuario.getNombre(), i, 0);
                defaultTableModel.setValueAt(licencia.getNombre(), i, 1);
                defaultTableModel.setValueAt(tipo_dispo.getNombre(), i, 2);
                defaultTableModel.setValueAt(servicio.getNombre(), i, 3);
                defaultTableModel.setValueAt(sesion.getPeticionesRestantes(), i, 4);
            }
            jTable.setModel(defaultTableModel);
            if (!this.sesiones.isEmpty()) {
                jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jTable.setRowSelectionInterval(0, 0);
            }

        } catch (Exception e) {
            ErrorLog error = new ErrorLog(parent, e);
        }
    }

    private void formatearTabla() {
        try {
            jTable.setFont(new Font("Arial", Font.PLAIN, 20));
            jTable.setRowHeight(30);
            jTable.getColumnModel().getColumn(USUARIO).setMinWidth(250);
            jTable.getColumnModel().getColumn(USUARIO).setMaxWidth(300);
            jTable.getColumnModel().getColumn(LICENCIA).setMinWidth(50);
            jTable.getColumnModel().getColumn(TIPO_DISPOSITIVO).setMinWidth(50);
            jTable.getColumnModel().getColumn(SERVICIO).setMinWidth(50);
            jTable.getColumnModel().getColumn(PETICIONES).setMaxWidth(100);
            jTable.getColumnModel().getColumn(PETICIONES).setMinWidth(85);
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
            ErrorLog error = new ErrorLog(parent, e);
        }
    }

    private void ver() {
        int selected = jTable.getSelectedRow();
        if (selected >= 0 && selected < sesiones.size()) {
            Sesion sesion = sesiones.get(selected);
            JDialogSesionVer ver = new JDialogSesionVer(parent, sesion);
        } else {
            JOptionPane.showMessageDialog(parent, "No se puede realizar la accion", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminar() {
        if (auth.isNotAuthorized(ROOT_SESION_ELIMINAR)) {
            return;
        }
        ConfiguracionConexion conexion = ConfiguracionConexion.getConfiguracionConexion();
        Sesion sesionActual = conexion.getSesion();
        int selected = jTable.getSelectedRow();
        if (selected >= 0 && selected < sesiones.size()) {
            Sesion sesion = sesiones.get(selected);
            if (sesion.getClave().equals(sesionActual.getClave())) {
                JOptionPane.showMessageDialog(parent, "Para eliminar la sesión actual, tiene que cerrar la sesión.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int op = JOptionPane.showConfirmDialog(
                        parent, "¿Seguro que desea eliminar la sesión de " + sesion.getUsuario().getNombre() + "?",
                        "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (op == JOptionPane.NO_OPTION) {
                    return;
                }
                Proceso proceso = new Proceso(parent) {
                    @Override
                    public void proceso() {
                        try {
                            getjProgressBar().setValue(40);
                            SesionConreoller sesionConreoller = new SesionConreoller();
                            sesionConreoller.delete(sesion.getClave());
                            getjProgressBar().setValue(80);
                            cargarDatos(pagina);
                            counstrurTabla();
                        } catch (Exception ex) {
                            Logger.getLogger(JPanelSesion.class.getName()).log(Level.SEVERE, null, ex);
                            String msj_401 = "<html><h3>Acceso denegado</h3>"
                                    + "<h4>Posibles causas:</h4>"
                                    + "<ul>"
                                    + "<li>Su sesion a caducado</li>"
                                    + "</ul></html>";
                            String msj_404 = "<html><h3>No se encontro la sesion</h3>"
                                    + "<h4>Posibles causas:</h4>"
                                    + "<ul>"
                                    + "<li>La sesion que intento eliminar a caducado</li>"
                                    + "</ul></html>";
                            if (ex.getMessage().equalsIgnoreCase("401 Unauthorized")) {
                                JOptionPane.showMessageDialog(this, msj_401, "Acceso no Authorizado", JOptionPane.WARNING_MESSAGE);
                            } else if (ex.getMessage().equalsIgnoreCase("404 Not Found")) {
                                JOptionPane.showMessageDialog(this, msj_404, "Recurso no encontrado", JOptionPane.ERROR_MESSAGE);
                            } else {
                                ErrorLog err = new ErrorLog(parent, ex);
                            }
                        }
                    }
                };
            }
        } else {
            JOptionPane.showMessageDialog(parent, "No se puede realizar la accion", "Warning", JOptionPane.WARNING_MESSAGE);
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
        jMenuItemVer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemEliminar = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemRefrescar = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonEliminar = new javax.swing.JButton();
        jButtonVer = new javax.swing.JButton();
        jButtonRefrescar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBoxBy = new javax.swing.JComboBox<>();
        jTextFieldSearch = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanelPaginacion = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        jMenuItemVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/ojo.png"))); // NOI18N
        jMenuItemVer.setText("Ver");
        jMenuItemVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVerActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemVer);
        jPopupMenu.add(jSeparator1);

        jMenuItemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/basura.png"))); // NOI18N
        jMenuItemEliminar.setText("Eliminar");
        jMenuItemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEliminarActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemEliminar);
        jPopupMenu.add(jSeparator2);

        jMenuItemRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/refresh.png"))); // NOI18N
        jMenuItemRefrescar.setText("Refrescar tabla");
        jMenuItemRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRefrescarActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemRefrescar);

        jButtonEliminar.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jButtonEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/basura.png"))); // NOI18N
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.setEnabled(false);
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonVer.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jButtonVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/ojo.png"))); // NOI18N
        jButtonVer.setText("Ver");
        jButtonVer.setEnabled(false);
        jButtonVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerActionPerformed(evt);
            }
        });

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/refresh.png"))); // NOI18N
        jButtonRefrescar.setText("Refrecar");
        jButtonRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/buscar.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC", "DES" }));
        jComboBox1.setEnabled(false);

        jComboBoxBy.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jComboBoxBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Licencia", "Servicio" }));

        jTextFieldSearch.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonVer, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldSearch)
                    .addComponent(jComboBoxBy, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonRefrescar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPaginacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
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
        jScrollPane1.setViewportView(jTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerActionPerformed
        ver();
    }//GEN-LAST:event_jButtonVerActionPerformed

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        init();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void jMenuItemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_jMenuItemEliminarActionPerformed

    private void jMenuItemVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVerActionPerformed
        ver();
    }//GEN-LAST:event_jMenuItemVerActionPerformed

    private void jMenuItemRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRefrescarActionPerformed
        init();
    }//GEN-LAST:event_jMenuItemRefrescarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        search = jTextFieldSearch.getText();
        init();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        search = jTextFieldSearch.getText();
        init();
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_jButtonEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonVer;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxBy;
    private javax.swing.JMenuItem jMenuItemEliminar;
    private javax.swing.JMenuItem jMenuItemRefrescar;
    private javax.swing.JMenuItem jMenuItemVer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelPaginacion;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
