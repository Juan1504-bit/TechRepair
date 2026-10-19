package co.edu.uniquindio.poo.model;

import java.util.ArrayList;

public class TechRepair {
    private String nombre;
    private String ciudad;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Tecnico> listaTecnicos;
    private ArrayList<Servicio> listaServicios;
    private ArrayList<OrdenServicio> listaOrdenes;
    private ArrayList<Repuesto> listaRepuestos;

    public TechRepair(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.listaClientes = new ArrayList<>();
        this.listaTecnicos = new ArrayList<>();
        this.listaServicios = new ArrayList<>();
        this.listaOrdenes = new ArrayList<>();
        this.listaRepuestos = new ArrayList<>();
        // Catálogo de servicios que ofrece la empresa
        listaServicios.add(new Diagnostico("SRV-001", "Diagnóstico", "Identificación de fallas del equipo"));
        listaServicios.add(new MantenimientoPreventivo("SRV-002", "Mantenimiento preventivo", "Limpieza, revisión y optimización"));
        listaServicios.add(new ReparacionCorrectivo("SRV-003", "Reparación correctiva", "Solución de fallas identificadas"));
    }

    // ---------------------------------------------------------------- 1
    /** Registra un cliente. Retorna "OK" o el mensaje del error. */
    public String registrarCliente(String documento, String nombreCompleto, String telefono,
                                   String correo, String direccion) {
        Cliente cliente = new Cliente(documento, nombreCompleto, telefono, correo, direccion, this);
        String resultado = cliente.validarDatos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (buscarCliente(cliente.getDocumento()) != null) {
            return "Ya existe un cliente con el documento " + cliente.getDocumento() + ".";
        }
        listaClientes.add(cliente);
        return "OK";
    }

    // ---------------------------------------------------------------- 2 (apoyo)
    /** Asocia un equipo a un cliente, verificando que código y serie no estén repetidos en la empresa. */
    public String registrarEquipoACliente(String documentoCliente, EquipoTecnologico equipo) {
        Cliente cliente = buscarCliente(documentoCliente);
        if (cliente == null) {
            return "No existe un cliente con el documento " + documentoCliente + ".";
        }
        String resultado = equipo.validarAtributos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        for (Cliente c : listaClientes) {
            for (EquipoTecnologico e : c.getListaEquipos()) {
                if (e.getCodigo().equalsIgnoreCase(equipo.getCodigo())) {
                    return "El código " + equipo.getCodigo() + " ya está registrado en TechRepair.";
                }
                if (e.getNumeroSerie().equalsIgnoreCase(equipo.getNumeroSerie())) {
                    return "El número de serie " + equipo.getNumeroSerie() + " ya está registrado en TechRepair.";
                }
            }
        }
        return cliente.registrarEquipo(equipo);
    }

    // ---------------------------------------------------------------- 3
    public String registrarTecnico(String codigo, String nombre, String documento,
                                   Especialidad especialidad, EstadoLaboral estadoLaboral) {
        Tecnico tecnico = new Tecnico(codigo, nombre, documento, especialidad, estadoLaboral);
        String resultado = tecnico.validarDatos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (buscarTecnico(tecnico.getCodigo()) != null) {
            return "Ya existe un técnico con el código " + tecnico.getCodigo() + ".";
        }
        for (Tecnico t : listaTecnicos) {
            if (t.getDocumento().equals(tecnico.getDocumento())) {
                return "Ya existe un técnico con el documento " + tecnico.getDocumento() + ".";
            }
        }
        listaTecnicos.add(tecnico);
        return "OK";
    }

    // ---------------------------------------------------------------- 4
    public String registrarRepuesto(String codigo, String nombre, String descripcion,
                                    double precio, int cantidad, String compatibilidad) {
        Repuesto repuesto = new Repuesto(codigo, nombre, descripcion, precio, cantidad, compatibilidad);
        String resultado = repuesto.validar();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (buscarRepuesto(repuesto.getCodigo()) != null) {
            return "Ya existe un repuesto con el código " + repuesto.getCodigo() + ".";
        }
        listaRepuestos.add(repuesto);
        return "OK";
    }

    // ---------------------------------------------------------------- 5
    /** Crea una orden abierta para un cliente existente. Retorna la orden o null si el cliente no existe. */
    public OrdenServicio crearOrdenServicio(String documentoCliente, String observaciones) {
        Cliente cliente = buscarCliente(documentoCliente);
        if (cliente == null) {
            return null;
        }
        String codigo = "ORD-" + (listaOrdenes.size() + 1);
        OrdenServicio orden = new OrdenServicio(codigo, cliente, observaciones, this);
        listaOrdenes.add(orden);
        cliente.agregarOrden(orden);
        return orden;
    }

