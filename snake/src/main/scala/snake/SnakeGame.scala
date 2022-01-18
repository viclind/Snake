package snake
import java.awt.Color

import introprog.BlockGame

abstract class SnakeGame(title: String) extends BlockGame(
  title, dim = (50, 30), blockSize = 15, background = Colors.Background,
  framesPerSecond = 50, messageAreaHeight = 3
) {
  var entities: Vector[Entity] = Vector.empty

  var players: Vector[Player] = Vector.empty

  sealed trait State
  case object Starting extends State
  case object Playing  extends State
  case object GameOver extends State
  case object Quitting extends State

  var state: State = Starting

  def enterStartingState(): Unit = {
    state = Starting
    pixelWindow.clear()
    pixelWindow.drawText("Tryck [space] för att starta", 100, 100, Colors.Green)
  } // rensa, meddela "tryck space för start"

  def enterPlayingState(): Unit = {
    state = Playing
    pixelWindow.clear()
    entities.foreach(_.reset())
    entities.foreach(_.draw())
  } // rensa, rita alla entiteter

  def enterGameOverState(snake: Snake): Unit = {
    state = GameOver
    pixelWindow.fill(0, 0, pixelWindow.width, pixelWindow.height, new Color(0, 0, 0, 100))

    pixelWindow.drawText("Game Over! Tryck [space] för att starta om", 100, 100, Colors.Green)
    val p1 = players(0).snake
    val p2 = players(1).snake
    pixelWindow.drawText(s"${players(0).name}: ${p1.nbrOfApples}", 100, 130, Colors.Green)
    pixelWindow.drawText(s"${players(1).name}: ${p2.nbrOfApples}", 100, 160, Colors.Green)
    if(p2 == snake) pixelWindow.drawText(s"${players(0).name} vann!", 100, 190, Colors.Green)
    else pixelWindow.drawText(s"${players(1).name} vann!", 100, 190, Colors.Green)

  } // meddela "game over"

  def enterQuittingState(): Unit = {
    println("Goodbye!")
    pixelWindow.hide()
    state = Quitting
  }

  def randomFreePos(): Pos = {
    var pos = Pos(0, 0, Dim(dim))
    var freePos = false
    while(!freePos) {
      pos = Pos((Math.random() * (dim._1 - 2) + 1).toInt, (Math.random() * (dim._2 - 2) + 1).toInt, Dim(dim))
      freePos = !entities.exists(_.isOccupyingBlockAt(pos))
    }
    pos
  } // dra slump-pos tills ledig plats, används av frukt

  override def onKeyDown(key: String): Unit = {
    //println(s"""key "$key" pressed""")
    state match {
      case Starting => if (key == " ") enterPlayingState()
      case Playing => {
        players.foreach(_.handleKey(key))
      }
      case GameOver =>
        if (key == " ") enterPlayingState()
          else if(key == "Escape") enterQuittingState()
      case _ =>
    }
  }

  override def onClose(): Unit = enterQuittingState()

  def isGameOver(): Boolean  // abstrakt metod, implementeras i subklass

  override def gameLoopAction(): Unit = {
    if(state == Playing) {
      entities.foreach(_.update())
      entities.foreach(_.draw())
    }
  }

  def startGameLoop(): Unit = {
    pixelWindow.show()  // möjliggör omstart även om fönstret stängts...
    enterStartingState()
    gameLoop(stopWhen = state == Quitting)
  }

  def play(playerNames: String*): Unit // abstrakt, implementeras i subklass
}
