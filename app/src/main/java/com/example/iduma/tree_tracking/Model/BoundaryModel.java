package com.example.iduma.tree_tracking.Model;

public class BoundaryModel {

    private String uid, dispute_reporter, disputer_name1,disputer_name2;
    private String dispute_location, disputer_email1, disputer_email2, tree_type, trees_no;
    private String witness_name, witness_email, image1,image2,image3;
    private double latitude, longitude;

    public BoundaryModel(String uid, String dispute_reporter, double latitude, double longitude,
                         String disputer_name1, String disputer_name2, String dispute_location,
                         String disputer_email1, String disputer_email2, String tree_type,
                         String trees_no, String witness_name, String witness_email, String image1,
                         String image2, String image3) {
        this.uid = uid;
        this.dispute_reporter = dispute_reporter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.disputer_name1 = disputer_name1;
        this.disputer_name2 = disputer_name2;
        this.dispute_location = dispute_location;
        this.disputer_email1 = disputer_email1;
        this.disputer_email2 = disputer_email2;
        this.tree_type = tree_type;
        this.trees_no = trees_no;
        this.witness_name = witness_name;
        this.witness_email = witness_email;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDispute_reporter() {
        return dispute_reporter;
    }

    public void setDispute_reporter(String dispute_reporter) {
        this.dispute_reporter = dispute_reporter;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDisputer_name1() {
        return disputer_name1;
    }

    public void setDisputer_name1(String disputer_name1) {
        this.disputer_name1 = disputer_name1;
    }

    public String getDisputer_name2() {
        return disputer_name2;
    }

    public void setDisputer_name2(String disputer_name2) {
        this.disputer_name2 = disputer_name2;
    }

    public String getDispute_location() {
        return dispute_location;
    }

    public void setDispute_location(String dispute_location) {
        this.dispute_location = dispute_location;
    }

    public String getDisputer_email1() {
        return disputer_email1;
    }

    public void setDisputer_email1(String disputer_email1) {
        this.disputer_email1 = disputer_email1;
    }

    public String getDisputer_email2() {
        return disputer_email2;
    }

    public void setDisputer_email2(String disputer_email2) {
        this.disputer_email2 = disputer_email2;
    }

    public String getTree_type() {
        return tree_type;
    }

    public void setTree_type(String tree_type) {
        this.tree_type = tree_type;
    }

    public String getTrees_no() {
        return trees_no;
    }

    public void setTrees_no(String trees_no) {
        this.trees_no = trees_no;
    }

    public String getWitness_name() {
        return witness_name;
    }

    public void setWitness_name(String witness_name) {
        this.witness_name = witness_name;
    }

    public String getWitness_email() {
        return witness_email;
    }

    public void setWitness_email(String witness_email) {
        this.witness_email = witness_email;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
