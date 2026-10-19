package co.edu.uniquindio.poo.model;

public class Repuesto {

    public static final String UNIVERSAL = "UNIVERSAL";
    public static final int LIMITE_CRITICO = 5;

    private String codigo;
    private String nombre;
    private String descripcion;
    private double precioPorUnidad;
    private int cantidadDisponible;
    /** Categoría de compatibilidad: PORTATIL, ESCRITORIO, TELEFONO o UNIVERSAL. */
    private String compatibilidad;

    public Repuesto(String codigo, String nombre, String descripcion, double precioPorUnidad,
                    int cantidadDisponible, String compatibilidad) {
        this.codigo = limpiar(codigo);
        this.nombre = limpiar(nombre);
        this.descripcion = limpiar(descripcion);
        this.precioPorUnidad = precioPorUnidad;
        this.cantidadDisponible = cantidadDisponible;
        this.compatibilidad = limpiar(compatibilidad).toUpperCase();
    }

    /** Retorna "OK" o el mensaje del error encontrado. */
    public String validar() {
        if (codigo.isEmpty() || nombre.isEmpty()) {
            return "El código y el nombre del repuesto son obligatorios.";
        }
        if (precioPorUnidad <= 0) {
            return "El precio unitario debe ser mayor que cero.";
        }
        if (cantidadDisponible < 0) {
            return "La cantidad disponible no puede ser negativa.";
        }
        if (!compatibilidad.equals(UNIVERSAL)
                && !compatibilidad.equals(EquipoTecnologico.CATEGORIA_PORTATIL)
                && !compatibilidad.equals(EquipoTecnologico.CATEGORIA_ESCRITORIO)
                && !compatibilidad.equals(EquipoTecnologico.CATEGORIA_TELEFONO)) {
            return "Categoría de compatibilidad no válida.";
        }
        return "OK";
    }

    public boolean verificarDisponibilidad(int cantidad) {
        return cantidad > 0 && cantidad <= cantidadDisponible;
    }

    /** Descuenta del inventario. Retorna false si la cantidad no es válida o supera las existencias. */
    public boolean descontar(int cantidad) {
        if (!verificarDisponibilidad(cantidad)) {
            return false;
        }
        cantidadDisponible = cantidadDisponible - cantidad;
        return true;
    }

    /** Devuelve unidades al inventario (cuando se cancela una reparación). */
    public void reponer(int cantidad) {
        if (cantidad > 0) {
            cantidadDisponible = cantidadDisponible + cantidad;
        }
    }

    public boolean esCompatibleCon(EquipoTecnologico equipo) {
        return compatibilidad.equals(UNIVERSAL) || compatibilidad.equals(equipo.obtenerCategoria());
    }

    /** Un repuesto es crítico si tiene menos de cinco unidades. */
    public boolean verificarCritico() {
        return cantidadDisponible < LIMITE_CRITICO;
    }

    private static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioPorUnidad() { return precioPorUnidad; }
    public int getCantidadDisponible() { return cantidadDisponible; }
    public String getCompatibilidad() { return compatibilidad; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | " + "$" + (long) (precioPorUnidad)
                + " | stock: " + cantidadDisponible + " | compatible: " + compatibilidad;
    }
}
