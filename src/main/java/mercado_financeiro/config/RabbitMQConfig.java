package mercado_financeiro.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "investhub.orders.exchange";
    public static final String ORDER_QUEUE = "investhub.orders.queue";
    public static final String ORDER_ROUTING_KEY = "investhub.orders.created";
    public static final String DLQ_EXCHANGE = "investhub.orders.dlx";
    public static final String DLQ_QUEUE = "investhub.orders.dlq";
    public static final String DLQ_ROUTING_KEY = "investhub.orders.failed";

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange  deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .deadLetterExchange(DLQ_EXCHANGE)
                .deadLetterRoutingKey(DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding orderBinding(
            Queue orderQueue,
            DirectExchange orderExchange) {

        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public org.springframework.amqp.support.converter.MessageConverter
    jacksonMessageConverter(
            com.fasterxml.jackson.databind.ObjectMapper objectMapper) {

        return new org.springframework.amqp.support.converter
                .Jackson2JsonMessageConverter(objectMapper);
    }


    @Bean
    public org.springframework.amqp.rabbit.core.RabbitTemplate
    rabbitTemplate(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory,
            org.springframework.amqp.support.converter.MessageConverter messageConverter) {

        org.springframework.amqp.rabbit.core.RabbitTemplate template =
                new org.springframework.amqp.rabbit.core.RabbitTemplate(
                        connectionFactory);

        template.setMessageConverter(messageConverter);

        return template;
    }

}
