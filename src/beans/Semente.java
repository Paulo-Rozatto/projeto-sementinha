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

    private static double plantIndireto;
    private static double quebraQuimica;
    private static double quebraFisica;
    private static double quebraEstratificacao;

    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty especie;
    private final DoubleProperty preco;
    private final BooleanProperty precoEmGramas;
    private final StringProperty tipoPlantio;
    private final StringProperty dormencia;

    public Semente() {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.especie = new SimpleStringProperty();
        this.preco = new SimpleDoubleProperty();
        this.precoEmGramas = new SimpleBooleanProperty();
        this.tipoPlantio = new SimpleStringProperty();
        this.dormencia = new SimpleStringProperty();
    }

    public static void setFatores(double plantIndireto, double quebraQuimica, double quebraFisica, double quebraEstratificacao) {
        Semente.plantIndireto = plantIndireto;
        Semente.quebraQuimica = quebraQuimica;
        Semente.quebraFisica = quebraFisica;
        Semente.quebraEstratificacao = quebraEstratificacao;
    }

    public static double[] getFatores() {
        double[] fatores = new double[4];
        fatores[0] = plantIndireto;
        fatores[1] = quebraQuimica;
        fatores[2] = quebraFisica;
        fatores[3] = quebraEstratificacao;
        return fatores;
    }

    public double precificar(double quant) {
        double total;

        total = preco.get() * quant;

        if (tipoPlantio.get().equals("Indireto")) {
            total *= plantIndireto;
        }
        switch (dormencia.get()) {
            //"Quebra Química", "Quebra Física", "Estratificação"
            case "Quebra Química":
                total *= quebraQuimica;
                break;
            case "Quebra Física":
                total *= quebraFisica;
                break;
            case "Estratificação":
                total *= quebraEstratificacao;
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
