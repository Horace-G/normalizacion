/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package normalizador;

import java.util.ArrayList;

public class Normalizacion {

    public Normalizacion() {

    }

    public String PrimeraFN(String ClavePrimaria, String universo) {
        String retVal = "R(" + ClavePrimaria;
        String Universo[] = universo.split(",");
        for (int i = 0; i < Universo.length; i++) {
            if (ClavePrimaria.contains(Universo[i])) {
                continue;
            }
            retVal += "," + Universo[i];

        }
        return retVal + ")";
    }

    public ArrayList<String> SegundaFN(String ClavePrimaria, String dependencias, String universo) {
        ArrayList<String> retVal = new ArrayList();
        String Dependencias[] = dependencias.split(",");
        universo = universo.replace(",", "");

        for (int i = 0; i < Dependencias.length; i++) {
            String Dependencia[] = Dependencias[i].split("->");
            String regex = "";
            for (int j = 0; j < Dependencia[0].length(); j++) {
                regex += ".*" + Dependencia[0].charAt(j) + ".*";
            }
            if (ClavePrimaria.matches(regex)) {
                String transitivas = addTransitivas(Dependencia[1], Dependencias);
                retVal.add("R(" + Dependencias[i].replace("->", "") + transitivas.replace("(.)\\1{1,}", "$1") + ")");
                if (Dependencia.length > 1) {
                    String REMOVE = Dependencia[1] + transitivas.replace("(.)\\1{1,}", "$1");
                    for (int j = 0; j < REMOVE.length(); j++) {
                        universo = universo.replace(REMOVE.charAt(j), ' ');
                    }
                }
            }
        }

        for (int i = 0; i < ClavePrimaria.length(); i++) {
            universo = universo.replace(ClavePrimaria.charAt(i), ' ');
        }

        universo = universo.replaceAll("\\s", "");
        if (!universo.trim().isEmpty()) {
            retVal.add("R(" + ClavePrimaria + "," + universo + ")");
        }

        return retVal;
    }

    private String addTransitivas(String Clave, String[] Dependencias) {
        String retVal = "";
        for (int i = 0; i < Dependencias.length; i++) {
            String Dependencia[] = Dependencias[i].split("->");
            if (Clave.contains(Dependencia[0])) {
                if (Dependencia.length > 1) {
                    retVal += Dependencia[1];
                }
            }
        }
        return retVal;
    }

    public ArrayList<String> TerceraFN(String ClavePrimaria, String dependencias, String universo) {
        ArrayList<String> retVal = new ArrayList();
        String Dependencias[] = dependencias.split(",");
        universo = universo.replace(",", "");

        for (int i = 0; i < Dependencias.length; i++) {
            String Dependencia[] = Dependencias[i].split("->");
            String regex = "";
            for (int j = 0; j < Dependencia[0].length(); j++) {
                regex += ".*" + Dependencia[0].charAt(j) + ".*";
            }
            String REMOVE = "";
            if (ClavePrimaria.matches(regex)) {
                String transitivas = addTransitivas(Dependencia[1], Dependencias);
                if (!transitivas.isEmpty()) {
                    retVal.add("R(" + Dependencia[1] + "," + transitivas.replace("(.)\\1{1,}", "$1") + ")");
                    REMOVE = Dependencia[1] + transitivas.replace("(.)\\1{1,}", "$1");
                } else {
                    REMOVE = Dependencia[1];
                }
                retVal.add("R(" + Dependencias[i].replace("->", "") + ")");
                if (Dependencia.length > 1) {

                    for (int j = 0; j < REMOVE.length(); j++) {
                        universo = universo.replace(REMOVE.charAt(j), ' ');
                    }
                }
            }
        }

        for (int i = 0; i < ClavePrimaria.length(); i++) {
            universo = universo.replace(ClavePrimaria.charAt(i), ' ');
        }

        universo = universo.replaceAll("\\s", "");

        for (int i = 0; i < universo.length(); i++) {
            Character Key = universo.charAt(i);
            String REMOVE = "";
            String transitivas = addTransitivas(Key.toString(), Dependencias);
            if (!transitivas.isEmpty()) {
                retVal.add("R(" + Key.toString() + "," + transitivas.replace("(.)\\1{1,}", "$1") + ")");
                REMOVE = Key.toString() + transitivas;

                for (int j = 0; j < REMOVE.length(); j++) {
                    universo = universo.replace(REMOVE.charAt(j), ' ');
                }
            }
        }

        if (!universo.trim().isEmpty()) {
            retVal.add("R(" + ClavePrimaria + "," + universo + ")");
        }
        return retVal;
    }
}
