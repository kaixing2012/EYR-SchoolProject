package mathquiz.guis;

//Import AWT package
import java.awt.Color;
import java.awt.event.KeyEvent;

//Import Swing package
import javax.swing.JOptionPane;

//Import networks package
import mathquiz.networks.Client;

//Import objs package
import mathquiz.objs.Account;


//**********************************************************/
// Filename: GuiLogin.java
// Purpose:  To provide GUI and functions that teachers and 
//           students can login to the network
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mql-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class GuiLogin extends javax.swing.JFrame {
    
    //Declare variables for client side object
    private final Client client;
    
    //Declare variables for other frames
    private GuiInstructor teacher;
    private GuiStudent student;
    
    //Construction
    public GuiLogin() {
        
        //Locate the location of the server
        client = new Client("localhost", 1201);
        initComponents();
    }
    
    //**********************************************************/
    // Mothod:  private void login()
    // Purpose: To provide a function that allows signed user to
    //          login the system
    // Input:   null
    // Output:  void
    //**********************************************************/
    private void login() {  
        
        //Check if the text fields are filled in
        if(txfUsername.getText().length() > 0 && 
           pwdPassword.getPassword().length > 0){
            
            //Declare a user object to store username and passward
            Account user = new Account(txfUsername.getText(), new String(pwdPassword.getPassword()));
            //Declare a title going to be passed into other frome
            String frameTitle = txfUsername.getText().substring(0, 1).toUpperCase()
                              + txfUsername.getText().substring(1);
            
            //Check if connect to the server side
            if(!client.connect()){
                JOptionPane.showMessageDialog(null, "Has no server responsed!", "LOGIN MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                resetFields();
            }
            else{
                System.out.println("Connect");
                
                //Declare frame for teacher and student
                student = new GuiStudent(client, frameTitle);
                teacher = new GuiInstructor(client);
                
                //Add frame for teacher into a data structure 
                //that listens students who are online
                client.addUserStatusListener(teacher);
                
                //Check if the username and password are valided
                if(!client.login(user.getUsername(), user.getPassword())){  
                    client.logOut();
                    JOptionPane.showMessageDialog(null, "The accoount does NOT exist!", "LOGIN MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    resetFields(); 
                }
                //Do login
                else{              
                    resetFields();
                    this.dispose();
                    JOptionPane.showMessageDialog(null, "You are in!", "LOGIN MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    
                    //Check if user is teacher or student than lead to
                    //customised frame
                    if ("teacher".equalsIgnoreCase(client.getRole())){                                               
                        teacher.setVisible(true);
                    }
                    else{
                        student.setVisible(true);
                    }
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Please enter your Username and Password", "LOGIN MESSAGE", JOptionPane.ERROR_MESSAGE);
            resetFields();
        }
    }
    
    //**********************************************************/
    // Mothod:  private void resetFields()
    // Purpose: To reset fields
    // Input:   null
    // Output:  void
    //**********************************************************/
    private void resetFields() {
        txfUsername.setText("");
        pwdPassword.setText(""); 
        txfUsername.requestFocusInWindow();
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txfUsername = new javax.swing.JTextField();
        btnSignIn = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pwdPassword = new javax.swing.JPasswordField();
        btnSignUp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblLogin.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogin.setText("Login");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Username: ");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Password: ");

        txfUsername.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txfUsername.setPreferredSize(new java.awt.Dimension(11, 35));
        txfUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfUsernameKeyPressed(evt);
            }
        });

        btnSignIn.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnSignIn.setText("Sign In");
        btnSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignInActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        pwdPassword.setPreferredSize(new java.awt.Dimension(131, 32));
        pwdPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdPasswordKeyPressed(evt);
            }
        });

        btnSignUp.setForeground(new java.awt.Color(0, 0, 255));
        btnSignUp.setText("Sign Up");
        btnSignUp.setBorder(null);
        btnSignUp.setBorderPainted(false);
        btnSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSignUpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSignUpMouseExited(evt);
            }
        });
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pwdPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)))
                .addGap(31, 31, 31))
            .addComponent(btnSignUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSignIn)
                    .addComponent(btnCancel))
                .addGap(18, 18, 18)
                .addComponent(btnSignUp, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        //Do login 
        login();
    }//GEN-LAST:event_btnSignInActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txfUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfUsernameKeyPressed
        //Check if users press the enter key
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            login();
        }
    }//GEN-LAST:event_txfUsernameKeyPressed

    private void pwdPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdPasswordKeyPressed
        //Check if users press the enter key
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            login();
        }
    }//GEN-LAST:event_pwdPasswordKeyPressed

    private void btnSignUpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSignUpMouseEntered
        btnSignUp.setForeground(Color.GRAY);
    }//GEN-LAST:event_btnSignUpMouseEntered

    private void btnSignUpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSignUpMouseExited
        btnSignUp.setForeground(Color.BLUE);
    }//GEN-LAST:event_btnSignUpMouseExited

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        GuiSignUp signUp = new GuiSignUp();
        signUp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSignUpActionPerformed

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
            java.util.logging.Logger.getLogger(GuiLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        java.awt.EventQueue.invokeLater(() -> {
            new GuiLogin().setVisible(true);
        });
         
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables
 
}
