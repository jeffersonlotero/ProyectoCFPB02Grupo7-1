package com.proyecto.generation;

import java.io.*;
import java.util.*;

public class Main {
    
    // Clase interna para representar vendedores
    // La clase Vendedor tiene información sobre el tipo de documento, número de documento, nombre y apellido del vendedor, y el total de ventas realizadas.
    private static class Vendedor {
        String tipoDoc;    // Tipo de documento del vendedor (DNI, Cédula, etc.)
        long numDoc;    // Número de documento del vendedor
        String nombres;    // Nombre(s) del vendedor
        String apellidos;    // Apellido(s) del vendedor
        double totalVentas;    // Total de ventas realizadas por el vendedor

        //Aqui tenemos el constructor que inicializa los datos del vendedor
        Vendedor(String tipoDoc, long numDoc, String nombres, String apellidos) {
            this.tipoDoc = tipoDoc;
            this.numDoc = numDoc;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.totalVentas = 0.0;
        }
        // Aqui tenemos el método que devuelve el nombre completo del vendedor
        
        String getNombreCompleto() {
            return nombres + " " + apellidos;
        }
    }
    // Importante aclarar que la clase producto tiene información sobre el ID del producto, nombre, precio y la cantidad vendida.
    private static class Producto {
        String id;    // ID único del producto
        String nombre;    // Nombre del producto
        double precio;    // Precio del producto
        int cantidadVendida;    // Cantidad de este producto vendida

