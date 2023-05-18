package gui;

import control.IControl;
import entidades.Empleado;
import java.awt.Component;
import java.time.LocalDate;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EmpleadoDialog extends javax.swing.JDialog {

    private final IControl control;
    private final MenuEmpleadosForm menuEmpleadosForm;
    private Empleado empleado;
    private final Integer operacion;
    private final int AGREGAR = 0;
    private final int ACTUALIZAR = 1;
    private final int CONSULTAR = 2;

    public EmpleadoDialog(IControl control, MenuEmpleadosForm menuEmpleadosForm, Empleado empleado, Integer operacion) {
        super(menuEmpleadosForm);
        initComponents();
        this.control = control;
        this.menuEmpleadosForm = menuEmpleadosForm;
        this.empleado = empleado;
        this.operacion = operacion;

        actualizarOpcionesCampos();
        cargarDatosEmpleado();
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setVisible(true);
    }

    private void guardar() {
        switch (operacion) {
            case AGREGAR ->
                this.agregar();
            case ACTUALIZAR ->
                this.actualizar();
            case CONSULTAR ->
                this.dispose();
        }
    }

    private void agregar() {
        if (validarCampos()) {
            empleado = new Empleado();
            obtenerEmpleadoFormulario();
            boolean seAgregoEmpleado = control.agregarEmpleado(empleado);
            if (seAgregoEmpleado) {
                JOptionPane.showMessageDialog(this, "Se ha registrado el empleado", "Información", JOptionPane.INFORMATION_MESSAGE);
                cerrarVentana();
                menuEmpleadosForm.llenarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible registrar el empleado", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizar() {
        if (validarCampos()) {
            obtenerEmpleadoFormulario();
            boolean seActualizoEmpleado = control.actualizarEmpleado(empleado);
            if (seActualizoEmpleado) {
                JOptionPane.showMessageDialog(this, "Se actualizó el empleado", "Información", JOptionPane.INFORMATION_MESSAGE);
                cerrarVentana();
                menuEmpleadosForm.llenarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible actualizar el empleado", "Información", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosEmpleado() {
        switch (operacion) {
            case AGREGAR ->
                limpiarFormulario();
            case ACTUALIZAR, CONSULTAR -> {
                txtID.setText(String.valueOf(empleado.getId()));
                txtNombre.setText(empleado.getNombre());
                txtApellidos.setText(empleado.getApellidos());
                txtTelefono.setText(empleado.getTelefono());
                txtCorreo.setText(empleado.getCorreo());
                txtDomicilio.setText(empleado.getDomicilio());
                dpFechaNacimiento.setDate(empleado.getFechaNacimiento());
                txtRFC.setText(empleado.getRfc());
                if(empleado.getEstado()){
                    cBoxEstado.setSelectedIndex(1);
                } else{
                    cBoxEstado.setSelectedIndex(0);
                }
            }
        }
    }

    private void obtenerEmpleadoFormulario() {
        empleado.setNombre(txtNombre.getText());
        empleado.setApellidos(txtApellidos.getText());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setCorreo(txtCorreo.getText());
        empleado.setDomicilio(txtDomicilio.getText());
        empleado.setFechaNacimiento(dpFechaNacimiento.getDate());
        empleado.setRfc(txtRFC.getText());
        empleado.setEstado(cBoxEstado.getSelectedIndex() != 0);
    }

    private void actualizarOpcionesCampos() {
        switch (operacion) {
            case AGREGAR -> {
                btnGuardar.setText("Agregar");
                this.setTitle("Agregar empleado");
            }
            case ACTUALIZAR -> {
                btnGuardar.setText("Actualizar");
                this.setTitle("Actualizar empleado");
            }
            case CONSULTAR -> {
                btnGuardar.setText("Aceptar");
                btnLimpiar.setEnabled(false);
                btnCancelar.setEnabled(false);
                desactivarCampos();
                this.setTitle("Consultar empleado");
            }
        }
    }

    private void desactivarCampos() {
        for (Component component : new Component[]{txtNombre, txtApellidos, txtTelefono, txtCorreo, txtDomicilio, dpFechaNacimiento, txtRFC}) {
            component.setEnabled(false);
        }
    }

    private boolean validarCampos() {
        if (this.txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un nombre", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.txtNombre.getText().length() > 200) {
            JOptionPane.showMessageDialog(this, "El nombre no puede superar los 200 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.txtNombre.getText().matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "El nombre no puede contener números", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtApellidos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir los apellidos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.txtApellidos.getText().length() > 200) {
            JOptionPane.showMessageDialog(this, "Los apellidos no pueden superar los 200 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.txtApellidos.getText().matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Los apellidos no pueden contener números", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un número de teléfono", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!this.txtTelefono.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "El número de teléfono debe tener 10 dígitos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un correo electrónico", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!this.txtCorreo.getText().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico no tiene un formato válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtDomicilio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un domicilio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.txtDomicilio.getText().length() > 300) {
            JOptionPane.showMessageDialog(this, "El domicilio no puede superar los 300 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        LocalDate fechaNacimiento = this.dpFechaNacimiento.getDate();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMinima = fechaActual.minusYears(18);
        if (fechaNacimiento == null || fechaNacimiento.isAfter(fechaMinima)) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento debe ser válida y no menor a 18 años", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.txtRFC.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Se debe introducir un RFC", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!this.txtRFC.getText().matches("[A-Z]{4}\\d{6}[A-Z0-9]{3}")) {
            JOptionPane.showMessageDialog(this, "El RFC no tiene un formato válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDomicilio.setText("");
        dpFechaNacimiento.setText("");
        txtRFC.setText("");
    }

    private void cerrarVentana() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtID = new javax.swing.JTextField();
        lblDomicilio = new javax.swing.JLabel();
        lblApellidos = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblTelefono = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtDomicilio = new javax.swing.JTextField();
        lblFechaNacimiento = new javax.swing.JLabel();
        dpFechaNacimiento = new com.github.lgooddatepicker.components.DatePicker();
        txtRFC = new javax.swing.JTextField();
        lblRFC = new javax.swing.JLabel();
        cBoxEstado = new javax.swing.JComboBox<>();
        lblEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        txtID.setEditable(false);

        lblDomicilio.setText("Domicilio");

        lblApellidos.setText("Apellidos");

        lblID.setText("ID Empleado");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        lblNombre.setText("Nombre");
        lblNombre.setToolTipText("");

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblTelefono.setText("Teléfono");

        lblCorreo.setText("Correo");

        lblFechaNacimiento.setText("Fecha Nacimiento");

        lblRFC.setText("RFC");

        cBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        lblEstado.setText("Estado");

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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCorreo)
                                    .addComponent(lblTelefono)
                                    .addComponent(lblApellidos)
                                    .addComponent(lblNombre)
                                    .addComponent(lblID)
                                    .addComponent(lblDomicilio))
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtApellidos)
                                    .addComponent(txtNombre)
                                    .addComponent(txtID)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtCorreo)
                                            .addComponent(txtDomicilio, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFechaNacimiento)
                                    .addComponent(lblRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRFC, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dpFechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cBoxEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(16, 16, 16))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblID)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidos)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCorreo)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDomicilio)
                    .addComponent(txtDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaNacimiento)
                    .addComponent(dpFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRFC))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
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
    private javax.swing.JComboBox<String> cBoxEstado;
    private com.github.lgooddatepicker.components.DatePicker dpFechaNacimiento;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDomicilio;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaNacimiento;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRFC;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDomicilio;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRFC;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
