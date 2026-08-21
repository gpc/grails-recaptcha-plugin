<html>
<head>
    <title>Error</title>
    <meta name="layout" content="main"/>
</head>
<body>
<h1>An error has occurred</h1>
<g:if env="development"><g:renderException exception="${exception}"/></g:if>
</body>
</html>
