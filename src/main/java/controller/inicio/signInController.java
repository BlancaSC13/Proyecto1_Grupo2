package controller.inicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import domain.System.Security;
import domain.System.User;
import domain.TDA.CircularLinkedList;
import exceptions.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.IntegerStringConverter;
import ucr.proyecto.HelloApplication;
import util.Utility;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class signInController {
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtId;
    @FXML
    private BorderPane bp;
    private Alert alert;
    private CircularLinkedList userList;


    @FXML
    public void initialize() {
        this.userList = new CircularLinkedList();
        leeArchivo("registroUsuarios.json");
        num();
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLogInOnAction(ActionEvent event) throws ListException {
        loadPage("login.fxml");
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        if (isValid()) {
            try {
                if (!userList.isEmpty() && userList.contains(new User(txtId.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Registro de usarios");
                    alert.setContentText("El usuario ya Existe\nIntente de nuevo");
                    alert.showAndWait();
                    clean();
                } else {
                    if (Utility.validatePassword(txtPassword.getText())) {
                        Security user = new Security(txtId.getText(), txtPassword.getText());
                        User registraUser = new User(txtId.getText(), txtUsername.getText(), txtEmail.getText(), txtAddress.getText(), "Cliente");
                        encriptaEnArchivo(user, "usuarios.json");
                        registraUsuarios(registraUser, "registroUsuarios");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Registro de usarios");
                        alert.setContentText("Usuario registrado con éxito");
                        alert.showAndWait();
                        //sendEmail();
                        clean();
                        loadPage("login.fxml");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Registro de usarios");
                        alert.setContentText("Contraseña inválida, debe contener:\n8 dígitos\nUna mayúscula\nUna minúscula\nUn número");
                        alert.showAndWait();
                        txtPassword.clear();
                    }

                }
            } catch (ListException e) {
                throw new RuntimeException(e);
            }

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Registro de usarios");
            alert.setContentText("Ingrese los datos para continuar");
            alert.showAndWait();
        }
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
            String destinatario = txtEmail.getText();
            String encabezado = "Bienvenido a FrankieStore";
            String usuario = "Su usuario de ingreso es: " + txtId.getText();
            String password = "Su clave de acceso al sistema es: " + txtPassword.getText();
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(correoPrincipal));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mail.setSubject(encabezado);

            mail.setText("        ¡Hola " + txtUsername.getText() + " Le damos la bienvenida a FrankieStore!\n" +
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

    public boolean isValid() {
        return !txtUsername.getText().isEmpty()
                && !txtPassword.getText().isEmpty()
                && !txtEmail.getText().isEmpty()
                && !txtId.getText().isEmpty()
                && !txtAddress.getText().isEmpty();
    }

    public void clean() {
        txtEmail.clear();
        txtPassword.clear();
        txtUsername.clear();
        txtId.clear();
        txtAddress.clear();
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

    public void num() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null);
        txtId.setTextFormatter(textFormatter);

        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                txtId.setText("");
            }
        });

        txtId.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
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