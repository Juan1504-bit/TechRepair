package co.edu.uniquindio.poo.model;

public class ReparacionCorrectivo extends Servicio {

    public static final double TARIFA_MANO_OBRA = 45000;

    private double tarifaManoObra;

    public ReparacionCorrectivo(String codigo, String nombre, String descripcion) {
        super(codigo, nombre, descripcion, TARIFA_MANO_OBRA);
        this.tarifaManoObra = TARIFA_MANO_OBRA;
    }

    @Override
    public double calcularCostoFinal(DetalleServicio detalle) {
        if (!(detalle instanceof DetalleReparacion)) {
            return 0;
        }
        DetalleReparacion reparacion = (DetalleReparacion) detalle;
        double manoObra = tarifaManoObra * reparacion.getHorasTrabajo();
        return manoObra + reparacion.calcularCostoRepuestos();
    }

    public double getTarifaManoObra() { return tarifaManoObra; }
}