         // Constructor que inicializa los datos del producto
        Producto(String id, String nombre, double precio) {
            this.id = id;    
            this.nombre = nombre;    
            this.precio = precio;    
            this.cantidadVendida = 0;    
        }
    }
    // Método principal que inicia el procesamiento de ventas y genera los reportes
    public static void main(String[] args) {
        System.out.println("=== PROCESADOR DE VENTAS ===");
        System.out.println("Iniciando procesamiento...\n");
        
        try {
            // 1. Cargar datos base
            List<Vendedor> vendedores = cargarVendedores("datos/vendedores.csv");
            List<Producto> productos = cargarProductos("datos/productos.csv");
            
            // 2. Procesar archivos de ventas
            procesarArchivosVentas("datos", vendedores, productos);
            
            // 3. Generar reportes
            generarReporteVendedores(vendedores);
            generarReporteProductos(productos);
            
            System.out.println("\n✅ Procesamiento completado con éxito!");
            System.out.println("Reportes generados en la carpeta 'datos'");
        } catch (IOException e) {
            System.err.println("\n❌ Error crítico durante el procesamiento: " + e.getMessage());
            e.printStackTrace();
        }
    }
     // Método para cargar los vendedores desde un archivo CSV
    private static List<Vendedor> cargarVendedores(String archivo) throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        System.out.println("📂 Leyendo archivo de vendedores...");
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // Leemos cada línea del archivo y la procesamos
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    try {
                         // Creamos un nuevo vendedor con los datos cargados
                        vendedores.add(new Vendedor(
                            datos[0].trim(), 
                            Long.parseLong(datos[1].trim()), 
                            datos[2].trim(), 
                            datos[3].trim()
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("⚠️ Número de documento inválido: " + datos[1]);
                    }
                }
            }
        }
        
        System.out.println("✅ " + vendedores.size() + " vendedores cargados");
        return vendedores;
    }
    
    // Método para cargar los productos desde un archivo CSV
    private static List<Producto> cargarProductos(String archivo) throws IOException {
        List<Producto> productos = new ArrayList<>();
        System.out.println("📂 Leyendo archivo de productos...");
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // Leemos cada línea del archivo y la procesamos
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 3) {
                    try {
                         // Creamos un nuevo producto con los datos cargados
                        productos.add(new Producto(
                            datos[0].trim(), 
                            datos[1].trim(), 
                            Double.parseDouble(datos[2].trim())
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("⚠️ Precio inválido para producto: " + datos[0]);
                    }
                }
            }
        }
        
        System.out.println("✅ " + productos.size() + " productos cargados");
        return productos;
    }
    
    // Método para procesar los archivos de ventas de los vendedores
    private static void procesarArchivosVentas(String directorio, List<Vendedor> vendedores, List<Producto> productos) throws IOException {
        File carpeta = new File(directorio);
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.startsWith("ventas_") && nombre.endsWith(".csv"));
        
        if (archivos == null || archivos.length == 0) {
            throw new IOException("No se encontraron archivos de ventas en el directorio");
        }
        
        System.out.println("\n🔍 Procesando " + archivos.length + " archivos de ventas...");

        // Procesamos cada archivo de ventas encontrado
        int archivosProcesados = 0;
        for (File archivo : archivos) {
            if (procesarArchivoVentas(archivo, vendedores, productos)) {
                archivosProcesados++;
            }
        }
        
        System.out.println("✅ " + archivosProcesados + "/" + archivos.length + " archivos procesados correctamente");
    }
    
    // Método para procesar cada archivo de ventas
    private static boolean procesarArchivoVentas(File archivo, List<Vendedor> vendedores, List<Producto> productos) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String primeraLinea = br.readLine();
            if (primeraLinea == null) {
                System.err.println("⚠️ Archivo vacío: " + archivo.getName());
                return false;
            }
            
            String[] infoVendedor = primeraLinea.split(";");
            if (infoVendedor.length < 2) {
                System.err.println("⚠️ Formato inválido en archivo: " + archivo.getName());
                return false;
            }
            
            String tipoDoc = infoVendedor[0].trim();
            long numDoc;
            try {
                numDoc = Long.parseLong(infoVendedor[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Número de documento inválido en archivo: " + archivo.getName());
                return false;
            }

            // Buscamos el vendedor correspondiente
            Vendedor vendedor = buscarVendedor(vendedores, tipoDoc, numDoc);
            if (vendedor == null) {
                System.err.println("⚠️ Vendedor no encontrado en archivo: " + archivo.getName());
                return false;
            }
            
            // Procesamos cada línea de ventas
            String linea;
            while ((linea = br.readLine()) != null) {
                procesarLineaVenta(linea, vendedor, productos);
            }
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error al procesar archivo " + archivo.getName() + ": " + e.getMessage());
            return false;
        }
    }

    // Método para buscar un vendedor en la lista de vendedores
    private static Vendedor buscarVendedor(List<Vendedor> vendedores, String tipoDoc, long numDoc) {
        for (Vendedor v : vendedores) {
            if (v.tipoDoc.equals(tipoDoc) && v.numDoc == numDoc) {
                return v;
            }
        }
        return null;
    }

    // Método para procesar cada línea de venta    
    private static void procesarLineaVenta(String linea, Vendedor vendedor, List<Producto> productos) {
        String[] datos = linea.split(";");
        if (datos.length < 2) {
            System.err.println("⚠️ Formato inválido en línea de venta: " + linea);
            return;
        }
        
        String idProducto = datos[0].trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(datos[1].trim());
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Cantidad inválida en línea: " + linea);
            return;
        }

        // Buscamos el producto correspondiente y actualizamos las ventas
        for (Producto p : productos) {
            if (p.id.equals(idProducto)) {
                double valorVenta = p.precio * cantidad;
                vendedor.totalVentas += valorVenta;
                p.cantidadVendida += cantidad;
                return;
            }
        }
        
        System.err.println("⚠️ Producto no encontrado: " + idProducto);
    }

    // Método para generar el reporte de productos

    private static void generarReporteVendedores(List<Vendedor> vendedores) throws IOException {
        vendedores.sort((v1, v2) -> Double.compare(v2.totalVentas, v1.totalVentas));
        String archivoSalida = "datos/reporte_vendedores.csv";
        
        try (FileWriter fw = new FileWriter(archivoSalida)) {
            fw.write("Posición;Vendedor;Documento;Total Ventas\n");
            int posicion = 1;
            for (Vendedor v : vendedores) {
                fw.write(String.format(
                    "%d;%s;%s %d;%,.2f\n",
                    posicion++,
                    v.getNombreCompleto(),
                    v.tipoDoc,
                    v.numDoc,
                    v.totalVentas
                ));
            }
        }
        System.out.println("📄 Reporte de vendedores generado: " + archivoSalida);
    }
    
    private static void generarReporteProductos(List<Producto> productos) throws IOException {
        productos.sort((p1, p2) -> Integer.compare(p2.cantidadVendida, p1.cantidadVendida));
        String archivoSalida = "datos/reporte_productos.csv";
        
        try (FileWriter fw = new FileWriter(archivoSalida)) {
            fw.write("Posición;Producto;Precio Unitario;Cantidad Vendida;Total Ventas\n");
            int posicion = 1;
            for (Producto p : productos) {
                double totalVentas = p.precio * p.cantidadVendida;
                fw.write(String.format(
                    "%d;%s;%,.2f;%d;%,.2f\n",
                    posicion++,
                    p.nombre,
                    p.precio,
                    p.cantidadVendida,
                    totalVentas
                ));
            }
        }
        System.out.println("📄 Reporte de productos generado: " + archivoSalida);
    }
}
