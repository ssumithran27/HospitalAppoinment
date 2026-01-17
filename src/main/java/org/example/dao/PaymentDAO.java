package org.example.dao;
import org.example.configs.DBConnection;
import org.example.exception.MyClassException;
import org.example.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDAO.class);

    static final int APPOINTMENT_ID=1;
    static final int PAYMENT_AMOUNT=2;
    static final int PAYMENT_TYPE=3;
    static final int PAYMENT_STATUS=4;
    static final int PAYMENT_ID=5;
    public void create(Payment py) throws Exception {
        String sql= """
                INSERT INTO payment (appointment_id,amount,payment_type,payment_status)
                VALUES(?,?,?,?)
                """;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(APPOINTMENT_ID,py.getAppointmentId());
            ps.setDouble(PAYMENT_AMOUNT,py.getAmount());
            ps.setString(PAYMENT_TYPE,py.getPaymentType());
            ps.setString(PAYMENT_STATUS,py.getPaymentStatus());
            int affectedRows=ps.executeUpdate();
            if (affectedRows==0){
                LOGGER.error("Insert failed,no rows affected for payment{} :",py.getPaymentId());
            } else{
                LOGGER.info("successfully inserted payment details{}: ",py.getPaymentId());
            }
        } catch (SQLException e) {
            throw new MyClassException("Error Creating payment details:",e);
        }
    }

    public List<Payment> findAll() {
        List<Payment> payments=new ArrayList<>();
        String sql="""
                  select payment_id,appointment_id,amount,payment_type,payment_status from payment
                  """;
        try(Connection con =DBConnection.getConnect().getConnection();
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while (rs.next()){
                Payment py=new Payment();
                py.setPaymentId(rs.getInt("paymentId"));
                py.setAppointmentId(rs.getInt("appointmentId"));
                py.setAmount(rs.getDouble("amount"));
                py.setPaymentType(rs.getString("paymentType"));
                py.setPaymentStatus(rs.getString("paymentStatus"));
                payments.add(py);
            }
        } catch(SQLException e) {
            throw new MyClassException("Error fetching payment details:",e);
        }
        return payments;
    }
    public boolean update(Payment py)  {
        String sql= " UPDATE payment SET appointment_id=?,amount=?,payment_type=?,payment_status=? where payment_id=?";
        boolean update;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt (APPOINTMENT_ID,py.getAppointmentId());
            ps.setDouble(PAYMENT_AMOUNT,py.getAmount());
            ps.setString(PAYMENT_TYPE,py.getPaymentType());
            ps.setString (PAYMENT_STATUS,py.getPaymentStatus());
            ps.setInt(PAYMENT_ID,py.getPaymentId());
            update= ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error updating payment details:",e);
        }
        return update;
    }

    public boolean delete(int id)
    {
        String sql="delete from payment where payment_id=?";
        boolean delete;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            delete= ps.executeUpdate()>0;
        }catch (SQLException e) {
            throw new MyClassException("Error deleting payment details:",e);
        }
      return delete;
    }
}