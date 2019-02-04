<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="ALLOW_NO_VIDEO_OPTION" type="java.lang.Boolean" -->
<div class="row margins-mobile">
    <div class="col-md-10 container-centered">
        <div class="row text-left">
            <div class="col-lg-6">
                <div id="setup-screen-recording">
                    <h2>3. Start recording</h2>
                    <p>The development technique is as important as the final solution. It is very informative to see
                        how the source code evolved over time and this is why we ask participants to record screen and
                        source code.</p>
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-warning-sign"></span>
                        We do not collect <strong>personal identifiable information</strong> (email, social
                        media), so please close all other tabs that contain these before you start recording.
                    </div>
                        <#if ALLOW_NO_VIDEO_OPTION>
                        <p>Although very useful, if recording video makes you uncomfortable, you can disable it by
                            adding the <code>--no-video</code> option to the recorder command line.</p>
                        </#if>
                    <p>
                        <ul>
                            <li>
                                If you have multiple screens attached, please only use the laptop screen for coding.
                            </li>
                            <li>
                                <p>Start the recorder in a new terminal tab/window.</p>
                                <div class="start-rec">
                                    <pre><code>./record_screen_and_upload.bat</code></pre>
                                </div>
                            </li>
                        </ul>
                    </p>
                </div>
            </div>
            <div class="col-lg-6">
                <#--noinspection HtmlUnknownTarget-->
                <img src="${RESOURCES_WEB_PATH}/img/hello-challenge.gif" class="img-fluid img-margins2">
            </div>
        </div>
    </div>
</div>