package br.com.sysprise.venda.model;

import br.com.sysprise.venda.infra.rabbit.model.DadosEmailCobranca;
import br.com.sysprise.venda.infra.rabbit.model.DadosMovimentacao;
import br.com.sysprise.venda.infra.rabbit.model.Movimentacao;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StatusVendaManager {

    private static RabbitTemplate rabbitTemplate;
    private static String routingKey;
    private static String exchangeName;


    @Autowired
    public StatusVendaManager(RabbitTemplate rabbitTemplate,
                               @Value("${spring.rabbitmq.exchange_name}") String exchangeName,
                               @Value("${spring.rabbitmq.routing_key}")String routingKey) {
        StatusVendaManager.rabbitTemplate = rabbitTemplate;
        StatusVendaManager.routingKey = routingKey;
        StatusVendaManager.exchangeName = exchangeName;
    }

    public static void publicarMensagemNoRabbitMQ(Venda venda, Movimentacao movimentacao) {
        venda.getItensDaVenda().forEach(itemCompra -> {
            MessageProperties props = new MessageProperties();
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            DadosMovimentacao dadosMovimentacao = new DadosMovimentacao(venda.getId(), itemCompra.getProdutoId(), itemCompra.getQuantidade(), movimentacao);
            Message mensagem = rabbitTemplate.getMessageConverter().toMessage(dadosMovimentacao, props);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, mensagem);
        });
    }

    public static void publicarMensagem(String routingKey, Long pessoaId, BigDecimal valor) {
        MessageProperties props = new MessageProperties();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        DadosEmailCobranca dadosEmail = new DadosEmailCobranca(pessoaId, valor);
        Message mensagem = rabbitTemplate.getMessageConverter().toMessage(dadosEmail, props);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, mensagem);
    }
}
