package com.example.application.backend.service;

import com.example.application.backend.Job;
import com.example.application.backend.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private List<Person> jobsList = new ArrayList<>();

    public List<Person> sortByName(String name) {
        return jobsList.stream()
                .filter(el -> el.getFilteringWord().equals(name))
                .collect(Collectors.toList());
    }

    public JobService() {
        jobsList = new ArrayList<>();
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("C# developer", "C# middle developer in project", "C#"));
        jobsList.add(new Person("Java script developer", "Java script junior developer in project", "JS"));
        jobsList.add(new Person("Python developer", "Python junior developer in project", "Python"));
        jobsList.add(new Person("GO developer", "GO junior developer in project", "GO"));
        jobsList.add(new Person("Perl developer", "Perl junior developer in project", "Perl"));
        jobsList.add(new Person("Java developer", "Java middle developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("C# developer", "C# middle developer in project", "C#"));
        jobsList.add(new Person("Java script developer", "Java script junior developer in project", "JS"));
        jobsList.add(new Person("Python developer", "Python junior developer in project", "Python"));
        jobsList.add(new Person("GO developer", "GO junior developer in project", "GO"));
        jobsList.add(new Person("Perl developer", "Perl junior developer in project", "Perl"));
        jobsList.add(new Person("Java developer", "Java middle developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("C# developer", "C# middle developer in project", "C#"));
        jobsList.add(new Person("Java script developer", "Java script junior developer in project", "JS"));
        jobsList.add(new Person("Python developer", "Python junior developer in project", "Python"));
        jobsList.add(new Person("GO developer", "GO junior developer in project", "GO"));
        jobsList.add(new Person("Perl developer", "Perl junior developer in project", "Perl"));
        jobsList.add(new Person("Java developer", "Java middle developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
        jobsList.add(new Person("Java developer", "Java junior developer in project", "Java"));
    }

    public List<Person> getAllJobs() {
        return jobsList;
    }
}
