package controllers

import play.api._
import play.api.mvc._

import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def logging = LoggingAction { request => 
    Ok("Got: " + request) 
  }

  def anotherLogging = Logging {
    Action { request =>
      Ok("Got: " + request)
    }
  }
}

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Incoming request")
    block(request)
  }
}

case class Logging[A](action: Action[A]) extends Action[A] {

  def apply(request: Request[A]): Future[Result] = {
    Logger.info("Calling action")
    action(request)
  }

  lazy val parser = action.parser
}
