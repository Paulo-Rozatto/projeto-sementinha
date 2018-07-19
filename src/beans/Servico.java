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
public class Servico {

    private final IntegerProperty id;
    private final StringProperty tipo;
    private final DoubleProperty preco;

    public Servico(int id, String tipo, double preco) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.preco = new SimpleDoubleProperty(preco);
    }

    public Servico(String tipo, double preco) {
        this.id = new SimpleIntegerProperty();
        this.tipo = new SimpleStringProperty(tipo);
        this.preco = new SimpleDoubleProperty(preco);
    }

    public void setId(int id) {
        if (this.id.get() == 0) {
            this.id.set(id);
        }
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public String getTipo() {
        return tipo.get();
    }

    public StringProperty tipoProperty() {
        return tipo;
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
