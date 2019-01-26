package ru.mirea.defaultFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import ru.mirea.Tokens.PayloadToken;
import ru.mirea.Tokens.TokenFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ru.mirea.Tokens.Role.ADMIN;
import static ru.mirea.Tokens.Role.USER;

@Component
@WebFilter(urlPatterns = {"/*"})
public class DefaultFilter implements Filter {

    Log log = LogFactory.getLog(getClass());

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("filter 2");
        if(!(servletRequest instanceof HttpServletRequest)) {
            throw new ServletException("Only http");
        }
        String tokenHttp = ((HttpServletRequest)servletRequest).getHeader("token");
        PayloadToken payloadToken = TokenFactory.decoderToken(tokenHttp);

        if (payloadToken != null) {
            //Если роль = admin
            if (payloadToken.getRole().contains(ADMIN))
                filterChain.doFilter(servletRequest, servletResponse);
            else if (payloadToken.getRole().contains(USER))
                filterChain.doFilter(servletRequest, servletResponse);
            else {
                log.warn("unknown role: " + payloadToken.getRole());
                throw new ServletException("Ошибка прав доступа. Пожалуйста войдите ещё раз!");
            }
        }
        else {
            log.info("Filter and user fail: " + tokenHttp);
            throw new ServletException("Требуется вход в систему");
        }
    }

    @Override
    public void destroy() {
    }
}
