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

import mx.tecabix.view.trabajador.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import mx.tecabix.db.entity.Turno;
import mx.tecabix.db.entity.TurnoDia;
import mx.tecabix.service.Auth;
import mx.tecabix.service.PageGeneric;
import mx.tecabix.service.controller.TurnoController;
import mx.tecabix.service.page.TurnoPage;
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
public class JPanelTurno extends javax.swing.JPanel {

    
    private JFrame parent;
    private final Auth auth;
    private String search;
    
    private ModelT defaultTableModel;
    private List<Turno> turnos;
    private short pagina;
    
    private final short NOMBRE = 0;
    private final short DESCRIPCION = 1;
    private final short DOMINGO = 2;
    private final short LUNES = 3;
    private final short MARTES = 4;
    private final short MIERCOLES = 5;
    private final short JUEVEZ = 6;
    private final short VIERNES = 7;
    private final short SABADO = 8;
    
    private final short NUM_COLUMNAS = 9;

    private final String TRABAJADOR_CREAR = "TRABAJADOR_CREAR";
    private final String TRABAJADOR_ELIMINAR = "TRABAJADOR_ELIMINAR";
    private final String TRABAJADOR_EDITAR = "TRABAJADOR_EDITAR";
    public JPanelTurno(JFrame parent) {
        initComponents();
        this.auth = new Auth();
        this.parent = parent;
        if (auth.isNotAuthorized(TRABAJADOR_ELIMINAR)) {
            jButtonEliminar.setEnabled(false);
            jMenuItemEliminar.setEnabled(false);
        }
        if (auth.isNotAuthorized(TRABAJADOR_CREAR)) {
            jButtonNuevo.setEnabled(false);
            jMenuItemNuevo.setEnabled(false);
        }
        if (auth.isNotAuthorized(TRABAJADOR_EDITAR)) {
            jButtonEditar.setEnabled(false);
            jMenuItemEditar.setEnabled(false);
        }
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
            TurnoController turnoController = new TurnoController();
            byte elements = 25;
           
            int by = jComboBoxBy.getSelectedIndex();
            TurnoPage response = null;
            if (search == null) {
                search = new String();
            }
            
            response = turnoController.find(search, by, elements, page);
            if (response != null) {
                this.turnos = response.getData();
                if (!this.turnos.isEmpty()) {
                    if (auth.isAuthorized(TRABAJADOR_ELIMINAR)) {
                        jButtonEliminar.setEnabled(true);
                    }
                    if (auth.isAuthorized(TRABAJADOR_EDITAR)) {
                        jButtonEditar.setEnabled(true);
                    }
                    jButtonVer.setEnabled(true);
                    response.getTotalPages();
                    paginacion(response, pagina);
                } else {
                    jButtonEliminar.setEnabled(false);
                    jButtonVer.setEnabled(false);
                    jButtonEditar.setEnabled(false);
                }
            } else {
                this.turnos = new ArrayList<>();
                jButtonEliminar.setEnabled(false);
                jButtonVer.setEnabled(false);
                jButtonEditar.setEnabled(false);
            }
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
            header[DESCRIPCION] = "DESCRIPCION";
            header[LUNES]       = "LUN";
            header[MARTES]      = "MAR";
            header[MIERCOLES]   = "MIE";
            header[JUEVEZ]      = "JUE";
            header[VIERNES]     = "VIE";
            header[SABADO]      = "SAB";
            header[DOMINGO]     = "DOM";
            Class[] headerType = new Class[NUM_COLUMNAS];

            headerType[NOMBRE]          = java.lang.String.class;
            headerType[DESCRIPCION]     = java.lang.String.class;
            headerType[LUNES]           = java.lang.String.class;
            headerType[MARTES]          = java.lang.String.class;
            headerType[MIERCOLES]       = java.lang.String.class;
            headerType[JUEVEZ]          = java.lang.String.class;
            headerType[VIERNES]         = java.lang.String.class;
            headerType[SABADO]          = java.lang.String.class;
            headerType[DOMINGO]         = java.lang.String.class;
            
            defaultTableModel = new ModelT(header,headerType);
            for (int i = 0; i < turnos.size(); i++) {
                Turno turno = turnos.get(i);
                List<TurnoDia> dias = turno.getTurnoDias();
                defaultTableModel.addRow(new java.util.Vector());
                defaultTableModel.setValueAt(turno.getNombre(),  i, NOMBRE);
                defaultTableModel.setValueAt(turno.getDescripcion(),  i, DESCRIPCION);
                for (TurnoDia dia : dias) {
                    if(dia == null || dia.getInicio() == null || dia.getFin() == null){
                        continue;
                    }
                    String hora = dia.getInicio().toString().concat(" - ").concat(dia.getFin().toString());
                    if(dia.getDia().getNombre().startsWith(header[LUNES])){
                        defaultTableModel.setValueAt(hora,  i, LUNES);
                    }else if(dia.getDia().getNombre().startsWith(header[MARTES])){
                        defaultTableModel.setValueAt(hora,  i, MARTES);
                    }else if(dia.getDia().getNombre().startsWith(header[MIERCOLES])){
                        defaultTableModel.setValueAt(hora,  i, MIERCOLES);
                    }else if(dia.getDia().getNombre().startsWith(header[JUEVEZ])){
                        defaultTableModel.setValueAt(hora,  i, JUEVEZ);
                    }else if(dia.getDia().getNombre().startsWith(header[VIERNES])){
                        defaultTableModel.setValueAt(hora,  i, VIERNES);
                    }else if(dia.getDia().getNombre().startsWith(header[SABADO])){
                        defaultTableModel.setValueAt(hora,  i, SABADO);
                    }else if(dia.getDia().getNombre().startsWith(header[DOMINGO])){
                        defaultTableModel.setValueAt(hora,  i, DOMINGO);
                    }   
                }
            }
            jTable.setModel(defaultTableModel);
            if(!this.turnos.isEmpty()){
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
            jTable.setRowHeight(30);
            jTable.getColumnModel().getColumn(NOMBRE).setMinWidth(150);
            jTable.getColumnModel().getColumn(DESCRIPCION).setMinWidth(350);
            jTable.getColumnModel().getColumn(LUNES).setMinWidth(110);
            jTable.getColumnModel().getColumn(MARTES).setMinWidth(110);
            jTable.getColumnModel().getColumn(MIERCOLES).setMinWidth(110);
            jTable.getColumnModel().getColumn(JUEVEZ).setMinWidth(110);
            jTable.getColumnModel().getColumn(VIERNES).setMinWidth(110);
            jTable.getColumnModel().getColumn(SABADO).setMinWidth(110);
            jTable.getColumnModel().getColumn(DOMINGO).setMinWidth(110);
            
            jTable.getColumnModel().getColumn(LUNES).setMaxWidth(150);
            jTable.getColumnModel().getColumn(MARTES).setMaxWidth(150);
            jTable.getColumnModel().getColumn(MIERCOLES).setMaxWidth(150);
            jTable.getColumnModel().getColumn(JUEVEZ).setMaxWidth(150);
            jTable.getColumnModel().getColumn(VIERNES).setMaxWidth(150);
            jTable.getColumnModel().getColumn(SABADO).setMaxWidth(150);
            jTable.getColumnModel().getColumn(DOMINGO).setMaxWidth(150);
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
            jTable.setComponentPopupMenu(jPopupMenu);
        } catch (Exception e) {
             ErrorLog error = new ErrorLog(parent, e);
        }
    }
    
