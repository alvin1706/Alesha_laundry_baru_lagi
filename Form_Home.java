package com.raven.form;

import Connection.DatabaseConnection;
import com.raven.chart.ModelChart;
import com.raven.model.Model_Card;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Form_Home extends javax.swing.JPanel {

    public Form_Home() {
        initComponents();
        tampilkanJumlahTransaksiPerBulan();
        
        Map<String, String> income = getIncomeSummary();

        card1.setData(new Model_Card(
            new ImageIcon(getClass().getResource("/com/raven/icon/income.png")),
            "Income Hari ini",
            income.get("hari")
        ));

        card2.setData(new Model_Card(
            new ImageIcon(getClass().getResource("/com/raven/icon/other_income.png")),
            "Income Minggu ini",
            income.get("minggu")
        ));

        card3.setData(new Model_Card(
            new ImageIcon(getClass().getResource("/com/raven/icon/expense.png")),
            "Income Bulan ini",
            income.get("bulan")
        ));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        card1 = new com.raven.component.Card();
        card2 = new com.raven.component.Card();
        card3 = new com.raven.component.Card();
        panelShadow1 = new com.raven.swing.PanelShadow();
        jLabel2 = new javax.swing.JLabel();
        chart = new com.raven.chart.Chart();

        setBackground(new java.awt.Color(182, 234, 234));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Dashboard");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Grafik Pendapatan");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        chart.setOpaque(false);

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShadow1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                    .addComponent(jLabel1))
                .addContainerGap())
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    public Map<String, String> getIncomeSummary() {
        Map<String, String> hasil = new HashMap<>();
        try {
            Connection conn = DatabaseConnection.getConn();

            // Query gabungan 3 income
            String sql = "SELECT " +
                         "IFNULL(SUM(CASE WHEN DATE(tanggal) = CURDATE() THEN total_akhir END), 0) AS total_hari_ini, " +
                         "IFNULL(SUM(CASE WHEN YEARWEEK(tanggal, 1) = YEARWEEK(CURDATE(), 1) THEN total_akhir END), 0) AS total_minggu_ini, " +
                         "IFNULL(SUM(CASE WHEN YEAR(tanggal) = YEAR(CURDATE()) AND MONTH(tanggal) = MONTH(CURDATE()) THEN total_akhir END), 0) AS total_bulan_ini " +
                         "FROM transaksi";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int hariIni = rs.getInt("total_hari_ini");
                int mingguIni = rs.getInt("total_minggu_ini");
                int bulanIni = rs.getInt("total_bulan_ini");

                // Format angka jadi "Rp. 1.000.000"
                hasil.put("hari", "Rp. " + String.format("%,d", hariIni).replace(',', '.'));
                hasil.put("minggu", "Rp. " + String.format("%,d", mingguIni).replace(',', '.'));
                hasil.put("bulan", "Rp. " + String.format("%,d", bulanIni).replace(',', '.'));
            }

        } catch (Exception e) {
            e.printStackTrace();
            hasil.put("hari", "Rp. 0");
            hasil.put("minggu", "Rp. 0");
            hasil.put("bulan", "Rp. 0");
        }

        return hasil;
    }

     public void tampilkanJumlahTransaksiPerBulan() {
        card1.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/raven/icon/income.png")), "Income Hari ini", "Rp. 800.000"));
        card2.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/raven/icon/other_income.png")), "Income Minggu ini", "Rp. 7.000.000"));
        card3.setData(new Model_Card(new ImageIcon(getClass().getResource("/com/raven/icon/expense.png")), "Income Bulan ini", "Rp. 10.000.000"));

    try {// Kosongkan chart
        chart.addLegend("Jumlah Transaksi", new Color(76, 175, 80));

        Connection conn = DatabaseConnection.getConn();

        String sql = "SELECT MONTH(tanggal) AS bulan, COUNT(*) AS jumlah " +
                     "FROM transaksi WHERE YEAR(tanggal) = YEAR(CURDATE()) " +
                     "GROUP BY MONTH(tanggal)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        Map<Integer, Integer> dataTransaksi = new HashMap<>();
        while (rs.next()) {
            int bulan = rs.getInt("bulan");     // 1 - 12
            int jumlah = rs.getInt("jumlah");   // jumlah transaksi
            System.out.println("Bulan: " + bulan + " -> " + jumlah);
            dataTransaksi.put(bulan, jumlah);
        }

        // Loop dari Januari (1) sampai Desember (12)
        String[] namaBulan = new DateFormatSymbols().getMonths(); // Nama-nama bulan lengkap

        for (int i = 1; i <= 12; i++) {
            String labelBulan = namaBulan[i - 1]; // nama bulan sesuai index (0-based)
            int jumlah = dataTransaksi.getOrDefault(i, 0); // Ambil jumlah atau 0

            // Tambahkan ke chart, meskipun jumlah 0
            chart.addData(new com.raven.chart.ModelChart(labelBulan, new double[]{jumlah}));
        }
 // Tampilkan chart

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.component.Card card1;
    private com.raven.component.Card card2;
    private com.raven.component.Card card3;
    private com.raven.chart.Chart chart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private com.raven.swing.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
