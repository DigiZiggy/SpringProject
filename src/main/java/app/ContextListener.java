package app;

import conf.HsqlDataSource;
import conf.MvcConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContextListener {

    public static void contextInitialized() {
        new AnnotationConfigApplicationContext(
                MvcConfig.class,
                HsqlDataSource.class
//                PostgresDataSource.class
        );

    }
}
