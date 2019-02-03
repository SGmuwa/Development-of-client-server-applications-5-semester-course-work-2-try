package ru.mirea.BalanceService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Данный сервис обеспечивает хранение средств пользователей
 * в разных валютах.
 * Стоит заметить, что сервис не ведёт историю транзакций,
 * так как это не входит в задание, а также увеличит проектирование
 * программы на несколько часов.
 * Данный класс не потокобезопасен.
 * @author <a href="https://www.github.com/SGmuwa">[SG]Muwa</a>
 * @since 01.01.2019
 */
@Component
public class BalanceService {

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
                balances.get(user_id).updateOrAddBalance(money);
            else
                balances.put(user_id, new User(user_id, money));
        }
        return balances.values();
    };

    @Autowired
    BalanceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        Collection<User> buffer = jdbcTemplate.query("SELECT * FROM balanceservice WHERE user_id = ?1", userRowMapper, user_id);
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
    void updateOrAddUser(User user) {
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
            args[numberOfParam+1] = m.getPenny();
            sb.append(String.format("(?%d, ?%d, ?%d)", numberOfParam++, numberOfParam++, numberOfParam++));
        }
        // Запрос полностью готов.
        jdbcTemplate.update("DELETE FROM balanceservice WHERE user_id = " + user.getUser_id());
        jdbcTemplate.update(sb.toString(), args);
    }

    /**
     * Снимает деньги с пользователя.
     * Если у пользователя нет денег, то функция не будет выполнена.
     * @param user_id Идентификатор пользователя, у которого сняли деньги.
     * @param money Деньги, который надо снять с пользователя.
     * @return True, если деньги были сняты. Существует пользователь и у него
     * достаточно средств. Иначе false.
     */
    boolean pay(long user_id, Money money) {
        return jdbcTemplate.update(
                "UPDATE balanceservice SET penny=(penny-?3) " +
                        "WHERE (user_id=?1 AND currency_name = ?2 AND penny>=(?3))",
                user_id,
                money.getCurrency(),
                money.getPenny()
        ) > 0;
    }

    boolean buyCurrency(long user_id, String fromName, Money target) {
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

    /**
     * Очистка всех записей базы данных.
     */
    void clear() {
        jdbcTemplate.execute("DROP TABLE balanceservice");
    }
}
