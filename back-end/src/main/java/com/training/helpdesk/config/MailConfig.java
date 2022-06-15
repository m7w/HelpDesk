package com.training.helpdesk.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

/*
 *    @Value("${spring.mail.host}")
 *    private String host;
 *
 *    @Value("${spring.mail.port}")
 *    private String port;
 *
 *    @Value("${spring.mail.protocol}")
 *    private String transportProtocol;
 *
 *    @Value("${spring.mail.username}")
 *    private String username;
 *
 *    @Value("${spring.mail.password}")
 *    private String password;
 *
 *    @Value("${spring.mail.properties.smtp.auth}")
 *    private String smtpAuth;
 *
 *    @Value("${spring.mail.properties.smtp.starttls.enable}")
 *    private String starttls;
 *
 *    @Value("${spring.mail.properties.debug}")
 *    private String debug;
 */

/*
 *    @Bean 
 *    public JavaMailSender getMailSender() {
 *
 *        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
 *
 *        mailSender.setHost(host);
 *        mailSender.setPort(Integer.parseInt(port));
 *        mailSender.setProtocol(transportProtocol);
 *        mailSender.setUsername(username);
 *        mailSender.setPassword(password);
 *
 *        Properties properties = mailSender.getJavaMailProperties();
 *        properties.put("mail.smtp.auth", smtpAuth);
 *        properties.put("mail.smtp.starttls.enable", starttls);
 *        properties.put("mail.debug", debug);
 *
 *        return mailSender;
 *    }
 *
 *    @Bean
 *    public TemplateEngine emailTemplateEngine() {
 *        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
 *        templateEngine.setTemplateResolver(htmlTemplateResolver());
 *        return templateEngine;
 *    }
 * 
 *    private ITemplateResolver htmlTemplateResolver() {
 *        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
 *        templateResolver.setResolvablePatterns(Collections.singleton("mail/*"));
 *        templateResolver.setPrefix("/templates/");
 *        templateResolver.setSuffix(".html");
 *        templateResolver.setTemplateMode(TemplateMode.HTML);
 *        templateResolver.setCacheable(false);
 *        return templateResolver;
 *    }
 */

}

