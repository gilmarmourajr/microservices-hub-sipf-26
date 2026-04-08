package com.github.FernandoNakasone.ms.pagamentos.controller;

import com.github.FernandoNakasone.ms.pagamentos.dto.PagamentoDTO;
import com.github.FernandoNakasone.ms.pagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> getAll(){
        List<PagamentoDTO> pagamentosDTOS = pagamentoService.findAllPagamentos();

        return ResponseEntity.ok(pagamentosDTOS);
    }

}
