package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedHashMap;

@Component
public class BalanceService {

    private JdbcTemplate jdbcTemplate;

    private Log log = LogFactory.getLog(getClass());

    private CurrencyService currencyService;

    /**
     * Маппер для всей таблицы целеком.
     * Вернётся not-null коллекция.
     */
    private final ResultSetExtractor<Collection<User>> userRowMapper = (ResultSet rs) -> {
        LinkedHashMap<Long, User> balances = new LinkedHashMap<>();
        while (rs.next()) {
            Long user_id = rs.getLong("user_id");
            Money money = new Money(rs.getLong("penny"), rs.getString("currency_name"));
            if (balances.keySet().contains(user_id)) // add money
                balances.get(user_id).updateOrAddBalance(money);
            else
                balances.put(user_id, new User(user_id, money));
        }
        return balances.values();
    };

    @Autowired
    BalanceService(JdbcTemplate jdbcTemplate, CurrencyService currencyService) {
        this.jdbcTemplate = jdbcTemplate;
        this.currencyService = currencyService;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS BalanceService(" +
                "user_id BIGINT NOT NULL," +
                "currency_name CHAR(32) NOT NULL," +
                "penny BIGINT NOT NULL, " +
                "PRIMARY KEY(user_id, currency_name))");
    }

    /**
     * Получение всех кошельков всех пользователей.
     */
    Collection<User> getUserInfo() {
        return jdbcTemplate.query("SELECT * FROM BalanceService", userRowMapper);
    }

    /**
     * Получает информации о пользователе. О его кошельках.
     * @param user_id Идентификатор интересующего нас пользователя.
     * @return Пользовательские кошельки.
     */
    User getUserInfo(long user_id) {
        Collection<User> buffer = jdbcTemplate.query("SELECT * FROM BalanceService WHERE user_id = ?1", userRowMapper, user_id);
        if (buffer.size() == 0) // Гаранитруется, что buffer != null этим кодом: userRowMapper.
            return null;
        if (buffer.size() > 1)
            log.error("Balance anomaly! Check user_id: " + user_id);
        return
                buffer.iterator().next();
    }

    /**
     * Удаляет все предыдущие кошельки пользователя и устанавливает новые.
     * @param user Новая информация о пользователе
     */
    public void updateOrAddUser(User user) {
        Object[] args = new Object[user.size()*3]; // Необходимо, чтобы jdbc экранировал.
        StringBuilder sb = new StringBuilder(
                "INSERT INTO BalanceService VALUES "
        );
        int numberOfParam = 1;
        for(Money m : user) {
            if(numberOfParam != 1)
                sb.append(",");
            args[numberOfParam-1] = user.getUser_id();
            args[numberOfParam] = m.getCurrency();
            args[numberOfParam+1] = m.getCountPenny();
            sb.append(String.format("(?%d, ?%d, ?%d)", numberOfParam++, numberOfParam++, numberOfParam++));
        }
        // Запрос полностью готов.
        jdbcTemplate.update("DELETE FROM BalanceService WHERE user_id = " + user.getUser_id());
        jdbcTemplate.update(sb.toString(), args);
    }

    public boolean buyCurrency(long user_id, String fromName, Money target) {
        try {
            User user = this.getUserInfo(user_id);
            Money from = user.getBalance(fromName);
            Money need = currencyService.howMuchYouNeedOldCurrencyForBuyCurrentNewCurrency(fromName, target);
            if (from.compareTo(need) < 0)
                return false; // Недостаточно средств. Можно позже будет убрать это, если будет система кредитования.
            user.updateOrAddBalance(from.minus(need)); // Отнимаем у пользователя деньги
            updateOrAddUser(user); // Отправляем в БД обновление
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }
}
