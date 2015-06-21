/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texto;

/**
 *
 * @author emerson
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LeerFichero {
    
    public LeerFichero(){
        
    }
    
    public ArrayList<Character> muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        boolean letra;
        ArrayList<Character> relacion = new ArrayList();
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            for(int i = 0; i<cadena.length();i++){
                letra = Character.isLetter(cadena.charAt(i));
                if(letra == true){
                 relacion.add(cadena.charAt(i));
                 
                } 
            }
        }
        b.close();
        return relacion;
    }
 
    
    
}
