package service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import domain.TDA.SinglyLinkedList;
import exceptions.ListException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionaRutas {
    private String ruta;
    private static String path = "imagen.json";

    public GestionaRutas() {
    }



    public String getRuta() {
         return leeArchivo2(path);
    }

    public void setRuta(String ruta) {
        registraRuta(ruta);
    }

    public static void registraRuta(String ruta) {

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
            String[] user = gson.fromJson(fileReader, String[].class);
            List<String> listaUser;

            if (user != null) {
                listaUser = new ArrayList<>(Arrays.asList(user));
            } else {
                listaUser = new ArrayList<>();
            }
            listaUser.clear();
            listaUser.add(ruta);

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
    private SinglyLinkedList userList;
    public String leeArchivo2(String path) {
        List<String> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<String>>() {
            }.getType();
            users = gson.fromJson(br, list);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        for (String usuarios : users) {
            String newUser = users.toString();
            if (this.userList != null && path == "registroUsuarios.json") {
                if (userList.isEmpty()) {
                    userList.add(newUser);
                } else {
                    try {
                        if (!userList.contains(newUser)) {
                            userList.add(newUser);
                        }
                    } catch (ListException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return eliminarPrimerYUltimoCaracter(users.toString());
    }
    public static String eliminarPrimerYUltimoCaracter(String texto) {
        if (texto == null || texto.length() < 2) {
            return "";
        }
        return texto.substring(1, texto.length() - 1);
    }
}
