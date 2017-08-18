package tutorial.classes

import utest._
import java.net.URL

object ClassesTest extends TestSuite {


  def tests = TestSuite {

    val u = new User("nick")

    'User {
      assert(
        u.name == "nick",
        u.online == false,
        )
    }

    'Comment {
      val c = new Comment(u, "whats good")
      assert(
        c.poster== u,
        c.body == "whats good",
        c.unread == true,
      )
    }

    'Post {
      // TODO this URL should fail IMO - we need better parsing libraries!
      val testUrl = new URL("http://watevs")
      val l = new Post(u, testUrl, Some("Interesting"))
      assert(
        l.poster== u,
        l.url == testUrl,
        l.title == Some("Interesting"),
        l.unread == true,
        l.comments.isEmpty
      )
      // check that title is optional
      val l2 = new Post(u, testUrl)
      assert(
        l2.url == testUrl,
        l2.title == None
      )
      // bad URL won't take
      intercept[Exception] {
        new URL("notaurl")
      }
    }

    'Room {
      val r = new Room("Great links", Set(u))
      assert(
        r.name == "Great links",
        // check that members are just u
        r.members == Set(u)
      )
    }

    'AppState {
      val a = new AppState()
      assert(
        a.loggedInAs == None,
        a.currentRoom == None,
        a.joinedRooms.isEmpty,
        a.notifications.isEmpty
      )
      // intercept[Exception] { a.leave(r) }
    }
  }
}
