package org.example.dao;
import org.example.DBConnection;
import org.example.Exception.MyClassException;
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
    private static final Logger logger = LoggerFactory.getLogger(PaymentDAO.class);
    public void create(Payment p) throws Exception {
        String sql= """
                INSERT INTO payment (appointment_id,amount,payment_type,payment_status)
                VALUES(?,?,?,?)
                """;
        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,p.getAppointmentId());
            ps.setDouble(2,p.getAmount());
            ps.setString(3,p.getPaymentType());
            ps.setString(4,p.getPaymentStatus());
            int affectedRows=ps.executeUpdate();
            if (affectedRows==0){
                logger.error("Insert failed,no rows affected for payment{} :",p.getPaymentId());
            } else{
                logger.info("successfully inserted payment details{}: ",p.getPaymentId());
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
        try(Connection con =DBConnection.getConnection();
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
        boolean update=false;
        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt (1,py.getAppointmentId());
            ps.setDouble(2,py.getAmount());
            ps.setString(3,py.getPaymentType());
            ps.setString (4,py.getPaymentStatus());
            update= ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error updating payment details:",e);
        }
        return update;
    }

    public boolean delete(int id)
    {
        String sql="delete from payment where payment_id=?";
        boolean delete=false;

        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            delete= ps.executeUpdate()>0;
        }catch (SQLException e) {
            throw new MyClassException("Error deleting payment details:",e);
        }
      return delete;
    }
}