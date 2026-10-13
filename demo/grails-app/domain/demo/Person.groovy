package demo

class Person {

    String firstName
    String lastName

    static constraints = {
        firstName(blank: false)
        lastName(blank: false)
    }

    String getFullName() { "$firstName $lastName" }
}
