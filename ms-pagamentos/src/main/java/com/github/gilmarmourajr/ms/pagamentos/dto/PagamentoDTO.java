package com.github.FernandoNakasone.ms.pagamentos.dto;

import com.github.FernandoNakasone.ms.pagamentos.entities.Pagamento;
import com.github.FernandoNakasone.ms.pagamentos.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagamentoDTO {

    private Long id;

    @NotNull(message = "O campo valor é obrigatório")
    @Positive(message = "O valor do pagamento deve ser um número postivo")
    private BigDecimal valor;

    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 a 50 caracteres")
    private String nome;

    @NotBlank(message = "O campo número do cartão é obrigatório")
    @Size(min = 16, max = 16, message = "O número do cartão deve ter 16 digitos")
    private String numeroCartao;

    @NotBlank(message = "O campo validade é obragatório")
    @Size(min = 5, max = 5, message = "A validade do cartão deve ter 5 caracteres")
    private String validade;

    @NotBlank(message = "O campo codído de segurança é obrigatório")
    @Size(min = 3, max = 3, message = "O código de segurança deve ter 3 digitos")
    private String codigoSeguranca;

    private Status status;

    @NotNull(message = "O campo id do pedido é obrigatório")
    @Positive(message = "O campo id do pedido deve ser positivo")
    private Long pedidoId;

    public PagamentoDTO(Pagamento pagamento){
        id = pagamento.getId();
        valor = pagamento.getValor();
        nome = pagamento.getNome();
        numeroCartao = pagamento.getNumeroCartao();
        validade = pagamento.getValidade();
        codigoSeguranca = pagamento.getCodigoSeguranca();
        status = pagamento.getStatus();
        pedidoId = pagamento.getPedidoId();
    }

}
