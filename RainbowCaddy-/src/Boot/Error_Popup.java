package Boot;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
   
    public Error_Popup(String title, String message, int x, int y, boolean allowClosing) {
        setType(Type.POPUP);
        setTitle(title);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(512, 256));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
       
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
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
    }

}
