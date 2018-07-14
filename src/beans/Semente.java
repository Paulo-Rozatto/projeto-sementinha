/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class Semente {

    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty especie;
    private final DoubleProperty preco;
    private final BooleanProperty precoEmGramas;
    private final StringProperty tipoPlantio;
    private final StringProperty dormencia;

    public Semente(int id, String nome, String especie, double preco, boolean precoEmGramas, String tipoPlantio, String dormencia){
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.especie = new SimpleStringProperty(especie);
        this.preco = new SimpleDoubleProperty(preco);
        this.precoEmGramas = new SimpleBooleanProperty(precoEmGramas);
        this.tipoPlantio = new SimpleStringProperty(tipoPlantio);
        this.dormencia = new SimpleStringProperty(dormencia);
    }

    public Semente(String nome, String especie, double preco, boolean precoEmGramas, String tipoPlantio, String dormencia) {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty(nome);
        this.especie = new SimpleStringProperty(especie);
        this.preco = new SimpleDoubleProperty(preco);
        this.precoEmGramas = new SimpleBooleanProperty(precoEmGramas);
        this.tipoPlantio = new SimpleStringProperty(tipoPlantio);
        this.dormencia = new SimpleStringProperty(dormencia);
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

    public void setEspecie(String especie) {
        this.especie.set(especie);
    }

    public String getEspecie() {
        return especie.get();
    }

    public StringProperty especieProperty() {
        return especie;
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

    public void setPrecoEmGramas(boolean precoEmGramas) {
        this.precoEmGramas.set(precoEmGramas);
    }

    public boolean isPrecoEmGramas() {
        return precoEmGramas.get();
    }

    public BooleanProperty precoEmGramasProperty() {
        return precoEmGramas;
    }

    public void setTipoPlantio(String tipoPlantio) {
        this.tipoPlantio.set(tipoPlantio);
    }

    public String getTipoPlantio() {
        return tipoPlantio.get();
    }

    public StringProperty tipoPlantioProperty() {
        return tipoPlantio;
    }

    public void setDomercia(String nome) {
        this.dormencia.set(nome);
    }

    public String getDormencia() {
        return dormencia.get();
    }

    public StringProperty dormenciaProperty() {
        return dormencia;
    }

}
