package TestHttp;
import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpMethod;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.client.RestTemplate;
        import ru.mirea.CartService.Item;
import ru.mirea.Tokens.PayloadToken;
import ru.mirea.Tokens.TokenFactory;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static ru.mirea.Tokens.Role.ADMIN;


public class TestHttpResponse {
    @Test
    public void testing_cart() throws JsonProcessingException {
        String tokenStr;
        String token = TokenFactory.generateToken(new PayloadToken( -1, "loginAdmin", ADMIN));

        ObjectMapper objectMapper = new ObjectMapper();
        tokenStr = objectMapper.writer().writeValueAsString(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", tokenStr);
        HttpEntity entity = new HttpEntity(headers);


        RestTemplate restTemplate2 = new RestTemplate();

        ResponseEntity<List<Item>> listResponse2  = restTemplate2.exchange("http://localhost:8081/user/item", HttpMethod.GET, entity,new ParameterizedTypeReference<List<Item>>() {});
        List<Item> itemList = listResponse2.getBody();
        assertNotNull(itemList);
        assertTrue(itemList.size() >= 2);
        System.out.println(itemList.get(0).getDescription()+"   "+ itemList.get(1).getDescription());
    }
}