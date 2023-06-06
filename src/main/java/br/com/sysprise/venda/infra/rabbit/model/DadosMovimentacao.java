package br.com.sysprise.venda.infra.rabbit.model;

public record DadosMovimentacao(Long venda_id, Long produto_id, Double quantidade, Movimentacao movimentacao) {

}