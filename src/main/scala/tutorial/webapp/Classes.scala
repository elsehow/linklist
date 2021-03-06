package tutorial.classes

// import scala.scalajs.js.annotation.JSExportTopLevel
import java.net.URL

sealed case class User(
  name: String,
  online: Boolean = false
)

// TODO timestamps?
sealed abstract class Readable {
  val poster: User
  val unread: Boolean = true
  // val timestamp: Int
}

// type Feed = List[Readable]

sealed case class Comment (
  poster: User,
  body: String
) extends Readable

sealed case class Post(
  poster: User,
  url: URL,
  title: Option[String],
  comments: List[Comment] = List()
) extends Readable

sealed case class Room(
  creator: User,
  name: String,
  posts: List[Post] = List(),
) {
  val members: Set[User] = Set(creator)
}

sealed case class Notification(
  body: Readable
)

sealed case class AppState(
  loggedInAs: Option[User] = None,
  joinedRooms: List[Room] = List(),
  currentRoom: Option[Room] = None,
  notifications: List[Notification] = List(),
)
