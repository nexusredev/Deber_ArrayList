package parqueadero;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Parqueadero {
    private static final String ARCHIVO_VEHICULOS = "vehiculos.txt";
    private static final String ARCHIVO_CAJA = "caja.txt";
    private static final double TARIFA = 0.50;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void ingresarVehiculo(String placa) {
        LocalDateTime horaIngreso = LocalDateTime.now();
        String registro = placa + "," + horaIngreso.format(FORMATO_FECHA);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_VEHICULOS, true))) {
            writer.write(registro);
            writer.newLine();
            System.out.println("Vehículo ingresado con éxito a las: " + horaIngreso.format(FORMATO_FECHA));
        } catch (IOException e) {
            System.out.println("Error al guardar en el archivo de vehículos: " + e.getMessage());
        }
    }

    public void registrarSalida(String placaBuscar) {
        File archivoVehiculos = new File(ARCHIVO_VEHICULOS);
        if (!archivoVehiculos.exists()) {
            System.out.println("No hay vehículos registrados en el parqueadero.");
            return;
        }

        List<String> vehiculosRestantes = new ArrayList<>();
        Vehiculo vehiculoSalida = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoVehiculos))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2 && partes[0].equals(placaBuscar)) {
                    LocalDateTime horaIngreso = LocalDateTime.parse(partes[1], FORMATO_FECHA);
                    vehiculoSalida = new Vehiculo(partes[0], horaIngreso);
                } else {
                    vehiculosRestantes.add(linea);
                }
            }
        } catch (IOException | java.time.format.DateTimeParseException e) {
            System.out.println("Error al leer el archivo de vehículos: " + e.getMessage());
            return;
        }

        if (vehiculoSalida != null) {
            procesarCobro(vehiculoSalida, vehiculosRestantes);
        } else {
            System.out.println("No se encontró ningún vehículo con la placa: " + placaBuscar);
        }
    }

    private void procesarCobro(Vehiculo vehiculo, List<String> vehiculosRestantes) {
        LocalDateTime horaSalida = LocalDateTime.now();
        
        Duration duracion = Duration.between(vehiculo.getHoraIngreso(), horaSalida);
        long minutos = duracion.toMinutes();
        
        if (minutos == 0) minutos = 1; 
        
        long horasACobrar = (long) Math.ceil(minutos / 60.0);
        double totalPagar = horasACobrar * TARIFA;

        System.out.println("\n--- DETALLE DE COBRO ---");
        System.out.println("Placa: " + vehiculo.getPlaca());
        System.out.println("Hora ingreso: " + vehiculo.getHoraIngreso().format(FORMATO_FECHA));
        System.out.println("Hora salida: " + horaSalida.format(FORMATO_FECHA));
        System.out.println("Tiempo total: " + minutos + " minutos (" + horasACobrar + " hora/fracción)");
        System.out.println("Total a pagar: $" + String.format("%.2f", totalPagar));

        actualizarArchivoVehiculos(vehiculosRestantes);
        registrarEnCaja(vehiculo.getPlaca(), horaSalida, totalPagar);
    }

    private void actualizarArchivoVehiculos(List<String> vehiculosRestantes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_VEHICULOS))) {
            for (String linea : vehiculosRestantes) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar vehiculos.txt");
        }
    }

    private void registrarEnCaja(String placa, LocalDateTime horaSalida, double totalPagar) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CAJA, true))) {
            String registro = placa + "," + horaSalida.format(FORMATO_FECHA) + ",$" + totalPagar;
            writer.write(registro);
            writer.newLine();
            System.out.println("Pago registrado exitosamente en caja.txt");
        } catch (IOException e) {
            System.out.println("Error al registrar en caja.txt: " + e.getMessage());
        }
    }
}