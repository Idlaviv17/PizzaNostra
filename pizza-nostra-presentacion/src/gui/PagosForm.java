package gui;

import control.Control;
import control.IControl;
import entidades.Pago;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PagosForm extends javax.swing.JFrame {
    
    private final IControl control;
    
    public PagosForm(IControl control) {
        initComponents();
        this.control = control;
        this.llenarTabla();
    }
    
//    private void guardar() {
//        if (this.txtID.getText().isEmpty()) {
//            this.agregar();
//        } else {
//            this.actualizar();
//        }
//    }
//    
//    private void agregar() {
//        String nombre = this.txtEmpleado.getText();
//        String curp = this.txtInicioPeriodo.getText();
//        //TODO: Agregar validaciones
//        boolean seAgregoSocio = this.sociosDAO.agregar(new Socio(nombre, curp));
//        if (seAgregoSocio) {
//            JOptionPane.showMessageDialog(this, "Se agregó el nuevo socio", "Información", JOptionPane.INFORMATION_MESSAGE);
//            this.limpiarFormulario();
//            this.llenarTabla();
//        } else {
//            JOptionPane.showMessageDialog(this, "No fue posible agregar el socio", "Información", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    private void actualizar() {
//        Long idSocio = Long.parseLong(this.txtID.getText());
//        String nombre = this.txtEmpleado.getText();
//        String curp = this.txtInicioPeriodo.getText();
//        boolean seActualizoSocio = this.sociosDAO.actualizar(new Socio(idSocio, nombre, curp));
//        if (seActualizoSocio) {
//            JOptionPane.showMessageDialog(this, "Se actualizó el socio", "Información", JOptionPane.INFORMATION_MESSAGE);
//            this.limpiarFormulario();
//            this.llenarTabla();
//        } else {
//            JOptionPane.showMessageDialog(this, "No fue posible actualizar el socio", "Información", JOptionPane.ERROR_MESSAGE);
//        }
//        
//    }
//    
//    private void editar() {
//        Long idSocioSeleccionado = this.getIdSocioSeleccionado();
//        if (idSocioSeleccionado == null) {   
//            JOptionPane.showMessageDialog(this, "Debes seleccionar un socio", "Información", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        Socio socio = this.sociosDAO.consultar(idSocioSeleccionado);
//        if (socio != null) {
//            this.llenarFormulario(socio);
//        }
//    }
//    
//    private void eliminar() {
//        Long idSocioSeleccionado = this.getIdSocioSeleccionado();
//        if (idSocioSeleccionado == null) {   
//            JOptionPane.showMessageDialog(this, "Debes seleccionar un socio", "Información", JOptionPane.WARNING_MESSAGE);
//        } else {
//            int opcionSeleccionada = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al socio?", "Confirmación", JOptionPane.YES_NO_OPTION);
//            
//            if (opcionSeleccionada == JOptionPane.NO_OPTION) {
//                return;
//            }
//            
//            boolean seEliminoSocio = this.sociosDAO.eliminar(idSocioSeleccionado);
//            if (seEliminoSocio) {
//                JOptionPane.showMessageDialog(this, "Se eliminó el socio correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
//                this.llenarTabla();
//            } else {
//                JOptionPane.showMessageDialog(this, "No se pudo eliminar el socio", "Información", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//    
//    private Long getIdSocioSeleccionado() {
//        int indiceFilaSeleccionada = this.tblSocios.getSelectedRow();
//        if (indiceFilaSeleccionada != -1) {
//            DefaultTableModel modelo = (DefaultTableModel) this.tblSocios.getModel();
//            int indiceColumnaId = 0;
//            Long idSocioSeleccionado = (Long) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
//            return idSocioSeleccionado;
//        } else {
//            return null;
//        }
//    }
//    
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
//    
//    private void llenarFormulario(Socio socio) {
//        this.txtID.setText(socio.getIdSocio().toString());
//        this.txtEmpleado.setText(socio.getNombre());
//        this.txtInicioPeriodo.setText(socio.getCurp());
//    }
//    
//    private void limpiarFormulario() {
//        this.txtID.setText("");
//        this.txtEmpleado.setText("");
//        this.txtInicioPeriodo.setText("");
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPagoID = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        lblInicioPeriodo = new javax.swing.JLabel();
        txtEmpleado = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        txtInicioPeriodo = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblFinPeriodo = new javax.swing.JLabel();
        txtFinPeriodo = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        lblComentario = new javax.swing.JLabel();
        lblSalario = new javax.swing.JLabel();
        txtSalario = new javax.swing.JTextField();
        lblHorasTrabajadas = new javax.swing.JLabel();
        txtHorasTrabajadas = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtComentario = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblPagoID.setText("ID Pago");

        lblEmpleado.setText("ID Empleado");
        lblEmpleado.setToolTipText("");

        lblInicioPeriodo.setText("Inicio Periodo");

        txtID.setEditable(false);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
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
                "ID Pago", "ID Empleado", "ID Salario", "Inicio Periodo", "Fin Periodo", "Fecha", "Estado", "Horas Trabajadas", "Comentario"
            }
        ));
        jScrollPane1.setViewportView(tblPagos);

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        lblFinPeriodo.setText("Fin Periodo");

        lblFecha.setText("Fecha");

        lblEstado.setText("Estado");

        lblComentario.setText("Comentario");

        lblSalario.setText("ID Salario");

        lblHorasTrabajadas.setText("Horas Trabajadas");

        txtComentario.setColumns(20);
        txtComentario.setRows(5);
        jScrollPane2.setViewportView(txtComentario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPagoID)
                                    .addComponent(lblEmpleado))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                    .addComponent(txtID)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblHorasTrabajadas)
                                    .addComponent(lblEstado)
                                    .addComponent(lblFecha)
                                    .addComponent(lblFinPeriodo)
                                    .addComponent(lblInicioPeriodo)
                                    .addComponent(lblSalario))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSalario, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                    .addComponent(txtInicioPeriodo)
                                    .addComponent(txtHorasTrabajadas)
                                    .addComponent(txtEstado)
                                    .addComponent(txtFinPeriodo, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtFecha))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(btnGuardar)
                            .addGap(18, 18, 18)
                            .addComponent(btnCancelar)
                            .addGap(64, 64, 64)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblComentario))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEditar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblPagoID)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblEmpleado))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblSalario)
                                    .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblInicioPeriodo)
                                    .addComponent(txtInicioPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblFinPeriodo)
                                    .addComponent(txtFinPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblFecha)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblEstado)
                                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblHorasTrabajadas)
                                    .addComponent(txtHorasTrabajadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(lblComentario)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnCancelar))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        //this.limpiarFormulario();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        //this.guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        //this.editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        //this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
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
    private javax.swing.JTextField txtEmpleado;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtFinPeriodo;
    private javax.swing.JTextField txtHorasTrabajadas;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtInicioPeriodo;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables
}
