import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog para gestionar el login como admin
 * @author Marina
 */

public class LogDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordField1;
    private JLabel label;

    public LogDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Función para comprobar si la contraseña es correcta al clickar "OK"
     */
    private void onOK() {
        if(ControladorInterficie.singleton.comparePasswords(passwordField1.getPassword())){
            dispose();
        }
        else {
            label.setText("Contraseña incorrecta, la contraseña es: admin");
        }
    }

    private void onCancel(){
        dispose();
    }

    /**
     * Metodo main del Dialog
     */
    public static void main() {
        LogDialog dialog = new LogDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
