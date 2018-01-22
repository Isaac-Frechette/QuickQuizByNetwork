package quickquizbynetworkClient;

import Managers.QuestionManager;
import Models.Question;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import quickquizbynetworkHost.ComponentLibrary;

/**
 * **************************************************************
 * PROGRAM: Quick Quiz By Network - Host AUTHOR: Isaac Frechette STUDENT ID:
 * 7105810416 DUE DATE: 17/11/2017
 *
 * FUNCTION: This program will act as a utility for students in the classroom.
 * It will allow them to answer pre-defined quiz questions sent by a teacher.
 *
 * INPUT: The program accepts input from the user and from the host program via
 * a network connection.
 *
 * OUTPUT: The only output from this program is sent to the host program.
 *
 * NOTES: Client program must be opened after the Host program to ensure a
 * connection between the two
***************************************************************
 */
public class QuizClient extends JFrame implements ActionListener, KeyListener {

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ClientThread client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    Question question;

    QuestionManager QM = new QuestionManager();
    SpringLayout myLayout = new SpringLayout();

    private JLabel lblBanner,
            lblTopic, lblQn, lblQnLabel, lblQnA, lblQnB, lblQnC, lblQnD,
            lblCorrectAns, lblConnection;

    private JTextArea //right panel
            txtTopic, txtQn, txtQnA, txtQnB, txtQnC, txtQnD,
            //main panel
            txtAns;

    private JButton btnExit, btnSend;

    public static void main(String[] args) {
        QuizClient QQBN = new QuizClient();
        QQBN.setLayout(QQBN.myLayout);
        QQBN.displayTextAreas();
        QQBN.displayLabels();
        QQBN.displayButtons();
        QQBN.run();
    }

    private void run() {
        //setting the frame in place
        int frameX = 450;
        int frameY = 650;
        Dimension screenD = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenD.width / 2 - frameX / 2), (screenD.height / 2 - frameY / 2), frameX, frameY);

        //Frame details
        setTitle("Quick Quiz By Network - Host");
        this.getContentPane().setBackground(new Color(190, 190, 190));
        this.setForeground(Color.GREEN);
        setResizable(false);
        setVisible(true);

        //window closing event registering
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        getParameters();
        connect(serverName, serverPort);
    }

    private void populateFields(Question q) {
        txtTopic.setText(q.getTopic());
        txtQn.setText(q.getQuestion());
        txtQnA.setText(q.getAnswerA());
        txtQnB.setText(q.getAnswerB());
        txtQnC.setText(q.getAnswerC());
        txtQnD.setText(q.getAnswerD());
        lblQnLabel.setText(Integer.toString(q.getQuestionNumber()));
    }

    //<editor-fold defaultstate="collapsed" desc="User Interface Builder"> 
    public void displayTextAreas() {
        //Textareas on the right
        txtTopic = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 10, 80, 80);
        txtQn = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 80, 130);
        txtQnA = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 80, 180);
        txtQnB = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 80, 230);
        txtQnC = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 80, 280);
        txtQnD = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 20, 80, 330);

        //Textareas in the in the center
        txtAns = ComponentLibrary.LocateAJTextArea(this, myLayout, 2, 5, 80, 420);

    }

    public void displayLabels() {
        //the header for the frame
        lblBanner = ComponentLibrary.LocateAJLabel(this, myLayout, "Quick Quiz by Network", Color.WHITE, null, 80, 0);
        lblBanner.setFont(new Font("Yu Gothic", 0, 28));

        //Labels on the right
        lblTopic = ComponentLibrary.LocateAJLabel(this, myLayout, "Topic:", Color.BLACK, null, 50, 60);
        lblQn = ComponentLibrary.LocateAJLabel(this, myLayout, "Qn:", Color.BLACK, null, 48, 130);
        lblQnLabel = ComponentLibrary.LocateAJLabel(this, myLayout, "0", Color.BLACK, null, 48, 145);
        lblQnA = ComponentLibrary.LocateAJLabel(this, myLayout, "A:", Color.BLACK, null, 50, 180);
        lblQnB = ComponentLibrary.LocateAJLabel(this, myLayout, "B:", Color.BLACK, null, 50, 230);
        lblQnC = ComponentLibrary.LocateAJLabel(this, myLayout, "C:", Color.BLACK, null, 50, 280);
        lblQnD = ComponentLibrary.LocateAJLabel(this, myLayout, "D:", Color.BLACK, null, 50, 330);
        lblCorrectAns = ComponentLibrary.LocateAJLabel(this, myLayout, "Ans:", Color.BLACK, null, 50, 400);
    }

    private void displayButtons() {
        btnExit = ComponentLibrary.LocateAJButton(this, myLayout, "Exit", this, 20, 495);
        btnSend = ComponentLibrary.LocateAJButton(this, myLayout, "Send", this, 100, 495);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Events"> 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSend) {
            send();
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

    public void connect(String serverName, int serverPort) {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void open() {
        try {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ClientThread(this, socket);
        } catch (IOException ioe) {
            System.out.println("Error opening output stream: " + ioe);
        }
    }

    private void send() {
        try {
            String a = "S;";
            if (txtAns.getText().equalsIgnoreCase(question.getCorrectAnswer())) {
                a += "true";
            } else {
                a += "false";
            }
            a += ";" + Integer.toString(question.getQuestionNumber());
            streamOut.writeUTF(a);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    public void close() {
        try {
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        client.close();
        client.stop();
    }

    public void handle(String msg) {
        if (msg.equals("true") || msg.equals("false")) {
            return;
        }
        String[] temp = msg.split(";");
        question = new Question(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7]);
        populateFields(question);
    }

    public void getParameters() {
//        serverName = getParameter("host");
//        serverPort = Integer.parseInt(getParameter("port"));

        serverName = "localhost";
        serverPort = 4444;
    }
}
