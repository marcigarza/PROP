import java.util.*;

/**
 * Clase para generar lista de documentos que satisfacen una consulta booleana
 * @author david
 */
public class InterpreteBooleano {

    /**
     * Función para hacer la interseccion de dos listas de documentos
     * @param list1 lista en la que se guardará la intersección
     * @param list2 lista con la que se hace la interseccion de la lista 1
     */
    private boolean intersection(ArrayList<Pair<String,String>> list1, ArrayList<Pair<String,String>> list2) {
        if (list1.retainAll(list2)) return true;
        return false;
    }

    /**
     * Función para hacer la unión de dos listas de documentos
     * @param list1 lista en la que se guardará la intersección
     * @param list2 lista con la que se hace la interseccion de la lista 1
     */
    private void union (ArrayList<Pair<String,String>> list1, ArrayList<Pair<String,String>> list2) {
        for (Pair<String,String> t : list2) {
            if(!list1.contains(t)) {
                list1.add(t);
            }
        }
        return;
    }

    /**
     * Función para hacer print de lista de Strings
     * @param list lista que se hace print
     */
    private void printListString (List <String> list){
        for (int i = 0; i < list.size(); ++i){
            System.out.println(list.get(i));
        }
    }

    /**
     * Función para hacer print de lista de fakeDocument
     * @param list lista que se hace print
     */
    private void printListfakeDocument (List<Documento> list){
        for (int i = 0; i < list.size(); ++i){
            System.out.println(list.get(i).getTitle());
        }
    }

