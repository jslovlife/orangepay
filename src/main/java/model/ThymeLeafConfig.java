package model;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import util.Constant;

public enum ThymeLeafConfig {
    INSTANCE;
    private TemplateEngine templateEngine;

    private ThymeLeafConfig(){
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(getTemplatePath());
        templateResolver.setTemplateMode("JAVA");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static String getTemplatePath(){
        return ThymeLeafConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "template/";
    }

    public static TemplateEngine getTemplateEngine(){
        return INSTANCE.templateEngine;
    }
}