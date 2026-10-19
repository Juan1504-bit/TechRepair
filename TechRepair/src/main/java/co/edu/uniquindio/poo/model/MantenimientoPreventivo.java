package co.edu.uniquindio.poo.model;

public class MantenimientoPreventivo extends Servicio {

    public static final double TARIFA_PORTATIL = 60000;
    public static final double TARIFA_ESCRITORIO = 70000;
    public static final double TARIFA_TELEFONO = 40000;

    private double tarifaPortatil;
    private double tarifaEscritorio;
    private double tarifaTelefono;

    public MantenimientoPreventivo(String codigo, String nombre, String descripcion) {
        // La tarifa base corresponde a la tarifa mínima (teléfono inteligente).
        super(codigo, nombre, descripcion, TARIFA_TELEFONO);
        this.tarifaPortatil = TARIFA_PORTATIL;
        this.tarifaEscritorio = TARIFA_ESCRITORIO;
        this.tarifaTelefono = TARIFA_TELEFONO;
    }

    @Override
    public double calcularCostoFinal(DetalleServicio detalle) {
        String categoria = detalle.getEquipo().obtenerCategoria();
        if (categoria.equals(EquipoTecnologico.CATEGORIA_PORTATIL)) {
            return tarifaPortatil;
        }
        if (categoria.equals(EquipoTecnologico.CATEGORIA_ESCRITORIO)) {
            return tarifaEscritorio;
        }
        return tarifaTelefono;
    }

    public double getTarifaPortatil() { return tarifaPortatil; }
    public double getTarifaEscritorio() { return tarifaEscritorio; }
    public double getTarifaTelefono() { return tarifaTelefono; }
}
