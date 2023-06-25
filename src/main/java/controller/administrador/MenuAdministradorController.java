package controller.administrador;


import controller.administrador.ventanasEmergentes.SettingsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.GestionaRutas;
import ucr.proyecto.HelloApplication;

import java.io.*;

public class MenuAdministradorController
{
    @FXML
    private AnchorPane ap;
    @FXML
    private BorderPane bp;
    public Stage stage;
    SettingsController settingsController;
    GestionaRutas gestionaRutas = new GestionaRutas();
    @FXML
    private ImageView imageView;
    @FXML
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

    @FXML
    void btnHomeOnAction(ActionEvent event) {
        this.bp.setCenter(ap);
    }

    @FXML
    void btnInventarioOnAction(ActionEvent event) {
        loadPage("inventario.fxml");
    }

    @FXML
    void btnProductosOnAction(ActionEvent event) {
        loadPage("gestionProductos.fxml");
    }

    @FXML
    void btnProveedoresOnAction(ActionEvent event) {
        loadPage("administrador/gestionProveedores.fxml");
    }

    @FXML
    void btnReportesOnAction(ActionEvent event) {
        loadPage("generarReporteAdmin.fxml");
    }

    @FXML
    void btnSalirOnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void btnUsuariosOnAction(ActionEvent event) {
        loadPage("administrador/gestionUsuarios.fxml");
    }

    @FXML
    void btnDemandaOnAction(ActionEvent event) {loadPage("previsionDemanda.fxml");}

    @FXML
    void btnSeetings(MouseEvent event) {
        loadPopUp("administrador/ventanasEmergentes/settings.fxml");
        settingsController.addController(this);
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadPopUp(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.show();
            settingsController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}