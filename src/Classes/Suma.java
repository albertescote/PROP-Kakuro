package Classes;

import java.util.ArrayList;

public class Suma {
    private int suma;
    private ArrayList<Cella> adjacents;
    private ArrayList<ArrayList<Integer>> formasNum;
    private ArrayList<ArrayList<ArrayList<Integer>>> formasLength;

    public Suma(int s, ArrayList<ArrayList<Integer>> formas) {
        this.suma = s;
        ArrayList<ArrayList<Integer>> aux = new ArrayList<>();

        if (formas != null) {
            for (ArrayList<Integer> f : formas) {
                aux.add((ArrayList<Integer>) f.clone());
            }
        }

        this.formasNum = aux;
    }

    public Suma(int s, ArrayList<ArrayList<Integer>> formas, ArrayList<ArrayList<ArrayList<Integer>>> formasL) {
        this.suma = s;
        ArrayList<ArrayList<Integer>> aux = new ArrayList<>();

        if (formas != null) {
            for (ArrayList<Integer> f : formas) {
                aux.add((ArrayList<Integer>) f.clone());
            }
        }

        this.formasNum = aux;

        ArrayList<ArrayList<ArrayList<Integer>>> aux1 = new ArrayList<>();
        if(formasL != null) {
            for (int i = 0; i < formasL.size(); i++) {
                ArrayList<ArrayList<Integer>> aux2 = new ArrayList<>();
                for (int j = 0; j < formasL.get(i).size(); j++) {
                    ArrayList<Integer> aux3 = new ArrayList<>();
                    for (int k = 0; k < formasL.get(i).get(j).size(); k++) {
                        aux3.add(k, formasL.get(i).get(j).get(k));
                    }
                    aux2.add(j, aux3);
                }
                aux1.add(i, aux2);
            }
        }
        this.formasLength = aux1;
    }

    public ArrayList<Cella> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(ArrayList<Cella> adjacents) {
        this.adjacents = adjacents;
    }

    public void setFormasNumSize() {
        ArrayList<ArrayList<Integer>> aux = new ArrayList<>();
        for (ArrayList<Integer> integers : formasNum) {
            if (integers.size() == adjacents.size()) {
                aux.add(integers);
            }
        }
        formasNum = aux;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int s) {
        this.suma = s;
    }

    public ArrayList<ArrayList<Integer>> getFormasNum() {
        return formasNum;
    }

    public void setFormasNum(ArrayList<ArrayList<Integer>> formasNum) {
        this.formasNum = formasNum;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getFormasLength() {
        return formasLength;
    }

    public void setFormasLength(ArrayList<ArrayList<ArrayList<Integer>>> formasLength) {
        this.formasLength = formasLength;
    }
}
