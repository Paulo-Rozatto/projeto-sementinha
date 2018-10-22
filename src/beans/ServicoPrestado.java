package beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class ServicoPrestado {

    private final IntegerProperty id;
    private Plantio plantio;
    private Servico servico;
    private final StringProperty horas;

    public ServicoPrestado() {
        this.id = new SimpleIntegerProperty();
        this.horas = new SimpleStringProperty();
    }

    public double precificar() {
        double quant = Double.parseDouble(horas.get());
        return servico.precficar(quant);
    }
    
    public ServicoPrestado clone(){
        ServicoPrestado clone = new ServicoPrestado();
        clone.setId(id.get());
        clone.setPlantio(plantio);
        clone.setServico(servico);
        clone.setHoras(horas.get());
        return clone;
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Plantio getPlantio() {
        return plantio;
    }

    public void setPlantio(Plantio plantio) {
        this.plantio = plantio;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public void setHoras(String horas) {
        this.horas.set(horas);
    }

    public String getHoras() {
        return horas.get();
    }

    public StringProperty horasProperty() {
        return horas;
    }
}
