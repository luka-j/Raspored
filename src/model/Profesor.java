package model;

/**
 *
 * @author Luka
 */
public class Profesor {
    final String ime;
    final String predmet;
    final int rowIndex;
    
    Profesor(String ime, String predmet, int rowIndex) {
        this.ime = ime;
        this.predmet = predmet;
        this.rowIndex = rowIndex;
    }
}
