module ucr.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires PDFjet;
    requires java.base;

    opens ucr.proyecto to javafx.fxml;
    opens domain.TDA to com.google.gson;
    exports domain.Objects to com.google.gson;
    exports ucr.proyecto;
    exports controller.inicio;
    opens controller.inicio to javafx.fxml;
    exports controller.administrador;
    opens controller.administrador to javafx.fxml;
    exports controller.cliente;
    opens controller.cliente to javafx.fxml;
    exports controller.consultor;
    opens controller.consultor to javafx.fxml;
    exports controller.administrador.ventanasEmergentes;
    opens controller.administrador.ventanasEmergentes to javafx.fxml;
    opens domain.System to com.google.gson, javafx.base;

}