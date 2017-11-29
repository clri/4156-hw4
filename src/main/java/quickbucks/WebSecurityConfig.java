package quickbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = {"quickbucks"})
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsService userDetailsService;

        @Autowired
        public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
          http.authorizeRequests()
         .antMatchers("/hello").access("hasRole('ROLE_ADMIN')")
         .antMatchers("/demo/adduser").permitAll()
         .antMatchers("/demo/reset").permitAll()
         .antMatchers("/savedreset.html").permitAll()
         .antMatchers("/demo/**").access("hasRole('ROLE_USER')")
         .antMatchers("/homePageLoggedIn**").access("hasRole('ROLE_USER')")
	 .antMatchers("/homepageloggedin**").access("hasRole('ROLE_USER')")
         .antMatchers("/post**").access("hasRole('ROLE_USER')")
	 .antMatchers("/sent*").access("hasRole('ROLE_USER')")
         .antMatchers("/create**").access("hasRole('ROLE_USER')")
         .antMatchers("/Post**").access("hasRole('ROLE_USER')")
         .antMatchers("/*Job*").access("hasRole('ROLE_USER')")
         .antMatchers("/*job*").access("hasRole('ROLE_USER')")
         .antMatchers("/notif*").access("hasRole('ROLE_USER')")
         .antMatchers("/decide*").access("hasRole('ROLE_USER')")
         .antMatchers("/read*").access("hasRole('ROLE_USER')")
         .antMatchers("/search*").access("hasRole('ROLE_USER')")
         .anyRequest().permitAll()
         .and()
           .formLogin().loginPage("/login")
           .usernameParameter("username").passwordParameter("password")
         .and()
           .logout().logoutSuccessUrl("/login?logout")
          .and()
          .exceptionHandling().accessDeniedPage("/403")
         .and()
           .csrf();
        }

	@Bean(name="passwordEncoder")
    public PasswordEncoder passwordencoder(){
    	return new BCryptPasswordEncoder();
    }
}
