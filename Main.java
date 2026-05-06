/*
  Gioco del Bingo
  
  Package: bingo

  Descrizione:
    Simulatore completo del gioco del Bingo.
    Il programma gestisce automaticamente la creazione delle cartelle
    dei giocatori, l’estrazione dei numeri in tempo reale (“live”) e
    il salvataggio delle cartelle su file di testo.

    Include funzionalità per la generazione, scelta del numero di player,
    stampa e archiviazione delle cartelle, con possibilità di pausa tra
    le estrazioni per rendere l’esperienza di gioco più realistica.

    Ad ogni vincita viene mostrato un resoconto dettagliato
    dei numeri estratti fino a quel momento.
 
 
 Autore: Alessio Severi
 Licenza: MIT License

 MIT License

 Copyright (c) 2025 Alessio Severi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.

*/


package bingo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;



public class Main {

    

    public static void main(String[] args) {
        

        Bingo go= new Bingo();

        System.out.println("\n\nGioco del Bingo\n\n");

        

        try(Scanner sc= new Scanner(System.in)){

            System.out.print("Inserisci il numero dei partecipanti: ");

            try {


                final int N_PLAYERS= sc.nextInt();

                // consumo newline dopo nextInt()
                sc.nextLine();


                if (N_PLAYERS < 3) throw new IllegalArgumentException("Il numero dei partecipanti deve essere almeno 3. ");

                System.out.println();


                for (int i = 0; i < N_PLAYERS; i++) go.creaCartella();

                
                
                printSaveCartelle(go, createFolder(new File("bingo/cartelle_utenti")));

                
                System.out.println("\nPremi INVIO per iniziare le estrazioni...");
                sc.nextLine();


                // Ciclo di estrazione con pausa
                while (go.estraiNumero()) {
                    System.out.println("\nPremi INVIO per estrarre il prossimo numero...");
                    sc.nextLine();
                }

                
    
                // opzionale: stampa contenuto del sacchetto
                go.stampaCartella(go.getID_SACCHETTO(), "\n\nIl contenuto del sacchetto iniziale è:\n");
                



            } catch (InputMismatchException e) {

                System.out.println("\n\nNumero dei partecipanti errato, tentativo di giocare al Bingo fallito.\n");

            }
            catch (IllegalArgumentException e) {

                System.out.println("\n\n" + e.getMessage() + "Tentativo di giocare al Bingo fallito.\n");

            }

        }


        System.out.println();
        
    }


    
    private static Path createFolder(File file) {

        
        if (file.exists()) {
            
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();   // cancella contenuti
                }
            }
            file.delete();
        }

        
        try {
            Path dir = Path.of("bingo/cartelle_utenti");
            Files.createDirectories(dir);

            return dir;

        } catch (IOException e) {
               System.out.println(e.getMessage());
        }
        return null;

        

    }



    private static void printSaveCartelle(Bingo go, Path dir){

        // Stampa e salvataggio delle cartelle dei giocatori
        for (Map.Entry<Integer, CartellaUtente> entry : go.getUtenti().entrySet()) {

            int id = entry.getKey();
            if (id == go.getID_SACCHETTO()) continue; // salta il sacchetto

            
            go.stampaCartella(id, "\n\nCartella del giocatore " + id + ":\n");


            CartellaUtente c = entry.getValue();
            StringBuilder sb = new StringBuilder();


            for (Integer[] riga : c.cartella()) {

                for (Integer elem : riga)

                    sb.append(String.format("%6d", elem));
                    
                sb.append("\n");
            }


            try {
                
                // Costruisce il percorso completo del file dentro quella cartella
                Path file = dir.resolve("cartella_giocatore_" + id + ".txt");

                // Scrittura del contenuto
                Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);

                System.out.println("Salvataggio cartella in: " + file.toAbsolutePath());


            } catch (IOException e) {
                System.out.println("Errore salvataggio cartella giocatore " + id + ": " + e.getMessage());
            }
        }

        
    }


}
