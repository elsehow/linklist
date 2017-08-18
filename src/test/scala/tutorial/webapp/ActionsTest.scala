package tutorial.actions

import utest._
import java.net.URL
import tutorial.classes._

object RoomActionsTest extends TestSuite {

  def tests = TestSuite {
    // two users
    val bob = User("bob")
    val alice = User("alice")
    // and one "malicious" user
    // who's not in the room!
    val mal = User("mallor")

    val testUrl = new URL("http://watevs")

    // a comment from each of them
    val commentBody = "Hmm"
    val aliceComment = Comment(alice, commentBody)
    val bobComment = Comment(bob, commentBody)
    val malComment = Comment(mal, commentBody)

    // a post from each of them
    val alicePost = Post(alice, testUrl)
    // bob's post has a comment
    val bobPost = Post(bob, testUrl, comments=List(aliceComment))
    val malPost = Post(mal, testUrl)

    // bob creates a room
    // and puts his post in it
    val room = Room("My room",
                    Set(bob, alice),
                    List(bobPost))

    'AddPostAction {
      // bob can add his own post to room
      AddPostAction(bob, room, bobPost)
      // alice can add her own post to room
      AddPostAction(alice, room, alicePost)
      // mal cannot add a post, she's not in the room
      intercept[Exception] {
        AddPostAction(mal, room, malPost)
      }
      // mal cannot add a post,
      // even if it's authored by Bob
      intercept[Exception] {
        AddPostAction(mal, room, bobPost)
      }
      // Bob cannot add a post by Alice
      intercept[Exception] {
        AddPostAction(bob, room, alicePost)
      }
      // Alice cannot add a post by Bob
      intercept[Exception] {
        AddPostAction(alice, room, bobPost)
      }
    }

    'RemovePostAction {
      // bob can remove his own post from a room
      RemovePostAction(bob, room, bobPost)
      // mal cannot delete bob's post -
      // she's not in the room!
      intercept[Exception] {
        RemovePostAction(mal, room, bobPost)
      }
      // alice cannot delete bob's post -
      // she didn't author it!
      intercept[Exception] {
        RemovePostAction(alice, room, bobPost)
      }
      // bob can't delete a post that's not in the room!
      intercept[Exception] {
        val bobPost2 = Post(bob, new URL("http://hi"))
        RemovePostAction(bob, room, bobPost2)
      }
      // NOTE bob can delete alice's post, he is the moderator?
    }

    'AddCommentAction {
      // bob can comment on his post
      AddCommentAction(bob, room, bobPost, bobComment)
      // alice can comment on bob's post
      AddCommentAction(alice, room, bobPost, aliceComment)
      // alice cannot post bob's comment
      intercept[Exception] {
        AddCommentAction(alice, room, bobPost, bobComment)
      }
      // bob cannot post alice's comment
      intercept[Exception] {
        AddCommentAction(bob, room, bobPost, aliceComment)
      }
      // bob can't comment on a post that doesn't exist
      intercept[Exception] {
        AddCommentAction(bob, room, alicePost, bobComment)
      }
      // mal cannot comment on bob's post
      intercept[Exception] {
        AddCommentAction(mal, room, bobPost, malComment)
      }
      // mal cannot comment on bob's post even with bob's comment
      intercept[Exception] {
        AddCommentAction(mal, room, bobPost, bobComment)
      }
    }

    'RemoveCommentAction {
      // alice can remove her own comment
      RemoveCommentAction(alice, room, bobPost, aliceComment)
      // bob cannot remove alice's comment
      intercept[Exception] {
        RemoveCommentAction(bob, room, bobPost, aliceComment)
      }
      // mal cannot remove bob's comment
      intercept[Exception] {
        RemoveCommentAction(mal, room, bobPost, aliceComment)
      }
      // bob cannot remove a comment that doesn't exist
      intercept[Exception] {
        RemoveCommentAction(bob, room, bobPost, bobComment)
      }
      // NOTE bob can remove alice's comment, he is the moderator?
    }

    'JoinRoomAction {
      // mal can join the room
      JoinRoomAction(mal, room)
      // bob cannot join the room, he is already a member
      intercept[Exception] {
        JoinRoomAction(bob, room)
      }
    }

    'LeaveRoomAction {
      // alice can leave the room
      LeaveRoomAction(alice, room)
      // mal cannot leave the room, she is not a member
      intercept[Exception] {
        LeaveRoomAction(mal, room)
      }
    }

  }
}
