package br.com.sysprise.venda.model;


import br.com.sysprise.venda.model.itemvenda.DadosAtualizarItemVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record DadosAtualizarVenda(@Min(1)
                                  @NotNull(message = "Obrigatório o envio do ID da venda")
                                  Long id,
                                  @FutureOrPresent(message = "A data de entrega deve ser atual ou futura")
                                  LocalDate dataDeEntrega,
                                  Double desconto,
                                  @Valid
                                  Optional<List<DadosAtualizarItemVenda>> itens) {
}
