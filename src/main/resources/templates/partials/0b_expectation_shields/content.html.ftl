<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="CODING_SESSION_DURATION" type="java.lang.String" -->
<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<div class="row">
    <div class="col-md-10 container-centered">
        <div class="row text-center">
            <div class="col-md-4">
                <#--noinspection HtmlUnknownTarget-->
                <img src="${RESOURCES_WEB_PATH}/img/ic_whatshot_black_24dp_2x.png">
                <h3>Average Completion Time</h3>
                <h4>${CODING_SESSION_DURATION}</h4>
                <p>
                    This does not mean you need to rush this. The quality of your code and your development technique are equaly important.
                </p>
            </div>
            <div class="col-md-4">
                <#--noinspection HtmlUnknownTarget-->
                <img src="${RESOURCES_WEB_PATH}/img/ic_settings_black_24dp_2x.png">
                <h3>You have</h3>
                <h4>9 hours remaining</h4>
                <p>
                    Complete the challenge by <br/><span class="exp-date font-weight-bold">${EXPIRATION_DATE}</span>
                </p>
            </div>
            <div class="col-md-4">
                <#--noinspection HtmlUnknownTarget-->
                <img src="${RESOURCES_WEB_PATH}/img/ic_timer_black_48px.svg">
                <h3>Pause/Resume</h3>
                <h4>anytime</h4>
                <p>
                    You are in control of your time. You can choose to solve the challenge in one go or multiple sessions.
                </p>
            </div>
        </div>
    </div>
</div>
