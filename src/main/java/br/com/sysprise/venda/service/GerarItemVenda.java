package br.com.sysprise.venda.service;

import br.com.sysprise.venda.model.Venda;
import br.com.sysprise.venda.model.itemvenda.DadosCadastroItemVenda;
import br.com.sysprise.venda.model.itemvenda.ItemVenda;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import pb.ProdutoId;
import pb.ProdutoServiceGrpc;
import pb.ProdutoVenda;

@Component
public class GerarItemVenda {

    @GrpcClient("produto")
    private ProdutoServiceGrpc.ProdutoServiceBlockingStub produtoStub;


    public void executar(DadosCadastroItemVenda itemDaVenda, Venda venda) {
        ProdutoVenda produtoVenda = produtoStub.getProdutoVenda(ProdutoId.newBuilder().setProdutoId(itemDaVenda.produto_id()).build());

        if(produtoVenda == null)
            throw new IllegalArgumentException("O ID do produto fornecido não é válido!");

        ItemVenda itemVenda =  new ItemVenda(itemDaVenda.quantidade(), produtoVenda.getId(),
                produtoVenda.getPrecoDeVenda(), produtoVenda.getVendaFracionada(), venda);
        venda.adicionarItem(itemVenda);
    }
}
