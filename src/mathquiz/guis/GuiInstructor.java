package mathquiz.guis;

//**********************************************************/
// Filename: GuiInstructor.java
// Purpose:  To provide GUI and functions that teacher can 
//           send students message in math quiz and receive 
//           answer from the students
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqi-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/

//Import Util package
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

//Import Swing package
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

//Import AWT package
import java.awt.event.KeyEvent;

//Import Networks package
import mathquiz.networks.Client;
import mathquiz.networks.IMessageListener;
import mathquiz.networks.IUserStatusListener;

//Import Objs package
import mathquiz.objs.SortByQuiz;
import mathquiz.objs.SortByAnswer;
import mathquiz.objs.SortByOperator;

//Import Methods package
import mathquiz.methods.FileManager;
import mathquiz.methods.SortAlgorithms;

//Import Third-Party libraries
import hans.BinaryTree;
import org.apache.commons.lang3.StringUtils;

public class GuiInstructor extends javax.swing.JFrame implements IMessageListener, IUserStatusListener{
    
    //Declare variables for layout
    private TableColumnModel quizTCM;
    private DefaultTableModel quizDTM;
    private DefaultListModel userDLM;
    private DefaultTableCellRenderer valueCenterDTCR;
    private DefaultListCellRenderer valueColorDLCR;
    
    //Declare variables for collections 
    private List<SortByQuiz> sbqList;
    private List<SortByOperator> sboList;
    private List<SortByAnswer> preList;
    private List<SortByAnswer> inList;
    private List<SortByAnswer> postList;
    private List<SortByOperator> wrongAnswerList;
    
    //Declare third party libraries
    private static BinaryTree<SortByAnswer> quizTree;
    
    //Declare variable for connectivity
    private Client client;
   
    private GuiInstructor() {
        
    }
    
    //Construction
    public GuiInstructor(Client c) {
        
        String[] columnNames = new String[]{"First Figure", "Operator", "Second Figure", "Equal", "Answer"};
        
        quizDTM = new DefaultTableModel();
        quizDTM.setColumnIdentifiers(columnNames);  
        
        userDLM = new DefaultListModel();
        
        sbqList = new LinkedList<>();
        sboList = new ArrayList<>();
        quizTree = new BinaryTree<>();
        wrongAnswerList = new LinkedList<>();
        
        //Assign client side from the input parameter
        client = c;
        //Add this frame to a data structure 
        //that listens message from the other side
        client.addMessageListener(this);
               
        initComponents();
        setTableLayout();
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
        //Check if there is nothing other than y and n 
        if("y".equalsIgnoreCase(msgBody.trim()) |
           "n".equalsIgnoreCase(msgBody.trim())){
            
            if("n".equalsIgnoreCase(msgBody.trim()))
            {
                //If the message back is a unexpected result then 
                //collect these data to a custom list
                wrongAnswerList.add(sboList.get(sboList.size() - 1)); 
                btnSend.setEnabled(true);
            } 

            //Initialise variables of lists
            preList = new ArrayList<>();
            inList = new ArrayList<>();
            postList = new ArrayList<>();

            //Display data on the table
            displayDataOnTable(sboList);

            //Display data on the text area
            displayDataOnTextArea("NON-ORDER: " + textFomation(sboList, "binaryTree"), "binaryTree");

            //Sort data with sprcific order required
            quizTree.preOrder(quizTree.getRoot(), preList);
            quizTree.inOrder(quizTree.getRoot(), inList);
            quizTree.postOrder(quizTree.getRoot(), postList);

            //Enable the send buttons
            btnSend.setEnabled(true);
            btnSendAll.setEnabled(true);
        }
    }
    
    //**********************************************************/
    // Mothod:  public void online(String login)
    // Purpose: To listen client who is online
    // Input:   String
    // Output:  void
    //**********************************************************/
    @Override
    public void online(String login) {
        userDLM.addElement(login);
    }
    
    //**********************************************************/
    // Mothod:  public void offline(String login)
    // Purpose: To listen client who is offline
    // Input:   String
    // Output:  void
    //**********************************************************/
    @Override
    public void offline(String login) {
        userDLM.removeElement(login);
    }
    
