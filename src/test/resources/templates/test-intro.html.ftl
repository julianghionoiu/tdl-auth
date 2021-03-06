<!DOCTYPE html>
<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="HEADER_IMAGE_NAME" type="java.lang.String" -->
<#-- @ftlvariable name="MAIN_CHALLENGE_TITLE" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="JOURNEY_ID" type="java.lang.String" -->
<#-- @ftlvariable name="INSPIRED_BY" type="java.lang.String" -->
<#-- @ftlvariable name="CODING_SESSION_DURATION" type="java.lang.String" -->
<#-- @ftlvariable name="VIDEO_RECORDING_OPTION" type="tdl.auth.linkgenerator.VideoRecordingOption" -->
<#-- @ftlvariable name="ENABLE_APPLY_PRESSURE" type="java.lang.Boolean" -->
<#-- @ftlvariable name="ENABLE_REPORT_SHARING" type="java.lang.Boolean" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="CHALLENGE" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.String" -->
<html>
    <body>
        <p>Using custom header image name: ${RESOURCES_WEB_PATH}/${HEADER_IMAGE_NAME}</p>
        <div class="text-center">
            <h1>Welcome to the ${MAIN_CHALLENGE_TITLE} challenge</h1>
            <h2>Sponsored by ${SPONSOR}</h2>
            <h3>Inspired by ${INSPIRED_BY}</h3>
        </div>

        This link will expire on: ${EXPIRATION_DATE}
        You will get ${CODING_SESSION_DURATION} of uninterrupted coding challenge


        <#if VIDEO_RECORDING_OPTION.name() == "MANDATORY">
        <p>video recording is mandatory</p>
        </#if>

        <#if ENABLE_APPLY_PRESSURE>
        <p>applying pressure</p>
        </#if>

        <#if ENABLE_REPORT_SHARING>
        <p>report sharing enabled</p>
        </#if>

        <!--suppress HtmlUnknownTarget -->
        <form action="${API_VERIFY_ENDPOINT}" method="post">
            <label for="usr">Username:</label>
            <input type="text" id="usr" name="username" value="${USERNAME}" size="35"><br/>
            <label for="tdl_chx">Challenge:</label>
            <input type="text" id="tdl_chx" name="challenge" value="${CHALLENGE}" size="35"><br/>
            <label for="tkn">Token:</label>
            <input type="text" id="tkn" name="token" value="${TOKEN}" size="120"><br/>
            <input type="submit" value="Download credentials">
        </form>

        <p>
            <label for="sid">Copy SessionId:</label>
            <input type="text" id="sid" name="journey_id" value="${JOURNEY_ID}" size="50"><br/>
            Go to: <a href="http://run.befaster.io:8111">http://run.befaster.io:8111</a>
        </p>
    </body>
</html>