/**
 * Created by David on 04/11/2016.
 */
public class IndexadorDocumentosBinario extends IndexadorDocumentos {
    public final static IndexadorDocumentosBinario singleton = new IndexadorDocumentosBinario();

    private IndexadorDocumentosBinario(){

    }

    /**
     * Funcion indexar con el peso de 1 el documento doc de autor author
     * @param doc contiene el documento cuyo contenido queremos indexar
     * @param author es el autor de doc
     */
    public void indexarDocumento(Documento doc, String author){
        String contenido = doc.getContent().getContenido();
        contenido = removeWords(contenido);
        String[] strArray = contenido.split("\\s+");
        String title = doc.getTitle();
        for (int i = 0; i < strArray.length; ++i){
            ContenidoIndexado.singleton.addWord(strArray[i], title, author, 1.0f);
        }

    }
}
