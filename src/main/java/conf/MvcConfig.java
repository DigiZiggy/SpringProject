package conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = { "app", "conf", "validation" })
public class MvcConfig {

    @Autowired
    public Environment env;

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource) {

        var populator = new ResourceDatabasePopulator(
                new ClassPathResource("schema.sql"));

        DatabasePopulatorUtils.execute(populator, dataSource);

        return new JdbcTemplate(dataSource);
    }

}
