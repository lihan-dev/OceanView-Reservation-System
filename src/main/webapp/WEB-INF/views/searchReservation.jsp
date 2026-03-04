<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    String q = request.getParameter("name");
    List<Reservation> results = (List<Reservation>) request.getAttribute("results");
%>

<div class="card pad">
    <div class="h1">Search Reservation</div>
    <div class="p">Search by guest name and view results.</div>

    <form method="get" action="<%= request.getContextPath() %>/search-reservation" style="margin-top:12px;">
        <div class="grid">
            <div class="col-6 field">
                <label>Guest Name</label>
                <input class="input" name="name" value="<%= (q != null ? q : "") %>" placeholder="e.g., Henry" required>
            </div>
            <div class="col-6 field" style="display:flex; align-items:flex-end; gap:10px;">
                <button class="btn btn-primary" type="submit">Search</button>
                <a class="btn" href="<%= request.getContextPath() %>/search-reservation">Clear</a>
            </div>
        </div>
    </form>

    <div style="height:12px;"></div>

    <% if (q != null) { %>
    <div class="p">Showing results for: <span class="badge"><%= q %></span></div>
    <div style="height:10px;"></div>

    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th><th>Guest</th><th>Phone</th><th>Room</th>
                <th>Check-in</th><th>Check-out</th><th>Nights</th><th>Total</th>
            </tr>

            <% if (results != null && !results.isEmpty()) {
                for (Reservation r : results) { %>
            <tr>
                <td><%= r.getId() %></td>
                <td><%= r.getGuestName() %></td>
                <td><%= r.getPhone() %></td>
                <td><%= r.getRoomTypeName() %></td>
                <td><%= r.getCheckIn() %></td>
                <td><%= r.getCheckOut() %></td>
                <td><%= r.getNights() %></td>
                <td><%= r.getTotal() %></td>
            </tr>
            <% }} else { %>
            <tr><td colspan="8">No results found.</td></tr>
            <% } %>
        </table>
    </div>
    <% } %>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>