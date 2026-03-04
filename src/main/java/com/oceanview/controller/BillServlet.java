package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/print-bill")
public class BillServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            req.setAttribute("reservation", null);
            req.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(req, resp);
            return;
        }

        int id = Integer.parseInt(idStr);

        // ✅ IMPORTANT: this must return a Reservation (or null if not found)
        Reservation r = reservationDAO.getReservationForBill(id);

        // ✅ this name MUST be "reservation"
        req.setAttribute("reservation", r);

        req.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(req, resp);
    }
}