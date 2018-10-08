package Access;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

@Component
class ConnectionFactory {
    @Value("${DB_URL}")
    private String url;
    private ComboPooledDataSource dataSource;

    public void init() {
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl(url);
            dataSource.getConnection();
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
