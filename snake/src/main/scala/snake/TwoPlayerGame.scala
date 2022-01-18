package snake

class TwoPlayerGame extends SnakeGame("Two Player Game") {  // ska ärva SnakeGame

  val snake1 = new Snake(Pos(10, 10, Dim(dim)), North, Colors.Green, Colors.DarkGreen, this)
  val snake2 = new Snake(Pos(20, 10, Dim(dim)), North, Colors.Blue, Colors.DarkBlue, this)
  // ormar och ev. äpple, bananer etc

  def play(playerNames: String*): Unit = {

    players = Vector(
    Player(playerNames(0), "A","S","D","W", snake1),
    Player(playerNames(1), "VÄNSTER", "NEDÅT", "HÖGER", "UPP", snake2))

    entities = Vector(snake1, snake2, Apple(this), Apple(this), Apple(this), Banana(this), Banana(this), Banana(this))

    startGameLoop()

  }  // ska överskugga play i SnakeGame
  override def isGameOver(): Boolean = true
}
