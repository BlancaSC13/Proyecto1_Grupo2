package controller.administrador.ventanasEmergentes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.administrador.GestionUsuariosController;
import domain.System.User;
import exceptions.ListException;
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

public class EditarUsuarioController {
    @FXML
    private TextField correoTxt;
    @FXML
    private TextField direccionTxt;
    @FXML
    private TextField nombreTxt;
    private User usuario;
    @FXML
    private Text rolLabel;
    String nombre;
    String direccion;
    String correo;
    String rol;

    GestionUsuariosController gestionUsuariosController;
    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) throws ListException {
        this.usuario = usuario;
        nombre = usuario.getNombre();
        nombreTxt.setText(nombre);
        direccion = usuario.getDireccion();
        direccionTxt.setText(direccion);
        correo = usuario.getCorreo();
        correoTxt.setText(correo);
        rol = usuario.getRol();
        rolLabel.setText(rol);
        gestionUsuariosController.elimina(usuario,"registroUsuarios.json");
    }

    public void addController(GestionUsuariosController gestionUsuariosController) {
        this.gestionUsuariosController = gestionUsuariosController;
    }

    @FXML
    public void btnAgregarOnAction(ActionEvent event) throws ListException {
        if (isValid()) {
            nombre = nombreTxt.getText();
            direccion = direccionTxt.getText();
            correo = correoTxt.getText();
            User user = new User(usuario.getIdentificacion(),nombre,correo,direccion, usuario.getRol());
            registraUsuarios(user, "registroUsuarios.json");
            gestionUsuariosController.setItems();
            gestionUsuariosController.stage.close();

        }
    }
    public static void registraUsuarios(User usuario, String path) {
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
            User[] user = gson.fromJson(fileReader, User[].class);
            List<User> listaUser;

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
        User user = new User(usuario.getIdentificacion(),nombre,correo,direccion, usuario.getRol());
        registraUsuarios(user, "registroUsuarios.json");
        gestionUsuariosController.stage.close();
        gestionUsuariosController.setItems();
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