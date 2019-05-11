package ru.populated.tetris.game.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.service.ContextService
import java.util.*

@RestController
@RequestMapping("/context")
class ContextController {

    @Autowired
    lateinit var contextService: ContextService

    @GetMapping
    @CrossOrigin(origins = arrayOf("http://localhost:3000"))
    fun getList(): List<Context> {
        return contextService.getListOfContext()
    }

    @GetMapping("/{contextId}/{userId}")
    @CrossOrigin(origins = arrayOf("http://localhost:3000"))
    fun addUserInContext(@PathVariable contextId: UUID, @PathVariable userId: UUID) {
        contextService.addUserINContext(contextId, userId)
    }

    @PostMapping
    @CrossOrigin(origins = arrayOf("http://localhost:3000"))
    fun createContext(): Context {
        return contextService.createNewContex()
    }

}