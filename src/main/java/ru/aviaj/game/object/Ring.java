package ru.aviaj.game.object;

import ru.aviaj.game.basetype.Dot;
import ru.aviaj.game.basetype.Vector;

@SuppressWarnings("unused")
public class Ring {

    private Dot center;
    private Vector normal;
    private long radius;

    public Ring(Dot center, Vector normal, long radius) {
        this.center = center;
        this.normal = normal;
        this.radius = radius;
    }

    public Ring(long xC, long yC, long zC, long xD, long yD, long zD, long radius) {
        this.center = new Dot(xC, yC, zC);
        this.normal = new Vector(xD, yD, zD);
        this.radius = radius;
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
}