    /**
     * Función que coje el string array del imput y devuelve una list con cada string
     * @param strArray String array del input
     * @return una list con cada una de las strings del input separadas
     */
    public ArrayList<String> makeList (String[] strArray){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0; i < strArray.length; i++){
            //System.out.println("Here with " + strArray[i]);
            char[] charArray = strArray[i].toCharArray(); //last position of char array
            int startingpoint = 0;
            boolean stop = false;
            for (int j = 0; j < charArray.length & !stop; ++j){ //check strange chars at start
                if (charArray[j] == '(' | charArray[j] == ')' | charArray[j] == '\"' | charArray[j] == '&'
                        | charArray[j] == '|' | charArray[j] == '!' | charArray[j] == '{' | charArray[j] == '}' | charArray[j] == ' '){
                    ++startingpoint;
                    String sign = String.valueOf(charArray[j]);
                    if (!sign.isEmpty())list.add(sign);
                }
                else {
                   stop = true;
                }
            }
            stop = false;
            int endingpoint = 0;
            for (int j = charArray.length-1; j > 0 & !stop; --j){ //check strange chars at start
                if (charArray[j] == '(' | charArray[j] == ')' | charArray[j] == '\"' | charArray[j] == '&'
                        | charArray[j] == '|' | charArray[j] == '!' | charArray[j] == '{' | charArray[j] == '}'){
                    ++endingpoint;
                }
                else {
                    stop = true;
                }
            }
            if (charArray.length - startingpoint - endingpoint < 0) return null;
            char[] aux = new char[charArray.length - startingpoint - endingpoint];
            int k = 0;
            for (int j = startingpoint; j < charArray.length - endingpoint ; ++j){
                aux[k] = charArray[j];
                ++k;
            }
            String word = String.valueOf(aux);
            if (!word.isEmpty())list.add(word);
            if(endingpoint > 0){
                for (int j = charArray.length - endingpoint; j < charArray.length; ++j){
                    String sign = String.valueOf(charArray[j]);
                    if (!sign.isEmpty()) list.add(sign);
                }
            }
        }
        return list;
    }

    /**
     * Función que encuentra los documentos que contienen las palabras de list
     * @param list contiene las palabras que tenemos que encontrar
     * @return una map con key igual al titulo del documento y key Documento
     */
    private ArrayList<Pair<String,String>> findDocumentSequenceOfWords (List <String> list){
        ArrayList <Pair<String,String>> docWithWords;
        docWithWords = findDocumentSeparateWords(list);
        String sequence = "";
        boolean first = true;
        for(String word : list){
            if (first){
                sequence = word;
                first = false;
            }
            else sequence += " "+word;
        }
        ArrayList <Pair<String,String>> returnArray = new ArrayList<>();
        for (int i = 0; i < docWithWords.size(); ++i){
            ContenidoDocumento a = ControladorDominio.singleton.getContent(docWithWords.get(i).fst, docWithWords.get(i).snd);
            if (a.containsSequence(sequence)) returnArray.add(docWithWords.get(i));
        }
        return returnArray;
    }

    /**
     * Función que encuentra los documentos que contienen las palabras de list
     * @param list contiene las palabras que tenemos que encontrar
     * @return una map con key igual al titulo del documento y key Documento
     */
    private ArrayList <Pair<String,String>> findDocumentSeparateWords (List <String> list){
        ArrayList <Pair<String,String>> total;
        ArrayList <Pair<String,String>> aux = ContenidoIndexado.singleton.getDocumentsWith(list.get(0));
        total = new ArrayList<Pair<String,String>>(aux);

        for (int i = 1; i < list.size(); ++i){
            ArrayList<Pair<String,String>> aux1 = ContenidoIndexado.singleton.getDocumentsWith(list.get(i));

            intersection(total, aux1);
        }
        return total;
    }

    /**
     * Función para processar el parentesis lo que será la ArrayList final
     * @param documentList la lista de documentos sin haber processado ni los parentesis ni los operandos
     * @return la ArrayList con los autores y documentos final
     */
    private ArrayList<Pair<String,String>> processParenthesis (ArrayList<Pair<String, ArrayList<Pair<String,String>>>> documentList){
        ListIterator listIterator = documentList.listIterator();
        /*System.out.println("starting processPar");
        for (int i = 0; i < documentList.size(); ++i){
            System.out.println(documentList.get(i).fst);  //Uncomment to test processParenthesis
            printListString(documentList.get(i).snd);
        }
        System.out.println("--------------------");*/
        for (int i = 0; i < documentList.size(); ++i){
            listIterator.next();
            if (documentList.get(i).fst.equals("(")){
                ArrayList<Pair<String, ArrayList<Pair<String,String>>>> aux = new ArrayList<>();
                int k = 1;
                boolean stop = false;
                while ((listIterator.hasNext() && (!stop))){
                    listIterator.remove();
                    listIterator.next();
                    if (documentList.get(i).fst.equals("(")) {
                        ++k;
                        aux.add(documentList.get(i));
                    }
                    else if (documentList.get(i).fst.equals(")")){
                        --k;
                        if (k == 0) stop = true;
                        if (!stop) aux.add(documentList.get(i));
                    }
                    else {
                        aux.add(documentList.get(i));
                    }
                }
                ArrayList<Pair<String,String>> aux2 = processParenthesis (aux);
                documentList.set(i, Pair.of("wordSequence", aux2));
            }
        }
        ArrayList<Pair<String,String>> returnArray;
        returnArray = documentList.get(0).snd;
        for (int i = 0; i < documentList.size(); ++i){
            if (documentList.get(i).fst.equals("&")){
                ++i;
                intersection(returnArray, documentList.get(i).snd); // error control
            }
            else if (documentList.get(i).fst.equals("|")){
                ++i;
                union (returnArray, documentList.get(i).snd);
            }
        }
        return returnArray;
    }

    /**
     * Funcion para comprobar si la ArrayList es una busqueda válida
     * @param list contiene la lista que se quiere comprobar si es válida
     * @return un booleano que indica si es válido o no
     */
    private boolean isValidrec (ArrayList<String> list){
        if (list == null) return false;
        boolean simbolExpected = false;
        boolean inpar = false;
        for (int i = 0; i < list.size(); ++i){
            if (list.get(i).equals("\"")){
                if (simbolExpected){
                    return false;
                }
                else simbolExpected = true;
                ++i;
                while (!list.get(i).equals("\"")){
                    ++i;
                    if (i == list.size()) return false;
                }

            }
            else if (list.get(i).equals("{")){
                if (simbolExpected) {
                    return false;
                }
                else if (!inpar) simbolExpected = true;
                ++i;
                while (!list.get(i).equals("}")){
                    if (list.get(i).equals("\"")) return false;
                    if (list.get(i).equals("{")) return false;
                    if (list.get(i).equals("!")) return false;
                    if (list.get(i).equals("(")) return false;
                    if (list.get(i).equals(")")) return false;
                    if (list.get(i).equals("&")) return false;
                    if (list.get(i).equals("|")) return false;
                    ++i;
                    if (i == list.size()) return false;
                }
            }
            else if (list.get(i).equals("!")){
                if (simbolExpected) return false;
            }
            else if (list.get(i).equals("(")) {
                int k = 1;
                boolean stop = false;
                ArrayList <String> aux = new ArrayList<>();
                ++i;
                while ((i < list.size()) && (!stop)){
                    if (list.get(i).equals("(")) {
                        ++k;
                        aux.add(list.get(i));
                    }
                    else if (list.get(i).equals(")")){
                        --k;
                        if (k == 0) stop = true;
                        if (!stop) aux.add(list.get(i));
                    }
                    else {
                        aux.add(list.get(i));
                    }
                    ++i;
                }
                if (k > 0) return false;
                --i;
                if (!isValidrec(aux)) return false;
                simbolExpected = true;
            }
            else if (list.get(i).equals("&")) {
                if (!simbolExpected) return false;
                else simbolExpected = false;
            }
            else if (list.get(i).equals("|")) {
                if (!simbolExpected) return false;
                else simbolExpected = false;
            }
            else  {
                if (simbolExpected) return false;
                else simbolExpected = true;
            }

        }
        if (!simbolExpected) return false;
        return true;
    }

    /**
     * Funcion para comprobar si la String es una busqueda válida
     * @param str contiene el string que se comprobará si es válido o no
     * @return un booleano que indica si str es válido o no
     */
    public boolean isValid (String str){
        String[] strArray = str.split("\\s+");
        ArrayList <String> aux = makeList(strArray);
        //printListString(aux);
        if (aux == null) return false;
        return isValidrec(aux);
    }

    /**
     * Función interpreta la consulta booleana del input
     * @param input contiene la consulta booleana
     * @return "acabará devolviendo una lista de Documentos"
     */
    public ArrayList <Pair<String,String>> interpretarConsultaBooleana(String input) {
        String[] strArray = input.split("\\s+");

        ArrayList<String> list = makeList(strArray);

        ArrayList<String> documents;
        boolean simbolExpected = false;
        ArrayList<Pair<String, ArrayList<Pair<String,String>>>> allDocuments = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i){
            ArrayList<Pair<String,String>> documentsFound = new ArrayList<>();
            if (list.get(i).equals("\"")){
                List<String> aux = new ArrayList<String>();
                ++i;
                while (!list.get(i).equals("\"")){
                    aux.add(list.get(i));
                    ++i;
                }
                documentsFound = findDocumentSequenceOfWords (aux);
                allDocuments.add(Pair.of("wordSequence", documentsFound));
            }
            else if (list.get(i).equals("{")){
                List<String> aux = new ArrayList<String>();
                ++i;
                while (!list.get(i).equals("}")){
                    aux.add(list.get(i));
                    ++i;
                }
                documentsFound = findDocumentSeparateWords (aux);
                allDocuments.add(Pair.of("wordSequence", documentsFound));
            }
            else if (list.get(i).equals("!")){
                ++i;
                documentsFound = ContenidoIndexado.singleton.getDocumentsWithout(list.get(i));
                allDocuments.add(Pair.of("wordSequence", documentsFound));
            }
            else if (list.get(i).equals("(")) {
                documentsFound = new ArrayList<>();
                documentsFound.add(Pair.of("(", ""));
                allDocuments.add(Pair.of("(", documentsFound));
            }
            else if (list.get(i).equals(")")) {
                documentsFound = new ArrayList<>();
                documentsFound.add(Pair.of(")", ""));
                allDocuments.add(Pair.of(")", documentsFound));
            }
            else if (list.get(i).equals("&")) {
                documentsFound = new ArrayList<>();
                documentsFound.add(Pair.of("&", ""));
                allDocuments.add(Pair.of("&", documentsFound));
            }
            else if (list.get(i).equals("|")) {
                documentsFound = new ArrayList<>();
                documentsFound.add(Pair.of("|", ""));
                allDocuments.add(Pair.of("|", documentsFound));
            }
            else {
                ArrayList <String> aux = new ArrayList<>();
                aux.add(list.get(i));
                documentsFound = findDocumentSeparateWords (aux);
                allDocuments.add(Pair.of("wordSequence", documentsFound));
            }
        }
        ArrayList <Pair<String, String>> DocList = processParenthesis (allDocuments);

        return DocList;
    }
}
