package co.edu.uniquindio.poo.model;

public class ComputadorPortatil extends EquipoTecnologico {

    private short tamanoPantallaPulgadas;
    private short memoriaRAM;
    private short capacidadAlmacenamiento;
    private boolean bateriaExtraible;

    public ComputadorPortatil(String codigo, String numeroSerie, String marca, String modelo,
                              short anoFabricacion, String estadoFisico,
                              short tamanoPantallaPulgadas, short memoriaRAM,
                              short capacidadAlmacenamiento, boolean bateriaExtraible) {
        super(codigo, numeroSerie, marca, modelo, anoFabricacion, estadoFisico);
        this.tamanoPantallaPulgadas = tamanoPantallaPulgadas;
        this.memoriaRAM = memoriaRAM;
        this.capacidadAlmacenamiento = capacidadAlmacenamiento;
        this.bateriaExtraible = bateriaExtraible;
    }

    @Override
    public String validarAtributos() {
        String resultado = super.validarAtributos();
        if (!resultado.equals("OK")) {
            return resultado;
        }
        if (tamanoPantallaPulgadas <= 0) {
            return "El tamaño de pantalla debe ser mayor que cero.";
        }
        if (memoriaRAM <= 0 || capacidadAlmacenamiento <= 0) {
            return "La memoria RAM y el almacenamiento deben ser mayores que cero.";
        }
        return "OK";
    }

    @Override
    public String obtenerCategoria() {
        return CATEGORIA_PORTATIL;
    }

    public short getTamanoPantallaPulgadas() { return tamanoPantallaPulgadas; }
    public short getMemoriaRAM() { return memoriaRAM; }
    public short getCapacidadAlmacenamiento() { return capacidadAlmacenamiento; }
    public boolean isBateriaExtraible() { return bateriaExtraible; }
}
