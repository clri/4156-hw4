package quickbucks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = {"quickbucks"})
public class AuthenticationProviderConfig {
        @Bean(name = "dataSource")
        public DriverManagerDataSource dataSource() {
    	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
    	    driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	    driverManagerDataSource.setUrl ("jdbc:mysql://localhost:3306/db_example");
    	    driverManagerDataSource.setUsername("springuser");
    	    driverManagerDataSource.setPassword("ThePassword");
    	    return driverManagerDataSource;
     }

    @Bean(name="userDetailsService")
    public UserDetailsService userDetailsService(){
    	JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
    	jdbcImpl.setDataSource(dataSource());
    	jdbcImpl.setUsersByUsernameQuery("select user_email,user_password, 1 as enabled from user where user_email=?");
    	jdbcImpl.setAuthoritiesByUsernameQuery("select b.user_email, a.role from user_roles a, user b where b.user_email=? and a.username=b.id");
    	return jdbcImpl;
    }
}
