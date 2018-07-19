/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private final IntegerProperty plaId;
    private final IntegerProperty serId;
    private final StringProperty servico;
    private final StringProperty horas;

    public ServicoPrestado(int plaId, int serId, String horas) {
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
    }
    
    public int getPlaId() {
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
    
    public void setHoras(String horas) {
        this.horas.set(horas);
    }

    public String getHoras() {
        return horas.get();
    }

    public StringProperty horasProperty() {
        return horas;
    }
    
    public String getServico(){
        return servico.get();
    }
    
    public void setServico(String servico){
        this.servico.set(servico);
    }
    
    public StringProperty servicoProperty(){
        return servico;
    }
}
