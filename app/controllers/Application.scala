package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import scala.concurrent.Future

case class UserData(name: String, age: Int)

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

  def user = Action { 
    Ok(views.html.user(userForm))
  }

  def userPost = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.user(formWithErrors))
      },
      userData => {
        val id = 1
        //TODO need to implement real create operation of model according to
        //the provided userData
        Redirect(routes.Application.home(id))
      }
    )
  }

  def home(id: Int) = Action {
    val name = "Steven"
    val age = 30
    //TODO need to implement real operation of retrieving name and age of the model from the database
    Ok(views.html.home(name, age))  
  }

  val userForm = Form(
    mapping(
      "name"->nonEmptyText, 
      "age"->number(min=0, max=150)
    )(UserData.apply)(UserData.unapply)
  )
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



