package CriptoVision.Cripto.Repository;

import CriptoVision.Cripto.domain.Variacao;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VariacaoRepository extends JpaRepository<Variacao, Integer> {
    public Variacao findFirstByOrderByIdDesc();
}
