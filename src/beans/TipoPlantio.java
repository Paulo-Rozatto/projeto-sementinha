package beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class TipoPlantio {
    private int id;
    private final StringProperty nome;
    private double fator;
    private String teste;
    
    public TipoPlantio(){
        this.nome = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int tp_id) {
        this.id = tp_id;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }
    
    public StringProperty nomeProperty() {
        return nome;
    }

    public double getFator() {
        return fator;
    }

    public void setFator(double fator) {
        this.fator = fator;
    }
}
