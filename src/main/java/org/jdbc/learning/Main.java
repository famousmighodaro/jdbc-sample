package org.jdbc.learning;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       DatabaseConnectionManagement dcm = new DatabaseConnectionManagement(
               "localhost",
               "sport",
               "root",
               "root"
       );
       try {
           Connection con = dcm.getConnection();
           CustomerDAO customerDAO = new CustomerDAO(con);
           Customer customer = new Customer();
           customer.setActive(1);
           customer.setStoreId(2);
           customer.setCreateDate(LocalDateTime.now());
           customer.setAddressId(3);
           customer.setEmail("famousmighodaro@gmail.com");
           customer.setFirstName("Famous");
           customer.setLastName("Ighodaro");

           //customerDAO.create(customer);

           Customer customer1 = customerDAO.findById(602);
           System.out.println(customer1);
           customer1.setFirstName("Jerimaih");
           customer1.setEmail("famousighodaro");
           customer1.setLastName("Albert");
           System.out.println("\n ############################################ \n");
           //System.out.println(customerDAO.update(customer1));
           //customerDAO.delete(603);
           //customerDAO.delete(604);

           Connection connection = dcm.getConnection();
           OrderDAO orderDAO = new OrderDAO(connection);
           //List<Order> orders = orderDAO.getOrderForCustomer(789);
           //orders.forEach(System.out::println);

           CustomerDAO customerDAO1 = new CustomerDAO(connection);
           List<Customer> customers = customerDAO1.findAllSorted(20);
           //customers.forEach(System.out::println);
           System.out.println("\n ########################### Page ##################################\n");
           for (int i = 1; i<3; i++) {
               System.out.println("\n ########################### Page " + i +" ##################################\n");
               customerDAO1.findAllPaged(20, i).forEach(System.out::println);
           }
       } catch (SQLException e) {
           System.out.println(e);
       }
    }
}
