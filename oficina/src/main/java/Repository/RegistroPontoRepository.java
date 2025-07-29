package Repository;

import entity.RegistroPonto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroPontoRepository extends CrudRepository<RegistroPonto, Long> {
}
