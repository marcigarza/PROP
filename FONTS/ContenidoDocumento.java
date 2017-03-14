/**
 * Clase para almacenar el contenido de un documento
 * @author marina
 */
public class ContenidoDocumento {
    //No need to save here individual words, search is better done via IndexedContent.
    private String content;

    /**
     * Inicializador de la clase.
     * @param doc contenido del documento a almacenar.
     */
    public ContenidoDocumento(String doc){
        content = doc;
    }

    /**
     * getter del contenido del documento.
     * @return el contneido del documento.
     */
    public String getContenido(){
        return content;
    }

    public void setContent(String newContent){
        content = newContent;
    }

    /**
     * Función para obtener si un documento contiene una secuencia de caracteres.
     * @param sequence secuencia a buscar
     * @return true si la contiene, false si no
     */
    public boolean containsSequence(String sequence){
        return content.contains(sequence);
    }

}
