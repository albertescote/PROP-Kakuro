package Classes;

public class Coordenada {

    private Integer first;

    private Integer second;

    public Coordenada() {

    }

    public Coordenada(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public void clone(Coordenada c) {
        this.first = c.getFirst();
        this.second = c.getSecond();
    }

    public boolean equals(Coordenada c) {
        return this.first.equals(c.getFirst()) && this.second.equals(c.getSecond());
    }
}
