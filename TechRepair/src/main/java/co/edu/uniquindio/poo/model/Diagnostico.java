package co.edu.uniquindio.poo.model;

public class Diagnostico extends Servicio {

    public static final double COSTO_FIJO = 35000;

    private double costoFijo;

    public Diagnostico(String codigo, String nombre, String descripcion) {
        super(codigo, nombre, descripcion, COSTO_FIJO);
        this.costoFijo = COSTO_FIJO;
    }

    @Override
    public double calcularCostoFinal(DetalleServicio detalle) {
        return costoFijo;
    }

    public double getCostoFijo() { return costoFijo; }
}
