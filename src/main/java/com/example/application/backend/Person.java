package com.example.application.backend;

public class Person {

    private String title;
    private String description;
    private String filteringWord;

    public Person() {
    }

    public Person(String title, String description, String filteringWord) {
        this.title = title;
        this.description = description;
        this.filteringWord = filteringWord;
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
