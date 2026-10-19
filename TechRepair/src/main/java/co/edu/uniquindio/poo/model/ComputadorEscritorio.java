package co.edu.uniquindio.poo.model;

public class ComputadorEscritorio extends EquipoTecnologico {

    private String tipoProcesador;
    private short memoriaRAM;
    private short capacidadAlmacenamientoGB;
    private boolean tarjetaGraficaDedicada;

    public ComputadorEscritorio(String codigo, String numeroSerie, String marca, String modelo,
                                short anoFabricacion, String estadoFisico,
                                String tipoProcesador, short memoriaRAM,
                                short capacidadAlmacenamientoGB, boolean tarjetaGraficaDedicada) {
        super(codigo, numeroSerie, marca, modelo, anoFabricacion, estadoFisico);
        this.tipoProcesador = limpiar(tipoProcesador);
        this.memoriaRAM = memoriaRAM;
        this.capacidadAlmacenamientoGB = capacidadAlmacenamientoGB;
        this.tarjetaGraficaDedicada = tarjetaGraficaDedicada;
    }

    @Override
    public String validarAtributos() {
        String resultado = super.validarAtributos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (tipoProcesador.isEmpty()) {
            return "El tipo de procesador es obligatorio.";
        }
        if (memoriaRAM <= 0 || capacidadAlmacenamientoGB <= 0) {
            return "La memoria RAM y el almacenamiento deben ser mayores que cero.";
        }
        return "OK";
    }

    @Override
    public String obtenerCategoria() {
        return CATEGORIA_ESCRITORIO;
    }

    public String getTipoProcesador() { return tipoProcesador; }
    public short getMemoriaRAM() { return memoriaRAM; }
    public short getCapacidadAlmacenamientoGB() { return capacidadAlmacenamientoGB; }
    public boolean isTarjetaGraficaDedicada() { return tarjetaGraficaDedicada; }
}