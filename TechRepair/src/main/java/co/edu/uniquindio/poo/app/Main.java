package co.edu.uniquindio.poo.app;
import co.edu.uniquindio.poo.model.*;

import javax.swing.JOptionPane;

public class Main {

    private static TechRepair empresa = new TechRepair("TechRepair", "Armenia, Quindío");

    static void main() {
        for (int opcion = leerEntero(textoMenu()); opcion != 0; opcion = leerEntero(textoMenu())) {
            ejecutar(opcion);
        }
        mostrar("Hasta pronto.");
    }
    private static String textoMenu() {
        return "=== " + empresa.getNombre() + " - " + empresa.getCiudad() + " ===\n"
                + " 1. Registrar cliente\n"
                + " 2. Registrar equipo\n"
                + " 3. Registrar técnico\n"
                + " 4. Registrar repuesto\n"
                + " 5. Crear orden de servicio\n"
                + " 6. Agregar detalle a una orden\n"
                + " 7. Asignar técnico\n"
                + " 8. Registrar repuestos utilizados\n"
                + " 9. Calcular valor de un detalle\n"
                + "10. Calcular valor total de la orden\n"
                + "11. Actualizar estado de un detalle\n"
                + "12. Finalizar una orden\n"
                + "13. Consultar órdenes de un cliente\n"
                + "14. Consultar trabajos de un técnico\n"
                + "15. Consultar equipos pendientes\n"
                + "16. Generar reporte de servicios\n"
                + "17. Generar reporte de técnicos\n"
                + "18. Consultar inventario crítico\n"
                + "--- Extras ---\n"
                + "19. Registrar horas / observaciones de un detalle\n"
                + "20. Ver listados\n"
                + "21. Cargar datos de ejemplo\n"
                + " 0. Salir\n\n"
                + "Digite el número de la opción:";
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1: registrarCliente(); break;
            case 2: registrarEquipo(); break;
            case 3: registrarTecnico(); break;
            case 4: registrarRepuesto(); break;
            case 5: crearOrden(); break;
            case 6: agregarDetalle(); break;
            case 7: asignarTecnico(); break;
            case 8: registrarRepuestosUtilizados(); break;
            case 9: calcularValorDetalle(); break;
            case 10: calcularValorTotal(); break;
            case 11: actualizarEstadoDetalle(); break;
            case 12: finalizarOrden(); break;
            case 13: mostrar(empresa.consultarOrdenesDeCliente(leerTexto("Documento del cliente:"))); break;
            case 14: mostrar(empresa.consultarTrabajosDeTecnico(leerTexto("Código del técnico:"))); break;
            case 15: mostrar(empresa.consultarEquiposPendientes()); break;
            case 16: mostrar(empresa.generarReporteServicios()); break;
            case 17: mostrar(empresa.generarReporteTecnicos()); break;
            case 18: mostrar(empresa.consultarInventarioCritico()); break;
            case 19: registrarHorasYObservaciones(); break;
            case 20: verListados(); break;
            case 21: cargarDatosEjemplo(); break;
            case 0: break;
            default: mostrar("Opción no válida.");
        }
    }

    // ---------------------------------------------------------------- operaciones
    private static void registrarCliente() {
        String resultado = empresa.registrarCliente(
                leerTexto("Documento:"),
                leerTexto("Nombre completo:"),
                leerTexto("Teléfono:"),
                leerTexto("Correo electrónico:"),
                leerTexto("Dirección:"));
        mostrarResultado(resultado, "Cliente registrado correctamente.");
    }

    private static void registrarEquipo() {
        String documento = leerTexto("Documento del cliente propietario:");
        if (empresa.buscarCliente(documento) == null) {
            mostrar("ERROR: no existe un cliente con el documento " + documento + ".");
            return;
        }
        int categoria = leerEntero("Categoría del equipo:\n1. Computador portátil\n2. Computador de escritorio\n3. Teléfono inteligente");
        if (categoria < 1 || categoria > 3) {
            mostrar("ERROR: categoría no válida.");
            return;
        }
        String codigo = leerTexto("Código del equipo:");
        String serie = leerTexto("Número de serie:");
        String marca = leerTexto("Marca:");
        String modelo = leerTexto("Modelo:");
        short ano = (short) leerEntero("Año de fabricación:");
        String estadoFisico = leerTexto("Estado físico al ingresar:");

        EquipoTecnologico equipo;
        if (categoria == 1) {
            short pantalla = (short) leerEntero("Tamaño de pantalla (pulgadas):");
            short ram = (short) leerEntero("Memoria RAM (GB):");
            short almacenamiento = (short) leerEntero("Capacidad de almacenamiento (GB):");
            boolean bateria = leerSiNo("¿Tiene batería extraíble?");
            equipo = new ComputadorPortatil(codigo, serie, marca, modelo, ano, estadoFisico,
                    pantalla, ram, almacenamiento, bateria);
        } else if (categoria == 2) {
            String procesador = leerTexto("Tipo de procesador:");
            short ram = (short) leerEntero("Memoria RAM (GB):");
            short almacenamiento = (short) leerEntero("Capacidad de almacenamiento (GB):");
            boolean grafica = leerSiNo("¿Tiene tarjeta gráfica dedicada?");
            equipo = new ComputadorEscritorio(codigo, serie, marca, modelo, ano, estadoFisico,
                    procesador, ram, almacenamiento, grafica);
        } else {
            String sistema = leerTexto("Sistema operativo:");
            short almacenamiento = (short) leerEntero("Capacidad de almacenamiento (GB):");
            short bateria = (short) leerEntero("Capacidad de batería (mAh):");
            boolean cincoG = leerSiNo("¿Es compatible con 5G?");
            equipo = new TelefonoInteligente(codigo, serie, marca, modelo, ano, estadoFisico,
                    sistema, almacenamiento, bateria, cincoG);
        }
        String resultado = empresa.registrarEquipoACliente(documento, equipo);
        mostrarResultado(resultado, "Equipo registrado: " + equipo);
    }

    private static void registrarTecnico() {
        String codigo = leerTexto("Código del técnico:");
        String nombre = leerTexto("Nombre:");
        String documento = leerTexto("Documento:");
        int opcion = leerEntero("Especialidad:\n1. Computadores\n2. Dispositivos móviles\n3. Soporte general");
        Especialidad especialidad = null;
        if (opcion == 1) {
            especialidad = Especialidad.ESPECIALISTA_COMPUTADOR;
        } else if (opcion == 2) {
            especialidad = Especialidad.ESPECIALISTA_MOVIL;
        } else if (opcion == 3) {
            especialidad = Especialidad.ESPECIALISTA_GENERAL;
        }
        EstadoLaboral estado = EstadoLaboral.INACTIVO;
        if (leerSiNo("¿El técnico está activo?")) {
            estado = EstadoLaboral.ACTIVO;
        }
        String resultado = empresa.registrarTecnico(codigo, nombre, documento, especialidad, estado);
        mostrarResultado(resultado, "Técnico registrado correctamente.");
    }

    private static void registrarRepuesto() {
        String codigo = leerTexto("Código del repuesto:");
        String nombre = leerTexto("Nombre:");
        String descripcion = leerTexto("Descripción:");
        int precio = leerEntero("Precio unitario (en pesos, solo números):");
        int cantidad = leerEntero("Cantidad inicial en inventario:");
        int opcion = leerEntero("Categoría de compatibilidad:\n1. Portátil\n2. Escritorio\n3. Teléfono\n4. Universal");
        String compatibilidad = "";
        if (opcion == 1) {
            compatibilidad = EquipoTecnologico.CATEGORIA_PORTATIL;
        } else if (opcion == 2) {
            compatibilidad = EquipoTecnologico.CATEGORIA_ESCRITORIO;
        } else if (opcion == 3) {
            compatibilidad = EquipoTecnologico.CATEGORIA_TELEFONO;
        } else if (opcion == 4) {
            compatibilidad = Repuesto.UNIVERSAL;
        }
        String resultado = empresa.registrarRepuesto(codigo, nombre, descripcion, precio, cantidad, compatibilidad);
        mostrarResultado(resultado, "Repuesto registrado correctamente.");
    }

    private static void crearOrden() {
        String documento = leerTexto("Documento del cliente:");
        String observaciones = leerTexto("Observaciones generales:");
        OrdenServicio orden = empresa.crearOrdenServicio(documento, observaciones);
        if (orden == null) {
            mostrar("ERROR: no existe un cliente con el documento " + documento + ".");
        } else {
            mostrar("Orden creada: " + orden);
        }
    }

    private static void agregarDetalle() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        String listaEquipos = "";
        for (EquipoTecnologico e : orden.getCliente().getListaEquipos()) {
            listaEquipos = listaEquipos + "  " + e + "\n";
        }
        EquipoTecnologico equipo = orden.getCliente().buscarEquipo(
                leerTexto("Equipos de " + orden.getCliente().getNombreCompleto() + ":\n" + listaEquipos + "\nCódigo del equipo:"));
        if (equipo == null) {
            mostrar("ERROR: el cliente no tiene un equipo con ese código.");
            return;
        }
        String listaServicios = "";
        for (Servicio s : empresa.getListaServicios()) {
            listaServicios = listaServicios + "  " + s + "\n";
        }
        Servicio servicio = empresa.buscarServicio(leerTexto("Servicios disponibles:\n" + listaServicios + "\nCódigo del servicio:"));
        if (servicio == null) {
            mostrar("ERROR: no existe un servicio con ese código.");
            return;
        }
        String falla = leerTexto("Falla o necesidad reportada:");
        DetalleServicio detalle = orden.agregarDetalle(equipo, servicio, falla);
        if (detalle == null) {
            mostrar("ERROR: no se pudo agregar el detalle (la orden debe estar ABIERTA o EN_PROCESO y la falla no puede estar vacía).");
        } else {
            mostrar("Detalle agregado: " + detalle);
        }
    }

    private static void asignarTecnico() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        DetalleServicio detalle = pedirDetalle(orden);
        if (detalle == null) {
            return;
        }
        String listaTecnicos = "";
        for (Tecnico t : empresa.getListaTecnicos()) {
            listaTecnicos = listaTecnicos + "  " + t + "\n";
        }
        Tecnico tecnico = empresa.buscarTecnico(leerTexto("Técnicos:\n" + listaTecnicos + "\nCódigo del técnico:"));
        String resultado = detalle.asignarTecnico(tecnico);
        mostrarResultado(resultado, "Técnico asignado: " + detalle);
    }

    private static void registrarRepuestosUtilizados() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        DetalleServicio detalle = pedirDetalle(orden);
        if (detalle == null) {
            return;
        }
        if (!(detalle instanceof DetalleReparacion)) {
            mostrar("ERROR: solo las reparaciones correctivas admiten repuestos.");
            return;
        }
        DetalleReparacion reparacion = (DetalleReparacion) detalle;
        String listaRepuestos = "";
        for (Repuesto r : empresa.getListaRepuestos()) {
            listaRepuestos = listaRepuestos + "  " + r + "\n";
        }
        Repuesto repuesto = empresa.buscarRepuesto(leerTexto("Repuestos:\n" + listaRepuestos + "\nCódigo del repuesto:"));
        if (repuesto == null) {
            mostrar("ERROR: no existe un repuesto con ese código.");
            return;
        }
        int cantidad = leerEntero("Cantidad requerida:");
        String resultado = reparacion.registrarRepuesto(repuesto, cantidad);
        mostrarResultado(resultado, "Repuesto registrado.\nCosto actual de repuestos: "
                + "$" + (long) (reparacion.calcularCostoRepuestos())
                + "\nExistencias restantes de " + repuesto.getNombre() + ": " + repuesto.getCantidadDisponible());
    }

    private static void calcularValorDetalle() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        DetalleServicio detalle = pedirDetalle(orden);
        if (detalle == null) {
            return;
        }
        mostrar("Valor del detalle " + detalle.getCodigo() + ": " + "$" + (long) (detalle.calcularValor()));
    }

    private static void calcularValorTotal() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        mostrar("Valor total de la orden " + orden.getCodigo() + ": " + "$" + (long) (orden.calcularValorTotal()));
    }

    private static void actualizarEstadoDetalle() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        DetalleServicio detalle = pedirDetalle(orden);
        if (detalle == null) {
            return;
        }
        int opcion = leerEntero("Estado actual: " + detalle.getEstado()
                + "\n\nNuevo estado:\n1. EN_PROCESO\n2. FINALIZADA\n3. ENTREGADA\n4. CANCELADA");
        EstadoOrden nuevo = null;
        if (opcion == 1) {
            nuevo = EstadoOrden.EN_PROCESO;
        } else if (opcion == 2) {
            nuevo = EstadoOrden.FINALIZADA;
        } else if (opcion == 3) {
            nuevo = EstadoOrden.ENTREGADA;
        } else if (opcion == 4) {
            nuevo = EstadoOrden.CANCELADA;
        }
        String resultado = orden.actualizarEstadoDetalle(detalle.getCodigo(), nuevo);
        mostrarResultado(resultado, "Detalle actualizado: " + detalle + "\nEstado de la orden: " + orden.getEstado());
    }

    private static void finalizarOrden() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        String resultado = orden.finalizar();
        mostrarResultado(resultado, "Orden finalizada: " + orden + "\nTotal: " + "$" + (long) (orden.calcularValorTotal()));
    }

    private static void registrarHorasYObservaciones() {
        OrdenServicio orden = pedirOrden();
        if (orden == null) {
            return;
        }
        DetalleServicio detalle = pedirDetalle(orden);
        if (detalle == null) {
            return;
        }
        short horas = (short) leerEntero("Horas de trabajo:");
        String resultado = detalle.setHorasTrabajo(horas);
        if (!resultado.equals("OK")) {
            mostrar("ERROR: " + resultado);
            return;
        }
        detalle.setObservacionesTecnicas(leerTexto("Observaciones técnicas (puede dejar vacío):"));
        mostrar("Detalle actualizado: " + detalle + "\nHoras de trabajo: " + detalle.getHorasTrabajo());
    }

    private static void verListados() {
        String texto = "--- CLIENTES Y EQUIPOS ---\n";
        for (Cliente c : empresa.getListaClientes()) {
            texto = texto + c + "\n";
            for (EquipoTecnologico e : c.getListaEquipos()) {
                texto = texto + "      " + e + "\n";
            }
        }
        texto = texto + "\n--- TÉCNICOS ---\n";
        for (Tecnico t : empresa.getListaTecnicos()) {
            texto = texto + t + "\n";
        }
        texto = texto + "\n--- REPUESTOS ---\n";
        for (Repuesto r : empresa.getListaRepuestos()) {
            texto = texto + r + "\n";
        }
        texto = texto + "\n--- SERVICIOS ---\n";
        for (Servicio s : empresa.getListaServicios()) {
            texto = texto + s + "\n";
        }
        texto = texto + "\n--- ÓRDENES ---\n";
        for (OrdenServicio o : empresa.getListaOrdenes()) {
            texto = texto + o + "\n";
            for (DetalleServicio d : o.getListaDetalles()) {
                texto = texto + "      " + d + "\n";
            }
        }
        mostrar(texto);
    }

    /** Carga datos de ejemplo para probar rápidamente las funcionalidades. */
    private static void cargarDatosEjemplo() {
        if (!empresa.getListaClientes().isEmpty()) {
            mostrar("Los datos de ejemplo solo se pueden cargar cuando el sistema está vacío.");
            return;
        }
        empresa.registrarCliente("1094567890", "Laura Gómez Rendón", "3105551234", "laura.gomez@correo.com", "Cra 14 # 20-35, Armenia");
        empresa.registrarCliente("1098765432", "Carlos Pérez Ruiz", "3126667788", "carlos.perez@correo.com", "Cl 5 # 12-10, Armenia");

        ComputadorPortatil portatil = new ComputadorPortatil("EQ-001", "SN-LP-1001", "Lenovo", "ThinkPad E14",
                (short) 2021, "Rayones leves en la tapa", (short) 14, (short) 16, (short) 512, false);
        ComputadorEscritorio escritorio = new ComputadorEscritorio("EQ-002", "SN-PC-2002", "HP", "Pavilion TP01",
                (short) 2020, "Carcasa en buen estado", "Intel Core i5", (short) 8, (short) 1000, true);
        TelefonoInteligente telefono = new TelefonoInteligente("EQ-003", "SN-TL-3003", "Samsung", "Galaxy A54",
                (short) 2023, "Pantalla estrellada", "Android", (short) 128, (short) 5000, true);
        empresa.registrarEquipoACliente("1094567890", portatil);
        empresa.registrarEquipoACliente("1094567890", telefono);
        empresa.registrarEquipoACliente("1098765432", escritorio);

        empresa.registrarTecnico("T-001", "Andrés Molina", "79123456", Especialidad.ESPECIALISTA_COMPUTADOR, EstadoLaboral.ACTIVO);
        empresa.registrarTecnico("T-002", "Marcela Ortiz", "52987654", Especialidad.ESPECIALISTA_MOVIL, EstadoLaboral.ACTIVO);
        empresa.registrarTecnico("T-003", "Julián Cardona", "10456789", Especialidad.ESPECIALISTA_GENERAL, EstadoLaboral.INACTIVO);
        Tecnico t1 = empresa.buscarTecnico("T-001");
        Tecnico t2 = empresa.buscarTecnico("T-002");

        empresa.registrarRepuesto("R-001", "Pantalla 14 pulgadas", "Pantalla LED para portátil", 280000, 3, "PORTATIL");
        empresa.registrarRepuesto("R-002", "Disco SSD 512GB", "Unidad de estado sólido", 180000, 10, "UNIVERSAL");
        empresa.registrarRepuesto("R-003", "Batería para teléfono", "Batería de ion de litio", 90000, 2, "TELEFONO");
        empresa.registrarRepuesto("R-004", "Fuente de poder 500W", "Fuente ATX", 120000, 8, "ESCRITORIO");
        Repuesto pantalla = empresa.buscarRepuesto("R-001");

        OrdenServicio orden = empresa.crearOrdenServicio("1094567890", "Cliente reporta fallas en portátil y teléfono");
        Servicio diagnostico = empresa.buscarServicio("SRV-001");
        Servicio mantenimiento = empresa.buscarServicio("SRV-002");
        Servicio reparacion = empresa.buscarServicio("SRV-003");

        DetalleServicio d1 = orden.agregarDetalle(portatil, diagnostico, "El portátil no enciende");
        d1.asignarTecnico(t1);
        orden.actualizarEstadoDetalle(d1.getCodigo(), EstadoOrden.EN_PROCESO);
        orden.actualizarEstadoDetalle(d1.getCodigo(), EstadoOrden.FINALIZADA);

        DetalleServicio d2 = orden.agregarDetalle(portatil, reparacion, "Pantalla dañada");
        d2.asignarTecnico(t1);
        d2.setHorasTrabajo((short) 3);
        ((DetalleReparacion) d2).registrarRepuesto(pantalla, 1);
        orden.actualizarEstadoDetalle(d2.getCodigo(), EstadoOrden.EN_PROCESO);

        DetalleServicio d3 = orden.agregarDetalle(telefono, mantenimiento, "Teléfono lento y se calienta");
        d3.asignarTecnico(t2);

        mostrar("Datos de ejemplo cargados correctamente (orden ORD-1).");
    }

    // ---------------------------------------------------------------- auxiliares
    private static OrdenServicio pedirOrden() {
        String codigo = leerTexto("Código de la orden (ej. ORD-1):");
        OrdenServicio orden = empresa.buscarOrden(codigo);
        if (orden == null) {
            mostrar("ERROR: no existe una orden con el código " + codigo + ".");
        }
        return orden;
    }

    private static DetalleServicio pedirDetalle(OrdenServicio orden) {
        String lista = "";
        for (DetalleServicio d : orden.getListaDetalles()) {
            lista = lista + "  " + d + "\n";
        }
        String codigo = leerTexto("Detalles de la orden " + orden.getCodigo() + ":\n" + lista + "\nCódigo del detalle (ej. DET-1):");
        DetalleServicio detalle = orden.buscarDetalle(codigo);
        if (detalle == null) {
            mostrar("ERROR: no existe el detalle " + codigo + " en la orden " + orden.getCodigo() + ".");
        }
        return detalle;
    }

    private static void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    private static void mostrarResultado(String resultado, String mensajeExito) {
        if (resultado.equals("OK")) {
            mostrar(mensajeExito);
        } else {
            mostrar("ERROR: " + resultado);
        }
    }

    /** Lee un texto. Si el usuario cancela, retorna cadena vacía. */
    private static String leerTexto(String mensaje) {
        String texto = JOptionPane.showInputDialog(mensaje);
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    /**
     * Lee un número entero (solo dígitos). Si el usuario cancela retorna 0;
     * si se equivoca tres veces seguidas retorna -1.
     */
    private static int leerEntero(String mensaje) {
        String texto = JOptionPane.showInputDialog(mensaje);
        if (texto == null) {
            return 0;
        }
        texto = texto.trim();
        for (int intento = 1; intento < 3 && !esEntero(texto); intento++) {
            texto = JOptionPane.showInputDialog("Valor no válido: ingrese solo números enteros.\n\n" + mensaje);
            if (texto == null) {
                return 0;
            }
            texto = texto.trim();
        }
        if (!esEntero(texto)) {
            return -1;
        }
        return Integer.parseInt(texto);
    }

    private static boolean esEntero(String texto) {
        if (texto.isEmpty() || texto.length() > 9) {
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

    private static boolean leerSiNo(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "TechRepair", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
}

