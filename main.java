import java.io.FileNotFoundException;

public class main {

    public static void main(String[] args) throws FileNotFoundException {
        Labirinto labirinto = new Labirinto();
        String lab[][] = labirinto.getLabirinto();
        
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab[0].length; j++) {
                System.out.print(lab[i][j]+" ");
            }
            System.out.println();
        }
    }
}
