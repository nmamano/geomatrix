
package geomatrix.presentation.swing;

import java.awt.BorderLayout;
import manticore.presentation.SwingController;

/**
 *
 * @author Nil
 */
public class MainFrame extends javax.swing.JFrame {

    private MapPanel mapPanel;
    private SwingController swing;
    /**
     * Creates new form MainFrame
     */
    public MainFrame(SwingController swing) { 
        initComponents();
        this.swing = swing;
        initMapPanel();
        pack();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        selectArea1 = new javax.swing.JRadioButton();
        selectArea2 = new javax.swing.JRadioButton();
        selectArea3 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        displayArea1 = new javax.swing.JToggleButton();
        displayArea2 = new javax.swing.JToggleButton();
        displayArea3 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Geomatrix");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Select"));

        selectArea1.setBackground(java.awt.Color.red);
        buttonGroup1.add(selectArea1);
        selectArea1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectArea1.setSelected(true);
        selectArea1.setText("Area 1");
        selectArea1.setToolTipText("Set Area 1 as selected area");
        selectArea1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectArea1ActionPerformed(evt);
            }
        });

        selectArea2.setBackground(new java.awt.Color(0, 51, 255));
        buttonGroup1.add(selectArea2);
        selectArea2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectArea2.setText("Area 2");
        selectArea2.setToolTipText("Set Area 2 as selected area");
        selectArea2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectArea2ActionPerformed(evt);
            }
        });

        selectArea3.setBackground(new java.awt.Color(0, 204, 0));
        buttonGroup1.add(selectArea3);
        selectArea3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectArea3.setText("Area 3");
        selectArea3.setToolTipText("Set Area 3 as selected area");
        selectArea3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectArea3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectArea1)
                    .addComponent(selectArea3)
                    .addComponent(selectArea2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {selectArea1, selectArea2, selectArea3});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectArea1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectArea2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectArea3)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Display"));

        displayArea1.setForeground(java.awt.Color.red);
        displayArea1.setSelected(true);
        displayArea1.setText("Area 1");
        displayArea1.setToolTipText("Display Area 1");
        displayArea1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayArea1ActionPerformed(evt);
            }
        });

        displayArea2.setForeground(new java.awt.Color(0, 51, 255));
        displayArea2.setSelected(true);
        displayArea2.setText("Area 2");
        displayArea2.setToolTipText("Display area 2");
        displayArea2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayArea2ActionPerformed(evt);
            }
        });

        displayArea3.setForeground(new java.awt.Color(0, 153, 0));
        displayArea3.setSelected(true);
        displayArea3.setText("Area 3");
        displayArea3.setToolTipText("Display area 3");
        displayArea3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayArea3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(displayArea1)
                    .addComponent(displayArea2)
                    .addComponent(displayArea3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {displayArea1, displayArea2, displayArea3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(displayArea1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayArea2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayArea3))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectArea1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectArea1ActionPerformed
        mapPanel.setSelected(1);
    }//GEN-LAST:event_selectArea1ActionPerformed

    private void displayArea1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayArea1ActionPerformed
        try {
            if (displayArea1.isSelected()) mapPanel.setDisplayed(1, true);
            else mapPanel.setDisplayed(1, false);
        }
        catch (HideSelectedAreaException e) {
            //do not allow to hide selected area
            displayArea1.setSelected(true);
        }
    }//GEN-LAST:event_displayArea1ActionPerformed

    private void displayArea2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayArea2ActionPerformed
        try {
            if (displayArea2.isSelected()) mapPanel.setDisplayed(2, true);
            else mapPanel.setDisplayed(2, false);
        }
        catch (HideSelectedAreaException e) {
            //do not allow to hide selected area
            displayArea2.setSelected(true);
        }
    }//GEN-LAST:event_displayArea2ActionPerformed

    private void displayArea3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayArea3ActionPerformed
        try {
            if (displayArea3.isSelected()) mapPanel.setDisplayed(3, true);
            else mapPanel.setDisplayed(3, false);
        }
        catch (HideSelectedAreaException e) {
            //do not allow to hide selected area
            displayArea3.setSelected(true);
        }
    }//GEN-LAST:event_displayArea3ActionPerformed

    private void selectArea2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectArea2ActionPerformed
        mapPanel.setSelected(2);
    }//GEN-LAST:event_selectArea2ActionPerformed

    private void selectArea3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectArea3ActionPerformed
        mapPanel.setSelected(3);
    }//GEN-LAST:event_selectArea3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton displayArea1;
    private javax.swing.JToggleButton displayArea2;
    private javax.swing.JToggleButton displayArea3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton selectArea1;
    private javax.swing.JRadioButton selectArea2;
    private javax.swing.JRadioButton selectArea3;
    // End of variables declaration//GEN-END:variables

    private void initMapPanel() {
        mapPanel = swing.get(MapPanel.class);
        add(mapPanel, BorderLayout.CENTER);
        mapPanel.setVisible(true);
        pack();
    }
}
