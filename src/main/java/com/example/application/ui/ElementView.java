package com.example.application.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "element", layout = MainLayout.class)
public class ElementView extends VerticalLayout {

    public ElementView() {

        Text text = new Text("Some data about job");


        Button button = new Button();
        button.setText("Back");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate("listElem"));
        });

        add(
                text,
                button

        );
    }
}
