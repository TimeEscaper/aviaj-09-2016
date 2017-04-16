package ru.aviaj.database.dao;

import ru.aviaj.database.executor.Executor;
import ru.aviaj.database.handler.PhotoListResultHandler;
import ru.aviaj.database.handler.PlaceListResultHandler;
import ru.aviaj.database.handler.PlaceResultHandler;
import ru.aviaj.model.Photo;
import ru.aviaj.model.Place;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {
    private Connection dbConnection;

    private static final double R = 6371; //Earth's radius in km

    public PlaceDAO(Connection connection) {
        this.dbConnection = connection;
    }

    private double degreeToRad(double deg) {
        return deg*(Math.PI/180);
    }

    private double measureDistance(double lon1, double lat1, double lon2, double lat2) {
        double deltaLat = degreeToRad(lat2 - lat1);
        double deltaLon = degreeToRad(lon2 - lon1);
        double a =
                Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                        Math.cos(degreeToRad(lat1)) * Math.cos(degreeToRad(lat2)) *
                                Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
    }

    public Place getPlaceById(long id) throws SQLException {
        final Executor executor = new Executor();
        String query = "SELECT * FROM Place WHERE id = " + String.valueOf(id) + ';';
        Place place = executor.execQuery(dbConnection, query, new PlaceResultHandler());
        query = "SELECT * FROM Photo WHERE placeId = " + String.valueOf(id) + ';';
        List<Photo> photos = executor.execQuery(dbConnection, query, new PhotoListResultHandler());
        List<String> photoList = place.getPhotos();
        for (Photo photo : photos) {
            photoList.add(photo.getUrl());
        }
        return place;
    }

    public List<Place> getNearestPlaces(double longitude, double latitude) throws SQLException {
        final Executor executor = new Executor();
        final String query = "SELECT id, title, longitude, latitude FROM Place;";
        List<Place> allPlaces = executor.execQuery(dbConnection, query, new PlaceListResultHandler());
        List<Place> result = new ArrayList<>();
        for (Place place : allPlaces) {
            if (measureDistance(longitude, latitude, place.getLongitude(), place.getLatitude()) <= 3) {
                result.add(place);
            }
        }
        return result;
    }
}
