
// © 2025 Alessio Severi — vedi licenza nel file Main.java

package bingo;

import java.util.Arrays;

public class CartellaUtente {


    private final int RIGHE= 3;


    private final Integer[][] cartella;
    private final int[] numeri_count= new int[RIGHE];


    public CartellaUtente(Integer[][] cartella, int numeri_count) {

        this.cartella = cartella;

        Arrays.fill(this.numeri_count, numeri_count);
        /*
        for(int i=0; i < this.numeri_count.length; i++)
            this.numeri_count[i] = numeri_count;
        */

    }

    public Integer[][] cartella() {
        return cartella;
    }

    public int[] getNumeri_count() {
        return numeri_count;
    }

    public void setNumeri_count(int index, Integer n) {
        this.numeri_count[index] = n;
    }
}