package ru.mirea.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mirea.Tokens.TokenFactory;

import javax.annotation.PostConstruct;
import java.util.List;



@Service
public class ServiceForCart {
    private CartDbConnection cartConnect;
    private String tokenStr;
    private final String secret_key = "sdkfda";
    @Autowired
    public ServiceForCart(CartDbConnection cartConnect/*,RESTtEMPLATE */){
        this.cartConnect = cartConnect;
    }

    @PostConstruct
    public void init()throws JsonProcessingException{

        String signature = DigestUtils.sha256Hex("-1"+"loginAdmin" + "admin" +secret_key);
        Token token = new Token(-1, "loginAdmin","admin", signature);

        ObjectMapper objectMapper = new ObjectMapper();
        this.tokenStr = TokenFactory.generateToken();objectMapper.writer().writeValueAsString(token);
/*
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);
*/
    }

    public String deleteCart(int user_id){return cartConnect.deleteCart(user_id);}

    public List<Cart> getCart(int user_id){
        System.out.println("Сервис1");
        return cartConnect.getCart(user_id);
    }

    public String putItem_inCart(String type, int user_id, int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate1 = new RestTemplate();
        ResponseEntity<Item> listResponse1  = restTemplate1.exchange("http://localhost:8081/user/item/{id}", HttpMethod.GET,entity,Item.class,id);
        Item item = listResponse1.getBody();
        //Item item = restTemplate1.getForObject("http://localhost:8081/user/item/{id}", Item.class, id);


        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<List<Item>> listResponse2  = restTemplate2.exchange("http://localhost:8081/user/item", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Item>>() {});
        List<Item> itemList = listResponse2.getBody();


        if (itemList.get(id-1).getCount() > 0)
            if ((item.getType().equals(type))) {
                System.out.println(item.getPrice()+"  "+item.getId()+"  "+item.getType());
                String strResult = cartConnect.putItem_inCart(type, user_id, id,item.getPrice());
                return strResult;
            }
        return "ERROR";
    }

    public String get_cart_cost(int user_id) {
        double tempCost = cartConnect.getCartCost(user_id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);

        if(tempCost == -0) return "Cart is empty";
        else {
            //String user_currency= restTemplate.getForObject("http://localhost:8083/user/currency/{user_id}", String.class, user_id);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> listResponse = restTemplate.exchange("http://localhost:8083/user/currency/{user_id}", HttpMethod.GET,entity, String.class, user_id);
            String user_currency = listResponse.getBody();
            CurrencyService cs = new CurrencyService();
            double resultCost = cs.getCurrency(tempCost,user_currency);
            return "Cart cost is " + resultCost+" "+user_currency;
        }
    }

    public String payCart(int user_id){
        CurrencyService cs = new CurrencyService();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);

        List<Cart> userCartList = cartConnect.getCart(user_id);
        double cartCost = cartConnect.getCartCost(user_id);
        //Balance balance  = restTemplate1.getForObject("http://localhost:8083/user/balance/{user_id}", Balance.class, user_id);
        ResponseEntity<Balance> listResponse = restTemplate.exchange("http://localhost:8083/user/balance/{user_id}", HttpMethod.GET,entity, Balance.class, user_id);
        Balance balance = listResponse.getBody();


        //String balances = restTemplate1.getForObject("http://localhost:8083/balance/{user_id}", String.class, user_id);
        //System.out.println(balances+"  balances");

        double tmpBalance = cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name());
        if(cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name())<cartCost) return "Пополните баланс";

        int tempSizeUserCart = userCartList.size();
        for(Cart cart : userCartList){
            Integer itId = cart.getItem_id();

            ResponseEntity<Integer> listResponse1 = restTemplate.exchange("http://localhost:8081/user/item/count/{id}", HttpMethod.GET,entity, Integer.class, itId);
            Integer counOfItem = listResponse1.getBody();

            //Integer counOfItem= restTemplate.getForObject("http://localhost:8081/user/item/count/{id}", Integer.class, itId);
            if(counOfItem> 0){
                //Удаляем 1 итем каунт--;
                restTemplate = new RestTemplate();
                //String tmpString1 = restTemplate1.exchange("http://localhost:8081/update/count/{id}", String.class,itId);



                ResponseEntity<String> listResponse2 = restTemplate.exchange("http://localhost:8081/admin/update/count/{id}", HttpMethod.POST, entity, String.class, itId);
                String tmpString1 = listResponse2.getBody();

                //HttpEntity<String> request = new HttpEntity<>(new String());
                //String tmpString1 = restTemplate.postForObject("http://localhost:8081/admin/update/count/{id}",request, String.class,itId);

                System.out.println(tmpString1+"Результат удаления 1 итема");

                //уменьшить баланс
                restTemplate = new RestTemplate();
                System.out.println(cart.getUser_id()+"    "+ balance.getBalance() +"     "+ cart.getPrice());
                System.out.println("Вычисления" + (cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name())));
                System.out.println("вычисления"+cart.getPrice());

                //String tmpString2 = restTemplate.postForObject("http://localhost:8083/admin/update/balance/{id}/{balance}",request, String.class,cart.getUser_id(), tmpBalance - cart.getPrice());
                ResponseEntity<String> listResponse3 = restTemplate.exchange("http://localhost:8083/admin/update/balance/{id}/{balance}", HttpMethod.POST,entity, String.class, cart.getUser_id(), tmpBalance - cart.getPrice());
                String tmpString2 = listResponse3.getBody();


                tmpBalance = tmpBalance - cart.getPrice();

                //удалить запись из корзины
                String tmpString13 =deleteOneItem(cart.getUser_id(),cart.getId());

                tempSizeUserCart--;
            }
        }
        System.out.println(tempSizeUserCart +"userCartList.size()");
        if(tempSizeUserCart== 0) return "Вся корзина оплачена";
        else return "Не вся корзина оплачена, возможно товар закончился";
    }

    public String deleteOneItem(int user_id,int id) {
        return cartConnect.deleteOneItem(user_id,id);
    }

}
