package pingpong

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PongController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Pong.list(params), model:[pongCount: Pong.count()]
    }

    def show(Pong pong) {
        respond pong
    }

    def create() {
        respond new Pong(params)
    }

    @Transactional
    def save(Pong pong) {
        if (pong == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (pong.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond pong.errors, view:'create'
            return
        }

        pong.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pong.label', default: 'Pong'), pong.id])
                redirect pong
            }
            '*' { respond pong, [status: CREATED] }
        }
    }

    def edit(Pong pong) {
        respond pong
    }

    @Transactional
    def update(Pong pong) {
        if (pong == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (pong.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond pong.errors, view:'edit'
            return
        }

        pong.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pong.label', default: 'Pong'), pong.id])
                redirect pong
            }
            '*'{ respond pong, [status: OK] }
        }
    }

    @Transactional
    def delete(Pong pong) {

        if (pong == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        pong.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'pong.label', default: 'Pong'), pong.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pong.label', default: 'Pong'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
