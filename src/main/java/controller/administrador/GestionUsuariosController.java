package controller.administrador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.administrador.ventanasEmergentes.AgregarUsuarioController;
import controller.administrador.ventanasEmergentes.EditarUsuarioController;
import domain.Objects.Node;
import domain.System.User;
import domain.TDA.SinglyLinkedList;
import exceptions.ListException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import ucr.proyecto.HelloApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestionUsuariosController {
    @FXML
    private TableView tblViewUsuarios;
    @FXML
    private TextField txtBuscar;
    public static Stage stage;
    @FXML
    private BorderPane bp;
    private SinglyLinkedList userList;
    private ObservableList<User> users;
    AgregarUsuarioController agregarUsuarioController;
    EditarUsuarioController editarUsuarioController = new EditarUsuarioController();

    @FXML
    public void initialize() {
        this.userList = new SinglyLinkedList();
        setTable();
        num();

    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.show();if (page.contains("agregar")) agregarUsuarioController = fxmlLoader.getController();
            else if (page.contains("editar")) this.editarUsuarioController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) {
        loadPage("administrador/ventanasEmergentes/agregarUsuarios.fxml");
        agregarUsuarioController.addController(this);
    }

    @FXML
    void btnEliminarOnAction(ActionEvent event) throws ListException {
        User product = (User) tblViewUsuarios.getSelectionModel().getSelectedItem();

        User admin = new User("12", "Administrador", "Admin@gmail.com", "Tucasa", "Administrador");
        if ( util.Utility.compare((User)admin,(User)product) == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Elimina usarios");
            alert.setContentText("El usuario seleccionado no se puede borrar\nIntente con otro");
            alert.showAndWait();
        }else {
        elimina(product, "registroUsuarios.json");
        }
    }

    @FXML
    void btnModificarOnAction(ActionEvent event) throws ListException {
        User user = (User) tblViewUsuarios.getSelectionModel().getSelectedItem();
            if (user != null){
                loadPage("administrador/ventanasEmergentes/editarUsuarios.fxml");
                editarUsuarioController.addController(this);
                editarUsuarioController.setUsuario(user);
            }
    }

    public List<User> leeArchivo2(String path) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<User>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (User usuarios : users) {
            User newUser = new User(usuarios.getIdentificacion(), usuarios.getNombre(), usuarios.getCorreo(), usuarios.getDireccion(), usuarios.getRol());
            if (this.userList != null && path == "registroUsuarios.json") {
                if (userList.isEmpty()) {
                    userList.add(newUser);
                } else {
                    try {
                        if (!userList.contains(newUser)) {
                            userList.add(newUser);
                        }
                    } catch (ListException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return users;
    }

    private void setTable() {

        TableColumn<User, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("identificacion"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<User, String> mailColumn = new TableColumn<>("E-Mail");
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));

        TableColumn<User, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<User, String> rolColumn = new TableColumn<>("Rol");
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        tblViewUsuarios.getColumns().addAll(idColumn, nameColumn, mailColumn, addressColumn, rolColumn);

        setItems();
    }

    public void setItems() {
        users = FXCollections.observableArrayList();
        users.addAll(leeArchivo2("registroUsuarios.json"));
        this.tblViewUsuarios.setItems(users);
    }

    public void num() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null);
        txtBuscar.setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                txtBuscar.setText("");
            }
        });

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtBuscar.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }


    public void elimina(User usuario, String path) throws ListException {
        if (!userList.isEmpty()) {
            if (userList.size() != 1) {
                userList.remove(usuario);
                Node aux = this.userList.getNode(1);
                List<User> a = new ArrayList<>();
                Gson gson = new GsonBuilder().create();
                while (aux != null) {
                    a.add((User) aux.data);
                    aux = aux.next;
                }

                try (FileWriter fileWriter = new FileWriter(path)) {
                    String json = gson.toJson(a.toArray());
                    fileWriter.write(json);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                setItems();
            } else {
                try {
                    userList.clear();
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write("");
                    fileWriter.close();
                } catch (IOException e) {
                    new RuntimeException(e);
                }
                ObservableList<Object> emptyList = FXCollections.observableArrayList();

                tblViewUsuarios.setItems(emptyList);
            }
        }
    }

    public void keyTapedBuscar(KeyEvent keyEvent) {
        FilteredList<User> filteredList = new FilteredList<>(users, b -> true);
        filteredList.setPredicate(user -> {
            if (txtBuscar.getText().isEmpty() || txtBuscar.getText() == null) return true;

            String search = txtBuscar.getText();
            String lowerCaseSearch = search.toLowerCase();

            if (String.valueOf(user.getIdentificacion()).contains(lowerCaseSearch))
                return true;
            else return false;
        });
        SortedList<User> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tblViewUsuarios.comparatorProperty());
        tblViewUsuarios.setItems(sortedData);
    }
}