package demo

class DemoTagLib {

    static namespace = 'demo'

    GreetingService greetingService

    def greeting = { attrs ->
        out << greetingService.greet(attrs.name ?: 'stranger')
    }
}
