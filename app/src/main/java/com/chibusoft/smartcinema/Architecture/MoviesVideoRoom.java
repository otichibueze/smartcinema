package com.chibusoft.smartcinema.Architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies_videos")
public class MoviesVideoRoom {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mkey; //this is movie key
    private String key;
    private String type;

    @Ignore
    public MoviesVideoRoom(int mkey,String key, String type) {
        this.mkey = mkey;
        this.id = id;
        this.key = key;
        this.type = type;
    }

    public MoviesVideoRoom(int id,int mkey, String key, String type) {
        this.id = id;
        this.mkey = mkey;
        this.key = key;
        this.type = type;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
