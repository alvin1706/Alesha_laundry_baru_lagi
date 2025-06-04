package com.raven.main;

import Login.Login;
import com.raven.event.EventMenu;
import com.raven.form.Form1;
import com.raven.form.Form_BarangMasuk;
import com.raven.form.Form_DataLayanan;
import com.raven.form.Form_Databarang;
import com.raven.form.Form_Datakaryawan;
import com.raven.form.Form_Diskon;
import com.raven.form.Form_Home;
import com.raven.form.Form_RiwayatKeluar;
import com.raven.form.Form_RiwayatMasuk;
import com.raven.form.Form_RiwayatTransaksi;
import com.raven.form.form_Absen;
import com.raven.utils.User;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;

public class Main extends javax.swing.JFrame {

    public Main() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        showForm(new Form_Home());
        init();
            }
    
     private void init() {
         titleBar.initJFram(this);
     }

    private void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.raven.swing.PanelBorder();
        body = new javax.swing.JLayeredPane();
        titleBar = new com.raven.tittlebar.TitleBar();
        menu1 = new com.raven.component.Menu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panelBorder1.setBackground(new java.awt.Color(179, 234, 234));

        body.setBackground(new java.awt.Color(182, 234, 234));
        body.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(body)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        menu1.setSelectedIndex(0);
        menu1.addEvent(new EventMenu() {
            @Override
            public void menuIndexChange(int index) {
                if (index == 0) {
                    showForm(new Form_Home());
                } else if (index == 1) {
                    showForm(new Form_Datakaryawan());
                }else if (index == 2) {
                    showForm(new Form_RiwayatTransaksi());
                }else if (index == 3) {
                    showForm(new Form_DataLayanan());
                }else if (index == 4) {
                    showForm(new Form_Databarang());
                }else if (index == 5) {
                    showForm(new Form_BarangMasuk());
                }else if (index == 6) {
                    showForm(new Form_RiwayatMasuk());
                }else if (index == 7) {
                    showForm(new Form_RiwayatKeluar());
                }else if (index == 8) {
                    showForm(new form_Absen());
                 }else if (index == 9) {
                    showForm(new Form_Diskon());
                }else{
                    int result = JOptionPane.showConfirmDialog(null, "Logout?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        User.setId("");
                        User.setNama("");
                        setVisible(false);
                        Login form = new Login();
                        form.show();
                    }
            }
            }
        });
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane body;
    private com.raven.component.Menu menu1;
    private com.raven.swing.PanelBorder panelBorder1;
    private com.raven.tittlebar.TitleBar titleBar;
    // End of variables declaration//GEN-END:variables
}
