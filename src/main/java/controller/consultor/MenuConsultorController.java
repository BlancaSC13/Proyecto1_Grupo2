package controller.consultor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import service.GestionaRutas;
import ucr.proyecto.HelloApplication;

import java.io.*;

public class MenuConsultorController
{
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private AnchorPane ap;

    private GestionaRutas gestionaRutas = new GestionaRutas();
    @FXML
    private ImageView imageView;
    @javafx.fxml.FXML
    public void initialize() {
        setImage();
    }
    public void setImage(){
        try {
            String ruta = gestionaRutas.getRuta();
            File img = new File(ruta);
            InputStream isImage = (FileInputStream) new FileInputStream(img);
            imageView.setImage(new Image(isImage));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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