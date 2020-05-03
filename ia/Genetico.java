package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    private String[][] lab = new Labirinto().getLabirinto();
    private ArrayList<ArrayList<Integer>> listList;

    public Genetico() throws FileNotFoundException {

        int qtdadeDeGerações=20;
        int lengthCaminhoInicial=5;
        int qtdadeDeFilhos=50;



        startPopulation(lengthCaminhoInicial, qtdadeDeFilhos);
        for (int i = 0; i < qtdadeDeGerações; i++) {
            //selectBest()
            //generateNewandAdd5();
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
        System.out.println(listList);
    }

    private void crossover(){
        Random r = new Random();
        int[] family = escolheElitismo();
        ArrayList<Integer> Pai = listList.get(family[0]);
        ArrayList<Integer> Mae = listList.get(family[1]);

        int ponto = r.nextInt(Pai.size()/2);

        ArrayList<Integer> filho1 = new ArrayList<Integer>(Pai);
        ArrayList<Integer> filho2 = new ArrayList<Integer>(Mae);

        for (int i = ponto; i < Pai.size() - ponto; i++) {
                filho1.add(i, Mae.get(i));
                filho2.add(i, Pai.get(i));
        }

    }

    private int[] escolheElitismo(){
        int posPai = 0;
        int posMae = 0;

        int valPai = listList.get(0).get(listList.size()-1);
        int valMae = valPai;

        int atual = 0;

        for(int i = 0; i < listList.size() ; i++){
            atual = listList.get(i).get(listList.size()-1);
            if(atual >  valPai){
                valPai = atual;
                posPai = i;

            }else{
                if(atual > valMae && atual != valPai){
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
        if (var[0] < 0 || var[0] > 7 || var[1] < 0 || var[1] > 7) {
            String[] s = {"1", pos[0] + "", pos[1] + ""};
            return s;
        }

        String[] s = {lab[var[0]][var[1]] + "", var[0] + "", var[1] + ""};
        return s;
    }


}
