package com.example.lenovo.myknow.Bean;

public class LongRemark {
    private String LongText;
    private String LongReviewer;
    private String LongReviewImage;
    private String LikeNum;
    public LongRemark(){}
    public String getLongText(){return  LongText;}

    public void setLongText(String longText){
        this.LongText = longText;
    }

    public String getLongReviewer() {
        return LongReviewer;
    }

    public void setLongReviewer(String longReviewer) { this.LongReviewer = longReviewer; }

    public String getLongReviewImage(){return LongReviewImage;}

    public void setLongReviewImage(String longReviewImage) {this.LongReviewImage = longReviewImage; }

    public String getLikeNum(){return LikeNum;}

    public void setLikeNum(String likeNum) {this.LikeNum = likeNum; }
}
