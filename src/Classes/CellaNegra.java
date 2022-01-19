package Classes;

import java.util.HashSet;

public class CellaNegra extends Cella {
    private String sumF;
    private String sumC;


    public CellaNegra(String vf, String vc, int x, int y) {
        super(x, y);
        this.sumF = vf;
        this.sumC = vc;
    }

    public String getValor() {
        if (sumF == null && sumC == null) {
            return "*";
        }
        if (sumF == null) {
            return "C" + sumC;
        }
        if (sumC == null) {
            return "F" + sumF;
        }
        return "C" + sumC + "F" + sumF;
    }

    public boolean isWhite() {
        return false;
    }

    public boolean isSolved() {
        return false;
    }

    public HashSet<Integer> getValues() {
        return null;
    }

    public void setSolved(boolean solved) {

    }

    public void setValue(String value) {

    }

    public void setValues(HashSet<Integer> values) {
    }

    public Coordenada getH() {
        return null;
    }

    public Coordenada getV() {
        return null;
    }

    public void setH(Coordenada h) {
    }

    public void setV(Coordenada v) {
    }

    public String getSumF() {
        return sumF;
    }

    public String getSumC() {
        return sumC;
    }

    public void setSumF(String sumF) {
        this.sumF = sumF;
    }

    public void setSumC(String sumC) {
        this.sumC = sumC;
    }
}
