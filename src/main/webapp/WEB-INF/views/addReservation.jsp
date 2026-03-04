<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.RoomType" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    String error = (String) request.getAttribute("error");
    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
%>

<div class="card pad">
    <div class="h1">Add Reservation</div>
    <div class="p">Fill details and save the reservation.</div>

    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/add-reservation" style="margin-top:12px;">
        <div class="grid">
            <div class="col-6 field">
                <label>Guest Name</label>
                <input class="input" name="guestName" required>
            </div>

            <div class="col-6 field">
                <label>Phone</label>
                <input class="input" name="phone" required>
            </div>

            <div class="col-6 field">
                <label>Room Type</label>
                <select name="roomTypeId" required>
                    <option value="">-- Select Room Type --</option>
                    <% if (roomTypes != null) {
                        for (RoomType rt : roomTypes) { %>
                    <option value="<%= rt.getId() %>"><%= rt.getTypeName() %> (Rs.<%= rt.getPricePerNight() %>)</option>
                    <% }} %>
                </select>
            </div>

            <div class="col-3 field">
                <label>Check-in</label>
                <input class="input" type="date" name="checkIn" required>
            </div>

            <div class="col-3 field">
                <label>Check-out</label>
                <input class="input" type="date" name="checkOut" required>
            </div>

            <div class="col-12 actions" style="margin-top:6px;">
                <button class="btn btn-primary" type="submit">Save Reservation</button>
                <a class="btn" href="<%= request.getContextPath() %>//dashboard">Cancel</a>
            </div>
        </div>
    </form>
</div>
<%@ include file="/WEB-INF/views/_footer.jspf" %>