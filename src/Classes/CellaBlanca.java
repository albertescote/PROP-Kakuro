package Classes;

import java.util.HashSet;

public class CellaBlanca extends Cella {


    private boolean solved;
    private HashSet<Integer> values;
    private Coordenada h;
    private Coordenada v;
    private String value;

    public CellaBlanca(String i, boolean sol, int x, int y) {
        super(x, y);
        this.solved = sol;
        this.value = i;
        values = new HashSet<>();
        this.h = new Coordenada();
        this.v = new Coordenada();
    }

    public void deepCopyCella(Cella caux) {
        if (caux.getH() != null) {
            this.h.clone(caux.getH());
        }
        if (caux.getV() != null) {
            this.v.clone(caux.getV());
        }
        this.values.addAll(caux.getValues());
    }

    public boolean isSolved() {
        return solved;
    }

    public String getValor() {
        return value;
    }

    public Coordenada getH() {
        return h;
    }

    public void setH(Coordenada h) {
        this.h = h;
    }

    public HashSet<Integer> getValues() {
        return values;
    }

    public void setValues(HashSet<Integer> values) {
        this.values = values;
    }


    public Coordenada getV() {
        return v;
    }

    public void setV(Coordenada v) {
        this.v = v;
    }

    public String getSumF() {
        return null;
    }

    public String getSumC() {
        return null;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isWhite() {
        return true;
    }

    public void setSumF(String sumF) {

    }

    public void setSumC(String sumC) {

    }
}

