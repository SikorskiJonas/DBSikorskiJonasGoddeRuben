package be.kuleuven.vrolijkezweters.jdbi;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class ConnectionManager {
    public static Handle handle;
    public static final String ConnectionString = "jdbc:sqlite:databaseJonasRuben.db";

    public static void connectDatabase(){
        Jdbi jdbi = Jdbi.create(ConnectionString);
        handle = jdbi.open();
        System.out.println("Connected to database");
    }

    /**
     * public Query getData(String objectName) throws ClassNotFoundException {
     *         System.out.println("fetching list of " + objectName);
     *         return ConnectionManager.handle.createQuery("SELECT * FROM" + objectName);
     *                 .mapToBean(c.class)
     *                 .list();
     *     }
     */

}
