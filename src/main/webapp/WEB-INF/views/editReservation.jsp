<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ page import="com.oceanview.model.Reservation" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    String error = (String) request.getAttribute("error");
    Reservation r = (Reservation) request.getAttribute("r");
    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
%>

<div class="card pad">
    <div class="h1">Edit Reservation</div>
    <div class="p">Update reservation details and save.</div>

    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <% if (r == null) { %>
    <div class="alert alert-danger">Reservation not found.</div>
    <a class="btn" href="<%= request.getContextPath() %>/view-reservations">Back</a>
    <% } else { %>

    <form method="post" action="<%= request.getContextPath() %>/edit-reservation" style="margin-top:12px;">
        <input type="hidden" name="id" value="<%= r.getId() %>">

        <div class="grid">
            <div class="col-6 field">
                <label>Guest Name</label>
                <input class="input" name="guestName" value="<%= r.getGuestName() %>" required>
            </div>

            <div class="col-6 field">
                <label>Phone</label>
                <input class="input" name="phone" value="<%= r.getPhone() %>" required>
            </div>

            <div class="col-6 field">
                <label>Room Type</label>
                <select name="roomTypeId" required>
                    <% if (roomTypes != null) {
                        for (RoomType rt : roomTypes) {
                            boolean selected = (rt.getId() == r.getRoomTypeId());
                    %>
                    <option value="<%= rt.getId() %>" <%= selected ? "selected" : "" %>>
                        <%= rt.getTypeName() %> (Rs.<%= rt.getPricePerNight() %>)
                    </option>
                    <% }} %>
                </select>
            </div>

            <div class="col-3 field">
                <label>Check-in</label>
                <input class="input" type="date" name="checkIn" value="<%= r.getCheckIn() %>" required>
            </div>

            <div class="col-3 field">
                <label>Check-out</label>
                <input class="input" type="date" name="checkOut" value="<%= r.getCheckOut() %>" required>
            </div>

            <div class="col-12 actions" style="margin-top:6px;">
                <button class="btn btn-primary" type="submit">Update</button>
                <a class="btn" href="<%= request.getContextPath() %>/view-reservations">Back</a>
            </div>
        </div>
    </form>

    <% } %>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>