package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        File f = new File("saida.txt");
        PrintWriter pw = new PrintWriter(f);
        
        /*System.out.println("Digite a taxa de mutacao: (padrao 0,01)");
        double taxa = sc.nextDouble();
        
        System.out.println("Digite o numero de geracoes: (padrao 500)");
        int geracoes = sc.nextInt();
        
        System.out.println("Saida detalhada? ('false' = nao ou 'true' = sim)");
        boolean out = sc.nextBoolean();
        
        System.out.println("Parar de rodar o generico apos achar a saida? ('false' = nao ou 'true' = sim)");*/
        boolean finaliza = true;
        
        Genetico gen = new Genetico(0.1, 50000, 41, 50, false, finaliza);
        int[] in = {0, 0};
        AStar a = new AStar(in, gen.getEnd());
        System.out.print("Resultado:");
        pw.print("Resultado:");
        pw.print(gen.toString(finaliza));
        pw.print(a.toString());
        System.out.println(gen.toString(finaliza));
        //System.out.println(a.toString());
        pw.close();
    }
}
