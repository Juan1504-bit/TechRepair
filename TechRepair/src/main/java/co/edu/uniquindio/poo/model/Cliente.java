package co.edu.uniquindio.poo.model;

import java.util.ArrayList;

public class Cliente {
    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private TechRepair ownedByTechRepair;
    private ArrayList<EquipoTecnologico> listaEquipos;
    private ArrayList<OrdenServicio> ordenes;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo, String direccion,
                   TechRepair ownedByTechRepair) {
        this.documento = limpiar(documento);
        this.nombreCompleto = limpiar(nombreCompleto);
        this.telefono = limpiar(telefono);
        this.correo = limpiar(correo);
        this.direccion = limpiar(direccion);
        this.ownedByTechRepair = ownedByTechRepair;
        this.listaEquipos = new ArrayList<>();
        this.ordenes = new ArrayList<>();
    }

    /** Valida los datos personales. Retorna "OK" o el mensaje del error encontrado. */
    public String validarDatos() {
        if (!esNumero(documento, 5, 15)) {
            return "El documento debe contener entre 5 y 15 dígitos.";
        }
        if (nombreCompleto.length() < 3) {
            return "El nombre completo es obligatorio (mínimo 3 caracteres).";
        }
        if (!esNumero(telefono, 7, 10)) {
            return "El teléfono debe contener entre 7 y 10 dígitos.";
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            return "El correo electrónico no es válido (debe tener @ y punto).";
        }
        if (direccion.isEmpty()) {
            return "La dirección es obligatoria.";
        }
        return "OK";
    }

    /** Asocia un equipo al cliente. Retorna "OK" o el mensaje del error encontrado. */
    public String registrarEquipo(EquipoTecnologico equipo) {
        if (equipo == null) {
            return "El equipo no puede ser nulo.";
        }
        String resultado = equipo.validarAtributos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (equipo.getOwnedByCliente() != null) {
            return "El equipo ya pertenece a otro cliente.";
        }
        if (buscarEquipo(equipo.getCodigo()) != null) {
            return "El cliente ya tiene un equipo con el código " + equipo.getCodigo() + ".";
        }
        for (EquipoTecnologico e : listaEquipos) {
            if (e.getNumeroSerie().equalsIgnoreCase(equipo.getNumeroSerie())) {
                return "El cliente ya tiene un equipo con el número de serie " + equipo.getNumeroSerie() + ".";
            }
        }
        equipo.setOwnedByCliente(this);
        listaEquipos.add(equipo);
        return "OK";
    }

    /** Busca un equipo del cliente por su código. Retorna null si no existe. */
    public EquipoTecnologico buscarEquipo(String codigo) {
        for (EquipoTecnologico e : listaEquipos) {
            if (e.getCodigo().equalsIgnoreCase(limpiar(codigo))) {
                return e;
            }
        }
        return null;
    }

    public void agregarOrden(OrdenServicio orden) {
        ordenes.add(orden);
    }

    private boolean esNumero(String texto, int minimo, int maximo) {
        if (texto.length() < minimo || texto.length() > maximo) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    public TechRepair getOwnedByTechRepair() { return ownedByTechRepair; }
    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public ArrayList<EquipoTecnologico> getListaEquipos() { return listaEquipos; }
    public ArrayList<OrdenServicio> getOrdenes() { return ordenes; }

    @Override
    public String toString() {
        return documento + " - " + nombreCompleto + " (tel. " + telefono + ")";
    }
}
