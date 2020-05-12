package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("saida.txt");
        PrintWriter pw = new PrintWriter(f);
        Genetico gen = new Genetico(0.01, 1000, 5, 50, false);
        int[] in = {0, 0};
        AStar a = new AStar(in, gen.getEnd());
        System.out.print("Resultado:");
        pw.print("Resultado:");
        pw.print(gen.toString());
        pw.print(a.toString());
        System.out.println(gen.toString());
        System.out.println(a.toString());
        pw.close();
    }
}
