package com.chibusoft.smartcinema.Models;


import java.util.List;

/**
 * Created by EBELE PC on 4/17/2018.
 */


public class MovieReviews {

    public int id;
    public int page;
    public int total_pages;
    public int total_results;
    public List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public class Results {

     public String author;

     public String content;


     public String getAuthor() {
         return author;
     }

     public String getContent() {
         return content;
     }
 }


}
