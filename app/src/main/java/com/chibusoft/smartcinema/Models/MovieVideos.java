package com.chibusoft.smartcinema.Models;

import java.util.List;


/**
 * Created by EBELE PC on 4/17/2018.
 */

public class MovieVideos {


    public int id;

    public List<Results> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }


    public class Results {
         String key;

         String type;

        public String getKey() {
            return key;
        }


        public String getType() {
            return type;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}