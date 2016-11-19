package pingpong

/**
 * Domain with a double and a number of Pongs.
 */
class Ping {
  Double weight

  static hasMany = [pongs: Pong]

  static constraints = {
    weight min: 0.0d, max: 100.0d
  }
}
