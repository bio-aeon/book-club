package trading

import cats.{ Eq, Order, Show }
import io.circe.{ Decoder, Encoder }
import monocle.Iso
import trading.Wrapper
import eu.timepit.refined.api.{ Refined, RefinedType, RefinedTypeOps }

abstract class Newtype[A](using
                          eqv: Eq[A],
                          ord: Order[A],
                          shw: Show[A],
                          enc: Encoder[A],
                          dec: Decoder[A]
                         ):
  opaque type Type = A

  inline def apply(a: A): Type = a

  protected inline final def derive[F[_]](using ev: F[A]): F[Type] = ev

  extension (t: Type) inline def value: A = t

  given Wrapper[A, Type] with
    def iso: Iso[A, Type] =
      Iso[A, Type](apply(_))(_.value)

  given Eq[Type]       = eqv
  given Order[Type]    = ord
  given Show[Type]     = shw
  given Encoder[Type]  = enc
  given Decoder[Type]  = dec
  given Ordering[Type] = ord.toOrdering

abstract class RefNewtype[T, RT](using
                                 eqv: Eq[RT],
                                 ord: Order[RT],
                                 shw: Show[RT],
                                 enc: Encoder[RT],
                                 dec: Decoder[RT],
                                 rt: RefinedType.AuxT[RT, T]
                                ) extends Newtype[RT]:
  object Ops extends RefinedTypeOps[RT, T]
  def from(t: T): Either[String, Type] = Ops.from(t).map(apply(_))
  def unsafeFrom(t: T): Type           = apply(Ops.unsafeFrom(t))

abstract class NumNewtype[A](using
                             eqv: Eq[A],
                             ord: Order[A],
                             shw: Show[A],
                             enc: Encoder[A],
                             dec: Decoder[A],
                             num: Numeric[A]
                            ) extends Newtype[A]:

  extension (x: Type)
    inline def -[T](using inv: T =:= Type)(y: T): Type = apply(num.minus(x.value, inv.apply(y).value))
    inline def +[T](using inv: T =:= Type)(y: T): Type = apply(num.plus(x.value, inv.apply(y).value))