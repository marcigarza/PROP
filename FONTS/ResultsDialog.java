import javax.swing.*;
import java.awt.event.*;

/**
 * Clase para mostrar los resultados de una consulta
 * @author marina
 */

public class ResultsDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveBtn;
    private JButton exitBtn;
    private JTextArea textArea;

    /**
     * creadora de la clase
     * @param query identificador de la consulta realizada
     * @param result resultado a mostrar
     */
    private ResultsDialog(String query, String result) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveBtn);
        textArea.setText(result);
        textArea.setEditable(false);

        saveBtn.addActionListener(e -> saveResult(query, result));

        exitBtn.addActionListener(e -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * función para almacenar en persistencia los resultados de una consulta
     * @param query id de la consulta (futuro titulo del archivo)
     * @param result resultado de la cosnulta (contenido del archivo)
     */
    private void saveResult(String query, String result) {
        query = query.replace(" ","_");
        ControladorInterficie.singleton.saveResult(query, result);
        dispose();
        JOptionPane.showMessageDialog(this, "Resultado de la consulta guardado satisfactoriamente.\n Puede encontrarlo en la carpeta " +
                        "\"results\" junto al ejecutable.",
                "Éxito!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Metodo main de la clase
     * @param query identificador de la consulta realizada
     * @param result resultado a mostrar
     */
    public static void main(String query, String result) {
        ResultsDialog dialog = new ResultsDialog(query, result);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
