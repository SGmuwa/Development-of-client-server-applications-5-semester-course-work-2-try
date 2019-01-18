package ru.mirea.Tokens;

import org.springframework.security.core.GrantedAuthority;

/**
 * Данное перечисление необходимо для обозначения, какие роли
 * могут иметь пользователи системы.
 *
  {@code
  * role = Role.ADMIN;
 *
  * if(user.role.getAuthority() == "ADMIN") {
 *
  * } else
  * return;
  }
 */
public enum Role implements GrantedAuthority {
    /**
     * Администратор
     */
    ADMIN,
    /**
     * Пользователь
     */
    USER,
    /**
     * Гость
     */
    GUEST;

    public String getAuthority() {
        return name();
    }

    public String toString() {
        return name();
    }
}
