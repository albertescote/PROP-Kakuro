package Algoritmes;

import Classes.*;

import java.util.*;

public class KakuroGenerator extends AlgoritmesKakuro {
    private final HashMap<Coordenada, Integer> pistes;
    private final int[][] miKakuro;
    private final KakuroSolver ks;

    public KakuroGenerator(int x, int y) {
        ks = new KakuroSolver();
        pistes = new HashMap<>();
        miKakuro = inicialitzarKakuro(x, y);
    }

    public void generarTaulerAuto(Tauler tauler, int x, int y, boolean s) {
        ponerValores(x, y, s);  //posa valors a celles blanques i * a celles negres de forma random, tenint en compte diferents condicions

        HashMap<Coordenada, Suma> vertical = new HashMap<>();
        HashMap<Coordenada, Suma> horitzontal = new HashMap<>();

        tauler.setTauler(inicialitzarMatriu(x, y, vertical, horitzontal));
        tauler.setHorizontal(horitzontal);
        tauler.setVertical(vertical);
        tauler.assignarAdjacentsSumes();

        comprovarResultat(tauler);
    }

    private int[][] inicialitzarKakuro(int x, int y) {
        int[][] mik = new int[x][y];
        for (int j = 0; j < y; j++) {
            mik[0][j] = -1; //posa la 1a fila a -1 (celles negres)
        }
        for (int i = 1; i < x; i++) {
            mik[i][0] = -1; //posa la 1a columna a -1 (celles negres)
        }
        return mik;
    }

    private void ponerValores(int x, int y, boolean s) {
        //bucle que t'omple numeros, per afegir-ho a poscolum
        HashSet[] posColum = new HashSet[y];
        for (int i = 0; i < y; i++) {
            posColum[i] = omplirPossibilitats();
        }
        for (int i = 1; i < x; i++) { //per cada fila

            HashSet<Integer> posFila = omplirPossibilitats();

            for (int j = 1; j < y; j++) {
                boolean poner = Math.random() > 0.4; //returns a double 1
                if (x == 2 || y == 2) {
                    poner = true;
                } else if (x == 3 && y == 3) {
                    poner = true;
                } else {
                    if (s && (2 * ((y - 1) * (i - 1) + j) > ((x - 1) * (y - 1)))) { //segunda mitad (simetria 31 13)...
                        poner = (miKakuro[x - i][y - j] != -1);
                    }
                    if (posFila.size() == 8 || posColum[j - 1].size() == 8) {
                        //si només has posat un valor posarne un altre (dsp eliminem de posfila)
                        poner = true;
                    }
                    if ((posFila.size() == 9 && j == y - 1) || (posColum[j - 1].size() == 9 && i == x - 1)) {
                        //si es el ultimo y aun no ha puesto, no poner
                        poner = false;
                    }
                    if (Math.min(9 - posFila.size(), 9 - posColum[j - 1].size()) == 2) {
                        //evitar grandes areas de  que venen donades per possibcolum
                        poner = false;
                    }
                }
                if (!poner) {
                    miKakuro[i][j] = -1;
                    posColum[j - 1] = omplirPossibilitats();
                    posFila = omplirPossibilitats();
                } else {
                    HashSet<Integer> p = new HashSet<>(posFila); //fas l'interseccio entre pos fila i poscolum amb deep copy
                    HashSet<Integer> c = new HashSet<>(posColum[j - 1]);
                    p.retainAll(c);

                    Integer[] posibilidades = new Integer[p.size()];
                    p.toArray(posibilidades);

                    if (posibilidades.length == 0) {
                        miKakuro[i][j] = -1;
                        posColum[j - 1] = omplirPossibilitats();
                        posFila = omplirPossibilitats();
                    } else {
                        int n = new Random().nextInt(posibilidades.length); //això et retorna un rnd de 0 inclusive a n exclusvie
                        int nu = posibilidades[n];
                        miKakuro[i][j] = nu;
                        posColum[j - 1].remove(nu);
                        posFila.remove(nu);
                    }
                }
            }

        }
    }

    private HashSet<Integer> omplirPossibilitats() {
        HashSet<Integer> e = new HashSet<>();
        for (int l = 1; l < 10; ++l) {
            e.add(l);
        }
        return e;
    }

