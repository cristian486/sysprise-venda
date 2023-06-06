package br.com.sysprise.venda.model;

import br.com.sysprise.venda.model.itemvenda.DadosDetalhamentoItemVenda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhamentoVenda(Long id, String documento, String observacao, LocalDateTime dataDeCriacao, LocalDate dataDeEntrega,
                                     String status, Double desconto, BigDecimal valorTotal,
                                     List<DadosDetalhamentoItemVenda> itensDaVenda) {

    public DadosDetalhamentoVenda(Venda venda, List<DadosDetalhamentoItemVenda> itensDaVenda) {
        this(venda.getId(), venda.getDocumento(), venda.getObservacao(), venda.getDataDeCriacao(), venda.getDataDeEntrega(),
                venda.getStatus().toString(), venda.getDesconto(), venda.getValorTotal(), itensDaVenda);
    }
}
