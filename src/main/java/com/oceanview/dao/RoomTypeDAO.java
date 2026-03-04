package com.oceanview.dao;

import com.oceanview.model.RoomType;
import com.oceanview.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {

    // ✅ GET PRICE BY ID (you already use this)
    public double getPriceById(int id) {

        String sql = "SELECT price_per_night FROM room_types WHERE id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price_per_night");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ✅ THIS IS THE MISSING METHOD 💥
    public List<RoomType> getAllRoomTypes() {

        List<RoomType> list = new ArrayList<>();

        String sql = "SELECT * FROM room_types";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RoomType rt = new RoomType();

                rt.setId(rs.getInt("id"));
                rt.setTypeName(rs.getString("type_name"));
                rt.setPricePerNight(rs.getDouble("price_per_night"));

                list.add(rt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}