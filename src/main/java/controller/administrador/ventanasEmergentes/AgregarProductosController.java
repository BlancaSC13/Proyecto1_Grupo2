package controller.administrador.ventanasEmergentes;

import controller.administrador.GestionProductosController;
import controller.administrador.InventarioController;
import domain.System.Inventory;
import domain.System.Product;
import interfaces.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import service.GestionaArchivo;

public class AgregarProductosController {
    @FXML
    private TextField descripTxt;

    @FXML
    private ChoiceBox<String> idProveedorChoiceBox;

    @FXML
    private TextField idTxt;

    @FXML
    private TextField precioTxt;

    @FXML
    private TextField stockMinimoTxt;

    @FXML
    private TextField stockTxt;
    private InventarioController inventarioController;
    private GestionProductosController gestionProductosController;
    private Tree tree;
    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();
        GestionaArchivo.getProveedor("registroProveedores.json").forEach(proveedor -> {
            items.add(String.valueOf(proveedor.getId()));
        });
        idProveedorChoiceBox.setItems(items);
        idProveedorChoiceBox.setValue("Proveedor");
    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
        if (!datosCompletos()) {
            int id = Integer.parseInt(idTxt.getText());
            String description = descripTxt.getText();
            double price = Double.parseDouble(precioTxt.getText());
            int stock = Integer.parseInt(stockTxt.getText());
            int minStock = Integer.parseInt(stockMinimoTxt.getText());
            int suplierId = Integer.parseInt(idProveedorChoiceBox.getValue());
            if (Inventory.agregaProducto(new Product(id, description, price, stock, minStock, suplierId), tree)) {
                alert.setContentText("Producto agregado con exito");
                alert.showAndWait();
                Inventory.setInventory(GestionaArchivo.getInventory("inventario.json"));
            } else {
                alert.setContentText("No se pudo agregar el producto porque ya existe");
                alert.showAndWait();
            }
            if (this.inventarioController != null){
                this.inventarioController.setItems();
            } else if (this.gestionProductosController != null) {
                this.gestionProductosController.setItems();
            }
            clear();
        }else {
            alert.setContentText("Complete todos los datos para continuar");
            alert.showAndWait();
        }
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        Inventory.stage.close();
    }

    @FXML
    void btnLimpiarOnAction(ActionEvent event) {
        clear();
    }

    private void clear(){
        descripTxt.clear();
        idTxt.clear();
        stockTxt.clear();
        precioTxt.clear();
        stockMinimoTxt.clear();
        idProveedorChoiceBox.setValue("Proveedor");
    }

    public void addController(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
    }

    public void addController(GestionProductosController gestionProductosController) {
        this.gestionProductosController = gestionProductosController;
    }

    private boolean datosCompletos(){
        return stockMinimoTxt.getText().replace(" ", "").equals("")
                || stockTxt.getText().replace(" ", "").equals("")
                || precioTxt.getText().replace(" ", "").equals("")
                || idTxt.getText().replace(" ", "").equals("")
                || descripTxt.getText().replace(" ", "").equals("")
                || idProveedorChoiceBox.getValue().compareToIgnoreCase("Proveedor") == 0;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}