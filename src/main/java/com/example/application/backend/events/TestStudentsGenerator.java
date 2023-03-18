package com.example.application.backend.events;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.StudentService;
import com.example.application.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestStudentsGenerator {

    private final UserService userService;
    private final StudentService studentService;

    public void generateTestStudents(){
        User andrey = createTestUser("andrey", "password");

        Student student = Student.builder()
                .user(andrey)
                .name("Андрей")
                .surname("Борисов")
                .courseOfStudy("4")
                .desiredPosition("Middle Java разработчик")
                .desiredSalary(170000)
                .experience(3)
                .desiredEmployment("Полная")
                .resume("Есть опыт коммерческой разработки около 2-х лет.\n" +
                        "\n" +
                        "За это время поучаствовал в разработки следующих проектов:\n" +
                        "- Финтех стартап инвестиций в ценные бумаги;\n" +
                        "- Несколько проектов Telecom компании;\n" +
                        "- Система документооборота банка;\n" +
                        "- Система сбора метрик и котировок;\n" +
                        "\n" +
                        "Навыки:\n" +
                        "- Java Core;\n" +
                        "- Spring(Boot, Web, Data, Test);\n" +
                        "- Работал с реляционными базами(PostgreSql, MySql);\n" +
                        "- Работал с NoSql решениями(DynamoDb, MongoDb);\n" +
                        "- Docker;\n" +
                        "- Kubernetes;\n" +
                        "- Работал с AWS(DynamoDb, EKS, SQS);\n" +
                        "- Покрываю свой код тестами(Junit, Mockito, Harmcrest, Testcontainers);\n" +
                        "- Работал c Elasticsearch;")
                .email("borisovandrey.work@gmail.com")
                .phone("89636333811")
                .build();
        studentService.addNewStudent(student);

        User anton = createTestUser("anton", "password");
        Student student2 = Student.builder()
                .user(anton)
                .name("Антон")
                .surname("Федоров")
                .courseOfStudy("3")
                .desiredPosition("Junior Puthon разработчик")
                .desiredSalary(100000)
                .experience(1)
                .desiredEmployment("Стажировка")
                .resume("Коммерческие проекты:\n" +
                        "Разработка фреймворка и компилятора для выполнения инференса нейронных сетей на тензорном процессоре IVA TPU.\n" +
                        "Основные задачи в команде компилятора:\n" +
                        "Разработка внутреннего представления нейронной сети, алгоритмы оптимизации и препроцессинга для выполнения на аппаратном ускорителе. (Python, Keras, TensorFlow, ONNX, NumPy)." +
                        " Покрытие тестами, CI/CD (Pytest, Allure, GiTlab).")
                .phone("89636333811")
                .build();
        studentService.addNewStudent(student2);

        User danil = createTestUser("danil", "password");
        Student student3 = Student.builder()
                .user(danil)
                .name("Данил")
                .surname("Измайлов")
                .courseOfStudy("3")
                .desiredPosition("Junior Go разработчик")
                .desiredSalary(120000)
                .experience(2)
                .desiredEmployment("Полная")
                .resume("Студент 2-го курса НИТУ \"МИСиС\", факультет \"Прикладная математика\".\n" +
                        "\n" +
                        "Технологии:\n" +
                        "Go (NATS)\n" +
                        "С# (ASP NET MVC, ASP NET CORE, Entity Framework)\n" +
                        "Python(Flask, FastAPI, Sqlalchemy)\n" +
                        "Js (React)\n" +
                        "html, css\n" +
                        "\n" +
                        "Базы Данных:\n" +
                        "MSSQL, T-SQL, PostgreSQL\n" +
                        "\n" +
                        "Также имею навыки использования систем контроля версий: git, svn\n" +
                        "\n" +
                        "Проекты:\n" +
                        "1. REST API Сервис на Go https://github.com/komtriangle/Bodroe_ytro_GO\n" +
                        "2. Интернет-магазин. Технологии: C#, ASP NET MVC, MSSQL\n" +
                        "3.Приложение \"Бодрое утро\" для Smart маркета СБЕРа. Технологии: Python, FastAPI, ReactJs, SQLite\n" +
                        "4.Клиент-серверное приложение по учету успеваемости студентов (курсовая работа на курсу \"Технологии программирования\"). Технологии: C++ (Qt), SQLite.")
                .phone("89636333811")
                .build();
        studentService.addNewStudent(student3);

        User max = createTestUser("max", "password");
        Student student4 = Student.builder()
                .user(max)
                .name("Максим")
                .surname("Олейников")
                .courseOfStudy("4")
                .desiredPosition("Junior Java разработчик")
                .desiredSalary(80000)
                .experience(2)
                .desiredEmployment("Полная")
                .resume("Java изучаю почти два года. Реализовывал и поддерживал мессенджер-ботов для публичного использования. Есть опыт разработки в геймдеве. Экспериментировал с изученными технологиями и теперь желаю применять полученные знания в работе.\n" +
                        "Владею следующим стеком технологий:\n" +
                        "\n" +
                        "• Java\n" +
                        "• C#\n" +
                        "\n" +
                        "• HTML, CSS\n" +
                        "• Atlassian stack: (Jira/Confluence/Service Desk/Bitbucket(git))\n" +
                        "• Database: (SQL/PostgreSQL)\n" +
                        "• XML (XSD, XPATH), JSON\n" +
                        "• Spring: (Boot)\n" +
                        "• BPMN\n" +
                        "• Postman\n" +
                        "• Maven\n" +
                        "• Docker\n" +
                        "• Apache Tomcat\n" +
                        "• Intellij IDEA\n" +
                        "• vCloud API\n" +
                        "\n" +
                        "Не прекращаю изучать что-то новое и дополнять стек. Иногда, в свободное время занимаюсь разработкой и поддержкой пет-проектов на Java.\n" +
                        "В портфолио имеются сертификаты компании CISCO, в качестве выпускной квалификационной работы была выбрана тема \"Разработка системы контроля и автоматизации электронных приборов\" в рамках которой была реализована система \"Умный дом\" в однокомнатной квартире. Работа защищена на отлично.\n" +
                        "Имею организационные навыки и опыт в управлении небольшой командой.")
                .phone("89636333811")
                .build();
        studentService.addNewStudent(student4);
    }










    private User createTestUser(String name, String password) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        userService.saveStudent(user);
        return user;
    }
}
