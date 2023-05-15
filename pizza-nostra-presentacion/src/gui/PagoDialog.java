package gui;

import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import control.IControl;
import entidades.DiaTrabajado;
import entidades.Empleado;
import entidades.Pago;
import entidades.Salario;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import renderers.EmpleadoRenderer;
import renderers.SalarioRenderer;

public class PagoDialog extends javax.swing.JDialog {

    private final IControl control;
    private final MenuPagosForm menuPagosForm;
    private final LocalDate fechaHoy;
    private Pago pago;
    private final Empleado empleado;
    private final Integer operacion;
    private static Salario ultimoSalario;
    private static LocalDate ultimaFechaInicioPeriodo;
    private static LocalDate ultimaFechaFinPeriodo;
    private static String ultimoEstado;
    private final int AGREGAR = 0;
    private final int ACTUALIZAR = 1;
    private final int CONSULTAR = 2;
    private final int AGREGAR_ACTIVOS = 3;

    public PagoDialog(IControl control, MenuPagosForm menuPagosForm, Pago pago, Empleado empleado, Integer operacion) {
        super(menuPagosForm);
        initComponents();
        this.control = control;
        this.menuPagosForm = menuPagosForm;
        this.fechaHoy = LocalDate.now();
        this.pago = pago;
        this.empleado = empleado;
        this.operacion = operacion;

        activarListeners();
        llenarEmpleados();
        llenarSalarios();
        actualizarOpcionesCampos();
        cargarDatosPago();
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setVisible(true);
    }

    private void guardar() {
        switch (operacion) {
            case AGREGAR, AGREGAR_ACTIVOS ->
                this.agregar();
            case ACTUALIZAR ->
                this.actualizar();
            case CONSULTAR ->
                this.dispose();
        }
    }

