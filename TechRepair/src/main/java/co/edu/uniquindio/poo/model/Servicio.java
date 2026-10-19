package co.edu.uniquindio.poo.model;

public abstract class Servicio {

    private String codigo;
    private String nombre;
    private String descripcion;
    private double tarifaBase;

    public Servicio(String codigo, String nombre, String descripcion, double tarifaBase) {
        this.codigo = codigo == null ? "" : codigo.trim();
        this.nombre = nombre == null ? "" : nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.tarifaBase = tarifaBase;
    }

    /** Calcula el costo final de la intervención descrita por el detalle. */
    public abstract double calcularCostoFinal(DetalleServicio detalle);

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getTarifaBase() { return tarifaBase; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (tarifa base " + "$" + (long) (tarifaBase) + ")";
    }
}