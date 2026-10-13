<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>People</title>
</head>
<body>
<ul id="people">
    <g:each in="${people}" var="person">
        <li>${person.fullName}</li>
    </g:each>
</ul>
</body>
</html>
