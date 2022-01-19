package Algoritmes;

import Classes.Cella;
import Classes.Coordenada;
import Classes.Suma;
import Classes.Tauler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class KakuroSolver extends AlgoritmesKakuro {

    public ArrayList<ArrayList<ArrayList<Integer>>> f;

    public KakuroSolver() {
        super();
        f = getFormas();
    }

    public boolean esCorrecte(Tauler t) {
        Tauler tcopia = new Tauler(t.getCreador(), t.getFiles(), t.getColumnes(), t.getHores(), t.getMinuts(), t.getSegons());
        tcopia.deepCopyClass(t);
        //Resol el tauler i guarda a tresultat totes les solucions trobades.
        ArrayList<Tauler> tResultat = resolverKakuro(tcopia);

        //Si el tauler té més d'una solució retorna false, sinó true.
        int cont = 0;
        if (tResultat != null) {
            for (Tauler tauler : tResultat) {
                if (tauler != null) {
                    cont++;
                }
            }
            return cont == 1;
        } else {
            return false;
        }
    }

    public ArrayList<Tauler> resolverKakuro(Tauler t) {
        //Cridem la funcio solve amb les posicions a resoldre, és a dir, totes les caselles blanques buides.
        return solve(t, omplirPosicionsBuides(t));
    }

    private ArrayList<Tauler> solve(Tauler t, Stack<Coordenada> posicions) {
        //Provem de solucionar el tauler de forma directe (comprovant interseccions)
        //Si alguna casella blanca no té cap valor possible retronem null.
        if (!solveSimple(t, posicions)) {
            return null;
        }

        //Comprovem si totes les celles blanques tene algun valor assignat.
        //Si està solucionat es retorna la solució trobada, altrament s'assigna a la coordenada c la coordenada amb menys possibles valors.
        Coordenada c = new Coordenada(-1, -1);
        if (solucionat(t, c)) {
            ArrayList<Tauler> result = new ArrayList<>();
            result.add(t);
            return result;
        }

        ArrayList<Tauler> resultats = new ArrayList<>();

        //Per cada valor possible de la coordenada c provem de resoldre el tauler (backtracking).
        for (Integer valor : t.getTauler()[c.getFirst()][c.getSecond()].getValues()) {

            //creem copies per provar valors no segurs
            Tauler tcopia = new Tauler(t.getCreador(), t.getFiles(), t.getColumnes(), t.getHores(), t.getMinuts(), t.getSegons());
            tcopia.deepCopyClass(t);

            //provem un valor dins els values posssibles
            tcopia.getTauler()[c.getFirst()][c.getSecond()].setSolved(true);
            tcopia.getTauler()[c.getFirst()][c.getSecond()].setValue(valor.toString());

            //creem una coordenada del valor que provem
            Stack<Coordenada> posicio = new Stack<>();
            posicio.push(c);

            //passem la coordenada del valor que provem a solve()
            ArrayList<Tauler> resultatsAux = solve(tcopia, posicio);
            if (resultatsAux != null) {
                resultats.addAll(resultatsAux);
            }
        }
        return resultats;
    }

    //Mira si hi ha alguna cella per resoldre encara, i si és el cas guarda a la coordenada coord, la coordenada amb menys valors possibles.
    private boolean solucionat(Tauler t, Coordenada coord) {
        boolean solved = true;
        for (int i = 0; (i < t.getFiles()) && (solved); i++) {
            for (int j = 0; (j < t.getColumnes()) && (solved); j++) {
                Cella c = t.getTauler()[i][j];
                if (c.isWhite() && !c.isSolved()) {
                    solved = false;
                    if (coord.getFirst() == -1) {
                        coord.setFirst(i);
                        coord.setSecond(j);
                    } else if (c.getValues().size() < t.getTauler()[coord.getFirst()][coord.getSecond()].getValues().size()) {
                        coord.setFirst(i);
                        coord.setSecond(j);
                    }
                }
            }
        }
        return solved;
    }

    public boolean solveSimple(Tauler t, Stack<Coordenada> posicions) {
        HashMap<Coordenada, Suma> horizontal = t.getHorizontal();
        HashMap<Coordenada, Suma> vertical = t.getVertical();
        while (posicions.size() > 0) {
            Coordenada actual = posicions.pop();
            Cella c = t.getTauler()[actual.getFirst()][actual.getSecond()];

            if (!c.isSolved()) {
                HashSet<Integer> posibFila = possibilitats(horizontal, c.getH());
                HashSet<Integer> posibColumna = possibilitats(vertical, c.getV());

                //Calculem intersecció dels possibles valors de la suma horitzontal i vertical i assignem els valors possibles a la cella.
                if (posibFila.size() == 0) c.setValues(posibColumna);
                else if (posibColumna.size() == 0) {
                    c.setValues(posibFila);
                } else {
                    posibFila.retainAll(posibColumna);
                    c.setValues(posibFila);
                }

                //Si no té cap valor possible retornem false. Si té varis valors possibles no fem res i passem a la següent posició.
                if (c.getValues().size() == 0) {
                    return false;
                } else if (c.getValues().size() != 1) {
                    continue;
                }
                //Si té un únic valor possible l'assignem.
                c.setSolved(true);
                Object[] newInt = c.getValues().toArray();
                String s = newInt[newInt.length - 1].toString();
                c.setValue(s);
                c.getValues().remove(Integer.parseInt(s));
            }
            //Ajustem les formes possibles de la suma horitzontal de la cella actual i afegim les celles adjacents a comprovar.
            adaptarFormesNum(horizontal, c.getH(), c.getValor());
            coordenadesComprovar(t, horizontal, c.getH(), actual, posicions);

            //Ajustem les formes possibles de la suma vertical de la cella actual i afegim les celles adjacents a comprovar.
            adaptarFormesNum(vertical, c.getV(), c.getValor());
            coordenadesComprovar(t, vertical, c.getV(), actual, posicions);
        }
        return true;
    }

    private HashSet<Integer> possibilitats(HashMap<Coordenada, Suma> mapaSumes, Coordenada c) {
        HashSet<Integer> posib = new HashSet<>();
        if (!mapaSumes.isEmpty()) {
            for (ArrayList<Integer> forma : mapaSumes.get(c).getFormasNum()) {
                posib.addAll(forma);
            }
        }
        return posib;
    }

    private void adaptarFormesNum(HashMap<Coordenada, Suma> mapaSumes, Coordenada posSuma, String val) {
        if (!mapaSumes.isEmpty()) {
            ArrayList<ArrayList<Integer>> newHorizontal = new ArrayList<>();
            Suma s = mapaSumes.get(posSuma);
            for (ArrayList<Integer> forma : s.getFormasNum()) {
                if (forma.contains(Integer.parseInt(val))) {
                    forma.remove((Integer) Integer.parseInt(val));
                    newHorizontal.add(forma);
                }
            }
            s.setFormasNum(newHorizontal);
        }
    }

    private void coordenadesComprovar(Tauler t, HashMap<Coordenada, Suma> mapaSumes, Coordenada posSuma, Coordenada actual, Stack<Coordenada> posicions){
        for (Cella loc : mapaSumes.get(posSuma).getAdjacents()) {
            Coordenada pos = loc.getPair();
            if (!pos.equals(actual)) {
                boolean b = conteCoordenada(posicions, pos);
                if ((!t.getTauler()[pos.getFirst()][pos.getSecond()].isSolved()) && !(b)) {
                    posicions.push(loc.getPair());
                }
            }
        }
    }

    private boolean conteCoordenada(Stack<Coordenada> pilaCoord, Coordenada coord) {
        for (Coordenada ints : pilaCoord) {
            if (ints.equals(coord)) {
                return true;
            }
        }
        return false;
    }
}
