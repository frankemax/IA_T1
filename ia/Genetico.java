package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private String[][] lab = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> populacao, populacaoIntermediaria;
    private double mutationRate;
    private int qtdadeDeGeracoes, tamanhoCromossomos, tamanhoPopulacao;
    private boolean foundIt;
    private int[] end = new int[2];
    private String log = "\n";
    private Random random = new Random();

    public Genetico(double mutationRate, int qtdadeDeGeracoes, int tamanhoCromossomos, int tamanhoPopulacao, boolean detalhado) throws FileNotFoundException {

        this.mutationRate = mutationRate;
        this.qtdadeDeGeracoes = qtdadeDeGeracoes;
        this.tamanhoCromossomos = tamanhoCromossomos;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.foundIt = false;

        startPopulation(tamanhoCromossomos, tamanhoPopulacao);

        for (int i = 0; i < qtdadeDeGeracoes; i++) {
            if (foundIt) {
                log += "Saida encontrada na geracao " + (i - 1) + "\n";
                log += "Tamanho agente: " + this.tamanhoCromossomos;
                return;
            }
            System.out.println(i);
            int[] aux = escolheElitismo();
            if (i % 5 == 0 && i > 0) {
                log += "\nGeracao " + i + ":\nMelhor agente:\n";
                log += path(populacao.get(aux[0]), false) + "\n";
                log += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\n";
                this.tamanhoCromossomos += 4;
            } else {
                if (detalhado) {
                    log += "\nGeracao " + i + ":\nMelhor agente:\n";
                    log += populacao.get(aux[0]) + "\n";
                    log += path(populacao.get(aux[0]), false) + "\n";
                    log += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";
                }
            }

            populacaoIntermediaria = new ArrayList<>();
            removeAll();

            populacaoIntermediaria.add(add(new ArrayList<Integer>(populacao.get(aux[0]))));
            populacaoIntermediaria.add(add(new ArrayList<Integer>(populacao.get(aux[1]))));
            crossoverTwo(aux);

            for (int j = 0; j < (populacao.size() / 2) - 1; j++) {
                aux[0] = random.nextInt(populacao.size());
                aux[1] = random.nextInt(populacao.size());
                crossoverTwo(aux);
            }
            populacao = populacaoIntermediaria;

            for (int j = 0; j < populacao.size(); j++) {
                populacao.get(j).add(aptidaoCalc(populacao.get(j)));
            }
        }
    }

    private void removeAll() {
        for (int i = 0; i < populacao.size(); i++) {
            populacao.get(i).remove(populacao.get(i).size() - 1);
        }
    }

    private ArrayList<Integer> add(ArrayList<Integer> array) {
        for (int i = array.size(); i < tamanhoCromossomos; i++) {
            array.add(random.nextInt(8));
        }
        return array;
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

        this.populacao = new ArrayList<ArrayList<Integer>>(vetQt);

        for (int i = 0; i < vetQt; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < vetLen; j++) {
                list.add(random.nextInt(8));
            }
            list.add(aptidaoCalc(list));
            populacao.add(list);
        }
    }

    private void crossoverTwo(int[] family) {
        ArrayList<Integer> pai = populacao.get(family[0]);
        ArrayList<Integer> mae = populacao.get(family[1]);

        int ponto = random.nextInt(pai.size() / 2);

        ArrayList<Integer> filho1 = new ArrayList<Integer>(pai);
        ArrayList<Integer> filho2 = new ArrayList<Integer>(mae);
        for (int i = ponto; i < pai.size() - ponto; i++) {
            filho1.set(i, mae.get(i));
            filho2.set(i, pai.get(i));
        }
        populacaoIntermediaria.add(add(filho1));
        populacaoIntermediaria.add(add(filho2));
        mutagenico();
    }

    private void mutagenico() {
        int choose = random.nextInt(2);

        for (int j = 0; j < mutationRate * tamanhoCromossomos; j++) {
            populacaoIntermediaria.get(populacaoIntermediaria.size() - choose - 1).set(random.nextInt(tamanhoCromossomos), random.nextInt(8));
        }
    }

    private int[] escolheElitismo() {
        int posPai = 0;
        int posMae = 0;
        int valPai = populacao.get(0).get(populacao.get(0).size() - 1);
        int valMae = valPai;

        int atual = 0;

        for (int i = 0; i < populacao.size(); i++) {
            atual = populacao.get(i).get(populacao.get(i).size() - 1);
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
                    log += "\n" + path(array, true);
                    foundIt = true;
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

    private String path(ArrayList<Integer> array, boolean b) {
        if (foundIt && b) {
            return "";
        }
        String l = "";

        String[][] print = new String[lab.length][lab.length];
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab.length; j++) {
                print[i][j] = lab[i][j];
            }
        }

        int[] pos = {0, 0};
        l += "Caminho percorrido:\n(0, 0) ";
        loop:
        for (int i = 0; i < array.size(); i++) {
            int aux = array.get(i);
            String[] str = anda(pos, aux);
            switch (str[0]) {
                case "S":
                    l += "(" + str[1] + ", " + str[2] + ")\n\n";
                    break loop;
                case "E":
                case "0":
                    pos[0] = Integer.parseInt(str[1]);
                    pos[1] = Integer.parseInt(str[2]);
                    l += "(" + str[1] + ", " + str[2] + ") ";
                    print[Integer.parseInt(str[1])][Integer.parseInt(str[2])] = "X";
                    break;
            }
        }
        if (b) {
            for (int i = 0; i < lab.length; i++) {
                for (int j = 0; j < lab.length; j++) {
                    l += print[i][j] + " ";
                }
                l += "\n";
            }
        }
        return l;
    }

    public int[] getEnd() {
        return end;
    }

    public String toString() {
        return log;
    }
}
