
/**
 * Created by David on 02/12/2016.
 */
public class Triplet {
    private String autor;
    private String titulo;
    private String contenido;

    public Triplet(){}
    public Triplet(String autor, String titulo, String contenido){
        this.autor = autor;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public void setAutor (String a){
        autor = a;
    }

    public void setTitulo (String t){
        titulo = t;
    }

    public void setContenido (String c){
        contenido = c;
    }

    public String getAutor (){
        return autor;
    }

    public String getTitulo (){
        return titulo;
    }

    public String getContenido (){
        return contenido;
    }
}
