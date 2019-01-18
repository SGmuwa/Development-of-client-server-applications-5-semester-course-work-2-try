package ru.mirea.BalanceService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = {"/*"})
public class FilterBalance implements Filter {

    final String secret_key = "sdkfda";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Фильтр 2");
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String tokenHttp = ((HttpServletRequest)servletRequest).getHeader("token");

        if (tokenHttp != null) {
            //Читаем токен
            ObjectMapper objectMapper = new ObjectMapper();
            Token token = objectMapper.readValue(tokenHttp, Token.class);
            //Считаем сигнатуру
            String signature = DigestUtils.sha256Hex(Integer.toString(token.getId())+token.getLogin()+token.getRole() + secret_key);
            //Если роль = admin и сигнатуры совпадают
            if (token.getRole().equals("admin") && token.getSignature().equals(signature)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }

            else if (token.getRole().equals("user") && token.getSignature().equals(signature)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else {
                System.out.println("НЕВАЛИДНЫЙ ТОКЕН!!!");
                throw new ServletException("Ошибка прав доступа. Пожалуйста войдите еще раз!");
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
