package servlet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = { "servlet", "servlet.main" })
public class Config {

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource) {

        var populator = new ResourceDatabasePopulator(
                new ClassPathResource("schema.sql"));

        DatabasePopulatorUtils.execute(populator, dataSource);

        return new JdbcTemplate(dataSource);
    }
}
