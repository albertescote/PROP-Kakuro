package Classes;

import java.util.HashSet;

public abstract class Cella {

    private final Coordenada pair;

    public Cella(int x, int y) {
        this.pair = new Coordenada();
        this.pair.setFirst(x);
        this.pair.setSecond(y);
    }

    public Coordenada getPair() {
        return pair;
    }

    public abstract String getValor();

    public abstract boolean isSolved();

    public abstract HashSet<Integer> getValues();

    public abstract void setValues(HashSet<Integer> values);

    public abstract void setSolved(boolean solved);

    public abstract void setValue(String value);

    public abstract boolean isWhite();

    public abstract Coordenada getH();

    public abstract Coordenada getV();

    public abstract void setH(Coordenada h);

    public abstract void setV(Coordenada v);

    public abstract String getSumF();

    public abstract String getSumC();

    public abstract void setSumF(String sumF);

    public abstract void setSumC(String sumC);

}

