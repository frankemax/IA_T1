package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Labirinto {

    private final String[][] labirinto;
    private static Labirinto instance;

    private Labirinto(String path) throws FileNotFoundException {
        File f = new File(path);
        Scanner sc = new Scanner(f);

        int tam = sc.nextInt();

        labirinto = new String[tam][tam];

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                labirinto[i][j] = sc.next();
            }
        }
    }

    public static Labirinto labirintoInstance(String filepath) throws FileNotFoundException {
        if( instance == null ){
            instance = new Labirinto(filepath);
        }
        return instance;
    }

    public String[][] getLabirinto() {
        return labirinto;
    }

    public int getEmpty() {

        int count = 0;
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                if (labirinto[i][j].equals("0")) {
                    count++;
                }
            }
        }
        return count + 1;
    }
}
