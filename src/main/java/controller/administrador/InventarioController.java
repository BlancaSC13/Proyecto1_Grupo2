package controller.administrador;

import controller.administrador.ventanasEmergentes.AgregarProductosController;
import controller.administrador.ventanasEmergentes.ModificarProductosController;
import domain.System.Inventory;
import domain.System.Product;
import domain.TDA.BTree;
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
import ucr.proyecto.HelloApplication;
import java.io.IOException;
import java.util.List;

public class InventarioController {

    @FXML
    private TableView inventarioTableView;
    @FXML
    private TextField buscarTxtField;
    private AgregarProductosController agregarProductosController;
    private ModificarProductosController modificarProductosController;
    private ObservableList<Product> items;
    private Tree productos;

    @FXML
    public void initialize() {
        productos = new BTree();
        Inventory.getInventory().forEach(product -> {
            productos.add(product);
        });
        setTable();
    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) {

        loadPage("administrador/ventanasEmergentes/agregarProductos.fxml");
        agregarProductosController.addController(this);
        agregarProductosController.setTree(productos);
    }

    @FXML
    void btnModificarOnAction(ActionEvent event) {
        Product product = (Product) inventarioTableView.getSelectionModel().getSelectedItem();
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
    }

    @FXML
    void keyTypedBuscar(KeyEvent event) {
        FilteredList<Product> filteredList = new FilteredList<>(items, b -> true);
        filteredList.setPredicate(product -> {
            if (buscarTxtField.getText().isEmpty() || buscarTxtField.getText() == null) return true;

            String search = buscarTxtField.getText();
            String lowerCaseSearch = search.toLowerCase();

            if (String.valueOf(product.getId()).contains(lowerCaseSearch)) return true;
            else return product.getDescription().toLowerCase().contains(lowerCaseSearch);
        });

        SortedList<Product> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(inventarioTableView.comparatorProperty());
        inventarioTableView.setItems(sortedData);
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            Inventory.stage = new Stage();
            Inventory.stage.setScene(new Scene(fxmlLoader.load()));
            Inventory.stage.setResizable(false);
            Inventory.stage.show();
            if (page.contains("agregar")) agregarProductosController = fxmlLoader.getController();
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

        inventarioTableView.getColumns().addAll(idColumn, descColumn, priceColumn, cuStockColumn, minStockColumn, suplierColumn);

        setItems();
    }

    public void setItems(){
        try {
            items = FXCollections.observableArrayList();
            items.addAll((List) productos.InOrder());
            inventarioTableView.setItems(items);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

}