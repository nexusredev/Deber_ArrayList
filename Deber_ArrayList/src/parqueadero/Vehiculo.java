package parqueadero;

import java.time.LocalDateTime;

public class Vehiculo {
    private String placa;
    private LocalDateTime horaIngreso;

    public Vehiculo(String placa, LocalDateTime horaIngreso) {
        this.placa = placa;
        this.horaIngreso = horaIngreso;
    }

    public String getPlaca() {
        return placa;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }
}