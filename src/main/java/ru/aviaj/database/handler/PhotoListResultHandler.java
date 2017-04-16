package ru.aviaj.database.handler;

import ru.aviaj.model.Photo;
import ru.aviaj.model.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoListResultHandler implements IResultHandler<List<Photo>> {

    @Override
    public List<Photo> handle(ResultSet resultSet) throws SQLException {
        final List<Photo> bufferPhotoList = new ArrayList<>();
        while (resultSet.next()) {
            bufferPhotoList.add(new Photo(resultSet.getLong("placeId"),
                    resultSet.getString("url")));
        }

        return bufferPhotoList;
    }
}
