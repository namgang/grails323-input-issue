package grails323.input.issue

import pingpong.Ping
import pingpong.Pong

class BootStrap {

    def init = { servletContext ->
      def ping = new Ping(weight: 9.9d).save(failOnError: true)
      def ponga = new Pong(name: 'Alpha')
      def pongb = new Pong(name: 'Beta')
      def pongg = new Pong(name: 'Gamma')
      ping.addToPongs(ponga).save(failOnError: true)
      ping.addToPongs(pongb).save(failOnError: true)
      ping.addToPongs(pongg).save(failOnError: true)
    }
    def destroy = {
    }
}
