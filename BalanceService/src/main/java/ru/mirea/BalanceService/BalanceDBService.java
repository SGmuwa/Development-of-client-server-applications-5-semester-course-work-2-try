package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class BalanceDBService {

    private JdbcTemplate jdbcTemplate;

    private Log log = LogFactory.getLog(getClass());

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
                balances.get(user_id).updateBalance(money);
            else
                balances.put(user_id, new User(user_id, money));
        }
        return balances.values();
    };

    @Autowired
    BalanceDBService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void init() {
        // init db инициализация базы данных
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS BalanceService(" +
                "user_id BIGINT NOT NULL," +
                "currency_name CHAR(32) NOT NULL," +
                "penny BIGINT NOT NULL, " +
                "PRIMARY KEY(user_id, currency_name))");

    }

    /**
     * Получение всех кошельков всех пользователей.
     */
    public Collection<User> getBalances() {
        return jdbcTemplate.query("SELECT * FROM BalanceService", userRowMapper);
    }

    /**
     * Получает информации о пользователе. О его кошельках.
     * @param user_id Идентификатор интересующего нас пользователя.
     * @return Пользовательские кошельки.
     */
    public User getUserInfo(long user_id) {
        Collection<User> buffer = jdbcTemplate.query("SELECT * FROM BalanceService WHERE user_id = ?1", userRowMapper, user_id);
        if (buffer.size() == 0) // Гаранитруется, что buffer != null этим кодом: userRowMapper.
            return null;
        if (buffer.size() > 1)
            log.error("Balance anomaly! Check user_id: " + user_id);
        return
                buffer.iterator().next();
    }

    public String updateBalance(int user_id, double bal) {
        return balanceConnect.updateBalance(user_id, bal);
    }

    public String changeCurrency(int user_id, String change_currency) {
        return balanceConnect.changeCurrency(user_id, change_currency);
    }

    //запретить обращаться пользователям
    public String updateBalance2(int id, double balance) {
        return balanceConnect.updateBalance2(id, balance);
    }
}
