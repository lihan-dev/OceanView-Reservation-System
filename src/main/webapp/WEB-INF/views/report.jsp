<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<%
    String from = request.getParameter("from");
    String to = request.getParameter("to");
    List<Reservation> list = (List<Reservation>) request.getAttribute("reportList");
%>

<div class="card pad">
    <div class="h1">Reservation Report</div>
    <div class="p">Generate report between dates.</div>

    <form method="get" action="<%= request.getContextPath() %>/report" style="margin-top:12px;">
        <div class="grid">
            <div class="col-4 field">
                <label>From</label>
                <input class="input" type="date" name="from" value="<%= (from != null ? from : "") %>" required>
            </div>

            <div class="col-4 field">
                <label>To</label>
                <input class="input" type="date" name="to" value="<%= (to != null ? to : "") %>" required>
            </div>

            <div class="col-4 field" style="display:flex; align-items:flex-end; gap:10px;">
                <button class="btn btn-primary" type="submit">Generate</button>
                <a class="btn" href="<%= request.getContextPath() %>/report">Clear</a>
            </div>
        </div>
    </form>

    <div style="height:12px;"></div>

    <% if (from != null && to != null) { %>
    <div class="p">Report: <span class="badge"><%= from %></span> to <span class="badge"><%= to %></span></div>
    <div style="height:10px;"></div>

    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th><th>Guest</th><th>Phone</th><th>Room</th>
                <th>Check-in</th><th>Check-out</th><th>Nights</th><th>Total</th>
            </tr>

            <% if (list != null && !list.isEmpty()) {
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
            </tr>
            <% }} else { %>
            <tr><td colspan="8">No reservations found in this date range.</td></tr>
            <% } %>
        </table>
    </div>
    <% } %>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>