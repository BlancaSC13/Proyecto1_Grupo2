package controller.administrador.ventanasEmergentes;


import controller.administrador.MenuAdministradorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import service.GestionaRutas;
import java.io.FileNotFoundException;


public class SettingsController {

    GestionaRutas gestionaRutas = new GestionaRutas();
    String rutaInicial;
    @FXML
    private TextField RutaTxt;

    private MenuAdministradorController menuAdministradorController;
    private int opcion;
    @FXML
    public void initialize() {
        rutaInicial = gestionaRutas.getRuta();
    }
    public SettingsController() throws FileNotFoundException {
    }

    public void addController(MenuAdministradorController menuAdministradorController) {
        this.menuAdministradorController = menuAdministradorController;
    }
    @FXML
    void btnAplicarOnAction(ActionEvent event) throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (opcion == 1) {
            gestionaRutas.setRuta("src/main/resources/images/logoCambio.png");
            alert.setContentText("Opción 1 aplicada");
            alert.showAndWait();
            menuAdministradorController.setImage();
            menuAdministradorController.stage.close();
        } else if (opcion == 2) {
        gestionaRutas.setRuta("src/main/resources/images/cortoLetras.png");
            alert.setContentText("Opción 2 aplicada");
            alert.showAndWait();
            menuAdministradorController.setImage();
            menuAdministradorController.stage.close();
        }else {
            alert.setContentText("¡No hay cambios por aplicar!");
            alert.showAndWait();
        }
    }

    @FXML
    void btnDefaultOnAction(ActionEvent event) {

    }

    @FXML
    void btnVolverOnAction(ActionEvent event) {
        menuAdministradorController.stage.close();
    }

    @FXML
    void btnOpcion1(ActionEvent event) throws FileNotFoundException {
        opcion = 1;
    }

    @FXML
    void btnOpcion2(ActionEvent event) throws FileNotFoundException {
        opcion = 2;
    }
}
