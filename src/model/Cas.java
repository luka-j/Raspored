package model;

import raspored.Raspored;
import raspored.Raspored;

/**
 *
 * @author Luka
 */
public class Cas {
    final String cas;
    final String ucionica;
    final String odeljenje;
    
    Cas(Profesor p, Dan.Vreme v) {
        this(Raspored.podaci[0][v.colIndex][p.rowIndex]);
    }
    
    private Cas(String s) {
        String[] tokens = s.split("/");
        odeljenje=tokens[0];
        if(tokens.length == 1)
            tokens[0] = "108";
        else
            tokens[1] = tokens[1].trim();
        if(s.contains("R"))
            cas = "COS";
        else if(s.contains("_"))
            cas = "Mentorska";
        else 
            cas = s; 
        ucionica=tokens[tokens.length-1];
    }
    
    
}
