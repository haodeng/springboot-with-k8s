package demo.hao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getDbTime() {
        return jdbcTemplate.queryForObject("SELECT NOW()", String.class, new Object[] {});
    }


}
