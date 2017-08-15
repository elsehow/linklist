package tutorial.webapp

import utest._
import java.net.URL

object TutorialTest extends TestSuite {


  def tests = TestSuite {

    val testUrl = new URL("http://watevs")

    val u = new User("nick")
    val c = new Comment(u, "whats good")
    val p = new Post(u, testUrl, Some("Interesting"))
    val r = new Room(u,  "Great links")
    val a = new App()

    'User {
      assert(
        u.name == "nick",
        u.online == false,
        u.toString() == "nick"
      )
    }

    'Comment {
      assert(
        c.poster== u,
        c.body == "whats good",
        c.unread == true,
        c.toString() == "[nick] whats good"
      )
      // read the comment
      c.read()
      assert(
        c.unread == false
      )
    }

    'Post {
      assert(
        p.poster== u,
        p.url == testUrl,
        p.title == Some("Interesting"),
        p.unread == true,
        p.comments.isEmpty == true
      )
      // read the post
      p.read()
      assert(
        p.unread == false
      )
      // check that comments is empty
      // add a comment, check comments
      p.add(c)
      p.add(c)
      assert(
        p.comments == List(c, c)
      )
      // remove a comment
      p.remove(c)
      assert(
        p.comments == List()
      )
      // check that title is optional
      val p2 = new Post(u, testUrl, None)
      assert(
        p2.url == testUrl,
        p2.title == None
      )
    }

    'Room {
      assert(
        r.creator == u,
        r.name == "Great links",
        // check that members are just u
        r.members == Set(u)
      )
      // add a post
      r.addPost(p)
      assert(
        r.posts == Set(p)
      )
      // remove a post
      r.removePost(p)
      assert(
        r.posts == Set()
      )
      val u2 = new User("steve")
      // add a member
      r.addMember(u2)
      assert(
        r.members == Set(u, u2)
      )
      // remove a member
      r.removeMember(u2)
      assert(
        r.members == Set(u)
      )
      // NOTE can't remove the creator of a room????
      // NOTE error if try to remove a person who's not in the room?
      // NOTE error if try to add a person who's already in the room?
      // NOTE must be moderator to remove from room..%coloredlevel.?
    }

    'App {
      assert(
        a.loggedInAs == None,
        a.currentRoom == None,
        a.joinedRooms == Set()
      )
      // log in as u
      a.logInAs(u)
      assert(
        a.loggedInAs == Some(u)
      )
      // join room r
      a.join(r)
      assert(
        a.joinedRooms == Set(r),
        a.currentRoom == Some(r)
      )
      // leave room r
      a.leave(r)
      assert(
        a.joinedRooms == Set(),
        // since our current room was the one we just left,
        // our current room should now be None
        a.currentRoom == None
      )
      // log out as u
      a.logOut()
      assert(
        a.loggedInAs == None
      )
      // log out when already logged out, should fail
      intercept[Exception] { a.logOut() }
      // join room when not logged in (should fail)
      intercept[Exception] { a.join(r) }
      // leave room when not logged in (should fail)
      intercept[Exception] { a.leave(r) }
    }
  }
}
