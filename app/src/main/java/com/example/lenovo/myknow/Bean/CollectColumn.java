package com.example.lenovo.myknow.Bean;

import android.support.v7.widget.RecyclerView;

public class CollectColumn {
    private String Image;
    private String Id;
    private String Title;
    public CollectColumn(){}
    public String getTitle(){return  Title;}

    public void setTitle(String title){
        this.Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getID(){return Id;}

    public void setID(String id) {
        this.Id = id;
    }
}
