package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private final long id;
    private final String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
