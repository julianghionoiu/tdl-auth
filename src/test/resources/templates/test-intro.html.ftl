<#-- @ftlvariable name="CHALLENGE_NAME" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="SESSION_ID" type="java.lang.String" -->
<!DOCTYPE html>
<html>
    <body>
        <div class="text-center">
            <h1>Welcome to the ${CHALLENGE_NAME} challenge</h1>
            <h2>Sponsored by ${SPONSOR}</h2>
        </div>

        <!--suppress HtmlUnknownTarget -->
        <form action="${API_VERIFY_ENDPOINT}" method="post">
            <label for="usr">Username:</label>
            <input type="text" id="usr" name="username" value="${USERNAME}" size="35"><br/>
            <label for="tkn">Token:</label>
            <input type="text" id="tkn" name="token" value="${TOKEN}" size="120"><br/>
            <input type="submit" value="Download credentials">
        </form>

        <p>
            <label for="sid">Copy SessionId:</label>
            <input type="text" id="sid" name="session_id" value="${SESSION_ID}" size="50"><br/>
            Go to: <a href="http://run.befaster.io:8111">http://run.befaster.io:8111</a>
        </p>
    </body>
</html>