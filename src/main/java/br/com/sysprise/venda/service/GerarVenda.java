package br.com.sysprise.venda.service;


import br.com.sysprise.venda.model.DadosCadastroVenda;
import br.com.sysprise.venda.model.Venda;
import br.com.sysprise.venda.repository.VendaRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pb.PessoaServiceGrpc;

@Component
public class GerarVenda {

    @Autowired
    private GerarItemVenda gerarItemVenda;
    @Autowired
    private VendaRepository vendaRepository;
    @GrpcClient("pessoa")
    private PessoaServiceGrpc.PessoaServiceBlockingStub pessoaStub;

    public Venda executar(DadosCadastroVenda dadosCadastro) {
        boolean pessoaNaoExiste = !pessoaStub.verifyExistence(pb.PessoaId.newBuilder().setId(dadosCadastro.pessoa_id()).build()).getExiste();

        if(pessoaNaoExiste)
            throw new IllegalArgumentException("O ID da pessoa não é válido!");

        Venda venda = new Venda(dadosCadastro);
        dadosCadastro.itens().forEach(i -> gerarItemVenda.executar(i, venda));
        vendaRepository.save(venda);
        return venda;
    }


}
