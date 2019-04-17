package ru.populated.tetris.game.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.populated.tetris.game.model.Context
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Service
class ContextService {
    private val LOG = LoggerFactory.getLogger(this.javaClass.name)

    lateinit var userService: UserService;
    val store: ConcurrentHashMap<UUID, Context> = ConcurrentHashMap()


    init {
        LOG.info("ContextService")
    }

    fun start(contextId: UUID) {

    }

    fun createNewContex(): Context {
        var context = Context()
        store.put(context.id, context)
        return context
    }

    fun getContextById(id: UUID): Context? {
        return store.get(id)
    }

    fun getListOfContext(): List<Context> {
        return store.toList().stream()
                .map { it.second }
                .collect(Collectors.toList())
    }

    fun addUserINContext(contextId: UUID, userId: UUID) {
        var user = userService.findUserById(userId)
        user?.let { getContextById(contextId)?.users?.add(it) }
    }
}