    private Cella[][] inicialitzarMatriu(int x, int y, HashMap<Coordenada, Suma> vertical, HashMap<Coordenada, Suma> horitzontal) {
        //inicialitzem matriu de sumes horitzontals a tot fals
        int[][][] sumasHoritzontals = new int[x][y][3]; //matriu de x,y en que en cada espai hi hauria boolea "1",
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < 3; k++) {
                    sumasHoritzontals[i][j][k] = 0; // 0 = false
                }
            }
        }

        //creerem la matriu de sumes horitzontals
        for (int i = 0; i < x; i++) { //per cada fila dadalt a baix
            int longitud = 0;
            int suma = 0;
            for (int j = y - 1; j > -1; --j) { //per cada casella d'esquerra a dreta
                int numb = miKakuro[i][j];
                if (numb == -1) {
                    if (longitud != 0) {
                        sumasHoritzontals[i][j][0] = 1;
                        sumasHoritzontals[i][j][1] = suma;
                        sumasHoritzontals[i][j][2] = longitud;

                    }
                    longitud = 0;
                    suma = 0;

                } else {
                    longitud = longitud + 1;
                    suma = suma + numb;
                }
            }
        }

        //inicialitzem matriu de sumes verticals a tot fals
        int[][][] sumaVerticals = new int[x][y][3]; //matriu de x,y en que en cada espai hi hauria boolea "1",
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < 3; k++) {
                    sumaVerticals[i][j][k] = 0; // 0 = false
                }
            }
        }

        //creerem la matriu de sumes verticals
        for (int j = 0; j < y; j++) {//per cada columna de esquerra a dreta
            int longitud = 0;
            int sum = 0;
            for (int i = x - 1; i > -1; --i) {
                int numb = miKakuro[i][j];
                if (numb == -1) {
                    if (longitud != 0) {
                        sumaVerticals[i][j][0] = 1;
                        sumaVerticals[i][j][1] = sum;
                        sumaVerticals[i][j][2] = longitud;
                    }
                    longitud = 0;
                    sum = 0;
                } else {
                    longitud = longitud + 1;
                    sum = sum + numb;
                }
            }
        }

        Cella[][] t = new Cella[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; ++j) {
                int numero = miKakuro[i][j];
                if (numero == -1) {
                    int[] sv;
                    int[] sh;
                    sv = sumaVerticals[i][j];
                    sh = sumasHoritzontals[i][j];
                    if ((sv[0] == 1) && (sh[0] == 1)) {   //indica que es vertical i horitzontal
                        Coordenada coord = new Coordenada(i, j);
                        Suma su1 = new Suma(sv[1], getFormasSuma(sv[1]));
                        Suma su2 = new Suma(sh[1], getFormasSuma(sh[1]));
                        vertical.put(coord, su1);
                        horitzontal.put(coord, su2);
                        Cella c = new CellaNegra(Integer.toString(sh[1]), Integer.toString(sv[1]), i, j);
                        t[i][j] = c;
                    } else if (sv[0] == 1) { //indica que si que es vertical
                        Coordenada coord = new Coordenada(i, j);
                        Suma su = new Suma(sv[1], getFormasSuma(sv[1]));
                        vertical.put(coord, su);
                        Cella c = new CellaNegra(null, Integer.toString(sv[1]), i, j);
                        t[i][j] = c;
                    } else if (sh[0] == 1) { //indica que es horitzontal
                        Coordenada coord = new Coordenada(i, j);
                        Suma su = new Suma(sh[1], getFormasSuma(sh[1]));
                        horitzontal.put(coord, su);
                        Cella c = new CellaNegra(Integer.toString(sh[1]), null, i, j);
                        t[i][j] = c;
                    } else if (sv[0] == 0) {
                        Cella c = new CellaNegra(null, null, i, j);
                        t[i][j] = c;
                    }


                } else {
                    Cella c = new CellaBlanca("?", false, i, j);
                    t[i][j] = c;
                }
            }
        }
        return t;
    }

    public void comprovarResultat(Tauler tauler) {
        ArrayList<Coordenada> posiciones = new ArrayList<>();
        for (int i = 0; i < tauler.getFiles(); i++) {
            for (int j = 0; j < tauler.getColumnes(); j++) {
                if (tauler.getTauler()[i][j].isWhite()) {
                    Coordenada pos = new Coordenada(i, j);
                    posiciones.add(pos);
                }
            }
        }

        solve2(tauler, posiciones);

        for (Map.Entry<Coordenada, Integer> entry : pistes.entrySet()) {
            tauler.getTauler()[entry.getKey().getFirst()][entry.getKey().getSecond()].setValue(Integer.toString(entry.getValue()));
            tauler.getTauler()[entry.getKey().getFirst()][entry.getKey().getSecond()].setSolved(true);
        }
    }

    private void solve2(Tauler t, ArrayList<Coordenada> posicions) {
        Tauler tcopia = new Tauler(t.getCreador(), t.getFiles(), t.getColumnes(), t.getHores(), t.getMinuts(), t.getSegons());
        tcopia.deepCopyClass(t);


        //ks.solveSimple(tcopia, posicions);

        int a = 0;
        int b = 0;
        boolean solved = true;
        for (int i = 0; (i < t.getFiles()) && (solved); i++) {
            for (int j = 0; (j < t.getColumnes()) && (solved); j++) {
                Cella c = t.getTauler()[i][j];
                if (c.isWhite() && !c.isSolved()) {
                    solved = false;
                    a = i;
                    b = j;
                }
            }
        }
        if (solved) {
            return;
        }

        int value = miKakuro[a][b];

        ArrayList<Coordenada> posi = new ArrayList<>();
        Coordenada coord = new Coordenada(a, b);
        posi.add(coord);

        pistes.put(coord, value);
        tcopia.getTauler()[a][b].setSolved(true);
        tcopia.getTauler()[a][b].setValue(Integer.toString(value));

        solve2(tcopia, posi);
    }

    public boolean esCorrecte(Tauler t) {
        return false;
    }

    public ArrayList<Tauler> resolverKakuro(Tauler t) {
        return null;
    }
}
