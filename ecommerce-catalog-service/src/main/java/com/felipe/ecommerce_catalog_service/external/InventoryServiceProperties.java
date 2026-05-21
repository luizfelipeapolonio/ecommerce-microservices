package com.felipe.ecommerce_catalog_service.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services.inventory-service")
public class InventoryServiceProperties {
  private String scheme;
  private String host;
  private int port;
  private String productsPath;
  private String categoriesPath;

  public String getScheme() { return this.scheme; }
  public void setScheme(String scheme) { this.scheme = scheme; }

  public String getHost() { return this.host; }
  public void setHost(String host) { this.host = host; }

  public int getPort() { return this.port; }
  public void setPort(int port) { this.port = port; }

  public String getProductsPath() { return this.productsPath; }
  public void setProductsPath(String productsPath) { this.productsPath = productsPath; }

  public String getCategoriesPath() { return this.categoriesPath; }
  public void setCategoriesPath(String categoriesPath) { this.categoriesPath = categoriesPath; }
}
