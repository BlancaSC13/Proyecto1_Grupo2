package controller.cliente;

import domain.System.*;
import domain.TDA.BTree;
import exceptions.TreeException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import service.GestionaArchivo;

import java.time.LocalDateTime;
import java.util.List;

public class RealizarCompras
{
    @FXML
    private ChoiceBox<String> ProductoChoiceBox = new ChoiceBox<>();

    @FXML
    private TextField cantidadtxt;

    @FXML
    private TableView pedidoTblView = new TableView<>();

    private ObservableList<OrderDetail> items;
    private BTree pedidos;
    private Order order;
    private Reportes reportes;
    @FXML
    public void initialize() {
        reportes = new Reportes("GaCha22");
        pedidos = new BTree();
        ObservableList<String> products = FXCollections.observableArrayList();
        GestionaArchivo.getInventory("inventario.json").forEach(product -> {
            products.add(product.getDescription());
        });
        ProductoChoiceBox.setItems(products);
        ProductoChoiceBox.setValue("Productos");
        setTable();

    }
    @FXML
    void btnAgregarOnAction(ActionEvent event) {
        if (ProductoChoiceBox.getValue().equals("Productos") || cantidadtxt.getText().replaceAll(" ", "").equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Complete todos los datos para agregar un producto a su pedido");
            alert.showAndWait();
            return;
        }

        Product producto = null;
        for (Product product : Inventory.getInventory()) {
            if (product.getDescription().equals(ProductoChoiceBox.getValue())){
                producto = product;
            }
        }
        int quantity = Integer.parseInt(cantidadtxt.getText());
        pedidos.add(new OrderDetail(quantity, producto));
        setItems();
        ProductoChoiceBox.setValue("Productos");
        cantidadtxt.clear();
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        pedidos.clear();
        pedidoTblView.getItems().clear();
        ProductoChoiceBox.setValue("Productos");
        cantidadtxt.clear();
    }

    @FXML
    void btnConsultarOnAction(ActionEvent event) {
        if (ProductoChoiceBox.getValue().equals("Productos")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un producto para comprobar su disponibilidad");
            alert.showAndWait();
            return;
        }

        Product producto = null;
        for (Product product : Inventory.getInventory()) {
            if (product.getDescription().equals(ProductoChoiceBox.getValue())){
                producto = product;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("El producto consultado tiene " + Inventory.consultarDisponibilidad(producto) + " existencias");

        alert.showAndWait();
    }

    @FXML
    void btnRealizarPedidoOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (!pedidos.isEmpty()) {
            if (Inventory.realizarCompra(this.pedidos)){
                alert.setContentText("Pedido realizado");
                try {
                    order = new Order(LocalDateTime.now(), "GaCha22", pedidos.InOrder());
                } catch (TreeException e) {
                    throw new RuntimeException(e);
                }
                reportes.addOrder(order);
//                GestionaArchivo.escribirReporte(reportes, "reportes.json"); //Descomentar esta linea cuando funcione el metodo de escribir reporte
            } else {
                alert.setContentText("El pedido no se pudo realizar");
            }
        }else{
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Seleccione al menos un producto para realizar su pedido");
        }
        alert.showAndWait();
    }

    @FXML
    void onActionEliminarProducto(ActionEvent event) {
        OrderDetail orderDetail = (OrderDetail) pedidoTblView.getSelectionModel().getSelectedItem();
        if (orderDetail == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un producto para eliminarlo del pedido");
            alert.showAndWait();
            return;
        }
        try {
            pedidos.remove(orderDetail);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        setItems();
    }

    private void setTable(){
        TableColumn<OrderDetail, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<OrderDetail, String> descColumn = new TableColumn<>("Producto");
        descColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue().getProduct();
            return new SimpleStringProperty(product != null ? product.getDescription() : "");
        });/*asdfasdfasdfa*/

        TableColumn<OrderDetail, Integer> quantityColumn = new TableColumn<>("Cantidad");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<OrderDetail, Double> precioUnitarioColumn = new TableColumn<>("Precio Unitario");
        precioUnitarioColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
//        cuStockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        TableColumn<OrderDetail, Integer> precioTotalColumn = new TableColumn<>("Precio Total");
        precioTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));


        pedidoTblView.getColumns().addAll(idColumn, descColumn, quantityColumn, precioUnitarioColumn, precioTotalColumn);

    }

    public void setItems(){
        try {
            items = FXCollections.observableArrayList();
            if (!pedidos.isEmpty()) items.addAll((List) pedidos.InOrder());
            pedidoTblView.setItems(items);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<OrderDetail> getItems() {
        return items;
    }

    public void setItems(ObservableList<OrderDetail> items) {
        this.items = items;
    }
}