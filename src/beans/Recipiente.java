/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
public class Recipiente {
    private final IntegerProperty id;
    private final StringProperty nome;
    private final DoubleProperty volume;
    private final DoubleProperty preco;
    
    public Recipiente(int id, String nome,double volume, double preco){
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.volume = new SimpleDoubleProperty(volume);
        this.preco = new SimpleDoubleProperty(preco);
    }
    
    public Recipiente(String nome,double volume, double preco){
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty(nome);
        this.volume = new SimpleDoubleProperty(volume);
        this.preco = new SimpleDoubleProperty(preco);
    }
    
     public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }
    
     public void setVolume(double preco) {
        this.preco.set(preco);
    }

    public double getVolume() {
        return volume.get();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }
    
     public void setPreco(double preco) {
        this.preco.set(preco);
    }

    public double getPreco() {
        return preco.get();
    }

    public DoubleProperty precoProperty() {
        return preco;
    }
}
