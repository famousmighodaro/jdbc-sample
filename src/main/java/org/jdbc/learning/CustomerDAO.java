package org.jdbc.learning;

import org.jdbc.learning.util.DataAccessObject;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DataAccessObject<Customer> {
    private static final String INSERT = "INSERT INTO customer (store_id, first_name, last_name, " +
            "email, address_id, active, create_date) VALUES (?,?,?,?,?,?,?)";

    private static final String GET_ONE = "SELECT customer_id, first_name, last_name, email FROM customer where customer_id=?";
    private static final String UPDATE = "UPDATE customer  SET " +
            "first_name=?, last_name=?, email=? WHERE customer_id=?";
    private static final  String DELETE = "DELETE FROM customer  where customer_id=?";

    private static final String GET_ALL_LIMIT = "select * from customer order by last_name, first_name LIMIT ?";
    private static final String GET_ALL_PAGED = "select * from customer order by last_name, first_name LIMIT ? OFFSET ?";

    public CustomerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Customer findById(long id) {
      Customer  customer = new Customer();
      try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customer.setId(rs.getLong("customer_id"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setFirstName(rs.getString("first_name"));
            }
      } catch (SQLException e ) {
          e.printStackTrace();
          throw new RuntimeException(e);
      }
      return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Customer update(Customer dto) {
        Customer customer = new Customer();
        try {
          this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {

            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setLong(4, dto.getId());
            statement.execute();
            this.connection.commit();
            customer = this.findById(dto.getId());

        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException sqe) {
                e.printStackTrace();
                throw new RuntimeException(sqe);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public Customer create(Customer dto) {
      try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {

          statement.setInt(1, dto.getStoreId());
          statement.setString(2, dto.getFirstName());
          statement.setString(3, dto.getLastName());
          statement.setString(4, dto.getEmail());
          statement.setInt(5, dto.getAddressId());
          statement.setInt(6, dto.getActive());

          statement.setDate(7, Date.valueOf(dto.getCreateDate().toLocalDate()));
          statement.execute();


          return findById(2);
      } catch (SQLException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
      }
    }

    @Override
    public void delete(long id) {
         try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
             statement.setLong(1, id);
             statement.execute();
         } catch (SQLException e) {
             e.printStackTrace();
             throw  new RuntimeException(e);
         }

    }
    public List<Customer> findAllSorted(int limit) {
       List<Customer> customers = new ArrayList<>();

       try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_LIMIT);) {
           statement.setInt(1, limit);
           ResultSet resultSet = statement.executeQuery();
           while (resultSet.next()) {
               Customer customer = new Customer();
               customer.setId(resultSet.getLong("customer_id"));
               customer.setLastName(resultSet.getString("last_name"));
               customer.setEmail(resultSet.getString("email"));
               customer.setFirstName(resultSet.getString("first_name"));
               customers.add(customer);
           }

       } catch (SQLException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
       return customers;
    }
    public  List<Customer> findAllPaged(int limit, int pageNumber) {
        List<org.jdbc.learning.Customer> customers = new ArrayList<>();
        int offset = ((pageNumber-1)*limit);
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);) {
            if (limit<1) {
                limit = 10;
            }
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                org.jdbc.learning.Customer customer = new org.jdbc.learning.Customer();
                customer.setId(resultSet.getLong("customer_id"));
                customer.setLastName(resultSet.getString("last_name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setFirstName(resultSet.getString("first_name"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customers;
    }
}
