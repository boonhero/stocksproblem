package module.data.mock

import com.google.inject.Singleton
import model.{Stock, User}
import module.data.UserDao

/**
 * Created by asales on 1/9/2015.
 */
@Singleton
class MockUserDao extends UserDao {
  var user: User = User("testId", List[Stock]())

  override def find(name: String): Option[User] = {
    Some(user)
  }

  override def update(user: User): Unit = {
    this.user = user;
  }
}
