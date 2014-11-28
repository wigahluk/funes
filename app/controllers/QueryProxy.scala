package controllers

import play.api._
import play.api.mvc._

object QueryProxy extends Controller {

  def query (path: String) = Action {
    Ok("Hello world " + pathç)
  }

}