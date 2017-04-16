package ru.aviaj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aviaj.database.DatabaseService;
import ru.aviaj.database.dao.PlaceDAO;
import ru.aviaj.database.dao.UserProfileDAO;
import ru.aviaj.database.exception.DbException;
import ru.aviaj.model.Place;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class PlaceService extends DatabaseService{

    @Autowired
    public PlaceService(DataSource ds) { this.ds = ds; }

    public Place getPlaceById(long id) throws DbException {
        final Connection dbConnection = getConnection();
        try {
            final PlaceDAO placeDao = new PlaceDAO(dbConnection);
            return placeDao.getPlaceById(id);
        } catch (SQLException e) {
            throw new DbException("Unable to get place!", e);
        }
    }

    public List<Place> getNearestPlaces(double longitude, double latitude) throws DbException {
        final Connection dbConnection = getConnection();
        try {
            final PlaceDAO placeDao = new PlaceDAO(dbConnection);
            return placeDao.getNearestPlaces(longitude, latitude);
        } catch (SQLException e) {
            throw new DbException("Unable to get places!", e);
        }
    }
}
