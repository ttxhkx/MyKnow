package com.example.lenovo.myknow.Bean;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class theColumn {
    private String Title;
    private String image;
    private String ID;
    private String description;
    public theColumn() {

    }
    public String getTitle() {return Title; }

    public void setTitle(String Title) {this.Title = Title; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setID(String ID){this.ID = ID;}

    public String getID() { return ID;}

    public void setDescription(String description){this.description = description;}

    public String getDescription() { return description;}
}
