package CriptoVision.Cripto.Repository;

import CriptoVision.Cripto.domain.Percentual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PercentualRepository extends JpaRepository<Percentual, Integer> {
    public Percentual findFirstByOrderByIdDesc();
    public Percentual findFirstByOrderByIdAsc();
}
