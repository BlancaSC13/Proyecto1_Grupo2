package controller.administrador.ventanasEmergentes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.administrador.GestionProveedoresController;
import domain.System.Supplier;
import exceptions.ListException;
import exceptions.TreeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditarProveedorController {
    @FXML
    private TextField correoTxt;

    @FXML
    private TextField direccionTxt;

    @FXML
    private Text idLabel;

    @FXML
    private TextField nombreTxt;

    @FXML
    private TextField telefonotxt;

    String nombre;
    String direccion;
    String correo;
    String id;
    String telefono;
    private Supplier supplier;
    GestionProveedoresController gestionProveedoresController;

    public void setSupplier(Supplier usuario) throws TreeException {
        this.supplier = usuario;
        nombre = usuario.getName();
        nombreTxt.setText(nombre);
        direccion = usuario.getAddress();
        direccionTxt.setText(direccion);
        telefono = usuario.getPhoneNumber();
        telefonotxt.setText(telefono);
        correo = usuario.getEmail();
        correoTxt.setText(correo);
        id = usuario.getId();
        idLabel.setText(id);
        gestionProveedoresController.elimina(usuario,"registroProveedores.json");
    }

    public void addController(GestionProveedoresController gestionProveedoresController) {
        this.gestionProveedoresController = gestionProveedoresController;
    }

    @FXML
    public void btnAgregarOnAction(ActionEvent event) throws ListException {
        if (isValid()) {
            nombre = nombreTxt.getText();
            direccion = direccionTxt.getText();
            correo = correoTxt.getText();

            Supplier user = new Supplier(id,nombre,telefono,correo,direccion);
            registraUsuarios(user, "registroProveedores.json");
            gestionProveedoresController.setItems();
            GestionProveedoresController.stage.close();

        }
    }
    public static void registraUsuarios(Supplier usuario, String path) {
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
            Supplier[] user = gson.fromJson(fileReader, Supplier[].class);
            List<Supplier> listaUser;

            if (user != null) {
                listaUser = new ArrayList<>(Arrays.asList(user));
            } else {
                listaUser = new ArrayList<>();
            }

            listaUser.add(usuario);

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

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        Supplier user = new Supplier(id,nombre,telefono,correo,direccion);
        registraUsuarios(user, "registroProveedores.json");
        gestionProveedoresController.setItems();
        GestionProveedoresController.stage.close();

    }

    @FXML
    void btnLimpiarOnAction(ActionEvent event) {
        nombreTxt.clear();
        direccionTxt.clear();
        correoTxt.clear();
    }

    private boolean isValid() {
        return !nombreTxt.getText().isEmpty()
                && !direccionTxt.getText().isEmpty() && !correoTxt.getText().isEmpty();
    }
}