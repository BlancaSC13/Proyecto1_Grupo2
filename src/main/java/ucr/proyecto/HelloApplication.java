package ucr.proyecto;

import domain.System.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.GestionaArchivo;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("administrador/menuAdministrador.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FrankieStore");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        Inventory.setInventory(GestionaArchivo.getInventory("inventario.json"));
    }

    public static void main(String[] args) {
        launch();
    }
}