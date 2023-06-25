package controller.administrador;

import com.google.gson.Gson;
import domain.System.CostsControl;
import domain.System.Product;
import domain.TDA.AVL;
import domain.TDA.HeaderLinkedQueue;
import domain.createPDF;
import exceptions.QueueException;
import exceptions.TreeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.PointLight;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import service.GestionaArchivo;

import java.util.Collections;
import java.util.List;

public class GenerarReporteAdminController {
    @javafx.fxml.FXML
    private ChoiceBox<String> tipoReporteChoiceBox;
    @FXML
    private TextArea reportesTXT;
    private Alert alert;

    @javafx.fxml.FXML
    public void initialize() {
        tipoReporteChoiceBox.getItems().add("Reporte de inventario");
        tipoReporteChoiceBox.getItems().add("Reporte de pedidos");
        tipoReporteChoiceBox.getItems().add("Reporte sobre demanda de productos");
        tipoReporteChoiceBox.getItems().add("Reporte de costos");
        this.alert = util.FXUtility.alert("", "");
        tipoReporteChoiceBox.setValue("Tipo de reporte");
        reportesTXT.setText("");
    }

    @FXML
    void btnCargarOnAction(ActionEvent event) throws TreeException, QueueException {
        reportesTXT.setText("");
        reportesTXT.clear();
        if (tipoReporteChoiceBox.getValue().equals("Reporte de inventario")) {
            AVL avl = new AVL();
            avl = GestionaArchivo.getInventory2("inventario.json");
            List<Object> datos = avl.InOrder();
            for (int i = 0; i < datos.size(); i++) {
                reportesTXT.setText(reportesTXT.getText() + datos.get(i) + "\n");
            }
        } else if (tipoReporteChoiceBox.getValue().equals("Reporte de costos")) {
            HeaderLinkedQueue info = new HeaderLinkedQueue();
            info = CostsControl.calculate();
            reportesTXT.setText(info.data());
        }else if (tipoReporteChoiceBox.getValue().equals("Reporte de pedidos")) {
            //agregar en text area
        } else if (tipoReporteChoiceBox.getValue().equals("Reporte sobre demanda de productos")) {
            //agregar en text area
        } else {
            this.alert.setAlertType(Alert.AlertType.ERROR);
            this.alert.setHeaderText("Error");
            this.alert.setContentText("Debe seleccionar el tipo de reporte");
            alert.showAndWait();
        }
    }

    @FXML
    void btnGenerarOnAction(ActionEvent event) throws Exception {
        if (tipoReporteChoiceBox.getValue().equals("Reporte de inventario")) {
            createPDF.createInventoryReport();
            this.alert.setAlertType(Alert.AlertType.INFORMATION);
            this.alert.setHeaderText("Reporte de inventario");
            this.alert.setContentText("Reporte generado correctamente");
            alert.showAndWait();
        } else if (tipoReporteChoiceBox.getValue().equals("Reporte de costos")) {
            createPDF.createProductCostsReport();
            this.alert.setAlertType(Alert.AlertType.INFORMATION);
            this.alert.setHeaderText("Reporte de costos");
            this.alert.setContentText("Reporte generado correctamente");
            alert.showAndWait();
        } else if (tipoReporteChoiceBox.getValue().equals("Reporte de pedidos")) {
            createPDF.createSalesReport();
            this.alert.setAlertType(Alert.AlertType.INFORMATION);
            this.alert.setHeaderText("Reporte de pedidos");
            this.alert.setContentText("Reporte generado correctamente");
            alert.showAndWait();
        } else if (tipoReporteChoiceBox.getValue().equals("Reporte sobre demanda de productos")) {
            createPDF.createProductDemandReport();
            this.alert.setAlertType(Alert.AlertType.INFORMATION);
            this.alert.setHeaderText("Reporte sobre demanda de pedidos");
            this.alert.setContentText("Reporte generado correctamente");
            alert.showAndWait();
        } else {
            this.alert.setAlertType(Alert.AlertType.ERROR);
            this.alert.setHeaderText("Error");
            this.alert.setContentText("Debe seleccionar el tipo de reporte");
            alert.showAndWait();
        }
    }
}