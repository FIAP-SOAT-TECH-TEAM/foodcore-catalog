locals {
  api_ingress_path_without_slash = replace(var.api_ingress_path, "/", "")
  repository_url = "oci://${data.azurerm_container_registry.acr.login_server}/helm"
}