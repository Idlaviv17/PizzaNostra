package gui;

import control.IControl;
import entidades.Empleado;
import entidades.Pago;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class MenuPagosForm extends javax.swing.JFrame {

    private final IControl control;

    public MenuPagosForm(IControl control) {
        initComponents();
        this.setExtendedState(MenuPagosForm.MAXIMIZED_BOTH);
        this.control = control;

        // Llenado de la tabla
        actualizarModeloTabla();
        llenarTabla();

        // Llenado de ComboBoxes
        activarListeners();

    }

    private void agregar() {
        new PagoDialog(control, this, null, null, 0);
    }

    private void agregarTodos() {
        List<Empleado> empleados = control.consultarEmpleadosPorEstado(false);
        showNextPagoDialog(empleados, 0);
    }

    private void showNextPagoDialog(List<Empleado> empleados, int index) {
        if (index >= empleados.size()) {
            llenarTabla();
            return; // stop recursion
        }

        PagoDialog pagoDialog = new PagoDialog(control, this, null, empleados.get(index), 3);
        pagoDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showNextPagoDialog(empleados, index + 1); // show next dialog
            }
        });
        pagoDialog.setVisible(true);
    }

    private void actualizar() {
        Long idPagoSeleccionado = getIdPagoSeleccionado();
        if (idPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pago pago = this.control.consultarPago(idPagoSeleccionado);
        if (pago != null) {
            //new PagoForm(control, this, pago, null, 1);
            new PagoDialog(control, this, pago, null, 1);
        }
    }

    private void consultar() {
        Pago pagoSeleccionado = verificarPagoSeleccionado();
        if (pagoSeleccionado != null) {
            //new PagoForm(control, this, pagoSeleccionado, null, 2);
            new PagoDialog(control, this, pagoSeleccionado, null, 2);
        }
    }

    private void cancelar() {
        Long idPagoSeleccionado = getIdPagoSeleccionado();
        if (idPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago", "Información", JOptionPane.WARNING_MESSAGE);
        } else {
            int opcionSeleccionada = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar el pago?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcionSeleccionada == JOptionPane.YES_OPTION) {
                Pago pagoCancelado = control.consultarPago(idPagoSeleccionado);
                pagoCancelado.setEstado("CANCELADO");
                boolean seCancelaPago = control.actualizarPago(pagoCancelado);
                if (seCancelaPago) {
                    JOptionPane.showMessageDialog(this, "Se canceló el pago correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cancelar el pago", "Información", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private Long getIdPagoSeleccionado() {
        int indiceFilaSeleccionada = tblPagos.getSelectedRow();
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) tblPagos.getModel();
            int indiceColumnaId = 0;
            Long idPagoSeleccionado = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
            return idPagoSeleccionado;
        } else {
            return null;
        }
    }

    protected void llenarTabla() {
        List<String> estadosSeleccionados = new ArrayList<>();
        if (cbPagosPagados.isSelected()) {
            estadosSeleccionados.add("PAGADO");
        }
        if (cbPagosPendientes.isSelected()) {
            estadosSeleccionados.add("PENDIENTE");
        }
        if (cbPagosCancelados.isSelected()) {
            estadosSeleccionados.add("CANCELADO");
        }

        // Clear the table model
        DefaultTableModel modeloTabla = (DefaultTableModel) tblPagos.getModel();
        modeloTabla.setRowCount(0);

        // Loop over the selected statuses and retrieve the payments with each status
        for (String estado : estadosSeleccionados) {
            for (Pago pago : control.consultarPagosPorEstado(estado)) {
                boolean isActivo = !pago.getEmpleado().getEstado();
                boolean isInactivo = !isActivo;
                boolean activosInactivosSeleccionados = cbEmpleadosActivos.isSelected() && cbEmpleadosInactivos.isSelected();
                boolean soloActivosSeleccionados = cbEmpleadosActivos.isSelected() && isActivo;
                boolean soloInactivosSeleccionados = cbEmpleadosInactivos.isSelected() && isInactivo;

                if (activosInactivosSeleccionados || soloActivosSeleccionados || soloInactivosSeleccionados) {
                    modeloTabla.addRow(new Object[]{
                        pago.getId(),
                        pago.getEmpleado().getNombre() + " " + pago.getEmpleado().getApellidos(),
                        pago.getSalario().getRol() + " / $" + pago.getSalario().getCostePorHora(),
                        pago.getInicioPeriodo(),
                        pago.getFinPeriodo(),
                        pago.getFecha(),
                        pago.getEstado(),
                        pago.getHorasTrabajadas(),
                        pago.getComentario()
                    });
                }
            }
        }

        if (txtBusqueda.getText().trim().length() != 0) {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabla);
            RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + txtBusqueda.getText(), 1, Pattern.CASE_INSENSITIVE);
            sorter.setRowFilter(filter);
            tblPagos.setRowSorter(sorter);
        } else {
            tblPagos.setRowSorter(null);
        }

        //Actualizar la tabla correctamente, de acuerdo a BD
        tblPagos.repaint();
    }

    private void activarListeners() {
        tblPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    consultar();
                }
            }
        });

        ItemListener checkBoxListener = (ItemEvent e) -> {
            llenarTabla();
        };
        cbPagosPagados.addItemListener(checkBoxListener);
        cbPagosPendientes.addItemListener(checkBoxListener);
        cbPagosCancelados.addItemListener(checkBoxListener);
        cbEmpleadosActivos.addItemListener(checkBoxListener);
        cbEmpleadosInactivos.addItemListener(checkBoxListener);
    }

    private Pago verificarPagoSeleccionado() {
        Long idPagoSeleccionado = getIdPagoSeleccionado();
        if (idPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago", "Información", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Pago pago = this.control.consultarPago(idPagoSeleccionado);
        if (pago == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el pago seleccionado", "Información", JOptionPane.WARNING_MESSAGE);
        }
        return pago;
    }

    private void actualizarModeloTabla() {
        tblPagos.setDefaultEditor(Object.class, null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JButton();
        btnBusqueda = new javax.swing.JButton();
        cbPagosPagados = new javax.swing.JCheckBox();
        cbPagosPendientes = new javax.swing.JCheckBox();
        cbPagosCancelados = new javax.swing.JCheckBox();
        btnFilaActivos = new javax.swing.JButton();
        cbEmpleadosInactivos = new javax.swing.JCheckBox();
        cbEmpleadosActivos = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pagos");
        setResizable(false);

        btnAgregar.setText("Agregar");
        btnAgregar.setFocusable(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        tblPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Pago", "Empleado", "Salario", "Inicio Periodo", "Fin Periodo", "Fecha", "Estado", "Horas Trabajadas", "Comentario"
            }
        ));
        tblPagos.setFocusable(false);
        jScrollPane1.setViewportView(tblPagos);

        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusable(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.setFocusable(false);
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/buscar.png"))); // NOI18N
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });

        cbPagosPagados.setSelected(true);
        cbPagosPagados.setText("Pagados");

        cbPagosPendientes.setSelected(true);
        cbPagosPendientes.setText("Pendientes");

        cbPagosCancelados.setText("Cancelados");

        btnFilaActivos.setText("Agregar todos");
        btnFilaActivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilaActivosActionPerformed(evt);
            }
        });

        cbEmpleadosInactivos.setText("Inactivos");

        cbEmpleadosActivos.setSelected(true);
        cbEmpleadosActivos.setText("Activos");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Pagos");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Empleados");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbPagosPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilaActivos)
                    .addComponent(cbEmpleadosInactivos)
                    .addComponent(cbEmpleadosActivos)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbPagosPagados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbPagosCancelados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBusqueda))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbPagosPagados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbPagosPendientes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbPagosCancelados)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbEmpleadosActivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbEmpleadosInactivos)
                        .addGap(30, 30, 30)
                        .addComponent(btnFilaActivos)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnBusqueda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)))
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
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnFilaActivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilaActivosActionPerformed
        agregarTodos();
    }//GEN-LAST:event_btnFilaActivosActionPerformed

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
        llenarTabla();
    }//GEN-LAST:event_btnBusquedaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnFilaActivos;
    private javax.swing.JCheckBox cbEmpleadosActivos;
    private javax.swing.JCheckBox cbEmpleadosInactivos;
    private javax.swing.JCheckBox cbPagosCancelados;
    private javax.swing.JCheckBox cbPagosPagados;
    private javax.swing.JCheckBox cbPagosPendientes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
