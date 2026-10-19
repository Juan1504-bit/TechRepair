package co.edu.uniquindio.poo.model;

import java.util.ArrayList;

public class DetalleReparacion extends DetalleServicio {

    private ArrayList<RepuestoUtilizado> repuestosUtilizados;

    public DetalleReparacion(String codigo, EquipoTecnologico equipo, Servicio servicio, String descripcionFalla,
                             OrdenServicio ownedByOrdenServicio) {
        super(codigo, equipo, servicio, descripcionFalla, ownedByOrdenServicio);
        this.repuestosUtilizados = new ArrayList<>();
    }

    /**
     * Agrega un repuesto validando estado, compatibilidad y existencias; al incorporarlo
     * descuenta la cantidad del inventario. Retorna "OK" o el mensaje del error.
     */
    public String registrarRepuesto(Repuesto repuesto, int cantidad) {
        if (repuesto == null) {
            return "Debe seleccionar un repuesto.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor que cero.";
        }
        if (!estaPendiente()) {
            return "Solo se pueden agregar repuestos a reparaciones abiertas o en proceso.";
        }
        if (!repuesto.esCompatibleCon(getEquipo())) {
            return "El repuesto '" + repuesto.getNombre() + "' (" + repuesto.getCompatibilidad()
                    + ") no es compatible con un equipo " + getEquipo().obtenerCategoria() + ".";
        }
        if (!repuesto.verificarDisponibilidad(cantidad)) {
            return "Existencias insuficientes de '" + repuesto.getNombre() + "': disponibles "
                    + repuesto.getCantidadDisponible() + ", solicitadas " + cantidad + ".";
        }
        repuesto.descontar(cantidad);

        for (RepuestoUtilizado ru : repuestosUtilizados) {
            if (ru.getRepuesto() == repuesto) {
                ru.aumentarCantidad(cantidad);
                return "OK";
            }
        }
        repuestosUtilizados.add(new RepuestoUtilizado(repuesto, cantidad, this));
        return "OK";
    }

    /** Suma cantidad x precio unitario de todos los repuestos utilizados. */
    public double calcularCostoRepuestos() {
        double total = 0;
        for (RepuestoUtilizado ru : repuestosUtilizados) {
            total = total + ru.calcularSubtotal();
        }
        return total;
    }

    /** Si la reparación se cancela, los repuestos vuelven al inventario. */
    @Override
    public String actualizarEstado(EstadoOrden nuevoEstado) {
        String resultado = super.actualizarEstado(nuevoEstado);
        if (resultado.equals("OK") && nuevoEstado == EstadoOrden.CANCELADA) {
            for (RepuestoUtilizado ru : repuestosUtilizados) {
                ru.getRepuesto().reponer(ru.getCantidad());
            }
            repuestosUtilizados.clear();
            calcularValor();
        }
        return resultado;
    }

    public ArrayList<RepuestoUtilizado> getRepuestosUtilizados() { return repuestosUtilizados; }
}
