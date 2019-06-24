package com.louis.spring.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /* 配置token获取合验证时的策略 */
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
        security.allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.tokenStore(tokenStore()).userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//            .withClient("SampleClientId") // clientId, 可以类比为用户名
//            .secret(passwordEncoder.encode("secret")) // secret， 可以类比为密码
//            .authorizedGrantTypes("authorization_code")	// 授权类型，这里选择授权码
//            .scopes("user_info") // 授权范围
//            .autoApprove(true) // 自动认证
//            .redirectUris("http://localhost:8882/login","http://localhost:8883/login")	// 认证成功重定向URL
//            .accessTokenValiditySeconds(10); // 超时时间，10s
        clients.inMemory()
                .withClient("client")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes("all")
                .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("secret"))
                .accessTokenValiditySeconds(12000)//Access token is only valid for 200 minutes.
                .refreshTokenValiditySeconds(60000)//Refresh token is only valid for 1000 minutes.
                .redirectUris("http://127.0.0.1:9001/connect/github");
    }

}
