/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.raven.form;
import Connection.DatabaseConnection;
import com.raven.table.TableCustom;
import com.raven.table.TableCustom;
import java.awt.Desktop;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author MUTIARA
 */
public class Form_RiwayatTransaksi extends javax.swing.JPanel {

    /**
     * Creates new form Form_RiwayatTransaksi
     */
    public Form_RiwayatTransaksi() {
        initComponents();
        TableCustom.apply(Scroll, TableCustom.TableType.DEFAULT);
        TableCustom.apply(scroll, TableCustom.TableType.DEFAULT);
        loadMainTable();
        uang.setText(String.valueOf(getTotalTransaksi()));
        fillMonthComboBox();
    }
    public void generateLaporanHariIniDanTampilkan(Date selectedDate) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            PDFont font = PDType1Font.TIMES_ROMAN;

            float y = 750;
            float leading = 15;

            // Header
            content.beginText();
            content.setFont(font, 14);
            content.newLineAtOffset(50, y);
            content.showText("LAPORAN KEUANGAN");
            content.endText();

            y -= leading * 2;

            // Format dan tampilkan tanggal yang dipilih
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(selectedDate);

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Tanggal: " + formattedDate);
            content.endText();
            y -= leading * 2;

            Connection conn = DatabaseConnection.getConn();

            // -------- PEMASUKAN --------
            PreparedStatement pemasukanStmt = conn.prepareStatement(
                "SELECT kode, total_akhir AS total FROM transaksi WHERE DATE(tanggal) = ?");
            pemasukanStmt.setString(1, formattedDate);
            ResultSet pemasukanRs = pemasukanStmt.executeQuery();

            int totalPemasukan = 0;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("PEMASUKAN:");
            content.endText();
            y -= leading;

            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(50, y);
            content.showText("Kode Transaksi        Total");
            content.endText();
            y -= leading;

            while (pemasukanRs.next()) {
                String kode = pemasukanRs.getString("kode");
                int total = pemasukanRs.getInt("total");
                totalPemasukan += total;

                content.beginText();
                content.setFont(font, 10);
                content.newLineAtOffset(50, y);
                content.showText(String.format("%-20s Rp. %,d", kode, total).replace(',', '.'));
                content.endText();
                y -= leading;
            }

            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(50, y);
            content.showText("Total Pemasukan: Rp. " + String.format("%,d", totalPemasukan).replace(',', '.'));
            content.endText();
            y -= leading * 2;

            // -------- PENGELUARAN --------
            PreparedStatement pengeluaranStmt = conn.prepareStatement(
                "SELECT b.nama AS nama_barang, p.qty, b.harga_avg AS harga, (p.qty * b.harga_avg) AS total " +
                "FROM pengeluaran_barang_transaksi p " +
                "JOIN transaksi t ON p.kode_transaksi = t.kode " +
                "JOIN barang b ON p.id_barang = b.id " +
                "WHERE DATE(t.tanggal_diambil) = ?");
            pengeluaranStmt.setString(1, formattedDate);
            ResultSet pengeluaranRs = pengeluaranStmt.executeQuery();

            int totalPengeluaran = 0;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("PENGELUARAN:");
            content.endText();
            y -= leading;

            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(50, y);
            content.showText("Barang               Qty   Harga    Total");
            content.endText();
            y -= leading;

            while (pengeluaranRs.next()) {
                String nama = pengeluaranRs.getString("nama_barang");
                int qty = pengeluaranRs.getInt("qty");
                int harga = pengeluaranRs.getInt("harga");
                int subtotal = pengeluaranRs.getInt("total");
                totalPengeluaran += subtotal;

                content.beginText();
                content.setFont(font, 10);
                content.newLineAtOffset(50, y);
                content.showText(String.format("%-20s %3d   Rp.%,d   Rp.%,d", nama, qty, harga, subtotal)
                        .replace(',', '.'));
                content.endText();
                y -= leading;
            }

            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(50, y);
            content.showText("Total Pengeluaran: Rp. " + String.format("%,d", totalPengeluaran).replace(',', '.'));
            content.endText();
            y -= leading * 2;

            // -------- LABA / RUGI --------
            int laba = totalPemasukan - totalPengeluaran;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("LABA / RUGI: Rp. " + String.format("%,d", laba).replace(',', '.'));
            content.endText();

            content.close();

            // Simpan dan tampilkan file PDF
            File tempFile = File.createTempFile("laporan_keuangan_", ".pdf");
            doc.save(tempFile);
            doc.close();

            Desktop.getDesktop().open(tempFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Scroll = new javax.swing.JScrollPane();
        MainTable = new javax.swing.JTable();
        scroll = new javax.swing.JScrollPane();
        SecondTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        uang = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(182, 234, 234));

        MainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "No Hp", "Total", "Tanggal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        MainTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MainTableMouseClicked(evt);
            }
        });
        Scroll.setViewportView(MainTable);

        SecondTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama Layanan", "Harga", "Quantity", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scroll.setViewportView(SecondTable);
        if (SecondTable.getColumnModel().getColumnCount() > 0) {
            SecondTable.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Riwayat Transaksi");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jButton1.setText("Filter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        uang.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        uang.setText("0");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Total Pendapatan = ");

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("<Transaksi>");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("<Detail Transaksi>");

        jButton2.setText("Laporan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(41, 41, 41))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1056, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Scroll)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(uang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(uang)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadTransactionsByMonth();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void MainTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MainTableMouseClicked
        int selectedRow = MainTable.getSelectedRow();

        if (selectedRow >= 0) {
            String kodeTransaksi = MainTable.getValueAt(selectedRow, 0).toString();
            loadSecondTable(kodeTransaksi);
        }
    }//GEN-LAST:event_MainTableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Date tanggal = jDateChooser1.getDate();
        generateLaporanHariIniDanTampilkan(tanggal);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void fillMonthComboBox() {
        String[] bulan = {
            "Januari", "Februari", "Maret", "April", "Mei", 
            "Juni", "Juli", "Agustus", "September", "Oktober", 
            "November", "Desember"
        };

        for (String b : bulan) {
            jComboBox1.addItem(b);
        }
    }
    public int getTotalTransaksi() {
        String query = "SELECT SUM(subtotal) AS total_transaksi FROM detail_transaksi WHERE kode_transaksi IN (SELECT kode FROM transaksi WHERE status = 'diambil')";
        

        try {
            ResultSet rs = DatabaseConnection.getData(query);

            rs.next();
            
            return rs.getInt("total_transaksi");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menghitung total transaksi: " + e.getMessage());
        }
        return 0;
    }
    public void loadTransactionsByMonth() {
        String selectedMonth = (String) jComboBox1.getSelectedItem(); 

        int monthNumber = getMonthNumber(selectedMonth);

        String query = "SELECT t.kode, p.nama AS nama_pelanggan, p.no_hp, t.tanggal, t.status FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id WHERE MONTH(t.tanggal) = ? AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) AND t.status = 'diambil'";
        

        DefaultTableModel mainTableModel = (DefaultTableModel) MainTable.getModel();
        mainTableModel.setRowCount(0);  // Reset data di tabel

        try {
            PreparedStatement pst = DatabaseConnection.getConn().prepareStatement(query);
            pst.setInt(1, monthNumber); 

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String kode = rs.getString("kode");
                String namaPelanggan = rs.getString("nama_pelanggan");
                String noHp = rs.getString("no_hp");
                String tanggal = rs.getString("tanggal");
                String status = rs.getString("status");

                mainTableModel.addRow(new Object[]{kode, namaPelanggan, noHp, tanggal, status});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data transaksi: " + e.getMessage());
        }
    }
    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "Januari": return 1;
            case "Februari": return 2;
            case "Maret": return 3;
            case "April": return 4;
            case "Mei": return 5;
            case "Juni": return 6;
            case "Juli": return 7;
            case "Agustus": return 8;
            case "September": return 9;
            case "Oktober": return 10;
            case "November": return 11;
            case "Desember": return 12;
            default: return -1; // Jika bulan tidak valid
        }
    }
    public void loadMainTable() {
        String query = "SELECT t.kode, p.nama AS nama_pelanggan, p.no_hp, t.total_akhir, t.tanggal, t.status FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id WHERE t.status = 'diambil' ";

        DefaultTableModel mainTableModel = (DefaultTableModel) MainTable.getModel();
        mainTableModel.setRowCount(0);

        try {
            ResultSet rs = DatabaseConnection.getData(query);

            while (rs.next()) {
                String kode = rs.getString("kode");
                String namaPelanggan = rs.getString("nama_pelanggan");
                String noHp = rs.getString("no_hp");
                String tanggal = rs.getString("tanggal");
                String status = rs.getString("status");
                String total = String.valueOf(rs.getInt("total_akhir"));

                mainTableModel.addRow(new Object[]{kode, namaPelanggan, noHp, total, tanggal, status});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data transaksi: " + e.getMessage());
        }
        MainTable.setModel(mainTableModel);
    }
     public void loadSecondTable(String kodeTransaksi) {
        String query = "SELECT dl.id_layanan, l.nama AS nama_layanan, dl.harga, dl.qty, dl.subtotal " +
               "FROM detail_transaksi dl " +
               "JOIN layanan l ON dl.id_layanan = l.id " +
               "WHERE dl.kode_transaksi = ?";

        DefaultTableModel secondTableModel = (DefaultTableModel) SecondTable.getModel();
        secondTableModel.setRowCount(0); 

        try {
            PreparedStatement ps = DatabaseConnection.getConn().prepareStatement(query);
            ps.setString(1, kodeTransaksi);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String idLayanan = rs.getString("id_layanan");
                String namaLayanan = rs.getString("nama_layanan");
                int harga = rs.getInt("harga");
                int qty = rs.getInt("qty");
                int subtotal = rs.getInt("subtotal");

                secondTableModel.addRow(new Object[]{idLayanan, namaLayanan, String.valueOf(harga), String.valueOf(qty), String.valueOf(subtotal)});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data detail transaksi: " + e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable MainTable;
    private javax.swing.JScrollPane Scroll;
    private javax.swing.JTable SecondTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JLabel uang;
    // End of variables declaration//GEN-END:variables
}
