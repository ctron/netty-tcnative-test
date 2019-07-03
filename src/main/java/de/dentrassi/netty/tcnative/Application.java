package de.dentrassi.netty.tcnative;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Application {
  public static void main(String[] args) throws Exception {
    try {
      Context context = new InitialContext();

      ConnectionFactory factory = (ConnectionFactory) context.lookup("myFactoryLookup");
      Destination destination = (Destination) context.lookup("myDestinationLookup");

      Connection connection = factory.createConnection("<username>", "<password>");
      connection.setExceptionListener(new MyExceptionListener());
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      MessageConsumer messageConsumer = session.createConsumer(destination);

      Message message = messageConsumer.receive(5000);

      if (message == null) {
        System.out.println("A message was not received within given time.");
      } else {
        System.out.println("Received message: " + ((TextMessage) message).getText());
      }

      connection.close();
    } catch (Exception exp) {
      System.out.println("Caught exception, exiting.");
      exp.printStackTrace(System.out);
      System.exit(1);
    }
 }

  private static class MyExceptionListener implements ExceptionListener {
    @Override
    public void onException(JMSException exception) {
      System.out.println("Connection ExceptionListener fired, exiting.");
      exception.printStackTrace(System.out);
      System.exit(1);
    }
  }
}