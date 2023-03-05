package com.mibemolsoft.realworldcrud.security;


/*@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {
    // TODO: Fix all this , get authentication details from the database, populate the database
    //
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user1")
                .password(passwordEncoder().encode("user1Pass"))
                .authorities("ROLE_USER");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests()
                .requestMatchers(toH2Console()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().ignoringRequestMatchers(toH2Console())
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

 */