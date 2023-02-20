package com.example.application.backend.service;

import com.example.application.backend.Contact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private List<Contact> contacts = generateContacts();

    public List<Contact> generateContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Andrey", "Borisov", 20));
        contacts.add(new Contact("Danil", "Semenov", 23));
        contacts.add(new Contact("Anna", "Volgina", 24));
        contacts.add(new Contact("Dan", "Petrovv", 40));
        contacts.add(new Contact("Max", "Dudnik", 21));
        contacts.add(new Contact("Ksenia", "Borisova", 29));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));
        contacts.add(new Contact("Artem", "Smirnov", 28));


        return contacts;
    }

    public List<Contact> getAllContactsByName(String name) {
        return contacts.stream()
                .filter(contact -> contact.getName().equals(name))
                .collect(Collectors.toList());
    }

    public long countContact() {
        return contacts.size();
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    public void saveContact(Contact contact) {
        contacts.add(contact);
    }
}
