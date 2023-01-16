package be.kuleuven.vrolijkezweters.model;

public class Categorie {
    String categorie;

    public Categorie() {

    }

    public Categorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "categorie'" + categorie +
                '}';
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