    //**********************************************************/
    // Mothod:  private void send(String outTo)
    // Purpose: To send message out to the other side of network
    // Input:   String
    // Output:  void
    //**********************************************************/
    private void send(String outTo) {
        
        //Gather elements needed and assign them to variables
        String fNum = tfdFirstFigure.getText().trim();
        String operator = cbxOperator.getSelectedItem().toString().trim();
        String sNum = tfdSecondFigure.getText().trim(); 
            
        try{
            //Add quiz to objects for different usage
            sbqList.add(new SortByQuiz(fNum, operator, sNum));
            sboList.add(new SortByOperator(fNum, operator, sNum));
            quizTree.add(new SortByAnswer(fNum, operator, sNum));

            //Assign the last quiz in the collection to the variable
            //that is about to be sent out
            String msgBody = sboList.get(sboList.size() - 1).toString();
            //Locate the side to send to
            String sendTo = outTo;
            client.send(sendTo, msgBody);

            //Empty fields
            tfdFirstFigure.setText("");
            tfdSecondFigure.setText("");  

            //Display answer
            tfdAnswer.setText(sboList.get(sboList.size() - 1).getAnswer());
            cbxOperator.setSelectedIndex(0);

            //Disable the send buttons 
            btnSend.setEnabled(false);
            btnSendAll.setEnabled(false);
        }
        //Catch if non-numeric value is inserted
        catch(NumberFormatException e){
            String value = "";
            
            if (! StringUtils.isNumeric(fNum) &
                ! StringUtils.isNumeric(sNum)){
                value = "Both values <" + fNum + ">" + " and " + "<" + sNum + ">";
            }
            else if(! StringUtils.isNumeric(fNum)){
                value = "First value <" + fNum + ">";
            }
            else if(! StringUtils.isNumeric(sNum)){
                value = "Second value <" + sNum + ">";
            }
            
            JOptionPane.showMessageDialog(null, value + " is or are not numeric value!", "WRONG INPUT ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //**********************************************************/
    // Mothod:  public final void setTableLayout()
    // Purpose: To set up the layout of table collection quiz 
    //          asked
    // Input:   null
    // Output:  void
    //**********************************************************/
    public final void setTableLayout(){
          
        quizTCM = tblQuiz.getColumnModel();
        quizTCM.getColumn(0).setPreferredWidth(110);
        quizTCM.getColumn(1).setPreferredWidth(30);
        quizTCM.getColumn(2).setPreferredWidth(110);
        quizTCM.getColumn(3).setPreferredWidth(30);
        quizTCM.getColumn(4).setPreferredWidth(110);
        
        valueCenterDTCR = new DefaultTableCellRenderer();
        valueCenterDTCR.setHorizontalAlignment(SwingConstants.CENTER);  
        
        //Set default to keep value in the cernter of each cell.
        for (int i = 0; i < quizTCM.getColumnCount(); i++){
            quizTCM.getColumn(i).setCellRenderer(valueCenterDTCR);
        }  
    }
    
    //**********************************************************/
    // Mothod:  public static void 
    //          displayDataOnTable (Collection c)
    // Purpose: To display data on a table by inputing a  
    //          cllection and run with Iterator through 
    //          each record.
    // Input:   Collection c
    // Output:  void
    //**********************************************************/
    public void displayDataOnTable (Collection c)
    {
        Iterator iterator = c.iterator(); 
        
        for (int i = quizDTM.getRowCount() - 1; i >= 0; i--){
            quizDTM.removeRow(i);
        }
        
        //Empty the table
        while (iterator.hasNext())
        {
            Object[] objArray = iterator.next().toString().split(" ");
            quizDTM.addRow(objArray);
        }  
    }
    
    //**********************************************************/
    // Mothod:  public static void 
    //          displayDataOnTextArea(String data, String area)
    // Purpose: To display data on a text area by inputing a  
    //          string type of data and an area defined to run 
    //          a specific function required.
    // Input:   String data, String area
    // Output:  void
    //**********************************************************/
    public static void displayDataOnTextArea(String data, String area)
    {
        switch (area)
        {
            case "linkedList":
                tarLinkedList.setText(data);
                break;
                
            case "binaryTree":
                tarBinaryTree.setText(data);
                break;
                
            default:
                break;
        }  
    }
    
    //**********************************************************/
    // Mothod:  public static String 
    //          textFomation (Collection c, String formatType)
    // Purpose: To format text in a costomised formation 
    //          for a named area and its data structure 
    //          required.
    // Input:   Collection c, String formatType
    // Output:  String
    //**********************************************************/
    public static String textFomation (Collection c, String formatType)
    {
        Iterator iterator = c.iterator();
        int quizCount = 0;
        
        //To generate a string if the type of formation is
        //equal to "linkedList"
        if (formatType.equals("linkedList"))
        {
            String wrongAnswerStr = "";
            
            while (iterator.hasNext())
            {   
                if(quizCount != 0)
                    wrongAnswerStr += " --- ";

                wrongAnswerStr += "< ";
                wrongAnswerStr += iterator.next().toString();
                wrongAnswerStr += " >";
                quizCount ++;
            }
            
            return wrongAnswerStr;
        }

        //To generate a string if the type of formation is
        //equal to "binaryTree"
        else if (formatType.equals("binaryTree"))
        {
            String binaryTreeOrderStr = "";
            
            while (iterator.hasNext())
            {   
                String[] strArray = iterator.next().toString().split(" ");
                
                if(quizCount != 0)
                    binaryTreeOrderStr += ", ";
            
                binaryTreeOrderStr += strArray[strArray.length - 1];
                binaryTreeOrderStr += "(" + strArray[0] + " " + strArray[1] + 
                                      " " + strArray[2] + ")";
                quizCount ++;
            }
            
            return binaryTreeOrderStr;
        }
        
        //To generate a string if the type of formation is
        //equal to "writeCSVToFile"
        else if (formatType.equals("writeCSVToFile"))
        {
            String writeToFileStr = "";
            
            while (iterator.hasNext())
            {   
                String[] strArray = iterator.next().toString().split(" ");
                
                if(quizCount != 0)
                    writeToFileStr += "\n";
            
                writeToFileStr += strArray[strArray.length - 1] + ",="
                                + "," + strArray[0] + "," + strArray[1] + "," 
                                + strArray[2];
                quizCount ++;
            }
            
            return writeToFileStr;
        }       
        return null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        pnlTitle = new javax.swing.JPanel();
        lblInstructor = new javax.swing.JLabel();
        pnlEnterQuiz = new javax.swing.JPanel();
        pnlEnterQuizTitle = new javax.swing.JPanel();
        lblEnterQuiz = new javax.swing.JLabel();
        pnlEnterQuizBody = new javax.swing.JPanel();
        btnSend = new javax.swing.JButton();
        pnlEnterQuizInnerBodyLeft = new javax.swing.JPanel();
        lblFirstFigure = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        lblSecondFigure = new javax.swing.JLabel();
        lblAnswer = new javax.swing.JLabel();
        pnlEnterQuizInnerBodyRight = new javax.swing.JPanel();
        tfdFirstFigure = new javax.swing.JTextField();
        cbxOperator = new javax.swing.JComboBox<>();
        tfdSecondFigure = new javax.swing.JTextField();
        tfdAnswer = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnSendAll = new javax.swing.JButton();
        pnlQuizArr = new javax.swing.JPanel();
        pnlQuizArrTitle = new javax.swing.JPanel();
        lblQuizArr = new javax.swing.JLabel();
        pnlQuizArrBody = new javax.swing.JPanel();
        spnlQuizTable = new javax.swing.JScrollPane();
        tblQuiz = new javax.swing.JTable();
        btnSignOut = new javax.swing.JButton();
        pnlSort = new javax.swing.JPanel();
        lblSort = new javax.swing.JLabel();
        btnMarginSortAsc = new javax.swing.JButton();
        btnQuickSortDesc = new javax.swing.JButton();
        btnInsertionSortAsc = new javax.swing.JButton();
        pnlLinkedList = new javax.swing.JPanel();
        pnlLinkedListTitle = new javax.swing.JPanel();
        lblLinkedList = new javax.swing.JLabel();
        btnDispalyLinkedList = new javax.swing.JButton();
        pnlLinkedListBody = new javax.swing.JPanel();
        spnlLinkedListTar = new javax.swing.JScrollPane();
        tarLinkedList = new javax.swing.JTextArea();
        pnlBinaryTree = new javax.swing.JPanel();
        pnlBinaryTreeTitle = new javax.swing.JPanel();
        lblBinaryTreeTitle = new javax.swing.JLabel();
        pnlBinaryTreeBody = new javax.swing.JPanel();
        spnlBinaryTreeTar = new javax.swing.JScrollPane();
        tarBinaryTree = new javax.swing.JTextArea();
        pnlButton = new javax.swing.JPanel();
        pnlPreOrder = new javax.swing.JPanel();
        pnlPreOrderTitle = new javax.swing.JPanel();
        lblPreOrderTitle = new javax.swing.JLabel();
        pnlPreOrderBtn = new javax.swing.JPanel();
        btnPreDisplay = new javax.swing.JButton();
        btnPreSave = new javax.swing.JButton();
        pnlBlankLeft = new javax.swing.JPanel();
        pnlBlankLeftInside = new javax.swing.JPanel();
        pnlInOrder = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lblSort2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        btnInDisplay = new javax.swing.JButton();
        btnInSave = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        lblSort3 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        btnPostDisplay = new javax.swing.JButton();
        btnPostSave = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lisUserOnline = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        lblStudents = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pnlTitle.setBackground(new java.awt.Color(55, 55, 55));

        lblInstructor.setFont(new java.awt.Font("Lucida Grande", 0, 22)); // NOI18N
        lblInstructor.setForeground(java.awt.Color.white);
        lblInstructor.setText("Instructor");

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInstructor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInstructor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlEnterQuiz.setBackground(java.awt.Color.white);

        pnlEnterQuizTitle.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizTitle.setPreferredSize(new java.awt.Dimension(350, 37));

        lblEnterQuiz.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblEnterQuiz.setForeground(java.awt.Color.white);
        lblEnterQuiz.setText("Enter question, then click Send");

        javax.swing.GroupLayout pnlEnterQuizTitleLayout = new javax.swing.GroupLayout(pnlEnterQuizTitle);
        pnlEnterQuizTitle.setLayout(pnlEnterQuizTitleLayout);
        pnlEnterQuizTitleLayout.setHorizontalGroup(
            pnlEnterQuizTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterQuizTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEnterQuiz, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlEnterQuizTitleLayout.setVerticalGroup(
            pnlEnterQuizTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterQuizTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEnterQuiz, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlEnterQuizBody.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizBody.setPreferredSize(new java.awt.Dimension(350, 231));

        btnSend.setBackground(new java.awt.Color(55, 55, 55));
        btnSend.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSend.setText("Send");
        btnSend.setPreferredSize(new java.awt.Dimension(80, 35));
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

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

        lblAnswer.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblAnswer.setForeground(java.awt.Color.white);
        lblAnswer.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblAnswer.setText("Answer: ");
        pnlEnterQuizInnerBodyLeft.add(lblAnswer);

        pnlEnterQuizInnerBodyRight.setBackground(new java.awt.Color(55, 55, 55));
        pnlEnterQuizInnerBodyRight.setLayout(new java.awt.GridLayout(4, 1));

        tfdFirstFigure.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tfdFirstFigure.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfdFirstFigureKeyPressed(evt);
            }
        });
        pnlEnterQuizInnerBodyRight.add(tfdFirstFigure);

        cbxOperator.setBackground(java.awt.Color.white);
        cbxOperator.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cbxOperator.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+", "-", "x", "÷" }));
        pnlEnterQuizInnerBodyRight.add(cbxOperator);

        tfdSecondFigure.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tfdSecondFigure.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfdSecondFigureKeyPressed(evt);
            }
        });
        pnlEnterQuizInnerBodyRight.add(tfdSecondFigure);

