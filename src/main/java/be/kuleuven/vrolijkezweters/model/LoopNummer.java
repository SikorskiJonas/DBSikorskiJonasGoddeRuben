package be.kuleuven.vrolijkezweters.model;

public class LoopNummer {
    private int nummer;
    private int loopTijd;
    private int loperId;
    private int etappeId;

    public LoopNummer(){

    }

    @Override
    public String toString() {
        return "LoopNummer{" +
                "nummer='" + nummer +
                ", loopTijd='" + loopTijd +
                ", loperId='" + loperId +
                ", etappeId='" + etappeId +
                '}';
    }

    public LoopNummer(int nummer, int loopTijd, int loperId, int etappeId) {
        this.nummer = nummer;
        this.loopTijd = loopTijd;
        this.loperId = loperId;
        this.etappeId = etappeId;

    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public int getLoopTijd() {
        return loopTijd;
    }

    public void setLoopTijd(int loopTijd) {
        this.loopTijd = loopTijd;
    }

    public int getLoperId() {
        return loperId;
    }

    public void setLoperId(int loperId) {
        this.loperId = loperId;
    }

    public int getEtappeId() {
        return etappeId;
    }

    public void setEtappeId(int etappeId) {
        this.etappeId = etappeId;
    }
}
