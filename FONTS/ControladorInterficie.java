import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by marina on 26/11/2016.
 */
public class ControladorInterficie {
    public static ControladorInterficie singleton = new ControladorInterficie();
    private boolean authenticated = false;
    /**
     * Creadora privada para generar el singleton del controlador.
     */
    private ControladorInterficie(){
    }

    /**
     * Función para mostrar por pantalla la ventana principal
     */
    public void showMainWindow(){
        MainWindow.main();
    }

    /**
     * función para cambiar el tipo de indexado del sistema
     * @param indexer id del indexador a usar
     * @throws SystemResetException excepcion que controla si el sistema se ha reiniciado correctamente
     * en caso de ya estar indexado previamente
     */
    public void setIndexerType(int indexer) throws SystemResetException {
        ControladorDominio.singleton.setIndexerType(indexer);
    }

    /**
     * función para añadir un documento al sistema
     * @param doc Triplet con el contenido de un documento
     * @throws ItemExistanceException Excepción que controla si ya esistia el documento en el sistema
     */
    public void addDocument(Triplet doc) throws ItemExistanceException {
        ControladorDominio.singleton.addDocument(doc, true);
    }

    /**
     * Función para borrar un documento
     * @param author autor del documento
     * @param title titulo del documento
     * @return true si este era el ultimo documento del autor y, por ende, se ha borrado también al autor.
     */
    public boolean deleteDocument(String author, String title) {
        return ControladorDominio.singleton.deleteDocument(author, title);
    }

    /**
     * consultora de si el usuario tiene privilegios de administración
     * @return
     */
    public boolean isAdmin(){
        return authenticated;
    }


    /**
     * Función para cerrar la sesión de un usuario
     */
    public void logOut(){
        authenticated = false;
    }

    /**
     * Función para comparar la contraseña introducida con la correcta, se ser iguales hace login
     * @param password char[] con la contraseña
     * @return true si son iguales
     */
    public boolean comparePasswords(char[] password){
        char[] correctPass = {'a','d','m','i','n'};
        if(Arrays.equals(password, correctPass)){
            authenticated = true;
            return true;
        }
        else return false;
    }

    /**
     * Consultora para saber si hay documentos en el sistema
     * @return true si hay documentos, false si no
     */
    public boolean isSystemEmpty(){
        try {
            ControladorDominio.singleton.startUpSystem();
        } catch (StartUpException e) {
            return true;
        }
        return false;
    }

    /**
     * Función para obtener todos los autores en el sistema
     * @return Set de strings con los nombres d elos autores
     */
    public Set<String> getAllAuthors() {
        return ControladorDominio.singleton.getAllAuthorsWithPrefix("");
    }

    /**
     * Función para obtener todos los titulos de un autor
     * @param author nombre del autor
     * @return Set de Strings con la serie de titulos del autor
     */
    public Set<String> getAllTitlesFromAuthor(String author) {
        return ControladorDominio.singleton.getAllTitlesFromAuthor(author);
    }

    /**
     * Función para obtener los K documentos similares a un documento concreto
     * @param title titulo del documento
     * @param author autor del documento
     * @param numDocs numero de documentos similares a encontrar
     * @return ArrayList  de Pairs con la estructura : <Grado similitud, Pair<Autor, titulo>
     */
    public ArrayList<Pair<Double,Pair<String,String>>> getSimilarDocuments(String title, String author, int numDocs) {
        return ControladorDominio.singleton.getSimilarDocuments(title, author, numDocs);
    }

    /**
     * Función para editar el contenido de un documento ya existente
     * @param title titulo del documento a editar
     * @param author nombre del autor del documento
     * @param content nuevo contenido
     */
    public void editDocument(String title, String author, String content) {
        ControladorDominio.singleton.editDocument(title, author, content);
    }

    /**
     * Función para almacenar en permanencia el resultado de una consulta
     * @param query id de la consulta (futuro titulo del archivo)
     * @param result resultado de la consulta
     */
    public void saveResult(String query, String result) {
        ControladorDominio.singleton.saveResult(query, result);
    }
}
