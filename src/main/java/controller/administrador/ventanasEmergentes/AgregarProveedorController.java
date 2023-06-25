package controller.administrador.ventanasEmergentes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.administrador.GestionProveedoresController;
import domain.System.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgregarProveedorController
{
    @FXML
    private TextField idTxt;
    @FXML
    private TextField nombreTxt;
    @FXML
    private TextField telefonoTxt;
    @FXML
    private TextField correoTxt;
    @FXML
    private TextField direccionTxt;
    private GestionProveedoresController gestionProveedoresController = new GestionProveedoresController();

    @FXML
    public void initialize() {
        num();
    }

    public void btnAgregarOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (isValid()) {
            Supplier supplier = new Supplier(idTxt.getText(), nombreTxt.getText(), telefonoTxt.getText(), correoTxt.getText(),direccionTxt.getText());
            registraUsuarios(supplier, "registroProveedores.json");
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Registro de usarios");
            alert.setContentText("Usuario registrado con éxito");
            alert.showAndWait();
            gestionProveedoresController.stage.close();
            gestionProveedoresController.setItems();
        } else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Registro de usarios");
            alert.setContentText("Ingrese los datos para continuar");
            alert.showAndWait();
        }
    }
    public void addController(GestionProveedoresController gestionProveedoresController) {
        this.gestionProveedoresController = gestionProveedoresController;
    }

    @FXML
    void btnLimpiarOnAction(ActionEvent event) {
        nombreTxt.clear();
        telefonoTxt.clear();
        idTxt.clear();
        direccionTxt.clear();
        correoTxt.clear();
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        GestionProveedoresController.stage.close();
    }
    public static void registraUsuarios(Supplier proveedor, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                boolean b = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new GsonBuilder().create();

        try (FileReader fileReader = new FileReader(path)) {
            Supplier[] suppliers = gson.fromJson(fileReader, Supplier[].class);
            List<Supplier> listaUser;

            if (suppliers != null) {
                listaUser = new ArrayList<>(Arrays.asList(suppliers));
            } else {
                listaUser = new ArrayList<>();
            }

            listaUser.add(proveedor);

            try (FileWriter fileWriter = new FileWriter(path)) {
                String json = gson.toJson(listaUser.toArray());
                fileWriter.write(json);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void num() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null);
        idTxt.setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                idTxt.setText("");
            }
        });

        idTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    private boolean isValid() {
        return !nombreTxt.getText().isEmpty() && !telefonoTxt.getText().isEmpty() && !idTxt.getText().isEmpty()
                && !direccionTxt.getText().isEmpty() && !correoTxt.getText().isEmpty();
    }
}
