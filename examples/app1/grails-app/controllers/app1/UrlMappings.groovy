package app1

class UrlMappings {

    static mappings = {
        '/'(redirect: '/captcha')
        "/$controller/$action?/$id?(.$format)?" { }
        '500'(view: '/error')
        '404'(view: '/notFound')
    }
}
