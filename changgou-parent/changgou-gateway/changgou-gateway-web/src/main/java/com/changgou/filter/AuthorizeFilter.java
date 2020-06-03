package com.changgou.filter;

import com.changgou.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    //令牌的名称
    private static final String AUTHORIZE_TOKEN="Authorization";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //判断用户访问的uri是否需要拦截，不需要拦截则直接放行
        String uri=request.getURI().toString();
        if(!UrlFilter.testUrl(uri)){
            return chain.filter(exchange);
        }
        //查了看请求头是否有参数,获取第一个名为定义的令牌名称的对应值
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //设置一个boolean，如果请求头中有令牌信息，则返回true，没有则返回false，我们需要将令牌设置到header中
        boolean flag=true;
        //如果这个值为空，那么继续判断参数是否包含
        if(StringUtils.isEmpty(token)){
            token=request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            flag=false;
        }
        //还为空，判断cookie是否包含
        if(StringUtils.isEmpty(token)){
            ResponseCookie cookie = response.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookie!=null){
                token=cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            //如果令牌为空，则返回空数据
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            //解析令牌，不报错则为解析成功，令牌可以使用
            JwtUtil.parseJWT(token);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //判断jwt令牌是否存在header请求头中，不存在则设置进去
        if(!flag){
            //设置头信息
           request.mutate().header(AUTHORIZE_TOKEN,token);
        }
        //返回验证成功
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
