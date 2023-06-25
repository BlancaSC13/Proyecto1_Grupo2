package controller.cliente;

import domain.createPDF;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GenerarReportesClienteController
{


    @FXML
    private TextArea reportesTXT;
    private RealizarCompras realizarCompras;
    private Alert alert;

    @javafx.fxml.FXML
    public void initialize() {
        this.alert = util.FXUtility.alert("", "");
    }
    @FXML
    void btnCargarDatosOnAction(ActionEvent event) {
        reportesTXT.setText(realizarCompras.getItems().toString());
    }

    @FXML
    void btnImprimirOnAction(ActionEvent event) throws Exception {
        createPDF.createSalesReport();
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
        this.alert.setHeaderText("Reporte de compras");
        this.alert.setContentText("Reporte generado correctamente");
        alert.showAndWait();
    }

}