package combat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements ActionListener, KeyListener {

    public JTextField textField;
    String text = "";
    boolean next;


    public InputHandler(JTextField textField, boolean next) {

        this.textField = textField;
        this.next = next;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        text = textField.getText();
        textField.setText("");
        next = true;
        textField.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) { /*not used*/ }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_ENTER) {

            text = textField.getText();
            textField.setText("");
            next = true;
            textField.requestFocus();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { /*not used*/ }

}
