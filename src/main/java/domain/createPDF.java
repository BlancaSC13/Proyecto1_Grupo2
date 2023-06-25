package domain;

import com.pdfjet.*;
import controller.cliente.RealizarCompras;
import domain.System.CostsControl;
import domain.System.OrderDetail;
import domain.TDA.HeaderLinkedQueue;
import javafx.collections.ObservableList;
import service.GestionaArchivo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.List;

public class createPDF {

    public static void createInventoryReport() throws Exception {
        PDF pdf = new PDF(new BufferedOutputStream(new FileOutputStream("inventoryReport.pdf")));
        Page page = new Page(pdf, Letter.PORTRAIT);
        Font font = new Font(pdf, CoreFont.TIMES_BOLD);
        font.setSize(20);
        domain.TDA.AVL avl = new domain.TDA.AVL();
        avl = GestionaArchivo.getInventory2("inventario.json");
        TextLine titulo = new TextLine(font, "Control de Inventario");
        titulo.setLocation(215f, 100f);
        titulo.drawOn(page);
        TextLine empresa = new TextLine(font, "FrankieStore");
        empresa.setLocation(255f, 120f);
        empresa.drawOn(page);
        List<Object> datos = avl.InOrder();
        Font font2 = new Font(pdf, CoreFont.TIMES_ROMAN);
        font.setSize(14);
        TextLine text = new TextLine(font);
        float num2 = 140;
        for (int i = 0; i < datos.size(); i++) {
            String texto = datos.get(i).toString();
            text = new TextLine(font2, texto);
            text.setLocation(50f, num2);
            text.drawOn(page);
            num2+=18;
        }
        //text.drawOn(page);
        pdf.close();
    }
    public static void createSalesReport() throws Exception {
        PDF pdf = new PDF(new BufferedOutputStream(new FileOutputStream("salesReport.pdf")));
        Page page = new Page(pdf, Letter.PORTRAIT);
        Font font = new Font(pdf, CoreFont.TIMES_BOLD);
        font.setSize(20);
        RealizarCompras realizarCompras = new RealizarCompras();
        ObservableList<OrderDetail> orderDetails= realizarCompras.getItems();
        TextLine titulo = new TextLine(font, "Reporte de pedidos");
        titulo.setLocation(215f, 100f);
        titulo.drawOn(page);
        TextLine empresa = new TextLine(font, "FrankieStore");
        empresa.setLocation(255f, 120f);
        empresa.drawOn(page);
        Font font2 = new Font(pdf, CoreFont.TIMES_ROMAN);
        font.setSize(14);
        TextLine text = new TextLine(font);
        float num2 = 140;
        for (int i = 0; i < orderDetails.size(); i++) {
            String texto = orderDetails.get(i).toString();
            text = new TextLine(font2, texto);
            text.setLocation(50f, num2);
            text.drawOn(page);
            num2+=18;
        }
        //text.drawOn(page);
        pdf.close();
    }
    public static void createProductDemandReport() throws Exception {
        PDF pdf = new PDF(new BufferedOutputStream(new FileOutputStream("productDemandReporteport.pdf")));
        Page page = new Page(pdf, Letter.PORTRAIT);
        Font font = new Font(pdf, CoreFont.TIMES_BOLD);
        font.setSize(20);
        domain.TDA.AVL avl = new domain.TDA.AVL();
        avl = GestionaArchivo.getInventory2("inventario.json");//todo cambiar
        TextLine titulo = new TextLine(font, "Reporte sobre demanda de productos");
        titulo.setLocation(215f, 100f);
        titulo.drawOn(page);
        TextLine empresa = new TextLine(font, "FrankieStore");
        empresa.setLocation(255f, 120f);
        empresa.drawOn(page);
        List<Object> datos = avl.InOrder();
        Font font2 = new Font(pdf, CoreFont.TIMES_ROMAN);
        font.setSize(14);
        TextLine text = new TextLine(font);
        float num2 = 140;
        for (int i = 0; i < datos.size(); i++) {
            String texto = datos.get(i).toString();
            text = new TextLine(font2, texto);
            text.setLocation(50f, num2);
            text.drawOn(page);
            num2+=18;
        }
        //text.drawOn(page);
        pdf.close();
    }
    public static void createProductCostsReport() throws Exception {
        PDF pdf = new PDF(new BufferedOutputStream(new FileOutputStream("productCostsReport.pdf")));
        Page page = new Page(pdf, Letter.PORTRAIT);
        Font font = new Font(pdf, CoreFont.TIMES_BOLD);
        font.setSize(20);
        HeaderLinkedQueue information = new HeaderLinkedQueue();
        //System.out.println(CostsControl.obtenerDatos("costsControl.json"));


        TextLine titulo = new TextLine(font, "Control de costos");
        titulo.setLocation(235f, 100f);
        titulo.drawOn(page);

        TextLine empresa = new TextLine(font, "FrankieStore");
        empresa.setLocation(255f, 120f);
        empresa.drawOn(page);

        information = CostsControl.calculate();
        Font font2 = new Font(pdf, CoreFont.TIMES_ROMAN);
        TextLine text = new TextLine(font);
        float num2 = 140;
        int size = information.size();
        for (int i = 0; i < size; i++) {
            String texto = information.front().toString();
            information.deQueue();
            text = new TextLine(font2, texto);
            text.setLocation(30f, num2);
            text.drawOn(page);
            num2+=18;
        }
        pdf.close();
    }
}
