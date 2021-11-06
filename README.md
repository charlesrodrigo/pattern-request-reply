# pattern-request-reply
Exemplo de implementação do padrão request reply usando kafka e spring

### Iniciar os containers com zookeeper, kafka e o akhq(caso queira vizualizar os topicos e mensagens)   
 ```
 docker-compose up -d
 ```

### Iniciar os microserviços, acessar a pasta raiz de cada um executar o seguinte comando

```
 mvn spring-boot:run
```

### Relizar uma chamada http para que a mensagem seja enviada e receber a resposta no mesmo request

```
curl --location --request POST 'http://localhost:8280//sendAndReceiverAndReplyToPartition' \
--header 'Content-Type: application/json' \
--data-raw '"teste"'
```
