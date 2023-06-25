package controller.administrador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.administrador.ventanasEmergentes.AgregarProveedorController;
import controller.administrador.ventanasEmergentes.EditarProveedorController;
import domain.System.Supplier;
import domain.TDA.BST;
import exceptions.TreeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ucr.proyecto.HelloApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestionProveedoresController
{
    @FXML
    private TableView tblViewUsuarios;
    @FXML
    private TextField txtBuscar;

    private ObservableList<Supplier> suppliers;
    private AgregarProveedorController agregarProveedorController;
    private EditarProveedorController editarProveedorController;
    public static Stage stage;
    BST bst = new BST();
    @FXML
    public void initialize() {
        setTable();
    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) {
        loadPage("administrador/ventanasEmergentes/agregarProveedor.fxml");
        agregarProveedorController.addController(this);
    }

    @FXML
    void btnModificarOnAction(ActionEvent event) throws TreeException {
        Supplier user = (Supplier) tblViewUsuarios.getSelectionModel().getSelectedItem();
        if (user != null){
            loadPage("administrador/ventanasEmergentes/editarProveedores.fxml");
            editarProveedorController.addController(this);
            editarProveedorController.setSupplier(user);
        }
    }
    @FXML
    void btnEliminarOnAction(ActionEvent event) throws TreeException {
        Supplier suplier = (Supplier) tblViewUsuarios.getSelectionModel().getSelectedItem();
        elimina(suplier, "registroProveedores.json");
    }
    public List<Supplier> leeArchivo2(String path) {
        List<Supplier> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<Supplier>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (Supplier usuarios : users) {
            Supplier newUser = new Supplier(usuarios.getId(), usuarios.getName(),usuarios.getPhoneNumber(), usuarios.getEmail(), usuarios.getAddress());
            if (this.bst != null && path == "registroProveedores.json") {
                if (bst.isEmpty()) {
                    bst.add(newUser);
                } else {
                    try {
                        if (!bst.contains(newUser)) {
                            bst.add(newUser);
                        }
                    } catch (TreeException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return users;
    }

    public void elimina(Supplier usuario, String path) throws TreeException {
        if (!bst.isEmpty()) {
            if (bst.size() != 1) {
                System.out.println("first \n" + bst.size());
                bst.remove(usuario);
                System.out.println("\nSecond\n" + bst.size());
                BST aux = bst;
                System.out.println(aux);
                List<Supplier> a = new ArrayList<>();
                Gson gson = new GsonBuilder().create();
                int i = aux.size();
                while (i!=0) {
                    Supplier min = (Supplier) bst.min();
                    a.add(min);
                    aux.remove(min);
                    i--;
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
                    bst.clear();
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write("");
                    fileWriter.close();
                    System.out.println("Contenido del archivo JSON eliminado con éxito.");
                } catch (IOException e) {
                    System.out.println("Error al intentar vaciar el contenido del archivo JSON: " + e.getMessage());
                }
                ObservableList<Object> emptyList = FXCollections.observableArrayList();

                tblViewUsuarios.setItems(emptyList);
            }
        }
    }



    private void setTable() {
        TableColumn<Supplier, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Supplier, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Supplier, String> mailColumn = new TableColumn<>("E-Mail");
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Supplier, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));


        tblViewUsuarios.getColumns().addAll(idColumn, nameColumn, mailColumn, addressColumn);
        setItems();
    }

    public void setItems() {
        suppliers = FXCollections.observableArrayList();
        suppliers.addAll(leeArchivo2("registroProveedores.json"));
        this.tblViewUsuarios.setItems(suppliers);
        try {
            System.out.println(bst.size());
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.show();
            if (page.contains("agregar")) agregarProveedorController = fxmlLoader.getController();
            else if (page.contains("editar")) this.editarProveedorController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void keyTypedBuscar(KeyEvent keyEvent) {
        FilteredList<Supplier> filteredList = new FilteredList<>(suppliers, b -> true);
        filteredList.setPredicate(user -> {
            if (txtBuscar.getText().isEmpty() || txtBuscar.getText() == null) return true;

            String search = txtBuscar.getText();
            String lowerCaseSearch = search.toLowerCase();

            if (String.valueOf(user.getId()).contains(lowerCaseSearch))
                return true;
            else return false;
        });

        SortedList<Supplier> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tblViewUsuarios.comparatorProperty());
        tblViewUsuarios.setItems(sortedData);
    }
}