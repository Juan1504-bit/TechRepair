package co.edu.uniquindio.poo.model;

import java.util.ArrayList;

public class Tecnico {

    private String codigo;
    private String nombre;
    private String documento;
    private Especialidad especialidad;
    private EstadoLaboral estadoLaboral;
    private ArrayList<DetalleServicio> detallesAsignados;

    public Tecnico(String codigo, String nombre, String documento,
                   Especialidad especialidad, EstadoLaboral estadoLaboral) {
        this.codigo = limpiar(codigo);
        this.nombre = limpiar(nombre);
        this.documento = limpiar(documento);
        this.especialidad = especialidad;
        this.estadoLaboral = estadoLaboral;
        this.detallesAsignados = new ArrayList<>();
    }

    /** Retorna "OK" o el mensaje del error encontrado. */
    public String validarDatos() {
        if (codigo.isEmpty()) {
            return "El código del técnico es obligatorio.";
        }
        if (nombre.length() < 3) {
            return "El nombre del técnico es obligatorio.";
        }
        if (documento.length() < 5 || documento.length() > 15) {
            return "El documento del técnico debe tener entre 5 y 15 dígitos.";
        }
        for (int i = 0; i < documento.length(); i++) {
            char c = documento.charAt(i);
            if (c < '0' || c > '9') {
                return "El documento del técnico solo debe contener números.";
            }
        }
        if (especialidad == null || estadoLaboral == null) {
            return "La especialidad y el estado laboral son obligatorios.";
        }
        return "OK";
    }

    public boolean estaActivo() {
        return estadoLaboral == EstadoLaboral.ACTIVO;
    }

    /**
     * Autorización según la especialidad: COMPUTADOR: portátil y escritorio,
     * MOVIL: teléfono, GENERAL: todos los equipos.
     */
    public boolean estaAutorizado(EquipoTecnologico equipo) {
        String categoria = equipo.obtenerCategoria();
        if (especialidad == Especialidad.ESPECIALISTA_GENERAL) {
            return true;
        }
        if (especialidad == Especialidad.ESPECIALISTA_COMPUTADOR) {
            return categoria.equals(EquipoTecnologico.CATEGORIA_PORTATIL)
                    || categoria.equals(EquipoTecnologico.CATEGORIA_ESCRITORIO);
        }
        return categoria.equals(EquipoTecnologico.CATEGORIA_TELEFONO);
    }

    public int contarIntervencionesFinalizadas() {
        int total = 0;
        for (DetalleServicio d : detallesAsignados) {
            if (d.estaFinalizado()) {
                total++;
            }
        }
        return total;
    }

    public void agregarDetalle(DetalleServicio detalle) {
        detallesAsignados.add(detalle);
    }

    public void quitarDetalle(DetalleServicio detalle) {
        detallesAsignados.remove(detalle);
    }

    private static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public Especialidad getEspecialidad() { return especialidad; }
    public EstadoLaboral getEstadoLaboral() { return estadoLaboral; }
    public ArrayList<DetalleServicio> getDetallesAsignados() { return detallesAsignados; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + especialidad + ", " + estadoLaboral + ")";
    }
}
