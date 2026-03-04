<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    String success = request.getParameter("success");
    List<Reservation> list = (List<Reservation>) request.getAttribute("reservations");
%>

<div class="card pad">
    <div class="h1">All Reservations</div>
    <div class="p">Manage reservations, print bills, edit or delete.</div>

    <% if ("added".equals(success)) { %>
    <div class="alert alert-success">✅ Reservation added successfully!</div>
    <% } else if ("updated".equals(success)) { %>
    <div class="alert alert-success">✅ Reservation updated successfully!</div>
    <% } else if ("deleted".equals(success)) { %>
    <div class="alert alert-success">✅ Reservation deleted successfully!</div>
    <% } %>

    <div class="actions" style="margin-top:10px;">
        <a class="btn btn-primary" href="<%= request.getContextPath() %>/add-reservation">+ Add Reservation</a>
        <a class="btn" href="<%= request.getContextPath() %>/search-reservation">Search</a>
    </div>

    <div style="height:12px;"></div>

    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th><th>Guest</th><th>Phone</th><th>Room</th>
                <th>Check-in</th><th>Check-out</th><th>Nights</th><th>Total</th><th>Actions</th>
            </tr>

            <% if (list != null) {
                for (Reservation r : list) { %>
            <tr>
                <td><%= r.getId() %></td>
                <td><%= r.getGuestName() %></td>
                <td><%= r.getPhone() %></td>
                <td><%= r.getRoomTypeName() %></td>
                <td><%= r.getCheckIn() %></td>
                <td><%= r.getCheckOut() %></td>
                <td><%= r.getNights() %></td>
                <td><%= r.getTotal() %></td>
                <td class="actions">
                    <a class="btn" href="<%= request.getContextPath() %>/edit-reservation?id=<%= r.getId() %>">Edit</a>
                    <a class="btn btn-primary" href="<%= request.getContextPath() %>/print-bill?id=<%= r.getId() %>">Bill</a>
                    <a class="btn btn-danger"
                       href="<%= request.getContextPath() %>/delete-reservation?id=<%= r.getId() %>"
                       onclick="return confirm('Delete this reservation?');">Delete</a>
                </td>
            </tr>
            <% }} %>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>