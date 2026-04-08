package com.github.gilmarmourajr.ms.pagamentos.repositories;

import com.github.gilmarmourajr.ms.pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
