package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private String[][] lab = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> ListaLista, intermediaryListaLista;
    private double mutationRate;
    private int qtdadeDeGeracoes, qtdCromossomos, qtdadeDeFilhos;
    private boolean foundIt;
    private int[] end = new int[2];
    private String log = "\n";
    private Random random = new Random();

    public Genetico(double mutationRate, int qtdadeDeGeracoes, int qtdCromossomos, int qtdadeDeFilhos) throws FileNotFoundException {

        this.mutationRate = mutationRate;
        this.qtdadeDeGeracoes = qtdadeDeGeracoes;
        this.qtdCromossomos = qtdCromossomos;
        this.qtdadeDeFilhos = qtdadeDeFilhos;
        this.foundIt = false;

        startPopulation(qtdCromossomos, qtdadeDeFilhos);

        for (int i = 0; i < qtdadeDeGeracoes; i++) {
            if (foundIt) {
                log += "Saida encontrada na geracao " + (i - 1) + "\n";
                return;
            }
            //System.out.println(i);
            int[] aux = escolheElitismo();
            System.out.println(ListaLista.get(aux[0]).get(ListaLista.get(aux[0]).size() - 1));
            System.out.println(ListaLista.get(aux[1]).get(ListaLista.get(aux[1]).size() - 1));
            intermediaryListaLista = new ArrayList<>();
            removeAll();

            intermediaryListaLista.add(new ArrayList<Integer>(ListaLista.get(aux[0])));
            intermediaryListaLista.add(new ArrayList<Integer>(ListaLista.get(aux[1])));
            crossoverTwo(aux);

            for (int j = 0; j < (ListaLista.size() / 2) - 1; j++) {
                aux[0] = random.nextInt(ListaLista.size());
                aux[1] = random.nextInt(ListaLista.size());
                crossoverTwo(aux);
            }
            ListaLista = intermediaryListaLista;

            for (int j = 0; j < ListaLista.size(); j++) {
                ListaLista.get(j).add(aptidaoCalc(ListaLista.get(j)));
            }
        }
    }

    private void removeAll() {
        for (int i = 0; i < ListaLista.size(); i++) {
            ListaLista.get(i).remove(ListaLista.get(i).size() - 1);
        }
    }

    private void startPopulation(int vetLen, int vetQt) {
        //0-> ↑
        //1-> ↗
        //2-> →
        //3-> ↘
        //4-> ↓
        //5-> ↙
        //6-> ←
        //7-> ↖

        this.ListaLista = new ArrayList<ArrayList<Integer>>(vetQt);

        for (int i = 0; i < vetQt; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < vetLen; j++) {
                list.add(random.nextInt(8));
            }
            list.add(aptidaoCalc(list));
            ListaLista.add(list);
        }
    }

    private void crossoverTwo(int[] family) {
        ArrayList<Integer> pai = ListaLista.get(family[0]);
        ArrayList<Integer> mae = ListaLista.get(family[1]);

        int ponto = random.nextInt(pai.size() / 2);

        ArrayList<Integer> filho1 = new ArrayList<Integer>(pai);
        ArrayList<Integer> filho2 = new ArrayList<Integer>(mae);
        for (int i = ponto; i < pai.size() - ponto; i++) {
            filho1.set(i, mae.get(i));
            filho2.set(i, pai.get(i));
        }
        intermediaryListaLista.add(filho1);
        intermediaryListaLista.add(filho2);
        mutagenico();
    }

    private void mutagenico() {
        int choose = random.nextInt(2);

        for (int j = 0; j < mutationRate * qtdCromossomos; j++) {
            intermediaryListaLista.get(intermediaryListaLista.size() - choose - 1).set(random.nextInt(qtdCromossomos), random.nextInt(8));
        }
    }

    private int[] escolheElitismo() {
        int posPai = 0;
        int posMae = 0;
        int valPai = ListaLista.get(0).get(ListaLista.get(0).size() - 1);
        int valMae = valPai;

        int atual = 0;

        for (int i = 0; i < ListaLista.size(); i++) {
            atual = ListaLista.get(i).get(ListaLista.get(i).size() - 1);
            if (atual > valPai) {
                valPai = atual;
                posPai = i;

            } else {
                if (atual > valMae && atual != valPai) {
                    valMae = atual;
                    posMae = i;
                }
            }
        }

        int[] family = {posPai, posMae};

        return family;
    }

    private int aptidaoCalc(ArrayList<Integer> array) {
        int buraco = -100;
        int parede = -10;
        int anda = -1;
        int saida = 10000;

        int[] pos = {0, 0};
        int pts = 0;

        for (int i = 0; i < array.size(); i++) {
            int aux = array.get(i);
            String[] str = anda(pos, aux);
            switch (str[0]) {
                case "1":
                    pts += parede;
                    break;
                case "B":
                    pts += buraco;
                    break;
                case "S":
                    pts += saida;
                    path(array);
                    end[0] = Integer.parseInt(str[1]);
                    end[1] = Integer.parseInt(str[2]);
                    return pts;
                case "E":
                case "0":
                    pts += anda;
                    pos[0] = Integer.parseInt(str[1]);
                    pos[1] = Integer.parseInt(str[2]);
                    break;
            }
        }
        return pts;
    }

    private String[] anda(int[] pos, int dir) {
        int[] var = new int[2];
        switch (dir) {
            case 0:
                var[0] = pos[0] - 1;
                var[1] = pos[1];
                break;
            case 1:
                var[0] = pos[0] - 1;
                var[1] = pos[1] + 1;
                break;
            case 2:
                var[0] = pos[0];
                var[1] = pos[1] + 1;
                break;
            case 3:
                var[0] = pos[0] + 1;
                var[1] = pos[1] + 1;
                break;
            case 4:
                var[0] = pos[0] + 1;
                var[1] = pos[1];
                break;
            case 5:
                var[0] = pos[0] + 1;
                var[1] = pos[1] - 1;
                break;
            case 6:
                var[0] = pos[0];
                var[1] = pos[1] - 1;
                break;
            case 7:
                var[0] = pos[0] - 1;
                var[1] = pos[1] - 1;
                break;
        }
        if (var[0] < 0 || var[0] >= lab.length || var[1] < 0 || var[1] >= lab.length) {
            String[] s = {"1", pos[0] + "", pos[1] + ""};
            return s;
        }
        return new String[]{lab[var[0]][var[1]] + "", var[0] + "", var[1] + ""};
    }

    private void path(ArrayList<Integer> array) {
        if (foundIt) {
            return;
        }
        foundIt = true;

        String[][] print = new String[lab.length][lab.length];
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab.length; j++) {
                print[i][j] = lab[i][j];
            }
        }

        int[] pos = {0, 0};
        log += "Caminho percorrido:\n(0, 0) ";
        loop:
        for (int i = 0; i < array.size(); i++) {
            int aux = array.get(i);
            String[] str = anda(pos, aux);
            switch (str[0]) {
                case "S":
                    log += "(" + str[1] + ", " + str[2] + ")\n";
                    break loop;
                case "E":
                case "0":
                    pos[0] = Integer.parseInt(str[1]);
                    pos[1] = Integer.parseInt(str[2]);
                    log += "(" + str[1] + ", " + str[2] + ") ";
                    print[Integer.parseInt(str[1])][Integer.parseInt(str[2])] = "X";
                    break;
            }
        }
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab.length; j++) {
                log += print[i][j] + " ";
            }
            log += "\n";
        }
    }

    public int[] getEnd() {
        return end;
    }

    public String toString() {
        return log;
    }
}
