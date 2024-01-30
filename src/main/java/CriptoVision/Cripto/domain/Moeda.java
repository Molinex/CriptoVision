package CriptoVision.Cripto.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Moeda {
    private String nome;
    private BigDecimal ultimaHora;
    private BigDecimal vinteQuatroHoras;
    private BigDecimal seteDias;
    private BigDecimal trintaDias;
    private BigDecimal precoAtual;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getUltimaHora() {
        return ultimaHora;
    }

    public void setUltimaHora(BigDecimal ultimaHora) {
        this.ultimaHora = ultimaHora;
    }

    public BigDecimal getVinteQuatroHoras() {
        return vinteQuatroHoras;
    }

    public void setVinteQuatroHoras(BigDecimal vinteQuatroHoras) {
        this.vinteQuatroHoras = vinteQuatroHoras;
    }

    public BigDecimal getSeteDias() {
        return seteDias;
    }

    public void setSeteDias(BigDecimal seteDias) {
        this.seteDias = seteDias;
    }

    public BigDecimal getTrintaDias() {
        return trintaDias;
    }

    public void setTrintaDias(BigDecimal trintaDias) {
        this.trintaDias = trintaDias;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    @Override
    public String toString() {
        return "Moeda{" +
                "nome='" + nome + '\'' +
                ", ultimaHora=" + ultimaHora +
                ", vinteQuatroHoras=" + vinteQuatroHoras +
                ", seteDias=" + seteDias +
                ", trintaDias=" + trintaDias +
                ", precoAtual=" + precoAtual +
                '}';
    }
}
