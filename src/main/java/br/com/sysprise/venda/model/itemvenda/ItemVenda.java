package br.com.sysprise.venda.model.itemvenda;

import br.com.sysprise.venda.model.Venda;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"venda", "produtoId"})
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Venda venda;
    private Long produtoId;
    private BigDecimal precoUnitario;
    private Double quantidade;

    public ItemVenda(Double quantidade, Long produtoId, Double precoDeVenda, Boolean vendaFracionada, Venda venda) {
        this.venda = venda;
        this.produtoId = produtoId;
        this.precoUnitario = new BigDecimal(precoDeVenda);
        this.quantidade = quantidade;
        this.verificarSePodeVenderQuantidadeFracionada(vendaFracionada);
    }

    private void verificarSePodeVenderQuantidadeFracionada(Boolean vendaFracionada) {
        if(!vendaFracionada && this.quantidade % 1 != 0)
            throw new RuntimeException("Não é possível a venda fracionada do produto " + this.produtoId);
    }

    public BigDecimal valorTotal() {
        return precoUnitario.multiply(new BigDecimal(this.quantidade));
    }

    public void atualizarCadastro(DadosAtualizarItemVenda item) {
        if(item.quantidade() != null && item.quantidade() > 0.00)
            this.quantidade = item.quantidade();
    }
}
