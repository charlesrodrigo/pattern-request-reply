package com.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaSendAndReceiveImpl implements KafkaSendAndReceive {

  private final Logger log = LoggerFactory.getLogger(KafkaSendAndReceiveImpl.class);
  private final String requestTopic;
  private final String replicTopic;
  private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

  public KafkaSendAndReceiveImpl(@Value("${kafka.topic.request}") final String requestTopic,
      @Value("${kafka.topic.reply}") final String replicTopic,
      final ReplyingKafkaTemplate<String, String, String> kafkaTemplate) {
    this.requestTopic = requestTopic;
    this.replicTopic = replicTopic;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public String sendAndReceiverAndReplyToPartition(final String text) throws Exception {

    ProducerRecord<String, String> record = new ProducerRecord<String, String>(requestTopic, text);

    TopicPartition replyPartition = getFirstAssignedReplyTopicPartition();

    record.headers()
        .add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replicTopic.getBytes()))
        //USANDO A ESTRATEGIA DE INFORMAR PARA QUAL PARTIÇÃO DEVE RESPONDER
        //SE MUDAR PARA GROUP-ID DIFERENTE, PODE REMOVER ESSE HEADER REPLY_PARTITION
        .add(new RecordHeader(KafkaHeaders.REPLY_PARTITION, intToBytesBigEndian(replyPartition.partition())));

    RequestReplyFuture<String, String, String> replyFuture = kafkaTemplate.sendAndReceive(record);

    SendResult<String, String> sendResult = replyFuture.getSendFuture().get();

    sendResult.getProducerRecord().headers()
        .forEach(header -> log.info("{}:{}", header.key(), header.value().toString()));

    ConsumerRecord<String, String> consumerRecord = replyFuture.get();

    return consumerRecord.value();
  }


  private TopicPartition getFirstAssignedReplyTopicPartition() {
    if (kafkaTemplate.getAssignedReplyTopicPartitions() != null &&
        kafkaTemplate.getAssignedReplyTopicPartitions().iterator().hasNext()) {
      TopicPartition replyPartition = kafkaTemplate.getAssignedReplyTopicPartitions().iterator().next();

      this.log.info("AssignedReplyTopic {}", replyPartition.partition());

      return replyPartition;
    } else {
      throw new KafkaException("Illegal state: No reply partition is assigned to this instance");
    }
  }

  private static byte[] intToBytesBigEndian(final int data) {
    return new byte[]{(byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff),
        (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff),};
  }

}
