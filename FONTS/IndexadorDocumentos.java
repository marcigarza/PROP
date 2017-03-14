import java.util.ArrayList;

/**
 * Created by marina on 04/11/2016.
 */
public abstract class IndexadorDocumentos {
    protected static ArrayList<String> forbiddenSymbols= ControladorDominio.singleton.getForbiddenWords(0);
    protected static ArrayList<String> forbiddenWords = ControladorDominio.singleton.getForbiddenWords(1);
    public abstract void indexarDocumento(Documento doc, String author);

    /**
     * Función para purgar una string de palabras y simbolos inutiles de cara a un indexado.
     * @param content String a purgar
     * @return string purgada
     */
    public String removeWords(String content){
        content = " "+content+" ";
        content = content.replace("\n"," ");
        content = content.toLowerCase();
        for (String symbol: forbiddenSymbols){
            content = content.replace(symbol," ");
        }
        content = content.replace(" ", "  ");
        for (String word: forbiddenWords){
            content = content.replace(" "+word+" ", " ");
        }
        return content;
    }
}
