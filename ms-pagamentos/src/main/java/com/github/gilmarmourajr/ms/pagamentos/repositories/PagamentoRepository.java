package com.github.FernandoNakasone.ms.pagamentos.repositories;

import com.github.FernandoNakasone.ms.pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
