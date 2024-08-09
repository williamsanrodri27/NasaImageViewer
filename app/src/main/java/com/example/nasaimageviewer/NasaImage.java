package com.example.nasaimageviewer;

public class NasaImage {
    private String date;
    private String url;
    private String hdurl;
    private String title; // Add this field
    private String section; // Add this field

    // Existing constructor
    public NasaImage(String date, String url, String hdurl) {
        this.date = date;
        this.url = url;
        this.hdurl = hdurl;
    }

    // New constructor that includes title and section
    public NasaImage(String date, String url, String hdurl, String title, String section) {
        this.date = date;
        this.url = url;
        this.hdurl = hdurl;
        this.title = title;
        this.section = section;
    }

    // Existing getters and setters
    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getHdurl() {
        return hdurl;
    }

    // New getters for title and section
    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    // Existing setters if needed
    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    // New setters for title and section if needed
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
