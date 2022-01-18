package snake

class Snake (
              val initPos: Pos,
              val initDir: Dir,
              val headColor: java.awt.Color,
              val tailColor: java.awt.Color,
              val game: SnakeGame
            ) extends CanMove {
  var dir: Dir = initDir

  val initBody: List[Pos] = List(initPos + initDir, initPos)

  var body: scala.collection.mutable.Buffer[Pos] = initBody.toBuffer

  val initTailSize: Int = 10 // välj själv vad som är lagom svårt

  var nbrOfStepsSinceReset = 0
  val growEvery = 10
  val startGrowingAfter = 400
  var nbrOfApples = 0

  // återställ starttillstånd, ge rätt svanslängd
  def reset(): Unit = {
    dir = initDir

    body.clear()
    for(a <- initBody) body += a

    for (i <- 1 to initTailSize) {
      body += Pos(body(1).x, body(1).y, Dim(game.dim))
    }
  }

  // väx i rätt riktning med extra svansposition
  def grow(): Unit = {
    body.prepend(Pos(body(0).x + dir.x, body(0).y + dir.y, Dim(game.dim)))
  }

  // krymp svansen om kroppslängden är större än 2
  def shrink(): Unit = {
    if (body.length > 2) body -= (body(body.length - 1))
  }

  // kolla om p finns i kroppen
  def isOccupyingBlockAt(p: Pos): Boolean = {
    body.contains(p)
  }

  // kolla om huvudena krockar
  def isHeadCollision(other: Snake): Boolean = {
    // TODO: referens eller innehållslikhet?
    other.body(0) == body(0)
  }

  // mitt huvud i annans svans
  def isTailCollision(other: Snake): Boolean = {
    var found = false
    for (i <- other.body.indices) {
      if (i == body(0)) found = true
    }
    found
  }

  // väx och krymp enl. regler; action om äter frukt
  def move(): Unit = {
    grow()
    var shouldShrink = true
    game.entities.foreach{e =>
      e match {
        case x: CanTeleport => {
          if(x.isOccupyingBlockAt(body(0))) {
            if(x.isInstanceOf[Banana]) shouldShrink = false
            else nbrOfApples += 1
            x.reset()
          }
        }
        case x: Snake => {
          if(x != this) {
            if(x.isOccupyingBlockAt(body(0))) {
              game.enterGameOverState(this)
            }
          }
        }
        case _ =>
      }
    }

    nbrOfStepsSinceReset += 1
    if(nbrOfStepsSinceReset - startGrowingAfter > 0 && Math.floorMod(nbrOfStepsSinceReset - startGrowingAfter, growEvery) == 0) {
      nbrOfStepsSinceReset = 0
      shouldShrink = false
    }

    if ( (body.length > initTailSize + 1) && shouldShrink) shrink()

    /**
    // Moving snake from last bit if tail to first
    for (i <- (body.length - 1) to 1) {
      body(i).x = body(i - 1)
      body(i).y = body(i - 1)
    }

    // Head
    body(0).x = body(0).x + dir.x
    body(0).y = body(0).y + dir.y
      */
  }

  def draw(): Unit = {
    // Head
    game.drawBlock(body(0).x, body(0).y, headColor)

    // Tail
    for (i <- 1 to (body.length - 1)) {
      game.drawBlock(body(i).x, body(i).y, tailColor)
    }

    //println(s"${body(0).x}, ${body(0).y}")

  }

  // TODO: skall ormen helt bort? målas över eller helt försvinna?
  def erase(): Unit = {
    for (i <- 0 to (body.length - 1)) {
      game.eraseBlock(body(i).x, body(i).y)
    }
  }

  override def toString =  // bra vid println-debugging
    body.map(p => (p.x, p.y)).mkString(">:)", "~", s" going $dir")
}
