package controller.administrador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ucr.proyecto.HelloApplication;

import java.io.IOException;

public class MenuAdministradorController
{
    @FXML
    private AnchorPane ap;
    @FXML
    private BorderPane bp;

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
    void btnInventarioOnAction(ActionEvent event) {
        loadPage("administrador/inventario.fxml");
    }

    @FXML
    void btnProductosOnAction(ActionEvent event) {
        loadPage("administrador/gestionProductos.fxml");
    }

    @FXML
    void btnProveedoresOnAction(ActionEvent event) {
        loadPage("administrador/gestionProveedores.fxml");
    }

    @FXML
    void btnReportesOnAction(ActionEvent event) {
        loadPage("administrador/generarReporteAdmin.fxml");
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
    void btnDemandaOnAction(ActionEvent event) {loadPage("administrador/previsionDemanda.fxml");}
}