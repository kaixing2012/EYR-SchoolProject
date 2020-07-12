package mathquiz.guis;

//Import AWT package
import java.awt.event.KeyEvent;

//Import Swing package
import javax.swing.JOptionPane;

//Import Networks package
import mathquiz.networks.Client;
import mathquiz.networks.IMessageListener;

//Import third-party package
import org.apache.commons.lang3.StringUtils;


//**********************************************************/
// Filename: GuiStudent.java
// Purpose:  To provide GUI and functions that student can interact
//           math quiz with teacher like answering a question
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqsd-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class GuiStudent extends javax.swing.JFrame implements IMessageListener{
    
    //Declare an array to store message in
    private static String[] messageInArr;
    
    //Declare a client object
    private Client client;
    
    private GuiStudent() {       
        initComponents();
    }
    
    //Construction
    public GuiStudent(Client c, String title) {
        
        client = c;
        client.addMessageListener(this);
        client.join("class");
        
        initComponents();
        
        this.setTitle(title);
    }
    
    //**********************************************************/
    // Mothod:  public void onMessage(String fromMessage, 
    //                                String msgBody)
    // Purpose: To listen message from the other sdie of network
    // Input:   String
    // Output:  void
    //**********************************************************/
    @Override
    public void onMessage(String fromMessage, String msgBody) {
        System.out.println(msgBody);
        String messageIn = "";
        messageInArr = StringUtils.split(msgBody);
        
        // Reformat message from server
        for (int i = 0; i < messageInArr.length - 1; i++){

            if(i != 0)
                messageIn += " ";

            messageIn += messageInArr[i];
        }
        
        //Display message on the text field
        tfdQuestion.setText(messageIn);
        messageIn = "";
    }
    
    //**********************************************************/
    // Mothod:  private void send()
    // Purpose: To send message out to the other side of network
    // Input:   none
    // Output:  void
    //**********************************************************/
    private void send(){
        //Check field for answer is filled in or not
        if (! tfdAnswer.getText().equals("")){
            String answer = tfdAnswer.getText().trim(); 
            
            //Try to catch if the input is not numeric value
            try{      
                
                //Check if the answer provided by student is correct or not
                if (answer.equals(messageInArr[messageInArr.length-1].trim())){
                    client.send("hans", "y");
                    JOptionPane.showMessageDialog(null, "Well Done", 
                            "Result", JOptionPane.PLAIN_MESSAGE);
                }
                //If the answer is wrong then return letter n as feedback to 
                //the teacher side
                else{
                    client.send("hans", "n");
                    JOptionPane.showMessageDialog(null, "Sorry - not correct.", 
                            "Result", JOptionPane.WARNING_MESSAGE);
                }

                // clear send text field
                tfdAnswer.setText("");
                tfdQuestion.setText("");
            }
            catch(NumberFormatException e){
                 JOptionPane.showMessageDialog(null, answer + " is not a numeric value!", "WRONG INPUT ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        else
        {
            JOptionPane.showMessageDialog(null, "INFO: Fill in your answer!", 
                    "Provide An Answer", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTitle = new javax.swing.JPanel();
        lblStudent = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();
        btnSignOut = new javax.swing.JButton();
        palInnerBody = new javax.swing.JPanel();
        lblQuestion = new javax.swing.JLabel();
        tfdQuestion = new javax.swing.JTextField();
        lblAnswer = new javax.swing.JLabel();
        tfdAnswer = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(java.awt.Color.white);
        setResizable(false);

        pnlTitle.setBackground(new java.awt.Color(55, 55, 55));

        lblStudent.setFont(new java.awt.Font("Lucida Grande", 0, 22)); // NOI18N
        lblStudent.setForeground(java.awt.Color.white);
        lblStudent.setText("Student");

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStudent)
                .addContainerGap(198, Short.MAX_VALUE))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBody.setBackground(new java.awt.Color(55, 55, 55));

        btnSubmit.setBackground(new java.awt.Color(55, 55, 55));
        btnSubmit.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSubmit.setText("Submit");
        btnSubmit.setPreferredSize(new java.awt.Dimension(85, 35));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnSignOut.setBackground(new java.awt.Color(55, 55, 55));
        btnSignOut.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSignOut.setText("Sign Out");
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutActionPerformed(evt);
            }
        });

        palInnerBody.setBackground(new java.awt.Color(55, 55, 55));
        palInnerBody.setLayout(new java.awt.GridLayout(2, 2, 0, 5));

        lblQuestion.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblQuestion.setForeground(java.awt.Color.white);
        lblQuestion.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblQuestion.setText("Question: ");
        palInnerBody.add(lblQuestion);

        tfdQuestion.setEditable(false);
        tfdQuestion.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        palInnerBody.add(tfdQuestion);

        lblAnswer.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblAnswer.setForeground(java.awt.Color.white);
        lblAnswer.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblAnswer.setText("Your Answer: ");
        palInnerBody.add(lblAnswer);

        tfdAnswer.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tfdAnswer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfdAnswerKeyPressed(evt);
            }
        });
        palInnerBody.add(tfdAnswer);

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addComponent(palInnerBody, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBodyLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSignOut, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(palInnerBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSignOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutActionPerformed
        //Log out the account
        client.logOut();
        this.setVisible(false);
        this.dispose();
        GuiLogin guiLogin = new GuiLogin();
        guiLogin.setVisible(true);
    }//GEN-LAST:event_btnSignOutActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
            send();
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void tfdAnswerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfdAnswerKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            send();
        }
    }//GEN-LAST:event_tfdAnswerKeyPressed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new GuiStudent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnSignOut;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel lblAnswer;
    private javax.swing.JLabel lblQuestion;
    private javax.swing.JLabel lblStudent;
    private javax.swing.JPanel palInnerBody;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JTextField tfdAnswer;
    private static javax.swing.JTextField tfdQuestion;
    // End of variables declaration//GEN-END:variables

}
