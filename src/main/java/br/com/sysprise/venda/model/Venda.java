package br.com.sysprise.venda.model;


import br.com.sysprise.venda.model.itemvenda.ItemVenda;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "documento")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documento;
    private String observacao;
    private LocalDateTime dataDeCriacao;
    private LocalDate dataDeEntrega;
    private Double desconto;
    private Boolean habilitado;
    @Enumerated(EnumType.STRING)
    private StatusVenda status;
    private Long clienteId;
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itensDaVenda = new ArrayList<>();

    public Venda(DadosCadastroVenda dadosCadastro) {
        this.documento = dadosCadastro.documento();
        this.dataDeCriacao = LocalDateTime.now();
        this.dataDeEntrega = dadosCadastro.dataDeEntrega();
        this.observacao = "";
        this.desconto = dadosCadastro.desconto();
        this.clienteId = dadosCadastro.pessoa_id();
        this.habilitado = Boolean.TRUE;
        this.status = StatusVenda.ABERTO;
    }

    public List<ItemVenda> getItensDaVenda() {
        return Collections.unmodifiableList(itensDaVenda);
    }

    public void adicionarItem(ItemVenda itemVenda) {
        this.itensDaVenda.add(itemVenda);
    }

    public void removerItem(ItemVenda itemVenda) {
        this.itensDaVenda.remove(itemVenda);
    }

    public Optional<ItemVenda> buscarItemVendaPorId(Long id) {
        return this.itensDaVenda.stream().filter(itemCadastrado -> itemCadastrado.getId().equals(id)).findFirst();
    }

    public void atualizarCadastro(DadosAtualizarVenda dadosAtualizar) {
        if(dadosAtualizar.dataDeEntrega() != null)
            this.dataDeEntrega = dadosAtualizar.dataDeEntrega();

        if(dadosAtualizar.desconto() != null && dadosAtualizar.desconto().doubleValue() > 0.000)
            this.desconto = dadosAtualizar.desconto();
    }

    public void desabilitarCadastro() {
        this.habilitado = Boolean.FALSE;
    }

    public BigDecimal getValorTotal() {
        BigDecimal valorDosItens = this.itensDaVenda.stream().map(ItemVenda::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal desconto = (this.desconto > 0.000d ? BigDecimal.valueOf(this.desconto / 100) : BigDecimal.ONE).setScale(2, RoundingMode.DOWN);
        BigDecimal valorDesconto = valorDosItens.multiply(desconto).setScale(3, RoundingMode.CEILING);
        return valorDosItens.subtract(valorDesconto).setScale(3, RoundingMode.CEILING);
    }

    public void atualizarStatus(StatusVenda status) {
        this.status = status;
    }

    public void aprovar() {
        this.status.aprovar(this);
    }

    public void cancelar() {
        this.status.cancelar(this);
    }

    public void definirObservacao(String observacao) {
        this.observacao += observacao;
    }
}
