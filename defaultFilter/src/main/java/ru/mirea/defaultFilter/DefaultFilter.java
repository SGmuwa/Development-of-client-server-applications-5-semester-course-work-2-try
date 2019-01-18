package ru.mirea.defaultFilter;
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

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Фильтр 2");
        String tokenHttp = ((HttpServletRequest)servletRequest).getHeader("token");
        PayloadToken payloadToken = TokenFactory.decoderToken(tokenHttp);

        if (payloadToken != null) {
            //Если роль = admin
            if (payloadToken.getRole().contains(ADMIN))
                filterChain.doFilter(servletRequest, servletResponse);
            else if (payloadToken.getRole().contains(USER))
                filterChain.doFilter(servletRequest, servletResponse);
            else {
                System.out.println("НЕВАЛИДНЫЙ ТОКЕН!!!");
                throw new ServletException("Ошибка прав доступа. Пожалуйста войдите ещё раз!");
            }
        }
        else {
            throw new ServletException("Требуется вход в систему");
        }

    }

    @Override
    public void destroy() {
    }
}
