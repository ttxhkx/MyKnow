package com.example.lenovo.myknow.Bean;

public class ShortRemark {
    private String ShortText;
    private String ShortReviewer;
    private String ShortReviewImage;
    private String SlikeNum;
    public ShortRemark(){}

    public String getShortText(){return ShortText;}

    public void setShortText(String shortText){
        this.ShortText = shortText;
    }

    public String getShortReviewer() {
        return ShortReviewer;
    }

    public void setShortReviewer(String shortReviewer) { this.ShortReviewer = shortReviewer; }

    public String getShortReviewImage(){return ShortReviewImage;}

    public void setShortReviewImage(String shortReviewImage) {this.ShortReviewImage = shortReviewImage; }

    public String getSlikeNum(){return SlikeNum;}

    public void setSlikeNum(String slikeNum) {this.SlikeNum = slikeNum; }
}
