<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="CHALLENGE" type="java.lang.String" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<div class="row">
    <div class="col-md-10 mb-5 container-centered">
        <h3>2. Download credentials</h3>
        <p>To securely connect to the server you need to download a pair of credentials <b>before each coding
            session</b>.
            Download and save the credentials to the <code>config</code> directory:</p>
        <form action="${API_VERIFY_ENDPOINT}" method="post">
            <p class="form-input-readonly">Your user ID:
                <input type="text" name="username" value="${USERNAME}" readonly>
            </p>
            <input type="hidden" name="challenge" value="${CHALLENGE}">
            <input type="hidden" name="token" value="${TOKEN}">
            <input type="submit" class="btn btn-primary" id="download_credentials" value="Download credentials">
        </form>
        <p class="alert-danger my-alert-danger">The link expired on: <span
                class="exp-date font-weight-bold">${EXPIRATION_DATE}</span></p>

        <div class="accelerate_runner">
            <p class="text-left">If the steps are successful you should see a folder called "accelerate_runner"
                which contains all the libraries required to connect to the server and run the coding challenge.</p>
        <#--noinspection HtmlUnknownTarget-->
            <img src="${RESOURCES_WEB_PATH}/img/runner_setup/download_runner_project.png" class="img-fluid download-credentials-img-margins">
        </div>
    </div>
</div>
