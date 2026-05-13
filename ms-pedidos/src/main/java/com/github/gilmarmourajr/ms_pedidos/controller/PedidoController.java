package com.github.gilmarmourajr.ms_pedidos.controller;

import com.github.gilmarmourajr.ms_pedidos.Service.PedidoService;
import com.github.gilmarmourajr.ms_pedidos.dto.PedidoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping("/port")
    public String port(@Value("${local.server.port}") String porta) {
        return "Instância respondeu na porta " + porta;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos(){
        List<PedidoDTO> list = pedidoService.findAllPedidos();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@Valid @PathVariable Long id){
        PedidoDTO pedidoDTO = pedidoService.findPedidoById(id);

        return ResponseEntity.ok(pedidoDTO);
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody @Valid PedidoDTO pedidoDTO){
        pedidoDTO = pedidoService.savePedido(pedidoDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(pedidoDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(pedidoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO pedidoDTO){
        pedidoDTO = pedidoService.updatePedido(id,pedidoDTO);

        return ResponseEntity.ok(pedidoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id){
        pedidoService.deletePedidoById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{pedidoId}/pagamento/confirmado")
    public void confirmarPagamento(@PathVariable Long pedidoId){
        pedidoService.confirmarPagamento(pedidoId);
    }
}
