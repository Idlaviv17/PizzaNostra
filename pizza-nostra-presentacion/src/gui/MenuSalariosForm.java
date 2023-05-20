package gui;

import control.IControl;
import entidades.Salario;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class MenuSalariosForm extends javax.swing.JFrame {

    private final IControl control;
    private Salario salario;
    private final MenuPrincipalForm menuPrincipal;

    public MenuSalariosForm(IControl control, MenuPrincipalForm menuPrincipal) {
        initComponents();
        this.setExtendedState(MenuSalariosForm.MAXIMIZED_BOTH);
        this.control = control;
        this.menuPrincipal = menuPrincipal;

        // Llenado de la tabla
        actualizarModeloTabla();
        btnCancelar.setVisible(false);
        try {
            llenarTabla();
            tblSalarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            activarListeners();
        } catch (Exception e) {
            mostrarErrorBD();
        }
    }

    private void guardar() {
        if (txtID.getText().isBlank()) {
            agregar();
        } else {
            actualizar();
        }
    }

    private void agregar() {
        if (validarCampos()) {
            salario = new Salario();
            obtenerSalarioFormulario();
            boolean seAgregoSalario = control.agregarSalario(salario);
            if (seAgregoSalario) {
                JOptionPane.showMessageDialog(this, "Se ha registrado el salario", "Información", JOptionPane.INFORMATION_MESSAGE);
                llenarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible registrar el salario", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizar() {
        if (validarCampos()) {
            obtenerSalarioFormulario();
            boolean seActualizoSalario = control.actualizarSalario(salario);
            if (seActualizoSalario) {
                JOptionPane.showMessageDialog(this, "Se ha actualizado el salario", "Información", JOptionPane.INFORMATION_MESSAGE);
                llenarTabla();
                limpiarFormulario();
                lblOperacion.setText("Agregar Salario");
                btnGuardar.setText("Agregar");
                btnCancelar.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible actualizar el salario", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void seleccionarSalario() {
        Long idSalarioSeleccionado = getIdSalarioSeleccionado();
        if (idSalarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un salario", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Salario salarioSeleccionado = control.consultarSalario(idSalarioSeleccionado);
        if (salarioSeleccionado != null) {
            lblOperacion.setText("Actualizar Salario");
            btnGuardar.setText("Actualizar");
            salario = control.consultarSalario(idSalarioSeleccionado);
            txtID.setText(String.valueOf(salario.getId()));
            txtRol.setText(salario.getRol());
            txtCosteHora.setText(String.valueOf(salario.getCostePorHora()));
            btnCancelar.setVisible(true);
        }
    }

    private void eliminar() {
        Long idSalarioSeleccionado = getIdSalarioSeleccionado();
        if (idSalarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un salario", "Información", JOptionPane.WARNING_MESSAGE);
        } else {
            int opcionSeleccionada = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el salario?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcionSeleccionada == JOptionPane.YES_OPTION) {
                Salario salarioEliminado = control.consultarSalario(idSalarioSeleccionado);

                boolean seCancelaSalario = control.eliminarSalario(salarioEliminado);
                if (seCancelaSalario) {
                    JOptionPane.showMessageDialog(this, "Se eliminó el salario correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cancelar el salario", "Información", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private Long getIdSalarioSeleccionado() {
        int indiceFilaSeleccionada = tblSalarios.convertRowIndexToModel(tblSalarios.getSelectedRow());
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) tblSalarios.getModel();
            int indiceColumnaId = 0;
            Long idSalarioSeleccionado = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
            return idSalarioSeleccionado;
        } else {
            return null;
        }
    }

    protected final void llenarTabla() {
        // Clear the table model
        DefaultTableModel modeloTabla = (DefaultTableModel) tblSalarios.getModel();
        modeloTabla.setRowCount(0);

        for (Salario salarioTabla : control.consultarSalarios()) {
            modeloTabla.addRow(new Object[]{
                salarioTabla.getId(),
                salarioTabla.getRol(),
                salarioTabla.getCostePorHora()
            });
        }

        //Actualizar lista de acuerdo a busqueda
        if (txtBusqueda.getText().trim().length() != 0) {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabla);
            RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + txtBusqueda.getText(), 1, Pattern.CASE_INSENSITIVE);
            sorter.setRowFilter(filter);
            tblSalarios.setRowSorter(sorter);
        } else {
            tblSalarios.setRowSorter(null);
        }

        //Actualizar vista de tabla
        tblSalarios.repaint();
    }

    private void activarListeners() {
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

    private void obtenerSalarioFormulario() {
        salario.setRol(txtRol.getText());
        salario.setCostePorHora(Float.valueOf(txtCosteHora.getText()));
    }

    private void actualizarModeloTabla() {
        tblSalarios.setDefaultEditor(Object.class, null);

    }

    private boolean validarCampos() {
        if (this.txtRol.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un rol", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtRol.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "El rol no puede superar los 100 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtCosteHora.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un coste", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Float.valueOf(this.txtCosteHora.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El campo Coste Por Hora debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtID.setText("");
        txtRol.setText("");
        txtCosteHora.setText("");
        btnCancelar.setVisible(false);
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

        lblOperacion = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        btnBusqueda = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        lblSalarioID = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lblRol = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        txtRol = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSalarios = new javax.swing.JTable();
        lblCosteHora = new javax.swing.JLabel();
        txtCosteHora = new javax.swing.JTextField();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnMenuPrincipal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Salarios");
        setResizable(false);

        lblOperacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblOperacion.setText("Agregar Salario");

        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setFocusable(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/x.png"))); // NOI18N
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setText("Tabulador");

        lblSalarioID.setText("ID Salario");

        txtID.setEditable(false);

        lblRol.setText("Rol");

        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setText("Agregar");
        btnGuardar.setFocusable(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        tblSalarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Rol", "Coste por hora"
            }
        ));
        tblSalarios.setFocusable(false);
        jScrollPane1.setViewportView(tblSalarios);

        lblCosteHora.setText("Coste p/ hora");

        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusable(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnMenuPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMenuPrincipal.setText("Menu Principal");
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(btnActualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(lblOperacion))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRol, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblSalarioID, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(lblCosteHora))
                            .addGap(22, 22, 22)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCosteHora, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                .addComponent(txtID)
                                .addComponent(txtRol)))
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnBusqueda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOperacion)
                .addGap(166, 166, 166)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, Short.MAX_VALUE)
                .addComponent(btnMenuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(lblTitulo)
                    .addGap(16, 16, 16)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(49, 49, 49)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSalarioID)
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblRol)
                                .addComponent(txtRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblCosteHora)
                                .addComponent(txtCosteHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(8, 8, 8)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
        llenarTabla();
        txtBusqueda.setText("");
    }//GEN-LAST:event_btnBusquedaActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        seleccionarSalario();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuPrincipalActionPerformed
        ocultarMenu();
        menuPrincipal.mostrarMenu();
    }//GEN-LAST:event_btnMenuPrincipalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMenuPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCosteHora;
    private javax.swing.JLabel lblOperacion;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblSalarioID;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblSalarios;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCosteHora;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtRol;
    // End of variables declaration//GEN-END:variables
}
