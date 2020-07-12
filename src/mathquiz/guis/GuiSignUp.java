package mathquiz.guis;

//Import AWT package
import java.awt.event.KeyEvent;

//Import Swing package
import javax.swing.JOptionPane;

//Import SQL package
import java.sql.ResultSet;
import java.sql.SQLException;

//Import DBC package
import mathquiz.dbc.DBClient;
import mathquiz.dbc.IDataCollector;

//Import Objes package
import mathquiz.objs.Account;
import mathquiz.objs.User;

//**********************************************************/
// Filename: GuiSignUp.java
// Purpose:  To provide GUI and functions the end-user can
//           sign up thier user accounts
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqs-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class GuiSignUp extends javax.swing.JFrame implements IDataCollector{

    //Declare variables for DBC
    private final String url;
    private final String usr;
    private final String pwd;
    
    //Declare variables for boolean
    private boolean isUserInputed = false;
    private boolean isAccountInputed = false;
    
    //Declare variables to store user info
    private User user;
    private Account account;

    //Constructor
    public GuiSignUp() {
        
        //DBC info
        this.url = "jdbc:mysql://localhost/math_quiz?"
                 + "useSSL=false&useUnicode=true&"
                 + "&useLegacyDatetimeCode=false&"
                 + "serverTimezone=UTC";
        this.usr = "root";
        this.pwd = "hiphop17"; 
        
        initComponents();
    }
    
    //**********************************************************/
    // Mothod:  public boolean retrieve(ResultSet rs, 
    //                                  String queryType)
    // Purpose: To retrieve data from database through the 
    //          DBClientManager defined for database connectivity.
    //          Overriding here is to customise functionality
    // Input:   ResuiltSet, String
    // Output:  boolean
    //**********************************************************/
    @Override
    public boolean retrieve(ResultSet rs, String queryType) {
        try {
            //Give a queryType to be a condition to run this 
            //method
            if("users".equalsIgnoreCase(queryType)){
                
                //Retrieve data to the class object
                user.setUserID(rs.getInt("user_id")); 
                
                //Return true of false back to 
                //DataClientManager function
                return true;
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }        
        return false;
    }
    
    //**********************************************************/
    // Mothod:  private void signUp()
    // Purpose: To collect user registration info and insert 
    //          those data into the database
    // Input:   none
    // Output:  void
    //**********************************************************/
    private void signUp() {  
        //Check if all fields are filled in
        if(txfFirstName.getText().length() > 0 &&
           txfSecondName.getText().length() > 0 &&  
           txfUsername.getText().length() > 0 && 
           txfPassword.getText().length() > 0){
            
            try {
                //Grab data for user object
                String fName = txfFirstName.getText();
                String lName = txfSecondName.getText();
                String role = cbxYourRole.getSelectedItem().toString();
                
                //Grab data for account object
                String username = txfUsername.getText();
                String password = txfPassword.getText();
                
                //Login to the database
                DBClient client = new DBClient(url, usr, pwd);
                
                //Insert user object into database with sql
                String sql = "INSERT INTO users(first_name, second_name, role) VALUE(?, ?, ?)";
                user = new User(fName, lName, role);
                if(client.manipulate(user, sql)){
                    isUserInputed = true;
                }
                
                //Retrieve user id that link to acount table in database
                sql = "SELECT user_id FROM users WHERE first_name = '" + user.getFirstName() +
                      "' AND second_name = '" + user.getSecondName() + "'";
                String queryType = "users";
                client.setDataCollector(this);
                client.setQueryType(queryType);
                client.query(sql);
                
                //Insert user account object into database with 
                //the user id retrived and the sql
                sql = "INSERT INTO accounts(username, password, user_id) VALUE(?, ?, ?)";
                account = new Account(username, password);
                account.setUserID(user.getUserID());
                if(client.manipulate(account, sql)){
                    isAccountInputed = true;
                }
                
                //Check if user and account table are inserted with data
                if(!isUserInputed && isAccountInputed){
                    JOptionPane.showMessageDialog(null, "Opps...You didn't sign up!", "SIGN UP MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    resetFields();
                }
                //If yes. Then, close the frome for signing and pup up the login frame
                else{
                    JOptionPane.showMessageDialog(null, "Welcome " + fName + " " + lName, "SIGN UP MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    GuiLogin login = new GuiLogin();
                    login.setVisible(true);
                    this.dispose();
                }
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Please enter ALL field and select your role!", "LOGIN MESSAGE", JOptionPane.ERROR_MESSAGE);
            resetFields();
        }
    }
    
    //**********************************************************/
    // Mothod:  private void resetFields()
    // Purpose: To empty all fields after action
    // Input:   none
    // Output:  void
    //**********************************************************/
    private void resetFields() {
        txfFirstName.setText("");
        txfSecondName.setText("");
        cbxYourRole.setSelectedIndex(0);
        txfUsername.setText("");
        txfPassword.setText("");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSecondName = new javax.swing.JLabel();
        txfFirstName = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        lblLogin = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnSignUp = new javax.swing.JButton();
        txfSecondName = new javax.swing.JTextField();
        txfUsername = new javax.swing.JTextField();
        lblRole = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        txfPassword = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        cbxYourRole = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblSecondName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblSecondName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSecondName.setText("Second name:");

        txfFirstName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txfFirstName.setPreferredSize(new java.awt.Dimension(11, 35));
        txfFirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfFirstNameKeyPressed(evt);
            }
        });

        lblFirstName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblFirstName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblFirstName.setText("First name:");

        lblLogin.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogin.setText("Sign up");

        btnCancel.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(97, 29));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSignUp.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        btnSignUp.setText("Sign up");
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        txfSecondName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txfSecondName.setPreferredSize(new java.awt.Dimension(11, 35));
        txfSecondName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfSecondNameKeyPressed(evt);
            }
        });

        txfUsername.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txfUsername.setPreferredSize(new java.awt.Dimension(11, 35));
        txfUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfUsernameKeyPressed(evt);
            }
        });

        lblRole.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblRole.setText("Your role:");

        lblUsername.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblUsername.setText("Username:");

        txfPassword.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txfPassword.setPreferredSize(new java.awt.Dimension(11, 35));
        txfPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfPasswordKeyPressed(evt);
            }
        });

        lblPassword.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPassword.setText("Password:");

        cbxYourRole.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        cbxYourRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select...", "teacher", "student" }));
        cbxYourRole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxYourRoleKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFirstName)
                            .addComponent(lblSecondName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txfSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblRole)
                                    .addComponent(lblUsername))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                    .addComponent(cbxYourRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txfSecondName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRole, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxYourRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        signUp();
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        GuiLogin login = new GuiLogin();
        this.dispose();
        login.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txfFirstNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfFirstNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            signUp();
        }
    }//GEN-LAST:event_txfFirstNameKeyPressed

    private void txfSecondNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfSecondNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            signUp();
        }
    }//GEN-LAST:event_txfSecondNameKeyPressed

    private void cbxYourRoleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxYourRoleKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            signUp();
        }
    }//GEN-LAST:event_cbxYourRoleKeyPressed

    private void txfUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfUsernameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            signUp();
        }
    }//GEN-LAST:event_txfUsernameKeyPressed

    private void txfPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfPasswordKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            signUp();
        }
    }//GEN-LAST:event_txfPasswordKeyPressed

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
            java.util.logging.Logger.getLogger(GuiSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiSignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiSignUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JComboBox<String> cbxYourRole;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSecondName;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txfFirstName;
    private javax.swing.JTextField txfPassword;
    private javax.swing.JTextField txfSecondName;
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables
  
}
