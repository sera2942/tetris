package ru.populated.tetris.game.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import ru.populated.tetris.game.model.User
import ru.populated.tetris.game.service.UserService

@Controller
@RequestMapping("/user")
class UserController {
    private val LOG = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var userService: UserService

    val map: ConcurrentMapCache = ConcurrentMapCache("geme")

    init {
        LOG.info("UserController")
    }

    @GetMapping("/{name}")
    fun registrateUser(@PathVariable name: String): User {
        LOG.info("Registering user with name $name")
        return userService.registration(name)
    }
}