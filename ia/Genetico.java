package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private final String[][] labirinto = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> populacao, populacaoIntermediaria;
    private final double mutationRate;
    private final int tamanhoCromossomos;
    private int timesFound = 0;
    private boolean foundIt = false;
    private final int[] exit = new int[2];
    private final StringBuilder log = new StringBuilder("\n");
    private String logLabirinto = "";
    private final Random random = new Random();

    public Genetico(double mutationRate, int qtdadeDeGeracoes, int tamanhoCromossomos, int tamanhoPopulacao, boolean detalhado) throws FileNotFoundException {

        this.mutationRate = mutationRate;
        this.tamanhoCromossomos = tamanhoCromossomos;

        startPopulation(tamanhoPopulacao + 1);

        boolean firstTime = false;
        for (int i = 0; i < qtdadeDeGeracoes; i++) {

            int[] aux = escolheElitismo();

            if ((i % 5 == 0 && i > 0) || detalhado) { //guarda os logs
                log.append("\nGeracao ").append(i).append(":\nMelhor agente:\n");
                log.append(populacao.get(aux[0]));
                log.append("\n").append(timesFound).append(" saidas encontrada na geracao ").append(i);
                if (foundIt && !firstTime) {
                    log.append(logLabirinto);
                    firstTime = true;
                } else {
                    log.append(path(populacao.get(aux[0]), false));
                }
                log.append("\nPontuacao: ").append(populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)).append("\nTamanho do agente: ").append(this.tamanhoCromossomos).append("\n");
            }

            if (100 == populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) { //quando encontra um caminho sem perder pontos guarda os logs e encerra
                log.append("\n\nCaminho encontrado pelo algoritmo genetico: ");
                log.append("\nGeracao ").append(i).append(":\nAgente:\n");
                log.append(populacao.get(aux[0]));
                this.foundIt = false;
                path(populacao.get(aux[0]), true);
                this.foundIt = true;
                log.append(logLabirinto);
                log.append("Pontuacao: ").append(populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)).append("\nTamanho do agente: ").append(this.tamanhoCromossomos).append("\n");
                return;
            }

            populacaoIntermediaria = new ArrayList<>();
            removeAll();

            populacaoIntermediaria.add(new ArrayList<>(populacao.get(aux[0])));
            populacaoIntermediaria.add(new ArrayList<>(populacao.get(aux[1])));

            crossoverTwo(aux);

            for (int j = 0; j < (tamanhoCromossomos / 2) + 3; j++) {

                aux[0] = random.nextInt(populacao.size());
                aux[1] = random.nextInt(populacao.size());
                int[] var = new int[2];

                if (populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1) > populacao.get(aux[1]).get(populacao.get(aux[1]).size() - 1)) {
                    var[0] = aux[0];
                } else {
                    var[0] = aux[1];
                }

                aux[0] = random.nextInt(populacao.size());
                aux[1] = random.nextInt(populacao.size());

                if (populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1) > populacao.get(aux[1]).get(populacao.get(aux[1]).size() - 1)) {
                    var[1] = aux[0];
                } else {
                    var[1] = aux[1];
                }

                crossoverTwo(var);
            }

            populacao = populacaoIntermediaria;
            timesFound = 0;

            for (ArrayList<Integer> integers : populacao) {
                integers.add(aptidaoCalc(integers));
            }
        }
    }

    private void removeAll() { // retira o resultado do calculo dos agentes
        for (ArrayList<Integer> integers : populacao) {
            integers.remove(integers.size() - 1);
        }
    }

    private void startPopulation(int tamanhoPopulacao) { //gera os agentes e seus movimentos
        //0-> ↑
        //1-> ↗
        //2-> →
        //3-> ↘
        //4-> ↓
        //5-> ↙
        //6-> ←
        //7-> ↖

        this.populacao = new ArrayList<>(tamanhoPopulacao);

        for (int i = 0; i < tamanhoPopulacao; i++) {
            ArrayList<Integer> list = new ArrayList<>();

            for (int j = 0; j < tamanhoCromossomos; j++) {
                list.add(random.nextInt(8));
            }

            list.add(aptidaoCalc(list));
            populacao.add(list);
        }
    }

    private void crossoverTwo(int[] family) { //faz o crossover com os elementos do vetor
        ArrayList<Integer> pai = populacao.get(family[0]);
        ArrayList<Integer> mae = populacao.get(family[1]);

        int ponto = random.nextInt(pai.size() / 2);

        ArrayList<Integer> filho1 = new ArrayList<>(pai);
        ArrayList<Integer> filho2 = new ArrayList<>(mae);

        for (int i = ponto; i < pai.size() - ponto; i++) {
            filho1.set(i, mae.get(i));
            filho2.set(i, pai.get(i));
        }
        populacaoIntermediaria.add(filho1);
        populacaoIntermediaria.add(filho2);

        mutagenico();
    }

    private void mutagenico() { //muta um dos dois ultimos cromossomos
        for (int j = 0; j < mutationRate * tamanhoCromossomos; j++) {
            populacaoIntermediaria.get(populacaoIntermediaria.size() - random.nextInt(2) - 1).set(random.nextInt(tamanhoCromossomos), random.nextInt(8));
        }
    }

    private int[] escolheElitismo() { //retorna os dois melhores cromossomos
        int posPai = 0;
        int posMae = 1;
        int valPai = populacao.get(0).get(populacao.get(0).size() - 1);
        int valMae = populacao.get(1).get(populacao.get(1).size() - 1);
        int atual;

        for (int i = 0; i < populacao.size(); i++) {
            atual = populacao.get(i).get(populacao.get(i).size() - 1);

            if (atual > valMae) {
                if (atual < valPai) {
                    valMae = atual;
                    posMae = i;
                } else {
                    if (atual > valPai) {
                        valMae = valPai;
                        posMae = posPai;
                        valPai = atual;
                        posPai = i;
                    } else {
                        if (i != posPai) {
                            valMae = atual;
                            posMae = i;
                        }
                    }
                }
            }
        }

        return new int[]{posPai, posMae};
    }

    private int aptidaoCalc(ArrayList<Integer> array) { //calcula a aptidao do cromossomo
        int buraco = -5;
        int parede = -1;
        int anda = 0;
        int saida = 100;

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
                    path(array, true);
                    timesFound++;
                    foundIt = true;
                    exit[0] = Integer.parseInt(str[1]);
                    exit[1] = Integer.parseInt(str[2]);
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

        if (var[0] < 0 || var[0] >= labirinto.length || var[1] < 0 || var[1] >= labirinto[0].length) {
            return new String[]{"1", pos[0] + "", pos[1] + ""};
        }

        return new String[]{labirinto[var[0]][var[1]] + "", var[0] + "", var[1] + ""};
    }

    private String path(ArrayList<Integer> array, boolean doMap) {
        if (foundIt && doMap) {
            return "";
        }

        StringBuilder l = new StringBuilder("\n");
        String[][] print = new String[labirinto.length][labirinto.length];

        for (int i = 0; i < labirinto.length; i++) {
            System.arraycopy(labirinto[i], 0, print[i], 0, labirinto.length);
        }

        int[] pos = {0, 0};
        l.append("Caminho percorrido:\n(0, 0) ");

        loop:
        for (int aux : array) {
            String[] str = anda(pos, aux);
            if (aux < 0 || aux > 7) {
                break;
            }
            switch (str[0]) {
                case "S":
                    l.append("(").append(str[1]).append(", ").append(str[2]).append(")");
                    break loop;
                case "E":
                case "0":
                    pos[0] = Integer.parseInt(str[1]);
                    pos[1] = Integer.parseInt(str[2]);
                    l.append("(").append(str[1]).append(", ").append(str[2]).append(") ");
                    print[Integer.parseInt(str[1])][Integer.parseInt(str[2])] = "X";
                    break;
            }
        }

        if (doMap) {
            logLabirinto = l + "\n\n";
            for (int i = 0; i < labirinto.length; i++) {
                for (int j = 0; j < labirinto.length; j++) {
                    logLabirinto += print[i][j] + " ";
                }
                logLabirinto += "\n";
            }
            logLabirinto += "\n";
        }

        return l.toString();
    }

    public int[] getExit() {
        return exit;
    }

    public String toString() {
        return log.toString();
    }
}