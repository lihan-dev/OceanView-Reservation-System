package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/search-reservation")
public class SearchReservationServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String name = req.getParameter("name");
        if (name != null && !name.trim().isEmpty()) {
            List<Reservation> results = reservationDAO.searchByGuestName(name.trim());
            req.setAttribute("results", results);
            req.setAttribute("q", name.trim());
        }

        req.getRequestDispatcher("/WEB-INF/views/searchReservation.jsp").forward(req, resp);
    }
}