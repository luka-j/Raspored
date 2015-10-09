package model;

/**
 *
 * @author Luka
 */
public class Dan {
    Vreme[] casovi;
    
    static class Vreme {
        final int redniBroj;
        final String vreme;
        final int colIndex;
        
        Vreme(int redniBroj, String vreme, int colIndex) {
            this.redniBroj=redniBroj;
            this.vreme=vreme;
            this.colIndex=colIndex;
        }
    }
}