        tfdAnswer.setEditable(false);
        tfdAnswer.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        pnlEnterQuizInnerBodyRight.add(tfdAnswer);

        btnSearch.setBackground(new java.awt.Color(55, 55, 55));
        btnSearch.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.setPreferredSize(new java.awt.Dimension(80, 35));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnSendAll.setBackground(new java.awt.Color(55, 55, 55));
        btnSendAll.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSendAll.setText("Send All");
        btnSendAll.setPreferredSize(new java.awt.Dimension(80, 35));
        btnSendAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEnterQuizBodyLayout = new javax.swing.GroupLayout(pnlEnterQuizBody);
        pnlEnterQuizBody.setLayout(pnlEnterQuizBodyLayout);
        pnlEnterQuizBodyLayout.setHorizontalGroup(
            pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterQuizBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEnterQuizBodyLayout.createSequentialGroup()
                        .addComponent(pnlEnterQuizInnerBodyLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlEnterQuizBodyLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(pnlEnterQuizInnerBodyRight, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnterQuizBodyLayout.createSequentialGroup()
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSendAll, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlEnterQuizBodyLayout.setVerticalGroup(
            pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnterQuizBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEnterQuizInnerBodyLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEnterQuizInnerBodyRight, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlEnterQuizBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlEnterQuizLayout = new javax.swing.GroupLayout(pnlEnterQuiz);
        pnlEnterQuiz.setLayout(pnlEnterQuizLayout);
        pnlEnterQuizLayout.setHorizontalGroup(
            pnlEnterQuizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEnterQuizLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlEnterQuizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlEnterQuizBody, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(pnlEnterQuizTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)))
        );
        pnlEnterQuizLayout.setVerticalGroup(
            pnlEnterQuizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterQuizLayout.createSequentialGroup()
                .addComponent(pnlEnterQuizTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEnterQuizBody, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlQuizArr.setBackground(java.awt.Color.white);

        pnlQuizArrTitle.setBackground(new java.awt.Color(55, 55, 55));
        pnlQuizArrTitle.setPreferredSize(new java.awt.Dimension(350, 37));

        lblQuizArr.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblQuizArr.setForeground(java.awt.Color.white);
        lblQuizArr.setText("Array of questions asked");

        javax.swing.GroupLayout pnlQuizArrTitleLayout = new javax.swing.GroupLayout(pnlQuizArrTitle);
        pnlQuizArrTitle.setLayout(pnlQuizArrTitleLayout);
        pnlQuizArrTitleLayout.setHorizontalGroup(
            pnlQuizArrTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuizArrTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQuizArr, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlQuizArrTitleLayout.setVerticalGroup(
            pnlQuizArrTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuizArrTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQuizArr, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlQuizArrBody.setBackground(new java.awt.Color(55, 55, 55));

        tblQuiz.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblQuiz.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tblQuiz.setModel(quizDTM);
        tblQuiz.setGridColor(java.awt.Color.black);
        tblQuiz.setRowHeight(26);
        tblQuiz.setTableHeader(null);
        spnlQuizTable.setViewportView(tblQuiz);

        btnSignOut.setBackground(new java.awt.Color(55, 55, 55));
        btnSignOut.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btnSignOut.setText("Sign Out");
        btnSignOut.setPreferredSize(new java.awt.Dimension(80, 35));
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutActionPerformed(evt);
            }
        });

        pnlSort.setBackground(new java.awt.Color(55, 55, 55));
        pnlSort.setLayout(new java.awt.GridLayout(1, 0));

        lblSort.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        lblSort.setForeground(java.awt.Color.white);
        lblSort.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSort.setText("Sort:");
        lblSort.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlSort.add(lblSort);

        btnMarginSortAsc.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnMarginSortAsc.setText("MSort");
        btnMarginSortAsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarginSortAscActionPerformed(evt);
            }
        });
        pnlSort.add(btnMarginSortAsc);