    private void agregar() {
        System.out.println("agregar");
        if (validarCampos()) {
            pago = new Pago();
            obtenerPagoFormulario();
            boolean seAgregoPago = control.agregarPago(pago);
            if (seAgregoPago) {
                JOptionPane.showMessageDialog(this, "Se ha registrado el pago", "Información", JOptionPane.INFORMATION_MESSAGE);
                if (ultimoSalario == null) {
                    ultimoSalario = pago.getSalario();
                    ultimaFechaInicioPeriodo = pago.getInicioPeriodo();
                    ultimaFechaFinPeriodo = pago.getFinPeriodo();
                    ultimoEstado = pago.getEstado();
                }
                cerrarVentana();
                menuPagosForm.llenarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible registrar el pago", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizar() {
        if (validarCampos()) {
            obtenerPagoFormulario();
            boolean seActualizoPago = control.actualizarPago(pago);
            if (seActualizoPago) {
                JOptionPane.showMessageDialog(this, "Se actualizó el pago", "Información", JOptionPane.INFORMATION_MESSAGE);
                cerrarVentana();
                menuPagosForm.llenarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible actualizar el pago", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosPago() {

        switch (operacion) {
            case AGREGAR ->
                limpiarFormulario();
            case ACTUALIZAR, CONSULTAR -> {
                txtID.setText(String.valueOf(pago.getId()));
                cbxEmpleado.setSelectedItem(pago.getEmpleado());
                cbxSalario.setSelectedItem(pago.getSalario());
                dpInicioPeriodo.setDate(pago.getInicioPeriodo());
                dpFinPeriodo.setDate(pago.getFinPeriodo());
                dpFecha.setDate(operacion == ACTUALIZAR ? fechaHoy : pago.getFecha());
                cbxEstado.setSelectedItem(pago.getEstado());
                txtHorasTrabajadas.setText(String.valueOf(pago.getHorasTrabajadas()));
                txtComentario.setText(pago.getComentario());
            }
            case AGREGAR_ACTIVOS -> {
                cbxEmpleado.setSelectedItem(empleado);
                cbxEmpleado.setEnabled(false);
                dpFecha.setDate(this.fechaHoy);
                if (ultimoSalario != null) {
                    cbxSalario.setSelectedItem(ultimoSalario);
                    dpInicioPeriodo.setDate(ultimaFechaInicioPeriodo);
                    dpFinPeriodo.setDate(ultimaFechaFinPeriodo);
                    cbxEstado.setSelectedItem(ultimoEstado);
                }
            }
        }
    }

    private void obtenerPagoFormulario() {
        pago.setEmpleado((Empleado) cbxEmpleado.getSelectedItem());
        pago.setSalario((Salario) cbxSalario.getSelectedItem());
        pago.setInicioPeriodo(dpInicioPeriodo.getDate());
        pago.setFinPeriodo(dpFinPeriodo.getDate());
        pago.setFecha(dpFecha.getDate());
        pago.setEstado((String) cbxEstado.getSelectedItem());
        pago.setHorasTrabajadas(Integer.valueOf(txtHorasTrabajadas.getText()));
        pago.setComentario(txtComentario.getText());
    }

    private void actualizarOpcionesCampos() {
        //Agregar "CANCELADO" al modelo
        String[] options = {"-- Seleccionar --", "PAGADO", "PENDIENTE", "CANCELADO"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(options);

        switch (operacion) {
            case AGREGAR, AGREGAR_ACTIVOS -> {
                btnGuardar.setText("Agregar");
                this.setTitle("Agregar pago");
            }
            case ACTUALIZAR -> {
                btnGuardar.setText("Actualizar");
                txtID.setEditable(false);
                cbxEstado.setModel(model);
                this.setTitle("Actualizar pago");
            }
            case CONSULTAR -> {
                btnGuardar.setText("Aceptar");
                btnLimpiar.setEnabled(false);
                btnCancelar.setEnabled(false);
                cbxEstado.setModel(model);
                desactivarCampos();
                this.setTitle("Consultar pago");
            }
        }
    }

    private void desactivarCampos() {
        JTextComponent[] componentsToDisable = {txtID, txtHorasTrabajadas, txtComentario};
        for (JTextComponent component : componentsToDisable) {
            component.setEditable(false);
        }
        for (Component component : new Component[]{cbxEmpleado, cbxSalario, dpInicioPeriodo, dpFinPeriodo, dpFecha, cbxEstado}) {
            component.setEnabled(false);
        }
    }

    private void llenarEmpleados() {
        this.cbxEmpleado.setRenderer(new EmpleadoRenderer());
        List<Empleado> listaEmpleados = control.consultarEmpleadosPorEstado(false);
        listaEmpleados.forEach(empleadoLista -> {
            this.cbxEmpleado.addItem(empleadoLista);
        });
    }

    private void llenarSalarios() {
        this.cbxSalario.setRenderer(new SalarioRenderer());
        List<Salario> listaSalarios = control.consultarSalarios();
        listaSalarios.forEach(salario -> {
            this.cbxSalario.addItem(salario);
        });
    }

    private boolean validarCampos() {
        if (this.cbxEmpleado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Se debe de seleccionar un empleado para poder realizar el pago", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.cbxSalario.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Se debe de seleccionar un salario para poder realizar el pago", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.dpInicioPeriodo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe de seleccionar el inicio del periodo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.dpFinPeriodo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe de seleccionar el final del periodo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.cbxEstado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Se debe de seleccionar el estado del pago", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.dpInicioPeriodo.getDate().isAfter(this.dpFinPeriodo.getDate())) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio no puede sobrepasar la fecha final", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void activarListeners() {
        cbxEmpleado.addActionListener((ActionEvent e) -> {
            // Perform action when combo box item is selected
            actualizarHorasTrabajadas();
        });

        // This method will be called whenever the date is changed
        dpInicioPeriodo.addDateChangeListener((DateChangeEvent event) -> {
            actualizarHorasTrabajadas();
        });
        dpFinPeriodo.addDateChangeListener((DateChangeEvent event) -> {
            actualizarHorasTrabajadas();
        });
    }

    private void actualizarHorasTrabajadas() {
        if (cbxEmpleado.getSelectedIndex() != 0) {
            if (dpInicioPeriodo.getDate() != null && dpFinPeriodo.getDate() != null) {
                txtHorasTrabajadas.setText(String.valueOf(calcularHorasTrabajadas()));
            }
        }
    }

    private int calcularHorasTrabajadas() {
        Empleado empleadoSeleccionado = (Empleado) cbxEmpleado.getSelectedItem();
        List<DiaTrabajado> dias = control.consultarDiasTrabajadosPorEmpleado(empleadoSeleccionado);

        int horasTrabajadas = 0;

        for (int i = 0; i < dias.size(); i++) {
            DiaTrabajado dia = dias.get(i);
            if (isInPeriod(dia.getFecha(), dpInicioPeriodo.getDate(), dpFinPeriodo.getDate())) {
                horasTrabajadas += (dias.get(i).getHoraSalida().getHour() - dias.get(i).getHoraEntrada().getHour());
            }
        }
        return horasTrabajadas;

        //Mejor
//            return dias.stream()
//               .filter(dia -> isInPeriod(dia.getFecha(), dpInicioPeriodo.getDate(), dpFinPeriodo.getDate()))
//               .mapToInt(dia -> dia.getHoraSalida().getHour() - dia.getHoraEntrada().getHour())
//               .sum();
    }

    private boolean isInPeriod(LocalDate fecha, LocalDate fechaInicio, LocalDate fechaFin) {
        return (fecha.isEqual(fechaInicio)
                || fecha.isEqual(fechaFin))
                || (fecha.isAfter(fechaInicio) && fecha.isBefore(fechaFin));
    }

    private void limpiarFormulario() {
        cbxEmpleado.setSelectedIndex(0);
        cbxSalario.setSelectedIndex(0);
        dpInicioPeriodo.setText("");
        dpFinPeriodo.setText("");
        dpFecha.setDate(this.fechaHoy);
        cbxEstado.setSelectedIndex(0);
        txtHorasTrabajadas.setText("");
        txtComentario.setText("");
    }

    private void cerrarVentana() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txtComentario = new javax.swing.JTextArea();
        txtID = new javax.swing.JTextField();
        dpFinPeriodo = new com.github.lgooddatepicker.components.DatePicker();
        lblFecha = new javax.swing.JLabel();
        lblSalario = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        dpFecha = new com.github.lgooddatepicker.components.DatePicker();
        cbxEmpleado = new javax.swing.JComboBox();
        lblHorasTrabajadas = new javax.swing.JLabel();
        lblPagoID = new javax.swing.JLabel();
        txtHorasTrabajadas = new javax.swing.JTextField();
        cbxSalario = new javax.swing.JComboBox();
        btnGuardar = new javax.swing.JButton();
        lblEmpleado = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        cbxEstado = new javax.swing.JComboBox<>();
        btnCancelar = new javax.swing.JButton();
        lblComentario = new javax.swing.JLabel();
        lblInicioPeriodo = new javax.swing.JLabel();
        lblFinPeriodo = new javax.swing.JLabel();
        dpInicioPeriodo = new com.github.lgooddatepicker.components.DatePicker();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        txtComentario.setColumns(20);
        txtComentario.setRows(5);
        jScrollPane2.setViewportView(txtComentario);

        txtID.setEditable(false);

        lblFecha.setText("Fecha");

        lblSalario.setText("Salario");

        lblEstado.setText("Estado");

        dpFecha.setEnabled(false);

        cbxEmpleado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Seleccionar --" }));

        lblHorasTrabajadas.setText("Horas Trabajadas");

        lblPagoID.setText("ID Pago");

        txtHorasTrabajadas.setEditable(false);

        cbxSalario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Seleccionar --" }));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        lblEmpleado.setText("Empleado");
        lblEmpleado.setToolTipText("");

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccionar --", "PAGADO", "PENDIENTE" }));

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblComentario.setText("Comentario");

        lblInicioPeriodo.setText("Inicio Periodo");

        lblFinPeriodo.setText("Fin Periodo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblComentario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblHorasTrabajadas)
                                        .addComponent(lblEstado)
                                        .addComponent(lblFecha)
                                        .addComponent(lblFinPeriodo)
                                        .addComponent(lblInicioPeriodo)
                                        .addComponent(lblSalario)
                                        .addComponent(lblEmpleado)
                                        .addComponent(lblPagoID))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(13, 13, 13)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtHorasTrabajadas, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cbxEstado, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(dpFecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGap(14, 14, 14)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtID)
                                                .addComponent(cbxEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cbxSalario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(dpInicioPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(dpFinPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(16, 16, 16))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPagoID)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpleado)
                    .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSalario)
                    .addComponent(cbxSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInicioPeriodo)
                    .addComponent(dpInicioPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFinPeriodo)
                    .addComponent(dpFinPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha)
                    .addComponent(dpFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHorasTrabajadas)
                    .addComponent(txtHorasTrabajadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblComentario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnCancelar))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox cbxEmpleado;
    private javax.swing.JComboBox<String> cbxEstado;
    private javax.swing.JComboBox cbxSalario;
    private com.github.lgooddatepicker.components.DatePicker dpFecha;
    private com.github.lgooddatepicker.components.DatePicker dpFinPeriodo;
    private com.github.lgooddatepicker.components.DatePicker dpInicioPeriodo;
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
    private javax.swing.JTextArea txtComentario;
    private javax.swing.JTextField txtHorasTrabajadas;
    private javax.swing.JTextField txtID;
    // End of variables declaration//GEN-END:variables
}
