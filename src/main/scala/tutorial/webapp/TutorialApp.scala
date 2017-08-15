package tutorial.webapp

// import scala.scalajs.js.annotation.JSExportTopLevel
import java.net.URL

trait Readable {
  var unread: Boolean = true
  def read (): Unit = {
    unread = false
  }
}

case class User(
  name: String,
  online: Boolean = false
) {
  override def toString = name
}

// TODO timestamp required...?
case class Comment (
  poster: User,
  body: String,
) extends Readable {
  override def toString = s"[${poster}] ${body}"
}

// TODO timestamp required...?
case class Post(
  poster: User,
  url: URL,
  title: Option[String],
  var comments: List[Comment] = List()
) extends Readable {
  def add (c: Comment): Unit = {
    comments = comments :+ c
  }
  def remove (c: Comment): Unit = {
    comments = comments.filterNot(comment => comment == c)
  }
}

case class Room(
  creator: User,
  name: String
) {

  // members should include only creator for now
  var members: Set[User] = Set(creator)
  // no posts for now
  var posts: Set[Post] = Set()
  // we can add a post or remove a post
  def addPost (p: Post): Unit = {
    posts = posts + p
  }
  def removePost (p: Post): Unit = {
    posts = posts - p
  }
  // we can also add or remove members
  def addMember (m: User): Unit = {
    members = members + m
  }
  def removeMember (m: User): Unit = {
    members = members - m
  }
}

case class App(
  var loggedInAs: Option[User] = None,
  var joinedRooms: Set[Room] = Set(),
  var currentRoom: Option[Room] = None
) {

  private def checkLoggedIn (cb: Unit => Unit) {
    if (loggedInAs == None)
      throw new Exception("Must be logged in to do that")
    else
      cb()
  }

  def logInAs (u: User): Unit = {
    loggedInAs = Some(u)
  }

  def logOut (): Unit = {
    checkLoggedIn(_ => {
                    loggedInAs = None
                  })
  }
  def join (r: Room): Unit = {
    checkLoggedIn(_ => {
                    joinedRooms = joinedRooms + r
                    currentRoom = Some(r)
                  })
  }
  def leave (r: Room): Unit = {
    checkLoggedIn(_ => {
                    joinedRooms = joinedRooms - r
                    // if you were in the room you just left
                    if (currentRoom == Some(r))
                      // now you're in no room
                      currentRoom = None
                  })
    }
}
