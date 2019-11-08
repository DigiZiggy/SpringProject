package main;

import config.HsqlDataSource;
import config.MvcConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContextListener {

    public static void contextInitialized() {
        new AnnotationConfigApplicationContext(
                MvcConfig.class,
                HsqlDataSource.class
        );

    }
}
