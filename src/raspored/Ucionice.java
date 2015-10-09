package raspored;

import java.io.File;
import static raspored.Odeljenja.getCas;
import static raspored.Odeljenja.getDan;

/**
 *
 * @author luka
 */
public class Ucionice {
    private final String broj;
    
    public Ucionice(String broj) {
        Raspored.out = new String[27][9];
        Raspored.out[0][1] = "Ponedeljak";
        Raspored.out[0][2] = "Utorak";
        Raspored.out[0][3] = "Sreda";
        Raspored.out[0][4] = "Četvrtak";
        Raspored.out[0][5] = "Petak";
        for(int i=1; i<14; i++)
            if(Raspored.outVreme)
                Raspored.out[i][0] = Raspored.podaci[0][i+1][1]; 
            else
                Raspored.out[i][0] = String.valueOf(i-1);
        this.broj = broj;
    }
    
    public void obrada() {
        Raspored.outFile = new File(Raspored.outFolder + "raspored uc" + broj + ".xlsx");
        for(int i=2; i<Raspored.podaci[0].length; i++) {
            for(int j=2; j<Raspored.podaci[0][i].length; j++) {
                String[] split = Raspored.podaci[0][i][j].split("/"); String ucionica;
                if(split.length>1) {
                    ucionica = split[1].split(" ")[0];
                } else ucionica="108"; //sala
                if(ucionica.contains(broj))
                    format(j, i);
            }
        }
    }
    
    private void format(int red, int kolona) {
        String odeljenje = Raspored.podaci[0][kolona][red].split("/")[0].split("_")[0].split(" ")[0];
        String predmet = Raspored.podaci[0][1][red];
        int outi1, outi2=-1;
        for(int i=1; i<6; i++) 
            if(Raspored.out[0][i].equals(getDan(kolona)))
                outi2 = i;
        outi1 = Integer.valueOf(getCas(kolona))+1;
        Raspored.out[outi1][outi2] = odeljenje + "/ " + predmet;
    }
}