        btnQuickSortDesc.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnQuickSortDesc.setText("QSort");
        btnQuickSortDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuickSortDescActionPerformed(evt);
            }
        });
        pnlSort.add(btnQuickSortDesc);

        btnInsertionSortAsc.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnInsertionSortAsc.setText("ISort");
        btnInsertionSortAsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertionSortAscActionPerformed(evt);
            }
        });
        pnlSort.add(btnInsertionSortAsc);

        javax.swing.GroupLayout pnlQuizArrBodyLayout = new javax.swing.GroupLayout(pnlQuizArrBody);
        pnlQuizArrBody.setLayout(pnlQuizArrBodyLayout);
        pnlQuizArrBodyLayout.setHorizontalGroup(
            pnlQuizArrBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuizArrBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlQuizArrBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnlQuizTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlQuizArrBodyLayout.createSequentialGroup()
                        .addComponent(pnlSort, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSignOut, javax.swing.GroupLayout.PREFERRED_SIZE, 94, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlQuizArrBodyLayout.setVerticalGroup(
            pnlQuizArrBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuizArrBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlQuizTable, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlQuizArrBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSignOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlQuizArrLayout = new javax.swing.GroupLayout(pnlQuizArr);
        pnlQuizArr.setLayout(pnlQuizArrLayout);
        pnlQuizArrLayout.setHorizontalGroup(
            pnlQuizArrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlQuizArrTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
            .addComponent(pnlQuizArrBody, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlQuizArrLayout.setVerticalGroup(
            pnlQuizArrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuizArrLayout.createSequentialGroup()
                .addComponent(pnlQuizArrTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlQuizArrBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlLinkedList.setBackground(java.awt.Color.white);
        pnlLinkedList.setPreferredSize(new java.awt.Dimension(0, 150));

        pnlLinkedListTitle.setBackground(new java.awt.Color(55, 55, 55));
        pnlLinkedListTitle.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        pnlLinkedListTitle.setPreferredSize(new java.awt.Dimension(350, 37));

        lblLinkedList.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblLinkedList.setForeground(java.awt.Color.white);
        lblLinkedList.setText("Linked List (of all incorrectly answered exercises):");

        btnDispalyLinkedList.setBackground(new java.awt.Color(55, 55, 55));
        btnDispalyLinkedList.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btnDispalyLinkedList.setText("Display");
        btnDispalyLinkedList.setPreferredSize(new java.awt.Dimension(80, 10));
        btnDispalyLinkedList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDispalyLinkedListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLinkedListTitleLayout = new javax.swing.GroupLayout(pnlLinkedListTitle);
        pnlLinkedListTitle.setLayout(pnlLinkedListTitleLayout);
        pnlLinkedListTitleLayout.setHorizontalGroup(
            pnlLinkedListTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinkedListTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLinkedList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDispalyLinkedList, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlLinkedListTitleLayout.setVerticalGroup(
            pnlLinkedListTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinkedListTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLinkedListTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLinkedList, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btnDispalyLinkedList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlLinkedListBody.setBackground(new java.awt.Color(55, 55, 55));

        tarLinkedList.setEditable(false);
        tarLinkedList.setColumns(20);
        tarLinkedList.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tarLinkedList.setLineWrap(true);
        tarLinkedList.setRows(5);
        spnlLinkedListTar.setViewportView(tarLinkedList);

        javax.swing.GroupLayout pnlLinkedListBodyLayout = new javax.swing.GroupLayout(pnlLinkedListBody);
        pnlLinkedListBody.setLayout(pnlLinkedListBodyLayout);
        pnlLinkedListBodyLayout.setHorizontalGroup(
            pnlLinkedListBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinkedListBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlLinkedListTar)
                .addContainerGap())
        );
        pnlLinkedListBodyLayout.setVerticalGroup(
            pnlLinkedListBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLinkedListBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlLinkedListTar, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlLinkedListLayout = new javax.swing.GroupLayout(pnlLinkedList);
        pnlLinkedList.setLayout(pnlLinkedListLayout);
        pnlLinkedListLayout.setHorizontalGroup(
            pnlLinkedListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLinkedListBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLinkedListTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
        );
        pnlLinkedListLayout.setVerticalGroup(
            pnlLinkedListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLinkedListLayout.createSequentialGroup()
                .addComponent(pnlLinkedListTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLinkedListBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBinaryTree.setBackground(java.awt.Color.white);
        pnlBinaryTree.setPreferredSize(new java.awt.Dimension(0, 150));

        pnlBinaryTreeTitle.setBackground(new java.awt.Color(55, 55, 55));
        pnlBinaryTreeTitle.setPreferredSize(new java.awt.Dimension(350, 37));

        lblBinaryTreeTitle.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblBinaryTreeTitle.setForeground(java.awt.Color.white);
        lblBinaryTreeTitle.setText("Binary Tree (of all questions - added in the order that they were asked):");

        javax.swing.GroupLayout pnlBinaryTreeTitleLayout = new javax.swing.GroupLayout(pnlBinaryTreeTitle);
        pnlBinaryTreeTitle.setLayout(pnlBinaryTreeTitleLayout);
        pnlBinaryTreeTitleLayout.setHorizontalGroup(
            pnlBinaryTreeTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBinaryTreeTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBinaryTreeTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBinaryTreeTitleLayout.setVerticalGroup(
            pnlBinaryTreeTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBinaryTreeTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBinaryTreeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBinaryTreeBody.setBackground(new java.awt.Color(55, 55, 55));

        tarBinaryTree.setEditable(false);
        tarBinaryTree.setColumns(20);
        tarBinaryTree.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        tarBinaryTree.setLineWrap(true);
        tarBinaryTree.setRows(5);
        spnlBinaryTreeTar.setViewportView(tarBinaryTree);

        javax.swing.GroupLayout pnlBinaryTreeBodyLayout = new javax.swing.GroupLayout(pnlBinaryTreeBody);
        pnlBinaryTreeBody.setLayout(pnlBinaryTreeBodyLayout);
        pnlBinaryTreeBodyLayout.setHorizontalGroup(
            pnlBinaryTreeBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBinaryTreeBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlBinaryTreeTar)
                .addContainerGap())
        );
        pnlBinaryTreeBodyLayout.setVerticalGroup(
            pnlBinaryTreeBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBinaryTreeBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spnlBinaryTreeTar, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlBinaryTreeLayout = new javax.swing.GroupLayout(pnlBinaryTree);
        pnlBinaryTree.setLayout(pnlBinaryTreeLayout);
        pnlBinaryTreeLayout.setHorizontalGroup(
            pnlBinaryTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBinaryTreeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addComponent(pnlBinaryTreeBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBinaryTreeLayout.setVerticalGroup(
            pnlBinaryTreeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBinaryTreeLayout.createSequentialGroup()
                .addComponent(pnlBinaryTreeTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlBinaryTreeBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlButton.setLayout(new java.awt.GridLayout(1, 5));

        pnlPreOrder.setBackground(java.awt.Color.white);
        pnlPreOrder.setLayout(new java.awt.GridLayout(2, 0));

        pnlPreOrderTitle.setBackground(new java.awt.Color(55, 55, 55));

        lblPreOrderTitle.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        lblPreOrderTitle.setForeground(java.awt.Color.white);
        lblPreOrderTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreOrderTitle.setText("Pre-Order");
        lblPreOrderTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlPreOrderTitleLayout = new javax.swing.GroupLayout(pnlPreOrderTitle);
        pnlPreOrderTitle.setLayout(pnlPreOrderTitleLayout);
        pnlPreOrderTitleLayout.setHorizontalGroup(
            pnlPreOrderTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPreOrderTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPreOrderTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPreOrderTitleLayout.setVerticalGroup(
            pnlPreOrderTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPreOrderTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPreOrderTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlPreOrder.add(pnlPreOrderTitle);

        pnlPreOrderBtn.setBackground(new java.awt.Color(55, 55, 55));
        pnlPreOrderBtn.setLayout(new java.awt.GridLayout(1, 0));

        btnPreDisplay.setText("Display");
        btnPreDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreDisplayActionPerformed(evt);
            }
        });
        pnlPreOrderBtn.add(btnPreDisplay);

        btnPreSave.setText("Save");
        btnPreSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreSaveActionPerformed(evt);
            }
        });
        pnlPreOrderBtn.add(btnPreSave);

        pnlPreOrder.add(pnlPreOrderBtn);

        pnlButton.add(pnlPreOrder);

        pnlBlankLeft.setBackground(java.awt.Color.white);

        pnlBlankLeftInside.setBackground(new java.awt.Color(55, 55, 55));

        javax.swing.GroupLayout pnlBlankLeftInsideLayout = new javax.swing.GroupLayout(pnlBlankLeftInside);
        pnlBlankLeftInside.setLayout(pnlBlankLeftInsideLayout);
        pnlBlankLeftInsideLayout.setHorizontalGroup(
            pnlBlankLeftInsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );
        pnlBlankLeftInsideLayout.setVerticalGroup(
            pnlBlankLeftInsideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBlankLeftLayout = new javax.swing.GroupLayout(pnlBlankLeft);
        pnlBlankLeft.setLayout(pnlBlankLeftLayout);
        pnlBlankLeftLayout.setHorizontalGroup(
            pnlBlankLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
            .addGroup(pnlBlankLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBlankLeftLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlBlankLeftInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlBlankLeftLayout.setVerticalGroup(
            pnlBlankLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
            .addGroup(pnlBlankLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlBlankLeftInside, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlButton.add(pnlBlankLeft);

        pnlInOrder.setBackground(java.awt.Color.white);
        pnlInOrder.setLayout(new java.awt.GridLayout(2, 0));

        jPanel11.setBackground(new java.awt.Color(55, 55, 55));

        lblSort2.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        lblSort2.setForeground(java.awt.Color.white);
        lblSort2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSort2.setText("In-Order");
        lblSort2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSort2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSort2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlInOrder.add(jPanel11);

        jPanel20.setBackground(new java.awt.Color(55, 55, 55));
        jPanel20.setLayout(new java.awt.GridLayout(1, 0));

        btnInDisplay.setText("Display");
        btnInDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInDisplayActionPerformed(evt);
            }
        });
        jPanel20.add(btnInDisplay);

        btnInSave.setText("Save");
        btnInSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInSaveActionPerformed(evt);
            }
        });
        jPanel20.add(btnInSave);

        pnlInOrder.add(jPanel20);

        pnlButton.add(pnlInOrder);

        jPanel7.setBackground(java.awt.Color.white);

        jPanel18.setBackground(new java.awt.Color(55, 55, 55));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlButton.add(jPanel7);

        jPanel8.setBackground(java.awt.Color.white);
        jPanel8.setLayout(new java.awt.GridLayout(2, 0));

        jPanel13.setBackground(new java.awt.Color(55, 55, 55));

        lblSort3.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        lblSort3.setForeground(java.awt.Color.white);
        lblSort3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSort3.setText("Post-Order");
        lblSort3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSort3, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSort3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.add(jPanel13);

        jPanel21.setBackground(new java.awt.Color(55, 55, 55));
        jPanel21.setLayout(new java.awt.GridLayout(1, 0));

        btnPostDisplay.setText("Display");
        btnPostDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostDisplayActionPerformed(evt);
            }
        });
        jPanel21.add(btnPostDisplay);

        btnPostSave.setText("Save");
        btnPostSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostSaveActionPerformed(evt);
            }
        });
        jPanel21.add(btnPostSave);

        jPanel8.add(jPanel21);

        pnlButton.add(jPanel8);

        jPanel1.setBackground(new java.awt.Color(55, 55, 55));

        lisUserOnline.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        lisUserOnline.setModel(userDLM);
        lisUserOnline.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lisUserOnlineKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(lisUserOnline);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(55, 55, 55));

        lblStudents.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lblStudents.setForeground(java.awt.Color.white);
        lblStudents.setText("Students");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlEnterQuiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlQuizArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnlLinkedList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addComponent(pnlBinaryTree, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlEnterQuiz, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlQuizArr, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLinkedList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBinaryTree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        //Check if there is no other client or user on the network
        if(!userDLM.isEmpty()){ 
            if (! tfdFirstFigure.getText().equals("") &
                ! tfdSecondFigure.getText().equals("") &
                ! lisUserOnline.isSelectionEmpty()){  

                send(lisUserOnline.getSelectedValue());
                lisUserOnline.clearSelection();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Fill in 1st and 2nd Number!\n and select a student to send", 
                        "Enter Question", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "No students are online!", "CONNECTIONS", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnSignOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutActionPerformed
        
        //Log out the account
        client.logOut();
        this.setVisible(false);
        this.dispose();
        GuiLogin guiLogin = new GuiLogin();
        guiLogin.setVisible(true);
    }//GEN-LAST:event_btnSignOutActionPerformed
    
    @SuppressWarnings("unchecked")
    private void btnMarginSortAscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarginSortAscActionPerformed
       displayDataOnTable(SortAlgorithms.mergeSort(sboList));
    }//GEN-LAST:event_btnMarginSortAscActionPerformed
    
    @SuppressWarnings("unchecked")
    private void btnQuickSortDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuickSortDescActionPerformed
        SortAlgorithms.quickSort(sboList);
        displayDataOnTable(sboList);
    }//GEN-LAST:event_btnQuickSortDescActionPerformed
    
    @SuppressWarnings("unchecked")
    private void btnInsertionSortAscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertionSortAscActionPerformed
        SortAlgorithms.insertionSortAsc(sboList);
        displayDataOnTable(sboList);
    }//GEN-LAST:event_btnInsertionSortAscActionPerformed

    private void btnDispalyLinkedListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDispalyLinkedListActionPerformed
        displayDataOnTextArea(textFomation(wrongAnswerList, "linkedList"), "linkedList");
    }//GEN-LAST:event_btnDispalyLinkedListActionPerformed

    private void btnPreDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreDisplayActionPerformed
        
        //Formation for a string using for puting into display function
        String displayStr = "PRE-ORDER: " + textFomation(quizTree.getPreList(), "binaryTree");
        displayDataOnTextArea(displayStr, "binaryTree");
    }//GEN-LAST:event_btnPreDisplayActionPerformed

    private void btnInDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInDisplayActionPerformed
        
        //Formation for a string using for puting into display function
        String displayStr = "IN-ORDER: " + textFomation(quizTree.getInList(), "binaryTree");
        displayDataOnTextArea(displayStr, "binaryTree");
    }//GEN-LAST:event_btnInDisplayActionPerformed

    private void btnPostDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostDisplayActionPerformed
        
        //Formation for a string using for puting into display function
        String displayStr = "POST-ORDER: " + textFomation(quizTree.getPostList(), "binaryTree");
        displayDataOnTextArea(displayStr, "binaryTree");
    }//GEN-LAST:event_btnPostDisplayActionPerformed

    private void btnPreSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreSaveActionPerformed
        
        //Declare an address for saving data 
        String path = "text files/PreOrder.csv";
        
        //Format data
        String data = textFomation(quizTree.getPreList(), "writeCSVToFile");
        
        //Empty the file if there are data whithin
        FileManager.emptyOutFile(path);
        
        //Write data to the file and return boolean if the data are saved
        if (FileManager.writeToFile(data + "\n", path))
            JOptionPane.showMessageDialog(null, "File Saved" , "Saving", JOptionPane.INFORMATION_MESSAGE);
        
        else
            JOptionPane.showMessageDialog(null, "Fail to Save" , "Saving", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnPreSaveActionPerformed

    private void btnInSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInSaveActionPerformed
        
        //Declare an address for saving data 
        String path = "text files/InOrder.csv";
        
         //Format data
        String data = textFomation(quizTree.getInList(), "writeCSVToFile");
        
        //Empty the file if there are data whithin
        FileManager.emptyOutFile(path);
        
        //Write data to the file and return boolean if the data are saved
        if (FileManager.writeToFile(data + "\n", path))
            JOptionPane.showMessageDialog(null, "File Saved" , "Saving", JOptionPane.INFORMATION_MESSAGE);
        
        else
            JOptionPane.showMessageDialog(null, "Fail to Save" , "Saving", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnInSaveActionPerformed

    private void btnPostSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostSaveActionPerformed
        
        //Declare an address for saving data 
        String path = "text files/PostOrder.csv";
        
        //Format data
        String data = textFomation(quizTree.getPostList(), "writeCSVToFile");
        
        //Empty the file if there are data whithin
        FileManager.emptyOutFile(path);
        
        //Write data to the file and return boolean if the data are saved
        if (FileManager.writeToFile(data + "\n", path))
            JOptionPane.showMessageDialog(null, "File Saved" , "Saving", JOptionPane.INFORMATION_MESSAGE);
        
        else
            JOptionPane.showMessageDialog(null, "Fail to Save" , "Saving", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnPostSaveActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        
        //Declare a frame for searching quiz
        GuiSeeker searchFrame = new GuiSeeker(sbqList);
        
        //Display the frame
        searchFrame.setVisible(true);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tfdFirstFigureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfdFirstFigureKeyPressed
        //Check if the user press enter key
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            //Check if no other client or user on the network
            if(!userDLM.isEmpty()){ 
                if (! tfdFirstFigure.getText().equals("") &
                    ! tfdSecondFigure.getText().equals("") &
                    ! lisUserOnline.isSelectionEmpty()){  

                    send(lisUserOnline.getSelectedValue());
                    lisUserOnline.clearSelection();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Fill in 1st and 2nd Number!\n and select a student to send", 
                            "Enter Question", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "No students are online!", "CONNECTIONS", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_tfdFirstFigureKeyPressed

    private void tfdSecondFigureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfdSecondFigureKeyPressed
        //Check if the user press enter key
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){   
            //Check if no other client or user on the network
            if(!userDLM.isEmpty()){ 
                if (! tfdFirstFigure.getText().equals("") &
                    ! tfdSecondFigure.getText().equals("") &
                    ! lisUserOnline.isSelectionEmpty()){  

                    send(lisUserOnline.getSelectedValue());
                    lisUserOnline.clearSelection();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Fill in 1st and 2nd Number!\n and select a student to send", 
                            "Enter Question", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "No students are online!", "CONNECTIONS", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_tfdSecondFigureKeyPressed

    private void btnSendAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendAllActionPerformed
        //Check if no other client or user on the network
        if(!userDLM.isEmpty()){ 
            if (! tfdFirstFigure.getText().equals("") &
                ! tfdSecondFigure.getText().equals("")){  

                send("#class");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Fill in 1st and 2nd Number!", 
                        "Enter Question", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "No students are online!", "CONNECTIONS", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSendAllActionPerformed

    private void lisUserOnlineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lisUserOnlineKeyPressed
        //Check if the user press enter key
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){  
            //Check if no other client or user on the network
            if(!userDLM.isEmpty()){ 
                if (! tfdFirstFigure.getText().equals("") &
                    ! tfdSecondFigure.getText().equals("") &
                    ! lisUserOnline.isSelectionEmpty()){  

                    send(lisUserOnline.getSelectedValue());
                    lisUserOnline.clearSelection();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Fill in 1st and 2nd Number!\n and select a student to send", 
                            "Enter Question", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "No students are online!", "CONNECTIONS", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_lisUserOnlineKeyPressed

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
            java.util.logging.Logger.getLogger(GuiInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiInstructor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new GuiInstructor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDispalyLinkedList;
    private javax.swing.JButton btnInDisplay;
    private javax.swing.JButton btnInSave;
    private javax.swing.JButton btnInsertionSortAsc;
    private javax.swing.JButton btnMarginSortAsc;
    private javax.swing.JButton btnPostDisplay;
    private javax.swing.JButton btnPostSave;
    private javax.swing.JButton btnPreDisplay;
    private javax.swing.JButton btnPreSave;
    private javax.swing.JButton btnQuickSortDesc;
    private static javax.swing.JButton btnSearch;
    private static javax.swing.JButton btnSend;
    private static javax.swing.JButton btnSendAll;
    private static javax.swing.JButton btnSignOut;
    private javax.swing.JComboBox<String> cbxOperator;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnswer;
    private javax.swing.JLabel lblBinaryTreeTitle;
    private javax.swing.JLabel lblEnterQuiz;
    private javax.swing.JLabel lblFirstFigure;
    private javax.swing.JLabel lblInstructor;
    private javax.swing.JLabel lblLinkedList;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblPreOrderTitle;
    private javax.swing.JLabel lblQuizArr;
    private javax.swing.JLabel lblSecondFigure;
    private javax.swing.JLabel lblSort;
    private javax.swing.JLabel lblSort2;
    private javax.swing.JLabel lblSort3;
    private javax.swing.JLabel lblStudents;
    private javax.swing.JList<String> lisUserOnline;
    private javax.swing.JPanel pnlBinaryTree;
    private javax.swing.JPanel pnlBinaryTreeBody;
    private javax.swing.JPanel pnlBinaryTreeTitle;
    private javax.swing.JPanel pnlBlankLeft;
    private javax.swing.JPanel pnlBlankLeftInside;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlEnterQuiz;
    private javax.swing.JPanel pnlEnterQuizBody;
    private javax.swing.JPanel pnlEnterQuizInnerBodyLeft;
    private javax.swing.JPanel pnlEnterQuizInnerBodyRight;
    private javax.swing.JPanel pnlEnterQuizTitle;
    private javax.swing.JPanel pnlInOrder;
    private javax.swing.JPanel pnlLinkedList;
    private javax.swing.JPanel pnlLinkedListBody;
    private javax.swing.JPanel pnlLinkedListTitle;
    private javax.swing.JPanel pnlPreOrder;
    private javax.swing.JPanel pnlPreOrderBtn;
    private javax.swing.JPanel pnlPreOrderTitle;
    private javax.swing.JPanel pnlQuizArr;
    private javax.swing.JPanel pnlQuizArrBody;
    private javax.swing.JPanel pnlQuizArrTitle;
    private javax.swing.JPanel pnlSort;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JScrollPane spnlBinaryTreeTar;
    private javax.swing.JScrollPane spnlLinkedListTar;
    private javax.swing.JScrollPane spnlQuizTable;
    private static javax.swing.JTextArea tarBinaryTree;
    private static javax.swing.JTextArea tarLinkedList;
    private javax.swing.JTable tblQuiz;
    private javax.swing.JTextField tfdAnswer;
    private javax.swing.JTextField tfdFirstFigure;
    private javax.swing.JTextField tfdSecondFigure;
    // End of variables declaration//GEN-END:variables

    
}
