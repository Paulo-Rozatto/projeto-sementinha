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
    private TipoPlantio tipoPlantio;
    private QuebraDormencia quebraDormencia;

    public Semente() {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.especie = new SimpleStringProperty();
        this.preco = new SimpleDoubleProperty();
        this.precoEmGramas = new SimpleBooleanProperty();
    }

    public double precificar(double quant) {
        double total;

        total = preco.get() * quant;
        total += total * tipoPlantio.getFator();
        if(quebraDormencia != null){
            total += total * quebraDormencia.getFator();
        }

        return total;
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
    
     public TipoPlantio getTipoPlantio() {
        return tipoPlantio;
    }

    public void setTipoPlantio(TipoPlantio tipoPlantio) {
        this.tipoPlantio = tipoPlantio;
    }

    public QuebraDormencia getQuebraDormencia() {
        return quebraDormencia;
    }

    public void setQuebraDormencia(QuebraDormencia quebraDormencia) {
        this.quebraDormencia = quebraDormencia;
    }

}
