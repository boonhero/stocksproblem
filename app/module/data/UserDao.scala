package module.data

import com.google.inject.ImplementedBy
import model.User
import module.data.mock.MockUserDao

@ImplementedBy(classOf[MockUserDao])
trait UserDao {
  def update(user: User): Unit

  def find(name: String): Option[User]
}

class UserDaoImpl extends UserDao {
  override def find(name: String): Option[User] = ???

  override def update(user: User): Unit = ???
}
