package beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author paulo
 */
public class QuebraDormencia {
    private int id;
    private StringProperty nome;
    private double fator;
    
    public QuebraDormencia(){
        this.nome = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
