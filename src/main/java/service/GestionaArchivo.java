package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import domain.System.Logbooks;
import domain.System.Product;
import domain.System.Reportes;
import domain.System.Supplier;
import domain.TDA.AVL;
import domain.TDA.HeaderLinkedQueue;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionaArchivo {
    public static List<Product> getInventory(String path) {
        AVL avlTree = new AVL();
        String jsonString; //Variable para almacenar el contenido del archivo JSON en formato de String

        String salida = ""; // String que contendrá el reporte de vuelos
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(path))); //Lee el contenido del archivo en forma de bytes y lo convierte a un String.
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage(), e); //Se lanza una excepción si hay un error al leer el archivo.
        }
        Gson gson = new GsonBuilder() //Intancia de Gson con LocalTimeTypeAdapter
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Product[] products = gson.fromJson(jsonString, Product[].class); //Se convierte el contenido del archivo JSON en un array de objetos Vuelo


        return Arrays.asList(products);
    }
    public static AVL getInventory2(String path) {
        AVL avlTree = new AVL();
        String jsonString; //Variable para almacenar el contenido del archivo JSON en formato de String

        String salida = ""; // String que contendrá el reporte de vuelos
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(path))); //Lee el contenido del archivo en forma de bytes y lo convierte a un String.
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage(), e); //Se lanza una excepción si hay un error al leer el archivo.
        }
        Gson gson = new GsonBuilder() //Intancia de Gson con LocalTimeTypeAdapter
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();

        Product[] products = gson.fromJson(jsonString, Product[].class); //Se convierte el contenido del archivo JSON en un array de objetos Vuelo


        if (products == null) return avlTree;
        for (Product vuelo : products) {
            avlTree.add(vuelo); ; //Si existen vuelos entonces se concatena su información en la variable que se va a retornar, así se imprime el reporte.
        }
        return avlTree; //Se retorna el reporte de vuelos como un String.
    }

    public static List<Supplier> getProveedor(String path) {
        String jsonString; //Variable para almacenar el contenido del archivo JSON en formato de String

        String salida = ""; // String que contendrá el reporte de vuelos
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(path))); //Lee el contenido del archivo en forma de bytes y lo convierte a un String.
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage(), e); //Se lanza una excepción si hay un error al leer el archivo.
        }
        Gson gson = new GsonBuilder() //Intancia de Gson con LocalTimeTypeAdapter
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Supplier[] products = gson.fromJson(jsonString, Supplier[].class); //Se convierte el contenido del archivo JSON en un array de objetos Vuelo

        return Arrays.asList(products); //Se retorna el reporte de vuelos como un String.
    }

    public static String classAJson(Object solicitud){
        String json = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();
        json = gson.toJson(solicitud);
        return json;
    }

    public static Product leerProducto(String path) { //Método que se encarga de leer un solo vuelo.
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Product vuelo = null;
        try (FileReader reader = new FileReader(path)) {
            vuelo =  gson.fromJson(reader, Product.class); //Se convierte el contenido del archivo en un objeto Vuelo.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vuelo; //Se retorna el vuelo leído.
    }

    public static Product jsonAProducto(String json){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Product vuelo = null;
        vuelo =  gson.fromJson(json, Product.class);

        return vuelo;
    }

    public static void escribirProducto(List products, String path) {
        File file = new File(path);
        if (!file.exists()){
            try {
                boolean b = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //Se crea una instancia de Gson con un adaptador de tipo LocalTimeTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        try (FileWriter fileWriter = new FileWriter(path)) {
            String json = gson.toJson(products.toArray()); // Convierte la lista de vuelos en un arreglo JSON
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir los vuelos en el archivo: " + e.getMessage(), e);
        }

    }

    public static void escribirReporte(Reportes reportes, String path) {
        File file = new File(path);
        if (!file.exists()){
            try {
                boolean b = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Se crea una instancia de Gson con un adaptador de tipo LocalTimeTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter())
                .create();

        try (FileReader fileReader = new FileReader(path)) {
            Reportes[] reportesExistentes = gson.fromJson(fileReader, Reportes[].class); // Lee los vuelos existentes del archivo
            boolean encontroUsuario = false;
            List<Reportes> listaVuelos;

            if (reportesExistentes != null) {
                for (int i = 0; i < reportesExistentes.length; i++) {
                    if (reportesExistentes[i].getId().equals(reportes.getId())){
                        reportesExistentes[i].getOrders().addAll(reportes.getOrders());
                        encontroUsuario = true;
                    }
                }
                listaVuelos = new ArrayList<>(Arrays.asList(reportesExistentes)); // Convierte el arreglo en una lista
            } else {
                listaVuelos = new ArrayList<>(); // Crea una nueva lista si no hay vuelos existentes
            }

            if (!encontroUsuario) listaVuelos.add(reportes); // Agrega el nuevo vuelo a la lista

            try (FileWriter fileWriter = new FileWriter(path)) {
                String json = gson.toJson(listaVuelos.toArray()); // Convierte la lista de vuelos en un arreglo JSON
                fileWriter.write(json);
            } catch (IOException e) {
                throw new RuntimeException("Error al escribir los vuelos en el archivo: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer los vuelos existentes del archivo: " + e.getMessage(), e);
        }
    }

    // archivos txt
    public void registrarPlacas(String placa, String nombreArchivo) {
        try {
            // Crea un BufferedWriter que permite escribir en el archivo especificado en modo (true), es decir que no borra el contenido existente del archivo
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true));

            // Escribe la placa en una nueva línea del archivo
            writer.write(placa);
            writer.newLine();

            // Cierra el BufferedWriter para asegurar que los cambios se guarden en el archivo
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean buscarPlacaEnArchivo(String placa, String nombreArchivo) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo)); //BufferedReader que permite leer el contenido del archivo
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.equals(placa)) { //Se compara cada línea del archivo con la placa buscada
                    reader.close();
                    return true; // La placa se encontró en el archivo
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // La placa no se encontró en el archivo
    }
    public static void escribirBitacora(String path, AVL logbooks) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String json = gson.toJson(logbooks);
        try (FileWriter fileWriter = new FileWriter(path, false)) {
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
            return context.serialize(dateTime.format(FORMATTER));
        }
    }
    //Lee Archivo de bitácora
    public static AVL leeArchivo(String path) {
        AVL avl = new AVL();
        List<Object> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            Type list = new TypeToken<List<Logbooks>>() {
            }.getType();
            avl = gson.fromJson(br, AVL.class);
        } catch (IOException e) {
            new RuntimeException(e);
        }
        return avl;
    }
}