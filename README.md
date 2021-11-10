# pattern-request-reply

O Apache Kafka é assíncrono por design. Portanto, a semântica de request-reply não é natural no Apache Kafka. No entanto, esse desafio não é novo. O [Request Reply](https://www.enterpriseintegrationpatterns.com/patterns/messaging/RequestReply.html) Enterprise Integration Pattern fornece um mecanismo comprovado para troca de mensagens síncronas em canais assíncronos:

![image](https://www.enterpriseintegrationpatterns.com/img/RequestReply.gif)

O padrão  [Return Address](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ReturnAddress.html) complementa Request Reply com um mecanismo para o solicitante especificar para qual endereço a resposta deve ser enviada:

![image](https://user-images.githubusercontent.com/73845002/141202050-668b2afd-ad2d-4ddc-a3ce-a8376fab1a32.png)

Meu Objetivo aqui é criar um simples exemplo de implementação do padrão request reply usando kafka e spring.

### Iniciar os containers com zookeeper, kafka e o akhq(caso queira vizualizar os topicos e mensagens)   
 ```
 docker-compose up -d
 ```

### Iniciar os microserviços, acessar a pasta raiz de cada um executar o seguinte comando

```
 mvn spring-boot:run
```

### Realizar chamada http para que a mensagem seja enviada e receber a resposta no mesmo request

```
curl --location --request POST 'http://localhost:8280//sendAndReceiverAndReplyToPartition' \
--header 'Content-Type: application/json' \
--data-raw '"teste"'
```
