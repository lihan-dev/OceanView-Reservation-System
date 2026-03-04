package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.model.Reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/edit-reservation")
public class EditReservationServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAO();

    // ✅ Open edit page with data
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        int id = parseIdOrRedirect(idStr, req, resp);
        if (id == -1) return;

        Reservation r = reservationDAO.getReservationById(id);
        if (r == null) {
            resp.sendRedirect(req.getContextPath() + "/view-reservations");
            return;
        }

        // ✅ dropdown data
        req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
        req.setAttribute("r", r);

        req.getRequestDispatcher("/WEB-INF/views/editReservation.jsp").forward(req, resp);
    }

    // ✅ Update reservation
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        int id = parseIdOrRedirect(idStr, req, resp);
        if (id == -1) return;

        // ✅ read inputs
        String guestName = req.getParameter("guestName");
        String phone = req.getParameter("phone");
        String roomTypeIdStr = req.getParameter("roomTypeId");
        String checkIn = req.getParameter("checkIn");
        String checkOut = req.getParameter("checkOut");

        // ✅ basic validation
        if (isBlank(guestName) || isBlank(phone) || isBlank(roomTypeIdStr) || isBlank(checkIn) || isBlank(checkOut)) {
            forwardBackWithError("❌ Please fill all fields!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        int roomTypeId;
        try {
            roomTypeId = Integer.parseInt(roomTypeIdStr);
        } catch (NumberFormatException e) {
            forwardBackWithError("❌ Invalid Room Type!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        // ✅ nights calc
        int nights;
        try {
            nights = (int) ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
        } catch (Exception e) {
            forwardBackWithError("❌ Invalid dates!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        if (nights <= 0) {
            forwardBackWithError("❌ Check-out must be after Check-in!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        // ✅ price check
        double pricePerNight = roomTypeDAO.getPriceById(roomTypeId);
        if (pricePerNight <= 0) {
            forwardBackWithError("❌ Invalid Room Type!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        // ✅ availability check (ignore itself)
        boolean ok = reservationDAO.isRoomAvailable(roomTypeId, checkIn, checkOut, id);
        if (!ok) {
            forwardBackWithError("❌ This room type is already booked for these dates!", id, guestName, phone, roomTypeIdStr, checkIn, checkOut, req, resp);
            return;
        }

        // ✅ update object
        Reservation r = new Reservation();
        r.setId(id);
        r.setGuestName(guestName.trim());
        r.setPhone(phone.trim());
        r.setRoomTypeId(roomTypeId);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        r.setNights(nights);
        r.setTotal(pricePerNight * nights);

        boolean updated = reservationDAO.updateReservation(r);

        if (updated) {
            resp.sendRedirect(req.getContextPath() + "/view-reservations?success=updated");
        } else {
            resp.sendRedirect(req.getContextPath() + "/view-reservations");
        }
    }

    // ---------------- HELPERS ----------------

    private int parseIdOrRedirect(String idStr, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/view-reservations");
            return -1;
        }
        try {
            return Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/view-reservations");
            return -1;
        }
    }

    private void forwardBackWithError(
            String errorMsg,
            int id,
            String guestName,
            String phone,
            String roomTypeIdStr,
            String checkIn,
            String checkOut,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        req.setAttribute("error", errorMsg);

        // ✅ keep dropdown working
        req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());

        // ✅ keep user-entered values (so form doesn't reset)
        Reservation r = new Reservation();
        r.setId(id);
        r.setGuestName(guestName != null ? guestName : "");
        r.setPhone(phone != null ? phone : "");
        try { r.setRoomTypeId(Integer.parseInt(roomTypeIdStr)); } catch (Exception ignored) {}
        r.setCheckIn(checkIn != null ? checkIn : "");
        r.setCheckOut(checkOut != null ? checkOut : "");

        req.setAttribute("r", r);

        req.getRequestDispatcher("/WEB-INF/views/editReservation.jsp").forward(req, resp);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}