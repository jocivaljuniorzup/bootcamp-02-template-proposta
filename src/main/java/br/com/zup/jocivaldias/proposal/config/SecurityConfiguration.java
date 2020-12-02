package br.com.zup.jocivaldias.proposal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.GET, "/proposals/**").hasAuthority("SCOPE_proposals:read")
                        .antMatchers(HttpMethod.POST, "/proposals/**").hasAuthority("SCOPE_proposals:write")
                        .antMatchers(HttpMethod.POST, "/cards/**/biometrics").hasAuthority("SCOPE_biometrics:write")
                        .antMatchers(HttpMethod.POST, "/cards/**/locks").hasAuthority("SCOPE_locks:write")
                        .anyRequest().authenticated()
        )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

}
