package br.com.sysprise.venda.model;


import br.com.sysprise.venda.model.itemvenda.DadosCadastroItemVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;


public record DadosCadastroVenda(@NotBlank(message = "Obrigatório o preenchimento do documento")
                                 String documento,
                                 @FutureOrPresent(message = "A data de entrega deve ser atual ou futura")
                                 LocalDate dataDeEntrega,
                                 @Min(value = 0, message = "O desconto mínimo é 0.0%")
                                 @Max(value = 100, message = "O desconto máximo é de 100.00%")
                                 Double desconto,
                                 @Min(1)
                                 @NotNull(message = "Obrigatório o envio do ID do cliente")
                                 Long pessoa_id,
                                 @Valid
                                 @NotNull(message = "Obrigatório o envio dos itens do pedido")
                                 @NotEmpty(message = "Obritório ao menos um item")
                                 List<DadosCadastroItemVenda> itens) {
}
