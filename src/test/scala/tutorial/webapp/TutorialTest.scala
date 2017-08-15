package tutorial.webapp

import utest._

object TutorialTest extends TestSuite {


  def tests = TestSuite {

    val testUrl = "https://watevs.com"

    val u = new User("nick")
    val c = new Comment(u, "whats good")
    val p = new Post(u, testUrl, Some("Interesting"))
    val r = new Room(u,  "Great links")

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
        c.toString() == "[nick] whats good"
      )
    }

    'Post {
      assert(
        p.poster== u,
        p.url == testUrl,
        p.title == Some("Interesting"),
        p.comments.isEmpty == true
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
      assert(p2.url == testUrl)
      assert(p2.title == None)
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
      // NOTE can't remove the creator of a room?
      // NOTE can't remove a person who's not in the room?
      // NOTE can't add a person who's already in the room?
    }

    'App {
      val a = new App()
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
    }
  }
}
