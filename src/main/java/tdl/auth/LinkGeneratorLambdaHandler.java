package tdl.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import tdl.auth.linkgenerator.Mailer;
import tdl.auth.linkgenerator.Page;

public class LinkGeneratorLambdaHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String email, Context cntxt) {
        Page page = new Page(email);
        page.generateAndUpload();
        Mailer mailer = new Mailer(email, page.getPublicUrl());
        mailer.send();
        return "OK";
    }

}
