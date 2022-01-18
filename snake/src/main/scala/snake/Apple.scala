package snake

case class Apple(override val game: SnakeGame) extends CanTeleport {

  override def teleportAfterSteps = 100
  def draw():  Unit = {
    game.drawBlock(pos.x, pos.y, Colors.Apple)
  }

  def erase():  Unit = {
    game.eraseBlock(pos.x, pos.y)
  }

  def isOccupyingBlockAt(p: Pos): Boolean = {
    p == pos
  }
}