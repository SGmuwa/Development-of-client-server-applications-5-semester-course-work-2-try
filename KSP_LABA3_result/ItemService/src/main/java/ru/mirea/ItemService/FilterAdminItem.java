/*package ru.mirea.ItemService;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/admin/*")
public class FilterAdminItem implements Filter{
    final String secret_key = "sdkfda";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String tokenHttp = ((HttpServletRequest)servletRequest).getHeader("token");
        if (tokenHttp != null) {
            //Конвертация Java объектов (readValue прочитать объект Token из JSON)
            ObjectMapper objectMapper = new ObjectMapper();
            Token token = objectMapper.readValue(tokenHttp, Token.class);
            String signature = DigestUtils.sha256Hex(Integer.toString(token.getId())+token.getLogin()+ token.getRole()+ secret_key);

            if (token.getSignature().equals(signature)) {
                filterChain.doFilter(servletRequest, servletResponse);
                System.out.println("Проверку прошёл");
            }
        }
        else {
            throw new ServletException("Sign in required");
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}
*/