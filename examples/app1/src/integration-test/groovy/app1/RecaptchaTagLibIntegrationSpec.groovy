package app1

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * Requests a real page from the running application to prove the tag library is
 * registered under the 'recaptcha' namespace and renders through the service.
 */
@Integration
class RecaptchaTagLibIntegrationSpec extends Specification {

    static final String SITE_KEY = '6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI'

    private String getIndexPage() {
        new URL("http://localhost:$serverPort/captcha/index").text
    }

    void 'the recaptcha tag renders the widget markup'() {
        when:
        def page = indexPage

        then:
        page.contains('class="g-recaptcha"')
        page.contains(/data-sitekey="$SITE_KEY"/)
        page.contains('data-theme="dark"')
    }

    void 'ifEnabled renders its body and ifDisabled does not'() {
        when:
        def page = indexPage

        then:
        page.contains('id="captcha-enabled"')
        !page.contains('id="captcha-disabled"')
    }

    void 'the script tag renders both inline and standalone'() {
        when:
        def page = indexPage

        then: 'once for the automatically rendered captcha, once for the standalone script tag'
        page.count('https://www.google.com/recaptcha/api.js?"') == 2
    }

    void 'recaptchaExplicit renders an explicit-mode script tag'() {
        when:
        def page = indexPage

        then:
        page.contains('render=explicit')
        page.contains('onload=onloadCallback')
    }

    void 'renderParameters renders the grecaptcha.render() arguments'() {
        when:
        def page = indexPage

        then:
        page.contains("{ 'sitekey': '$SITE_KEY', 'theme': 'dark', 'type': 'audio', 'tabindex': '2'}")
    }

    void 'ifFailed does not render when no verification has failed'() {
        when:
        def page = indexPage

        then:
        !page.contains('id="verification-failed"')
    }
}
