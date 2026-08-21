package com.megatome.grails

import grails.plugins.Plugin

class RecaptchaGrailsPlugin extends Plugin {

    def grailsVersion = '7.0.0 > *'
    def pluginExcludes = [
            'grails-app/views/error.gsp'
    ]

    def title = 'Grails ReCaptcha Plugin'
    def author = 'Chad Johnston'
    def authorEmail = 'cjohnston@megatome.com'
    def description = '''\
Adds Google ReCaptcha support to Grails applications through a tag library for rendering the captcha widget and a service for verifying the user's response.
'''
    def profiles = ['web']
    def documentation = 'https://gpc.github.io/grails-recaptcha-plugin/'
    def license = 'APACHE'
    def organization = [name: 'Grails Plugins Collective', url: 'https://github.com/gpc']
    def issueManagement = [system: 'GitHub', url: 'https://github.com/gpc/grails-recaptcha-plugin/issues']
    def scm = [url: 'https://github.com/gpc/grails-recaptcha-plugin/']
}
