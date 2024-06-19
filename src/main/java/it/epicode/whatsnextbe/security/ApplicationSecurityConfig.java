package it.epicode.whatsnextbe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity()
@EnableMethodSecurity
public class ApplicationSecurityConfig {
    @Bean
    PasswordEncoder stdPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    AuthTokenFilter authenticationJwtToken() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Utilizza la configurazione CORS
                .authorizeHttpRequests(authorize ->
                                authorize //CONFIGURAZIONE DELLA PROTEZIONE DEI VARI ENDPOINT
                                        .requestMatchers("/api/user/login").permitAll()
                                        .requestMatchers("/api/user/registerAdmin").permitAll() // DA CANCELLARE DOPO AVER CREATO L'ADMIN
                                        .requestMatchers("/api/user/register").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/user").hasAuthority("ADMIN") //ENDPOINT DI REGISTRAZIONE APERTO AI SOLI ADMIN
                                        .requestMatchers(HttpMethod.PATCH, "/api/user/{id}").authenticated() //SOLO UN UTENTE AUTENTICATO PUO MODIFICARE I SUOI DATI
                                        .requestMatchers(HttpMethod.DELETE, "/api/user/{id}").authenticated() //TUTTE LE DELETE POSSONO ESSERE FATTE SOLO DALL'ADMIN
                                        .requestMatchers(HttpMethod.GET, "/api/task").permitAll() //ENDPOINT PER OTTENERE TUTTE LE TASK AI SOLI ADMIN
                                        .requestMatchers(HttpMethod.POST, "/api/task").permitAll() //TUTTE LE PUT POSSONO ESSERE FATTE SOLO DALL'ADMIN
                                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated() //TUTTE GLI ENDPOINTS DI TIPO GET SONO RICHIAMABILI SOLO SE L'UTENTE E AUTENTICATO
                        //.requestMatchers("/**").authenticated() //TUTTO CIO CHE PUO ESSERE SFUGGITO RICHIEDE L'AUTENTICAZIONE (SERVE A GESTIRE EVENTUALI DIMENTICANZE)
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                //COMUNICA ALLA FILTERCHAIN QUALE FILTRO UTILIZZARE, SENZA QUESTA RIGA DI CODICE IL FILTRO NON VIENE RICHIAMATO
                http.addFilterBefore(authenticationJwtToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
