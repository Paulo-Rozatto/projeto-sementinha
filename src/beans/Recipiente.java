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
    
    public Recipiente(){
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.volume = new SimpleDoubleProperty();
        this.preco = new SimpleDoubleProperty();
    }
    
    public Recipiente(int id, String nome, double volume, double preco) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.volume = new SimpleDoubleProperty(volume);
        this.preco = new SimpleDoubleProperty(preco);
    }
    
    public double precificar(double quant){
        return preco.get() * quant;
    }

    public void setId(int id){
        if(this.id.get() == 0){
            this.id.set(id);
        }
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

    public void setVolume(double volume) {
        this.volume.set(volume);
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
