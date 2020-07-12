package mathquiz.guis;

//Import util libraries
import java.util.List;

//Import Swing libraries
import javax.swing.JOptionPane;

import mathquiz.objs.SortByQuiz;
import mathquiz.methods.SearchMethods;

//Import third-party libraries
import org.apache.commons.lang3.StringUtils;

//**********************************************************/
// Filename: GuiSeeker.java
// Purpose:  To provide GUI and functions that teachers can
//           check questions that have asked
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqs-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class GuiSeeker<T> extends javax.swing.JFrame {
    
    //Declare a variable for data set
    private List dataList;
    
    public GuiSeeker(){
        
    }
    
    //Construction
    public GuiSeeker(List<T> list) {
        
        this.dataList = list;
        
        initComponents();   
    }
    
    //**********************************************************/
    // Mothod:  private void findQuiz()
    // Purpose: To find a quiz from a data set
    // Input:   none
    // Output:  void
    //**********************************************************/
    private void findQuiz(){
        
        String fNum = tfdFirstFigure.getText().trim();
        String operator = cbxOperator.getSelectedItem().toString().trim();
        String sNum = tfdSecondFigure.getText().trim();
        
        try{
            //Declare a quiz object for searching
            SortByQuiz quizToSearch = new SortByQuiz(fNum, operator, sNum);
            
            //Display boolean value on the text field
            tfdReturn.setText
            (
                String.valueOf
                (
                    SearchMethods.binarySearch(dataList, quizToSearch)
                )
                .toUpperCase()
            );
        }
        //Catch if user accidently inser non-numeric values
        catch(NumberFormatException e){
            String value = "";
            
            //Checking if both first and second figures are non-numeric values
            if (! StringUtils.isNumeric(fNum) &
                ! StringUtils.isNumeric(sNum)){
                value = "Both values <" + fNum + ">" + " and " + "<" + sNum + ">";
            }
            //Checking first figure
            else if(! StringUtils.isNumeric(fNum)){
                value = "First value <" + fNum + ">";
            }
            //Checking second figure
            else if(! StringUtils.isNumeric(sNum)){
                value = "Second value <" + sNum + ">";
            }
            
            JOptionPane.showMessageDialog(null, value + " is or are not numeric value!", "WRONG INPUT ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTitle = new javax.swing.JPanel();
        lblSearchPanel = new javax.swing.JLabel();
        pnlEnterQuizBody = new javax.swing.JPanel();
        pnlEnterQuizInnerBodyLeft = new javax.swing.JPanel();
        lblFirstFigure = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        lblSecondFigure = new javax.swing.JLabel();
        lblReturn = new javax.swing.JLabel();
        pnlEnterQuizInnerBodyRight = new javax.swing.JPanel();
        tfdFirstFigure = new javax.swing.JTextField();
        cbxOperator = new javax.swing.JComboBox<>();
        tfdSecondFigure = new javax.swing.JTextField();
        tfdReturn = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlTitle.setBackground(new java.awt.Color(55, 55, 55));

        lblSearchPanel.setFont(new java.awt.Font("Lucida Grande", 0, 22)); // NOI18N
        lblSearchPanel.setForeground(java.awt.Color.white);
        lblSearchPanel.setText("Search");

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSearchPanel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSearchPanel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlEnterQuizBody.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizBody.setPreferredSize(new java.awt.Dimension(350, 231));

        pnlEnterQuizInnerBodyLeft.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizInnerBodyLeft.setLayout(new java.awt.GridLayout(4, 2));

        lblFirstFigure.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblFirstFigure.setForeground(java.awt.Color.white);
        lblFirstFigure.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblFirstFigure.setText("First Figure: ");
        pnlEnterQuizInnerBodyLeft.add(lblFirstFigure);

        lblOperator.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblOperator.setForeground(java.awt.Color.white);
        lblOperator.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOperator.setText("Operator: ");
        pnlEnterQuizInnerBodyLeft.add(lblOperator);

        lblSecondFigure.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblSecondFigure.setForeground(java.awt.Color.white);
        lblSecondFigure.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSecondFigure.setText("Second Figure: ");
        pnlEnterQuizInnerBodyLeft.add(lblSecondFigure);

        lblReturn.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblReturn.setForeground(java.awt.Color.white);
        lblReturn.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblReturn.setText("Found: ");
        pnlEnterQuizInnerBodyLeft.add(lblReturn);

        pnlEnterQuizInnerBodyRight.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizInnerBodyRight.setLayout(new java.awt.GridLayout(4, 1));

        tfdFirstFigure.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        pnlEnterQuizInnerBodyRight.add(tfdFirstFigure);

        cbxOperator.setBackground(java.awt.Color.white);
        cbxOperator.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cbxOperator.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+", "-", "x", "÷" }));
        pnlEnterQuizInnerBodyRight.add(cbxOperator);

        tfdSecondFigure.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        pnlEnterQuizInnerBodyRight.add(tfdSecondFigure);

        tfdReturn.setEditable(false);
        tfdReturn.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        pnlEnterQuizInnerBodyRight.add(tfdReturn);

        btnFind.setBackground(new java.awt.Color(55, 55, 55));
        btnFind.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnFind.setText("Find");
        btnFind.setPreferredSize(new java.awt.Dimension(85, 35));
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnClose.setBackground(new java.awt.Color(55, 55, 55));
        btnClose.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(80, 35));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEnterQuizBodyLayout = new javax.swing.GroupLayout(pnlEnterQuizBody);
        pnlEnterQuizBody.setLayout(pnlEnterQuizBodyLayout);
        pnlEnterQuizBodyLayout.setHorizontalGroup(
            pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnterQuizBodyLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlEnterQuizInnerBodyLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEnterQuizInnerBodyRight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEnterQuizBodyLayout.setVerticalGroup(
            pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnterQuizBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlEnterQuizInnerBodyLeft, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(pnlEnterQuizInnerBodyRight, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlEnterQuizBody, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEnterQuizBody, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        this.findQuiz();
    }//GEN-LAST:event_btnFindActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiSeeker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiSeeker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiSeeker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiSeeker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiSeeker().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnClose;
    private javax.swing.JButton btnFind;
    private javax.swing.JComboBox<String> cbxOperator;
    private javax.swing.JLabel lblFirstFigure;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblReturn;
    private javax.swing.JLabel lblSearchPanel;
    private javax.swing.JLabel lblSecondFigure;
    private javax.swing.JPanel pnlEnterQuizBody;
    private javax.swing.JPanel pnlEnterQuizInnerBodyLeft;
    private javax.swing.JPanel pnlEnterQuizInnerBodyRight;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JTextField tfdFirstFigure;
    private javax.swing.JTextField tfdReturn;
    private javax.swing.JTextField tfdSecondFigure;
    // End of variables declaration//GEN-END:variables

    
}
