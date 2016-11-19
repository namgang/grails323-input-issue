package pingpong

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PingController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Ping.list(params), model:[pingCount: Ping.count()]
    }

    def show(Ping ping) {
        respond ping
    }

    def create() {
        respond new Ping(params)
    }

    @Transactional
    def save(Ping ping) {
        if (ping == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (ping.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond ping.errors, view:'create'
            return
        }

        ping.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ping.label', default: 'Ping'), ping.id])
                redirect ping
            }
            '*' { respond ping, [status: CREATED] }
        }
    }

    def edit(Ping ping) {
        respond ping
    }

    @Transactional
    def update(Ping ping) {
        if (ping == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (ping.hasErrors()) {
	  if (log.debugEnabled) log.debug "--PING UPDATE ERRORS: ${ping.errors.allErrors.join('|')}"
            transactionStatus.setRollbackOnly()
            respond ping.errors, view:'edit'
            return
        }

        ping.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ping.label', default: 'Ping'), ping.id])
                redirect ping
            }
            '*'{ respond ping, [status: OK] }
        }
    }

    @Transactional
    def delete(Ping ping) {

        if (ping == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        ping.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ping.label', default: 'Ping'), ping.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ping.label', default: 'Ping'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
