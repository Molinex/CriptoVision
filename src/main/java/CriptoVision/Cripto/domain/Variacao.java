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
    private Long id;
    String nomeCripto;
    BigDecimal precoBase;
    Double variacaoBase;
    Double valorAlerta;

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

    public Double getVariacaoBase() {
        return variacaoBase;
    }

    public void setVariacaoBase(Double variacaoBase) {
        this.variacaoBase = variacaoBase;
    }

    public Double getValorAlerta() {
        return valorAlerta;
    }

    public void setValorAlerta(Double valorAlerta) {
        this.valorAlerta = valorAlerta;
    }
}
