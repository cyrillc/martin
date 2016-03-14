package ch.zhaw.psit4.martin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
      ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

      HelloMartin obj = (HelloMartin) context.getBean("helloMartin");

      obj.getMessagePrint();
    }
}
