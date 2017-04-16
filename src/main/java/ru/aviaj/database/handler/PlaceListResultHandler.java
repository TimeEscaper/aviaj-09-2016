package ru.aviaj.database.handler;

import ru.aviaj.model.Place;
import ru.aviaj.model.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceListResultHandler implements IResultHandler<List<Place>> {
    @Override
    public List<Place> handle(ResultSet resultSet) throws SQLException {
        final List<Place> bufferPlaceList = new ArrayList<>();
        while (resultSet.next()) {
            bufferPlaceList.add(new Place(resultSet.getLong("id"),
                    resultSet.getString("title"),
                    null,
                    resultSet.getDouble("longitude"),
                    resultSet.getDouble("latitude")));
        }

        return bufferPlaceList;
    }
}