    // ---------------------------------------------------------------- 13
    public String consultarOrdenesDeCliente(String documentoCliente) {
        Cliente cliente = buscarCliente(documentoCliente);
        if (cliente == null) {
            return "No existe un cliente con el documento " + documentoCliente + ".";
        }
        String texto = "ÓRDENES DE " + cliente.getNombreCompleto() + "\n";
        if (cliente.getOrdenes().isEmpty()) {
            texto = texto + "\n(el cliente no tiene órdenes)\n";
        }
        for (OrdenServicio orden : cliente.getOrdenes()) {
            texto = texto + "\n" + orden + "\n";
            if (!orden.getObservaciones().isEmpty()) {
                texto = texto + "  Observaciones: " + orden.getObservaciones() + "\n";
            }
            for (DetalleServicio d : orden.getListaDetalles()) {
                d.calcularValor();
                texto = texto + "  - " + d + "\n";
            }
            texto = texto + "  TOTAL ORDEN: " + "$" + (long) (orden.calcularValorTotal()) + "\n";
        }
        return texto;
    }

    // ---------------------------------------------------------------- 14
    public String consultarTrabajosDeTecnico(String codigoTecnico) {
        Tecnico tecnico = buscarTecnico(codigoTecnico);
        if (tecnico == null) {
            return "No existe un técnico con el código " + codigoTecnico + ".";
        }
        String texto = "TRABAJOS DE " + tecnico + "\n";
        int total = 0;
        for (EstadoOrden estado : EstadoOrden.values()) {
            String grupo = "";
            int cantidad = 0;
            for (DetalleServicio d : tecnico.getDetallesAsignados()) {
                if (d.getEstado() == estado) {
                    grupo = grupo + "    - " + d.getOwnedByOrdenServicio().getCodigo() + " / " + d + "\n";
                    cantidad++;
                }
            }
            if (cantidad > 0) {
                texto = texto + "\n  " + estado + " (" + cantidad + "):\n" + grupo;
                total = total + cantidad;
            }
        }
        if (total == 0) {
            texto = texto + "\n(sin trabajos asignados)\n";
        }
        return texto;
    }

    // ---------------------------------------------------------------- 15
    public String consultarEquiposPendientes() {
        String texto = "EQUIPOS CON INTERVENCIONES SIN FINALIZAR\n";
        int total = 0;
        for (Cliente cliente : listaClientes) {
            for (EquipoTecnologico equipo : cliente.getListaEquipos()) {
                ArrayList<DetalleServicio> pendientes = equipo.identificarIntervencionesPendientes(cliente.getOrdenes());
                if (!pendientes.isEmpty()) {
                    total++;
                    texto = texto + "\n" + equipo + "\n  Cliente: " + cliente.getNombreCompleto() + "\n";
                    for (DetalleServicio d : pendientes) {
                        texto = texto + "    · " + d.getOwnedByOrdenServicio().getCodigo() + " / " + d.getCodigo() + " " + d.getServicio().getNombre()
                                + " [" + d.getEstado() + "] falla: " + d.getDescripcionFalla() + "\n";
                    }
                }
            }
        }
        if (total == 0) {
            texto = texto + "\n(no hay equipos pendientes)\n";
        }
        return texto;
    }

