package com.example.iduma.tree_tracking.Model;

public class FireModel {
    private String uid, reporter, coordinate,fireCause, forestImage;

    public FireModel() {
    }

    public FireModel(String uid, String reporter, String coordinate, String fireCause, String forestImage) {
        this.uid = uid;
        this.reporter = reporter;
        this.coordinate = coordinate;
        this.fireCause = fireCause;
        this.forestImage = forestImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getFireCause() {
        return fireCause;
    }

    public void setFireCause(String fireCause) {
        this.fireCause = fireCause;
    }

    public String getForestImage() {
        return forestImage;
    }

    public void setForestImage(String forestImage) {
        this.forestImage = forestImage;
    }


}
