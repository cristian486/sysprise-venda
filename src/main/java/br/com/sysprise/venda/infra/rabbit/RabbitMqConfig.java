package br.com.sysprise.venda.infra.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.queue_estoque_insuficiente}")
    private String queueEstoqueInsuficiente;
    @Value("${spring.rabbitmq.routing_key_estoque_insuficiente}")
    private String routingKeyEstoqueInsuficiente;
    @Value("${spring.rabbitmq.exchange_name}")
    private String directExchage;


    @Bean
    Queue queueEstoqueInsuficiente() {
        return new Queue(queueEstoqueInsuficiente, true);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(directExchage);
    }

    @Bean
    Binding bindEstoqueInsuficiente() {
        return BindingBuilder.bind(queueEstoqueInsuficiente()).to(directExchange()).with(routingKeyEstoqueInsuficiente);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
