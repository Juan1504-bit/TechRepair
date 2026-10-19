package co.edu.uniquindio.poo.model;

public class RepuestoUtilizado {

    private int cantidad;
    private double precioPorUnidad;
    private Repuesto repuesto;
    private DetalleReparacion ownedByDetalleReparacion;

    public RepuestoUtilizado(Repuesto repuesto, int cantidad, DetalleReparacion ownedByDetalleReparacion) {
        this.repuesto = repuesto;
        this.ownedByDetalleReparacion = ownedByDetalleReparacion;
        this.cantidad = cantidad;
        this.precioPorUnidad = repuesto.getPrecioPorUnidad();
    }

    public double calcularSubtotal() {
        return cantidad * precioPorUnidad;
    }

    public void aumentarCantidad(int adicional) {
        cantidad = cantidad + adicional;
    }

    public DetalleReparacion getOwnedByDetalleReparacion() { return ownedByDetalleReparacion; }
    public int getCantidad() { return cantidad; }
    public double getPrecioPorUnidad() { return precioPorUnidad; }
    public Repuesto getRepuesto() { return repuesto; }

    @Override
    public String toString() {
        return repuesto.getNombre() + " x" + cantidad + " @ " + "$" + (long) (precioPorUnidad)
                + " = " + "$" + (long) (calcularSubtotal());
    }
}