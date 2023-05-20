package gui;

import control.IControl;

public class MenuPrincipalForm extends javax.swing.JFrame {

    IControl control;

    public MenuPrincipalForm(IControl control) {
        initComponents();
        this.control = control;

        imgLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgLogo.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        
        this.setLocationRelativeTo(null);
    }

    private void ocultarMenu() {
        this.setVisible(false);
    }

    protected void mostrarMenu() {
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imgLogo = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        secciones = new javax.swing.JMenu();
        miEmpleados = new javax.swing.JMenuItem();
        miSalarios = new javax.swing.JMenuItem();
        miPagos = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/pizza_nostra_logo_1-removebg-preview.png"))); // NOI18N

        secciones.setText("Recursos");

        miEmpleados.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        miEmpleados.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        miEmpleados.setText("Empleados");
        miEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEmpleadosActionPerformed(evt);
            }
        });
        secciones.add(miEmpleados);

        miSalarios.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        miSalarios.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        miSalarios.setText("Salarios");
        miSalarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalariosActionPerformed(evt);
            }
        });
        secciones.add(miSalarios);

        miPagos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, 0));
        miPagos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        miPagos.setText("Pagos");
        miPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPagosActionPerformed(evt);
            }
        });
        secciones.add(miPagos);

        menuBar.add(secciones);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(imgLogo)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imgLogo)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void miEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEmpleadosActionPerformed
        new MenuEmpleadosForm(control, this).setVisible(true);
        ocultarMenu();
    }//GEN-LAST:event_miEmpleadosActionPerformed

    private void miSalariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalariosActionPerformed
        new MenuSalariosForm(control, this).setVisible(true);
        ocultarMenu();
    }//GEN-LAST:event_miSalariosActionPerformed

    private void miPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPagosActionPerformed
        new MenuPagosForm(control, this).setVisible(true);
        ocultarMenu();
    }//GEN-LAST:event_miPagosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imgLogo;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem miEmpleados;
    private javax.swing.JMenuItem miPagos;
    private javax.swing.JMenuItem miSalarios;
    private javax.swing.JMenu secciones;
    // End of variables declaration//GEN-END:variables
}
