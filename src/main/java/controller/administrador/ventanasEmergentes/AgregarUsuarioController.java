package controller.administrador.ventanasEmergentes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.administrador.GestionUsuariosController;
import domain.System.Security;
import domain.System.User;
import domain.TDA.SinglyLinkedList;
import exceptions.ListException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import util.Utility;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AgregarUsuarioController {
    @FXML
    private TextField correoTxt;
    private GestionUsuariosController gestionUsuariosController = new GestionUsuariosController();
    @FXML
    private TextField direccionTxt;
    @FXML
    private TextField idTxt;
    @FXML
    private ChoiceBox<String> rolChoiseBox;
    @FXML
    private TextField nombreTxt;
    private SinglyLinkedList userList;
    private Alert alert;
    @FXML
    private TextField passwordTxt;


    public void initialize() {
        num();
        ObservableList<String> options = FXCollections.observableArrayList("Cliente", "Consultor", "Administrador");
        rolChoiseBox.setItems(options);
        this.userList = new SinglyLinkedList();
        leeArchivo("registroUsuarios.json");
    }

    @FXML
    void btnAgregarOnAction(ActionEvent event) throws ListException {
        if (isValid()) {
            if(!userList.isEmpty()&&userList.contains(new User(idTxt.getText()))){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Registro de usarios");
                alert.setContentText("El usuario ya Existe\nIntente de nuevo");
                alert.showAndWait();
                btnLimpiarOnAction(event);
            }else{
                if(Utility.validatePassword(passwordTxt.getText())){
                    Security user = new Security(idTxt.getText(), passwordTxt.getText());
                    User usuario = new User(idTxt.getText(), nombreTxt.getText(), correoTxt.getText(), direccionTxt.getText(), rolChoiseBox.getValue());
                    registraUsuarios(usuario, "registroUsuarios.json");

                    switch (rolChoiseBox.getValue()){
                        case "Cliente":
                            encriptaEnArchivo(user, "usuarios.json");
                            break;
                        case "Administrador":
                            encriptaEnArchivo(user, "admis.json");
                            break;
                        case "Consultor":
                            encriptaEnArchivo(user, "consultores.json");
                            break;
                    }
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Registro de usarios");
                    alert.setContentText("Usuario registrado con éxito");
                    alert.showAndWait();
                    sendEmail();
                    btnCancelarOnAction(event);
                    gestionUsuariosController.setItems();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Registro de usarios");
                    alert.setContentText("Contraseña inválida, debe contener:\n8 dígitos\nUna mayúscula\nUna minúscula\nUn número");
                    alert.showAndWait();
                    passwordTxt.clear();
                }
            }
        } else{
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Registro de usarios");
        alert.setContentText("Ingrese los datos para continuar");
        alert.showAndWait();
    }

}

    public static void encriptaEnArchivo(Security security, String path) {
        String data = security.getPassword();
        byte[] bytes = Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
        String base64 = new String(bytes);
        security.setPassword(base64);
        File file = new File(path);
        if (!file.exists()) {
            try {
                boolean b = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new GsonBuilder().create();

        try (FileReader fileReader = new FileReader(path)) {
            Security[] password = gson.fromJson(fileReader, Security[].class);
            List<Security> listaPassword;

            if (password != null) {
                listaPassword = new ArrayList<>(Arrays.asList(password));
            } else {
                listaPassword = new ArrayList<>();
            }

            listaPassword.add(security);

            try (FileWriter fileWriter = new FileWriter(path)) {
                String json = gson.toJson(listaPassword.toArray());
                fileWriter.write(json);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void num() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null);
        idTxt.setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                idTxt.setText("");
            }
        });

        idTxt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void sendEmail() {
        try {
            Properties propiedad = new Properties();
            propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedad.setProperty("mail.smtp.starttls.enable", "true");
            propiedad.setProperty("mail.smtp.port", "587");
            propiedad.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(propiedad);
            String correoPrincipal = "frankiestore4@gmail.com";
            String contraseña = "qdgefmkeiforlybr"; //es la clave de acceso que usa gmail, no cambiar//
            String destinatario = correoTxt.getText();
            String encabezado = "Bienvenido a FrankieStore";
            String usuario = "Su usuario de ingreso es: " + idTxt.getText();
            String password = "Su clave de acceso al sistema es: " + passwordTxt.getText();
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(correoPrincipal));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mail.setSubject(encabezado);

            mail.setText("        ¡Hola " + nombreTxt.getText() + " Le damos la bienvenida a FrankieStore!\n" +
                    "Una tienda especializada en la venta de camisetas, sudaderas y sombreros inspirados en series retro.\n\n" +
                    "La fecha de apertura de su cuenta es: " + Utility.fecha() + "\n\n" +
                    "Sus datos de ingreso al sistema son:\n\n" + usuario + "\n\n" + password + "\n\n" +
                    "        ¡Gracias por elegir nuestros productos!\n" +
                    "\n\nNo responda a esta dirección de correo electrónico, ya que solo se utiliza para el envío de información.\n");

            Transport transport = session.getTransport("smtp");
            transport.connect(correoPrincipal, contraseña);
            transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void registraUsuarios(User usuario, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                boolean b = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new GsonBuilder().create();

        try (FileReader fileReader = new FileReader(path)) {
            User[] user = gson.fromJson(fileReader, User[].class);
            List<User> listaUser;

            if (user != null) {
                listaUser = new ArrayList<>(Arrays.asList(user));
            } else {
                listaUser = new ArrayList<>();
            }

            listaUser.add(usuario);

            try (FileWriter fileWriter = new FileWriter(path)) {
                String json = gson.toJson(listaUser.toArray());
                fileWriter.write(json);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        GestionUsuariosController.stage.close();
    }

    @FXML
    void btnLimpiarOnAction(ActionEvent event) {
        nombreTxt.clear();
        idTxt.clear();
        direccionTxt.clear();
        correoTxt.clear();
        passwordTxt.clear();
    }

    public void addController(GestionUsuariosController gestionUsuariosController) {
        this.gestionUsuariosController = gestionUsuariosController;
    }

    private boolean isValid() {
        return !nombreTxt.getText().isEmpty()  && !idTxt.getText().isEmpty()
                && !direccionTxt.getText().isEmpty() && !correoTxt.getText().isEmpty() && !passwordTxt.getText().isEmpty();
    }
    public List<User> leeArchivo(String path) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<User>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (User usuarios : users) {
            User newUser = new User(usuarios.getIdentificacion());
            if (this.userList != null && path == "registroUsuarios.json")
                userList.add(newUser);
        }
        return users;
    }
}