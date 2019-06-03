package ru.populated.tetris.game.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.service.ContextService
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("/context")
class ContextController {

    @Autowired
    lateinit var contextService: ContextService

    @GetMapping
    fun getList(): List<Context> {
        return contextService.getListOfContext()
    }

    @GetMapping("/{contextId}/{userId}")
    fun addUserInContext(@PathVariable contextId: UUID, @PathVariable userId: UUID) {
        contextService.addUserINContext(contextId, userId)
    }

    @PostMapping
    fun createContext(): Context {
        return contextService.createNewContex()
    }

}