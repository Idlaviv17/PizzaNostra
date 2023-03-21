package gui;

import control.IControl;
import entidades.Empleado;
import entidades.Pago;
import entidades.Salario;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import renderers.EmpleadoRenderer;
import renderers.SalarioRenderer;

public class PagosForm extends javax.swing.JFrame {

    private final IControl control;

    public PagosForm(IControl control) {
        initComponents();
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.control = control;
        
        // Llenado de la tabla
        this.llenarTabla();
        
        // Llenado de ComboBoxes
        this.llenarEmpleados();
        this.llenarSalarios();
    }

    private void guardar() {
        if (this.txtID.getText().isEmpty()) {
            this.agregar();
        } else {
            this.actualizar();
        }
    }

    private void agregar() {
        Empleado empleado = (Empleado) this.cbxEmpleado.getSelectedItem();
        Salario salario = (Salario) this.cbxSalario.getSelectedItem();
        LocalDate inicioPeriodo = this.dpInicioPeriodo.getDate();
        LocalDate finPeriodo = this.dpFinPeriodo.getDate();
        LocalDate fecha = this.dpFecha.getDate();
        String estado = (String) this.cbxEstado.getSelectedItem();
        String comentario = this.txtComentario.getText();
        boolean seAgregoPago = this.control.agregarPago(new Pago(empleado , salario, inicioPeriodo, finPeriodo, fecha, estado, comentario, null));
        if (seAgregoPago) {
            JOptionPane.showMessageDialog(this, "Se ha creado el pago", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.limpiarFormulario();
            this.llenarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible crear el pago", "Información", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        Long idPago = Long.parseLong(this.txtID.getText());
        Empleado empleado = (Empleado) this.cbxEmpleado.getSelectedItem();
        Salario salario = (Salario) this.cbxSalario.getSelectedItem();
        LocalDate inicioPeriodo = this.dpInicioPeriodo.getDate();
        LocalDate finPeriodo = this.dpFinPeriodo.getDate();
        LocalDate fecha = this.dpFecha.getDate();
        String estado = (String) this.cbxEstado.getSelectedItem();
        String comentario = this.txtComentario.getText();
        boolean seActualizoPago = this.control.actualizarPago(new Pago(idPago, empleado , salario, inicioPeriodo, finPeriodo, fecha, estado, comentario, null));
        if (seActualizoPago) {
            JOptionPane.showMessageDialog(this, "Se actualizó el pago", "Información", JOptionPane.INFORMATION_MESSAGE);
            this.limpiarFormulario();
            this.llenarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el pago", "Información", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void editar() {
        Long idPagoSeleccionado = this.getIdPagoSeleccionado();
        if (idPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago", "Información", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pago pago = this.control.consultarPago(idPagoSeleccionado);
        if (pago != null) {
            this.llenarFormulario(pago);
        }
    }

    private void cancelar() {
        Long idPagoSeleccionado = this.getIdPagoSeleccionado();
        if (idPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago", "Información", JOptionPane.WARNING_MESSAGE);
        } else {
            int opcionSeleccionada = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar el pago?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcionSeleccionada == JOptionPane.NO_OPTION) {
                return;
            }

            Pago pagoCancelado = this.control.consultarPago(idPagoSeleccionado);
            pagoCancelado.setEstado("CANCELADO");
            boolean seCancelaPago = this.control.actualizarPago(pagoCancelado);
            if (seCancelaPago) {
                JOptionPane.showMessageDialog(this, "Se canceló el pago correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                this.llenarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cancelar el pago", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void llenarFormulario(Pago pago) {
        this.txtID.setText(pago.getId().toString());
        this.cbxEmpleado.setSelectedItem(pago.getEmpleado());
        this.cbxSalario.setSelectedItem(pago.getSalario());
        this.dpInicioPeriodo.setDate(pago.getInicioPeriodo());
        this.dpFinPeriodo.setDate(pago.getFinPeriodo());
        this.dpFecha.setDate(pago.getFecha());
        this.cbxEstado.setSelectedItem(pago.getEstado());
    }

    private Long getIdPagoSeleccionado() {
        int indiceFilaSeleccionada = this.tblPagos.getSelectedRow();
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) this.tblPagos.getModel();
            int indiceColumnaId = 0;
            Long idPagoSeleccionado = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
            return idPagoSeleccionado;
        } else {
            return null;
        }
    }

    private void llenarEmpleados() {
        this.cbxEmpleado.setRenderer(new EmpleadoRenderer());
        List<Empleado> listaEmpleados = this.control.consultarEmpleados();
        listaEmpleados.forEach(empleado -> {
            this.cbxEmpleado.addItem(empleado);
        });
    }

    private void llenarSalarios() {
        this.cbxSalario.setRenderer(new SalarioRenderer());
        List<Salario> listaSalarios = this.control.consultarSalarios();
        listaSalarios.forEach(salario -> {
            this.cbxSalario.addItem(salario);
        });
    }

    private void llenarTabla() {
        List<Pago> listaPagos = this.control.consultarPagos();
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblPagos.getModel();
        modeloTabla.setRowCount(0);
        listaPagos.forEach(pago -> {
            Object[] fila = new Object[9];
            fila[0] = pago.getId();
            fila[1] = pago.getEmpleado();
            fila[2] = pago.getSalario();
            fila[3] = pago.getInicioPeriodo();
            fila[4] = pago.getFinPeriodo();
            fila[5] = pago.getFecha();
            fila[6] = pago.getEstado();
            fila[7] = pago.getHorasTrabajadas();
            fila[8] = pago.getComentario();
            modeloTabla.addRow(fila);
        });
    }

    private void limpiarFormulario() {
        this.txtID.setText("");
        this.cbxEmpleado.setSelectedIndex(0);
        this.cbxSalario.setSelectedIndex(0);
        this.dpInicioPeriodo.setText("");
        this.dpFinPeriodo.setText("");
        this.dpFecha.setText("");
        this.cbxEstado.setSelectedIndex(0);
        this.txtHorasTrabajadas.setText("");
        this.txtComentario.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLimpiar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtSalario = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblFinPeriodo = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblPagoID = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        lblInicioPeriodo = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lblSalario = new javax.swing.JLabel();
        lblHorasTrabajadas = new javax.swing.JLabel();
        txtHorasTrabajadas = new javax.swing.JTextField();
        lblComentario = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtComentario = new javax.swing.JTextArea();
        cbxEmpleado = new javax.swing.JComboBox();
        cbxSalario = new javax.swing.JComboBox();
        cbxEstado = new javax.swing.JComboBox<>();
        dpInicioPeriodo = new com.github.lgooddatepicker.components.DatePicker();
        dpFinPeriodo = new com.github.lgooddatepicker.components.DatePicker();
        dpFecha = new com.github.lgooddatepicker.components.DatePicker();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pagos");
        setResizable(false);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.setFocusable(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/buscar.png"))); // NOI18N

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblFinPeriodo.setText("Fin Periodo");

        lblFecha.setText("Fecha");

        lblEstado.setText("Estado");

        lblPagoID.setText("ID Pago");

        lblEmpleado.setText("Empleado");
        lblEmpleado.setToolTipText("");

        lblInicioPeriodo.setText("Inicio Periodo");

        txtID.setEditable(false);
        txtID.setEnabled(false);

        lblSalario.setText("Salario");

        lblHorasTrabajadas.setText("Horas Trabajadas");

        txtHorasTrabajadas.setEditable(false);
        txtHorasTrabajadas.setEnabled(false);

        lblComentario.setText("Comentario");

        txtComentario.setColumns(20);
        txtComentario.setRows(5);
        jScrollPane2.setViewportView(txtComentario);

        cbxEmpleado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Seleccionar --" }));

        cbxSalario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Seleccionar --" }));

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccionar --", "PAGADO ", "CANCELADO", "PENDIENTE" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblComentario)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblHorasTrabajadas)
                                    .addComponent(lblEstado)
                                    .addComponent(lblFecha)
                                    .addComponent(lblFinPeriodo)
                                    .addComponent(lblInicioPeriodo)
                                    .addComponent(lblSalario)
                                    .addComponent(lblEmpleado)
                                    .addComponent(lblPagoID))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtHorasTrabajadas, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbxEstado, javax.swing.GroupLayout.Alignment.LEADING, 0, 313, Short.MAX_VALUE)
                                            .addComponent(dpFecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtID)
                                            .addComponent(cbxEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbxSalario, 0, 312, Short.MAX_VALUE)
                                            .addComponent(dpInicioPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(dpFinPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addGap(31, 31, 31))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPagoID)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpleado)
                    .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSalario)
                    .addComponent(cbxSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInicioPeriodo)
                    .addComponent(dpInicioPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFinPeriodo)
                    .addComponent(dpFinPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha)
                    .addComponent(dpFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHorasTrabajadas)
                    .addComponent(txtHorasTrabajadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblComentario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 65, Short.MAX_VALUE)
                        .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSalario, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        this.limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        this.guardar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        this.guardar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        this.editar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox cbxEmpleado;
    private javax.swing.JComboBox<String> cbxEstado;
    private javax.swing.JComboBox cbxSalario;
    private com.github.lgooddatepicker.components.DatePicker dpFecha;
    private com.github.lgooddatepicker.components.DatePicker dpFinPeriodo;
    private com.github.lgooddatepicker.components.DatePicker dpInicioPeriodo;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblComentario;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFinPeriodo;
    private javax.swing.JLabel lblHorasTrabajadas;
    private javax.swing.JLabel lblInicioPeriodo;
    private javax.swing.JLabel lblPagoID;
    private javax.swing.JLabel lblSalario;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextArea txtComentario;
    private javax.swing.JTextField txtHorasTrabajadas;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables
}
