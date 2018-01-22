package quickquizbynetworkHost;

import Managers.QuestionManager;
import Models.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 * **************************************************************
 * PROGRAM: Quick Quiz By Network - Host AUTHOR: Isaac Frechette STUDENT ID:
 * 7105810416 DUE DATE: 17/11/2017
 *
 * FUNCTION: This program will act as a utility for teachers in the classroom.
 * It will allow them to send pre-defined quiz questions to students running the
 * client version of this program and collect statistics on answers given.
 *
 * INPUT: Input is gathered from a data file on the disk, user input into text
 * boxes in the program and input gathered via a networked connection to the
 * clients.
 *
 * OUTPUT: This program outputs statistics on questions asked and answers given
 * to the screen for the user to read as well as writing HashMap tables to the
 * disk.
 *
 * NOTES: Files on the disk are written to and read from the same directory as
 * the program.
***************************************************************
 */
public class QuizHost extends JFrame implements ActionListener, KeyListener, Runnable {

    private ServerThread clients[] = new ServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    private int port = 4444;

    QuestionManager QM = new QuestionManager();
    SpringLayout myLayout = new SpringLayout();

    DList linkedList = new DList();
    DListNode currentNode = linkedList.getHead();
    BinaryTree bTree = new BinaryTree();
    HashMap<String, Integer> hm = new HashMap<>();

    JTable table = new JTable();

    private JLabel lblBanner,
            lblTableTitle,
            lblOrderPre, lblOrder, lblOrderPost,
            lblTopic, lblQn, lblQnLabel, lblQnA, lblQnB, lblQnC, lblQnD,
            lblCorrectAns, lblLinkedList, lblBinaryTree;

    private JTextArea //right panel
            txtTopic, txtQn, txtQnA, txtQnB, txtQnC, txtQnD,
            //main panel
            txtAns, txtLinkedList, txtBinaryTree;

    private JButton //left Panel
            btnTopicSort, btnQuestionNoSort, btnQuestionSort,
            //main Panel
            btnExit, btnSend, btnDisplay,
            //binary tree ordering buttons
            btnPreOrderDisplay, btnPreOrderSave, btnInOrderDisplay, btnInOrderSave, btnPostOrderDisplay, btnPostOrderSave;

    private JPanel panBanner, panTable, panMiddle, panFooter;

    private JScrollPane scrollPane;

    String[] columnNames
            = {
                "#", "Topic", "Question", "A", "B", "C", "D"
            };

    public static void main(String[] args) {
        QuizHost QQBN = new QuizHost();
        QQBN.setLayout(QQBN.myLayout);
        QQBN.displayTable();
        QQBN.displayTextAreas();
        QQBN.displayLabels();
        QQBN.displayButtons();
        QQBN.startup();
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void startup() {
        try {
            server = new ServerSocket(port);
            System.out.println("Bound to port " + port + ", please wait  ...");
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }

        //setting the frame in place
        int frameX = 1000;
        int frameY = 800;
        Dimension screenD = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenD.width / 2 - frameX / 2), (screenD.height / 2 - frameY / 2), frameX, frameY);

        //Frame details
        setTitle("Quick Quiz By Network - Host");
        this.getContentPane().setBackground(new Color(190, 190, 190));
        setResizable(false);
        setVisible(true);

        //window closing event registering
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void SortByQuestionNumber(ArrayList<Object[]> arr) {
        //Selection Sort
        {
            int i, j, first;
            Object[] temp;
            for (i = arr.size() - 1; i > 0; i--) {
                first = 0; //initialize to subscript of first element
                for (j = 1; j <= i; j++) //locate smallest element between positions 1 and i.
                {
                    if (Integer.parseInt(arr.get(first)[0].toString()) < Integer.parseInt(arr.get(j)[0].toString())) {
                        first = j;
                    }
                }
                temp = arr.get(first); //swap smallest found with element in position i.
                arr.set(first, arr.get(i));
                arr.set(i, temp);
            }
            RebuildTable(arr);
        }
    }

