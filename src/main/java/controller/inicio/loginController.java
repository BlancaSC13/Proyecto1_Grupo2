package controller.inicio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.administrador.GestionProveedoresController;
import controller.administrador.MenuAdministradorController;
import domain.System.Security;
import domain.TDA.CircularLinkedList;
import exceptions.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import ucr.proyecto.HelloApplication;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class loginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private BorderPane bp;
    private CircularLinkedList usarios;
    private CircularLinkedList admis;
    private CircularLinkedList consultores;
    private Alert alert;
    private String username;
    public static Stage stage;


    @FXML
    public void initialize() {
        alert = new Alert(Alert.AlertType.ERROR);
        this.usarios = new CircularLinkedList();
        this.admis = new CircularLinkedList();
        this.consultores = new CircularLinkedList();
        //llena la CircularLinkedList cuando inicia con los datos del archivo
        leeArchivo("usuarios.json");
        leeArchivo("admis.json");
        leeArchivo("consultores.json");
        num();
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadPage2(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage= new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void btnLogInOnAction(ActionEvent event) {
        if (isValid()) {
            try {
                if (!usarios.isEmpty() && usarios.contains(new Security(txtUsername.getText(), txtPassword.getText()))) {
                    loadPage2("cliente/menuCliente.fxml");
                    this.setUsername(txtUsername.getText());
                } else if (!admis.isEmpty() && admis.contains(new Security(txtUsername.getText(), txtPassword.getText()))) {
                    loadPage2("administrador/menuAdministrador.fxml");
                } else if (!consultores.isEmpty() && consultores.contains(new Security(txtUsername.getText(), txtPassword.getText()))) {
                    loadPage2("consultor/menuConsultor.fxml");
                }else {
                    alert.setHeaderText("SignIn Error");
                    alert.setContentText("El usuario ingresado no existe");
                    alert.showAndWait();
                    clean();

                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("SignIn Error");
            alert.setContentText("Ingrese los datos para continuar");
            alert.showAndWait();
        }
    }
    public void num() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null);
        txtUsername.setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                txtUsername.setText("");
            }
        });

        txtUsername.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtUsername.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    @FXML
    void btnSignInOnAction(ActionEvent event){
        loadPage("signIn.fxml");
    }

    //Método que lee del archivo y decodifica la contraseña para luego validar si el usuario existe
    public List<Security> leeArchivo(String path) {
        List<Security> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<Security>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (Security security : users) {
            byte[] bytes = Base64.getDecoder().decode(security.getPassword());
            String decoderPassword = new String(bytes, StandardCharsets.UTF_8);
            security.setPassword(decoderPassword);
            Security newUser = new Security(security.getUser(), security.getPassword());
            if (this.usarios != null && path == "usuarios.json") {
                usarios.add(newUser);
            } else if (this.admis != null && path == "admis.json") {
                admis.add(newUser);
            } else if (this.usarios != null && path == "consultores.json") {
                consultores.add(newUser);
            }
        }
        return users;
    }

    public boolean isValid() {
        return !txtUsername.getText().isEmpty()
                && !txtPassword.getText().isEmpty();
    }

    public void clean() {
        txtPassword.clear();
        txtUsername.clear();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}