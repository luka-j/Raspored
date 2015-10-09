package raspored;

import sun.print.RasterPrinterJob;

import java.io.File;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luka
 */
public class Odeljenja {
    /**
     * Poslednja 'verzija' rasporeda je došla bez upisanih predmeta (samo imena profesora). Zašto, kako, nemam pojma.
     */
    public static final boolean HAS_SUBJECTS = false;

    private final String razred;
    private final Map<String, String> nastavnici;
    
    Odeljenja(String razred) {
        Raspored.out = new String[27][9];
        Raspored.out[0][1] = "Ponedeljak";
        Raspored.out[0][2] = "Utorak";
        Raspored.out[0][3] = "Sreda";
        Raspored.out[0][4] = "Četvrtak";
        Raspored.out[0][5] = "Petak";
        //Raspored.out[0][6]="";
        Raspored.out[0][7] = "Predmet:";
        Raspored.out[0][8] = "Nastavnik:";
        for(int i=1; i<14; i++)
            if(Raspored.outVreme)
                Raspored.out[i][0] = Raspored.podaci[0][i+1][1]; 
            else
                Raspored.out[i][0] = String.valueOf(i-1);
        this.razred=razred.toLowerCase();
        nastavnici=new HashMap<>();
    }
    
    public void obrada() {
        findRazred();
        Iterator<String> iter = nastavnici.keySet().iterator();
        String next;
        if(HAS_SUBJECTS) {
            for (int i = 0; i < nastavnici.size(); i++) {
                next = iter.next();
                Raspored.out[i + 1][7] = next;
                Raspored.out[i + 1][8] = nastavnici.get(next).substring(2);
            }
        }
    }
    
    private void findRazred() {
        Raspored.outFile = new File(Raspored.outFolder + "raspored " + razred + ".xlsx");
        for(int i= HAS_SUBJECTS ? 2 : 1; i<Raspored.podaci[0].length; i++) {
            for(int j=2; j<Raspored.podaci[0][i].length; j++) {
                try {
                String[] odeljenja = Raspored.podaci[0][i][j].split("/")[0].split("-");
                    for(String odeljenje : odeljenja) {
                        if (odeljenje.startsWith(razred))
                            format(j, i);
                    }
                } catch(NullPointerException ex) {
                    System.out.println("NPE; i: " + i + "j: " + j);
                    System.out.println("raw: " + Raspored.podaci[0][i][j]);
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
    
    private void format(int red, int kolona) {
        String[] ucionica = Raspored.podaci[0][kolona][red].split("/");
        if(ucionica.length == 1)
            ucionica[0] = "108";
        else
            ucionica[1] = ucionica[1].trim();
        int outi2 = 0, outi1;
        for(int i=1; i<6; i++)
            if(Raspored.out[0][i].equals(getDan(kolona)))
                outi2 = i;
        outi1 = Integer.valueOf(getCas(kolona))+1;
        
        //if(Raspored.podaci[0][kolona][red].contains("R"))
        //  Raspored.out[outi1][outi2] = "COS/" + ucionica[ucionica.length-1];
        if(Raspored.podaci[0][kolona][red].matches(".*_(Ge|A|Al|F|P|R[0-9]*)/.*")) {
            Raspored.out[outi1][outi2] = "Mentorska";
        }
        /*if(Raspored.podaci[0][kolona][red].contains("_Ge") || Raspored.podaci[0][kolona][red].contains("_A/")
                || Raspored.podaci[0][kolona][red].contains("_Al/") || Raspored.podaci[0][kolona][red].contains("_F/")
                || Raspored.podaci[0][kolona][red].contains("_P/") || Raspored.podaci[0][kolona][red].contains("_R/"))
            Raspored.out[outi1][outi2] = "Mentorska";*/
        else
        if(HAS_SUBJECTS) {
            Raspored.out[outi1][outi2] = Raspored.podaci[0][1][red] + "/" + ucionica[ucionica.length-1];
        } else {
            Raspored.out[outi1][outi2] = Raspored.podaci[0][0][red].substring(2) + "/" + ucionica[ucionica.length-1];
        }

        nastavnici.put(Raspored.podaci[0][1][red], Raspored.podaci[0][0][red]);
    }
    
    //UTILS
    
    public static String getDan(int kolona) {
        for(int i=kolona; i> (HAS_SUBJECTS ? 2 : 1); i--) {
            if(Raspored.podaci[0][i][0] != null && !Raspored.podaci[0][i][0].isEmpty())
                return Raspored.podaci[0][i][0];
        }
        return "Ponedeljak";
    }
    
    public static String getCas(int kolona) {
        int i = kolona;
        for(; i> (HAS_SUBJECTS ? 2 : 1); i--) {
            if(Integer.valueOf(Raspored.podaci[0][i][1].split(":")[0]) < 
                    Integer.valueOf(Raspored.podaci[0][i-1][1].split(":")[0])) {
                return String.valueOf(kolona-i);
            }
        }
        return String.valueOf(kolona-i);
    }
}
