package br.com.sysprise.venda.model;

import br.com.sysprise.venda.infra.rabbit.model.Movimentacao;

public enum StatusVenda {
    ABERTO("Aberto") {
        @Override
        public void aprovar(Venda venda) {
            StatusVendaManager.publicarMensagemNoRabbitMQ(venda, Movimentacao.RESERVAR);
            StatusVendaManager.publicarMensagem("cobranca", venda.getClienteId(), venda.getValorTotal());
            venda.atualizarStatus(AGUARDANDO_PAGAMENTO);
        }

        @Override
        public void cancelar(Venda venda) {
            venda.atualizarStatus(CANCELADO);
        }
    },
    AGUARDANDO_PAGAMENTO("Aguardando Aprovação") {

        @Override
        public void aprovar(Venda venda) {
            StatusVendaManager.publicarMensagemNoRabbitMQ(venda, Movimentacao.SAIDA);
            venda.atualizarStatus(PROCESSAMENTO);
        }

        @Override
        public void cancelar(Venda venda) {
            venda.atualizarStatus(CANCELADO);
        }
    }, CANCELADO("Cancelado"), FINALIZADO("Finalizado"),
    PROCESSAMENTO("Processamento") {

        @Override
        public void aprovar(Venda venda) {
            venda.atualizarStatus(FINALIZADO);
        }

        @Override
        public void cancelar(Venda venda) {
            venda.atualizarStatus(CANCELADO);
        }
    };

    private final String nome;

    StatusVenda(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public void aprovar(Venda venda) {
        throw new RuntimeException("Não é possível mudar o status da venda!");
    }

    public void cancelar(Venda venda) {
        throw new RuntimeException("Não é possível mudar o status da venda!");
    }
}
