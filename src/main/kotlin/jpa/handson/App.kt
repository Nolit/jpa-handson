/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package jpa.handson

import jpa.handson.entities.User
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class App {
    val greeting: String
        get() {
            return "Hello world2."
        }
}

fun main(args: Array<String>) {
    val user = User()
    user.name = "test"
    val emf = Persistence.createEntityManagerFactory("default_pu")
    val em = emf.createEntityManager()
    em.transaction.begin()
    em.persist(user)
    em.transaction.commit()
    em.close()
    println("終了！")
}
