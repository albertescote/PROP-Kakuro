package Algoritmes;

import Classes.*;

import java.util.*;

public class KakuroGenerator2 extends AlgoritmesKakuro {

    public void controladorGenerator(Tauler t, int i, int j) {
        Tauler tcopia;
        boolean correcte;

        //Genero taulers automàticament fins que sigui correcte, és a dir, que tingui solució única.
        do {
            tcopia = new Tauler(t.getCreador(), t.getFiles(), t.getColumnes());
            correcte = generarTaulerAuto(tcopia, i, j);
        } while (!correcte);

        //Deixo les caselles a punt per poder-lo passar pel solve, és a dir pos "?" a les caselles blanques
        deixarBuit(tcopia);
        t.deepCopyClass(tcopia);
    }

    private boolean generarTaulerAuto(Tauler t, int i, int j) {
        //Omplo caselles negres i blanques random
        generarTaulerBuit(t, i, j);

        //Poso a posicions totes les caselles blanques
        Stack<Coordenada> posicions = omplirPosicionsBuides(t);

        //Provo de resoldre el tauler buscant interseccions úniques
        while (posicions.size() > 0) {
            Coordenada pos = posicions.pop();
            if (!t.getTauler()[pos.getFirst()][pos.getSecond()].isSolved()) {
                interseccioUnica(t, pos);
            }
        }

        //Comprovo que totes les caselles blanques tinguin solució, sinó retrona false
        return teSolucio(t);
    }

    private void deixarBuit(Tauler t) {
        for (int x = 1; x < t.getFiles(); x++) {
            for (int y = 1; y < t.getColumnes(); y++) {
                if (t.getTauler()[x][y].isWhite()) {
                    t.getTauler()[x][y].setValue("?");
                    t.getTauler()[x][y].setSolved(false);
                }
            }
        }
    }

