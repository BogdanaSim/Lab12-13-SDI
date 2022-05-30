package catalog.client;

import catalog.client.ui.AnimalConsole;
import catalog.client.ui.MasterConsole;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("catalog.client.config");

        MasterConsole masterConsole=context.getBean(MasterConsole.class);

//        System.out.println(org.apache.logging.log4j.Logger.class.getResource("/org/ap‌​ache/logging/log4j/Logger.class"));
////core
//        System.out.println(org.apache.logging.log4j.Logger.class.getResource("/org/ap‌​ache/logging/log4j/core/Appender.class"));
////config
//        System.out.println(org.apache.logging.log4j.Logger.class.getResource("/log4j2.xml"));
        masterConsole.run();
        System.out.println("bye ");
    }
}
