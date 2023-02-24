package com.example.application.backend.service;

import com.example.application.backend.entities.Job;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private List<Job> jobsList = new ArrayList<>();

    public List<Job> sortByName(String name) {
        return jobsList.stream()
                .filter(el -> el.getFilteringWord().equals(name))
                .collect(Collectors.toList());
    }

    public JobService() {
        jobsList = new ArrayList<>();
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 1));
        jobsList.add(new Job("C# developer", "C# middle developer in project", "C#", 2));
        jobsList.add(new Job("Java script developer", "Java script junior developer in project", "JS", 3));
        jobsList.add(new Job("Python developer", "Python junior developer in project", "Python", 4));
        jobsList.add(new Job("GO developer", "GO junior developer in project", "GO", 5));
        jobsList.add(new Job("Perl developer", "Perl junior developer in project", "Perl", 6));
        jobsList.add(new Job("Java developer", "Java middle developer in project", "Java", 7));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 8));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 9));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 10));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 11));
        jobsList.add(new Job("C# developer", "C# middle developer in project", "C#", 12));
        jobsList.add(new Job("Java script developer", "Java script junior developer in project", "JS", 13));
        jobsList.add(new Job("Python developer", "Python junior developer in project", "Python", 14));
        jobsList.add(new Job("GO developer", "GO junior developer in project", "GO", 15));
        jobsList.add(new Job("Perl developer", "Perl junior developer in project", "Perl", 16));
        jobsList.add(new Job("Java developer", "Java middle developer in project", "Java", 17));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 18));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 19));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 20));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 21));
        jobsList.add(new Job("C# developer", "C# middle developer in project", "C#", 22));
        jobsList.add(new Job("Java script developer", "Java script junior developer in project", "JS", 23));
        jobsList.add(new Job("Python developer", "Python junior developer in project", "Python", 24));
        jobsList.add(new Job("GO developer", "GO junior developer in project", "GO", 25));
        jobsList.add(new Job("Perl developer", "Perl junior developer in project", "Perl", 26));
        jobsList.add(new Job("Java developer", "Java middle developer in project", "Java", 27));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 28));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 29));
        jobsList.add(new Job("Java developer", "Java junior developer in project", "Java", 30));
    }

    public List<Job> getAllJobs() {
        return jobsList;
    }
}
