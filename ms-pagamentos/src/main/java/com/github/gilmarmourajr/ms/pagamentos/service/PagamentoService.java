package com.github.FernandoNakasone.ms.pagamentos.service;

import com.github.FernandoNakasone.ms.pagamentos.dto.PagamentoDTO;
import com.github.FernandoNakasone.ms.pagamentos.entities.Pagamento;
import com.github.FernandoNakasone.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.FernandoNakasone.ms.pagamentos.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

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

}
