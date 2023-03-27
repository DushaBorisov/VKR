package com.example.application.ui.student;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.JobService;
import com.example.application.backend.service.StudentService;
import com.example.application.ui.ElementView;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@RequiredArgsConstructor
@Route(value = "students", layout = MainLayout.class)
public class ListOfStudents extends Div implements AfterNavigationObserver {

    private StudentService studentService;
    private H2 title;

    Grid<Student> grid = new Grid<>();
    TextField filterText = new TextField();

    MultiSelectComboBox<EmploymentEnum> comboBox = new MultiSelectComboBox<>(
            "Занятость");

    @Autowired
    public ListOfStudents(StudentService studentService) {
        this.studentService = studentService;

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(student -> createCard(student));
        add(getToolbar(), grid);
    }

    private HorizontalLayout createCard(Student student) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(student.getDesiredPosition());
        name.addClassName("name");
        header.add(name);

        StringBuilder bodyBuilder = new StringBuilder();

        bodyBuilder.append("Опыт : " + student.getExperience())
                .append("\n")
                .append("Заработная плата : " + student.getDesiredSalary() + "₽")
                .append("\n")
                .append("Занятость: " + student.getDesiredEmployment())
                .append("\n")
                .append("Курс обучения: " + student.getCourseOfStudy());

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button button = new Button("Подробнее");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate(StudentInfoPage.class, student.getStudentId()));
        });

        description.add(header, salary, button);


        card.add(description);

        return card;
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Поск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setLabel("Ключевое слово");

        //filterText.addValueChangeListener(e -> updateList());

        Button searchButton = new Button("Поиск");
        searchButton.addClickListener(
                clickEvent->{
                    updateList();
                }
        );
        comboBox.setItems(EmploymentEnum.values());
        comboBox.setItemLabelGenerator(EmploymentEnum::getEmploymentType);

        title = new H2("Список студентов");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.LARGE);

        HorizontalLayout searchAndFiltersContainer = new HorizontalLayout(filterText, comboBox, searchButton);
        searchAndFiltersContainer.setAlignItems(FlexComponent.Alignment.END);
        searchAndFiltersContainer.setPadding(true);
        searchAndFiltersContainer.setWidthFull();

        searchAndFiltersContainer.addClassName("toolbar");
        return searchAndFiltersContainer;
    }

    private void updateList() {
        grid.setItems(studentService.findByKeyWordsWithFilters(filterText.getValue(), comboBox.getSelectedItems()));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<Student> studentList = studentService.getAllStudents();
        grid.setItems(studentList);
    }

}
