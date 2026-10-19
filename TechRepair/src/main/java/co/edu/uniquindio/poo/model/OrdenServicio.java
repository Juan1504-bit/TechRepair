package co.edu.uniquindio.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrdenServicio {

    private String codigo;
    private LocalDate fechaCreacion;
    private String observaciones;
    private EstadoOrden estado;
    private TechRepair ownedByTechRepair;
    private Cliente cliente;
    private ArrayList<DetalleServicio> listaDetalles;

    public OrdenServicio(String codigo, Cliente cliente, String observaciones, TechRepair ownedByTechRepair) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.ownedByTechRepair = ownedByTechRepair;
        if (observaciones == null) {
            this.observaciones = "";
        } else {
            this.observaciones = observaciones.trim();
        }
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoOrden.ABIERTA;
        this.listaDetalles = new ArrayList<>();
    }

    /**
     * Agrega una intervención sobre un equipo del cliente (un mismo equipo puede tener varios detalles).
     * Si el servicio es una reparación correctiva crea un DetalleReparacion.
     * Retorna el detalle creado o null si no fue posible crearlo.
     */
    public DetalleServicio agregarDetalle(EquipoTecnologico equipo, Servicio servicio, String descripcionFalla) {
        if (estado != EstadoOrden.ABIERTA && estado != EstadoOrden.EN_PROCESO) {
            return null;
        }
        if (equipo == null || cliente.buscarEquipo(equipo.getCodigo()) != equipo) {
            return null;
        }
        if (servicio == null || descripcionFalla == null || descripcionFalla.trim().isEmpty()) {
            return null;
        }
        String codigoDetalle = "DET-" + (listaDetalles.size() + 1);
        DetalleServicio detalle;
        if (servicio instanceof ReparacionCorrectivo) {
            detalle = new DetalleReparacion(codigoDetalle, equipo, servicio, descripcionFalla, this);
        } else {
            detalle = new DetalleServicio(codigoDetalle, equipo, servicio, descripcionFalla, this);
        }
        listaDetalles.add(detalle);
        return detalle;
    }

    public DetalleServicio buscarDetalle(String codigoDetalle) {
        if (codigoDetalle == null) {
            return null;
        }
        for (DetalleServicio d : listaDetalles) {
            if (d.getCodigo().equalsIgnoreCase(codigoDetalle.trim())) {
                return d;
            }
        }
        return null;
    }

    /** Suma el valor de todos los detalles (los cancelados valen cero). */
    public double calcularValorTotal() {
        double total = 0;
        for (DetalleServicio d : listaDetalles) {
            total = total + d.calcularValor();
        }
        return total;
    }

    /** Actualiza el estado de un detalle y sincroniza el estado de la orden. Retorna "OK" o el error. */
    public String actualizarEstadoDetalle(String codigoDetalle, EstadoOrden nuevoEstado) {
        DetalleServicio detalle = buscarDetalle(codigoDetalle);
        if (detalle == null) {
            return "No existe el detalle " + codigoDetalle + " en la orden " + codigo + ".";
        }
        if (estado == EstadoOrden.CANCELADA) {
            return "La orden está cancelada.";
        }
        String resultado = detalle.actualizarEstado(nuevoEstado);
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (estado == EstadoOrden.ABIERTA && nuevoEstado == EstadoOrden.EN_PROCESO) {
            estado = EstadoOrden.EN_PROCESO;
        }
        if (todosCancelados()) {
            estado = EstadoOrden.CANCELADA;
        } else if (estado == EstadoOrden.FINALIZADA && todosEntregadosOCancelados()) {
            estado = EstadoOrden.ENTREGADA;
        }
        return "OK";
    }

    /** Finaliza la orden si todos sus detalles están finalizados (los cancelados no cuentan). */
    public String finalizar() {
        if (estado == EstadoOrden.FINALIZADA || estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            return "La orden ya está " + estado + ".";
        }
        if (!todosDetallesFinalizados()) {
            return "No se puede finalizar: existen detalles sin finalizar.";
        }
        estado = EstadoOrden.FINALIZADA;
        return "OK";
    }

    public boolean todosDetallesFinalizados() {
        boolean hayFinalizado = false;
        for (DetalleServicio d : listaDetalles) {
            if (d.estaFinalizado()) {
                hayFinalizado = true;
            } else if (d.getEstado() != EstadoOrden.CANCELADA) {
                return false;
            }
        }
        return hayFinalizado;
    }

    private boolean todosCancelados() {
        if (listaDetalles.isEmpty()) {
            return false;
        }
        for (DetalleServicio d : listaDetalles) {
            if (d.getEstado() != EstadoOrden.CANCELADA) {
                return false;
            }
        }
        return true;
    }

    private boolean todosEntregadosOCancelados() {
        for (DetalleServicio d : listaDetalles) {
            if (d.getEstado() != EstadoOrden.ENTREGADA && d.getEstado() != EstadoOrden.CANCELADA) {
                return false;
            }
        }
        return true;
    }

    public TechRepair getOwnedByTechRepair() { return ownedByTechRepair; }
    public String getCodigo() { return codigo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public String getObservaciones() { return observaciones; }
    public EstadoOrden getEstado() { return estado; }
    public Cliente getCliente() { return cliente; }
    public ArrayList<DetalleServicio> getListaDetalles() { return listaDetalles; }

    @Override
    public String toString() {
        return codigo + " | " + fechaCreacion + " | " + estado + " | Cliente: " + cliente.getNombreCompleto();
    }
}
