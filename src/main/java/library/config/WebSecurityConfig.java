package library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Bean
    public static BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/administration/**")
                        .hasAnyAuthority("ADMINISTRATOR")
                    .antMatchers("/reader/**")
                        .hasAnyAuthority("READER")
                    .antMatchers("/author/**")
                        .hasAnyAuthority("AUTHOR")
                    .antMatchers("/profile/**")
                        .fullyAuthenticated()
                    .antMatchers("/", "/webjars/**", "/css/**", "/home", "/sign_up")
                        .permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/sign_in")
                    .defaultSuccessUrl("/profile")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(encoder())
                .usersByUsernameQuery("select nickname, password, 'true' from public.\"User\" where nickname=$1")
                .authoritiesByUsernameQuery("\n" +
                        "select u.nickname, r.name from public.\"User\" u" +
                        " inner join public.\"User_Role\" ur on u.id = ur.user_id" +
                        " inner join public.\"Role_of_User\" r on ur.role_set_id = r.id" +
                        " where u.nickname=$1");
    }
}
