package ru.mirea.CartService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;



@Service
public class CartService {

    private JdbcTemplate jdbcTemplate;

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    public CartService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CartService(" +
                "user_id BIGINT NOT NULL," +
                "item_id BIGINT NOT NULL," +
                "count BIGINT NOT NULL, " +
                "PRIMARY KEY(user_id, item_id))");
    }

    /**
     * Обновление состояния корзины пользователя.
     * Конкретно обновляет конкретный товар и его количество, а не все товары в корзине.
     * @param user_id Идентификатор пользователя.
     * @param cartElement Элемент, который надо установить в корзине, а именно:
     *                    В случае, если он ужен существует, обновить количество.
     *                    В случае, если товара нет в корзине, добавить в корзину.
     *                    В случае, если количество = 0, то удалить из корзины.
     */
    void updateOrAdd(long user_id, CartElement cartElement) {
        if(cartElement.getCount() < 0) // Принимаем только неотрицательные числа.
            throw new IllegalArgumentException("count must be 0 or more.");
        else if(cartElement.getCount() > 0) { // Положительное число количества товара положить в корзину.
            int count = jdbcTemplate.update("UPDATE CartService SET count=? WHERE (user_id=? AND item_id=?)",
                    cartElement.getCount(),
                    user_id,
                    cartElement.getItem_id()
            );
            if(count > 1)
                log.error("Primary key duplicate error. user_id: " + user_id + ", CartElement: " + cartElement);
            if(count == 0) {
                jdbcTemplate.update("INSERT INTO CartService (user_id, item_id, count) VALUES (?, ?, ?)",
                        user_id,
                        cartElement.getItem_id(),
                        cartElement.getCount());
            }
        }
        else { // Количество равно 0. Значит, надо удалить из корзины товар.
            jdbcTemplate.update("DELETE FROM CartService WHERE (user_id=? AND item_id=?)",
                    user_id,
                    cartElement.getItem_id());
        }
    }

    /**
     * Очистка корзины пользователя.
     * @param user_id Идентификатор пользователя, корзину которого надо удалить.
     */
    void clear(long user_id){
        jdbcTemplate.update("DELETE FROM CartService WHERE user_id=?", user_id);
    }

    /**
     * Удалить товар из корзины.
     * @param user_id Идентификатор пользователя, который хочет удалить товар из корзины.
     * @param item_id Идентификатор товара, который нужно весь убрать из корзины.
     *           Например, в корзине было 2 пачки масла и 4 пачки молока. Удалить молоко?
     *           Удаляем тогда все 4 пачки молока.
     */
    void clear(long user_id, long item_id) {
        updateOrAdd(user_id, new CartElement(item_id, 0));
    }

    /**
     * Получение списка товаров и их количество в корзине.
     * @param user_id Идентификатор пользователя, корзина которого интересует.
     * @return Список вещей и их количество в корзине.
     */
    List<CartElement> get(long user_id){
        log.info("Пользователь получает свою корзину.");
        return jdbcTemplate.query("SELECT item_id, count FROM CartService WHERE user_id=?",
                (ResultSet rs, int i) -> new CartElement(rs.getLong("item_id"), rs.getLong("count")),
                user_id);
    }

    /**
     * Оплата всей корзины.
     * Внимание! Отсутсвует сервис, который знает о вещах пользователя!
     * То есть после оплаты вещи исчизают вникуда!
     * @param user_id Идентификатор пользователя, который хочет оплатить корзину.
     */
    void pay(long user_id) {
        List<CartElement> buy = get(user_id);
        clear(user_id);
        List<Item> price = getPrice(buy);
    }

    /**
     * Узнаёт цену предметов.
     * @param help Список предметов, цену которых надо узнать.
     * @return Список покупок. Если в help было записано 3 молока, то вернётся
     * 3 элемента "молоко" с подписью их цены.
     */
    private List<Item> getPrice(Collection<CartElement> help) {
        new RestTemplate()
                .postForObject("http://localhost:8081/get")
    }

    String payOld(int user_id){
        CurrencyService cs = new CurrencyService();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);

        List<CartElement> userCartElementList = jdbcTemplate.getCart(user_id);
        double cartCost = jdbcTemplate.getCartCost(user_id);
        //Balance balance  = restTemplate1.getForObject("http://localhost:8083/user/balance/{user_id}", Balance.class, user_id);
        ResponseEntity<Balance> listResponse = restTemplate.exchange("http://localhost:8083/user/balance/{user_id}", HttpMethod.GET,entity, Balance.class, user_id);
        Balance balance = listResponse.getBody();


        //String balances = restTemplate1.getForObject("http://localhost:8083/balance/{user_id}", String.class, user_id);
        //System.out.println(balances+"  balances");

        double tmpBalance = cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name());
        if(cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name())<cartCost) return "Пополните баланс";

        int tempSizeUserCart = userCartElementList.size();
        for(CartElement cartElement : userCartElementList){
            Integer itId = cartElement.getItem_id();

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
                System.out.println(cartElement.getUser_id()+"    "+ balance.getBalance() +"     "+ cartElement.getPrice());
                System.out.println("Вычисления" + (cs.changeValue_toUSD(balance.getBalance(), balance.getCurrency_name())));
                System.out.println("вычисления"+ cartElement.getPrice());

                //String tmpString2 = restTemplate.postForObject("http://localhost:8083/admin/update/balance/{id}/{balance}",request, String.class,cartElement.getUser_id(), tmpBalance - cartElement.getPrice());
                ResponseEntity<String> listResponse3 = restTemplate.exchange("http://localhost:8083/admin/update/balance/{id}/{balance}", HttpMethod.POST,entity, String.class, cartElement.getUser_id(), tmpBalance - cartElement.getPrice());
                String tmpString2 = listResponse3.getBody();


                tmpBalance = tmpBalance - cartElement.getPrice();

                //удалить запись из корзины
                String tmpString13 = clear(cartElement.getUser_id(), cartElement.getId());

                tempSizeUserCart--;
            }
        }
        System.out.println(tempSizeUserCart +"userCartElementList.size()");
        if(tempSizeUserCart== 0) return "Вся корзина оплачена";
        else return "Не вся корзина оплачена, возможно товар закончился";
    }

    String updateOrAdd(String type, int user_id, int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token" , tokenStr);
        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate1 = new RestTemplate();
        ResponseEntity<Item> listResponse1  = restTemplate1.exchange("http://localhost:8081/user/item/{id}", HttpMethod.GET,entity,Item.class,id);
        Item item = listResponse1.getBody();
        //Item item = restTemplate1.getForObject("http://localhost:8081/user/item/{id}", Item.class, id);


        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<List<Item>> listResponse2  = restTemplate2.exchange("http://localhost:8081/user/item", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Item>>() {});
        List<Item> itemList = listResponse2.getBody();


        if (itemList.get(id-1).getCount() > 0)
            if ((item.getDescription().equals(type))) {
                System.out.println(item.getPrice()+"  "+item.getItem_id()+"  "+item.getDescription());
                String strResult = jdbcTemplate.putItem_inCart(type, user_id, id,item.getPrice());
                return strResult;
            }
        return "ERROR";
    }
}
