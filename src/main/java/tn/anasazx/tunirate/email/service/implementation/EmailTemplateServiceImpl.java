package tn.anasazx.tunirate.email.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import tn.anasazx.tunirate.email.service.EmailTemplateService;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final TemplateEngine templateEngine;

    @Override
    public String verificationEmail(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email/verification-email", context);
    }

}
