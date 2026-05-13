package com.github.gilmarmourajr.ms.pagamentos.service;

import com.github.gilmarmourajr.ms.pagamentos.client.PedidoClient;
import com.github.gilmarmourajr.ms.pagamentos.dto.PagamentoDTO;
import com.github.gilmarmourajr.ms.pagamentos.entities.Pagamento;
import com.github.gilmarmourajr.ms.pagamentos.entities.Status;
import com.github.gilmarmourajr.ms.pagamentos.exceptions.PagamentoAprovadoException;
import com.github.gilmarmourajr.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.gilmarmourajr.ms.pagamentos.repositories.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllPagamentos(){
        List<Pagamento> pagamentos = pagamentoRepository.findAll();
        return pagamentos.stream().map(PagamentoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findPagamentoById(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado. ID:" + id));

        return new PagamentoDTO(pagamento);
    }

    @Transactional(readOnly = true)
    public PagamentoDTO savePagamento(PagamentoDTO pagamentoDTO){
        Pagamento pagamento = new Pagamento();
        pagamento.setStatus(Status.CRIADO);
        mapperDtoToPagamento(pagamentoDTO,pagamento);
        pagamento = pagamentoRepository.save(pagamento);

        return new PagamentoDTO(pagamento);
    }

    public void  mapperDtoToPagamento(PagamentoDTO pagamentoDTO, Pagamento pagamento){
        pagamento.setId(pagamentoDTO.getId());
        pagamento.setNome(pagamentoDTO.getNome());
        pagamento.setValor(pagamentoDTO.getValor());
        pagamento.setNumeroCartao(pagamentoDTO.getNumeroCartao());
        pagamento.setValidade(pagamentoDTO.getValidade());
        pagamento.setCodigoSeguranca(pagamentoDTO.getCodigoSeguranca());
        pagamento.setPedidoId(pagamentoDTO.getPedidoId());
    }

    @Transactional(readOnly = true)
    public PagamentoDTO updatePagamento(Long id, PagamentoDTO pagamentoDTO){
        try {
            Pagamento pagamento = pagamentoRepository.getReferenceById(id);

            if (pagamento.getStatus().equals(Status.APROVADO)) {
                throw new PagamentoAprovadoException(
                        String.format("Pagamento id %d já está APROVADO e não pode ser alterado", id)
                );
            }
            mapperDtoToPagamento(pagamentoDTO, pagamento);
            pagamento.setStatus(pagamentoDTO.getStatus());
            pagamento = pagamentoRepository.save(pagamento);

            return new PagamentoDTO(pagamento);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado. ID:" + id);
        }
    }

    @Transactional
    public void deletePagamentoById(Long id){
        if(!pagamentoRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado. ID:" + id);
        }

        pagamentoRepository.deleteById(id);

    }

    @Transactional
    public PagamentoDTO confirmarPagamentoDoPedido(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pagamento não encontrado. ID: " + id)
        );

        pagamento.setStatus(Status.APROVADO);
        pagamentoRepository.save(pagamento);
        pedidoClient.confirmarPagamento(pagamento.getPedidoId());
        return new PagamentoDTO(pagamento);
    }

}
