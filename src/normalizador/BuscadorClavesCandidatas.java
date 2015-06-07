package normalizador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuscadorClavesCandidatas {

    private final HashMap<String, ArrayList<Character>> mapaDFs = new HashMap<>();
    private final ArrayList<Character> universo = new ArrayList<>();

    
    public ArrayList<String> obtenerLlaves() {

        // Initialize variables
        ArrayList<String> clavesCandidatas = new ArrayList<>();
        ArrayList<Character> clausura;
        StringBuffer combinacion;
        GeneradorCombinaciones cg;
        int[] indices;

        // Analizaremos todas las posibles combinaciones de claves
        for (int i = 1; i <= universo.size(); i++) {
            //System.out.println("FOR PRINCIPAL I=" + i );
            
            //Nos dará todas las posibles combinaciones de claves
            cg = new GeneradorCombinaciones(universo.size(), i);

            //Se analizará cada una de las claves generadas
            while (cg.hasMore()) {//Verifica si existen más grupos de claves a analizar
                //System.out.println("cg.hasMore() I=" + i );
                
                clausura = new ArrayList<>();
                combinacion = new StringBuffer();
                indices = cg.getNext();
                
                /*System.out.println("Indices I=" + i );
                for(int g=0; g<indices.length; g++){
                    System.out.print(indices[g] + ", ");
                }
                System.out.println();*/
                
                // Generamos la clave en base a la combinación obtenida del generador de combinaciones
                //System.out.println("agregando a combinacion los indices del universo -- I=" + i );
                for (int j : indices) {
                    combinacion.append(universo.get(j));
                }

                //La combinación se agrega al lado izquierdo de la DF
                for (int k = 0; k < combinacion.length(); k++) {
                    clausura.add(combinacion.charAt(k));
                }
                
                //Se buscará la clausura [elementos a los que puede llegar esta clave en base a las DFs] de la clave en la variable clausura
                int size;
                do {
                    size = clausura.size();
                    clausura(clausura, clausura, 1, clausura.size());
                    mapaDFs.put(combinacion.toString(), clausura);
                } while (size < clausura.size());

                //Verifica que la clave sea una clave candidata; si lo es, la mete en la lista de claves candidatas
                if (esClave(clausura)) {
                    clavesCandidatas.add(combinacion.toString());
                }
            }

            //Revisa si se encontraron claves candidatas; si hay, retorna la lista de claves candidatas
            if (!clavesCandidatas.isEmpty()) {
                return clavesCandidatas;
            }
        }
        return null;
    }

    /* 
     * Ordena de manera alfabetica una cadena y la retorna.
     */
    private StringBuffer OrdenarCadena(String cadena) {
        char[] a_ordenar = cadena.toCharArray();
        Arrays.sort(a_ordenar);
        return new StringBuffer(new String(a_ordenar));
    }

    /* 
     * Determina si una clave es candidata. Revisa la lista de los elmentos
     * a los que la clave candidata 'alcanza' o 'llega' y la compara con el
     * universo. Si todos los elementos del universo están presentes en la
     * cadena recibida, entonces es una clave candidata y retorna true.
     */
    private boolean esClave(ArrayList<Character> elementos_a_revisar) {
        return universo.stream().noneMatch((c) -> (!elementos_a_revisar.contains(c)));
    }

    /* 
     * Recibe una cadena con los elementos del universo, los separa y los agregar al arraylist universo
     */
    public void agregarUniverso(String attributes) {
        String[] elementos = attributes.split(",");
        for (String s : elementos) {
            universo.add(s.charAt(0));
        }
    }

    /* 
    * Agrega las dependencias funcionalas al hashmap
    */
    public void agregarDFs(String fds) {
        //Separa las DFs
        String[] dfs = fds.split(",");
        
        //Separa en lado derecho e izquierdo la DF
        for (String s : dfs) {
            String[] split = s.split("->");
            String lado_derecho = split[1];
            String lado_izquierdo = split[0];
            
            //Verifica que no exista el lado derecho de las DFs como clave en el hashmap
            ArrayList<Character> ld_check = mapaDFs.get(lado_derecho);
            if (ld_check == null) {
                ld_check = new ArrayList<>();
            }

            //Ordena de manera alfabetica el lado izquierdo de la DF para evitar redundancia en el hashmap
            lado_izquierdo = OrdenarCadena(lado_izquierdo).toString();

            for (int i = 0; i < lado_derecho.length(); i++) {
                ld_check.add(lado_derecho.charAt(i));
            }

            mapaDFs.put(lado_izquierdo, ld_check);
        }
    }

   /* 
    * Elimina los datos en el hashmap y el arraylist con el universo
    */
    public void vaciar() {
        universo.clear();
        mapaDFs.clear();
    }
    
    /* 
     * Algoritmo que calcula la clausura de una clave. 
     */
    private void clausura(ArrayList<Character> one, ArrayList<Character> clausura,
                         int inicio, int fin) {

        GeneradorCombinaciones cg;
        StringBuffer combinacion;
        int[] indices;
        
        // Analiza todas las posibles claves en base al tamaño indicado (fin-inicio)
        for (int i = inicio; i <= fin; i++) {
            cg = new GeneradorCombinaciones(one.size(), i);
            combinacion = new StringBuffer();

            while (cg.hasMore()) {
                combinacion = new StringBuffer();
                indices = cg.getNext();
                /*for(int g=0; g<indices.length; g++){
                    System.out.print(indices[g] + ", ");
                }
                System.out.println();*/
                
                //System.out.println("agregando a combinacion los indices del universo");
                for (int j : indices) {
                    combinacion.append(one.get(j));
                }
                //System.out.println(combinacion);
                
                combinacion = OrdenarCadena(combinacion.toString());
                //System.out.println(combinacion);
                
                ArrayList<Character> strFD = mapaDFs.get(combinacion.toString());
                if (strFD == null) {
                    //System.out.println("aqui");
                    continue;
                }

                strFD.stream().filter((c) -> (!clausura.contains(c))).forEach((c) -> {
                    //System.out.println("Me voy al otro clausura");
                    clausura(c, clausura);
                });
            }
        }
    }
    
     /* 
     * Calcula la clausura de un elemento de manera recursiva.
     */
    private void clausura(char one, ArrayList<Character> clausura) {

        clausura.add(one);//Agrega el elemento analizado a la lista de clausura
        ArrayList<Character> oneFD = mapaDFs.get(String.valueOf(one));//Revisa si el elemento tiene DFs

        // Si el elemento no tiene DF, lo retorna
        if (oneFD == null) {
            //System.out.println("no encontre DF");
            return;
        }

        //Si tiene una DF, recursivamente, se calculará su clausura
        oneFD.stream().filter((c) -> (!clausura.contains(c))).forEach((c) -> {
            clausura(c, clausura);
        });
    }
}
