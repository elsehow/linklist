package tutorial.webapp

// import scala.scalajs.js.annotation.JSExportTopLevel

class User(username: String) {
  // require(d != 0)
  val name: String = username
  val online: Boolean = false
  override def toString = name
}

class Comment(u: User, s: String) {
  val poster: User = u
  val body: String = s
  override def toString = s"${poster} - ${body}"
  // TODO timestamp required...?
}


class Post(u: User, l: String, t: Option[String]) {
  val poster: User = u
  val url: String = l
  val title: Option[String] = t
  val comments: List[Comment] = Nil
  // TODO timestamp required...?
}

class Room(c: User, n: String) {
  val creator: User = c
  val name: String = n
  val members: Set[User] = Set() // TODO should include only creator
  val posts: Set[Post] = Set()
  // TODO add/remove post
  // TODO add/remove member
}

class App() {
  val loggedInAs: Option[User] = None
  val room: Option[Room] = None
  // TODO log in
  // TODO join room
}
