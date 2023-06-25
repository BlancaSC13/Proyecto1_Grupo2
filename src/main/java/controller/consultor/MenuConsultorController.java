package controller.consultor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ucr.proyecto.HelloApplication;

import java.io.IOException;

public class MenuConsultorController
{
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private AnchorPane ap;

    @javafx.fxml.FXML
    public void initialize() {
    }
    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnHomeOnAction(ActionEvent event) {
        this.bp.setCenter(ap);
    }

    @FXML
    void btnReportesOnAction(ActionEvent event) {
        loadPage("generarReportesConsultor.fxml");
    }

    @FXML
    void btnSalirOnAction(ActionEvent event) {
        System.exit(0);
    }
    }