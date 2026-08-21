<html>
<head>
    <meta name="layout" content="main"/>
    <title>ReCaptcha example</title>
    <script type="text/javascript">
        var onloadCallback = function () {
            grecaptcha.render('explicit_captcha', <recaptcha:renderParameters theme="dark" type="audio" tabindex="2"/>);
        };
    </script>
</head>
<body>

<h1>ReCaptcha example</h1>

<g:if test="${verified != null}">
    <p id="verification-result">Verification result: ${verified}</p>
</g:if>

<recaptcha:ifFailed>
    <p id="verification-failed">The captcha answer was rejected.</p>
</recaptcha:ifFailed>

<recaptcha:ifEnabled>
    <p id="captcha-enabled">The captcha is enabled.</p>
</recaptcha:ifEnabled>

<recaptcha:ifDisabled>
    <p id="captcha-disabled">The captcha is disabled.</p>
</recaptcha:ifDisabled>

<h2>Automatic rendering</h2>
<g:form controller="captcha" action="verify" method="post">
    <recaptcha:ifEnabled>
        <recaptcha:recaptcha theme="dark"/>
    </recaptcha:ifEnabled>
    <g:submitButton name="submit" value="Verify"/>
</g:form>

<h2>Explicit rendering</h2>
<recaptcha:ifEnabled>
    <recaptcha:recaptchaExplicit loadCallback="onloadCallback"/>
    <div id="explicit_captcha"></div>
</recaptcha:ifEnabled>

<h2>Separate script tag</h2>
<recaptcha:ifEnabled>
    <recaptcha:recaptcha includeScript="false"/>
</recaptcha:ifEnabled>
<recaptcha:script/>

</body>
</html>
