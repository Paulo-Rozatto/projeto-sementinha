/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class Plantio {
    private final IntegerProperty id;
    private final StringProperty data;
    private final DoubleProperty quantSem;
    private final IntegerProperty quantRec;
    private final DoubleProperty quantSub;
    private final DoubleProperty total;
    private final StringProperty semente;
    private final StringProperty recipiente;
    private final StringProperty substrato;
    private int sementeId;
    private int recipienteId;
    private int substratoId;
    private List<ServicoPrestado> servicos;
    
    public Plantio() {
        this.id = new SimpleIntegerProperty();
        this.data = new SimpleStringProperty();
        this.quantSem = new SimpleDoubleProperty();
        this.quantRec = new SimpleIntegerProperty();
        this.quantSub = new SimpleDoubleProperty();
        this.total = new SimpleDoubleProperty();
        this.semente = new SimpleStringProperty();
        this.recipiente = new SimpleStringProperty();
        this.substrato = new SimpleStringProperty();
    }
    
    public Plantio(int id,String data, int semente, double quantSem, int recipiente, int quantRec, int substrato, double quantSub, List<ServicoPrestado> servicos, double total){
        this.id = new SimpleIntegerProperty(id);
        this.data = new SimpleStringProperty(data);
        this.quantSem = new SimpleDoubleProperty(quantSem);
        this.quantRec = new SimpleIntegerProperty(quantRec);
        this.quantSub = new SimpleDoubleProperty(quantSub);
        this.sementeId =  semente;
        this.recipienteId = recipiente;
        this.substratoId = substrato;
        this.servicos = servicos;
        this.total = new SimpleDoubleProperty(total);
        this.semente = new SimpleStringProperty();
        this.recipiente = new SimpleStringProperty();
        this.substrato = new SimpleStringProperty();
    }
    
    public Plantio(String data, int semente, double quantSem, int recipiente, int quantRec, int substrato, double quantSub, List<ServicoPrestado> servicos, double total){
        this.id = new SimpleIntegerProperty();
        this.data = new SimpleStringProperty(data);
        this.quantSem = new SimpleDoubleProperty(quantSem);
        this.quantRec = new SimpleIntegerProperty(quantRec);
        this.quantSub = new SimpleDoubleProperty(quantSub);
        this.sementeId =  semente;
        this.recipienteId = recipiente;
        this.substratoId = substrato;
        this.servicos = servicos;
        this.total = new SimpleDoubleProperty(total);
        this.semente = new SimpleStringProperty();
        this.recipiente = new SimpleStringProperty();
        this.substrato = new SimpleStringProperty();
    }

    
    public void setId(int id){
        if(this.id.get() == 0){
            this.id.set(id);
        }
    }
    
    public int getId(){
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    
    public void setData(String data) {
        this.data.set(data);
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }
    
    public void setSemente(String semente){
        this.semente.set(semente);
    }
    
    public String getSemente(){
        return semente.get();
    }
    
    public StringProperty sementeProperty(){
        return semente;
    }
    
    public void setRecipiente(String recipiente){
        this.recipiente.set(recipiente);
    }
    
    public String getRecipiente(){
        return recipiente.get();
    }
    
    public StringProperty recipienteProperty(){
        return recipiente;
    }
    
    public void setSubstrato(String substrato){
        this.substrato.set(substrato);
    }
    
    public String getSubstrato(){
        return substrato.get();
    }
    
    public StringProperty substratoProperty(){
        return substrato;
    }
    
    public void setQuantSem(double quantSem) {
        this.quantSem.set(quantSem);
    }

    public double getQuantSem() {
        return quantSem.get();
    }

    public DoubleProperty quantSemProperty() {
        return quantSem;
    }
    
    public void setQuantRec(int quantRec) {
        this.quantRec.set(quantRec);
    }

    public int getQuantRec() {
        return quantRec.get();
    }

    public IntegerProperty quantRecProperty() {
        return quantRec;
    }
    
    public void setQuantSub(double quantSub) {
        this.quantSub.set(quantSub);
    }

    public double getQuantSub() {
        return quantSub.get();
    }

    public DoubleProperty quantSubProperty() {
        return quantSub;
    }
    
    public void setTotal(double total){
         this.total.set(total);
    }
    
    public double getTotal(){
        return total.get();
    }
    
    public DoubleProperty totalProperty(){
        return total;
    }

    public int getSementeId() {
        return sementeId;
    }

    public void setSementeId(int sementeId) {
        this.sementeId = sementeId;
    }

    public int getRecipienteId() {
        return recipienteId;
    }

    public void setRecipienteId(int recipienteId) {
        this.recipienteId = recipienteId;
    }

    public int getSubstratoId() {
        return substratoId;
    }

    public void setSubstratoId(int substratoId) {
        this.substratoId = substratoId;
    }

    public List<ServicoPrestado> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoPrestado> servicos) {
        this.servicos = servicos;
    }
}