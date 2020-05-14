package ia;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AStar {

    private static class Node {

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

    private final String[][] labirinto = new Labirinto().getLabirinto();
    private final int[] entrada, saida;
    private final ArrayList<Node> open;
    private final ArrayList<Node> closed;
    private String log = "";

    public AStar(int[] entrada, int[] saida) throws FileNotFoundException {

        this.entrada = entrada;
        this.saida = saida;

        open = new ArrayList<>();
        closed = new ArrayList<>();

        Node n = solve();

        if (n == null || (saida[0] == 0 && saida[1] == 0)) {
            log = "\n\nO caminho entre a entrada e a saida nao foi encontrado pelo A*";
        }
    }

    private Node solve() { //resolve o labirinto
        open.add(new Node(0, heuristica(0, 0, saida[0], saida[1]), entrada, null, "open"));

        Node current;

        for (int i = 0; i < labirinto.length * labirinto[0].length; i++) {

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

            vizinhos(new int[]{current.coord[0], current.coord[1]}, current);
        }
        return null;
    }

    private void vizinhos(int[] cord, Node n) { //chama para todos os vizinhos da coordenada atual
        vizinhos(cord[0] - 1, cord[1] - 1, n);
        vizinhos(cord[0] - 1, cord[1], n);
        vizinhos(cord[0], cord[1] - 1, n);
        vizinhos(cord[0] - 1, cord[1] + 1, n);
        vizinhos(cord[0] + 1, cord[1] - 1, n);
        vizinhos(cord[0] + 1, cord[1] + 1, n);
        vizinhos(cord[0] + 1, cord[1], n);
        vizinhos(cord[0], cord[1] + 1, n);
    }

    private void vizinhos(int a, int b, Node ant) { //recebe uma coordenada, trata e cria o nodo
        if (a < 0 || b < 0 || a > labirinto.length - 1 || b > labirinto[0].length - 1) {
            return;
        }

        if (labirinto[a][b].equals("1") || labirinto[a][b].equals("B")) {
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

    private void path(Node n) { //guarda o caminho e o labirinto no log

        log += "\nMelhor saida encontrada pelo A*\n";
        String[][] print = new String[labirinto.length][labirinto.length];

        for (int i = 0; i < labirinto.length; i++) {
            System.arraycopy(labirinto[i], 0, print[i], 0, labirinto.length);
        }

        String path = "";

        while (n.ant != null) {
            print[n.coord[0]][n.coord[1]] = "X";
            path = "(" + n.coord[0] + ", " + n.coord[1] + ") " + path;
            n = n.ant;
        }

        print[saida[0]][saida[1]] = "S";
        path = "(0, 0) " + path;
        log += path + "\n\n";

        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto.length; j++) {
                log += print[i][j] + " ";
            }
            log += "\n";
        }
    }

    private Node lowest() { //retorna o menor da lista open
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

    private int heuristica(int atual1, int atual2, int entrada1, int entrada2) { //calcula a distancia entre duas coordenadas
        return Math.max(Math.abs(atual1 - entrada1), Math.abs(atual2 - entrada2));
    }

    public String toString() {
        return log;
    }
}
