
// © 2025 Alessio Severi — vedi licenza nel file Main.java

package bingo;


import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Bingo {
    

    final int ID_SACCHETTO = 0;             // indice 0
    final int tot_numeri= 0;                // indice 0

    private int id_utente= ID_SACCHETTO;

    // utenti: utente sacchetto e concorrenti
    private final Map<Integer, CartellaUtente> utenti = new LinkedHashMap<>();


    // premi
    private static final Map<Integer, String> premi = Map.of(
        2, "Ambo",
        3, "Terno",
        4, "Quaterna",
        5, "Cinquina",
        6, "Bingo"
        
    );

    int current_premio= 2;

    private int RIGHE, COLONNE;


    {
        RIGHE= 18;
        COLONNE= 5;

        creaCartella();


        RIGHE= 3;
        COLONNE= 5;

    }


    

    public void creaCartella(){


        // stream.toList() restituisce un’istanza di java.util.ImmutableCollections,
        // che non supporta operazioni come shuffle() o swap(): Collections.swap(lista, i, j);

        // opzione 1: List<Integer> numeri = new ArrayList<>(IntStream.range(1, 91).boxed().toList());
        // opzione 2: List<Integer> numeri = IntStream.range(1, 91).boxed().collect(Collectors.toList());
        // opzione 3: List<Integer> numeri = IntStream.range(1, 91).boxed().collect(Collectors.toCollection(ArrayList::new));
        List<Integer> numeri = IntStream.range(1, 91).boxed().collect(Collectors.toList());
        
        Collections.shuffle(numeri);

        Integer[][] value = IntStream.range(0, RIGHE)
                                     .mapToObj(i -> numeri.subList(i * COLONNE, (i + 1) * COLONNE).toArray(Integer[]::new))
                                     .toArray(Integer[][]::new);

        
        
       
        // dati utente del sacchetto (o sacchetto virtuale) e utenti: in un campo la cartella o sacchetto 
        // e in un altro il conteggio iniziale a 0
        // dei numeri coperti per ogni riga (corrispondente ad ogni elento dell'array), per gli utenti, 
        // e per il sacchetto i numeri totali usciti (il conteggio) all'indice 0 dell'array
        CartellaUtente cartella_utente = new CartellaUtente(value, 0);

        // catalogati nella mappa
        utenti.put(id_utente++, cartella_utente);

    }

    public Map<Integer, CartellaUtente> getUtenti() {
        return utenti;
    }

    public boolean estraiNumero(){


        // numeri del sacchetto terminati senza nessun Bingo
        if (utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri] == 90) {

            System.out.println("\n\nSono stati estratti tutti i numeri, ma nessun concorrente ha fatto Bingo!");

            return false;
        }


        // inizio estrazione numero e aggiornamento sacchetto
        //      sacchetto: numero corrente che è estratto dal sacchetto (dalla matrice): riga 97
        //      contatore del numero delle estrazioni dei numeri dal sacchetto: riga 100
        int i= utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri] / COLONNE;
        int j= utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri] % COLONNE;

        Integer numb_est= utenti.get(ID_SACCHETTO).cartella()[i][j];

        System.out.println("\n\n Il numero estratto è: " + numb_est);

        
        utenti.get(ID_SACCHETTO).setNumeri_count(tot_numeri, utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri] + 1);

        
        // controllo e aggiornamento cartella per ogni utente e assegnazione premi
        // con aggiornamenti annessi
        int index;
        for (int k = 1; k < utenti.size(); k++) {
            
            // controllo cartella
            // NB: le variabili catturate da una lambda devono essere final o effectively final
            //     userIndex è final, mentre numb_est è effectively final
            final int userIndex = k;

            index = IntStream.range(0, RIGHE * COLONNE)
                             .filter(pos -> {
                                 
                                        int r = pos / COLONNE;
                                        int c = pos % COLONNE;
                                        return utenti.get(userIndex).cartella()[r][c].equals(numb_est);

                                    })
                                    .findAny()
                                    .orElse(-1); // se -1, il numero non è stato trovato


            // aggiornamento cartella
            if(index != -1){

                index= index / COLONNE;         // riga i-esima

                utenti.get(k).setNumeri_count(index, (utenti.get(k).getNumeri_count()[index]) + 1);

                    

                // controllo assegnazione premi
                if(current_premio == Arrays.stream(utenti.get(k).getNumeri_count()).reduce(0, Integer::max)){

                    System.out.println("\n\nIl concorrente " + k + " ha fatto " + premi.get(current_premio) + " alla riga " + (index + 1));
                    stampaCartella(k, "\nLa cartella del concorrente è:\n");
                    stampaNumEstratti();

                    current_premio++;

                }
                else if(Arrays.stream(utenti.get(k).getNumeri_count()).allMatch( l -> l == 5)){

                    System.out.println("\n\nIl concorrente " + k + " ha fatto " + premi.get(current_premio) + " e ha vinto!");
                    stampaCartella(k, "\nLa cartella del concorrente è:\n");
                    stampaNumEstratti();

                    return false;
                }
                  
                 
            }
               
        }

        
        return true;
    }



    public int getID_SACCHETTO() {
        return ID_SACCHETTO;
    }


    public void stampaCartella(int k, String message){

        System.out.println(message);

        for(Integer[] riga : utenti.get(k).cartella()){

            for(Integer ele : riga)

                System.out.print(String.format("%6d", ele));

            System.out.println();


        }

        System.out.println();

    }



    public void stampaNumEstratti(){


        System.out.println("\nElenco dei " + utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri] + " numeri estratti dal sacchetto:\n");

        Arrays.stream(utenti.get(ID_SACCHETTO).cartella()).flatMap(Arrays::stream).limit(utenti.get(ID_SACCHETTO).getNumeri_count()[tot_numeri])
                    .forEach(n -> System.out.print(n + " "));


        System.out.println("\n");

    }

    
}







