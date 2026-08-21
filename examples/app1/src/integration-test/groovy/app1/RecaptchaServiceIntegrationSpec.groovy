package app1

import com.megatome.grails.RecaptchaService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * Proves the plugin registers RecaptchaService as a Spring bean and that it reads its
 * configuration from the application's application.yml.
 */
@Integration
class RecaptchaServiceIntegrationSpec extends Specification {

    static final String SITE_KEY = '6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI'

    RecaptchaService recaptchaService

    void 'the service is wired into the application context'() {
        expect:
        recaptchaService
    }

    void 'the captcha is enabled by the configuration in application.yml'() {
        expect:
        recaptchaService.isEnabled()
    }

    void 'createCaptcha renders the site key from the application configuration'() {
        when:
        def html = recaptchaService.createCaptcha([:])

        then:
        html.contains(/data-sitekey="$SITE_KEY"/)

        and: 'includeScript is true in the configuration, so the script tag is inlined'
        html.contains('<script src="https://www.google.com/recaptcha/api.js?" async defer></script>')

        and: 'includeNoScript is false in the configuration'
        !html.contains('<noscript>')
    }

    void 'createCaptcha maps tag attributes onto ReCaptcha data attributes'() {
        when:
        def html = recaptchaService.createCaptcha(theme: 'dark', size: 'compact', type: 'audio', tabindex: '2')

        then:
        html.contains('data-theme="dark"')
        html.contains('data-size="compact"')
        html.contains('data-type="audio"')
        html.contains('data-tabindex="2"')
    }

    void 'includeScript can be overridden per captcha'() {
        when:
        def html = recaptchaService.createCaptcha(includeScript: 'false')

        then:
        !html.contains('<script')
    }

    void 'createScriptEntry renders the script tag on its own'() {
        expect:
        recaptchaService.createScriptEntry(lang: 'da') ==
                '<script src="https://www.google.com/recaptcha/api.js?hl=da" async defer></script>'
    }

    void 'createCaptchaExplicit renders an explicit-mode script tag'() {
        when:
        def html = recaptchaService.createCaptchaExplicit(loadCallback: 'onloadCallback')

        then:
        html.contains('render=explicit')
        html.contains('onload=onloadCallback')
    }

    void 'createRenderParameters renders the parameters for grecaptcha.render()'() {
        expect:
        recaptchaService.createRenderParameters(theme: 'dark', type: 'audio') ==
                "{ 'sitekey': '$SITE_KEY', 'theme': 'dark', 'type': 'audio'}"
    }

    void 'validationFailed tracks the failure flag on the session'() {
        given:
        def session = [:]

        expect:
        !recaptchaService.validationFailed(session)

        when:
        session['recaptcha_error'] = true

        then:
        recaptchaService.validationFailed(session)

        when:
        recaptchaService.cleanUp(session)

        then:
        !recaptchaService.validationFailed(session)
    }
}
