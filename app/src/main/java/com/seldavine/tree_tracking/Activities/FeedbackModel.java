package com.seldavine.tree_tracking.Activities;

public class FeedbackModel {
    private String message, sender,country,date;

    public FeedbackModel() {
    }

    public FeedbackModel(String message, String sender, String country, String date) {
        this.message = message;
        this.sender = sender;
        this.country = country;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
