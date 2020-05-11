package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Labirinto {

    private String[][] lab;

    public Labirinto() throws FileNotFoundException {
        File f = new File("lab10.txt");
        Scanner sc = new Scanner(f);

        int tam = sc.nextInt();

        lab = new String[tam][tam];

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                lab[i][j] = sc.next();
            }
        }
    }

    public String[][] getLabirinto() {
        return lab;
    }
}
