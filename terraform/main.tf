terraform {
  required_providers {
    heroku = {
      source  = "heroku/heroku"
      version = "~> 5.0"
    }
  }

  backend "pg" {}
}

resource "heroku_app" "app_prd" {
  name   = "${local.app_name}-prd"
  region = "eu"
}

resource "heroku_formation" "prd_formation" {
  app_id   = heroku_app.app_prd.id
  quantity = 1
  size     = "eco"
  type     = "web"
}

resource "heroku_build" "dashclever_backend_build" {
  app_id     = heroku_app.app_prd.id
  buildpacks = ["https://github.com/heroku/heroku-buildpack-gradle.git"]

  source {
    path = "../tar/app.tar.gz"
  }
}

locals {
  app_name = var.app_name
}
