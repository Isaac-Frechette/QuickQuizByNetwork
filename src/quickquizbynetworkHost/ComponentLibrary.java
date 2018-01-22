package quickquizbynetworkHost;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class ComponentLibrary {

    public static JLabel LocateAJLabel(JFrame myJFrame, SpringLayout layout, String JLabelCaption, Color JLabelForgroundColor, Color JLabelBackgroundColor, int x, int y) {
        JLabel myJLabel = new JLabel(JLabelCaption);
        myJLabel.setForeground(JLabelForgroundColor);
        myJLabel.setBackground(JLabelBackgroundColor);
        myJFrame.add(myJLabel);
        layout.putConstraint(SpringLayout.WEST, (Component)myJLabel, x, SpringLayout.WEST, myJFrame);
        layout.putConstraint(SpringLayout.NORTH, (Component)myJLabel, y, SpringLayout.NORTH, myJFrame);
        return myJLabel;
    }

    public static JTextArea LocateAJTextArea(JFrame myJFrame, SpringLayout layout, int rows, int columns, int x, int y) {
        JTextArea myJTextArea = new JTextArea(rows, columns);
        myJFrame.add(myJTextArea);
        layout.putConstraint(SpringLayout.WEST, (Component)myJTextArea, x, SpringLayout.WEST, myJFrame);
        layout.putConstraint(SpringLayout.NORTH, (Component)myJTextArea, y, SpringLayout.NORTH, myJFrame);
        myJTextArea.setLineWrap(true);
        return myJTextArea;
    }

    public static JPanel LocateAJPanel(JFrame myJFrame, Color JFrameColor) {
        JPanel panel = new JPanel();
        panel.setBackground(JFrameColor);
        return panel;
    }
    
    public static JButton LocateAJButton(JFrame myJFrame, SpringLayout layout, String JButtonTxt,ActionListener myActnLstnr, int x, int y)
    {
        JButton myJButton = new JButton(JButtonTxt);
        myJButton.addActionListener(myActnLstnr);
        myJFrame.add(myJButton);
        layout.putConstraint(SpringLayout.WEST, (Component)myJButton, x, SpringLayout.WEST, myJFrame);
        layout.putConstraint(SpringLayout.NORTH, (Component)myJButton, y, SpringLayout.NORTH, myJFrame);
        return myJButton;
    }
}