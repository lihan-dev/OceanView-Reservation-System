package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String from = req.getParameter("from");
        String to = req.getParameter("to");

        // ✅ Java 8 safe check
        if (from != null && to != null && !from.trim().isEmpty() && !to.trim().isEmpty()) {
            List<Reservation> list = reservationDAO.getReservationsBetweenDates(from, to);
            req.setAttribute("reportList", list);
        }

        req.getRequestDispatcher("/WEB-INF/views/report.jsp").forward(req, resp);
    }
}