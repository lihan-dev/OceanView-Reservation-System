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

@WebServlet("/add-reservation")
public class AddReservationServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final RoomTypeDAO roomTypeDAO = new RoomTypeDAO();

    // ✅ when you OPEN the page
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // ✅ Load room types for dropdown
        req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
        req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
    }

    // ✅ when you CLICK "SAVE RESERVATION"
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ✅ ALSO protect POST (important security)
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String guestName = req.getParameter("guestName");
            String phone = req.getParameter("phone");
            String roomTypeIdStr = req.getParameter("roomTypeId"); // from dropdown
            String checkIn = req.getParameter("checkIn");
            String checkOut = req.getParameter("checkOut");

            // ✅ basic validations
            if (guestName == null || guestName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || roomTypeIdStr == null || roomTypeIdStr.trim().isEmpty()
                    || checkIn == null || checkIn.trim().isEmpty()
                    || checkOut == null || checkOut.trim().isEmpty()) {

                req.setAttribute("error", "❌ Please fill all fields!");
                req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
                req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
                return;
            }

            int roomTypeId = Integer.parseInt(roomTypeIdStr);

            // ✅ price check (must exist)
            double price = roomTypeDAO.getPriceById(roomTypeId);
            if (price <= 0) {
                req.setAttribute("error", "❌ Invalid Room Type!");
                req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
                req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
                return;
            }

            // ✅ nights calculate
            int nights = calculateNights(checkIn, checkOut);
            if (nights <= 0) {
                req.setAttribute("error", "❌ Check-out must be AFTER check-in!");
                req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
                req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
                return;
            }

            // ✅ conflict check (block overlapping)
            boolean available = reservationDAO.isRoomAvailable(roomTypeId, checkIn, checkOut, null);
            if (!available) {
                req.setAttribute("error", "❌ This room type is already booked for these dates!");
                req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
                req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
                return;
            }

            // ✅ create reservation object
            Reservation r = new Reservation();
            r.setGuestName(guestName.trim());
            r.setPhone(phone.trim());
            r.setRoomTypeId(roomTypeId);
            r.setCheckIn(checkIn);
            r.setCheckOut(checkOut);
            r.setNights(nights);
            r.setTotal(price * nights);

            // ✅ save to DB
            reservationDAO.addReservation(r);

            // ✅ SUCCESS MESSAGE redirect
            resp.sendRedirect(req.getContextPath() + "/view-reservations?success=added");

        } catch (NumberFormatException e) {
            req.setAttribute("error", "❌ Room Type is not valid. Please select again!");
            req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
            req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Something went wrong. Check inputs!");
            req.setAttribute("roomTypes", roomTypeDAO.getAllRoomTypes());
            req.getRequestDispatcher("/WEB-INF/views/addReservation.jsp").forward(req, resp);
        }
    }

    // ✅ helper
    private int calculateNights(String checkIn, String checkOut) {
        LocalDate inDate = LocalDate.parse(checkIn);
        LocalDate outDate = LocalDate.parse(checkOut);
        return (int) ChronoUnit.DAYS.between(inDate, outDate);
    }
}