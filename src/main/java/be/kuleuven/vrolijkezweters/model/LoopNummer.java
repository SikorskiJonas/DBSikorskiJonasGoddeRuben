package be.kuleuven.vrolijkezweters.model;

public class LoopNummer {
    private int nummer;
    private int looptijd;
    private int loperId;
    private int etappeId;

    public LoopNummer() {

    }

    public LoopNummer(int nummer, int looptijd, int loperId, int etappeId) {
        this.nummer = nummer;
        this.looptijd = looptijd;
        this.loperId = loperId;
        this.etappeId = etappeId;

    }

    @Override
    public String toString() {
        return "LoopNummer{" + "nummer=" + nummer + ", looptijd=" + looptijd + ", loperId=" + loperId + ", etappeId=" + etappeId + '}';
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public int getLoopTijd() {
        return looptijd;
    }

    public void setLoopTijd(int looptijd) {
        this.looptijd = looptijd;
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
