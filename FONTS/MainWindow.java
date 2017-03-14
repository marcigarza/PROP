import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Clase de la ventana principal de la aplicación
 * @author Marina
 */
public class MainWindow {
    private JButton selectedBtn;
    private static JFrame frame;

    /**
     * Creadora de la clase
     */
    public MainWindow() {
        tabbedPane1.addChangeListener(e -> panelChange());
        logBtn.addActionListener(e -> logOut());
        exitBtn.addActionListener(e -> System.exit(0));
        exitBtnU.addActionListener(e -> System.exit(0));
        userPanel.registerKeyboardAction(e -> consultBtn.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        titlesBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            titlesBtn.setSelected(true);
            selectedBtn = titlesBtn;
            loadTitlesScreen();
        });
        prefixBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            prefixBtn.setSelected(true);
            selectedBtn = prefixBtn;
            loadPrefixScreen();
        });
        contentBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            contentBtn.setSelected(true);
            selectedBtn = contentBtn;
            loadContentScreen();
        });
        compareBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            compareBtn.setSelected(true);
            selectedBtn = compareBtn;
            loadCompareScreen();
        });
        queryBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            queryBtn.setSelected(true);
            selectedBtn = queryBtn;
            loadQueryScreen();
        });
        addDocBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            addDocBtn.setSelected(true);
            selectedBtn = addDocBtn;
            loadAddDocumentScreen();
        });
        delDocBtn.addActionListener(e -> {
            selectedBtn.setSelected(false);
            delDocBtn.setSelected(true);
            selectedBtn = delDocBtn;
            loadDelDocumentScreen();
        });
        modDocBtn.addActionListener(e-> {
            selectedBtn.setSelected(false);
            modDocBtn.setSelected(true);
            selectedBtn = modDocBtn;
            loadModDocumentScreen();
        });
        indexadoBinarioRadioButton.addActionListener(e-> {
            if(indexadoBinarioRadioButton.isSelected()){
                indexadoTFIDFRadioButton.setSelected(false);
                changeIndexerType(0);
            }
            else indexadoBinarioRadioButton.setSelected(true);
        });
        indexadoTFIDFRadioButton.addActionListener(e-> {
            if(indexadoTFIDFRadioButton.isSelected()){
                indexadoBinarioRadioButton.setSelected(false);
                changeIndexerType(1);
            }
            else indexadoTFIDFRadioButton.setSelected(true);
        });
        indexadoBinarioRadioButton.setSelected(true);

        authorsCB.addActionListener(e -> populateTitles(titlesCB, (String)authorsCB.getSelectedItem()));
        adminAuthorNameCB.addActionListener(e -> populateTitles(adminDocTitleCB, (String)adminAuthorNameCB.getSelectedItem()));

        selectedBtn = titlesBtn;
        hideParamScreen();
        loadTitlesScreen();
        optionsPanel.setBackground(Color.lightGray);
        adminOptionsPanel.setBackground(Color.lightGray);
        checkSystemUsability();
    }

    /**
     * función privada para cambiar de indexador
     * @param indexer id del indexador
     */
    private void changeIndexerType(int indexer){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    ControladorInterficie.singleton.setIndexerType(indexer);
                } catch (SystemResetException e1) {
                    LoadingDialog.workFinished();
                    JOptionPane.showMessageDialog(frame, "Indexador cambiado con exito",
                            "Atención!", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        });
    }

    /**
     * función privada para inicializar el sistema.
     */
    private void checkSystemUsability() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(()-> {
                boolean aux = ControladorInterficie.singleton.isSystemEmpty();
                LoadingDialog.workFinished();
                if (aux) {
                    tabbedPane1.setSelectedIndex(1);
                }
                else {
                    populateAuthor(authorsCB);
                    populateAuthor(adminAuthorNameCB);
                    loadTitlesScreen();
                }
            });
        });
    }

    /**
     * metodo main de la clase
     */
    public static void main() {
        frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    /**
     * Función privada para gestionar el cmabio de paneles de usuario y administrador
     */
    public void panelChange(){
        if(tabbedPane1.getSelectedIndex() == 1){
            logBtn.setVisible(false);
            selectedBtn.setSelected(false);
            selectedBtn = addDocBtn;
            selectedBtn.setSelected(true);
            loadAddDocumentScreen();
            if(ControladorInterficie.singleton.isAdmin()){
                logBtn.setVisible(true);
            }
            else{
                javax.swing.SwingUtilities.invokeLater(
                        () -> {
                            LogDialog.main();
                            if (ControladorInterficie.singleton.isAdmin()) logBtn.setVisible(true);
                            else {
                                tabbedPane1.setSelectedIndex(0);
                            }
                        }
                );

            }
        }
        else{
            selectedBtn.setSelected(false);
            selectedBtn = titlesBtn;
            selectedBtn.setSelected(true);
            loadTitlesScreen();
        }
    }

    /**
     * Función privada para cerrar la sesión de administrador
     */
    private void logOut(){
        ControladorInterficie.singleton.logOut();
        tabbedPane1.setSelectedIndex(0);
    }

    /**
     * Función privada para añadir un documento al sistema
     */
    private void addDocument(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                Triplet doc = new Triplet(adminAuthorNameField.getText(),
                        adminDocTitleField.getText(), adminDocContentField.getText());
                if (doc.getTitulo().equals("")) {
                    LoadingDialog.workFinished();
                    JOptionPane.showMessageDialog(frame, "Introduzca un título válido.",
                            "Atención!", JOptionPane.WARNING_MESSAGE);
                }
                else if (doc.getAutor().equals("")) {
                    LoadingDialog.workFinished();
                    JOptionPane.showMessageDialog(frame, "Introduzca un autor válido.",
                            "Atención!", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    try {
                        ControladorInterficie.singleton.addDocument(doc);
                        LoadingDialog.workFinished();
                        JOptionPane.showMessageDialog(frame, "Documento creado satisfactoriamente.",
                                "Éxito!", JOptionPane.INFORMATION_MESSAGE);
                        adminAuthorNameField.setText("");
                        adminDocContentField.setText("");
                        adminDocTitleField.setText("");

                    } catch (ItemExistanceException e) {
                        LoadingDialog.workFinished();
                        JOptionPane.showMessageDialog(frame, "La combinación de autor y titulo ya existe en el sistema.",
                                "Error!", JOptionPane.WARNING_MESSAGE);
                    }
                }

            });
        });
    }

    /**
     * Función privada para eliminar un documento del sistema
     */
    private void deleteDocument(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                String author = (String)adminAuthorNameCB.getSelectedItem();
                String title = (String)adminDocTitleCB.getSelectedItem();
                boolean authorEliminated = ControladorInterficie.singleton.deleteDocument(author, title);
                if(authorEliminated) {
                    adminAuthorNameCB.removeItem(author);
                    authorsCB.removeItem(author);
                }
                LoadingDialog.workFinished();
                JOptionPane.showMessageDialog(frame, "Se ha eliminado el documento correctamente",
                        "Atención!", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }

    /**
     * Función privada para modificar un documento del sistema
     */
    private void modifyDocument(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                String author = (String)adminAuthorNameCB.getSelectedItem();
                String title = (String)adminDocTitleCB.getSelectedItem();
                String content = adminDocContentField.getText();
                ControladorInterficie.singleton.editDocument(title, author, content);
                LoadingDialog.workFinished();
                JOptionPane.showMessageDialog(frame, "Se ha modificado el documento correctamente",
                        "Atención!", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }

    /**
     * Función privada para gestionar la consulta de titulos de un autor
     */
    private void obtainTitles(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(()-> {
                String titlesResult = "Los títlos del autor "+authorsCB.getSelectedItem()+" presentes en el sistema son:\n";
                int i =1;
                for (String doc : ControladorDominio.singleton.getAllTitlesFromAuthor((String)authorsCB.getSelectedItem())) {
                    titlesResult += i+++"-"+doc + "\n";
                }
                final String param = titlesResult;
                LoadingDialog.workFinished();
                javax.swing.SwingUtilities.invokeLater(() -> ResultsDialog.main("titulos_"+authorsCB.getSelectedItem(), param));
            });
        });
    }

    /**
     * Función privada para gestionar la consulta dle listado de autores con un prefijo concreto
     */
    private void obtainAuthorsPrefix(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(()-> {
                if(ControladorDominio.singleton.getAllAuthorsWithPrefix(prefixField.getText()).size() == 0){
                    JOptionPane.showMessageDialog(frame, "No hay ningún autor con dicho prefijo",
                            "Atención!", JOptionPane.WARNING_MESSAGE);
                }
                else{
                    int i = 1;
                    String prefixResult = "Los autores que comienzan por el prefijo '"+prefixField.getText()+"' son:\n";
                    for (String authorM : ControladorDominio.singleton.getAllAuthorsWithPrefix(prefixField.getText())) {
                        prefixResult += i+++"- "+authorM+"\n";
                    }
                    final String param = prefixResult;
                    LoadingDialog.workFinished();
                    javax.swing.SwingUtilities.invokeLater(() -> ResultsDialog.main("autores_con_prefijo_"+prefixField.getText(), param));
                }
            });
        });
    }

    /**
     * Función privada para gestionar la consulta del contenido de un documento
     */
    private void obtainDocContent(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                final String aux = "El contenido del documento '"+titlesCB.getSelectedItem()+"', del autor '"+
                        authorsCB.getSelectedItem()+"' es: \n\n"+
                        ControladorDominio.singleton.getDocumentContent((String)titlesCB.getSelectedItem(),
                                (String)authorsCB.getSelectedItem());
                LoadingDialog.workFinished();
                javax.swing.SwingUtilities.invokeLater(() -> ResultsDialog.main("contenido_"+titlesCB.getSelectedItem()+"_de_"+
                        authorsCB.getSelectedItem(), aux));
            });
        });
    }

    /**
     * Función privada para gestionar la consulta de N documentos similares a un documento dado
     */
    private void obtainSimilarDocs(){
        //ArrayList<Pair<Double,Pair<String,String>> >
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    int numDocs = Integer.parseInt(numDocField.getText());
                    if (numDocs <= 0) throw new NumberFormatException("");
                        ArrayList<Pair<Double, Pair<String, String>>> similars = ControladorInterficie.singleton.getSimilarDocuments((String) titlesCB.getSelectedItem(),
                                (String) authorsCB.getSelectedItem(), numDocs);
                        if (similars.size() == 0)
                            JOptionPane.showMessageDialog(frame, "No hay ningún documento parecido.",
                                    "Atención!", JOptionPane.WARNING_MESSAGE);
                        String resultStr = "Los documentos más parecidos a '" + titlesCB.getSelectedItem() + "' del autor '" + authorsCB.getSelectedItem() +
                                "' y su grado de similitud son:\n\n";
                        int i = 0;
                        for (Pair<Double, Pair<String, String>> similarDoc : similars) {
                            resultStr += similarDoc.fst + "-> '" + similarDoc.snd.fst + "', de '" + similarDoc.snd.snd + "'\n";
                        }
                        final String param = resultStr;
                        LoadingDialog.workFinished();
                        javax.swing.SwingUtilities.invokeLater(() -> ResultsDialog.main(numDocs+"_similares_a_"+
                                titlesCB.getSelectedItem()+"_de_"+authorsCB.getSelectedItem(), param));
                } catch (NumberFormatException e) {
                    LoadingDialog.workFinished();
                    JOptionPane.showMessageDialog(frame, "Por favor, introduce un numero entero mayor que 0.",
                            "Atención!", JOptionPane.WARNING_MESSAGE);
                }
            });
        });
    }

    /**
     * Función privada para gestionar la consulta de una query booleana
     */
    private void obtainBooleanQuery(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoadingDialog.main();
            javax.swing.SwingUtilities.invokeLater(() -> {

                String query = queryField.getText();
                String resultStr = "Los documentos que cumplem la query '"+query+"' son:\n";
                try{
                    int i =0;
                    for( Pair<String, String> doc: ControladorDominio.singleton.getDocumentsByBooleanQuery(query)){
                        resultStr += i+++"-> '"+doc.snd+"', de '"+doc.fst+"'\n";
                    }
                    LoadingDialog.workFinished();
                    if(i==0){
                        JOptionPane.showMessageDialog(frame, "Ningún documento satisface la consulta.",
                                "Atención!", JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        final String param = resultStr;
                        javax.swing.SwingUtilities.invokeLater(() -> ResultsDialog.main("query_"+query, param));
                    }
                }
                catch(InvalidParameterException e){
                    JOptionPane.showMessageDialog(frame, e.getMessage(),
                            "Atención!", JOptionPane.WARNING_MESSAGE);
                }
            });
        });
    }

    /**
     * Función privada que oculta los campos de la pantalla de consulta
     */
    private void hideParamScreen(){
        authorsCB.setVisible(false);
        prefixField.setVisible(false);
        titlesCB.setVisible(false);
        numDocField.setVisible(false);
        queryField.setVisible(false);
        authorsLabel.setVisible(false);
        prefixLabel.setVisible(false);
        titlesLabel.setVisible(false);
        numDocLabel.setVisible(false);
        queryLabel.setVisible(false);
    }

    /**
     * Función privada que oculta los campos de la pantalla de administración
     */
    private void hideAdminParamScreen(){
        adminAuthorNameField.setVisible(false);
        adminAuthorNameCB.setVisible(false);
        adminDocTitleField.setVisible(false);
        adminDocTitleCB.setVisible(false);
        adminDocContentField.setVisible(false);
        adminAuthorNameLabel.setVisible(false);
        adminDocTitleLabel.setVisible(false);
        adminDocContentLabel.setVisible(false);
        adminScrollPanel.setVisible(false);
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de administración, muestra los
     * necesarios para el añadido de documentos y configura los botones pertinentes
     */
    private void loadAddDocumentScreen(){
        hideAdminParamScreen();
        adminAuthorNameLabel.setVisible(true);
        adminAuthorNameField.setVisible(true);
        adminDocTitleLabel.setVisible(true);
        adminDocTitleField.setVisible(true);
        adminDocContentLabel.setVisible(true);
        adminScrollPanel.setVisible(true);
        adminDocContentField.setVisible(true);
        adminConsultBtn.setText("Añadir");
        if(adminConsultBtn.getActionListeners().length >0) adminConsultBtn.removeActionListener(adminConsultBtn.getActionListeners()[0]);
        adminConsultBtn.addActionListener(e -> addDocument());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de administración, muestra los
     * necesarios para la eliminación de documentos y configura los botones pertinentes
     */
    private void loadDelDocumentScreen(){
        populateAuthor(adminAuthorNameCB);
        hideAdminParamScreen();
        adminAuthorNameLabel.setVisible(true);
        adminAuthorNameCB.setVisible(true);
        adminDocTitleLabel.setVisible(true);
        adminDocTitleCB.setVisible(true);
        adminConsultBtn.setText("Eliminar");
        if(adminConsultBtn.getActionListeners().length >0) adminConsultBtn.removeActionListener(adminConsultBtn.getActionListeners()[0]);
        adminConsultBtn.addActionListener(e -> deleteDocument());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de administración, muestra los
     * necesarios para la modificación de documentos y configura los botones pertinentes
     */
    private void loadModDocumentScreen(){
        populateAuthor(adminAuthorNameCB);
        hideAdminParamScreen();
        adminAuthorNameLabel.setVisible(true);
        adminAuthorNameCB.setVisible(true);
        adminDocTitleLabel.setVisible(true);
        adminDocTitleCB.setVisible(true);
        adminDocContentLabel.setVisible(true);
        adminScrollPanel.setVisible(true);
        adminDocContentField.setVisible(true);
        adminConsultBtn.setText("Modificar");
        if(adminConsultBtn.getActionListeners().length >0) adminConsultBtn.removeActionListener(adminConsultBtn.getActionListeners()[0]);
        adminConsultBtn.addActionListener(e -> modifyDocument());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de consulta, muestra los
     * necesarios para la consulta por titulos y configura los botones pertinentes
     */
    private void loadTitlesScreen(){
        populateAuthor(authorsCB);
        hideParamScreen();
        authorsLabel.setVisible(true);
        authorsCB.setVisible(true);
        consultBtn.setText("Consultar Titulos");
        if(consultBtn.getActionListeners().length >0) consultBtn.removeActionListener(consultBtn.getActionListeners()[0]);
        consultBtn.addActionListener(e -> obtainTitles());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de consulta, muestra los
     * necesarios para la consulta por prefijo y configura los botones pertinentes
     */
    private void loadPrefixScreen(){
        hideParamScreen();
        prefixLabel.setVisible(true);
        prefixField.setVisible(true);
        consultBtn.setText("Obtener autores con el prefijo");
        if(consultBtn.getActionListeners().length >0) consultBtn.removeActionListener(consultBtn.getActionListeners()[0]);
        consultBtn.addActionListener(e -> obtainAuthorsPrefix());

    }

    /**
     * Función privada que, despues de ocultar todos los parametros de consulta, muestra los
     * necesarios para la consulta de contenido y configura los botones pertinentes
     */
    private void loadContentScreen(){
        populateAuthor(authorsCB);
        hideParamScreen();
        authorsLabel.setVisible(true);
        authorsCB.setVisible(true);
        titlesLabel.setVisible(true);
        titlesCB.setVisible(true);
        consultBtn.setText("Obtener el contenido");
        if(consultBtn.getActionListeners().length >0) consultBtn.removeActionListener(consultBtn.getActionListeners()[0]);
        consultBtn.addActionListener(e -> obtainDocContent());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de consulta, muestra los
     * necesarios para la comparación de documentos y configura los botones pertinentes
     */
    private void loadCompareScreen(){
        populateAuthor(authorsCB);
        hideParamScreen();
        authorsLabel.setVisible(true);
        authorsCB.setVisible(true);
        titlesLabel.setVisible(true);
        titlesCB.setVisible(true);
        numDocLabel.setVisible(true);
        numDocField.setVisible(true);
        consultBtn.setText("Obtener documentos similares");
        if(consultBtn.getActionListeners().length >0) consultBtn.removeActionListener(consultBtn.getActionListeners()[0]);
        consultBtn.addActionListener(e -> obtainSimilarDocs());
    }

    /**
     * Función privada que, despues de ocultar todos los parametros de consulta, muestra los
     * necesarios para la consulta mediante una query booleana y configura los botones pertinentes
     */
    private void loadQueryScreen(){
        hideParamScreen();
        queryLabel.setVisible(true);
        queryField.setVisible(true);
        consultBtn.setText("Ejecutar la query");
        if(consultBtn.getActionListeners().length >0) consultBtn.removeActionListener(consultBtn.getActionListeners()[0]);
        consultBtn.addActionListener(e -> obtainBooleanQuery());
    }

    /**
     * Función privada para poblar una JComboBox con los nombres de los autores
     * @param authorBox JComboBox a rellenar
     */
    private void populateAuthor(JComboBox authorBox){
        ActionListener[] aux = authorBox.getActionListeners();
        authorBox.removeActionListener(aux[0]);
        authorBox.removeAllItems();
        authorBox.addActionListener(aux[0]);
        ControladorInterficie.singleton.getAllAuthors().forEach(authorBox::addItem);
    }

    /**
     * Función privada para poblar una JComboBox con los titulso de los documentos de un autor
     * @param titlesBox JComboBox a rellenar
     * @param author autor cuyos titulos hay que almacenar.
     */
    private void populateTitles(JComboBox titlesBox, String author){
        titlesBox.removeAllItems();
        Set<String> titles = ControladorInterficie.singleton.getAllTitlesFromAuthor(author);
        titles.forEach(titlesBox::addItem);
    }

    private JTabbedPane tabbedPane1;
    private JPanel mainWindow;
    private JPanel adminPanel;
    private JPanel userPanel;
    private JButton exitBtn;
    private JButton logBtn;
    private JButton exitBtnU;
    private JButton titlesBtn;
    private JButton prefixBtn;
    private JButton contentBtn;
    private JButton compareBtn;
    private JButton queryBtn;
    private JButton consultBtn;
    private JPanel optionsPanel;
    private JComboBox authorsCB;
    private JTextField prefixField;
    private JComboBox titlesCB;
    private JTextField numDocField;
    private JTextField queryField;
    private JLabel authorsLabel;
    private JLabel prefixLabel;
    private JLabel titlesLabel;
    private JLabel numDocLabel;
    private JLabel queryLabel;
    private JButton addDocBtn;
    private JButton delDocBtn;
    private JButton modDocBtn;
    private JRadioButton indexadoBinarioRadioButton;
    private JRadioButton indexadoTFIDFRadioButton;
    private JTextField adminAuthorNameField;
    private JComboBox adminAuthorNameCB;
    private JTextField adminDocTitleField;
    private JComboBox adminDocTitleCB;
    private JPanel adminOptionsPanel;
    private JTextArea adminDocContentField;
    private JButton adminConsultBtn;
    private JLabel adminAuthorNameLabel;
    private JLabel adminDocTitleLabel;
    private JLabel adminDocContentLabel;
    private JScrollPane adminScrollPanel;
}
