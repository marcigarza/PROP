/**
 * Created by Marc on 12/10/2016.
 */


 /**
 * Clase para gestionar un documento
 *@author marc
 */
public class Documento {

    private  String title;
    private ContenidoDocumento content;


    /**
    *Creadora de un Documento
    */
    public Documento() {
        title = null;
        content = null;
    }

    /**
    *Creadora de un Documento
    *@params title titulo del documento que se quiere crear
    */
    public Documento(String title) {
        this.title = title;
        content = null;
    }
    /**
    *Creadora de un Documento
    *@params title titulo del documento que se quiere crear
    *@params content contenido del documento que se quiere crear
    */
    public Documento(String title, ContenidoDocumento content) {
        this.title = title;
        this.content = content;
    }
    /**
    *Creadora de un Documento
    *@params title titulo del documento que se quiere crear
    *@params content contenido del documento que se quiere crear
    */
    public Documento(ContenidoDocumento content) {
        this.content = content;
    }

    /**
    *Función que devuelve el titulo del documento
    *@return titulo del documento
    */
    public  String getTitle() {
        return title;
    }

    /**
    *Función que devuelve el contenido del documento
    *@return contenido del documento
    */    public ContenidoDocumento getContent() {
        return content;
    }

    /**
    *Funcion que asigna el titulo del documento
    *@params title titulo del documento que se quiere crear
    */
    public  void setTitle(String title) {
        this.title = title;
    }
    /**
    *Funcion que asigna el contenido del documento
    *@params content contenido del documento que se quiere crear
    */
    public void setContent(ContenidoDocumento content) {
        this.content = content;
    }

}
