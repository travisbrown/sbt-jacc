package dev.travisbrown.jacc

sealed abstract class Fixity extends Product with Serializable {
  def precedence: Int
}

object Fixity {
  def left(precedence: Int): Fixity = LeftAssociative(precedence)
  def right(precedence: Int): Fixity = RightAssociative(precedence)
  def nonAssociative(precedence: Int): Fixity = NonAssociative(precedence)

  private case class LeftAssociative(precedence: Int) extends Fixity
  private case class RightAssociative(precedence: Int) extends Fixity
  private case class NonAssociative(precedence: Int) extends Fixity

  implicit val order: PartialOrdering[Fixity] = new PartialOrdering[Fixity] {
    def tryCompare(x: Fixity, y: Fixity): Option[Int] =
      if (x.precedence == y.precedence) {
        (x, y) match {
          case (LeftAssociative(_), LeftAssociative(_))   => Some(1)
          case (RightAssociative(_), RightAssociative(_)) => Some(-1)
          case _                                          => None
        }
      } else Some(x.precedence - y.precedence)

    def lteq(x: Fixity, y: Fixity): Boolean = tryCompare(x, y).exists(_ < 0)
  }
}
