package app1

import com.megatome.grails.RecaptchaService

/**
 * Exercises the plugin the way a real consumer does: the GSP renders the captcha
 * through the tag library, and this controller verifies the answer through the service.
 */
class CaptchaController {

    RecaptchaService recaptchaService

    def index() {
    }

    def verify() {
        def verified = recaptchaService.verifyAnswer(session, request.remoteAddr, params)
        if (verified) {
            recaptchaService.cleanUp(session)
        }
        render(view: 'index', model: [verified: verified])
    }
}
