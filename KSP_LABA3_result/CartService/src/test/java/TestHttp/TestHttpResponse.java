package TestHttp;
import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import org.apache.commons.codec.digest.DigestUtils;
        import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpMethod;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.client.RestTemplate;
        import ru.mirea.CartService.Item;
        import ru.mirea.CartService.Token;

import java.util.List;


public class TestHttpResponse {
    @Test
    public void testing_cart() throws JsonProcessingException {
        int id = 1;
        String tokenStr;
        final String secret_key = "sdkfda";
        String signature = DigestUtils.sha256Hex("-1" + "admin" + "loginAdmin" + secret_key);
        Token token = new Token(-1, "loginAdmin", "admin", signature);

        ObjectMapper objectMapper = new ObjectMapper();
        tokenStr = objectMapper.writer().writeValueAsString(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", tokenStr);
        HttpEntity entity = new HttpEntity(headers);


        RestTemplate restTemplate2 = new RestTemplate();

        ResponseEntity<List<Item>> listResponse2  = restTemplate2.exchange("http://localhost:8081/user/item", HttpMethod.GET, entity,new ParameterizedTypeReference<List<Item>>() {});
        List<Item> itemList = listResponse2.getBody();
        System.out.println(itemList.get(0).getType()+"   "+ itemList.get(1).getType());
    }
}