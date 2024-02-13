package CriptoVision.Cripto.Repository;

import CriptoVision.Cripto.domain.Variacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariacaoRepository extends CrudRepository<Variacao, Integer> {
}
