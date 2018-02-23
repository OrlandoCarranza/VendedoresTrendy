/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sucursalesupdatesavingmodified;

import java.awt.Color;

public class iconLoader extends javax.swing.JFrame {

    private int auxiliar = 0;
    private final boolean ejecutado = false;
    hilo ejecutando = new hilo();

    /**
     * Creates new form iconLoader
     */
    public iconLoader() {
        initComponents();
        iconLoader.this.getRootPane().setOpaque(false);
        iconLoader.this.getContentPane().setBackground(new Color(0, 0, 0, 0));
        iconLoader.this.setBackground(new Color(0, 0, 0, 0));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text = new javax.swing.JLabel();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        text.setFont(new java.awt.Font("Consolas", 1, 10)); // NOI18N
        getContentPane().add(text, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 200, 10));
        getContentPane().add(barra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 300, 10));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconImages/Logo trendy negro.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if(ejecutado == false){
            boolean name = ejecutado == true; 
            barra.setMaximum(49);
            barra.setMinimum(0);
            barra.setStringPainted(true);
            ejecutando.start();
        }
    }//GEN-LAST:event_formWindowActivated

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(iconLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(iconLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(iconLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(iconLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new iconLoader().setVisible(true);
            }
        });
    }

    private class hilo extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    auxiliar++;
                    barra.setValue(auxiliar);
                    repaint();
                    switch (auxiliar) {
                        case 3:
                            text.setText("Cargando...");
                            break;
                        case 30:
                            text.setText("Iniciando las bases de datos...");
                            break;
                        case 45:
                            text.setText("Cargando módulos..");
                            break;
                        case 55:
                            text.setText("Carga finalizada!!!");
                            Vendedores objeto = new Vendedores();
                            objeto.setVisible(true);
                            objeto.setLocationRelativeTo(iconLoader.this);
                            iconLoader.this.dispose();
                            break;
                    }
                    Thread.sleep(85);
                }
            } catch (InterruptedException ex) {
//                Logger.getLogger(iconLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel text;
    // End of variables declaration//GEN-END:variables
}