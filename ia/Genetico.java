package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {

    String[][] lab = new Labirinto().getLabirinto();

    public Genetico() throws FileNotFoundException {

        startPopulation(5, 10);

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

        ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>(vetQt);

        for (int i = 0; i < vetQt; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < vetLen; j++) {
                list.add(r.nextInt(8));

            }
            list.add(aptidaoCalc(list));
            array.add(list);

        }
        System.out.println(array);
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
                    pts += anda;
                    pos[0] = Integer.parseInt(str[1]);
                    pos[1] = Integer.parseInt(str[2]);
                    break;
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
