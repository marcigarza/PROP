import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Clase para almacenar un autor
 * @author david
 */
public class Autor {
    private String name;
    private Map<String, Documento> documents;

    public Autor (){
        name = "anonymous"; //not sure about this
        documents = new HashMap<String, Documento>();
    }

    /**
     * Función para crear un autor de nombre igual a name
     * @param name nombre que le queremos asignar al atributo privado name
     */
    public Autor (String name){
        this.name = name;
        documents = new HashMap<String, Documento>();
    }

    /**
     * Función para crear un autor con documentos igual a documents
     * @param documents el mapa de documentos de este autor
     */
    public Autor (Map<String, Documento> documents){
        this.documents = documents;
    }

    /**
     * Función para crear un autor de nombre igual a name y con documentos igual a documents
     * @param name nombre que le queremos asignar al atributo privado name
     * @param documents el mapa de documentos de este autor
     */
    public Autor (String name, Map<String, Documento> documents){
        this.name = name;
        this.documents = documents;
    }


    /**
     * Función que retorna el nombre del Autor
     * @return devuelve el nombre de autor
     */
    public String getName(){
        return name;
    }

    /**
     * Función que devuelve el Documento con title = title, sinó devuelve un nulo
     * @param title, string del titulo que queremos que nos devuelvan
     * @return Documento con title = title
     */
    public Documento getDocument (String title){
        if (documents.containsKey(title)) {
            return documents.get(title);
        }
        System.out.println("error, Document no with that title");
        return null;
    }

    /**
     * Devuelve la map de todos los documentos del autor
     * @return map de todos los documentos del autor
     */
    public  Map<String, Documento> getAllDocuments (){
        return documents;
    }


    /**
     * Devuelve la map de todos los documentos del autor
     * @return la ArrayList que coniene el nombre de todos los titulos de documentos que tiene Autor
     */
    public ArrayList<String> getAllTitles (){
        ArrayList<String>  aux = new ArrayList<String>();
        Iterator it = documents.keySet().iterator();
        for (int i = 0; i < documents.size(); ++i){
            aux.add(it.next().toString());
        }
        return aux;
    }

    /**
     * Devuelve la map de todos los documentos del autor
     * @return la ArrayList que coniene el nombre de todos los titulos de documentos que tiene Autor
     */
    public void addDocument (Documento newDoc){

        if (!documents.containsKey(newDoc.getTitle())) documents.put(newDoc.getTitle(), newDoc);
        else System.out.println("error, Document already there");
        return;
    }

    /**
     * Función para eliminar el documento con titulo Doc
     * @param Doc es el titulo que  queremos eliminar
     */
    public void deleteDocument (String Doc){
        if (documents.containsKey(Doc)) documents.remove(Doc);
        else System.out.println("error, No document with name Doc");
        return;
    }

    /**
     * Función para saber la cantidad de documentos que tiene autor
     * @return la cantidad de documentos que tiene este autor
     */
    public int numberOfDocuments (){
        return documents.size();
    }

    /**
     * Función para  cambiar el nombre de autor
     * @param name el nombre nuevo que le queremos assignar
     */
    public void setName (String name){
        this.name = name;
    }

    /**
     * Función para añadir el mapa de documentos de autor
     * @param doc el mapa de documentos que queremos que tenga autor
     */
    public void setDocuments (Map<String, Documento> doc){
        documents.putAll(doc);
    }

}
