package com.sxk.thymeleaf;

import java.util.Map;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.StandardLinkBuilder;

public class NoWebLinkBuilder extends StandardLinkBuilder {

  public NoWebLinkBuilder() {
    super();
  }

  @Override
  protected String processLink(final IExpressionContext context, final String link) {
    //return "[" + link + "]";
    return link;
  }

  @Override
  protected String computeContextPath(final IExpressionContext context, final String base,
      final Map<String, Object> parameters) {
    return "/";
  }
}
