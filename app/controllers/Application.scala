package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller {

    def index = Action {
        Ok(views.html.index("Hello World, Play Framework"))
    }

    def user() = Action { 
        Ok(views.html.user("User Login"))
    }
  
}
