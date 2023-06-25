package controller.administrador;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.System.Product;
import domain.TDA.BST;
import exceptions.TreeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrevisionDemandaController {
    @FXML
    private TableView demandaTblView;
    private BST bst = new BST();
    private ObservableList<Product> items;

    @FXML
    public void initialize() {
        leeArchivo2("inventario.json");
        setTable();
    }

    @FXML
    void btnProcesarOnAction(ActionEvent event) throws TreeException {
        Product product = (Product) demandaTblView.getSelectionModel().getSelectedItem();

        if(product.getCurrentStock()<= product.getMinimumStock()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Previsión de Demanda");
            alert.setContentText("El producto cuenta con stock menor a stock mínimo\nFavor contacte a los proveedores para llenar Stock");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Previsión de Demanda");
            alert.setContentText("El producto cuenta con stock suficiente");
            alert.showAndWait();
        }

    }

    public List<Product> leeArchivo2(String path) {
        List<Product> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<Product>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (Product product : users) {
            Product newProduct = new Product(product.getId(), product.getDescription(), product.getPrice(), product.getCurrentStock(), product.getMinimumStock(), product.getSuplierId());
            if (this.bst != null && path == "inventario.json") {
                if (bst.isEmpty()) {
                    bst.add(newProduct);
                    users.contains(newProduct);
                } else {
                    try {
                        if (!bst.contains(newProduct)) {
                            bst.add(newProduct);
                        }
                    } catch (TreeException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return users;
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

        TableColumn<Product, Integer> minStockColumn = new TableColumn<>("Min Stock");
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minimumStock"));

        TableColumn<Product, Integer> suplierColumn = new TableColumn<>("Id Proveedor");
        suplierColumn.setCellValueFactory(new PropertyValueFactory<>("suplierId"));

        demandaTblView.getColumns().addAll(idColumn, descColumn, priceColumn, cuStockColumn, minStockColumn, suplierColumn);

        setItems();
    }

    public void setItems() {
        items = FXCollections.observableArrayList();
        items.addAll(leeArchivo2("inventario.json"));
        this.demandaTblView.setItems(items);

    }

}