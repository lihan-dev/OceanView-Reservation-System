<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/_header.jspf" %>

<div class="card pad">
    <div class="h1">System Help Guide</div>
    <div class="p">Welcome to Ocean View Resort Reservation System.</div>
</div>

<div style="height:15px;"></div>

<div class="card pad">
    <h3>How To Use The System</h3>

    <ol>
        <li><b>Login:</b> Enter your assigned username and password.</li>
        <li><b>Add Reservation:</b> Click “Add Reservation” and fill guest details correctly.</li>
        <li><b>View Reservations:</b> See all existing bookings.</li>
        <li><b>Search Reservation:</b> Search using reservation ID.</li>
        <li><b>Generate Reports:</b> View income and booking statistics.</li>
        <li><b>Logout:</b> Always logout after finishing work.</li>
    </ol>
</div>

<div style="height:15px;"></div>

<div class="card pad">
    <h3>Important Notes</h3>
    <ul>
        <li>All fields must be filled correctly.</li>
        <li>Reservation number must be unique.</li>
        <li>System automatically calculates total income.</li>
    </ul>
</div>

<%@ include file="/WEB-INF/views/_footer.jspf" %>