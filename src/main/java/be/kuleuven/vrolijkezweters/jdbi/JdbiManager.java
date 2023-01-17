package be.kuleuven.vrolijkezweters.jdbi;

import org.jdbi.v3.core.Jdbi;

public class JdbiManager {
    private static Jdbi jdbi;

    private JdbiManager() {
        // private constructor to prevent instantiation
    }

    public static void init(String connectionString) {
        jdbi = Jdbi.create(connectionString);
    }

    public static Jdbi getJdbi() {
        if (jdbi == null) {
            throw new IllegalStateException("JdbiManager not initialized");
        }
        return jdbi;
    }
}
