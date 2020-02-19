package ru.dgi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import ru.dgi.model.Role;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepository(@Qualifier("dataSource") DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GrantedAuthority> getAuthoritiesByUsername(String username) {
        String query = "SELECT authority FROM authorities a WHERE a.username=?";

        List<GrantedAuthority> authorities = jdbcTemplate.query(query, new RowMapper<GrantedAuthority>(){
            public GrantedAuthority mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return Role.valueOf(rs.getString(1));
            }
        }, username);
        return authorities;
    }

}
