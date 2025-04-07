package com.proyecto.generation;

import java.io.*;
import java.util.*;

public class GenerateInfoFiles {
    
    // Datos para generación pseudoaleatoria
    private static final String[] NOMBRES = {"Camilo", "Wendy", "Emanuel", "Jefferson", "Jose", "Ana", "Luisa", "Pedro", "María", "David"};
    private static final String[] APELLIDOS = {"Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Fernandez", "Diaz", "Moreno", "Jimenez"};
    private static final String[] TIPOS_DOC = {"CC", "TI", "CE", "NIT"};
    private static final String[] PRODUCTOS_BASE = {"Laptop", "Teléfono", "Tablet", "Monitor", "Teclado", 
                                                  "Mouse", "Impresora", "Disco Duro", "Memoria USB", "Router"};
    
    /**
     * Genera un archivo CSV con información de vendedores
     * @param cantidadVendedores Número de vendedores a generar
     */
    public static void createSalesManInfoFile(int cantidadVendedores) {
        // Asegurar que el directorio existe
        new File("datos").mkdirs();
        
        try (FileWriter writer = new FileWriter("datos/vendedores.csv")) {
            Random rand = new Random();
            for (int i = 0; i < cantidadVendedores; i++) {
                String tipoDoc = TIPOS_DOC[rand.nextInt(TIPOS_DOC.length)];
                long id = 10000000L + rand.nextInt(90000000);
                String nombre = NOMBRES[rand.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[rand.nextInt(APELLIDOS.length)];
                writer.write(tipoDoc + ";" + id + ";" + nombre + ";" + apellido + "\n");
            }
<<<<<<< HEAD
            System.out.println("vendedores.csv generado con éxito.");
       } catch (IOException e) {
    System.err.println("⚠️ Error al escribir el archivo: " + e.getMessage());
    e.printStackTrace(); // Esto mostrará más detalles si algo sale mal.
}
=======
            System.out.println("✅ Archivo 'vendedores.csv' generado con éxito. (" + cantidadVendedores + " registros)");
        } catch (IOException e) {
            System.err.println("❌ Error crítico al generar 'vendedores.csv': " + e.getMessage());
            e.printStackTrace();
        }
>>>>>>> c94a03d (🔥 Segunda entrega - archivos corregidos y mejoras en GenerateInfoFiles.java y creacion de Main.java)
    }

    /**
     * Genera un archivo CSV con información de productos
     * @param cantidadProductos Número de productos a generar
     */
    public static void createProductsFile(int cantidadProductos) {
        try (FileWriter writer = new FileWriter("datos/productos.csv")) {
            Random rand = new Random();
            for (int i = 1; i <= cantidadProductos; i++) {
                String id = "P" + String.format("%03d", i);
                String nombreBase = PRODUCTOS_BASE[rand.nextInt(PRODUCTOS_BASE.length)];
                String nombre = nombreBase + " " + (rand.nextInt(5) + 1); // Ej: "Laptop 3"
                double precio = (10000 + (rand.nextDouble() * 490000)); // Precio entre 10,000 y 500,000
                writer.write(id + ";" + nombre + ";" + String.format("%.2f", precio) + "\n");
            }
<<<<<<< HEAD
            System.out.println("productos.csv generado con éxito.");
      } catch (IOException e) {
    System.err.println("⚠️ Error al escribir el archivo: " + e.getMessage());
    e.printStackTrace(); // Esto mostrará más detalles si algo sale mal.
}
=======
            System.out.println("✅ Archivo 'productos.csv' generado con éxito. (" + cantidadProductos + " productos)");
        } catch (IOException e) {
            System.err.println("❌ Error crítico al generar 'productos.csv': " + e.getMessage());
            e.printStackTrace();
        }
>>>>>>> c94a03d (🔥 Segunda entrega - archivos corregidos y mejoras en GenerateInfoFiles.java y creacion de Main.java)
    }

    /**
     * Genera un archivo CSV con ventas de un vendedor
     * @param ventasAleatorias Cantidad de transacciones a generar
     * @param tipoDoc Tipo de documento del vendedor (CC, TI, CE, NIT)
     * @param id Identificación del vendedor
     * @param nombre Nombre del vendedor (para referencia)
     */
    public static void createSalesMenFile(int ventasAleatorias, String tipoDoc, long id, String nombre) {
        try (FileWriter writer = new FileWriter("datos/ventas_" + id + ".csv")) {
            // Escribir primera línea con el tipo de documento y ID correctos
            writer.write(tipoDoc + ";" + id + "\n");
            
            Random rand = new Random();
            for (int i = 0; i < ventasAleatorias; i++) {
                String idProducto = "P" + String.format("%03d", rand.nextInt(10) + 1);
                int cantidad = rand.nextInt(10) + 1; // Entre 1 y 10 unidades
                writer.write(idProducto + ";" + cantidad + "\n");
            }
<<<<<<< HEAD
            System.out.println("ventas_" + id + ".csv generado con éxito.");
      } catch (IOException e) {
    System.err.println("⚠️ Error al escribir el archivo: " + e.getMessage());
    e.printStackTrace(); // Esto mostrará más detalles si algo sale mal.
}
=======
            System.out.println("✅ Archivo 'ventas_" + id + ".csv' generado con éxito. (" + ventasAleatorias + " ventas)");
        } catch (IOException e) {
            System.err.println("❌ Error al generar archivo de ventas para ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
>>>>>>> c94a03d (🔥 Segunda entrega - archivos corregidos y mejoras en GenerateInfoFiles.java y creacion de Main.java)
    }

    /**
     * Método principal que genera todos los archivos de prueba
     */
    public static void main(String[] args) {
        System.out.println("=== GENERADOR DE ARCHIVOS DE PRUEBA ===");
        
        // Crear estructura de directorios
        new File("datos").mkdirs();
        
        // Generar archivos base
        createSalesManInfoFile(5);  // 5 vendedores
        createProductsFile(10);     // 10 productos
        
        // Generar archivos de ventas para cada vendedor existente
        try (BufferedReader br = new BufferedReader(new FileReader("datos/vendedores.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4) {
                    String tipoDoc = datos[0];
                    long id = Long.parseLong(datos[1]);
                    String nombre = datos[2];
                    int ventas = 3 + new Random().nextInt(5); // Entre 3 y 7 ventas por vendedor
                    createSalesMenFile(ventas, tipoDoc, id, nombre);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error al leer archivo de vendedores: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n✅ Todos los archivos generados correctamente en la carpeta 'datos'");
    }
}