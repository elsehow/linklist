package tutorial.webapp

import utest._

object TutorialTest extends TestSuite {


  def tests = TestSuite {

    val u = new User("nick")

    'User {
      assert(u.name == "nick")
      assert(u.online == false)
      assert(u.toString() == "nick")
    }

    'Comment {
      val c = new Comment(u, "whats good")
      assert(c.poster== u)
      assert(c.body == "whats good")
      assert(c.toString() == "nick - whats good")
    }

    'Post {
      val p = new Post(u, "https://fart.com", Some("Interesting"))
      assert(p.poster== u)
      assert(p.url == "https://fart.com")
      assert(p.title == Some("Interesting"))
      // TODO check that comments is empty
      // TODO add a comment, check comments
      // TODO remove a comment
      // check that title is optional
      val p2 = new Post(u, "https://fart.com", None)
      assert(p2.url == "https://fart.com")
      assert(p2.title == None)
    }

    'Room {
      val r = new Room(u,  "Great links")
      assert(r.creator == u)
      assert(r.name == "Great links")
      // TODO check that members are just u
      // TODO add a post
      // TODO add a member
      // TODO remove a post
      // TODO remove a member
    }

    'App {
      val a = new App()
      assert(a.loggedInAs.getOrElse(None) == None)
      assert(a.room.getOrElse(None) == None)
      // TODO log in as u
      // TODO join room r
      // TODO leave room r
      // TODO log out as u
      // TODO log out as u (should fail)
      // TODO join room when not logged in (should fail)
    }
  }
}
