package ia;

import java.io.FileNotFoundException;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        Labirinto labirinto = new Labirinto();
        String lab[][] = labirinto.getLabirinto();

        for (int i = 0; i < lab[0].length; i++) {
            for (int j = 0; j < lab[0].length; j++) {
                System.out.print(lab[i][j]+" ");
            }
            System.out.println();
        }
    }
}
