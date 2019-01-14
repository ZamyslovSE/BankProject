package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import web.exception.UserNotFoundException;
import web.pojo.Product;
import web.pojo.User;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by zdoba on 14.01.2019.
 */
@Component
public class OtherDAOImpl implements OtherDAO{

    private static final Logger log = Logger.getLogger(OtherDAOImpl.class.getName());

    private static NamedParameterJdbcTemplate jdbcTemplate;

    private static String SQL_SELECT_OPER_BY_PASSPORT = "SELECT * FROM Operations WHERE Sender_passport=:passport OR Receiver_passport=:passport";
    private static String SQL_CHECK_BALANCE = "SELECT Balance FROM Users WHERE passport=:passport";

    @Autowired
    private void OtherDAOImpl(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Product> getProducts() {
        log.info("Retrieve products.");

        List<Product> products = jdbcTemplate.query("SELECT * FROM product_type", (resultSet, i) -> {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setType(resultSet.getString("type"));
            product.setName(resultSet.getString("name"));
            product.setFee(resultSet.getDouble("fee"));
            product.setPercent(resultSet.getDouble("percent"));
            product.setDescription(resultSet.getString("description"));
            return product;
        });
        return products;
    }

    public List<Product> getProductsByType(String productType) {
        log.info("Retrieve products " + productType);

        Map<String,String> params = new HashMap<>();
        params.put("type",productType);

        List<Product> products = jdbcTemplate.query("SELECT type, name, percent, description, fee FROM product_type WHERE type=:type", params,  (resultSet, i) -> {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setType(resultSet.getString("type"));
            product.setName(resultSet.getString("name"));
            product.setFee(resultSet.getDouble("fee"));
            product.setPercent(resultSet.getDouble("percent"));
            product.setDescription(resultSet.getString("description"));
            return product;
        });
        return products;
    }
}
