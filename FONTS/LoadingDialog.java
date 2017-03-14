import javax.swing.*;

/**
 * Dialog para mostrar una barra de progreso animada
 * @author Marina
 */

public class LoadingDialog extends JDialog {
    private JPanel contentPane;
    private JProgressBar progressBar;
    private static JFrame frame;

    private LoadingDialog() {
        setContentPane(contentPane);
        setModal(true);
    }

    /**
     * Función para cerrar el Dialog una vez terminada la operación de carga
     */
    public static void workFinished() {
        frame.dispose();
    }

    /**
     * metodo main de la clase
     */
    public static void main() {
        frame = new JFrame("LoadingDialog");
        frame.setContentPane(new LoadingDialog().contentPane);
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
