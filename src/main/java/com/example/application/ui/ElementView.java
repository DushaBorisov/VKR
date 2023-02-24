package com.example.application.ui;

import com.example.application.backend.entities.Job;
import com.example.application.backend.service.JobService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "element", layout = MainLayout.class)
public class ElementView extends VerticalLayout implements HasUrlParameter<Integer> {


    private Text title;
    private Text description;

    private JobService jobService;

    private Integer jobId;

    @Autowired
    public ElementView(JobService jobService) {
        this.jobService = jobService;


        title = new Text("Title");
        description = new Text("description");
        VerticalLayout comp = new VerticalLayout();
        comp.setSpacing(true);
        comp.add(title, description);

        Button button = new Button();
        button.setText("Назад");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate(ListOfElements.class));
        });

        add(
               title,
                description,
                button
        );
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer s) {
        this.jobId = s;
        createView(s);
    }

    private void createView(Integer jobId) {
        Optional<Job> jobOp = jobService.getAllJobs().stream()
                .filter(el -> el.getId().equals(jobId))
                .findFirst();

        if (jobOp.isEmpty()) {
            title.setText("Вакансия не найдена. Ошибка!");
            return;
        }

//        Job job = jobOp.get();
//
//        title = new Text(job.getTitle());
//        description = new Text(job.getDescription());
//        VerticalLayout comp = new VerticalLayout();
//        comp.setSpacing(true);
//        comp.add(title, description);
//
//        Button button = new Button();
//        button.setText("Назад");
//        button.addClickListener(clickEvent ->
//        {
//            button.getUI().ifPresent(ui ->
//                    ui.navigate(ListOfElements.class));
//        });
//
//        add(
//               title,
//                description,
//                button
//        );
    }
}
