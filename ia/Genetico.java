package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private final Labirinto lab = new Labirinto();
    private final String[][] labirinto = lab.getLabirinto();
    private ArrayList<ArrayList<Integer>> populacao, populacaoIntermediaria;
    private final double mutationRate;
    private final int tamanhoCromossomos;
    private int timesFound = 0;
    private boolean foundIt = false;
    private final int[] exit = new int[2];
    private final StringBuilder log = new StringBuilder("\n");
    private String logLabirinto = "";
    private final Random random = new Random();

    public Genetico(double mutationRate, int qtdadeDeGeracoes, int tamanhoPopulacao, int freqLog) throws FileNotFoundException {

        this.mutationRate = mutationRate;
        this.tamanhoCromossomos = lab.getEmpty();

        startPopulation(tamanhoPopulacao + 1);

        boolean firstTime = false;
        for (int i = 0; i < qtdadeDeGeracoes; i++) {

            int[] aux = escolheElitismo();

            if (i % freqLog == 0 && i > 0) { //guarda os logs
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

            if (10000 == populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) { //quando encontra um caminho sem perder pontos guarda os logs e encerra
                log.append("\n\nCaminho encontrado pelo algoritmo genetico: ");
                log.append("\nGeracao ").append(i).append(":\nAgente:\n");
                log.append(populacao.get(aux[0]));
                this.foundIt = false;
                path(populacao.get(aux[0]), true);
                this.foundIt = true;
                log.append(logLabirinto);
                log.append("Pontuacao: ").append(populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)).append("\nTamanho do agente: ").append(this.tamanhoCromossomos).append("\n");
                return;
            } //log final

            removeCalcAptidao();

            populacaoIntermediaria = new ArrayList<>();
            populacaoIntermediaria.add(new ArrayList<>(populacao.get(aux[0])));
            populacaoIntermediaria.add(new ArrayList<>(populacao.get(aux[1])));

            crossoverTwo(aux);

            int[] torneio = new int[2];
            for (int j = 0; j < (tamanhoCromossomos / 2) + 3; j++) {

                aux[0] = random.nextInt(populacao.size());
                aux[1] = random.nextInt(populacao.size());

                torneio[0] = (populacao.get(aux[0]).get(tamanhoCromossomos - 1) >
                        populacao.get(aux[1]).get(tamanhoCromossomos - 1)) ? aux[0] : aux[1];

                aux[0] = random.nextInt(populacao.size());
                aux[1] = random.nextInt(populacao.size());

                torneio[1] = (populacao.get(aux[0]).get(tamanhoCromossomos - 1) >
                        populacao.get(aux[1]).get(tamanhoCromossomos - 1)) ? aux[0] : aux[1];

                crossoverTwo(torneio);
            }

            populacao = populacaoIntermediaria;
            timesFound = 0;

            for (ArrayList<Integer> agente : populacao) {
                agente.add(aptidaoCalc(agente));
            }
        }
    }

    private void removeCalcAptidao() { // retira o resultado do calculo dos agentes
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
            ArrayList<Integer> agente = new ArrayList<>();

            for (int j = 0; j < tamanhoCromossomos; j++) {
                agente.add(random.nextInt(8));
            }

            agente.add(aptidaoCalc(agente));
            populacao.add(agente);
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

        if(random.nextInt(100) > 20)return; //PODE SER ALTERADA

        for (int i = 0; i < mutationRate * tamanhoCromossomos; i++) {
            populacaoIntermediaria.get(populacaoIntermediaria.size() - random.nextInt(2) - 1).set(random.nextInt(tamanhoCromossomos), random.nextInt(8));
        }
    }

    private int[] escolheElitismo() { //retorna os dois melhores cromossomos

        int posPai, posMae, valPai, valMae, atual;
        if (populacao.get(0).get(populacao.get(0).size() - 1) < populacao.get(1).get(populacao.get(1).size() - 1)) {
            posPai = 1;
            posMae = 0;
            valPai = populacao.get(1).get(populacao.get(1).size() - 1);
            valMae = populacao.get(0).get(populacao.get(0).size() - 1);
        } else {
            posPai = 0;
            posMae = 1;
            valPai = populacao.get(0).get(populacao.get(0).size() - 1);
            valMae = populacao.get(1).get(populacao.get(1).size() - 1);
        }

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
        int saida = 10000;

        int[] pos = {0, 0};
        int pts = 0;

        ArrayList<int[]> moves = new ArrayList<>();

        for (int aux : array) {

            String[] str = anda(pos, aux);
            moves.add(new int[]{Integer.parseInt(str[1]), Integer.parseInt(str[2])});

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

        int count = 0;
        for (int i = 0; i < moves.size(); i++) { //PODE SER ALTERADA
            for (int j = i+1; j < moves.size(); j++) {
                if (moves.get(i)[0] == moves.get(j)[0] && moves.get(i)[1] == moves.get(j)[1]) {
                    count -= 1;
                    moves.remove(j);
                    j--;
                }
            }
        }
        if(count>1)count = count / 3;

        return pts + count;
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
        for (int dir : array) {
            String[] str = anda(pos, dir);
            if (dir < 0 || dir > 7) {
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