package ru.aviaj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aviaj.database.exception.DbException;
import ru.aviaj.model.ErrorList;
import ru.aviaj.model.ErrorType;
import ru.aviaj.model.Place;
import ru.aviaj.model.UserProfile;
import ru.aviaj.service.PlaceService;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@RestController
public class PerfectureController {

    private final PlaceService placeService;

    @Autowired
    public PerfectureController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(path = "/api/places/id/{placeId}", method = RequestMethod.GET)
    public ResponseEntity getPlaceById(@PathVariable long placeId) {

        if (placeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorList(ErrorType.WRONGUSERID)
            );
        }

        try {
            final Place place = placeService.getPlaceById(placeId);
            if (place == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorList(ErrorType.NOUSERID)
                );
            }

            return ResponseEntity.ok(place);
        } catch (DbException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }
    }

    @RequestMapping(path = "/api/places/")
    public ResponseEntity getNearestPlaces(@RequestParam(value = "latitude") double latitude,
                                           @RequestParam(value = "longitude") double longitude) {
        final List<Place> placeList;
        try {
            placeList = placeService.getNearestPlaces(longitude, latitude);
        } catch (DbException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        return ResponseEntity.ok(new PlaceListResponse(placeList));
    }

    private class PlaceIdRequest {
        private long id;

        public PlaceIdRequest() { }

        public PlaceIdRequest(long id) {
            this.id = id;
        }

        public long getId() { return id; }
    }

    private class PlaceListResponse {
        private List<Place> places = new ArrayList<>();

        public PlaceListResponse(List<Place> places) {
            this.places = places;
        }
    }
}
