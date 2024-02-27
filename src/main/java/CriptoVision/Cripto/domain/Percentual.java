package CriptoVision.Cripto.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "percentual_cripto")
@Entity(name = "Percentual")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Percentual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeMoeda;
    private BigDecimal percentual24h;
    private BigDecimal valor;
    private BigDecimal alerta;
    private BigDecimal variacao;

    public Integer getId() {
        return id;
    }

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

    public BigDecimal getAlerta() {
        return alerta;
    }

    public void setAlerta(BigDecimal alerta) {
        this.alerta = alerta;
    }

    public BigDecimal getVariacao() {
        return variacao;
    }

    public void setVariacao(BigDecimal variacao) {
        this.variacao = variacao;
    }

    @Override
    public String toString() {
        return "Percentual{" +
                "id=" + id +
                ", nomeMoeda='" + nomeMoeda + '\'' +
                ", percentual24h=" + percentual24h +
                ", valor=" + valor +
                ", alerta=" + alerta +
                ", variacao=" + variacao +
                '}';
    }
}
