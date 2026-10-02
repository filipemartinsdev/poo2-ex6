package com.poo2ex6;

import atlantafx.base.theme.PrimerLight;
import com.poo2ex6.controller.ImcController;
import com.poo2ex6.persistence.ImcFileRepository;
import com.poo2ex6.service.ImcService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ImcApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(ImcApplication.class.getResource("imc.fxml"));

        fxmlLoader.setControllerFactory(type -> {
            if (type == ImcController.class){
                return new ImcController(
                        new ImcService(
                                new ImcFileRepository()
                        )
                );
            }

            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("IMC");
        stage.setScene(scene);
        stage.setWidth(720);
        stage.setHeight(480);
        stage.show();
    }
}
