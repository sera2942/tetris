package ru.populated.tetris.game.service

import org.springframework.stereotype.Service
import ru.populated.tetris.game.model.Event
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class GameService {
    private val queue: Queue<Event> = ConcurrentLinkedQueue()

    fun addEvent(event: Event) {
        queue.add(event)
    }

    fun nextEvent() : Event{
        return queue.poll()
    }     
}