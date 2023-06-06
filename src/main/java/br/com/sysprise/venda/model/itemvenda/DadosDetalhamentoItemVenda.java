package br.com.sysprise.venda.model.itemvenda;

import java.math.BigDecimal;

public record DadosDetalhamentoItemVenda(Long id, Long produto_id, String nomeDoProduto, Double quantidade, BigDecimal valorTotal) {

    public DadosDetalhamentoItemVenda(ItemVenda itemVenda, Long produto_id, String nomeDoProduto) {
        this(itemVenda.getId(), produto_id, nomeDoProduto,
                itemVenda.getQuantidade(), itemVenda.valorTotal());
    }
}
