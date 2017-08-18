package tutorial.actions

import tutorial.classes._

abstract class Action {
  val executor: User
  def validate (ps: List[Boolean]): Unit = {
    if (ps.exists(_ == false))
      throw new Exception
  }
}

/*
 Traits
 */

trait RequiresRoomMembership extends Action {
  val executor: User
  val room: Room
  // validators
  val executorIsRoomMember = room.members contains executor
  validate(List(executorIsRoomMember))
}

// TODO odd that this is an abstract class, while everything else is a trait
abstract class PostingAction (
  executor: User,
  room: Room,
  readable: Readable
) extends Action
    with RequiresRoomMembership {
  val executedByAuthor = executor == readable.poster
  validate(List(executedByAuthor))
}

trait RequiresExistingPost extends PostingAction {
  val room: Room
  val post: Post
  // validators
  val roomContainsPost = room.posts contains post
  validate(List(roomContainsPost))
}

trait RequiresExistingComment extends PostingAction {
  val post: Post
  val comment: Comment
  // validators
  // TODO almost identical to RequiresExistingPost
  val postContainsComment = post.comments contains comment 
  validate(List(postContainsComment))
}


/* Room actions */

case class JoinRoomAction (
  executor: User,
  room: Room,
  ) extends Action {
  val executorNotInRoom = !(room.members contains executor)
  validate(List(executorNotInRoom))
}

case class LeaveRoomAction (
  executor: User,
  room: Room,
  ) extends Action with RequiresRoomMembership {
  val executorInRoom = (room.members contains executor)
  validate(List(executorInRoom))
}


case class AddPostAction (
  executor: User,
  room: Room,
  post: Post,
  ) extends PostingAction(executor, room, post)

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

/* Application actions */

// TODO switchRoom(app, room)
// - must be a member of the room you're switching to!
 // TODO joinRoom(app, room)
 // - call RoomActions.joinRoom
 // - add to joinedRooms
 // TODO leaveRoom(app, room)
 // - opposite of above
 // TODO notify(app, notification)

