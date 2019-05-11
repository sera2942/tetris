package ru.populated.tetris.game.service

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import ru.populated.tetris.game.model.User
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class UserService {
    val store: ConcurrentHashMap<UUID, User> = ConcurrentHashMap()

    fun registration(name: String): User {

        var user = User()
        if (StringUtils.isEmpty(name)) {
            user.name = UUID.randomUUID().toString()
        } else {
            user.name = name
        }

        store.put(user.id, user)
        return user
    }

    fun findUserById(id: UUID): User? {
        return store.get(id)
    }
}