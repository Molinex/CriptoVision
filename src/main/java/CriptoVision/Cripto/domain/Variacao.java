package CriptoVision.Cripto.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "variacao_cripto")
@Entity(name = "Variacao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Variacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String nomeCripto;
    BigDecimal precoBase;
    BigDecimal variacaoBase;
    BigDecimal valorAlerta;

    public String getNomeCripto() {
        return nomeCripto;
    }

    public void setNomeCripto(String nomeCripto) {
        this.nomeCripto = nomeCripto;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public BigDecimal getVariacaoBase() {
        return variacaoBase;
    }

    public void setVariacaoBase(BigDecimal variacaoBase) {
        this.variacaoBase = variacaoBase;
    }

    public BigDecimal getValorAlerta() {
        return valorAlerta;
    }

    public void setValorAlerta(BigDecimal valorAlerta) {
        this.valorAlerta = valorAlerta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Variacao{" +
                "id=" + id +
                ", nomeCripto='" + nomeCripto + '\'' +
                ", precoBase=" + precoBase +
                ", variacaoBase=" + variacaoBase +
                ", valorAlerta=" + valorAlerta +
                '}';
    }
}
