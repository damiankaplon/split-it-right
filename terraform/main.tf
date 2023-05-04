terraform {
  required_providers {
    heroku = {
      source  = "heroku/heroku"
      version = "~> 5.0"
    }
  }

  backend "pg" {}
}

resource "heroku_app" "split_it_right-prd" {
  name   = "${local.app_name}-prd"
  region = "eu"
}

resource "heroku_formation" "split_it_right-prd_formation" {
  app_id   = heroku_app.split_it_right-prd.id
  quantity = 1
  size     = "eco"
  type     = "web"
}

resource "heroku_build" "split_it_right_build" {
  app_id     = heroku_app.split_it_right-prd.id
  buildpacks = ["https://github.com/heroku/heroku-buildpack-gradle.git"]

  source {
    path = "../tar/app.tar.gz"
  }
}

resource "heroku_config" "split_it_right_config" {
  vars = {
    GRADLE_TASK = "clean vaadinClean vaadinBuildFrontend build -Pvaadin.productionMode=true"
  }

  sensitive_vars = {
    CURRENCY_API_URL = var.currency_api_url
    CURRENCY_API_KEY = var.currency_api_key

  }
}

resource "heroku_app_config_association" "split_it_right_config_assoc" {
  app_id = heroku_app.split_it_right-prd.id

  vars           = heroku_config.split_it_right_config.vars
  sensitive_vars = heroku_config.split_it_right_config.sensitive_vars
}

locals {
  app_name = var.app_name
}
