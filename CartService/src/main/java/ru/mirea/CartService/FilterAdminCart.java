package ru.mirea.CartService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/admin/*")
public class FilterAdminCart implements Filter{

    final String secret_key = "sdkfda";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String tokenHttp = ((HttpServletRequest)servletRequest).getHeader("token");

        if (tokenHttp != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Token token = objectMapper.readValue(tokenHttp, Token.class);
            if (!token.getRole().equals("admin")) {
                throw new ServletException("Доступ только для администраторов!");

            }
            String signature = DigestUtils.sha256Hex(Integer.toString(token.getId())+token.getLogin()+token.getRole() + secret_key);

            if (token.getSignature().equals(signature)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        else {
            throw new ServletException();
        }

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
