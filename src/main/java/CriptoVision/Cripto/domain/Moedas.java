package CriptoVision.Cripto.domain;

import lombok.Data;

import java.util.List;

@Data
public class Moedas {

    private List<Moeda> moedas;

    public List<Moeda> getMoedas() {
        return moedas;
    }

    public void setMoedas(List<Moeda> moedas) {
        this.moedas = moedas;
    }

    @Override
    public String toString() {
        return "Moedas{" +
                "moedas=" + moedas +
                '}';
    }
}
