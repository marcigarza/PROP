import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by Marc on 15/10/2016.
 */


/**
 * Clase para comparar documentos *
 * @author marc
 */
public class ComparadorDocumentos  {
    public static final ComparadorDocumentos singleton = new ComparadorDocumentos();
    private ComparadorDocumentos(){}

    /**
     * Funcion para devolver un grado de similitud entre dos documentos
     * @param d1 Primer de los documentos a comparar
     * @param d2 Segundo de los documentos a comparar
     * @param author autor del primer documento
     * @param title titulo del primer documento
     * @param author2 autor del segundo documento
     * @param title2 titulo del segundo documento
     * @return 0 <= grado <= 1.
     */
    public Double GradoSimilitud(List<String> d1, List<String> d2, String author, String title, String author2, String title2) {
        float samewords = 0;
        float totalwords = 0;
        for (String a: d1) { totalwords += ContenidoIndexado.singleton.getWeight(a, title, author);
        }
        for (String a: d2) { totalwords += ContenidoIndexado.singleton.getWeight(a, title2, author2);
        }
        for (String a : d1) {
            for (String b: d2) {
                if (a == b) {
                    float aux = (ContenidoIndexado.singleton.getWeight(a,title,author)+ ContenidoIndexado.singleton.getWeight(b,title2,author2))/(float)2.0;
                    samewords += aux;
                    totalwords -= aux;
                }
            }
        }
        Double result = samewords/((double)(totalwords));
        return result;
    }


    /**
     * Funcion para devolver un vector de String con los titulos de los k documentos mas parecidos a T
     * @param title titulo del documento original
     * @param author autor del documento original
     * @param k tamaño del vector de titulos
     * @return Vector de Strings. De tamaño <= k
     */
    public ArrayList<Pair<Double,Pair<String,String>> > comparador(String title, String author, int k) {
        ArrayList<Pair<Double,Pair<String,String>> >grades = new ArrayList<>();
        ArrayList<String> Twords = ContenidoIndexado.singleton.getWords(title, author);
        ArrayList<Pair<String, String>> D = ControladorDominio.singleton.getAllTitles();
        for(Pair<String,String> c: D) {
            if(!(c.snd.equals(title) && c.fst.equals(author))) {
                Double g = GradoSimilitud(Twords, ContenidoIndexado.singleton.getWords(c.snd, c.fst), author, title, c.fst, c.snd);
                Pair<Double,Pair<String,String>> p = new Pair<Double,Pair<String,String>>(g,c);
                if((g != null) && (g > 0.0)) grades.add(p);
            }
        }
        int size =  Math.min(k, grades.size() );
        Collections.sort(grades, comparador());
        return new ArrayList<>(grades.subList(0, size));

    }

    /**
     *Función Comparator para poder ordenar una secuencia de Pair<Double,Pair<String,String>>
     */

    private static final Comparator<Pair<Double,Pair<String,String>>> comparador() {
        Comparator<Pair<Double,Pair<String,String>>> comp = new Comparator<Pair<Double,Pair<String,String>>>(){
            @Override
            public int compare(Pair<Double,Pair<String,String>> s1, Pair<Double,Pair<String,String>> s2)
            {
                return s2.fst.compareTo(s1.fst);
            }
        };
        return comp;
    }
}