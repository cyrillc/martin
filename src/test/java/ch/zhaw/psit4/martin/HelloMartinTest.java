package ch.zhaw.psit4.martin;


import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(locations = {"/Beans.xml"})

public class HelloMartinTest {

    @Test
    public void test() {
       String mess = "Scheiss die Wand an!!";

       ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
       HelloMartin obj = (HelloMartin) context.getBean("helloMartin");
       obj.setMessage("Scheiss die Wand an!!");

       assertTrue(obj.getMessage().equals(mess));      
    }


}