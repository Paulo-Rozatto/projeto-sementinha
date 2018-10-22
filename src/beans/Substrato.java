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
public class Substrato {

    private final IntegerProperty id;
    private final StringProperty nome;
    private final DoubleProperty preco;
    private String descricao;

    public Substrato() {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.preco = new SimpleDoubleProperty();
    }

    public double precificar(double quant) {
        return preco.get() * quant;
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

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getNome() {
        return nome.get();
    }

    public void setPreco(double preco) {
        this.preco.set(preco);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public double getPreco() {
        return preco.get();
    }

    public DoubleProperty precoProperty() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
