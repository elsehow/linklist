package tutorial.actions

import tutorial.classes._

import scalaz._
import scalaz.Scalaz._

// RoomActions
object RoomActions {

  /*
   Utility functions
   */

  def replace[A](original: A, replacement: A, l: List[A]): List[A] = {
    l.patch(
      l.indexOf(original),
      Seq(replacement),
      1
    )
  }

  def remove[A](item: A, l: List[A]): List[A] = {
    l.diff(List(item))
  }

  /*
   Validators
   */

  // def validateReadableAction (readable: Readable, user: User, room: Room)
  type StringValidation[T] = Validation[String, T]

  def validate (room: Room, user: User, readable: Readable): StringValidation[Room] = {

    def posterInRoom (): StringValidation[Room] =
      if (room.members contains readable.poster) room.success
      else "Poster not in room".failure

    def originalPoster (): StringValidation[Room] =
      if (user == readable.poster) room.success
      else "User attempted to post something not created by user".failure

    (posterInRoom |@| originalPoster)({case _ => room})

  }

  // TODO VALIDATORS ---------------------------------------
  private def poster_in_room (r: Room, c: Readable): Room = {
    if (r.members contains c.poster) {
      r
    } else
        throw new Exception
  }

  private def original_poster (r: Room, u: User, c: Readable): Room = {
    if (u == c.poster) {
      r
    } else
        throw new Exception
  }
  // TODO VALIDATORS ---------------------------------------


  def addPost (r: Room, p: Post): Room = {
    // TODO user should also be post creator
    def add () = {
      r.copy(posts = (p :: r.posts))
    }
    poster_in_room(r, p)
    add
  }

  def addComment (r: Room, p: Post, c: Comment): Room = {
    // TODO user should also be comment creator
    def add () = {
      // prepend the new comment, creating a new post
      val newPost = p.copy(comments = (c :: p.comments))
      // replace the old post with the new one
      val newPosts = replace(p, newPost, r.posts)
      // create a nwe room with the new posts
      r.copy(posts=newPosts)
    }
    poster_in_room(r, c)
    add
  }

  def removePost (r: Room, u:User, p: Post): Room = {
    def rm () = {
      val newPosts = remove(p, r.posts)
      r.copy(posts=newPosts)
    }
    // TODO user should also be in room
    original_poster(r, u, p)
    rm
  }

  def removeComment (r: Room, u: User, p: Post, c: Comment): Room = {
    def rm () = {
      val newComments = remove(c, p.comments)
      val newPost = p.copy(comments = newComments)
      // TODO duplicate with addComment
      val newPosts = replace(p, newPost, r.posts)
      // create a nwe room with the new posts
      r.copy(posts=newPosts)
    }
    // TODO user should also be in room
    validate(r, u, c)
    // original_poster(r, u, c)
    rm
  }

  // TODO joinRoom(room, user)
  // - can't join if room is invite only!
  // TODO leaveRoom(room, user)
  // - only if you're a member
  // TODO inviteToRoom(room, user)
  // - only if you have moderator privs!
  // TODO appointModerator(room, user)
  // - only if you have moderator privs!
  // TODO stepDownAsModerator(room, user)
  // - only if you have moderator privs!
  // - only if there's at least 1 moderator left!
  // NOTE reactToReadable(room, user, readable, reaction)

}

object AppActions {

  // TODO switchRoom(app, room)
  // - must be a member of the room you're switching to!
  // TODO joinRoom(app, room)
  // - call RoomActions.joinRoom
  // - add to joinedRooms
  // TODO leaveRoom(app, room)
  // - opposite of above
  // TODO notify(app, notification)
}
