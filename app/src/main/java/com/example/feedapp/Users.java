package com.example.feedapp;

public class Users
{
    public String Name;
    public String image;
    public String status;

    public Users() {
    }

    public Users(String name, String image, String status) {
        this.Name = name;
        this.image = image;
        this.status = status;
    }

    public String getName() {
        return Name;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
