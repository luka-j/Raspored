/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspored;

/**
 *
 * @author Luka
 */
public class Utils {
    public static String getDan(int kolona) {
        for(int i=kolona; i>2; i--) {
            if(!Raspored.podaci[0][i][0].isEmpty())
                return Raspored.podaci[0][i][0];
        }
        return "Ponedeljak";
    }
    
    public static String getCas(int kolona) {
        int i = kolona;
        for(; i>2; i--) {
            if(Integer.valueOf(Raspored.podaci[0][i][1].split(":")[0]) < 
                    Integer.valueOf(Raspored.podaci[0][i-1][1].split(":")[0])) {
                return String.valueOf(kolona-i);
            }
        }
        return String.valueOf(kolona-i);
    }
}
