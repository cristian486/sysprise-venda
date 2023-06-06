package br.com.sysprise.venda.controller;

import br.com.sysprise.venda.model.DadosAtualizarStatusVenda;
import br.com.sysprise.venda.service.VendaService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMqConsumer {

    private final VendaService vendaService;

    @Transactional
    @RabbitListener(queues = "${spring.rabbitmq.queue_estoque_insuficiente}")
    public void consumerSemEstoque(@Payload String id) {
        DadosAtualizarStatusVenda atualizarStatus = new DadosAtualizarStatusVenda(Long.valueOf(id), false);
        try {
            vendaService.atualizarStatus(atualizarStatus);
        } catch (RuntimeException ignored) {}
    }
}