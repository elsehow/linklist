package tutorial.actions

import utest._
import java.net.URL
import tutorial.classes._

object RoomActionsTest extends TestSuite {

  def tests = TestSuite {

    val bob = User("bob")
    val testUrl = new URL("http://watevs")
    // bob creates a room
    val r = Room(bob, "My room")
    // bob makes a post
    val p = new Post(bob, testUrl, Some("Interesting"))
    // and writes a comment for his post (whatever bob)
    val c = new Comment(bob, "very nice")
    // (there's also a sneaky user who's *not* a room member)
    val mallory = User("mallory")
    // bob adds the post to the room (making a new room)
    val r2 = RoomActions.addPost(r, p)

    'addPost {
      assert(
        r2.posts == List(p)
      )
      // add another post
      val p2 = new Post(bob, testUrl, Some("v interesting"))
      val r3 = RoomActions.addPost(r2, p2)
      assert(
        r3.posts == List(p2, p)
      )

      // someone who is in the room CANNOT add a post
      val pMal = new Post(mallory, testUrl, Some("hax"))
      intercept[Exception] {
        RoomActions.addPost(r3, pMal)
      }
    }

    'addComment{
      val r3 = RoomActions.addComment(r2, p, c)
      assert(
        r3.posts.head.comments == List(c)
      )

      // someone who is not in the room CANNOT add a comment
      val cMal = new Comment(mallory, "hax")
      intercept[Exception] {
        RoomActions.addComment(r3, p, cMal)
      }
    }

    'removePost {
      val r3 = RoomActions.removePost(r2, bob, p)
      assert(
        r3.posts == List()
      )
      // somone who did not post the post cannot remove it
      intercept[Exception] {
        RoomActions.removePost(r2, mallory, p)
      }
      // TODO remove a post that isn't there?
      // TODO you can ALSO remove a post if you're a moderator
    }

    'removeComment{
      val r3 = RoomActions.addComment(r2, p, c)
      val r4 = RoomActions.removeComment(r3, bob, p, c)
      assert(
        r4.posts.head.comments == List()
      )
      // somone who did not post the post cannot remove it
      intercept[Exception] {
        RoomActions.removeComment(r3, mallory, p, c)
      }
      // TODO remove a comment that isn't there?
      // TODO you can ALSO remove a post if you're a moderator
    }


  }
}
