package ru.populated.tetris.game.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.populated.tetris.game.model.Context
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Service
class ContextService {
    private val LOG = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var userService: UserService
    val store: ConcurrentHashMap<UUID, Context> = ConcurrentHashMap()


    init {
        LOG.info("ContextService")
    }

    fun start(contextId: UUID) {

    }

    fun createNewContex(): Context {
        val context = Context()
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
        val user = userService.findUserById(userId)
        user?.let {
            var context: Context = this.getContextById(contextId)!!
                context.users.add(it)
                context.gameField.expendGameField(4, 0)
        }
    }
}