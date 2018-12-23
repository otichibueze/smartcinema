package com.chibusoft.smartcinema.Architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies_reviews")
public class MovieReviewsRoom {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mkey; //this is movie key
    private String author;
    private String content;

    @Ignore
    public MovieReviewsRoom(int mkey, String author, String content) {
        this.id = id;
        this.mkey = mkey;
        this.author = author;
        this.content = content;
    }

    public MovieReviewsRoom(int id, int mkey, String author, String content) {
        this.id = id;
        this.mkey = mkey;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMkey() {
        return mkey;
    }

    public void setMkey(int mkey) {
        this.mkey = mkey;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
