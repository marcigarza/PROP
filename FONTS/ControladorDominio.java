import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Clase para almacenar el conjunto de Autor presente en el sistema
 * @author marina
 */
public class ControladorDominio {
    public static ControladorDominio singleton = new ControladorDominio();
    HashMap<String, Autor> autores;

    /**
     * Creadora privada para generar el singleton del controlador.
     */
    private ControladorDominio(){
        autores = new HashMap<>();
    }

    /**
     * Función para inicializar el sistema
     * @throws StartUpException Excepcion que controla si el sistema estaba vacio.
     */
    public void startUpSystem() throws StartUpException {
        ArrayList<Triplet> docs = ControladorPersistencia.singleton.loadLibrary();
        ContenidoIndexado.singleton.setMethod(0);
        if(docs.size() == 0) throw new StartUpException("No hay documentos en el sistema");
        for (Triplet doc : docs){
            try {
                addDocument(doc, false);
            } catch (ItemExistanceException e) {}
        }
    }

    /**
     * Función para añadir un documento al sistema
     * @param doc Triplet con la informacion de un documento
     * @param admin bool que indica si es un documento creado desde la pantalla de admin o no.
     * @throws ItemExistanceException Exception que controla si la combianción de titulo y autor ya existe.
     */
    public void addDocument(Triplet doc, boolean admin) throws ItemExistanceException {
        //Comprobación error
        if(autores.containsKey(doc.getAutor())){
            if(autores.get(doc.getAutor()).getAllTitles().contains(doc.getTitulo())){
                throw new ItemExistanceException("La combinación de título y autor ya existe");
            }
        }
        //Código
        else{
            autores.put(doc.getAutor(), new Autor(doc.getAutor()));
        }
        if(admin)ControladorPersistencia.singleton.saveDoc(doc);
        ContenidoDocumento docCont = new ContenidoDocumento(doc.getContenido());
        Documento newDoc = new Documento(doc.getTitulo(), docCont);
        autores.get(doc.getAutor()).addDocument(newDoc);
        getIndexerType().indexarDocumento(newDoc, doc.getAutor());
    }

    /**
     * Fucnión para borrar un documento.
     * @param author nombre del autor del documento.
     * @param title titulo del documento a borrar.
     * @return boolean indicating if the author was deleted
     */
    public boolean deleteDocument(String author, String title){
        //Código
        ContenidoIndexado.singleton.deleteDocument(title, author);
        autores.get(author).deleteDocument(title);
        ControladorPersistencia.singleton.deleteDoc(author, title);
        if (autores.get(author).numberOfDocuments()==0){
            autores.remove(author);
            return true;
        }
        return false;
    }
    
    /**
     * Función para editar el contenido de un documento
     * @param title titulo del documento a editar.
     * @param author nombre del autor del documento.
     * @param newContent Nuevo contenido del documento
     */
    public void editDocument(String title, String author, String newContent){
        //Código
        ContenidoIndexado.singleton.deleteDocument(title, author);
        autores.get(author).getDocument(title).getContent().setContent(newContent);
        //ContenidoDocumento contAux = new ContenidoDocumento(newContent);
      //  autores.get(author).getDocument(title).setContent(contAux);
        getIndexerType().indexarDocumento(autores.get(author).getDocument(title), author);
        ControladorPersistencia.singleton.saveDoc(new Triplet(author, title, newContent));
    }



    /**
     * Función para obtener todos los titulos de los documentos de un autor
     * @param author nombre del autor a consultar
     * @return Set de Strings con los titulos de los documentos
     */
    public Set<String> getAllTitlesFromAuthor (String author){
        return autores.get(author).getAllDocuments().keySet();
    }

    /**
     * Función para obtener los autores cuyo nombre empieza por un determinado prefijo.
     * @param prefix prefijo a consultar
     * @return Set de Stirngs con los nombres de los autores.
     */
    public Set<String> getAllAuthorsWithPrefix (String prefix){
        Set<String> setAux = autores.keySet();
        Set<String> setRet = new TreeSet<>();
        for (String aut: setAux){
            if (aut.startsWith(prefix)){
                setRet.add(aut);
                //setAux.remove(aut);
            }
        }
        return setRet;
    }

    /**
     * Función para obtener el contenido de un documento
     * @param title título del documento a bsucar
     * @param author nombre del autor a buscar
     * @return String con el contenido del documento
     */
    public String getDocumentContent (String title, String author){
        return autores.get(author).getDocument(title).getContent().getContenido();
    }


