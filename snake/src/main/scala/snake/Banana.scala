package snake

case class Banana(override val game: SnakeGame) extends CanTeleport {

  override def teleportAfterSteps = 100
  def draw():  Unit = {
    game.drawBlock(pos.x, pos.y, Colors.Banana)
    game.drawBlock(pos.x, pos.y + 1, Colors.Banana)
    game.drawBlock(pos.x, pos.y - 1, Colors.Banana)
  }

  def erase():  Unit = {
    game.eraseBlock(pos.x, pos.y)
    game.eraseBlock(pos.x, pos.y + 1)
    game.eraseBlock(pos.x, pos.y -1)
  }

  def isOccupyingBlockAt(p: Pos): Boolean = {
    if(p.x == pos.x) {
      if(p.y == pos.y || p.y == pos.y + 1 || p.y == pos.y - 1) return true
    }
    false
  }
}