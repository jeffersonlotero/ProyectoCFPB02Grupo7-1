package com.proyecto.generation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {
    
    private static final String[] NOMBRES = {"Camilo", "Wendy", "Emanuel", "Jefferson", "Jose"};
    private static final String[] APELLIDOS = {"Pérez", "Gómez", "Rodríguez", "López", "Martínez"};
    
    public static void createSalesManInfoFile(int cantidadVendedores) {
        try (FileWriter writer = new FileWriter("datos/vendedores.csv")) {
            Random rand = new Random();
            for (int i = 0; i < cantidadVendedores; i++) {
                String tipoDoc = "CC";
                long id = 10000000 + rand.nextInt(90000000);
                String nombre = NOMBRES[rand.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[rand.nextInt(APELLIDOS.length)];
                writer.write(tipoDoc + ";" + id + ";" + nombre + ";" + apellido + "\n");
            }
            System.out.println("vendedores.csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al generar vendedores.csv: " + e.getMessage());
        }
    }

    public static void createProductsFile(int cantidadProductos) {
        try (FileWriter writer = new FileWriter("datos/productos.csv")) {
            Random rand = new Random();
            for (int i = 1; i <= cantidadProductos; i++) {
                String id = "P" + String.format("%03d", i);
                String nombre = "Producto" + i;
                int precio = rand.nextInt(500000) + 10000;
                writer.write(id + ";" + nombre + ";" + precio + "\n");
            }
            System.out.println("productos.csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al generar productos.csv: " + e.getMessage());
        }
    }

    public static void createSalesMenFile(int ventasAleatorias, String nombre, long id) {
        try (FileWriter writer = new FileWriter("datos/ventas_" + id + ".csv")) {
            writer.write("CC;" + id + "\n");
            Random rand = new Random();
            for (int i = 0; i < ventasAleatorias; i++) {
                String idProducto = "P" + String.format("%03d", rand.nextInt(10) + 1);
                int cantidad = rand.nextInt(5) + 1;
                writer.write(idProducto + ";" + cantidad + "\n");
            }
            System.out.println("ventas_" + id + ".csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al generar ventas_" + id + ".csv: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createSalesManInfoFile(5);
        createProductsFile(10);
        createSalesMenFile(5, "Camilo", 12345678);
    }
}

