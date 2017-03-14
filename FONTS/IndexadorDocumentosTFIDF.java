import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by marcigarza on 7/11/16.
 */

 /**
  * Clase para indexar un documento segun el metodo tf-idf
  * @author marc
  */
public class IndexadorDocumentosTFIDF extends IndexadorDocumentos {
    public final static IndexadorDocumentosTFIDF singleton = new IndexadorDocumentosTFIDF();

    /**
    *Creadora privada de la clase
    */
    private IndexadorDocumentosTFIDF(){

    }

    /**
     * Función para conocer el numero de palabras diferentes de una secuencia
     * @param strArray secuencia de palabras
     * @return numero de palabras diferentes de una secuencia
     */
    public  int DifWords(List<String> strArray) {
        ArrayList<String> listofWords = new ArrayList<>();
        for(String s : strArray) {
            if(!listofWords.contains(s)) {
                listofWords.add(s);
            }
        }
        if(listofWords.size() == 1) return 1;
        return listofWords.size() - 1;
    }

    /**
    *Función para actualizar el contenido indexado de la base de datos
    *@param Palabra String que se modificara en la base de datos
    */
    public  void IndexarContenidoRestante(String Palabra) {
        ArrayList<Pair<String,String>> docsPalabra = ContenidoIndexado.singleton.getDocumentsWith(Palabra);
        for(Pair<String,String> str: docsPalabra) {
            ArrayList<String> words = ContenidoIndexado.singleton.getWords(str.snd,str.fst);
            int d = DifWords(words);
            float Ftd = (float)0.0;
            for(int i = 0; i < words.size();++i) {
                if(words.get(i).equals(Palabra))  ++Ftd;
            }
            float TF = Ftd/d;
            int N = ControladorDominio.singleton.getNumberOfDocuments();

            int n1 = ContenidoIndexado.singleton.getDocumentsWith(Palabra).size();
            float IDF = (float)Math.log(1+(N/n1));
            ContenidoIndexado.singleton.addWord(Palabra,str.snd,str.fst,TF*IDF);

        }
    }

    /**
    *Función para indexar un Documento
    *@param doc Documento a indexar
    *@param author Autor del documento a indexar
    */
    public  void indexarDocumento(Documento doc, String author){
        String contenido = doc.getContent().getContenido();
        contenido = removeWords(contenido);
        String[] auxiliar = contenido.split("\\s+");
        List<String> strArray =  Arrays.asList(auxiliar);
        String title = doc.getTitle();
        int d = DifWords(strArray);
        for(String str : strArray) {
            Float Ftd = (float)0.0;
            for(String aux : strArray) {
                if(Objects.equals(aux, str))  ++Ftd;
            }
            Float TF = Ftd/d;
            int N = ControladorDominio.singleton.getNumberOfDocuments();
            int n1 = ContenidoIndexado.singleton.getDocumentsWith(str).size();
            if (n1 == 0) n1 = 1;
            Float IDF = (float)Math.log(1+(N/n1));
            ContenidoIndexado.singleton.addWord(str, title, author,(TF*IDF) );
            IndexarContenidoRestante(str);
        }
    }




}
