package ia;

import java.io.FileNotFoundException;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        Genetico gen = new Genetico(0.01,2000,20,50);
        int[] in = {0,0};
        int[] out = {18,17};
        AStar a = new AStar(in,gen.end);
    }
}
