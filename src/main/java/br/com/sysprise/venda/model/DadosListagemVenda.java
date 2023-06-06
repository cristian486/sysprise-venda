package br.com.sysprise.venda.model;

import java.time.LocalDate;

public record DadosListagemVenda(Long id, String documento, LocalDate dataDeEntrega, String observacao, String status) {

    public DadosListagemVenda(Venda venda) {
        this(venda.getId(), venda.getDocumento(), venda.getDataDeEntrega(), venda.getObservacao(), venda.getStatus().toString());
    }
}
