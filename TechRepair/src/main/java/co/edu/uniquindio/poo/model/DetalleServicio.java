package co.edu.uniquindio.poo.model;

public class DetalleServicio {

    private String codigo;
    private String descripcionFalla;
    private short horasTrabajo;
    private String observacionesTecnicas;
    private EstadoOrden estado;
    private double valor;
    private EquipoTecnologico equipo;
    private Servicio servicio;
    private Tecnico tecnico;
    private OrdenServicio ownedByOrdenServicio;

    public DetalleServicio(String codigo, EquipoTecnologico equipo, Servicio servicio, String descripcionFalla,
                           OrdenServicio ownedByOrdenServicio) {
        this.codigo = codigo;
        this.ownedByOrdenServicio = ownedByOrdenServicio;
        this.equipo = equipo;
        this.servicio = servicio;
        this.descripcionFalla = descripcionFalla.trim();
        this.observacionesTecnicas = "";
        this.estado = EstadoOrden.ABIERTA;
        this.horasTrabajo = 0;
        this.valor = 0;
    }

    /** Asigna un técnico activo y autorizado. Retorna "OK" o el mensaje del error. */
    public String asignarTecnico(Tecnico nuevoTecnico) {
        if (nuevoTecnico == null) {
            return "Debe seleccionar un técnico.";
        }
        if (estaFinalizado() || estado == EstadoOrden.CANCELADA) {
            return "No se puede asignar técnico a una intervención " + estado + ".";
        }
        if (!nuevoTecnico.estaActivo()) {
            return "El técnico " + nuevoTecnico.getNombre() + " está inactivo.";
        }
        if (!nuevoTecnico.estaAutorizado(equipo)) {
            return "El técnico " + nuevoTecnico.getNombre() + " (" + nuevoTecnico.getEspecialidad()
                    + ") no está autorizado para equipos de categoría " + equipo.obtenerCategoria() + ".";
        }
        if (tecnico != null) {
            tecnico.quitarDetalle(this);
        }
        tecnico = nuevoTecnico;
        nuevoTecnico.agregarDetalle(this);
        return "OK";
    }

    /** Calcula (y guarda) el valor de la intervención según el tipo de servicio. */
    public double calcularValor() {
        if (estado == EstadoOrden.CANCELADA) {
            valor = 0;
        } else {
            valor = servicio.calcularCostoFinal(this);
        }
        return valor;
    }

    /** Actualiza el estado respetando el flujo permitido. Retorna "OK" o el mensaje del error. */
    public String actualizarEstado(EstadoOrden nuevoEstado) {
        if (nuevoEstado == null) {
            return "Debe seleccionar un estado.";
        }
        if (!transicionValida(estado, nuevoEstado)) {
            return "Transición no permitida: " + estado + " a " + nuevoEstado + ".";
        }
        if ((nuevoEstado == EstadoOrden.EN_PROCESO || nuevoEstado == EstadoOrden.FINALIZADA) && tecnico == null) {
            return "Debe asignar un técnico antes de pasar a " + nuevoEstado + ".";
        }
        if (nuevoEstado == EstadoOrden.FINALIZADA && servicio instanceof ReparacionCorrectivo && horasTrabajo <= 0) {
            return "Debe registrar las horas de trabajo antes de finalizar una reparación correctiva.";
        }
        estado = nuevoEstado;
        calcularValor();
        return "OK";
    }

    private boolean transicionValida(EstadoOrden actual, EstadoOrden nuevo) {
        if (actual == EstadoOrden.ABIERTA) {
            return nuevo == EstadoOrden.EN_PROCESO || nuevo == EstadoOrden.CANCELADA;
        }
        if (actual == EstadoOrden.EN_PROCESO) {
            return nuevo == EstadoOrden.FINALIZADA || nuevo == EstadoOrden.CANCELADA;
        }
        if (actual == EstadoOrden.FINALIZADA) {
            return nuevo == EstadoOrden.ENTREGADA;
        }
        return false;
    }

    public boolean estaFinalizado() {
        return estado == EstadoOrden.FINALIZADA || estado == EstadoOrden.ENTREGADA;
    }

    /** Pendiente = abierta o en proceso. */
    public boolean estaPendiente() {
        return estado == EstadoOrden.ABIERTA || estado == EstadoOrden.EN_PROCESO;
    }

    /** Retorna "OK" o el mensaje del error. */
    public String setHorasTrabajo(short horasTrabajo) {
        if (horasTrabajo < 0) {
            return "Las horas de trabajo no pueden ser negativas.";
        }
        if (!estaPendiente()) {
            return "No se pueden modificar las horas de una intervención " + estado + ".";
        }
        this.horasTrabajo = horasTrabajo;
        return "OK";
    }

    public void setObservacionesTecnicas(String observacionesTecnicas) {
        if (observacionesTecnicas == null) {
            this.observacionesTecnicas = "";
        } else {
            this.observacionesTecnicas = observacionesTecnicas.trim();
        }
    }

    public OrdenServicio getOwnedByOrdenServicio() { return ownedByOrdenServicio; }
    public String getCodigo() { return codigo; }
    public String getDescripcionFalla() { return descripcionFalla; }
    public short getHorasTrabajo() { return horasTrabajo; }
    public String getObservacionesTecnicas() { return observacionesTecnicas; }
    public EstadoOrden getEstado() { return estado; }
    public double getValor() { return valor; }
    public EquipoTecnologico getEquipo() { return equipo; }
    public Servicio getServicio() { return servicio; }
    public Tecnico getTecnico() { return tecnico; }

    @Override
    public String toString() {
        String nombreTecnico = "sin asignar";
        if (tecnico != null) {
            nombreTecnico = tecnico.getNombre();
        }
        return codigo + " | " + equipo.getMarca() + " " + equipo.getModelo() + " [" + equipo.getCodigo() + "]"
                + " | " + servicio.getNombre()
                + " | " + estado
                + " | Técnico: " + nombreTecnico
                + " | " + "$" + (long) (valor);
    }
}
