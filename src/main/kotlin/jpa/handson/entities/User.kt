package jpa.handson.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(){
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String? = null
}