package demo

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        '/people'(controller: 'person', action: 'index')
        "/hello/$name?"(controller: 'person', action: 'hello')

        '/'(view: '/index')
        '500'(view: '/error')
        '404'(view: '/notFound')
    }
}
