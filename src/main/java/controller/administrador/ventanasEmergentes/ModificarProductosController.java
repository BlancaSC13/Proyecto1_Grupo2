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

public class ModificarProductosController
{
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
    private Product product;
    private InventarioController inventarioController;
    private GestionProductosController gestionProductosController;
    private Tree tree;

    @FXML
    public void initialize() {

    }

    @FXML
    void btnModificarOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (!datosCompletos()) {
            int id = Integer.parseInt(idTxt.getText());
            String description = descripTxt.getText();
            double price = Double.parseDouble(precioTxt.getText());
            int stock = Integer.parseInt(stockTxt.getText());
            int minStock = Integer.parseInt(stockMinimoTxt.getText());
            int suplierId = Integer.parseInt(idProveedorChoiceBox.getValue());
            Product product1 = new Product(id, description, price, stock, minStock, suplierId);
            if (Inventory.modificarProducto(product1, tree)) {
                alert.setContentText("Producto modificado con exito");
                alert.showAndWait();
                Inventory.stage.close();
                if (this.inventarioController != null){
                    this.inventarioController.setItems();
                } else if (this.gestionProductosController != null) {
                    this.gestionProductosController.setItems();
                }
            } else {
                alert.setContentText("No se pudo modificar el producto");
                alert.showAndWait();
            }
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

    public void addController(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
    }

    public void addController(GestionProductosController gestionProductosController) {
        this.gestionProductosController = gestionProductosController;
    }


    private void clear(){
        descripTxt.clear();
        stockTxt.clear();
        precioTxt.clear();
        stockMinimoTxt.clear();
    }

    private boolean datosCompletos(){
        return stockMinimoTxt.getText().replaceAll(" ", "").equals("")
                || stockTxt.getText().replaceAll(" ", "").equals("")
                || precioTxt.getText().replaceAll(" ", "").equals("")
                || idTxt.getText().replaceAll(" ", "").equals("")
                || descripTxt.getText().replaceAll(" ", "").equals("");
    }

    public void setProduct(Product product, Tree tree) {
        this.tree = tree;
        this.product = product;
        idProveedorChoiceBox.setValue(String.valueOf(product.getSuplierId()));
        idTxt.setText(String.valueOf(product.getId()));
        descripTxt.setText(product.getDescription());
        precioTxt.setText(String.valueOf(product.getPrice()));
        stockTxt.setText(String.valueOf(product.getCurrentStock()));
        stockMinimoTxt.setText(String.valueOf(product.getMinimumStock()));
    }
}