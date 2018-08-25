package beans;

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
    private final DoubleProperty quantSem;
    private final IntegerProperty quantRec;
    private final DoubleProperty quantSub;
    private final DoubleProperty total;
    private Semente semente;
    private Recipiente recipiente;
    private Substrato substrato;
    private  List<ServicoPrestado> servicosPrestados;

    public Plantio() {
        this.id = new SimpleIntegerProperty();
        this.data = new SimpleStringProperty();
        this.quantSem = new SimpleDoubleProperty();
        this.quantRec = new SimpleIntegerProperty();
        this.quantSub = new SimpleDoubleProperty();
        this.total = new SimpleDoubleProperty();
    }

    public double precificar() {
        double valorTotal = 0.0;
        valorTotal += semente.precificar(quantSem.get());
        valorTotal += recipiente.precificar(quantRec.get());
        valorTotal += substrato.precificar(quantSub.get());

        for (ServicoPrestado s : servicosPrestados) {
            valorTotal += s.precificar();
        }

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
        this.quantSem.set(quantSem);
    }

    public double getQuantSem() {
        return quantSem.get();
    }

    public DoubleProperty quantSemProperty() {
        return quantSem;
    }

    public void setQuantRec(int quantRec) {
        this.quantRec.set(quantRec);
    }

    public int getQuantRec() {
        return quantRec.get();
    }

    public IntegerProperty quantRecProperty() {
        return quantRec;
    }

    public void setQuantSub(double quantSub) {
        this.quantSub.set(quantSub);
    }

    public double getQuantSub() {
        return quantSub.get();
    }

    public DoubleProperty quantSubProperty() {
        return quantSub;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public List<ServicoPrestado> getServicosPrestados() {
        return servicosPrestados;
    }

    public void setServicosPrestados(List<ServicoPrestado> servicosPrestados) {
        this.servicosPrestados = servicosPrestados;
    }
        
}