    /**
     * Función para obtener los k documentos mas similates a un documento concreto
     * @param title titulo del documento
     * @param author nombre del autor
     * @param k numero de documentos similares a buscar
     * @return ArrayList de Pairs de Strings tal que {autor, titulo}
     */
    public ArrayList<Pair<Double,Pair<String,String>> > getSimilarDocuments (String title, String author, int k){
        //Exceptions
        if(k <= 0) throw new InvalidParameterException("K ha de ser mayor que 0");
        //Codigo
        return ComparadorDocumentos.singleton.comparador(title, author, k);
    }


    /**
     * Función para obtener los documentos que cumplen una query booleana
     * @param booleanQuery la query booleana
     * @return ArrayList de pairs de Strings con la estructura {autor, titulo}
     * @throws InvalidParameterException Excpecion que ocntrola que la query sea valida.
     */
    public ArrayList<Pair<String, String>> getDocumentsByBooleanQuery (String booleanQuery) throws InvalidParameterException {
        InterpreteBooleano interBoo = new InterpreteBooleano();
        if(!interBoo.isValid(booleanQuery)){
            throw new InvalidParameterException("La consulta no es válida.");
        }
        //execute the query
        return interBoo.interpretarConsultaBooleana(booleanQuery);
    }

    /**
     * Función para obtener el numero total de documentos en el sistema
     * @return numero de documentos en el sistema
     */
    public int getNumberOfDocuments(){
        int sum = 0;
        for(String aut: autores.keySet()){
            sum += autores.get(aut).numberOfDocuments();
        }
        return sum;
    }


    /**
     * Función para definir el tipo de indexador a usar.
     * @param type int para indicar que indexador usar
     * @throws SystemResetException Exception para controlar si se ha reiniciado el sistema
     */
    public void setIndexerType(int type) throws SystemResetException {
        boolean exists = false;
        if(ContenidoIndexado.singleton.getMethod() !=-1) exists = true;
        ContenidoIndexado.singleton.setMethod(type);
        if(exists){
            ContenidoIndexado.singleton.destroyIndexedContent(type);
            for(String aut: autores.keySet()){
                for(String doc: autores.get(aut).getAllTitles()){
                    getIndexerType().indexarDocumento(autores.get(aut).getDocument(doc), aut);
                }
            }
            throw new SystemResetException("El sistema ha sido reiniciado con el nuevo indexador");
        }
    }

    /**
     * Función privada para que el sistema use el indexador adecuado.
     * @return Instancia singleton del indexador adecuado.
     */
    private IndexadorDocumentos getIndexerType(){
        switch(ContenidoIndexado.singleton.getMethod()){
            case 1:
                return IndexadorDocumentosTFIDF.singleton;
            default:
                return IndexadorDocumentosBinario.singleton;
        }
    }

    /**
     * función para obtener la lista de palabras a eliminar antes de indexar
     * @param file indicador de si queremos la lista de simbolos (0) o la de palabras (1)
     * @return la lista de strings solicitada
     */
    public ArrayList<String> getForbiddenWords (int file){
        return ControladorPersistencia.singleton.getForbiddenWords(file);
    }

    /**
     * Fucnión para obtener todos los titulos presentes en el sistema
     * @return ArrayList de pairs de strings con el formato autor, titulo
     */
    public ArrayList<Pair<String,String>> getAllTitles(){
        ArrayList<Pair<String, String>> docs = new ArrayList<>();
        for(String aut: autores.keySet()){
            for(String doc: autores.get(aut).getAllTitles()){
                docs.add(new Pair<>(aut, doc));
            }
        }
        return docs;
    }

    /**
     * Función para obtener el contenido de un documento
     * @param author String con el nombre del autor
     * @param title String con el titulo del autor
     * @return ContenidoDocumento con el contenido del documento
     */
    public ContenidoDocumento getContent(String author, String title){
        return autores.get(author).getDocument(title).getContent();
    }

    /**
     * Función para almacenar en permanencia el resultado de una consulta
     * @param query id de la consulta (futuro nombre del archivo)
     * @param result resultado de la consulta.
     */
    public void saveResult(String query, String result) {
        ControladorPersistencia.singleton.saveResult(query, result);
    }
}
