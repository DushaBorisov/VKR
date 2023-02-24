package com.example.application.backend.entities;

public class Job {

    private Integer id;
    private String title;
    private String description;
    private String filteringWord;

    public Job(String title, String description, String filteringWord, Integer id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filteringWord = filteringWord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilteringWord() {
        return filteringWord;
    }

    public void setFilteringWord(String filteringWord) {
        this.filteringWord = filteringWord;
    }
}
