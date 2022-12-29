package be.kuleuven.vrolijkezweters.jdbc;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

public class ConnectionManager {
    public static Handle handle;
    public static final String ConnectionString = "jdbc:sqlite:databaseJonasRuben.db";

    public static void connectDatabase(){
        Jdbi jdbi = Jdbi.create("jdbc:sqlite:databaseJonasRuben.db");
        handle = jdbi.open();
        System.out.println("Connected to database");
    }

    public static void initTables(Connection connection) throws Exception {
        Path path = new File(ConnectionManager.class.getResource("/dbcreate.sql").getFile()).toPath();
        var sql = new String(Files.readAllBytes(path));
        System.out.println(sql);

        var s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }


}