    private void SortByTopic(ArrayList<Object[]> arr) {
        //Bubble Sort
        for (int j = 0; j < arr.size(); j++) {
            for (int i = j + 1; i < arr.size(); i++) {
                if ((arr.get(i)[1]).toString().compareToIgnoreCase(arr.get(j)[1].toString()) < 0) {
                    Object[] words = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, words);
                }
            }
        }
        RebuildTable(arr);
    }

    private void SortByQuestion(ArrayList<Object[]> arr) {
        //Insertion Sort
        int j; // the number of items sorted so far
        Object[] key; // the item to be inserted
        int i;

        for (j = 1; j < arr.size(); j++) {
            key = arr.get(j);
            for (i = j - 1; (i >= 0) && (key[2].toString().compareToIgnoreCase(arr.get(i)[2].toString()) < 0); i--)//values moving
            {
                arr.set(i + 1, arr.get(i));
            }
            arr.set(i + 1, key); // Put the key in its proper location;
        }
        RebuildTable(arr);
    }

    private void populateFields(Question q) {
        txtTopic.setText(q.getTopic());
        txtQn.setText(q.getQuestion());
        txtQnA.setText(q.getAnswerA());
        txtQnB.setText(q.getAnswerB());
        txtQnC.setText(q.getAnswerC());
        txtQnD.setText(q.getAnswerD());
        txtAns.setText(q.getCorrectAnswer());
        lblQnLabel.setText(Integer.toString(q.getQuestionNumber()));
    }

    private void RebuildTable(ArrayList<Object[]> list) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Object[] row : list) {
            Object[] o = new Object[]{
                row[0], row[1], row[2], row[3], row[4], row[5], row[6]
            };
            model.addRow(o);
        }
        table.setModel(model);
    }

    public void preOrderHashTree(Node focusNode) {
        if (focusNode != null) {
            hm.put(focusNode.getTopic(), focusNode.getQuestionNo());
            preOrderHashTree(focusNode.leftChild);
            preOrderHashTree(focusNode.rightChild);
        }
    }

    public void inOrderHashTree(Node focusNode) {
        if (focusNode != null) {
            inOrderHashTree(focusNode.leftChild);
            hm.put(focusNode.getTopic(), focusNode.getQuestionNo());
            inOrderHashTree(focusNode.rightChild);
        }
    }

    public void postOrderHashTree(Node focusNode) {
        if (focusNode != null) {
            postOrderHashTree(focusNode.leftChild);
            postOrderHashTree(focusNode.rightChild);
            hm.put(focusNode.getTopic(), focusNode.getQuestionNo());
        }
    }

    public void writeHashMapOut(String filename) {
        try {
            PrintWriter printFile = new PrintWriter(new FileWriter(filename + ".txt"));
            printFile.println(hm);
            printFile.close();
        } catch (Exception ex) {
            System.err.println("Error Writing File: " + ex.getMessage());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="User Interface Builder"> 

    private void displayTable() {
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVisible(true);
        add(scrollPane);

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (Question row : QM.getAllQuestions()) {
            Object[] o = new Object[]{
                row.getQuestionNumber(), row.getTopic(), row.getQuestion(), row.getAnswerA(), row.getAnswerB(), row.getAnswerC(), row.getAnswerD()
            };
            model.addRow(o);
        }
        myLayout.putConstraint(SpringLayout.WEST, scrollPane, 15, SpringLayout.WEST, this);
        myLayout.putConstraint(SpringLayout.NORTH, scrollPane, 55, SpringLayout.NORTH, this);
        table.setModel(model);
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            populateFields(QM.getByID(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        });
    }

    public void displayTextAreas() {
        //Textareas on the right
        txtTopic = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 10, 650, 80);
        txtQn = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 650, 130);
        txtQnA = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 650, 180);
        txtQnB = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 650, 230);
        txtQnC = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 650, 280);
        txtQnD = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 650, 330);

        //Textareas in the in the center
        txtAns = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 5, 600, 420);
        txtLinkedList = ComponentLibrary.LocateAJTextArea(this, myLayout, 4, 80, 20, 530);
        txtBinaryTree = ComponentLibrary.LocateAJTextArea(this, myLayout, 4, 80, 20, 640);
    }

    public void displayLabels() {
        //the header for the frame
        lblBanner = ComponentLibrary.LocateAJLabel(this, myLayout, "Quick Quiz by Network", Color.WHITE, null, 400, 0);
        lblBanner.setFont(new Font("Yu Gothic", 0, 28));

        //Labels in the footer
        lblOrderPre = ComponentLibrary.LocateAJLabel(this, myLayout, "Pre-Order", Color.WHITE, null, 250, 720);
        lblOrder = ComponentLibrary.LocateAJLabel(this, myLayout, "In-Order", Color.WHITE, null, 500, 720);
        lblOrderPost = ComponentLibrary.LocateAJLabel(this, myLayout, "Post-Order", Color.WHITE, null, 750, 720);

        //Labels on the left
        lblTableTitle = ComponentLibrary.LocateAJLabel(this, myLayout, "Quick Quiz Questions", Color.BLACK, null, 20, 30);

        //Labels on the right
        lblTopic = ComponentLibrary.LocateAJLabel(this, myLayout, "Topic:", Color.BLACK, null, 600, 80);
        lblQn = ComponentLibrary.LocateAJLabel(this, myLayout, "Qn:", Color.BLACK, null, 628, 130);
        lblQnLabel = ComponentLibrary.LocateAJLabel(this, myLayout, "0", Color.BLACK, null, 628, 145);
        lblQnA = ComponentLibrary.LocateAJLabel(this, myLayout, "A:", Color.BLACK, null, 630, 180);
        lblQnB = ComponentLibrary.LocateAJLabel(this, myLayout, "B:", Color.BLACK, null, 630, 230);
        lblQnC = ComponentLibrary.LocateAJLabel(this, myLayout, "C:", Color.BLACK, null, 630, 280);
        lblQnD = ComponentLibrary.LocateAJLabel(this, myLayout, "D:", Color.BLACK, null, 630, 330);
        lblCorrectAns = ComponentLibrary.LocateAJLabel(this, myLayout, "Correct Ans:", Color.BLACK, null, 595, 400);
        lblLinkedList = ComponentLibrary.LocateAJLabel(this, myLayout, "Linked List:", Color.BLACK, null, 20, 510);
        lblBinaryTree = ComponentLibrary.LocateAJLabel(this, myLayout, "Binary Tree:", Color.BLACK, null, 20, 620);
    }

    private void displayButtons() {
        //Buttons on the left
        btnQuestionNoSort = ComponentLibrary.LocateAJButton(this, myLayout, "Question #", this, 470, 140);
        btnTopicSort = ComponentLibrary.LocateAJButton(this, myLayout, "Topic", this, 470, 170);
        btnQuestionSort = ComponentLibrary.LocateAJButton(this, myLayout, "Question", this, 470, 200);

        //Buttons in the center
        btnExit = ComponentLibrary.LocateAJButton(this, myLayout, "Exit", this, 1, 745);
        btnSend = ComponentLibrary.LocateAJButton(this, myLayout, "Send", this, 750, 422);
        btnDisplay = ComponentLibrary.LocateAJButton(this, myLayout, "Display", this, 850, 610);

        //Buttons in the Footer
        btnPreOrderDisplay = ComponentLibrary.LocateAJButton(this, myLayout, "Display", this, 200, 740);
        btnPreOrderSave = ComponentLibrary.LocateAJButton(this, myLayout, "Save", this, 275, 740);
        btnInOrderDisplay = ComponentLibrary.LocateAJButton(this, myLayout, "Display", this, 450, 740);
        btnInOrderSave = ComponentLibrary.LocateAJButton(this, myLayout, "Save", this, 525, 740);
        btnPostOrderDisplay = ComponentLibrary.LocateAJButton(this, myLayout, "Display", this, 720, 740);
        btnPostOrderSave = ComponentLibrary.LocateAJButton(this, myLayout, "Save", this, 795, 740);
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Events"> 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTopicSort) {
            ArrayList<Object[]> questionList = new ArrayList<>();

            for (Question row : QM.getAllQuestions()) {
                Object[] o = new Object[]{
                    row.getQuestionNumber(), row.getTopic(), row.getQuestion(), row.getAnswerA(), row.getAnswerB(), row.getAnswerC(), row.getAnswerD()
                };
                questionList.add(o);
            }
            SortByTopic(questionList);
        }

        if (e.getSource() == btnQuestionSort) {
            ArrayList<Object[]> questionList = new ArrayList<>();

            for (Question row : QM.getAllQuestions()) {
                Object[] o = new Object[]{
                    row.getQuestionNumber(), row.getTopic(), row.getQuestion(), row.getAnswerA(), row.getAnswerB(), row.getAnswerC(), row.getAnswerD()
                };
                questionList.add(o);
            }
            SortByQuestion(questionList);
        }

        if (e.getSource() == btnQuestionNoSort) {
            ArrayList<Object[]> questionList = new ArrayList<>();

            for (Question row : QM.getAllQuestions()) {
                Object[] o = new Object[]{
                    row.getQuestionNumber(), row.getTopic(), row.getQuestion(), row.getAnswerA(), row.getAnswerB(), row.getAnswerC(), row.getAnswerD()
                };
                questionList.add(o);
            }
            SortByQuestionNumber(questionList);
        }

        if (e.getSource() == btnSend) {
            DListNode node = new DListNode(txtTopic.getText(), lblQnLabel.getText());

            currentNode.append(node);
            currentNode = node;
            txtLinkedList.setText(linkedList.print());
            handle(4444, lblQnLabel.getText() + ";" + txtTopic.getText() + ";" + txtQn.getText() + ";" + txtQnA.getText() + ";" + txtQnB.getText() + ";" + txtQnC.getText() + ";" + txtQnD.getText() + ";" + txtAns.getText());
            bTree.addNode(Integer.parseInt(lblQnLabel.getText()), txtTopic.getText());
        }

        if (e.getSource() == btnPreOrderDisplay) {
            txtBinaryTree.setText(bTree.preorderTraverseTree(bTree.root));
        }

        if (e.getSource() == btnInOrderDisplay) {
            txtBinaryTree.setText(bTree.inOrderTraverseTree(bTree.root));
        }

        if (e.getSource() == btnPostOrderDisplay) {
            txtBinaryTree.setText(bTree.postOrderTraverseTree(bTree.root));
        }

        if (e.getSource() == btnPreOrderSave) {
            hm = new HashMap<String, Integer>();
            preOrderHashTree(bTree.root);
            writeHashMapOut("PreOrderHashMap");
        }
        if (e.getSource() == btnInOrderSave) {
            hm = new HashMap<String, Integer>();
            inOrderHashTree(bTree.root);
            writeHashMapOut("InOrderHashMap");
        }

        if (e.getSource() == btnPostOrderSave) {
            hm = new HashMap<String, Integer>();
            postOrderHashTree(bTree.root);
            writeHashMapOut("PostOrderHashMap");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Networking Methods"> 
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

    public synchronized void handle(int ID, String input) {
        String[] temp = input.split(";");

        //S at begining of string represents a student sent this
        if (temp[0].equals("S")) {
            switch (temp[1]) {
                case "true":
                    Node tempNode = bTree.findNode(Integer.parseInt(temp[2]));
                    tempNode.setCorrectCount(tempNode.getCorrectCount() + 1);
                    break;

                case "false":
                    break;
            }
        } else {
            for (int i = 0; clients[i] != null; i++) {
                clients[i].send(input);
            }
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    //add a send method that sends a whole question object
    //</editor-fold>
}
