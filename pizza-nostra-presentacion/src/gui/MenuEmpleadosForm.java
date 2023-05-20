package gui;

import control.IControl;
import entidades.Empleado;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class MenuEmpleadosForm extends javax.swing.JFrame {

    private final IControl control;
    private final MenuPrincipalForm menuPrincipal;

    public MenuEmpleadosForm(IControl control, MenuPrincipalForm menuPrincipal) {
        initComponents();
        this.setExtendedState(MenuEmpleadosForm.MAXIMIZED_BOTH);
        this.control = control;
        this.menuPrincipal = menuPrincipal;

        // Llenado de la tabla
        actualizarModeloTabla();
        try {
            llenarTabla();
            tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // Llenado de ComboBoxes
            activarListeners();
        } catch (Exception e) {
            mostrarErrorBD();
        }
    }

    private void agregar() {
        EmpleadoDialog empleadoDialog = new EmpleadoDialog(control, this, null, 0);
    }

    private void actualizar() {
        Long idEmpleadoSeleccionado = getIdEmpleadoSeleccionado();
        if (idEmpleadoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Empleado empleadoSeleccionado = this.control.consultarEmpleado(idEmpleadoSeleccionado);
        if (empleadoSeleccionado != null) {
            EmpleadoDialog empleadoDialog = new EmpleadoDialog(control, this, empleadoSeleccionado, 1);
        }
    }

    private void consultar() {
        Empleado empleadoSeleccionado = verificarEmpleadoSeleccionado();
        if (empleadoSeleccionado != null) {
            EmpleadoDialog empleadoDialog = new EmpleadoDialog(control, this, empleadoSeleccionado, 2);
        }
    }

    private void desactivar() {
        Long idEmpleadoSeleccionado = getIdEmpleadoSeleccionado();
        if (idEmpleadoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado", "Información", JOptionPane.WARNING_MESSAGE);
        } else {
            int opcionSeleccionada = JOptionPane.showConfirmDialog(this, "¿Está seguro de desactivar el empleado?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcionSeleccionada == JOptionPane.YES_OPTION) {
                Empleado empleadoDesactivado = control.consultarEmpleado(idEmpleadoSeleccionado);
                empleadoDesactivado.setEstado(true);
                boolean seDesactivoEmpleado = control.actualizarEmpleado(empleadoDesactivado);
                if (seDesactivoEmpleado) {
                    JOptionPane.showMessageDialog(this, "Se desactivó el empleado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo desactivar el empleado", "Información", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private Long getIdEmpleadoSeleccionado() {
        int indiceFilaSeleccionada = tblEmpleados.convertRowIndexToModel(tblEmpleados.getSelectedRow());
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) tblEmpleados.getModel();
            int indiceColumnaId = 0;
            Long idEmpleadoSeleccionado = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
            return idEmpleadoSeleccionado;
        } else {
            return null;
        }
    }

protected final void llenarTabla() {
    // Clear the table model
    DefaultTableModel modeloTabla = (DefaultTableModel) tblEmpleados.getModel();
    modeloTabla.setRowCount(0);

    List<Empleado> empleadosActivos = new ArrayList<>();
    List<Empleado> empleadosInactivos = new ArrayList<>();

    // Loop over the selected statuses and retrieve the employees with each status
    for (Empleado empleado : control.consultarEmpleados()) {
        boolean isActivo = !empleado.getEstado();
        boolean isInactivo = !isActivo;
        boolean activosInactivosSeleccionados = cbActivos.isSelected() && cbInactivos.isSelected();
        boolean soloActivosSeleccionados = cbActivos.isSelected() && isActivo;
        boolean soloInactivosSeleccionados = cbInactivos.isSelected() && isInactivo;

        if (activosInactivosSeleccionados || soloActivosSeleccionados || soloInactivosSeleccionados) {
            if (isActivo) {
                empleadosActivos.add(empleado);
            } else {
                empleadosInactivos.add(empleado);
            }
        }
    }

    // Agregar empleados activos al modelo de tabla
    for (Empleado empleado : empleadosActivos) {
        modeloTabla.addRow(new Object[]{
            empleado.getId(),
            empleado.getNombre(),
            empleado.getApellidos(),
            empleado.getTelefono(),
            empleado.getCorreo(),
            empleado.getDomicilio(),
            empleado.getFechaNacimiento(),
            empleado.getRfc(),
            "Activo"
        });
    }

    // Agregar empleados inactivos al modelo de tabla
    for (Empleado empleado : empleadosInactivos) {
        modeloTabla.addRow(new Object[]{
            empleado.getId(),
            empleado.getNombre(),
            empleado.getApellidos(),
            empleado.getTelefono(),
            empleado.getCorreo(),
            empleado.getDomicilio(),
            empleado.getFechaNacimiento(),
            empleado.getRfc(),
            "Inactivo"
        });
    }

    //Actualizar lista de acuerdo a búsqueda
    if (txtBusqueda.getText().trim().length() != 0) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabla);
        RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + txtBusqueda.getText(), 1, Pattern.CASE_INSENSITIVE);
        sorter.setRowFilter(filter);
        tblEmpleados.setRowSorter(sorter);
    } else {
        tblEmpleados.setRowSorter(null);
    }

    //Actualizar vista de tabla
    tblEmpleados.repaint();
}

    private void activarListeners() {
        //Doble click a fila de tabla
        tblEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    consultar();
                }
            }
        });

        //Check boxes
        ItemListener checkBoxListener = (ItemEvent e) -> {
            llenarTabla();
        };

        cbActivos.addItemListener(checkBoxListener);
        cbInactivos.addItemListener(checkBoxListener);

        //Buscador
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Called when text is inserted into the document
                llenarTabla();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Called when text is removed from the document
                llenarTabla();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                llenarTabla();
            }
        });
    }

    private Empleado verificarEmpleadoSeleccionado() {
        Long idEmpleadoSeleccionado = getIdEmpleadoSeleccionado();
        if (idEmpleadoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado", "Información", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Empleado empleado = this.control.consultarEmpleado(idEmpleadoSeleccionado);
        if (empleado == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el empleado seleccionado", "Información", JOptionPane.WARNING_MESSAGE);
        }
        return empleado;
    }

    private void actualizarModeloTabla() {
        tblEmpleados.setDefaultEditor(Object.class, null);

    }

    private void mostrarErrorBD() {
        JOptionPane.showMessageDialog(this,
                "Ha ocasionado un error de comunicación con las base de datos", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void ocultarMenu() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JButton();
        btnBusqueda = new javax.swing.JButton();
        cbInactivos = new javax.swing.JCheckBox();
        cbActivos = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        btnMenuPrincipal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pagos");
        setResizable(false);

        btnAgregar.setText("Agregar");
        btnAgregar.setFocusable(false);
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellidos", "Telefono", "Correo", "Domicilio", "Fecha Nacimiento", "RFC", "Estado"
            }
        ));
        tblEmpleados.setFocusable(false);
        jScrollPane1.setViewportView(tblEmpleados);

        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusable(false);
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Desactivar");
        btnCancelar.setFocusable(false);
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.setFocusable(false);
        btnConsultar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/x.png"))); // NOI18N
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });

        cbInactivos.setText("Inactivos");

        cbActivos.setSelected(true);
        cbActivos.setText("Activos");

        jLabel1.setText("Empleados");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        btnMenuPrincipal.setText("Menu Principal");
        btnMenuPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuPrincipalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbInactivos)
                    .addComponent(cbActivos)
                    .addComponent(jLabel1)
                    .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConsultar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBusqueda))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cbActivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbInactivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnBusqueda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desactivar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
        llenarTabla();
        txtBusqueda.setText("");
    }//GEN-LAST:event_btnBusquedaActionPerformed

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        ocultarMenu();
        menuPrincipal.mostrarMenu();
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JCheckBox cbActivos;
    private javax.swing.JCheckBox cbInactivos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
