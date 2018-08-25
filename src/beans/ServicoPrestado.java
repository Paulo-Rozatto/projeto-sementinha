package beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class ServicoPrestado {
//    private final IntegerProperty plaId;
//    private final IntegerProperty serId;
    private Plantio plantio;
    private Servico servico;
//    private final StringProperty servico;
    private final StringProperty horas;
    
    public ServicoPrestado(){
        this.horas = new SimpleStringProperty();
    }

    /*public ServicoPrestado(int plaId, int serId, String horas) {
        this.plaId = new SimpleIntegerProperty(plaId);
        this.serId = new SimpleIntegerProperty(serId);
        this.servico = new SimpleStringProperty();
        this.horas = new SimpleStringProperty(horas);
    }
    
    public ServicoPrestado( int serId, String servico, String horas) {
        this.plaId = new SimpleIntegerProperty();
        this.serId = new SimpleIntegerProperty(serId);
        this.servico = new SimpleStringProperty(servico);
        this.horas = new SimpleStringProperty(horas);
    }*/
    
    public double precificar(){
        double quant = Double.parseDouble(horas.get());
        return servico.precficar(quant);
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
    
    
    
    /*public int getPlaId() {
        return plaId.get();
    }

    public IntegerProperty plaIdProperty() {
        return plaId;
    }
    
    public int getSerId() {
        return serId.get();
    }

    public IntegerProperty serIdProperty() {
        return serId;
    }
    */
    
    public void setHoras(String horas) {
        this.horas.set(horas);
    }

    public String getHoras() {
        return horas.get();
    }

    public StringProperty horasProperty() {
        return horas;
    }
    
    /*
    public String getServico(){
        return servico.get();
    }
    
    public void setServico(String servico){
        this.servico.set(servico);
    }
    
    public StringProperty servicoProperty(){
        return servico;
    }
    */
}
