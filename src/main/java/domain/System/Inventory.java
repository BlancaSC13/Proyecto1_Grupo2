package domain.System;

import domain.TDA.AVL;
import exceptions.TreeException;
import interfaces.Tree;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import service.GestionaArchivo;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static List<Product> inventory = new ArrayList<>();
    private static final String PATH = "inventario.json";
    public static Stage stage;

    public static boolean agregaProducto(Product product, Tree tree){
        try {
            if (tree.isEmpty()){
                tree.add(product);
                GestionaArchivo.escribirProducto(tree.InOrder(), PATH);
                return true;
            }
            if (!tree.contains(product)) {
                tree.add(product);
                GestionaArchivo.escribirProducto(tree.InOrder(), PATH);
                return true;
            }
            inventory.add(product);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean eliminarProducto(Product product, Tree tree){
        try {
            tree.remove(product);
            GestionaArchivo.escribirProducto(tree.InOrder(), PATH);
            return true;
        } catch (TreeException e) {
            return false;
        }
    }

    public static boolean modificarProducto(Product product, Tree tree){
        try {
            boolean bool = tree.modify(product);
            GestionaArchivo.escribirProducto(tree.InOrder(), PATH);
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getId() == product.getId()){
                    inventory.set(i, product);
                }
            }
            return bool;
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    public static int consultarDisponibilidad(Product product){
        for (Product producto :
                inventory) {
            if (producto.getId() == product.getId()){
                return producto.getCurrentStock();
            }
        }
        return -1;
    }

    public static boolean realizarCompra(Tree tree){
        try {
            for (int i = 0; i < inventory.size(); i++) {
                    for (Object object : tree.InOrder()) {
                        OrderDetail orderDetail = ((OrderDetail)object);
                        if (inventory.get(i).getId() == orderDetail.getProduct().getId()){
                            if (inventory.get(i).getCurrentStock() >= orderDetail.getQuantity()) {
                                inventory.get(i).setCurrentStock(inventory.get(i).getCurrentStock() - orderDetail.getQuantity());
                            }else {
                                return false;
                            }
                        }
                    }
            }
            GestionaArchivo.escribirProducto(inventory, PATH);
            return true;
        } catch (TreeException e) {
            return false;
        }
    }

    public static void setInventory(List<Product> products) {
        inventory = products;
    }

    public static List<Product> getInventory() {
        return inventory;
    }
}
