package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

  private final Logger log = LoggerFactory.getLogger(RequestController.class);

  private KafkaSendAndReceive kafkaSendAndReceive;

  public RequestController(final KafkaSendAndReceive kafkaSendAndReceive) {
    this.kafkaSendAndReceive = kafkaSendAndReceive;
  }

  @ResponseBody
  @PostMapping(value = "/sendAndReceiverAndReplyToPartition", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public String sendAndReceiverAndReplyToPartition(@RequestBody String text) throws Exception {
    var retorno = kafkaSendAndReceive.sendAndReceiverAndReplyToPartition(text);
    return retorno;
  }

}
