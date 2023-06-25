package domain.System;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.TDA.HeaderLinkedQueue;
import exceptions.QueueException;
import exceptions.TreeException;
import service.GestionaArchivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CostsControl {
    public static HeaderLinkedQueue costs;
    static String totalCostos = "Costo total de la mercancía: ₡";
    public static final String path = "costsControl.json";

    public HeaderLinkedQueue getCosts() {
        return costs;
    }

    public void setCosts(HeaderLinkedQueue costs) {
        this.costs = costs;
    }

    public static HeaderLinkedQueue calculate() throws TreeException, QueueException {
        costs = new HeaderLinkedQueue();
        totalCostos = "Costo total de la mercancía en colones: ";
        domain.TDA.AVL avl = new domain.TDA.AVL();
        avl = GestionaArchivo.getInventory2("inventario.json");
        List<Object> datos = avl.InOrder();
        List<Product> datos2 = new ArrayList<>();
        long total = 0;
        for (int i = 0; i < datos.size(); i++) {
            datos2.add((Product) datos.get(i));
            costs.enQueue(datos.get(i));
        }
        for (int i = 0; i < datos2.size(); i++) {
            total += ((Product) datos.get(i)).getCurrentStock() * ((Product) datos.get(i)).getPrice();
        }
        costs.enQueue(totalCostos);
        costs.enQueue(total);
       // escribir(path, costs);
        return costs;
    }



   /* public static List<Object> obtenerDatos(String path) throws QueueException {
        List<Object> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            Type list = new TypeToken<HeaderLinkedQueue>() {
            }.getType();
           data=(gson.fromJson(br, list));
        } catch (IOException e) {
            new RuntimeException(e);
        }
        return data;
    }*/

}