    // ---------------------------------------------------------------- 16
    /** Cantidad de servicios por tipo y total facturado por tipo y por categoría de equipo. */
    public String generarReporteServicios() {
        int solDiagnostico = 0;
        int solMantenimiento = 0;
        int solReparacion = 0;
        int finDiagnostico = 0;
        int finMantenimiento = 0;
        int finReparacion = 0;
        double factDiagnostico = 0;
        double factMantenimiento = 0;
        double factReparacion = 0;
        double factPortatil = 0;
        double factEscritorio = 0;
        double factTelefono = 0;

        for (OrdenServicio orden : listaOrdenes) {
            for (DetalleServicio d : orden.getListaDetalles()) {
                if (d.getEstado() != EstadoOrden.CANCELADA) {
                    Servicio s = d.getServicio();
                    boolean finalizado = d.estaFinalizado();
                    double valor = d.calcularValor();

                    if (s instanceof Diagnostico) {
                        solDiagnostico++;
                        if (finalizado) {
                            finDiagnostico++;
                            factDiagnostico = factDiagnostico + valor;
                        }
                    } else if (s instanceof MantenimientoPreventivo) {
                        solMantenimiento++;
                        if (finalizado) {
                            finMantenimiento++;
                            factMantenimiento = factMantenimiento + valor;
                        }
                    } else {
                        solReparacion++;
                        if (finalizado) {
                            finReparacion++;
                            factReparacion = factReparacion + valor;
                        }
                    }

                    if (finalizado) {
                        String categoria = d.getEquipo().obtenerCategoria();
                        if (categoria.equals(EquipoTecnologico.CATEGORIA_PORTATIL)) {
                            factPortatil = factPortatil + valor;
                        } else if (categoria.equals(EquipoTecnologico.CATEGORIA_ESCRITORIO)) {
                            factEscritorio = factEscritorio + valor;
                        } else {
                            factTelefono = factTelefono + valor;
                        }
                    }
                }
            }
        }
        double total = factDiagnostico + factMantenimiento + factReparacion;

        String texto = "REPORTE DE SERVICIOS (no incluye cancelados)\n\nPor tipo de servicio:\n";
        texto = texto + "  Diagnóstico: solicitados " + solDiagnostico + ", finalizados " + finDiagnostico
                + ", facturado $" + (long) factDiagnostico + "\n";
        texto = texto + "  Mantenimiento preventivo: solicitados " + solMantenimiento + ", finalizados " + finMantenimiento
                + ", facturado $" + (long) factMantenimiento + "\n";
        texto = texto + "  Reparación correctiva: solicitados " + solReparacion + ", finalizados " + finReparacion
                + ", facturado $" + (long) factReparacion + "\n";
        texto = texto + "\nTotal facturado por categoría de equipo:\n";
        texto = texto + "  Portátiles: $" + (long) factPortatil + "\n";
        texto = texto + "  Escritorio: $" + (long) factEscritorio + "\n";
        texto = texto + "  Teléfonos: $" + (long) factTelefono + "\n";
        texto = texto + "\nTOTAL FACTURADO: $" + (long) total + "\n";
        return texto;
    }

    // ---------------------------------------------------------------- 17
    public String generarReporteTecnicos() {
        String texto = "REPORTE DE TÉCNICOS\n";
        if (listaTecnicos.isEmpty()) {
            texto = texto + "\n(sin técnicos registrados)\n";
        }
        for (Tecnico t : listaTecnicos) {
            double valorAtendido = 0;
            for (DetalleServicio d : t.getDetallesAsignados()) {
                if (d.estaFinalizado()) {
                    valorAtendido = valorAtendido + d.calcularValor();
                }
            }
            texto = texto + "\n" + t.getCodigo() + " - " + t.getNombre()
                    + "\n    Intervenciones finalizadas: " + t.contarIntervencionesFinalizadas()
                    + "\n    Valor de servicios atendidos: " + "$" + (long) (valorAtendido) + "\n";
        }
        return texto;
    }

    // ---------------------------------------------------------------- 18
    public String consultarInventarioCritico() {
        String texto = "INVENTARIO CRÍTICO (menos de " + Repuesto.LIMITE_CRITICO + " unidades)\n";
        int total = 0;
        for (Repuesto r : listaRepuestos) {
            if (r.verificarCritico()) {
                texto = texto + "\n" + r;
                total++;
            }
        }
        if (total == 0) {
            texto = texto + "\n(no hay repuestos en nivel crítico)";
        }
        return texto;
    }

    // ---------------------------------------------------------------- búsquedas
    public Cliente buscarCliente(String documento) {
        for (Cliente c : listaClientes) {
            if (c.getDocumento().equals(documento.trim())) {
                return c;
            }
        }
        return null;
    }

    public Tecnico buscarTecnico(String codigo) {
        for (Tecnico t : listaTecnicos) {
            if (t.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return t;
            }
        }
        return null;
    }

    public Servicio buscarServicio(String codigo) {
        for (Servicio s : listaServicios) {
            if (s.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return s;
            }
        }
        return null;
    }

    public OrdenServicio buscarOrden(String codigo) {
        for (OrdenServicio o : listaOrdenes) {
            if (o.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return o;
            }
        }
        return null;
    }

    public Repuesto buscarRepuesto(String codigo) {
        for (Repuesto r : listaRepuestos) {
            if (r.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return r;
            }
        }
        return null;
    }

    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public ArrayList<Cliente> getListaClientes() { return listaClientes; }
    public ArrayList<Tecnico> getListaTecnicos() { return listaTecnicos; }
    public ArrayList<Servicio> getListaServicios() { return listaServicios; }
    public ArrayList<OrdenServicio> getListaOrdenes() { return listaOrdenes; }
    public ArrayList<Repuesto> getListaRepuestos() { return listaRepuestos; }
}

