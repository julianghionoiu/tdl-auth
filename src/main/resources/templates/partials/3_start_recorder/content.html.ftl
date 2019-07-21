<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="VIDEO_RECORDING_OPTION" type="tdl.auth.linkgenerator.VideoRecordingOption" -->
<div class="row dark-background margins-mobile">
    <div class="col-md-10 container-centered">
        <div class="row text-left">
            <div class="col-lg-8">
                <div id="setup-screen-recording">

                    <#if VIDEO_RECORDING_OPTION.name() == "MANDATORY">
                        <h2>3. Start recording source code and video</h2>
                    <#-- ~~~~~~~~~~~~~  VIDEO_RECORDING_OPTION.MANDATORY  ~~~~~~~~~~~~~ -->

                        <p>The development technique is as important as the final solution. It is very informative to
                            see how the source code evolved over time and this is why we ask participants to
                            record both screen and source code.</p>

                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-warning-sign"></span>
                            We do not collect <strong>personal identifiable information</strong> (email, social
                            media), so please close all other tabs that contain these before you start recording.
                        </div>

                        <ul>
                            <li>
                                If you have multiple screens attached, please only use the laptop screen for coding.
                            </li>
                            <li>
                                Start the recorder in a new terminal tab/window:
                                <div class="start-rec copy-block">
                                    <div class="copy-block-line">
                                    <#-- READ THIS! placeholder value - actual value is set by the script -->
                                        <pre><code class="copy-code">./record_screen_and_upload.bat</code></pre>
                                        <p><a href="#" class="copy-btn copy-text">
                                            <#--noinspection HtmlUnknownTarget-->
                                            <img src="${RESOURCES_WEB_PATH}/img/record/clippy.svg" width="14"
                                                 height="16">
                                        </a></p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    <#elseif VIDEO_RECORDING_OPTION.name() == "DISABLED">
                    <#-- ~~~~~~~~~~~~~  VIDEO_RECORDING_OPTION.DISABLED  ~~~~~~~~~~~~~ -->
                        <h2>3. Start recording source code</h2>

                        <p>The development technique is as important as the final solution. It is very informative to
                            see how the source code evolved over time and this is why we record changes to
                            your source code files.</p>

                        <p>By default we do <b>NOT</b> record the screen.
                            If you wish to create a video recording of the session,
                            just remove the <code>--no-video</code> option from the recorder command line.</p>

                        <p>Start the recorder in a new terminal tab/window:</p>
                        <div class="start-rec copy-block">
                            <div class="copy-block-line">
                            <#-- READ THIS! placeholder value - actual value is set by the script -->
                                <pre><code class="copy-code">./record_screen_and_upload.bat</code></pre>
                                <p><a href="#" class="copy-btn copy-text">
                                    <#--noinspection HtmlUnknownTarget-->
                                    <img src="${RESOURCES_WEB_PATH}/img/record/clippy.svg" width="14"
                                         height="16">
                                </a></p>
                            </div>
                        </div>

                    <#else>
                    <#-- ~~~~~~~~~~~~~  The default is VIDEO_RECORDING_OPTION.OPTIONAL  ~~~~~~~~~~~~~ -->
                        <h2>3. Start recording source code and video</h2>

                        <p>The development technique is as important as the final solution. It is very informative to
                            see how the source code evolved over time and this is why we ask participants to
                            record both screen and source code.</p>

                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-warning-sign"></span>
                            We do not collect <strong>personal identifiable information</strong> (email, social
                            media), so please close all other tabs that contain these before you start recording.
                        </div>

                        <p>Although very useful, if recording video makes you uncomfortable, you can disable it by
                            adding the <code>--no-video</code> option to the recorder command line.</p>

                        <ul>
                            <li>
                                If you have multiple screens attached, please only use the laptop screen for coding.
                            </li>
                            <li>
                                Start the recorder in a new terminal tab/window:
                                <div class="start-rec copy-block">
                                    <div class="copy-block-line">
                                    <#-- READ THIS! placeholder value - actual value is set by the script -->
                                        <pre><code class="copy-code">./record_screen_and_upload.bat</code></pre>
                                        <p><a href="#" class="copy-btn copy-text">
                                            <#--noinspection HtmlUnknownTarget-->
                                            <img src="${RESOURCES_WEB_PATH}/img/record/clippy.svg" width="14"
                                                 height="16">
                                        </a></p>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </#if>
                </div>
            </div>
            <div class="col-lg-4">
                <#--noinspection HtmlUnknownTarget-->
                <img src="${RESOURCES_WEB_PATH}/img/record/record.png" class="img-fluid start-recording-img-margins">
            </div>
        </div>
    </div>
</div>