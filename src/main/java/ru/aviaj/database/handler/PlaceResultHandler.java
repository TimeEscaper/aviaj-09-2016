package ru.aviaj.database.handler;

import ru.aviaj.model.Place;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceResultHandler implements IResultHandler<Place> {
    @Override
    public Place handle(ResultSet resultSet) throws SQLException {
        if (resultSet.next())
            return new Place(resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getDouble("longitude"),
                    resultSet.getDouble("latitude"));
        return null;
    }
}
