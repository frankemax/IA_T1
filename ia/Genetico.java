package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private String[][] lab = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> listList;
    private ArrayList<ArrayList<Integer>> intermediaryListList;
    private int filho;

    public Genetico(int filho, int qtdadeDeGeracoes, int lengthCaminhoInicial,  int qtdadeDeFilhos) throws FileNotFoundException {

        this.filho = filho;
        Random r = new Random();

        startPopulation(lengthCaminhoInicial, qtdadeDeFilhos);




        for (int i = 0; i < qtdadeDeGeracoes; i++) {
            System.out.println(i);
            int[] aux = escolheElitismo();

            intermediaryListList = new ArrayList<>();
            removeAll();

            intermediaryListList.add(new ArrayList<Integer>(listList.get(aux[0])));
            intermediaryListList.add(new ArrayList<Integer>(listList.get(aux[1])));
            crossoverTwo(aux);

            for (int j = 0; j < (listList.size() / 2) - 1; j++) {
                aux[0] = r.nextInt(listList.size());
                aux[1] = r.nextInt(listList.size());
                crossoverTwo(aux);
            }
            listList = intermediaryListList;

            for (int j = 0; j < listList.size(); j++) {
                listList.get(j).add(aptidaoCalc(listList.get(j)));
            }
        }
        System.out.println(listList);
    }

    private void removeAll() {
        for (int i = 0; i < listList.size(); i++) {
            listList.get(i).remove(listList.get(i).size() - 1);
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

        Random r = new Random();

        this.listList = new ArrayList<ArrayList<Integer>>(vetQt);

        for (int i = 0; i < vetQt; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < vetLen; j++) {
                list.add(r.nextInt(8));
            }
            list.add(aptidaoCalc(list));
            listList.add(list);
        }
    }

    private void crossoverTwo(int[] family) {
        Random r = new Random();

        ArrayList<Integer> pai = listList.get(family[0]);
        ArrayList<Integer> mae = listList.get(family[1]);

        int ponto = r.nextInt(pai.size() / 2);

        ArrayList<Integer> filho1 = new ArrayList<Integer>(pai);
        ArrayList<Integer> filho2 = new ArrayList<Integer>(mae);
        for (int i = ponto; i < pai.size() - ponto; i++) {
            filho1.set(i, mae.get(i));
            filho2.set(i, pai.get(i));
        }
        intermediaryListList.add(filho1);
        intermediaryListList.add(filho2);
        mutagenico();
    }

    private void mutagenico() {
        Random r = new Random();
        int f = r.nextInt(filho);
        if (f > 1) {
            return;
        }
        int pos = r.nextInt(intermediaryListList.get(0).size());
        int var = r.nextInt(8);

        intermediaryListList.get(intermediaryListList.size() - f - 1).set(pos, var);
    }

    private int[] escolheElitismo() {
        int posPai = 0;
        int posMae = 0;
        int valPai = listList.get(0).get(listList.get(0).size() - 1);
        int valMae = valPai;

        int atual = 0;

        for (int i = 0; i < listList.size(); i++) {
            atual = listList.get(i).get(listList.get(i).size() - 1);
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
        int buraco = -10000;
        int parede = -100;
        int anda = -1;
        int saida = 1000000000;

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
                    System.out.println("monstro");
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

        String[] s = {lab[var[0]][var[1]] + "", var[0] + "", var[1] + ""};
        return s;
    }

}
