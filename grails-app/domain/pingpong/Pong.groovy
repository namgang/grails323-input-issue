package pingpong

/**
 * At the "many" end of the relationship to Ping.
 */
class Pong {
  String name

  static belongsTo = [ping: Ping]
  static constraints = {
    name maxLength: 80
  }

}
