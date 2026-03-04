<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Ocean View Resort - Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style.css">
</head>

<body class="auth-body">
<div class="auth-wrap">
    <div class="auth-brand">
        <div class="logo"></div>
        <div>
            <div class="auth-title" style=": center">Ocean View Resort</div>
        </div>
    </div>

    <div class="card auth-card">
        <h1 class="auth-h1">Login</h1>
        <p class="auth-p">Admin / Staff Access</p>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/login" class="auth-form">
            <div class="field">
                <label>Username</label>
                <input class="input" type="text" name="username" required autocomplete="username">
            </div>

            <div class="field" style="margin-top:12px;">
                <label>Password</label>
                <input class="input" type="password" name="password" required autocomplete="current-password">
            </div>

            <div style="margin-top:16px;">
                <button class="btn btn-primary auth-btn" type="submit">Login</button>
            </div>
        </form>
    </div>

    <div class="auth-foot">© 2026 Ocean View Resort</div>
</div>
</body>
</html>