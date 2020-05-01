package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Labirinto {
    String[][] lab;
    
    public Labirinto() throws FileNotFoundException{
        File f = new File("lab10.txt");
        Scanner sc = new Scanner(f);
        
        int tam = sc.nextInt();
        
        String[][] labirinto = new String[tam][tam];
        
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                labirinto[i][j] = sc.next();
            }
        }
        
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                System.out.print(labirinto[i][j]+" ");
            }
            System.out.println();
        }
    }
}