    private void ver(){
//        int selected = jTable.getSelectedRow();
//        int CLAVE = 6;
//        if(selected >= 0 && selected < turnos.size()){
//            Trabajador trabajador = turnos.get(selected);
//            JDialogTrabajadorVer jddv = new JDialogTrabajadorVer(parent, trabajador);
//            if(jddv.getAccion() != JDialogTrabajadorVer.ACCION_NINGUNO){
//                Trabajador trabajadorBuscando = jddv.getTrabajador();
//                jTextFieldSearch.setText(trabajadorBuscando.getClave().toString());
//                jComboBoxBy.setSelectedIndex(CLAVE);
//                search = jTextFieldSearch.getText();
//                init();
//            }
//        }else{
//            JOptionPane.showMessageDialog(parent, "No se puede realizar la accion", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
    }
    private void editar(){
//        int selected = jTable.getSelectedRow();
//        if(selected >= 0 && selected < trabajadores.size()){
//            Departamento departamento = trabajadores.get(selected);
//            JDialogDepartamentoEditar jdde = new JDialogDepartamentoEditar(parent, departamento);
//            init();
//        }else{
//            JOptionPane.showMessageDialog(parent, "No se puede realizar la accion", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
    }
    
    private void eliminar(){
//        int selected = jTable.getSelectedRow();
//        if(selected >= 0 && selected < trabajadores.size()){
//            Departamento departamento = trabajadores.get(selected);
//            
//            int op = JOptionPane.showConfirmDialog(parent, "¿Estás seguro de eliminar el departamentos de "+departamento.getNombre()+"?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//            if(op == JOptionPane.YES_OPTION){
//                try {
//                    DepartamentoController departamentoController = new DepartamentoController();
//                    departamentoController.delete(departamento.getClave());
//                    init();
//                } catch (Exception ex) {
//                    Logger.getLogger(JPanelPerfil.class.getName()).log(Level.SEVERE, null, ex);
//                    ErrorLog err= new ErrorLog(parent, ex);
//                }
//            }
//            
//        }else{
//            JOptionPane.showMessageDialog(parent, "No se puede realizar la accion", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
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
        jMenuItemEditar = new javax.swing.JMenuItem();
        jMenuItemEliminar = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemNuevo = new javax.swing.JMenuItem();
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
        jButtonEditar = new javax.swing.JButton();
        jButtonNuevo = new javax.swing.JButton();
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

        jMenuItemEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/lapiz.png"))); // NOI18N
        jMenuItemEditar.setText("Editar");
        jMenuItemEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditarActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemEditar);

        jMenuItemEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/basura.png"))); // NOI18N
        jMenuItemEliminar.setText("Eliminar");
        jMenuItemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEliminarActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemEliminar);
        jPopupMenu.add(jSeparator2);

        jMenuItemNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/16/agregar-archivo.png"))); // NOI18N
        jMenuItemNuevo.setText("Nuevo");
        jMenuItemNuevo.setToolTipText("");
        jMenuItemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNuevoActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemNuevo);

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
        jComboBoxBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "A. Paterno", "A. Materno", "CURP", "Puesto", "Plantel", "Clave" }));

        jTextFieldSearch.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        jButtonEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/lapiz.png"))); // NOI18N
        jButtonEditar.setText("Editar");
        jButtonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarActionPerformed(evt);
            }
        });

        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconos/24/agregar-archivo.png"))); // NOI18N
        jButtonNuevo.setText("Nuevo");
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonRefrescar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonVer, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonRefrescar)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonNuevo))
                    .addComponent(jComboBox1)
                    .addComponent(jComboBoxBy)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
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

    private void jMenuItemVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVerActionPerformed
        ver();
    }//GEN-LAST:event_jMenuItemVerActionPerformed

    private void jMenuItemEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_jMenuItemEliminarActionPerformed

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

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        JDialogTrabajadorNuevo jdtn = new JDialogTrabajadorNuevo(parent);
        init();
    }//GEN-LAST:event_jButtonNuevoActionPerformed

    private void jButtonEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarActionPerformed
        editar();
    }//GEN-LAST:event_jButtonEditarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jMenuItemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNuevoActionPerformed
        JDialogTrabajadorNuevo jdtn = new JDialogTrabajadorNuevo(parent);
        init();
    }//GEN-LAST:event_jMenuItemNuevoActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        editar();
    }//GEN-LAST:event_jMenuItemEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonEditar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonVer;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxBy;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemEliminar;
    private javax.swing.JMenuItem jMenuItemNuevo;
    private javax.swing.JMenuItem jMenuItemRefrescar;
    private javax.swing.JMenuItem jMenuItemVer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelPaginacion;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
