package demo

class AuditInterceptor {

    AuditInterceptor() {
        match(controller: 'person')
    }

    boolean before() {
        request.audited = true
        true
    }
}
