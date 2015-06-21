/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texto;

import java.io.IOException;

/**
 *
 * @author emerson
 */
public class main {
    public static void main(String[] args) throws IOException{
        LeerFichero f = new LeerFichero();
        System.out.print(f.muestraContenido("hola.txt"));
    }
   
}
