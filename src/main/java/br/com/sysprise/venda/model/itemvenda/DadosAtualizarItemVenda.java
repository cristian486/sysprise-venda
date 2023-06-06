package br.com.sysprise.venda.model.itemvenda;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record DadosAtualizarItemVenda(@Min(1)
                                      @NotNull(message = "Obrigatório o envio do ID do item da venda")
                                      Optional<Long> id,
                                      Double quantidade,
                                      Boolean remover) {

    public Boolean deveRemover() {
        return this.remover != null && this.remover;
    }
}
