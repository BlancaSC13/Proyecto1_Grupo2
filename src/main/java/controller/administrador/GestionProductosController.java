package controller.administrador;

import controller.administrador.ventanasEmergentes.AgregarProductosController;
import controller.administrador.ventanasEmergentes.ModificarProductosController;
import controller.inicio.loginController;
import domain.System.Event;
import domain.System.Inventory;
import domain.System.Logbooks;
import domain.System.Product;
import domain.TDA.AVL;
import exceptions.TreeException;
import interfaces.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import service.GestionaArchivo;
import ucr.proyecto.HelloApplication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionProductosController
{
    @FXML
    private TableView tblViewUsuarios;
    @FXML
    private TextField txtBuscar;
    private AgregarProductosController agregarProductosController;
    private ModificarProductosController modificarProductosController;
    private ObservableList<Product> items;
    private Tree productos;
    String username;
    private List<Logbooks> logbooks= new ArrayList<>();


    @FXML
    public void initialize() {
        productos = new AVL();
        GestionaArchivo.getInventory("inventario.json").forEach(product -> {
            productos.add(product);
        });
        setTable();


    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) {
        AVL avl = new AVL();
        loadPage("administrador/ventanasEmergentes/agregarProductos.fxml");
        agregarProductosController.addController(this);
        agregarProductosController.setTree(productos);
        logbooks.add(new Logbooks(this.username, new Event(LocalDateTime.now(),"Agregar producto")));
        avl.add(logbooks);
        GestionaArchivo.escribirBitacora("logbooks.json", avl);
    }

    @FXML
    void btnEliminarOnAction(ActionEvent event) {
        AVL avl = new AVL();
        Product product = (Product) tblViewUsuarios.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (product == null){
            alert.setContentText("Seleccione un producto para continuar");
            alert.showAndWait();
            return;
        }
        if (Inventory.eliminarProducto(product, productos)){
            alert.setContentText("Producto eliminado");
            alert.showAndWait();
            setItems();
        }else {
            alert.setContentText("No se pudo eliminar el producto");
            alert.showAndWait();
        }
        logbooks.add(new Logbooks(this.username, new Event(LocalDateTime.now(),"Eliminar producto")));
        avl.add(logbooks);
        GestionaArchivo.escribirBitacora("logbooks.json", avl);
    }

    @FXML
    void btnModificarOnAction(ActionEvent event) {
        AVL avl = new AVL();
        Product product = (Product) tblViewUsuarios.getSelectionModel().getSelectedItem();
        if (product != null) {
            loadPage("administrador/ventanasEmergentes/modificarProductos.fxml");
            modificarProductosController.addController(this);
            modificarProductosController.setProduct(product, productos);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un producto para continuar");
            alert.showAndWait();
        }
        logbooks.add(new Logbooks(this.username, new Event(LocalDateTime.now(),"Modificar producto")));
        avl.add(logbooks);
        GestionaArchivo.escribirBitacora("logbooks.json", avl);
    }

    @FXML
    void keyTypedBuscar(KeyEvent event) {
        FilteredList<Product> filteredList = new FilteredList<>(items, b -> true);
        filteredList.setPredicate(product -> {
            if (txtBuscar.getText().isEmpty() || txtBuscar.getText() == null) return true;

            String search = txtBuscar.getText();
            String lowerCaseSearch = search.toLowerCase();

            if (String.valueOf(product.getId()).contains(lowerCaseSearch)) return true;
            else return product.getDescription().toLowerCase().contains(lowerCaseSearch);
        });

        SortedList<Product> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tblViewUsuarios.comparatorProperty());
        tblViewUsuarios.setItems(sortedData);
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            Inventory.stage = new Stage();
            Inventory.stage.setScene(new Scene(fxmlLoader.load()));
            Inventory.stage.setResizable(false);
            Inventory.stage.show();
            if (page.contains("agregar"))agregarProductosController = fxmlLoader.getController();
            else if (page.contains("modificar")) this.modificarProductosController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTable(){
        TableColumn<Product, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> descColumn = new TableColumn<>("Descripción");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, Integer> priceColumn = new TableColumn<>("Precio");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> cuStockColumn = new TableColumn<>("Stock");
        cuStockColumn.setCellValueFactory(new PropertyValueFactory<>("currentStock"));

        TableColumn<Product, Integer> minStockColumn = new TableColumn<>("Stock");
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minimumStock"));

        TableColumn<Product, Integer> suplierColumn = new TableColumn<>("Id Proveedor");
        suplierColumn.setCellValueFactory(new PropertyValueFactory<>("suplierId"));

        tblViewUsuarios.getColumns().addAll(idColumn, descColumn, priceColumn, cuStockColumn, minStockColumn, suplierColumn);

        setItems();
    }

    public void setItems(){
        try {
            items = FXCollections.observableArrayList();
            items.addAll((List) productos.InOrder());
            tblViewUsuarios.setItems(items);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLogbooks(List<Logbooks> logbooks) {
        this.logbooks = logbooks;
    }

    public List<Logbooks> getLogbooks() {
        return logbooks;
    }
}