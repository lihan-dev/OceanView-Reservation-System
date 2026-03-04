package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // ✅ ADD RESERVATION
    public void addReservation(Reservation r) {

        String sql = "INSERT INTO reservations " +
                "(guest_name, phone, room_type_id, check_in, check_out, nights, total) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getPhone());
            ps.setInt(3, r.getRoomTypeId());
            ps.setString(4, r.getCheckIn());
            ps.setString(5, r.getCheckOut());
            ps.setInt(6, r.getNights());
            ps.setDouble(7, r.getTotal());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ CHECK ROOM AVAILABILITY (BLOCK OVERLAPS)
    // ignoreReservationId = null for ADD
    // ignoreReservationId = id for UPDATE (so it doesn't clash with itself)
    public boolean isRoomAvailable(int roomTypeId, String checkIn, String checkOut, Integer ignoreReservationId) {

        String sql =
                "SELECT COUNT(*) AS cnt " +
                        "FROM reservations " +
                        "WHERE room_type_id = ? " +
                        "AND (check_in < ? AND check_out > ?) " +
                        (ignoreReservationId != null ? "AND id <> ? " : "");

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);
            ps.setString(2, checkOut); // old.check_in < new.check_out
            ps.setString(3, checkIn);  // old.check_out > new.check_in

            if (ignoreReservationId != null) {
                ps.setInt(4, ignoreReservationId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cnt = rs.getInt("cnt");
                    return cnt == 0; // ✅ available if no overlaps
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ VIEW ALL RESERVATIONS
    public List<Reservation> getAllReservations() {

        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT r.id, r.guest_name, r.phone, rt.type_name, " +
                        "r.check_in, r.check_out, r.nights, r.total " +
                        "FROM reservations r " +
                        "JOIN room_types rt ON r.room_type_id = rt.id " +
                        "ORDER BY r.id DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservation r = new Reservation();

                r.setId(rs.getInt("id"));
                r.setGuestName(rs.getString("guest_name"));
                r.setPhone(rs.getString("phone"));
                r.setRoomTypeName(rs.getString("type_name"));
                r.setCheckIn(rs.getString("check_in"));
                r.setCheckOut(rs.getString("check_out"));
                r.setNights(rs.getInt("nights"));
                r.setTotal(rs.getDouble("total"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ SEARCH BY GUEST NAME
    public List<Reservation> searchByGuestName(String name) {

        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT r.id, r.guest_name, r.phone, rt.type_name, " +
                        "r.check_in, r.check_out, r.nights, r.total " +
                        "FROM reservations r " +
                        "JOIN room_types rt ON r.room_type_id = rt.id " +
                        "WHERE r.guest_name LIKE ? " +
                        "ORDER BY r.id DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();

                    r.setId(rs.getInt("id"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("phone"));
                    r.setRoomTypeName(rs.getString("type_name"));
                    r.setCheckIn(rs.getString("check_in"));
                    r.setCheckOut(rs.getString("check_out"));
                    r.setNights(rs.getInt("nights"));
                    r.setTotal(rs.getDouble("total"));

                    list.add(r);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ REPORT: reservations that OVERLAP the date range (best logic)
    public List<Reservation> getReservationsBetweenDates(String from, String to) {

        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT r.id, r.guest_name, r.phone, rt.type_name, " +
                        "r.check_in, r.check_out, r.nights, r.total " +
                        "FROM reservations r " +
                        "JOIN room_types rt ON r.room_type_id = rt.id " +
                        "WHERE (r.check_in < ? AND r.check_out > ?) " +   // ✅ overlap range
                        "ORDER BY r.check_in ASC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // IMPORTANT:
            // newRange: [from, to]
            // condition: old.check_in < to AND old.check_out > from
            ps.setString(1, to);
            ps.setString(2, from);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getInt("id"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("phone"));
                    r.setRoomTypeName(rs.getString("type_name"));
                    r.setCheckIn(rs.getString("check_in"));
                    r.setCheckOut(rs.getString("check_out"));
                    r.setNights(rs.getInt("nights"));
                    r.setTotal(rs.getDouble("total"));
                    list.add(r);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ DELETE BY ID
    public boolean deleteReservationById(int id) {

        String sql = "DELETE FROM reservations WHERE id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ GET ONE reservation by id (works for EDIT + BILL)
    public Reservation getReservationById(int id) {

        String sql =
                "SELECT r.id, r.guest_name, r.phone, r.room_type_id, rt.type_name, rt.price_per_night, " +
                        "r.check_in, r.check_out, r.nights, r.total " +
                        "FROM reservations r " +
                        "JOIN room_types rt ON r.room_type_id = rt.id " +
                        "WHERE r.id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reservation r = new Reservation();

                    r.setId(rs.getInt("id"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("phone"));
                    r.setRoomTypeId(rs.getInt("room_type_id"));
                    r.setRoomTypeName(rs.getString("type_name"));
                    r.setPricePerNight(rs.getDouble("price_per_night"));
                    r.setCheckIn(rs.getString("check_in"));
                    r.setCheckOut(rs.getString("check_out"));
                    r.setNights(rs.getInt("nights"));
                    r.setTotal(rs.getDouble("total"));

                    return r;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ (optional) If your BillServlet calls this method name
    public Reservation getReservationForBill(int id) {
        return getReservationById(id);
    }

    // ✅ UPDATE RESERVATION (ONLY ONE)
    public boolean updateReservation(Reservation r) {

        String sql = "UPDATE reservations SET guest_name=?, phone=?, room_type_id=?, " +
                "check_in=?, check_out=?, nights=?, total=? WHERE id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getPhone());
            ps.setInt(3, r.getRoomTypeId());
            ps.setString(4, r.getCheckIn());
            ps.setString(5, r.getCheckOut());
            ps.setInt(6, r.getNights());
            ps.setDouble(7, r.getTotal());
            ps.setInt(8, r.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // ✅ DASHBOARD METHODS
    // =========================

    public int getTotalReservations() {
        String sql = "SELECT COUNT(*) AS cnt FROM reservations";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("cnt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCheckins(String today) {
        String sql = "SELECT COUNT(*) AS cnt FROM reservations WHERE check_in = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCheckouts(String today) {
        String sql = "SELECT COUNT(*) AS cnt FROM reservations WHERE check_out = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalIncome() {
        String sql = "SELECT COALESCE(SUM(total), 0) AS sumTotal FROM reservations";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("sumTotal");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}