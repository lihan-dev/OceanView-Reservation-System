<%@ page import="com.oceanview.model.Reservation" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    Reservation r = (Reservation) request.getAttribute("reservation");
%>

<div class="card pad">
    <div class="h1">Bill / Invoice</div>
    <div class="p">Reservation payment summary</div>

    <% if (r == null) { %>
    <div class="alert alert-danger">No reservation found!</div>
    <% } else { %>

    <div class="grid" style="margin-top:12px;">
        <div class="col-6">
            <div class="stat">
                <div class="stat-k">Reservation ID</div>
                <div class="stat-v"><%= r.getId() %></div>
            </div>
        </div>

        <div class="col-6">
            <div class="stat">
                <div class="stat-k">Guest</div>
                <div class="stat-v"><%= r.getGuestName() %></div>
            </div>
        </div>

        <div class="col-6"><div class="stat"><div class="stat-k">Phone</div><div class="stat-v"><%= r.getPhone() %></div></div></div>
        <div class="col-6"><div class="stat"><div class="stat-k">Room Type</div><div class="stat-v"><%= r.getRoomTypeName() %></div></div></div>

        <div class="col-4"><div class="stat"><div class="stat-k">Check-in</div><div class="stat-v"><%= r.getCheckIn() %></div></div></div>
        <div class="col-4"><div class="stat"><div class="stat-k">Check-out</div><div class="stat-v"><%= r.getCheckOut() %></div></div></div>
        <div class="col-4"><div class="stat"><div class="stat-k">Nights</div><div class="stat-v"><%= r.getNights() %></div></div></div>

        <div class="col-6"><div class="stat"><div class="stat-k">Price per Night</div><div class="stat-v"><%= r.getPricePerNight() %></div></div></div>
        <div class="col-6"><div class="stat"><div class="stat-k">Total</div><div class="stat-v"><%= r.getTotal() %></div></div></div>
    </div>

    <div class="actions" style="margin-top:14px;">
        <button class="btn btn-primary" onclick="window.print()">Print</button>
        <a class="btn" href="<%= request.getContextPath() %>/view-reservations">Back</a>
    </div>

    <% } %>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>