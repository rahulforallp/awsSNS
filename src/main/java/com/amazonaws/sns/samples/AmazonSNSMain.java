package com.amazonaws.sns.samples;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class AmazonSNSMain {
  private static AmazonSNS snsClient;

  static {
    AWSCredentials awsCredentials = new BasicAWSCredentials("secretKey", "acceskey");
    snsClient = AmazonSNSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(Regions.US_EAST_2).build();
    System.out.println("SNSClient created");
  }

  public static String createTopic(String topicName) {
    //create a new SNS topic
    CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
    CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
    //print TopicArn
    System.out.println(createTopicResult);
    //get request id for CreateTopicRequest from SNS metadata
    System.out
        .println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
    return createTopicResult.getTopicArn();
  }

  public static void subscribeTopic(String topicArn, String[] emailId) {
    for (int i = 0; i < emailId.length; i++) {
      //subscribe to an SNS topic
      SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", emailId[i]);
      snsClient.subscribe(subRequest);
      //get request id for SubscribeRequest from SNS metadata
      System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
      System.out.println("Check your email and confirm subscription.");
    }
  }

  public static void publishmessage(String topicArn, String message) {
    //publish to an SNS topic
    PublishRequest publishRequest = new PublishRequest(topicArn, message);
    PublishResult publishResult = snsClient.publish(publishRequest);
    //print MessageId of message published to SNS topic
    System.out.println("MessageId - " + publishResult.getMessageId());
  }

  public static void main(String[] args) {
    String arn = AmazonSNSMain.createTopic("topicCreatedByApi3");
    String[] emailId = { "rahulforallp@gmail.com" };
    AmazonSNSMain.subscribeTopic(arn, emailId);
    AmazonSNSMain.publishmessage(arn, "Message sent by API3");
  }
}
