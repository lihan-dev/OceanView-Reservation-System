<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    Integer totalReservations = (Integer) request.getAttribute("totalReservations");
    Integer todayCheckins = (Integer) request.getAttribute("todayCheckins");
    Integer todayCheckouts = (Integer) request.getAttribute("todayCheckouts");
    Double totalIncome = (Double) request.getAttribute("totalIncome");

    if (totalReservations == null) totalReservations = 0;
    if (todayCheckins == null) todayCheckins = 0;
    if (todayCheckouts == null) todayCheckouts = 0;
    if (totalIncome == null) totalIncome = 0.0;

    com.oceanview.model.User u = (com.oceanview.model.User) session.getAttribute("user");
%>

<div class="card pad">
    <div class="h1">Welcome 👋</div>
    <div class="p">Hello, <b><%= (u != null ? u.getUsername() : "User") %></b> • Role: <span class="badge">ADMIN</span></div>
</div>

<div style="height:14px;"></div>

<div class="grid">
    <div class="col-3">
        <div class="stat">
            <div class="stat-k">Total Reservations</div>
            <div class="stat-v"><%= totalReservations %></div>
        </div>
    </div>

    <div class="col-3">
        <div class="stat">
            <div class="stat-k">Today Check-ins</div>
            <div class="stat-v"><%= todayCheckins %></div>
        </div>
    </div>

    <div class="col-3">
        <div class="stat">
            <div class="stat-k">Today Check-outs</div>
            <div class="stat-v"><%= todayCheckouts %></div>
        </div>
    </div>

    <div class="col-3">
        <div class="stat">
            <div class="stat-k">Total Income</div>
            <div class="stat-v"><%= String.format("%.2f", totalIncome) %></div>
        </div>
    </div>

    <div class="col-12">
        <div class="card pad">
            <div class="h1">Quick Actions</div>
            <div class="actions" style="margin-top:10px;">
                <a class="btn btn-primary" href="<%= request.getContextPath() %>/add-reservation">+ Add Reservation</a>
                <a class="btn" href="<%= request.getContextPath() %>/view-reservations">View Reservations</a>
                <a class="btn" href="<%= request.getContextPath() %>/search-reservation">Search Reservation</a>
                <a class="btn" href="<%= request.getContextPath() %>/report">Reservation Report</a>
                <a class="btn" href="<%= request.getContextPath() %>/help">Help</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>