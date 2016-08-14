package mmo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Jin Shuai
 */
public class HikariCPFactory implements DataSourceFactory {
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setProperties(Properties ps) {
        HikariConfig config = new HikariConfig(ps);
        dataSource = new HikariDataSource(config);
    }

}
