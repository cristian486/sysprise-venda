package br.com.sysprise.venda.model.itemvenda;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroItemVenda(@NotNull(message = "Obrigatório o envio do ID do produto")
                                     Long produto_id,
                                     @NotNull(message = "Obrigatório o envio da quantidade do produto")
                                     Double quantidade) {
}
