package com.github.gilmarmourajr.ms.pagamentos.service;

import com.github.gilmarmourajr.ms.pagamentos.dto.PagamentoDTO;
import com.github.gilmarmourajr.ms.pagamentos.entities.Pagamento;
import com.github.gilmarmourajr.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.gilmarmourajr.ms.pagamentos.repositories.PagamentoRepository;
import com.github.gilmarmourajr.ms.pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existngId;
    private Long nonExistingId;

    private Pagamento pagamento;

    @BeforeEach
    void setUp(){
        existngId = 1L;
        nonExistingId = Long.MAX_VALUE;
        pagamento = Factory.createPagamento();
    }

    @Test
    void deletePagamenoByIdShouldDeleteWhenIdExists(){
        //Arrange - prepara o comportamento do mock
        Mockito.when(pagamentoRepository.existsById(existngId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existngId);

        //Verifica que o mock pagamentoRepository recebeu uma chamada ao medoto existsById
        Mockito.verify(pagamentoRepository).existsById(existngId);

        //Verifica se o metodo deleteById do repository foi chamado exatamente 1 vez (padrão)
        Mockito.verify(pagamentoRepository, Mockito.times(1)).deleteById(existngId);
    }

    @Test
    @DisplayName("deletePagamentoById deveria lançar ResourceNotFoundException quando o Id não existir")
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        //Arrange
        Mockito.when(pagamentoRepository.existsById(nonExistingId)).thenReturn(false);

        //Act + Assert
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    pagamentoService.deletePagamentoById(nonExistingId);
                });

        Mockito.verify(pagamentoRepository).existsById(nonExistingId);

        Mockito.verify(pagamentoRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    void findPagamentoByIdSouldReturnPagamentoDTOwhenIdExists(){

        //Arrangue
        Mockito.when(pagamentoRepository.findById(existngId))
                .thenReturn(Optional.of(pagamento));


        //Act
        PagamentoDTO result = pagamentoService.findPagamentoById(existngId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(),result.getValor());

        Mockito.verify(pagamentoRepository).findById(existngId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void findPagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(pagamentoRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.findPagamentoById(nonExistingId));

        Mockito.verify(pagamentoRepository).findById(nonExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository)   ;
    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo" +
            "quando chamar Salvar Pagaento" +
            "então deve gerar ID e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_themShouldPersistsPagamento(){

        pagamento.setId(null);

        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);

        PagamentoDTO result = pagamentoService.savePagamento(inputDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(),result.getId());

            Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
            Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShouldReturnPagamentoDTOWhenIdExists(){
        Long id = pagamento.getId();
        Mockito.when(pagamentoRepository.getReferenceById(id))
                .thenReturn(pagamento);
        Mockito.when(pagamentoRepository.save(any(Pagamento.class)))
                .thenReturn(pagamento);

        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(id,result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(pagamentoRepository).getReferenceById(id);
        Mockito.verify(pagamentoRepository).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShoulThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Mockito.when(pagamentoRepository.getReferenceById(nonExistingId))
                .thenThrow(EntityNotFoundException.class);

        PagamentoDTO inputDto = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.updatePagamento(nonExistingId,inputDto));

        Mockito.verify(pagamentoRepository).getReferenceById(nonExistingId);
        Mockito.verify(pagamentoRepository, Mockito.never()).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

}