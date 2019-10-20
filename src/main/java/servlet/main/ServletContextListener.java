package servlet.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import servlet.config.Config;
import servlet.config.PostgresDataSource;

public class ServletContextListener {

    public static AnnotationConfigApplicationContext contextInitialized() {
        return new AnnotationConfigApplicationContext(
                Config.class,
                PostgresDataSource.class);

    }
}
