package demo

class PersonController {

    PersonService personService

    def index() {
        [people: Person.list(sort: 'lastName')]
    }

    def show(Long id) {
        def person = Person.get(id)
        if (!person) {
            render(status: 404)
            return
        }
        respond(person)
    }

    def hello(String name) {
        render("Hello, ${name ?: 'World'}!")
    }
}
