package br.com.sysprise.venda.service;

import br.com.sysprise.venda.model.*;
import br.com.sysprise.venda.model.itemvenda.DadosDetalhamentoItemVenda;
import br.com.sysprise.venda.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pb.ProdutoServiceGrpc;

import java.util.List;

@Service
public class VendaService {

    @GrpcClient("produto")
    private ProdutoServiceGrpc.ProdutoServiceBlockingStub produtoStub;

    @Autowired
    private GerarVenda gerarVenda;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private AtualizarItemVendaService atualizarItemVendaService;

    public Venda findVendaById(Long id) {
        return vendaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("A venda solicitada não foi encontrada!"));
    }

    public Page<DadosListagemVenda> listar(Pageable pageable){
        return vendaRepository.findAllByHabilitadoTrue(pageable).map(DadosListagemVenda::new);
    }

    public DadosDetalhamentoVenda detalhar(Long id) {
        Venda venda = this.findVendaById(id);
        List<DadosDetalhamentoItemVenda> listaDetalhamentoItensvenda = this.getListaDetalhamentoItensvenda(venda);
        return new DadosDetalhamentoVenda(venda, listaDetalhamentoItensvenda);

    }

    public DadosDetalhamentoVenda cadastrar(DadosCadastroVenda dadosCadastro) {
        Venda venda = gerarVenda.executar(dadosCadastro);
        new VerificarHistoricoCliente().verificar(venda, vendaRepository);
        List<DadosDetalhamentoItemVenda> listaDetalhamentoItensvenda = this.getListaDetalhamentoItensvenda(venda);
        return new DadosDetalhamentoVenda(venda, listaDetalhamentoItensvenda);

    }

    public void atualizarStatus(DadosAtualizarStatusVenda dadosAtualizar) {
        Venda venda = this.findVendaById(dadosAtualizar.id());

        if(!dadosAtualizar.aprovar()) {
           venda.cancelar();
           return;
        }

        venda.aprovar();
    }

    public DadosDetalhamentoVenda atualizar(DadosAtualizarVenda dadosAtualizar) {
        Venda venda = this.findVendaById(dadosAtualizar.id());
        venda.atualizarCadastro(dadosAtualizar);
        dadosAtualizar.itens().ifPresent(itensAtualizar -> atualizarItemVendaService.executar(itensAtualizar, venda));
        new VerificarHistoricoCliente().verificar(venda, vendaRepository);
        List<DadosDetalhamentoItemVenda> listaDetalhamentoItensvenda = this.getListaDetalhamentoItensvenda(venda);
        return new DadosDetalhamentoVenda(venda, listaDetalhamentoItensvenda);
    }


    public void deletar(Long id) {
        Venda venda = this.findVendaById(id);
        String statusDoPedido = venda.getStatus().toString();

        if(!statusDoPedido.equalsIgnoreCase(StatusVenda.ABERTO.toString())) {
            venda.desabilitarCadastro();
            return;
        }

        vendaRepository.delete(venda);
    }

    private List<DadosDetalhamentoItemVenda> getListaDetalhamentoItensvenda(Venda venda) {
        return venda.getItensDaVenda().stream().map(item -> {
            pb.ProdutoId produtoId = pb.ProdutoId.newBuilder().setProdutoId(item.getProdutoId()).build();
            String nomeDoProduto = produtoStub.getProductname(produtoId).getNome();
            return new DadosDetalhamentoItemVenda(item, item.getProdutoId(), nomeDoProduto);
        }).toList();
    }
}
