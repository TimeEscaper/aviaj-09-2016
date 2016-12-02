package ru.aviaj.game.baseobject;

import ru.aviaj.game.basetype.Dot;
import ru.aviaj.game.basetype.Vector;

@SuppressWarnings("unused")
public class Ring {

    private Dot center;
    private Vector normal;
    private long radius;
    private long ratingValue;

    public Ring(Dot center, Vector normal, long radius, long ratingValue) {
        this.center = center;
        this.normal = normal;
        this.radius = radius;
        this.ratingValue = ratingValue;
    }

    public Ring(long xC, long yC, long zC, long xD, long yD, long zD, long radius, long ratingValue) {
        this.center = new Dot(xC, yC, zC);
        this.normal = new Vector(xD, yD, zD);
        this.radius = radius;
        this.ratingValue = ratingValue;
    }

    public Dot getCenter() {
        return center;
    }

    public Vector getNormal() {
        return normal;
    }

    public void setCenter(Dot center) {
        this.center = center;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

    public long getRadius() {
        return radius;
    }

    public void setRatingValue(long ratingValue) {
        this.ratingValue = ratingValue;
    }

    public long getRatingValue() {
        return ratingValue;
    }
}
