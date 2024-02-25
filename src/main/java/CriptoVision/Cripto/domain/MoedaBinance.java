package CriptoVision.Cripto.domain;

import java.math.BigDecimal;

public class MoedaBinance {

    private String nomeMoeda;
    private BigDecimal percentual24h;
    private BigDecimal valor;

    public String getNomeMoeda() {
        return nomeMoeda;
    }

    public void setNomeMoeda(String nomeMoeda) {
        this.nomeMoeda = nomeMoeda;
    }

    public BigDecimal getPercentual24h() {
        return percentual24h;
    }

    public void setPercentual24h(BigDecimal percentual24h) {
        this.percentual24h = percentual24h;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "MoedaBinance{" +
                "nomeMoeda='" + nomeMoeda + '\'' +
                ", percentual24h=" + percentual24h +
                ", valor=" + valor +
                '}';
    }
}
