package com.github.FernandoNakasone.ms_pedidos.repositories;

import com.github.FernandoNakasone.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepositoy extends JpaRepository<Pedido, Long> {
}
