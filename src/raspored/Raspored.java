package raspored;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Pre upotrebe prebaciti nazive dana u latinicu !
 * I proveriti da li se svi redovi završavaju istom kolonom (tj. tabela mora biti pravougaona)
 *
 * @author luka
 */
public class Raspored {

    /**
     * Fajl iz koga se ucitava tabela.
     */
    public static File podaciFile;
    /**
     * Fajl u koji se upisuju obradjeni podaci.
     */
    public static File outFile;
    /**
     * Oznacava vrstu tabele. Ako je true, tabela je u .xls formatu. Ako je
     * false, tabela je u .xlsx formatu
     */
    public static boolean HSSF;
    
    public static String outFolder;
    public static boolean outVreme;

    /**
     * Matrica sa podacima. Prvi index oznacava sheet (od 0), drugi kolonu (od
     * 0), a treci red(od 0)
     * podaci.length - broj sheetova; podaci[n].length - broj kolona u n-tom sheetu
     * podaci[n][m].length - broj redova u n-tom sheetu u m-toj kolonu
     */
    public static String podaci[][][];
    
    public static String out[][];

    public static void main(String[] args) {
        if (args.length != 2) {
            String in = JOptionPane.showInputDialog("Lokacija ulaznog fajla (rasporeda):");
            if (in.isEmpty()) {
                if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux"))
                    in = "/home/luka/Documents/raspored-2017.xls";
                else in = "C:/Users/luka/Documents/raspored 201617.xlsx";
            }
            podaciFile = new File(in);
            String out = JOptionPane.showInputDialog("Folder u kojem ce biti kreiran izlazni fajl:");
            if(out.isEmpty())
                if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux"))
                    out = "/home/luka/Documents/rasporedi sept17/";
                else out = "C:/Users/luka/Documents/rasporedi sept1617/";
            else if(!out.endsWith("/"))
                out+='/';
            outFolder=out;
        } else {
            podaciFile = new File(args[0]);
            outFile = new File(args[1]);
        }
        if (podaciFile.getAbsolutePath().toLowerCase().endsWith("xls")) {
            HSSF = true;
        } else if (podaciFile.getAbsolutePath().toLowerCase().endsWith("xlsx") || 
                podaciFile.getAbsolutePath().toLowerCase().endsWith("xlxs")) {
            HSSF = false;
        } else {
            System.out.println("Nije uneta lokacija xls ili xlsx fajla");
            main(new String[]{});
        }
        try {
            if (HSSF) {
                IO.readHSSF();
            } else {
                IO.readXSSF();
            }
        } catch (IOException ex) {
            System.out.print("I/O greska pri citanju tabela.\n");
            ex.printStackTrace();
        }
        
        int vreme = JOptionPane.showConfirmDialog(null, "Koristiti vreme pocetka"
                + " casa umesto rednog broja u izlaznoj tabeli?", "Format izlaza", JOptionPane.YES_NO_OPTION);
        outVreme = vreme==JOptionPane.YES_OPTION;
        System.gc();

        String in = JOptionPane.showInputDialog("Unesi odeljenje ili ucionicu: ");
        if(in.length()==3)
            new Ucionice(in).obrada();
        else if (in.length() == 2)
            new Odeljenja(in).obrada();
        else if (in.equals("test")) {
            testOdeljenja();
            testUcionice();
            System.exit(0);
        }
        
        try {
            IO.writeXSSF();
        } catch (IOException ex) {
            System.out.print("I/O greska pri pisanju tabela.\n");
            ex.printStackTrace();
        }
        System.out.println("Uradjeno.");
    }

    /**
     * Tumaci argument kao redni broj kolone i pretvara ga u odgovarajuce slovo.
     * @param colNum broj kolone za koje je potrebno slovo
     * @return slovo date kolone
     */
    public static String iToColLetter(int colNum) {
        String colLet = "";
        while (colNum >= 0) {
            int ostatak = colNum % 26;
            colLet = (char) (ostatak + 'A') + colLet;
            colNum = (colNum / 26) - 1;
        }

        return colLet;
    }

    /**
     * Tumaci argument kao slovo/slova kolone i pretvara ih u odgovarajuce indexe matrice sa podacima.
     * @param colLet slovo kolone
     * @return index kolone u matrici
     */
    public static int colLetToIndex(String colLet) {
        colLet = colLet.toUpperCase();
        int zbir = 0;
        for (int i = 0; i < colLet.length(); i++) {
            zbir *= 26;
            zbir += (colLet.charAt(i) - 'A' + 1);
        }
        return zbir - 1;
    }
    
    public static void testOdeljenja() {
        for(int i=1; i<=4; i++) {
            for(char j='a'; j<='e'; j++) {
                new Odeljenja(String.valueOf(i) + String.valueOf(j)).obrada();
                try {
                    IO.writeXSSF();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        for (int i = 7; i <= 8; i++) {
            for (char j = 'a'; j <= 'b'; j++) {
                new Odeljenja(String.valueOf(i) + String.valueOf(j)).obrada();
                try {
                    IO.writeXSSF();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static final String[] ucionice = {"002", "004", "011", "012", "100", "101", "102", "108", "200", "201", "202", "203",
    "206", "207", "208", "300", "301", "302", "303", "305", "306", "307", "308", "309", "310", "311"};
    public static void testUcionice() {
        for(String ucionica : ucionice) {
            new Ucionice(ucionica).obrada();
            try {IO.writeXSSF();} catch (IOException ex) {}
        }
    }
}
