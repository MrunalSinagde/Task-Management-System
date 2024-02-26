package Utils

import Service.TaskService
import jakarta.inject.Inject
import org.apache.pekko.actor.ActorSystem

import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class ActiveTaskScheduler @Inject()(taskService: TaskService,actorSystem: ActorSystem)
                                   (implicit executionContext: ExecutionContext){

  val taskRunnable = new Runnable {
    override def run(): Unit = {
      taskService.getActiveTasks().map { tasks =>
        println("Active tasks: ")
        tasks.foreach(task => println(s"Name: ${task.name}"))
      }
    }
  }

  actorSystem.scheduler.scheduleAtFixedRate(
    initialDelay = 10.seconds,
    interval = 15.seconds
  )(taskRunnable)

}
