package Algoritmes;


import Classes.Coordenada;
import Classes.Tauler;

import java.util.ArrayList;
import java.util.Stack;

public abstract class AlgoritmesKakuro {
    //TODO: crear classe per formas
    private final ArrayList<ArrayList<ArrayList<Integer>>> formas;

    public AlgoritmesKakuro() {
        formas = new ArrayList<>();
        inicialitzarFormes();
    }

    private void inicialitzarFormes() {
        for (int i = 0; i < 46; i++) {
            formas.add(new ArrayList<>());
        }

        for (int i = 0; i < 512; i++) {
            ArrayList<Integer> valores = new ArrayList<>();
            int suma = 0;
            for (int j = 0; j < 10; j++) {
                double x = Math.pow(2, j);
                x = i / x;
                double v = Math.floor(x);
                v = v % 2;

                if (v == 1) {
                    valores.add(j + 1);
                }
                suma += v * (j + 1);
            }
            formas.get(suma).add(valores);
        }
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getFormas() {
        return formas;
    }

    public Stack<Coordenada> omplirPosicionsBuides(Tauler t) {
        Stack<Coordenada> posicions = new Stack<>();
        for (int x = 0; x < t.getFiles(); x++) {
            for (int y = 0; y < t.getColumnes(); y++) {
                if (t.getTauler()[x][y].isWhite() && !t.getTauler()[x][y].isSolved()) {
                    posicions.push(t.getTauler()[x][y].getPair());
                }
            }
        }
        return posicions;
    }

    public ArrayList<ArrayList<Integer>> getFormasSuma(Integer suma) {
        return formas.get(suma);
    }

    public abstract boolean esCorrecte(Tauler t);

    public abstract ArrayList<Tauler> resolverKakuro(Tauler t);

}
