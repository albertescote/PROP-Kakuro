package Classes;

public class Usuari {

    private final String name;
    private String pwd;

    public Usuari(String usr, String pwd) {
        this.name = usr;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
