package parqueadero;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parqueadero parqueadero = new Parqueadero();
        int opcion = 0;

        while (opcion != 3) {
            System.out.println("\n--- SISTEMA DE PARQUEADERO ---");
            System.out.println("1. Ingresar vehículo");
            System.out.println("2. Registrar salida y cobrar");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la placa del vehículo: ");
                    String placaIngreso = scanner.nextLine().trim().toUpperCase();
                    if (!placaIngreso.isEmpty()) {
                        parqueadero.ingresarVehiculo(placaIngreso);
                    } else {
                        System.out.println("La placa no puede estar vacía.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese la placa del vehículo que sale: ");
                    String placaSalida = scanner.nextLine().trim().toUpperCase();
                    if (!placaSalida.isEmpty()) {
                        parqueadero.registrarSalida(placaSalida);
                    } else {
                        System.out.println("La placa no puede estar vacía.");
                    }
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }
}