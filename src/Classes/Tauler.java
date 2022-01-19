package Classes;

import Algoritmes.AlgoritmesKakuro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tauler {

    private final int files;
    private final int columnes;
    private Cella[][] tauler;
    HashMap<Coordenada, Suma> horizontal;
    HashMap<Coordenada, Suma> vertical;
    private final Usuari creador;
    private int minuts, segons, hores;


    public Tauler(Usuari usr, int fil, int col) {
        this.files = fil;
        this.columnes = col;
        this.creador = usr;
        this.hores = 0;
        this.minuts = 0;
        this.segons = 0;
        this.horizontal = new HashMap<>();
        this.vertical = new HashMap<>();
        this.tauler = new Cella[files][columnes];
    }

    public Tauler(Usuari usr, int fil, int col, int hores, int minuts, int segons) {
        this.files = fil;
        this.columnes = col;
        this.creador = usr;
        this.hores = hores;
        this.minuts = minuts;
        this.segons = segons;
        this.horizontal = new HashMap<>();
        this.vertical = new HashMap<>();
        this.tauler = new Cella[files][columnes];
    }

    public void generarTaulerCella(String[][] taulerLlegit, AlgoritmesKakuro ak) {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (!esBlanca(taulerLlegit[i][j], i, j)) {
                    tauler[i][j] = crearNegra(i, j, taulerLlegit[i][j], ak);
                }
            }
        }
        assignarAdjacentsSumes();
    }

    public void assignarAdjacentsSumes() {
        for (Map.Entry<Coordenada, Suma> entry : horizontal.entrySet()) {
            Suma s = entry.getValue();
            Coordenada k = entry.getKey();
            boolean trobat = false;
            ArrayList<Cella> adjacents = new ArrayList<>();
            for (int i = k.getSecond() + 1; i < columnes && !trobat; i++) {
                if (!tauler[k.getFirst()][i].isWhite()) {
                    trobat = true;
                } else {
                    adjacents.add(tauler[k.getFirst()][i]);
                    tauler[k.getFirst()][i].setH(entry.getKey());
                }
            }
            s.setAdjacents(adjacents);
            s.setFormasNumSize();
        }
        for (Map.Entry<Coordenada, Suma> entry : vertical.entrySet()) {
            Suma s = entry.getValue();
            Coordenada k = entry.getKey();
            boolean trobat = false;
            ArrayList<Cella> adjacents = new ArrayList<>();
            for (int i = k.getFirst() + 1; i < files && !trobat; i++) {
                if (!tauler[i][k.getSecond()].isWhite()) {
                    trobat = true;
                } else {
                    adjacents.add(tauler[i][k.getSecond()]);
                    tauler[i][k.getSecond()].setV(entry.getKey());
                }
            }
            s.setAdjacents(adjacents);
            s.setFormasNumSize();
        }
    }

    private CellaNegra crearNegra(int i, int j, String val, AlgoritmesKakuro ak) {
        Coordenada coord = new Coordenada();
        coord.setFirst(i);
        coord.setSecond(j);
        CellaNegra c;

        if (!val.equals("*")) {
            if (val.charAt(0) == 'F') {
                String[] a = val.split("F");
                c = new CellaNegra(a[1], null, i, j);
                tauler[i][j] = c;
                String valor = a[1];
                Suma s = new Suma(Integer.parseInt(valor), ak.getFormasSuma(Integer.parseInt(valor)), null);
                horizontal.put(coord, s);
            } else {
                int b = val.indexOf('F');
                String[] a = val.split("C");
                if (b == -1) {
                    c = new CellaNegra(null, a[1], i, j);
                    tauler[i][j] = c;
                    String valor = a[1];
                    Suma s = new Suma(Integer.parseInt(valor), ak.getFormasSuma(Integer.parseInt(valor)), null);
                    vertical.put(coord, s);

                } else {
                    String[] d = a[1].split("F");
                    c = new CellaNegra(d[1], d[0], i, j);
                    tauler[i][j] = c;
                    String valor1 = d[0];
                    String valor2 = d[1];
                    Suma s1 = new Suma(Integer.parseInt(valor1), ak.getFormasSuma(Integer.parseInt(valor1)), null);
                    Suma s2 = new Suma(Integer.parseInt(valor2), ak.getFormasSuma(Integer.parseInt(valor2)), null);
                    vertical.put(coord, s1);
                    horizontal.put(coord, s2);

                }
            }
        } else {
            c = new CellaNegra(null, null, i, j);

        }
        return c;
    }

    private boolean esBlanca(String val, int indexf, int indexc) {
        if (val.equals("?")) {
            CellaBlanca c = new CellaBlanca(val, false, indexf, indexc);
            tauler[indexf][indexc] = c;
            return true;
        } else if ((val.compareTo("0") >= 0) && (val.compareTo("9") <= 0)) {
            CellaBlanca c = new CellaBlanca(val, true, indexf, indexc);
            tauler[indexf][indexc] = c;
            return true;
        } else return false;
    }

    public void deepCopyClass(Tauler t) {
        for (int i = 0; i < t.getTauler().length; i++) {
            for (int j = 0; j < t.getTauler()[i].length; j++) {
                Cella caux = t.getTauler()[i][j];
                if (caux.isWhite()) {
                    CellaBlanca c = new CellaBlanca(caux.getValor(), caux.isSolved(), i, j);
                    c.deepCopyCella(caux);
                    tauler[i][j] = c;
                } else {

                    CellaNegra c = new CellaNegra(caux.getSumF(), caux.getSumC(), i, j);

                    tauler[i][j] = c;
                }
            }
        }
        deepCopySumes(t.getHorizontal(), t.getVertical());
    }


    private void deepCopySumes(HashMap<Coordenada, Suma> hor, HashMap<Coordenada, Suma> ver) {
        for (Coordenada i : hor.keySet()) {
            Suma s = new Suma(hor.get(i).getSuma(), hor.get(i).getFormasNum(), hor.get(i).getFormasLength());
            s.setAdjacents(deepCopyAdjacents(hor.get(i).getAdjacents(), true));
            horizontal.put(i, s);
        }
        for (Coordenada i : ver.keySet()) {
            Suma s = new Suma(ver.get(i).getSuma(), ver.get(i).getFormasNum(), ver.get(i).getFormasLength());
            s.setAdjacents(deepCopyAdjacents(ver.get(i).getAdjacents(), false));
            vertical.put(i, s);
        }
    }

    private ArrayList<Cella> deepCopyAdjacents(ArrayList<Cella> adjacents, boolean h){
        ArrayList<Cella> newAdjacents = new ArrayList<>();
        for(Cella c: adjacents){
            newAdjacents.add(tauler[c.getPair().getFirst()][c.getPair().getSecond()]);
            if(h) {
                tauler[c.getPair().getFirst()][c.getPair().getSecond()].setH(c.getH());
            }else{
                tauler[c.getPair().getFirst()][c.getPair().getSecond()].setV(c.getV());
            }
        }
        return newAdjacents;
    }

    public void inicilaitzarPosVal(){
        for(int i = 0; i<files; i++){
            for(int j = 0; j<columnes; j++){
                if(tauler[i][j].isWhite()) {
                    Suma s = horizontal.get(tauler[i][j].getH());
                    HashSet<Integer> posval = new HashSet<>();
                    for (ArrayList<ArrayList<Integer>> arrayLists : s.getFormasLength()) {
                        for (ArrayList<Integer> arrayList : arrayLists) {
                            posval.addAll(arrayList);
                        }
                    }
                    tauler[i][j].setValues(posval);

                    s = vertical.get(tauler[i][j].getV());
                    posval = new HashSet<>();
                    for (ArrayList<ArrayList<Integer>> arrayLists : s.getFormasLength()) {
                        for (ArrayList<Integer> arrayList : arrayLists) {
                            posval.addAll(arrayList);
                        }
                    }
                    tauler[i][j].setValues(posval);
                }
            }
        }
    }

    public void imprimeixTauler() {
        System.out.println(creador.getName());
        System.out.println(hores + ":" + minuts + ":" + segons);
        System.out.println(files + "," + columnes);
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes - 1; j++) {
                System.out.print(tauler[i][j].getValor() + ",");
            }
            System.out.println(tauler[i][columnes - 1].getValor());
        }
    }

    public int getFiles() {
        return files;
    }

    public int getColumnes() {
        return columnes;
    }

    public Usuari getCreador() {
        return creador;
    }

    public Cella[][] getTauler() {
        return tauler;
    }

    public void setTauler(Cella[][] tauler) {
        this.tauler = tauler;
    }

    public int getMinuts() {
        return minuts;
    }

    public int getSegons() {
        return segons;
    }

    public int getHores() {
        return hores;
    }

    public HashMap<Coordenada, Suma> getHorizontal() {
        return horizontal;
    }

    public HashMap<Coordenada, Suma> getVertical() {
        return vertical;
    }

    public void setHores(int hores) {
        this.hores = hores;
    }

    public void setMinuts(int minuts) {
        this.minuts = minuts;
    }

    public void setSegons(int segons) {
        this.segons = segons;
    }

    public void setHorizontal(HashMap<Coordenada, Suma> horizontal) {
        this.horizontal = horizontal;
    }

    public void setVertical(HashMap<Coordenada, Suma> vertical) {
        this.vertical = vertical;
    }
}
