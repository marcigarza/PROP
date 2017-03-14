import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 * Clase singleton para representar el contenido indexado de los documentos.
 *
 * @author marina
 */
public class ContenidoIndexado {
    /**
     * Instancia singleton de la clase
     */
    public final static ContenidoIndexado singleton = new ContenidoIndexado();
    /**
     * indicador del metodo de indexado actual
     */
    private static int method = -1;

    //Map< word, Map < author, Map < title, weight>>>
    private static Map<String, Map<String, Map<String, Float>>> indexedContent;

    /**
     * Creadora privada de la clase.
     */
    private ContenidoIndexado(){
        indexedContent = new HashMap<>();
    }

    /**
     * getter del metodo de indexado
     * @return int referente al metodo
     */
    public int getMethod(){return method;}

    /**
     * setter del metodo de indexado
     * @param m int indicador del metodo.
     */
    public void setMethod(int m){method = m;}

    /**
     * Función para destruir el contenido indexado actual.
     */
    public void destroyIndexedContent(int m){
        indexedContent = new HashMap<>();
        setMethod(m);
    }

    /**
     * Funcion para añadir una palabra al documento con un determinado peso.
     * @param word palabra a añadir.
     * @param title titulo del documento al que se quiere añadir la palabra.
     * @param weight peso que se quiere registrar.
     */
    public void addWord(String word, String title, String author, Float weight){
        if(!word.equals("")) {
            Map<String, Float> aux = new HashMap<>();
            aux.put(title, weight);
            if (!indexedContent.containsKey(word)) {
                Map<String, Map<String, Float>> aux2 = new HashMap<>();
                aux2.put(author, aux);
                indexedContent.put(word, aux2);
            } else if (!indexedContent.get(word).containsKey(author)) {
                indexedContent.get(word).put(author, aux);
            } else {
                indexedContent.get(word).get(author).put(title, weight);
            }
        }
    }

    /**
     * Función para borrar el contenido indexado de un documento.
     * @param title titulo del documento a borrar.
     * @param author nombre dle autor del documento
     */
    public void deleteDocument(String title, String author){
        for(String word: indexedContent.keySet()){
           if(indexedContent.get(word).keySet().contains(author)&&
              indexedContent.get(word).get(author).keySet().contains(title))
              {
                indexedContent.get(word).get(author).remove(title);
              }
           if(indexedContent.get(word).keySet().contains(author)&&
             indexedContent.get(word).get(author).keySet().size()==0){
             indexedContent.get(word).remove(author);
             break;
           }
        }
    }

    /**
     * Funcion para obtener el peso de una palabra en un documento.
     * @param word la palabra a buscar.
     * @param title el titulo del documento que queremos consultar.
     * @return el peso de la palabra.
     */
    public Float getWeight(String word, String title, String author){
        return indexedContent.get(word).get(author).get(title);
    }

    /**
     * Funcion para obtener todas las palabras presentes en un documento.
     * @param title titulo del documento a consultar
     * @return StringArray conteniendo las palabras del documento.
     */
    public ArrayList<String> getWords(String title, String author){
        ArrayList<String> docWords = new ArrayList<String>();
        Iterator itAux = this.indexedContent.keySet().iterator();
        String word;
        for (int i =0; i < indexedContent.size(); ++i){
            word = itAux.next().toString();
            if(indexedContent.get(word).containsKey(author)){
                if(indexedContent.get(word).get(author).containsKey(title)){
                    docWords.add(word);
                }
            }
        }
        return docWords;
    }

    /**
     * Función para obtener los documentos en los que existe una palabra concreta
     * @param word la palabra a buscar
     * @return StringArray con pairs formados por  \<autr,titulos> de los documentos.
     */

     //Map < author, Map < title, weight>>>
    public ArrayList<Pair<String,String>> getDocumentsWith(String word){
      ArrayList<Pair<String, String>> docs = new ArrayList<>();
      if(indexedContent.containsKey(word)){
         for(String aut: indexedContent.get(word).keySet()){
           for(String doc: indexedContent.get(word).get(aut).keySet()){
             docs.add(new Pair<>(aut, doc));
           }
         }
      }
      return docs;
    }

    /**
     * Función para obtener los documentos que no contienen
     * @param word palabra a buscar
     * @return ArrayList formada por pairs con los autores y titulos.
     */
    public ArrayList<Pair<String,String>> getDocumentsWithout(String word){
        ArrayList<Pair<String, String>> docsWithWord = getDocumentsWith(word);
        ArrayList<Pair<String, String>> allDocs = ControladorDominio.singleton.getAllTitles();
        for(Pair<String, String> pair : docsWithWord){
            allDocs.remove(pair);
        }
        return allDocs;
    }
}
