package co.edu.uniquindio.poo.model;

public class TelefonoInteligente extends EquipoTecnologico {

    private String sistemaOperativo;
    private short capacidadAlmacenamientoGB;
    private short capacidadBateriaMah;
    private boolean compatible5G;

    public TelefonoInteligente(String codigo, String numeroSerie, String marca, String modelo,
                               short anoFabricacion, String estadoFisico,
                               String sistemaOperativo, short capacidadAlmacenamientoGB,
                               short capacidadBateriaMah, boolean compatible5G) {
        super(codigo, numeroSerie, marca, modelo, anoFabricacion, estadoFisico);
        this.sistemaOperativo = limpiar(sistemaOperativo);
        this.capacidadAlmacenamientoGB = capacidadAlmacenamientoGB;
        this.capacidadBateriaMah = capacidadBateriaMah;
        this.compatible5G = compatible5G;
    }

    @Override
    public String validarAtributos() {
        String resultado = super.validarAtributos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (sistemaOperativo.isEmpty()) {
            return "El sistema operativo es obligatorio.";
        }
        if (capacidadAlmacenamientoGB <= 0 || capacidadBateriaMah <= 0) {
            return "El almacenamiento y la batería deben ser mayores que cero.";
        }
        return "OK";
    }

    @Override
    public String obtenerCategoria() {
        return CATEGORIA_TELEFONO;
    }

    public String getSistemaOperativo() { return sistemaOperativo; }
    public short getCapacidadAlmacenamientoGB() { return capacidadAlmacenamientoGB; }
    public short getCapacidadBateriaMah() { return capacidadBateriaMah; }
    public boolean isCompatible5G() { return compatible5G; }
}