package tutorial.actions

import tutorial.classes._

abstract class Action {
  val executor: User
}

abstract class RoomAction (
  executor: User,
  room: Room
) extends Action

// TODO odd that this is an abstract class, while RequiresExistingPost is a trait
abstract class PostingAction (
  executor: User,
  room: Room,
  readable: Readable
) {

  val executedByAuthor = executor == readable.poster
  val authorIsMemberOfRoom = (room.members contains executor)

  // TODO this part isn't so dry
  if (!executedByAuthor || !authorIsMemberOfRoom)
    throw new Exception
}

case class AddPostAction (
  executor: User,
  room: Room,
  post: Post,
) extends PostingAction(executor, room, post)


// validation traits --------------
// TODO Not dry with one another

trait RequiresExistingPost {
  val room: Room
  val post: Post

  val roomContainsPost = room.posts contains post
  // TODO this part isn't so dry
  if (!roomContainsPost)
    throw new Exception
}
trait RequiresExistingComment {
  val post: Post
  val comment: Comment

  val postContainsComment = post.comments contains comment
  // TODO this part isn't so dry
  if (!postContainsComment)
    throw new Exception
}
// --------------------------------

case class RemovePostAction (
  executor: User,
  room: Room,
  post: Post,
  ) extends PostingAction(executor, room, post)
    with RequiresExistingPost

case class AddCommentAction (
  executor: User,
  room: Room,
  post: Post,
  comment: Comment,
  ) extends PostingAction(executor, room, comment)
    with RequiresExistingPost

case class RemoveCommentAction (
  executor: User,
  room: Room,
  post: Post,
  comment: Comment,
  ) extends PostingAction(executor, room, comment)
    with RequiresExistingPost
    with RequiresExistingComment

case class JoinRoomAction (
  executor: User,
  room: Room,
  ) extends RoomAction(executor, room) {
  val executorNotInRoom = !(room.members contains executor)

  if (!executorNotInRoom)
    throw new Exception
}

case class LeaveRoomAction (
  executor: User,
  room: Room,
  ) extends RoomAction(executor, room) {
  val executorInRoom = (room.members contains executor)
  if (!executorInRoom)
    throw new Exception
}


// }

// object AppActions {

//   // TODO switchRoom(app, room)
//   // - must be a member of the room you're switching to!
//   // TODO joinRoom(app, room)
//   // - call RoomActions.joinRoom
//   // - add to joinedRooms
//   // TODO leaveRoom(app, room)
//   // - opposite of above
//   // TODO notify(app, notification)
// }
