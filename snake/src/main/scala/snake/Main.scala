package snake

import introprog.Dialog

object Main {
  def main(args: Array[String]): Unit = {
    val buttons = Seq("One","Two", "Tournament", "Cancel")
    val selected =
      introprog.Dialog.select("Number of players?", buttons, "Snake")
    selected match {
      case "One" => (new OnePlayerGame).play("Green")
      case "Two" => {
        val p1 = Dialog.input("Player 1 name:")
        val p2 = Dialog.input("Player 2 name:")
        (new TwoPlayerGame).play(p1, p2)
      }
      case "Tournament" => ??? // valbart krav
      case _ => println("Exiting main... Goodbye!")
    }
  }
}
