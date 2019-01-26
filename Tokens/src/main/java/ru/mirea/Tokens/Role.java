package ru.mirea.Tokens;

import org.springframework.security.core.GrantedAuthority;

/**
 * Данное перечисление необходимо для обозначения, какие роли
 * могут иметь пользователи системы.
 * <code>                                           <br/>
 * role = Role.ADMIN;                               <br/>
 * if(user.role.getAuthority() == "ADMIN") {        <br/>
 * } else                                           <br/>
 * return;                                          <br/>
 * }
 * </code>
 */
public enum Role implements GrantedAuthority {
    /**
     * Администратор
     */
    ADMIN,
    /**
     * Пользователь
     */
    USER;

    public String getAuthority() {
        return name();
    }

    public String toString() {
        return name();
    }
}
