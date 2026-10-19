package co.edu.uniquindio.poo.model;
import java.time.LocalDate;
import java.util.ArrayList;

/** Clase abstracta que representa un equipo tecnológico recibido en el taller. */
public abstract class EquipoTecnologico {

    public static final String CATEGORIA_PORTATIL = "PORTATIL";
    public static final String CATEGORIA_ESCRITORIO = "ESCRITORIO";
    public static final String CATEGORIA_TELEFONO = "TELEFONO";

    private String codigo;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private short anoFabricacion;
    private String estadoFisico;
    private Cliente ownedByCliente;

    public EquipoTecnologico(String codigo, String numeroSerie, String marca, String modelo,
                             short anoFabricacion, String estadoFisico) {
        this.codigo = limpiar(codigo);
        this.numeroSerie = limpiar(numeroSerie);
        this.marca = limpiar(marca);
        this.modelo = limpiar(modelo);
        this.anoFabricacion = anoFabricacion;
        this.estadoFisico = limpiar(estadoFisico);
    }

    /** Valida los atributos comunes. Retorna "OK" o el mensaje del error encontrado. */
    public String validarAtributos() {
        if (codigo.isEmpty()) {
            return "El código del equipo es obligatorio.";
        }
        if (numeroSerie.isEmpty()) {
            return "El número de serie del equipo es obligatorio.";
        }
        if (marca.isEmpty() || modelo.isEmpty()) {
            return "La marca y el modelo del equipo son obligatorios.";
        }
        int anioActual = LocalDate.now().getYear();
        if (anoFabricacion < 1980 || anoFabricacion > anioActual) {
            return "El año de fabricación debe estar entre 1980 y " + anioActual + ".";
        }
        if (estadoFisico.isEmpty()) {
            return "Debe describir el estado físico del equipo al ingresar.";
        }
        return "OK";
    }

    /** Retorna la categoría del equipo (PORTATIL, ESCRITORIO o TELEFONO). */
    public abstract String obtenerCategoria();

    /** Retorna los detalles de este equipo que aún están abiertos o en proceso. */
    public ArrayList<DetalleServicio> identificarIntervencionesPendientes(ArrayList<OrdenServicio> ordenes) {
        ArrayList<DetalleServicio> pendientes = new ArrayList<>();
        for (OrdenServicio orden : ordenes) {
            for (DetalleServicio detalle : orden.getListaDetalles()) {
                if (detalle.getEquipo().getCodigo().equalsIgnoreCase(codigo) && detalle.estaPendiente()) {
                    pendientes.add(detalle);
                }
            }
        }
        return pendientes;
    }

    protected static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    public Cliente getOwnedByCliente() { return ownedByCliente; }
    public void setOwnedByCliente(Cliente ownedByCliente) { this.ownedByCliente = ownedByCliente; }
    public String getCodigo() { return codigo; }
    public String getNumeroSerie() { return numeroSerie; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public short getAnoFabricacion() { return anoFabricacion; }
    public String getEstadoFisico() { return estadoFisico; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + obtenerCategoria() + " " + marca + " " + modelo + " (serie " + numeroSerie + ")";
    }
}