package ia;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;

public class Genetico {
    String[][] lab = new Labirinto().getLabirinto() ;

    public Genetico() throws FileNotFoundException {

        startPopulation(5,10);




    }

    private void startPopulation(int vetLen, int vetQt){
        //0-> ←
        //1-> →
        //2-> ↑
        //3-> ↓
        //4-> ↖
        //5-> ↗
        //6-> ↘
        //7-> ↙

        Random r = new Random();

        ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>(vetQt);

        for (int i = 0; i < vetQt; i++) {
            ArrayList <Integer> list = new ArrayList<Integer>();

            for (int j = 0; j < vetLen; j++) {
                list.add(r.nextInt(8));

            }
            array.add(list);
        }

        System.out.println(array);
    }

    private int aptidaoCalc(ArrayList <Integer> array){
        int buraco = +100;
        int parede = +100;
        int anda = -1;




    }
}
