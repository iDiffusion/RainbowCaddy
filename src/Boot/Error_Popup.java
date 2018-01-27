package Boot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Error_Popup extends JFrame {
    private static final long serialVersionUID = 4013003667450751116L;
    private JPanel contentPane;
    private String message = "";
    private JTextArea textArea;
   
    public Error_Popup(String title, String message, int x, int y, boolean allowClosing) {
    	this.message = message;
        setType(Type.POPUP);
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(512, 256));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
       
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(new Font(Font.DIALOG, 10, 20));
        textArea.setWrapStyleWord(true);
        textArea.setText(message);
        contentPane.add(textArea, BorderLayout.CENTER);
        if (allowClosing) {
	        JButton button = new JButton("Close this");
	        button.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                dispose();
	            }
	        });
	        contentPane.add(button, BorderLayout.SOUTH);
        }
        pack();
        setSize(getWidth(), getHeight()  + textArea.getPreferredSize().height);
        setLocation(x - getWidth() / 2, y - getHeight() / 2);
        setVisible(true);
        toFront();

    }
    
    public void appendText(String message) {
    	this.message += message;
    	textArea.setText(this.message);
    }
    public void tempAppendText(String message) {
    	textArea.setText(this.message+message);
    }

}
