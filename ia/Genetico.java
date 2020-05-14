package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private final String[][] lab = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> populacao, populacaoIntermediaria;
    private final double mutationRate;
    private final int tamanhoCromossomos;
    private int count = 0;
    private boolean foundIt;
    private final int[] end = new int[2];
    private String log = "\n";
    private String logFull = "\n";
    private String logLabirinto = "";
    private final Random random = new Random();

    public Genetico(double mutationRate, int qtdadeDeGeracoes, int tamanhoCromossomos, int tamanhoPopulacao, boolean detalhado, boolean finaliza) throws FileNotFoundException {

        this.mutationRate = mutationRate;
        this.tamanhoCromossomos = tamanhoCromossomos;
        this.foundIt = false;

        startPopulation(tamanhoCromossomos, tamanhoPopulacao);
        boolean ver = false;
        for (int i = 0; i < qtdadeDeGeracoes; i++) {
            String str = "";
            if (foundIt) {
                if (finaliza) {
                    str += "\n" + count + " saidas encontrada na geracao " + (i);
                } else {
                    log += logLabirinto;
                    log += "Saida encontrada na geracao " + (i) + "\n";
                    log += "Tamanho agente: " + this.tamanhoCromossomos;
                    return;
                }
            }
            count = 0;
            int[] aux = escolheElitismo();


            if (i % 5 == 0 && i > 0) {
                if (finaliza) {
                    logFull += "\nGeracao " + i + ":\nMelhor agente:\n";
                    logFull += populacao.get(aux[0]);
                    logFull += str;
                    if (foundIt && !ver) {
                        logFull += logLabirinto;
                        ver = true;
                    } else {
                        logFull += path(populacao.get(aux[0]), false);
                    }
                    logFull += ("\nPontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";
                } else {
                    log += "\nGeracao " + i + ":\nMelhor agente:\n";
                    log += populacao.get(aux[0]);
                    if (foundIt && !ver) {
                        log += logLabirinto;
                        ver = true;
                    } else {
                        log += path(populacao.get(aux[0]), false) + "\n";
                    }
                    log += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";

                }
                //this.tamanhoCromossomos += 4;
            } else {
                if (detalhado) {
                    if (finaliza) {
                        logFull += "\nGeracao " + i + ":\nMelhor agente:\n";
                        logFull += populacao.get(aux[0]);
                        logFull += str;
                        if (foundIt && !ver) {
                            logFull += logLabirinto;
                            ver = true;
                        } else {
                            logFull += path(populacao.get(aux[0]), false) + "\n";
                        }
                        logFull += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";
                    } else {
                        log += "\nGeracao " + i + ":\nMelhor agente:\n";
                        log += populacao.get(aux[0]);
                        if (foundIt && !ver) {
                            log += logLabirinto;
                            ver = true;
                        } else {
                            log += path(populacao.get(aux[0]), false) + "\n";
                        }
                        log += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";
                    }
                }
            }
            if (100 == populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) {
                System.out.println(i);

                logFull += "\nGeracao " + i + ":\nMelhor agente:\n";
                logFull += populacao.get(aux[0]);
                logFull += str;
                this.foundIt = false;
                path(populacao.get(aux[0]), true);
                this.foundIt = true;
                logFull += logLabirinto;
                logFull += ("Pontuacao: " + populacao.get(aux[0]).get(populacao.get(aux[0]).size() - 1)) + "\nTamanho do agente: " + this.tamanhoCromossomos + "\n";
                return;
            }
            populacaoIntermediaria = new ArrayList<>();
            removeAll();

            populacaoIntermediaria.add(add(new ArrayList<>(populacao.get(aux[0]))));
            populacaoIntermediaria.add(add(new ArrayList<>(populacao.get(aux[1]))));
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
            for (ArrayList<Integer> integers : populacao) {
                integers.add(aptidaoCalc(integers));
            }
        }
    }

    private void removeAll() {
        for (ArrayList<Integer> integers : populacao) {
            integers.remove(integers.size() - 1);
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

        this.populacao = new ArrayList<>(vetQt);


        for (int i = 0; i < vetQt; i++) {
            ArrayList<Integer> list = new ArrayList<>();

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

        ArrayList<Integer> filho1 = new ArrayList<>(pai);
        ArrayList<Integer> filho2 = new ArrayList<>(mae);
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

        int atual;

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

        return new int[]{posPai, posMae};
    }

    private int aptidaoCalc(ArrayList<Integer> array) {
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
                    count++;
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
            return new String[]{"1", pos[0] + "", pos[1] + ""};
        }
        return new String[]{lab[var[0]][var[1]] + "", var[0] + "", var[1] + ""};
    }

    private String path(ArrayList<Integer> array, boolean b) {
        if (foundIt && b) {
            return "";
        }
        StringBuilder l = new StringBuilder("\n");

        String[][] print = new String[lab.length][lab.length];
        for (int i = 0; i < lab.length; i++) {
            System.arraycopy(lab[i], 0, print[i], 0, lab.length);
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
        if (b) {
            logLabirinto = l + "\n\n";
            for (int i = 0; i < lab.length; i++) {
                for (int j = 0; j < lab.length; j++) {
                    logLabirinto += print[i][j] + " ";
                }
                logLabirinto += "\n";
            }
            logLabirinto += "\n";
        }
        return l.toString();
    }

    public int[] getEnd() {
        return end;
    }

    public String toString(boolean b) {
        if (b) {
            return logFull;
        }
        return log;
    }
}