    private boolean teSolucio(Tauler t) {
        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes(); j++) {
                if (t.getTauler()[i][j].isWhite() && !t.getTauler()[i][j].isSolved()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void generarTaulerBuit(Tauler t, int i, int j) {

        blancaNegra(t, i, j);
        horitzontalVertical(t, i, j);

        inicialitzarFormesLength(t.getHorizontal());
        inicialitzarFormesLength(t.getVertical());

        t.inicilaitzarPosVal();
    }

    private void blancaNegra(Tauler t, int filesT, int columnesT){
        Cella[][] tauler = new Cella[filesT][columnesT];

        //Poso celles negres a la primera fila i columna
        for (int x = 0; x < filesT; x++) {
            tauler[x][0] = new CellaNegra(null, null, x, 0);
        }
        for (int y = 0; y < columnesT; y++) {
            tauler[0][y] = new CellaNegra(null, null, 0, y);
        }

        //Inicilaitzo els valors possibles de les columnes a 9
        int[] posColum = new int[columnesT];
        for (int k = 0; k < columnesT; k++) {
            posColum[k] = 9;
        }

        //Itero sobre les files
        for (int x = 1; x < filesT; x++) {
            int posFila = 9;
            for (int y = 1; y < columnesT; y++) {
                //Si vull posar una cella blanca, primer comprovo que no hi hagi 9 celles blanques seguides a les files i columnes.
                if (posarBlanca(filesT, columnesT, x, y, posFila, posColum[y])) {
                    if (posFila == 0 || posColum[y] == 0) {
                        tauler[x][y] = new CellaNegra(null, null, x, y);
                        posColum[y] = 9;
                        posFila = 9;
                    }else {
                        tauler[x][y] = new CellaBlanca("?", false, x, y);
                        posColum[y]--;
                        posFila--;
                    }
                } else {
                    tauler[x][y] = new CellaNegra(null, null, x, y);
                    posColum[y] = 9;
                    posFila = 9;
                }
            }
        }
        t.setTauler(tauler);
    }

    //TODO:s'ha de millorar
    private boolean posarBlanca(int filesT, int columnesT, int fila, int columna, int posFila, int posColum){
        if(posFila == 0 || posColum == 0){
            return false;
        }

        //tablero pequeno, solo poner blancas
        if(filesT <= 3 && columnesT <= 3){
            return true;
        }

        boolean posar = Math.random() > 0.5;

        //Posar com a mínim dos valors
        if(posFila == 8 || posColum == 8){
            posar = true;
        }

        //Si és l'últim i encara no ha posat cap blanca, no cal que la posi.
        if(posFila == 9 && (fila == filesT-1)){
            posar = true;
        }
        if(posColum== 9 && (columna == columnesT-1)){
            posar = true;
        }

        //Minimitzem les àrees blanques grans
        if(Math.min(9-posColum, 9-posFila) == 2){
            posar = false;
        }

        return posar;
    }

    private void horitzontalVertical(Tauler t, int filesT, int columnesT){
        HashMap<Coordenada, Suma> horizontal = new HashMap<>();
        HashMap<Coordenada, Suma> vertical = new HashMap<>();

        for (int x = 0; x < filesT; x++) {
            for (int y = 0; y < columnesT; y++) {
                if (!t.getTauler()[x][y].isWhite()) {

                    //Si estem en una casella negra i la casella de la dreta és blanca, afegim la cella negra a horitzontal.
                    if ((y + 1) < columnesT) {
                        if (t.getTauler()[x][y + 1].isWhite()) {
                            Coordenada coord = new Coordenada(x, y);
                            Suma s1 = new Suma(0, null, null);
                            horizontal.put(coord, s1);
                        }
                    }

                    //Si estem en una casella negra i la casella de sota és blanca, afegim la cella negra a vertical.
                    if ((x + 1) < filesT) {
                        if (t.getTauler()[x + 1][y].isWhite()) {
                            Coordenada coord = new Coordenada(x, y);
                            Suma s2 = new Suma(0, null, null);
                            vertical.put(coord, s2);
                        }
                    }
                }
            }
        }

        t.setHorizontal(horizontal);
        t.setVertical(vertical);
        t.assignarAdjacentsSumes();
    }

    private void inicialitzarFormesLength(HashMap<Coordenada, Suma> mapaSumes){
        for (Map.Entry<Coordenada, Suma> entry : mapaSumes.entrySet()) {
            entry.getValue().setFormasLength(getFormasLength(entry.getValue().getAdjacents().size()));
        }
    }


    private void interseccioUnica(Tauler t, Coordenada c) {
        Coordenada ch = t.getTauler()[c.getFirst()][c.getSecond()].getH();
        Coordenada cv = t.getTauler()[c.getFirst()][c.getSecond()].getV();

        Suma sh = t.getHorizontal().get(ch);
        Suma sv = t.getVertical().get(cv);

        ArrayList<ArrayList<ArrayList<Integer>>> formesH = sh.getFormasLength();
        ArrayList<ArrayList<ArrayList<Integer>>> formesV = sv.getFormasLength();
        boolean b = false;

        //Per tots els valors possibles de la suma horitzontal, busco si hi ha una intersecció única amb la suma vertical.
        //La suma hortzontal comença des de 0, i la vertical des de 45, ja que l'intersecció de valors extrems té menys solucions.
        for (int ih = 0; ih < formesH.size() && !b; ih++) {
            if (formesH.get(ih).size() != 0) {
                for (int iv = formesV.size() - 1; iv >= 0 && !b; iv--) {
                    if (formesV.get(iv).size() != 0) {
                        ArrayList<ArrayList<Integer>> auxh = deepCopyArrayList(formesH.get(ih));
                        ArrayList<ArrayList<Integer>> auxv = deepCopyArrayList(formesV.get(iv));
                        int[] interseccio = unicaInt(auxh, auxv);

                        //Si  l'intersecció és 1 voldrà dir que hi ha un valor únic per la suma horitzontal ih i la suma vertical iv.
                        //Pero s'haura de comprovar que aquell valor és correcte.
                        if (interseccio[0] == 1) {
                            //Creem copies per provar valors no segurs
                            Tauler tcopia = new Tauler(t.getCreador(), t.getFiles(), t.getColumnes(), t.getHores(), t.getMinuts(), t.getSegons());
                            tcopia.deepCopyClass(t);

                            //Provem el valor de l'intersecció única trobada
                            tcopia.getTauler()[c.getFirst()][c.getSecond()].setSolved(true);
                            tcopia.getTauler()[c.getFirst()][c.getSecond()].setValue(Integer.toString(interseccio[1]));

                            //Si suma horitzontal i vertical no tenia valor assignat, li posem.
                            if (sh.getSuma() == 0) {
                                tcopia.getHorizontal().get(ch).setSuma(ih);
                                tcopia.getTauler()[ch.getFirst()][ch.getSecond()].setSumF(Integer.toString(ih));
                            }
                            if (sv.getSuma() == 0) {
                                tcopia.getVertical().get(cv).setSuma(iv);
                                tcopia.getTauler()[cv.getFirst()][cv.getSecond()].setSumC(Integer.toString(iv));
                            }

                            //Ajustem el tauler (possibles valors de les celles) i mirem si podem posar algun valor més.
                            adaptarFormasLength(tcopia.getHorizontal().get(ch), tcopia.getVertical().get(cv), interseccio[1]);
                            comprovarTauler(tcopia, omplirPosicionsBuides(tcopia));
                            
                            //Comprovem que cap cella s'hagi quedat sense valors possibles
                            b = valorCorrecte(tcopia);

                            //Si es correcte assignem la copia al tauler real
                            if (b) {
                                t.deepCopyClass(tcopia);
                                formesH.set(ih, auxh);
                                formesV.set(iv, auxv);
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ArrayList<Integer>> deepCopyArrayList(ArrayList<ArrayList<Integer>> formes) {
        ArrayList<ArrayList<Integer>> aux = new ArrayList<>();
        for (int i = 0; i < formes.size(); i++) {
            ArrayList<Integer> aux2 = new ArrayList<>();
            for (int j = 0; j < formes.get(i).size(); j++) {
                aux2.add(j, formes.get(i).get(j));
            }
            aux.add(i, aux2);
        }
        return aux;
    }

    private int[] unicaInt(ArrayList<ArrayList<Integer>> formesH, ArrayList<ArrayList<Integer>> formesV) {
        int[] ret = new int[2];
        for (ArrayList<Integer> h : formesH) {
            for (ArrayList<Integer> v : formesV) {
                ArrayList<Integer> aux = (ArrayList<Integer>) h.clone();
                for (Integer integer : h) {
                    if (v.contains(integer)) {
                        ret[0]++;
                    }
                }
                aux.retainAll(v);
                if (aux.size() == 1) {
                    ret[1] = aux.get(0);
                }
            }
        }
        return ret;
    }

    private void adaptarFormasLength(Suma sh, Suma sv, int val) {
        //Ajusto les formes de la suma horitzontal
        sh.setFormasLength(adaptarFormasLengthIndividual(sh, val));
        //Ajusto els valors possibles de les celles blanques adjacents de sh segons els valors possibles de sh.
        recalcularPosVal(sh);

        //Ajusto les formes de la suma vertical
        sv.setFormasLength(adaptarFormasLengthIndividual(sv, val));
        //Ajusto els valors possibles de les celles blanques adjacents de sv segons els valors possibles de sv.
        recalcularPosVal(sv);
    }

    private ArrayList<ArrayList<ArrayList<Integer>>> adaptarFormasLengthIndividual(Suma s, int val){
        Object o = val;

        //Si s té valor deixo només els valors possibles que sumin sh, i a més, d'aquests valors possibles en trec val ja que ja està assignat.
        ArrayList<ArrayList<ArrayList<Integer>>> newFormasLength = new ArrayList<>();
        if (s.getSuma() != 0) {
            for (int i = 0; i < s.getFormasLength().size(); i++) {
                ArrayList<ArrayList<Integer>> auxlist = new ArrayList<>();
                if (i == s.getSuma()) {
                    for (int j = 0; j < s.getFormasLength().get(i).size(); j++) {
                        if (s.getFormasLength().get(i).get(j).contains(val)) {
                            s.getFormasLength().get(i).get(j).remove(o);
                            auxlist.add(s.getFormasLength().get(i).get(j));
                        }
                    }
                }
                newFormasLength.add(i, auxlist);
            }
        }
        //Si s no té valor trec val de tots els valors possibles.
        else {
            for (int i = 0; i < s.getFormasLength().size(); i++) {
                ArrayList<ArrayList<Integer>> auxlist = new ArrayList<>();
                for (int j = 0; j < s.getFormasLength().get(i).size(); j++) {
                    if (s.getFormasLength().get(i).get(j).contains(val)) {
                        s.getFormasLength().get(i).get(j).remove(o);
                        auxlist.add(s.getFormasLength().get(i).get(j));
                    }
                }
                newFormasLength.add(i, auxlist);
            }
        }
        return newFormasLength;
    }

    private void comprovarTauler(Tauler t, Stack<Coordenada> posiconsComprovar) {
        Stack<Coordenada> comprovar = new Stack<>();
        boolean modificat = false;

        while (posiconsComprovar.size() > 0) {
            Coordenada posaux = posiconsComprovar.pop();
            Cella c = t.getTauler()[posaux.getFirst()][posaux.getSecond()];
            int valor = 0;

            //Comprovem si algun valor del tauler té 1 sol possible valor. Si és així li assignem el valor a la casella.
            if (c.getValues().size() == 1) {
                modificat = true;
                c.setSolved(true);
                Object[] o = c.getValues().toArray();
                c.setValue(o[0].toString());
                valor = Integer.parseInt(o[0].toString());
            }
            //Comprovem si la cella que estem mirant té un únic valor possible de la fila d'adjacents horitzontals. Si és així li assignem el valor a la casella.
            else if (t.getHorizontal().get(c.getH()).getSuma() != 0) {
                int val = unicValor(t.getHorizontal().get(c.getH()), c);
                if (val != -1) {
                    modificat = true;
                    c.setSolved(true);
                    c.setValue(Integer.toString(val));
                    valor = val;
                }
            }
            //Comprovem si la cella que estem mirant té un únic valor possible de la fila d'adjacents verticals. Si és així li assignem el valor a la casella.
            else if (t.getVertical().get(c.getV()).getSuma() != 0) {
                int val = unicValor(t.getVertical().get(c.getV()), c);
                if (val != -1) {
                    modificat = true;
                    c.setSolved(true);
                    c.setValue(Integer.toString(val));
                    valor = val;
                }
            }
            //Si hem resolt la casella comprovem les caselles negres suma horitzontal i vertical i ajustem les formes d'aquestes.
            //Si no l'hem resolt l'afegim a la pila per tornar-la a comprovar en cas d'haver modificat alguna casella.
            if (c.isSolved()) {
                setSumaBuida(t, c);
                adaptarFormasLength(t.getHorizontal().get(c.getH()), t.getVertical().get(c.getV()), valor);
            } else {
                comprovar.push(posaux);
            }
        }
        //Si hem modificat alguna casella tornem a comprovar el tauler pels valors que no han estat resolts.
        if (modificat) {
            comprovarTauler(t, comprovar);
        }
    }

    private void setSumaBuida(Tauler t, Cella c) {
        //Comprova si la suma horitzontal de la cella c té valor. Si no en té i té tots els adjacents resolts, li assigna el valor de la suma dels adjacents.
        if (t.getTauler()[c.getV().getFirst()][c.getV().getSecond()].getSumC() == null) {
            String s = adjacentsComplet(t.getVertical().get(c.getV()).getAdjacents());
            if (s != null) {
                t.getTauler()[c.getV().getFirst()][c.getV().getSecond()].setSumC(s);
                t.getVertical().get(c.getV()).setSuma(Integer.parseInt(s));
            }
        }
        //Comprova si la suma vertical de la cella c té valor. Si no en té i té tots els adjacents resolts, li assigna el valor de la suma dels adjacents.
        if (t.getTauler()[c.getH().getFirst()][c.getH().getSecond()].getSumF() == null) {
            String s = adjacentsComplet(t.getHorizontal().get(c.getH()).getAdjacents());
            if (s != null) {
                t.getTauler()[c.getH().getFirst()][c.getH().getSecond()].setSumF(s);
                t.getHorizontal().get(c.getH()).setSuma(Integer.parseInt(s));
            }
        }
    }

    private int unicValor(Suma s, Cella c) {
        int value = -1;
        int cont = 0;
        //Mira per tots els valors possibles que pot agafar la cella c si les altres celles adjacents poden agafar aquell valor.
        //Si cap altra cella el té, trobat = true.
        for (Integer val : c.getValues()) {
            boolean trobat = false;
            for (Cella caux : s.getAdjacents()) {
                if (!(c.getPair().getFirst().equals(caux.getPair().getFirst()) && c.getPair().getSecond().equals(caux.getPair().getSecond()))) {
                    if (!caux.isSolved() && caux.getValues().contains(val)) {
                        trobat = true;
                    }
                }
            }
            if (!trobat) {
                value = val;
                cont++;
            }
        }
        //Si només té un sol valor únic, retorna aquell valor.
        if (cont == 1) return value;
        else return -1;
    }


    private void recalcularPosVal(Suma s) {
        HashSet<Integer> posval = new HashSet<>();

        //Posa tots el valors possibles de la suma s en un set posval.
        for (ArrayList<ArrayList<Integer>> arrayLists : s.getFormasLength()) {
            for (ArrayList<Integer> arrayList : arrayLists) {
                posval.addAll(arrayList);
            }
        }

        //Per totes les celles adjacents de la suma s mira si la cella té algun valor possible que no estigui a posval. Si és així l'elimina.
        for (Cella c : s.getAdjacents()) {
            if (!c.isSolved()) {
                HashSet<Integer> posvalaux = (HashSet<Integer>) posval.clone();
                for (Integer it : posval) {
                    if (!c.getValues().contains(it)) {
                        posvalaux.remove(it);
                    }
                }
                c.setValues(posvalaux);
            }
        }
    }

    //Per totes les celles d'adjacents comprova si estan resoltes. Si totes ho estan retorna el valor de la suma.
    private String adjacentsComplet(ArrayList<Cella> adjacents) {
        boolean complet = true;
        int sum = 0;

        for (Cella c : adjacents) {
            if (!c.isSolved()) {
                complet = false;
            } else {
                sum += Integer.parseInt(c.getValor());
            }
        }
        if (complet) {
            return Integer.toString(sum);
        } else return null;
    }

    private boolean valorCorrecte(Tauler t) {
        boolean correcte = valorCorrecteIndividual(t.getHorizontal());
        if(correcte)    return valorCorrecteIndividual(t.getVertical());
        else    return false;
    }

    private boolean valorCorrecteIndividual(HashMap<Coordenada, Suma> mapaSumes){
        //Per totes les sumes de mapaSumes comprova si alguna de les seves celles adjacents te 0 valors possibles.
        //També comprova si la suma de valors possibles és menor al número de celles adjacnets.
        //Si es compleix algun dels casos anteriors retorna false.
        for (Map.Entry<Coordenada, Suma> entry : mapaSumes.entrySet()) {
            HashSet<Integer> posval = new HashSet<>();
            int cont = 0;
            for (Cella c : entry.getValue().getAdjacents()) {
                if (!c.isSolved()) {
                    if (c.getValues().size() == 0) return false;
                    posval.addAll(c.getValues());
                    cont++;
                }
            }
            if (posval.size() < cont) return false;
        }
        return true;
    }

    //Retorna totes les formes possibles de sumar els numeros del 0 al 45 segons la llargada de les celles adjacents
    private ArrayList<ArrayList<ArrayList<Integer>>> getFormasLength(int length) {
        ArrayList<ArrayList<ArrayList<Integer>>> aux = new ArrayList<>();
        for (int i = 0; i < getFormas().size(); i++) {
            ArrayList<ArrayList<Integer>> aux2 = new ArrayList<>();
            for (ArrayList<Integer> ai2 : getFormas().get(i)) {
                if (ai2.size() == length) {
                    aux2.add(ai2);
                }
            }
            aux.add(i, aux2);
        }
        return aux;
    }

    public boolean esCorrecte(Tauler t) {
        return false;
    }

    public ArrayList<Tauler> resolverKakuro(Tauler t) {
        return null;
    }

}
