package beans;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private final StringProperty status;
    private final DoubleProperty total;
    private double quantSem;
    private int quantRec;
    private double quantSub;
    private Semente semente;
    private Recipiente recipiente;
    private Substrato substrato;
    private List<ServicoPrestado> servicosPrestados;

    public Plantio() {
        this.id = new SimpleIntegerProperty();
        this.data = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.servicosPrestados = new ArrayList();
        this.total = new SimpleDoubleProperty();
    }

    public double precificar() {
        double valorTotal = 0.0;
        valorTotal += semente.precificar(quantSem);
        valorTotal += recipiente.precificar(quantRec);
        valorTotal += substrato.precificar(quantSub);

        for (ServicoPrestado s : servicosPrestados) {
            valorTotal += s.precificar();
        }

        valorTotal /= quantSem;

        total.set(valorTotal);
        return valorTotal;
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

    public void setData(String data) {
        this.data.set(data);
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public double getTotal() {
        return total.get();
    }

    public void setTotal(double value) {
        total.set(value);
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public Semente getSemente() {
        return semente;
    }

    public void setSemente(Semente semente) {
        this.semente = semente;
    }

    public Recipiente getRecipiente() {
        return recipiente;
    }

    public void setRecipiente(Recipiente recipiente) {
        this.recipiente = recipiente;
    }

    public Substrato getSubstrato() {
        return substrato;
    }

    public void setSubstrato(Substrato substrato) {
        this.substrato = substrato;
    }

    public void setQuantSem(double quantSem) {
        this.quantSem = quantSem;
    }

    public double getQuantSem() {
        return quantSem;
    }

    public void setQuantRec(int quantRec) {
        this.quantRec = quantRec;
    }

    public int getQuantRec() {
        return quantRec;
    }

    public void setQuantSub(double quantSub) {
        this.quantSub = quantSub;
    }

    public double getQuantSub() {
        return quantSub;
    }

    public List<ServicoPrestado> getServicosPrestados() {
        return servicosPrestados;
    }

    public void setServicosPrestados(List<ServicoPrestado> servicosPrestados) {
        this.servicosPrestados = servicosPrestados;
    }

}
