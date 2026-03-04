package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String today = LocalDate.now().toString();

        req.setAttribute("totalReservations", reservationDAO.getTotalReservations());
        req.setAttribute("todayCheckins", reservationDAO.getTodayCheckins(today));
        req.setAttribute("todayCheckouts", reservationDAO.getTodayCheckouts(today));
        req.setAttribute("totalIncome", reservationDAO.getTotalIncome());

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}