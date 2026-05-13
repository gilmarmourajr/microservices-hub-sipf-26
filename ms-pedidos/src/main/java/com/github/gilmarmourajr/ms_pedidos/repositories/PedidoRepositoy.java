package com.github.gilmarmourajr.ms_pedidos.repositories;

import com.github.gilmarmourajr.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepositoy extends JpaRepository<Pedido, Long> {
}
