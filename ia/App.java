package ia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o path do labirinto:");
        String path = sc.next();
        Labirinto lab = Labirinto.labirintoInstance(path);

        System.out.println("Digite o path do labirinto:");
        String saida = sc.next();
        File f = new File("saida.txt");
        PrintWriter pw = new PrintWriter(f);

        System.out.println("Digite a taxa de mutacao:");
        double taxa = sc.nextDouble();

        System.out.println("Digite o numero de geracoes:");
        int geracoes = sc.nextInt();

        System.out.println("Digite o tamanho da populacao:");
        int tamanhoPop = sc.nextInt();

        System.out.println("Frequncia dos logs: (a cada x geracoes)");
        int freqLog = sc.nextInt();

        Genetico genetico = new Genetico(taxa, geracoes, tamanhoPop, freqLog);

        AStar aStar = new AStar(new int[]{0, 0}, genetico.getExit());

        System.out.print("Resultado:");
        System.out.println(genetico.toString());
        System.out.println(aStar.toString());

        pw.print("Resultado:");
        pw.print(genetico.toString());
        pw.print(aStar.toString());

        pw.close();
    }
}
