import Macros._

case class Alpha(x: Beta)

case class Beta(y: Gamma)

case class Gamma(z: Int)

def main(args: Array[String]): Unit = {
  assert("Hello world!".length == 5)

    val v = Gamma(1)
    val v2 = copy(v.z, 3)

//  val v = Alpha(Beta(Gamma(1)))
//  val v2 = copy(v.x.y.z, 3)
}

