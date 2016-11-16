package ru.aviaj.database.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aviaj.database.exception.DbException;
import ru.aviaj.database.exception.DbQueryException;
import ru.aviaj.database.exception.DbResultSetException;
import ru.aviaj.database.exception.DbUpdateException;
import ru.aviaj.database.executor.Executor;

import java.sql.Connection;

public class SessionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDAO.class);

    private Connection dbConnection;

    public SessionDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public long getUserIdBySession(String session) throws DbException {
        final Executor executor = new Executor();
        try {
            final String query = "SELECT userId FROM Session WHERE session = '" + session + "';";
            return executor.execQuery(dbConnection, query, resultSet -> {
                resultSet.next();
                return resultSet.getLong("userId");
            });
        } catch (DbQueryException | DbResultSetException e) {
            LOGGER.warn(e.getMessage());
            return 0;
        }
    }

    public boolean addSession(String session, long userId) throws DbException {
        final Executor executor = new Executor();
        try {
            final String update = "INSERT INTO Session (session, userId) VALUES ('" + session +
                    "', " + Long.toString(userId) + ");";
            executor.execUpdate(dbConnection, update);

            return true;
        }
        catch (DbUpdateException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean removeSession(String session) throws DbException {
        final Executor executor = new Executor();
        try {
            final String update = "DELETE FROM Session WHERE session = '" + session + "';";
            executor.execUpdate(dbConnection, update);

            return true;
        }
        catch (DbUpdateException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }

    public boolean removeAll() throws DbException {
        final Executor executor = new Executor();
        try {
            final String update = "DELETE * FROM Session;";
            executor.execUpdate(dbConnection, update);

            return true;
        }
        catch (DbUpdateException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }
}
