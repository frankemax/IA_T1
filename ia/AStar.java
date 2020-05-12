package ia;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AStar {

    private class Node {

        int g;
        int h;
        int f;
        int[] coord;
        Node ant;
        String status;

        public Node(int g, int h, int[] coord, Node anterior, String status) {
            this.g = g;
            this.h = h;
            f = g + h;
            this.coord = coord;
            ant = anterior;
            this.status = status;
        }
    }

    private String[][] lab = new Labirinto().getLabirinto();
    private int[] entrada;
    private int[] saida;
    private ArrayList<Node> open;
    private ArrayList<Node> closed;
    private String log = "";

    public AStar(int[] entrada, int[] saida) throws FileNotFoundException {
        this.entrada = entrada;
        this.saida = saida;
        open = new ArrayList<>();
        closed = new ArrayList<>();
        Node n = solve();
        if(n == null){
            log = "O caminho entre a entrada e a saida nao foi encontrado";
        }
    }

    private Node solve() {
        open.add(new Node(0, heuristica(0, 0, saida[0], saida[1]), entrada, null, "open"));
        Node current;
        for (int i = 0; i < lab.length * lab[0].length; i++) {
            current = lowest();
            if (current == null) {
                break;
            }
            open.remove(current);
            closed.add(current);
            current.status = "closed";
            if (current.coord[0] == saida[0] && current.coord[1] == saida[1]) {
                return current;
            }
            vizinhos(new int[] {current.coord[0], current.coord[1]}, current);
        }
        return null;
    }

    private void vizinhos(int[] cord, Node n) {
        vizinhoAux(cord[0] - 1, cord[1] - 1, n);
        vizinhoAux(cord[0] - 1, cord[1], n);
        vizinhoAux(cord[0], cord[1] - 1, n);
        vizinhoAux(cord[0] - 1, cord[1] + 1, n);
        vizinhoAux(cord[0] + 1, cord[1] - 1, n);
        vizinhoAux(cord[0] + 1, cord[1] + 1, n);
        vizinhoAux(cord[0] + 1, cord[1], n);
        vizinhoAux(cord[0], cord[1] + 1, n);
    }

    private void vizinhoAux(int a, int b, Node ant) {
        if (a < 0 || b < 0 || a > lab.length - 1 || b > lab[0].length - 1) {
            return;
        }

        if (lab[a][b].equals("1") || lab[a][b].equals("B")) {
            return;
        }
        for (Node node : closed) {
            if (node.coord[0] == a && node.coord[1] == b) {
                return;
            }
        }
        Node n = null;
        for (Node node : open) {
            if (node.coord[0] == a && node.coord[1] == b) {
                n = node;
                if (n.g > ant.g + 1) {
                    n.g = ant.g + 1;
                    n.ant = ant;
                }
            }
        }
        int[] c = {a, b};
        if (n == null) {
            n = new Node(ant.g + 1, heuristica(a, b, saida[0], saida[0]), c, ant, "open");
            open.add(n);
            if (n.coord[0] == saida[0] && n.coord[1] == saida[1]) {
                path(n);
            }
        }

    }

    private void path(Node n) {
        log += "\n\nMelhor saida encontrada pelo A*\n";
        String[][] print = new String[lab.length][lab.length];
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab.length; j++) {
                print[i][j] = lab[i][j];
            }
        }

        String path = "";
        while (n.ant != null) {
            print[n.coord[0]][n.coord[1]] = "X";
            path = "(" + n.coord[0] + ", " + n.coord[1] + ") " + path;
            n = n.ant;
        }
        path = "(0, 0) " + path;
        print[saida[0]][saida[1]] = "S";
        log += path + "\n\n";
        for (int i = 0; i < lab.length; i++) {
            for (int j = 0; j < lab.length; j++) {
                log += print[i][j] + " ";
            }
            log += "\n";
        }
    }

    private Node lowest() {
        if (open.size() == 0) {
            return null;
        }
        Node menor = open.get(0);
        for (Node n : open) {
            if (n.f < menor.f) {
                menor = n;
            }
        }
        return menor;
    }

    private int heuristica(int atual1, int atual2, int entrada1, int entrada2) {
        int a = 0;
        int e = 0;
        if (atual1 > entrada1) {
            a = atual1 - entrada1;
        } else {
            a = entrada1 - atual1;
        }
        if (atual2 > entrada2) {
            e = atual2 - entrada2;
        } else {
            e = entrada2 - atual2;
        }
        if (a > e) {
            return a;
        } else {
            return e;
        }
    }
    
    public String toString(){
        return log;
    }
}
