package com.bochao.auth.permission.handler;


import com.bochao.auth.config.RestClientDetailsServiceImpl;
import com.bochao.auth.permission.properties.AuthServerProperties;
import com.bochao.auth.permission.token.BaseUserDetail;
import com.bochao.common.entity.TokenEntity;
import com.bochao.common.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.security.auth.message.AuthException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:
 * @Date 16:33 2019/11/27
 */
public class WebLoginAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements ResponseUtil<Map> {
    /**
	 * 配置日志
	 */
	private final static Logger logger = LoggerFactory.getLogger(WebLoginAuthSuccessHandler.class);

    @Resource
    private RestClientDetailsServiceImpl restClientDetailsService;

	@Autowired
	private DefaultTokenServices defaultTokenServices;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TokenStore authTokenStore;

    @Resource
    private RedisTemplate<String, TokenEntity> tokenEntityRedisTemplate;

    @Autowired
    private AuthServerProperties authServerProperties;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String,String> result = createToken(request,authentication);
        getResponseWeb(response,objectMapper,result);
        logger.info("登录成功");
    }

    /**
     * 创建token
     * @param request
     * @param authentication
     */
    private Map<String, String> createToken(HttpServletRequest request, Authentication authentication) throws AuthException {
        String clientId = request.getParameter("client_id");
        String clientSecret = request.getParameter("client_secret");

        ClientDetails clientDetails = restClientDetailsService.loadClientByClientId(clientId);
        //密码工具
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (null == clientDetails) {
            throw new UnapprovedClientAuthenticationException("clientId不存在" + clientId);
        }
        //比较secret是否相等
        else if  (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配" + clientId);
        }

        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(1), clientId, clientDetails.getScope(),"password");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        defaultTokenServices.setTokenStore(authTokenStore);
        logger.info("==="+authentication.getPrincipal());
        defaultTokenServices.setAccessTokenValiditySeconds(authServerProperties.getTokenValid());
        //开启刷新功能
        if(authServerProperties.getStartRefresh()) {
            defaultTokenServices.setRefreshTokenValiditySeconds(authServerProperties.getRefreshTokenValid());
        }

        OAuth2AccessToken token = defaultTokenServices.createAccessToken(oAuth2Authentication);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,String> result = new HashMap<>();
        result.put("access_token", token.getValue());
        result.put("token_Expiration", sdf.format(token.getExpiration()));
        //开启刷新功能
        if(authServerProperties.getStartRefresh()) {
            //获取刷新Token
            DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) token.getRefreshToken();
            result.put("refresh_token", refreshToken.getValue());
            result.put("refresh_token_Expiration", sdf.format(refreshToken.getExpiration()));
        }

        logger.debug("token:"+token.getValue());
        //判断token的和方法性
        String id = String.valueOf(((BaseUserDetail)authentication.getPrincipal()).getBaseUser().getId());
        if(!TokenUtil.pushToken(id,tokenEntityRedisTemplate,token.getValue(),token.getExpiration(),authServerProperties.getMaxClient())){
            throw new AuthException("登录限制，同时登录人数过多");
        }
        return result;
    }
}
