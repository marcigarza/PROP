
import java.io.*;
import java.util.ArrayList;

/**
 * Clase para controlar la capa de persistencia
 * @author david
 */

public class ControladorPersistencia {
    public static ControladorPersistencia singleton = new ControladorPersistencia();

    /**
     * Función para cargar todos los documentos de la carpeta /documents
     * @return ArrayList<Triplet> de todos los documentos cargados
     */
    public ArrayList<Triplet> loadLibrary (){
        ArrayList<Triplet> list = new ArrayList<>();
        try {
            File folder = new File("documents/");
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; ++i){
                BufferedReader reader = new BufferedReader(new FileReader (listOfFiles[i]));
                String text = "";
                Triplet a = new Triplet();
                try{
                    String line = reader.readLine();
                    boolean first = true;
                    int j = 0;
                    while (line != null){
                        if (j < 1 | first) {
                            if (!(j < 1)) first = false;
                        }
                        else text += "\n";
                        if (j == 0) a.setAutor(line);
                        else if (j == 1) a.setTitulo(line);
                        else{
                            text += line;
                        }
                        ++j;
                        line = reader.readLine();
                    }
                    if (!listOfFiles[i].getName().equals(".DS_Store")){
                        String[] strArray = listOfFiles[i].getName().split("_");
                        if (strArray.length != 2) {
                            System.out.println("Non valid title: " + strArray[0]);
                        }
                        else {
                            a.setContenido(text);
                            list.add(a);
                        }
                    }
                    else System.out.println(".Ds_Store ignored");


                }
                catch (IOException e){
                    System.out.println("File is empty");
                }
            }


        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return list;
    }

    /**
     * Función para guardar fichero llamado autor_title.txt del triplet doc en la carpeta documents
     * @param doc triplete del documento que se quiere guardar
     */
    public void saveDoc (Triplet doc){
        String filename = doc.getAutor().replaceAll("\\s","");
        filename += "_";
        filename += doc.getTitulo().replaceAll("\\s", "");
        filename += ".txt";
        filename = filename.replaceAll("/",""); /// \ : * ? <> |
        filename = filename.replaceAll(":","");
        filename = filename.replaceAll("\\*","");
        filename = filename.replaceAll("\\?","");
        filename = filename.replaceAll("<","");
        filename = filename.replaceAll(">","");
        filename = filename.replaceAll("\\|","");
        filename = filename.replaceAll("\\\\","");
        try{
            File folder = new File("documents/" + filename);
            try{
                PrintWriter out = new PrintWriter(folder, "UTF-8");
                out.println(doc.getAutor());
                out.println(doc.getTitulo());
                out.println(doc.getContenido());
                out.close();
            }
            catch (UnsupportedEncodingException e){
                System.out.println("Unsupported Encoding Exception");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return;
    }

    /**
     * Función para eliminar fichero llamado autor_title.txt de la carpeta documents
     * @param Autor el autor del documento title
     * @param Title titulo del documento que se desea eliminar
     */
    public void deleteDoc (String Autor, String Title){
        String filename = Autor.replaceAll("\\s","");
        filename += "_";
        filename += Title.replaceAll("\\s", "");
        filename += ".txt";
        filename = filename.replaceAll("/",""); /// \ : * ? <> |
        filename = filename.replaceAll(":","");
        filename = filename.replaceAll("\\*","");
        filename = filename.replaceAll("\\?","");
        filename = filename.replaceAll("<","");
        filename = filename.replaceAll(">","");
        filename = filename.replaceAll("\\|","");
        filename = filename.replaceAll("\\\\","");
        try{
            File file = new File("documents/" + filename);
            if (!file.delete()) throw new FileNotFoundException();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return;
    }


    /**
     * Función para guardad fichero llamado title en la carpeta results
     * @param title el nombre del fichero sin .txt
     * @param content contenido que se guardará en el fichero title
     */
    public void saveResult (String title, String content){
        title += ".txt";
        try{
            File folder = new File("results/" + title);
            try{
                PrintWriter out = new PrintWriter(folder, "UTF-8");
                out.println(content);
                out.close();
            }
            catch (UnsupportedEncodingException e){
                System.out.println("Unsupported Encoding Exception");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return;
    }

    /**
     * Función para obtener la lista de palabras a eliminar antes de indexar
     * @param file indicador de si queremos la lista de simbolos (0) o la de palabras (1)
     * @return la lista de strings solicitada
     */
    public ArrayList<String> getForbiddenWords (int file){
        ArrayList<String> forbiddenWords = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(file == 0? new FileReader("resources/symbol.txt"):new FileReader("resources/wholeWords.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                forbiddenWords.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return forbiddenWords;
    }
}