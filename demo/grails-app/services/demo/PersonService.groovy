package demo

import grails.gorm.transactions.Transactional

@Transactional
class PersonService {

    GreetingService greetingService

    Person create(String firstName, String lastName) {
        new Person(firstName: firstName, lastName: lastName).save(failOnError: true)
    }

    String welcome(Long id) {
        def person = Person.get(id)
        person ? greetingService.greet(person.fullName) : null
    }

    List<Person> findByLastName(String lastName) {
        Person.findAllByLastName(lastName, [sort: 'firstName'])
    }
}
