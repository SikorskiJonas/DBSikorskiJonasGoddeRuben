package be.kuleuven.vrolijkezweters.jdbi;

import be.kuleuven.vrolijkezweters.model.Functie;
import be.kuleuven.vrolijkezweters.model.LoopNummer;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LoopNummerDao {

    private final Jdbi jdbi;

    public LoopNummerDao() {
        this.jdbi = JdbiManager.getJdbi();
    }

    public List<LoopNummer> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM LoopNummer")
                .mapToBean(LoopNummer.class)
                .list());
    }

    public List<LoopNummer> getAllSorted() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM LoopNummer ORDER BY nummer DESC")
                .mapToBean(LoopNummer.class)
                .list());
    }

    public void insert(LoopNummer loopNummer) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO LoopNummer (nummer, looptijd, loperId, etappeId) VALUES (:nummer, :looptijd, :loperId, :etappeId)")
                .bindBean(loopNummer)
                .execute());
    }

    public void update(LoopNummer loopNummerNew, LoopNummer loopNummerOld) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE LoopNummer SET (nummer, looptijd, loperId, etappeId) = (:nummer, :looptijd, :loperId, :etappeId) WHERE loperId= :loperIdOld AND etappeId = :etappeIdOld")
                .bind("loperIdOld", loopNummerOld.getLoperId())
                .bind("etappeIdOld", loopNummerNew.getEtappeId())
                .execute());
    }

    public void delete(LoopNummer loopNummer) {
        jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM LoopNummer WHERE loperId = :loperId AND etappeId = :etappeId")
                .bind("loperIdOld", loopNummer.getLoperId())
                .bind("etappeIdOld", loopNummer.getEtappeId())
                .execute());
    }

}