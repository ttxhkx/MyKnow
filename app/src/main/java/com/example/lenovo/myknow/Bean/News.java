package com.example.lenovo.myknow.Bean;

public class News {
    private String NewsTitle;
    private String NewsID;
    private String NewsImage;
    public News(){}
    public String getNewsTitle(){return  NewsTitle;}

    public void setNewsTitle(String newsTitle){
        this.NewsTitle = newsTitle;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        this.NewsImage = newsImage;
    }

    public String getNewsID(){return NewsID;}

    public void setNewsID(String newsID) {
        this.NewsID = newsID;
    }
}
