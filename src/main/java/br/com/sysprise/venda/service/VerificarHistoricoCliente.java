package br.com.sysprise.venda.service;

import br.com.sysprise.venda.model.StatusVenda;
import br.com.sysprise.venda.model.Venda;
import br.com.sysprise.venda.repository.VendaRepository;

public class VerificarHistoricoCliente {

    public void verificar(Venda venda, VendaRepository vendaRepository) {
        long quantidadeDeRegistrosEmAberto = vendaRepository.findAllByClienteId(venda.getClienteId())
                .stream().filter(v -> v.getStatus().toString().equals(StatusVenda.AGUARDANDO_PAGAMENTO.toString()))
                .count();

        if(quantidadeDeRegistrosEmAberto > 1) {
            venda.definirObservacao("O cliente contém débitos em aberto");
            venda.cancelar();
            return;
        }

        venda.aprovar();
    }
}
