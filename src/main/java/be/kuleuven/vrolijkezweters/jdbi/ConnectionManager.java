package be.kuleuven.vrolijkezweters.jdbi;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class ConnectionManager {
    public static final String ConnectionString = "jdbc:sqlite:databaseJonasRuben.db";
    public static Handle handle;

    public static void connectDatabase() {
        Jdbi jdbi = Jdbi.create(ConnectionString);
        handle = jdbi.open();
        System.out.println("Connected to database");
    }

}
