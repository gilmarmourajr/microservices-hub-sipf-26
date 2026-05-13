package com.github.gilmarmourajr.ms_pedidos.Service;

import com.github.gilmarmourajr.ms_pedidos.dto.ItemDoPedidoDTO;
import com.github.gilmarmourajr.ms_pedidos.dto.PedidoDTO;
import com.github.gilmarmourajr.ms_pedidos.entities.ItemDoPedido;
import com.github.gilmarmourajr.ms_pedidos.entities.Pedido;
import com.github.gilmarmourajr.ms_pedidos.entities.Status;
import com.github.gilmarmourajr.ms_pedidos.exceptions.ResourceNotFoundException;
import com.github.gilmarmourajr.ms_pedidos.repositories.ItemDoPedidoRepository;
import com.github.gilmarmourajr.ms_pedidos.repositories.PedidoRepositoy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepositoy pedidoRepositoy;

    @Autowired
    private ItemDoPedidoRepository itemDoPedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos(){
        return pedidoRepositoy.findAll().stream().map(PedidoDTO :: new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findPedidoById(Long id){
        Pedido pedido = pedidoRepositoy.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID:" + id)
        );

        return new PedidoDTO(pedido);
    }

    @Transactional
    public PedidoDTO savePedido(PedidoDTO pedidoDTO){

        Pedido pedido = new Pedido();
        pedido.setData(LocalDate.now());
        pedido.setStatus(Status.CRIADO);
        mapDtoToPedido(pedidoDTO, pedido);
        pedido.calcularValorTotalDoPedido();
        pedido = pedidoRepositoy.save(pedido);

        return new PedidoDTO(pedido);
    }

    @Transactional
    public PedidoDTO updatePedido(Long id, PedidoDTO pedidoDTO){

        try {
            Pedido pedido = pedidoRepositoy.getReferenceById(id);
            pedido.getItens().clear();
            pedido.setData(LocalDate.now());
            pedido.setStatus(Status.CRIADO);
            mapDtoToPedido(pedidoDTO,pedido);
            pedido.calcularValorTotalDoPedido();
            pedido = pedidoRepositoy.save(pedido);
            return new PedidoDTO(pedido);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado. ID:" + id);
        }
    }

    @Transactional
    public void deletePedidoById(Long id){
        if(!pedidoRepositoy.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado. ID:" + id);
        }

        pedidoRepositoy.deleteById(id);
    }

    public void mapDtoToPedido(PedidoDTO pedidoDTO, Pedido pedido){
        pedido.setNome(pedidoDTO.getNome());
        pedido.setCpf(pedidoDTO.getCpf());

        for (ItemDoPedidoDTO itemDTO: pedidoDTO.getItens()){
            ItemDoPedido itemDoPedido = new ItemDoPedido();
            itemDoPedido.setQuantidade(itemDTO.getQuantidade());
            itemDoPedido.setDescricao(itemDTO.getDescricao());
            itemDoPedido.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itemDoPedido.setPedido(pedido);
            pedido.getItens().add(itemDoPedido);
        }
    }

}
