<!DOCTYPE html>
<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="HEADER_IMAGE_NAME" type="java.lang.String" -->
<#-- @ftlvariable name="MAIN_CHALLENGE_TITLE" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="CODING_SESSION_DURATION" type="java.lang.String" -->
<#-- @ftlvariable name="ALLOW_NO_VIDEO_OPTION" type="java.lang.Boolean" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="CHALLENGE" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<#setting number_format="computer">
<html lang="en">
<head>
    <title>${SPONSOR} - Coding Challenge</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="apple-touch-icon" sizes="57x57" href="${RESOURCES_WEB_PATH}/img/logo_favicon_apple.png"/>
    <link rel="icon" type="image/png" sizes="32x32" href="${RESOURCES_WEB_PATH}/img/logo_favicon.png"/>


    <style>
        div {
            height: 100%;
        }

        img {
            margin: 10px;
            vertical-align: middle;
            max-width: 300px;
        }

        .row {
            padding: 45px 0;
        }

        .dark-background {
            background-color: #f3f3f3;
        }

        .dark-background pre {
            background-color: #ffffff;
        }

        .footer p {
            padding: 70px 0;
        }

        .jumbotron {
            background-image: url("${RESOURCES_WEB_PATH}/img/header/${HEADER_IMAGE_NAME}");
            background-position: top center;
            background-repeat: no-repeat;
            background-size: cover;
            color: #fff;
            margin-bottom: 0;
            padding-bottom: 75px;
            padding-top: 80px;
        }

        .jumbotron h1 {
            text-shadow: 2px 2px 0 #333;
            font-size: 63px;
            margin-top: 20px;
            margin-bottom: 10px;
        }

        h2 {
            font-size: 30px;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        h3 {
            font-size: 24px;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        h4 {
            font-size: 18px;
            margin-top: 10px;
            margin-bottom: 10px;
        }
        p{
            font-size: 14px;
            margin: 0 0 10px;
        }

        #challenge-flow .glyphicon {
            color: #337ab7;
        }

        .horizontal-arrow {
            position: absolute;
            left: 100%;
            top: 40%;
        }

        #challenge-flow .glyphicon {
            font-size: 150%;
        }

        .bottom-border {
            border-bottom-width: 2px;
            border-bottom-color: #b1afaf;
            border-bottom-style: solid;
        }
        .container-centered{
            margin: 0 auto;
        }
        ul li{
            font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
            font-size: 14px;
            line-height: 1.42857143;
            color: #333;
        }
        .alert-info {
            color: #31708f;
            background-color: #d9edf7;
            border-color: #bce8f1;
        }
        .alert-danger {
            color: #a94442;
            background-color: #f2dede;
            border-color: #ebccd1;
        }
        a {
            color: #337ab7;
            text-decoration: none;
        }
        pre {
            display: block;
            padding: 9.5px;
            margin: 0 0 10px;
            font-size: 13px;
            line-height: 1.42857143;
            color: #333;
            word-break: break-all;
            word-wrap: break-word;
            background-color: #f5f5f5;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        code {
            padding: 2px 4px;
            font-size: 90%;
            color: #c7254e;
            background-color: #f9f2f4;
            border-radius: 4px;
        }
        pre code {
            padding: 0;
            font-size: inherit;
            color: inherit;
            white-space: pre-wrap;
            background-color: transparent;
            border-radius: 0;
        }
        .os-list-tab, .lang-list-tab{
            border-bottom: none
        }
        .os-list-tab .nav-link, .lang-list-tab .nav-link{
            border-radius: 0.25rem;
        }
        .os-list-tab .nav-link.active, .lang-list-tab .nav-link.active{
            border-color: #dee2e6;
            font-weight: 700;
            background-color: orange;
            color: white;
        }
        .btn-primary {
            color: #fff;
            background-color: #337ab7;
            border-color: #2e6da4;
        }
        .btn-primary-darkBlue{
            background-color: #4C5675;
            border-color: #4C5675;
        }
        .btn-primary-darkBlue:hover{
            background-color: #6b769f;
            border-color: #4C5675;
        }
        .btn-primary.disabled, .btn-primary:disabled {
            color: #fff;
            background-color: #b1afaf;
            border-color: #b1afaf;
        }
        .copy-btn{
            text-decoration: none;
            border: 1px solid gray;
            padding: 7px 7px 9px 7px;
            margin: 0 0 0 10px;
            border-radius: 3px;
            display: inline-block;
        }
        .container-half{
            width: 70%;
        }
        .card-body #languagesTabContent{
            height: auto;
        }
        .card-body {
            padding: 0 1.25rem;
        }
        #languagesTab, #osTab, #languagesTabContent, #unzipTabContent{
            padding: 18px 0;
        }
        .step-title{
            display: flex;
            display: -webkit-flex;
            align-items: center;
            -webkit-align-items: center;
        }
        p.step-text{
            color: #4C5675;
            margin: 0 0 0 10px;
        }
        p.step{
            border: 1px solid #4C5675;
            border-radius: 50%;
            padding: 5px 6px 5px 8px;
            text-decoration: none;
            margin: 0;
            color: #4C5675;
        }
        .step-title .btn-link {
            font-weight: 400;
            color: #4C5675;
            text-align: left;
        }
        p.step:hover{
            text-decoration: none;
        }
        .step-title .btn-link:hover {
            text-decoration: none;
        }
        .form-input-readonly input{
            color: #b1afaf;
            background: white;
            border: 1px solid #b1afaf;
            border-radius: 3px;
            padding: 5px;
            width: 250px;
        }
        .form-input-readonly input:focus{
            box-shadow: none;
            border: 1px solid #b1afaf;
        }
        .my-alert-danger{
            display: none;
            padding: 2px 5px;
            margin-top: 10px;
        }
        .start-rec p{
            text-transform:capitalize;
        }
        .copy-block-line{
            display: flex;
            display: -webkit-flex;
            width: 100%;
            justify-content: space-between;
            -webkit-justify-content: space-between;
            height: auto;
        }
        .copy-block-line pre{
            width: 100%;
        }
        @media only screen and (max-width: 767px) {
            img {
                margin: 0;
            }
            .horizontal-arrow{
                display: none;
            }
        }
        @media only screen and (max-width: 1360px) {
            .container-half{
                width: 100%;
            }
        }
    </style>
</head>
<body>

<div class="jumbotron text-center">
    <h1>The ${SPONSOR} challenge</h1>
    <h2>${MAIN_CHALLENGE_TITLE}</h2>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-10 container-centered">
            <h2>How does this work?</h2>
            <div class="row text-center">
                <div class="col-md-4">
                    <img src="${RESOURCES_WEB_PATH}/img/ic_settings_black_24dp_2x.png">
                    <h3>Set up</h3>
                    <h4>in minutes</h4>
                    <p>
                        You will clone a github repo, install some libraries and will then be ready
                        to write code in your own IDE using your choice of frameworks and tools.
                    </p>
                </div>
                <div class="col-md-4">
                    <img src="${RESOURCES_WEB_PATH}/img/ic_whatshot_black_24dp_2x.png">
                    <h3>Challenge</h3>
                    <h4>${CODING_SESSION_DURATION}</h4>
                    <p>
                        A realistic coding challenge designed to measure code quality. Code through multiple incremental requirements.
                    </p>
                </div>
                <div class="col-md-4">
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

    <div class="row dark-background">
        <div class="col-md-10 container-centered">
            <div class="row text-left">
                <div class="col-md-6">
                    <h2>It is not just a coding exercise</h2>
                    <p>You go through a realistic coding challenge designed to put your core software development skills to the
                        test.</p>
                    <p>We promise that you will learn a lot by just going through this challenge.</p>

                    <p>Focus on your development technique and see how well it plays out. You will be able to compare metrics
                        afterwards.</p>
                    <p>Each screen gets recorded so after the challenge you get the opportunity to see (anonymously) how other
                        people have approached this problem. You might be able to learn something from them.</p>
                </div>
                <div class="col-md-6">
                    <img src="${RESOURCES_WEB_PATH}/img/leaderboard-transparent-bg.png" class="img-fluid"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-10 container-centered">
            <h2>Before you start</h2>
            <p class="mb-0">You will need:</p>
            <ul>
                <li>15 minutes for Preparation + Warmup</li>
                <li>${CODING_SESSION_DURATION} challenge time, can be done in multiple coding sessions</li>
                <li>your laptop + your IDE and a connection to the internet</li>
            </ul>
            <div class="alert alert-info">
                We encourage you put aside some time in a quiet environment where you won't be interrupted so you can
                focus on
                the challenge.
            </div>
            <p>People who have completed the challenge will have access to the other results and recordings.</p>
            <p>If you are not ready you can come back to this page whenever you have
                enough time. The link will expire on: <span class="exp-date font-weight-bold">${EXPIRATION_DATE}</span></p>
            <p>The challenge can pe paused and resumed, you are in control of your time. You can do it one go or multiple sessions.</p>
            <p>Ready? Let's get started.</p>
        </div>
    </div>


    <div class="row dark-background" id="clone-repo-section">
        <div class="col-md-10 container-centered">
            <div>
                <h3>1. Download runner project</h3>
                <p>Clone the git repository for the language of your choice.</p>

                <div class="accordion mt-3" id="accordionExample">
                    <div class="card">
                        <div class="card-header" id="headingOne">
                            <h2 class="mb-0 mt-0 step-title">
                                <p class="step">1.</p>
                                <p class="step-text">
                                    Select your operating system
                                </p>
                            </h2>
                        </div>

                        <div id="" class="collapse show" aria-labelledby="headingOne" data-parent="#accordionExample">
                            <div class="card-body">
                                <ul class="nav nav-tabs os-list-tab" id="osTab" role="tablist">
                                    <li class="nav-item os-list">
                                        <a class="nav-link" id="windows-tab" data-toggle="tab" href="#windows" role="tab" aria-selected="false">Windows</a>
                                    </li>
                                    <li class="nav-item os-list">
                                        <a class="nav-link" id="macos-tab" data-toggle="tab" href="#macos" role="tab" aria-selected="false">Mac</a>
                                    </li>
                                    <li class="nav-item os-list">
                                        <a class="nav-link" id="linux-tab" data-toggle="tab" href="#linux" role="tab" aria-selected="true">Linux</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header" id="headingTwo">
                            <h2 class="mb-0 mt-0 step-title">
                                <p class="step">2.</p>
                                <p class="step-text">
                                    Select your preferred language
                                </p>
                            </h2>
                        </div>
                        <div id="" class="collapse show" aria-labelledby="headingTwo" data-parent="#accordionExample">
                            <div class="card-body">
                                <ul class="nav nav-tabs lang-list-tab" id="languagesTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link" id="csharp-tab" data-toggle="tab" href="#csharp" role="tab" aria-selected="false">C#</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="fsharp-tab" data-toggle="tab" href="#fsharp" role="tab" aria-selected="false">F#</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link active" id="java-tab" data-toggle="tab" href="#java" role="tab" aria-selected="true">Java</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="nodejs-tab" data-toggle="tab" href="#nodejs" role="tab" aria-selected="false">Node.js</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="python-tab" data-toggle="tab" href="#python" role="tab" aria-selected="false">Python</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="ruby-tab" data-toggle="tab" href="#ruby" role="tab" aria-selected="false">Ruby</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="scala-tab" data-toggle="tab" href="#scala" role="tab" aria-selected="false">Scala</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="vbnet-tab" data-toggle="tab" href="#vbnet" role="tab" aria-selected="false">VB.Net</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header" id="headingThree">
                            <h2 class="mb-0 mt-0 step-title">
                                <p class="step">3.</p>
                                <p class="step-text">
                                    Download the runner project.
                                </p>
                            </h2>
                        </div>
                        <div id="" class="collapse show" aria-labelledby="headingThree" data-parent="#accordionExample">
                            <div class="card-body">
                                <div class="tab-content" id="languagesTabContent">
                                    <div class="copy-block" id="download" role="tabpanel" aria-labelledby="down-tab">
                                        <div class="copy-block-line">
                                            <pre><code class="copy-code">wget https://get.accelerate.io/v0/runner-for-java-windows.zip</code></pre>
                                            <p><a href="#" class="copy-btn copy-text"><svg class="octicon octicon-clippy" viewBox="0 0 14 16" version="1.1" width="14" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M2 13h4v1H2v-1zm5-6H2v1h5V7zm2 3V8l-3 3 3 3v-2h5v-2H9zM4.5 9H2v1h2.5V9zM2 12h2.5v-1H2v1zm9 1h1v2c-.02.28-.11.52-.3.7-.19.18-.42.28-.7.3H1c-.55 0-1-.45-1-1V4c0-.55.45-1 1-1h3c0-1.11.89-2 2-2 1.11 0 2 .89 2 2h3c.55 0 1 .45 1 1v5h-1V6H1v9h10v-2zM2 5h8c0-.55-.45-1-1-1H8c-.55 0-1-.45-1-1s-.45-1-1-1-1 .45-1 1-.45 1-1 1H3c-.55 0-1 .45-1 1z"></path></svg></a></p>
                                        </div>
                                        <p>OR</p>
                                        <p><a href="https://get.accelerate.io/v0/runner-for-java-windows.zip" class="btn btn-primary btn-primary-darkBlue download-link" target="_blank">Download runner project</a></p>
                                       </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header" id="headingThree">
                            <h2 class="mb-0 mt-0 step-title">
                                <p class="step">4.</p>
                                <p class="step-text">
                                    Extract archive - unzip
                                </p>
                            </h2>
                        </div>
                        <div id="" class="collapse show" aria-labelledby="headingThree" data-parent="#accordionExample">
                            <div class="card-body">
                                <div class="tab-content" id="unzipTabContent">
                                    <div id="unzip" class="copy-block" role="tabpanel" aria-labelledby="unzip-tab">
                                        <div class="copy-block-line">
                                            <pre><code class="copy-code">unzip runner-for-java-windows.zip</code></pre>
                                            <p><a href="#" class="copy-btn copy-text"><svg class="octicon octicon-clippy" viewBox="0 0 14 16" version="1.1" width="14" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M2 13h4v1H2v-1zm5-6H2v1h5V7zm2 3V8l-3 3 3 3v-2h5v-2H9zM4.5 9H2v1h2.5V9zM2 12h2.5v-1H2v1zm9 1h1v2c-.02.28-.11.52-.3.7-.19.18-.42.28-.7.3H1c-.55 0-1-.45-1-1V4c0-.55.45-1 1-1h3c0-1.11.89-2 2-2 1.11 0 2 .89 2 2h3c.55 0 1 .45 1 1v5h-1V6H1v9h10v-2zM2 5h8c0-.55-.45-1-1-1H8c-.55 0-1-.45-1-1s-.45-1-1-1-1 .45-1 1-.45 1-1 1H3c-.55 0-1 .45-1 1z"></path></svg></a></p>
                                        </div>
                                        <p>The <code>accelerate_runner</code> will be created, which represents the root of your project.</p>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row dark-background">
        <div class="col-md-10 mb-5 container-centered">
            <h3>2. Download credentials</h3>
            <p>To securely connect to the server you need to download a pair of credentials <b>before each coding session</b>.
                Download and save the credentials to the <code>config</code> directory:</p>
            <form action="${API_VERIFY_ENDPOINT}" method="post">
                <p class="form-input-readonly"><input type="text" name="username" value="${USERNAME}" readonly></p>
                <input type="hidden" name="challenge" value="${CHALLENGE}">
                <input type="hidden" name="token" value="${TOKEN}">
                <input type="submit" class="btn btn-primary" id="download_credentials" value="Download credentials">
            </form>
            <p class="alert-danger my-alert-danger">The link is expired on: <span class="exp-date font-weight-bold">${EXPIRATION_DATE}</span></p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-10 container-centered">
            <div class="row text-left">
                <div class="col-md-6">
                    <div id="setup-screen-recording">
                        <h2>3. Start recording</h2>
                        <p>The development technique is as important as the final solution. It is very informative to see
                            how the source code evolved over time and this is why we ask participants to record screen and source code.</p>
                        <p>This also means that you get access to all the data. After you perform the challenge you get to see (anonymously) how the
                            other participants tackled the same challenge. This includes video and source code.</p>
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-warning-sign"></span>
                            We do not collect <strong>personal identifiable information</strong> (email, social
                            media), so please close all other tabs that contain these before you start recording.
                        </div>
                        <#if ALLOW_NO_VIDEO_OPTION>
                        <p>Although very useful, if recording video makes you uncomfortable, you can disable it by adding the <code>--no-video</code> option to the recorder command line.</p>
                        </#if>
                        <p>
                            <ul>
                                <li>
                                    Requires Java 8. If you do not have Java 8 <a
                                            href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">download</a>
                                        and install it.
                                </li>
                                <li>
                                    If you have multiple screens attached, please only use the laptop screen for coding.
                                </li>
                                <li>
                                    <p>Start the recorder in a new terminal tab/window.</p>
                                    <div class="start-rec">
                                        <p>Windows:</p>
                                        <pre><code>./record_screen_and_upload.bat</code></pre>
                                    </div>
                                </li>
                            </ul>
                        </p>
                    </div>
                </div>
                <div class="col-md-6">
                    <img src="${RESOURCES_WEB_PATH}/img/hello-challenge.gif" class="img-fluid">
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div id="challenge-flow" class="col-md-10 container-centered">
            <h2>4. Start the warmup</h2>
            <p>In your IDE of choice, open up the <code>README.md</code> file of the repo that you cloned.</p>
            <p>To get started, follow the instructions provided in the README.md and source code comments.</p>
            <br/>
            <p>While working on the challenge you will be interacting with the server by running the code and you will provide your solutions within the same project.</p>
            <p>The challenge has the following flow. Do not worry about the details, the Warmup will guide you through
                the process.</p>
            <div class="row">
                <div class="col-sm-6 col-md-5">
                    <img src="${RESOURCES_WEB_PATH}/img/start-challenge.png" alt="Start challenge." class="img-fluid">
                    <p>1. Start the challenge</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-right hidden-sm hidden-xs"><i class="fa fa-arrow-right" aria-hidden="true"></i>
</span>
                    </div>
                </div>
                <div class="col-sm-6 col-md-5">
                    <img src="${RESOURCES_WEB_PATH}/img/read-instructionsv3.png" alt="Get challenge instructions"
                         class="img-fluid">
                    <p>2. Read the challenge instructions</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-down hidden-sm hidden-xs"><i class="fa fa-arrow-down" aria-hidden="true"></i>
</span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-md-5">
                    <img src="${RESOURCES_WEB_PATH}/img/write-a-solution.png" alt="Example of challenge instructions"
                         class="img-fluid">
                    <p>3. Write a solution</p>
                </div>
                <div class="col-sm-6 col-md-5">
                    <img src="${RESOURCES_WEB_PATH}/img/check-resultsv2.png" alt="Deploy your solution to production."
                         class="img-fluid">
                    <p>4. Deploy and get feedback</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-left hidden-sm hidden-xs"><i class="fa fa-arrow-left" aria-hidden="true"></i>
</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row bottom-border dark-background">
        <div class="col-md-10 container-centered">
            <div>
                <h3>5. Finishing</h3>
                <p>You can stop any time you want. We will consider the progress you have made so far.</p>
                <p>When you have finished, you simply have to stop the recorder (<code>CTRL + C</code> or <code>CTRL +
                    D</code>).</p>
                <p>After finishing, your recording will be reviewed by ${SPONSOR} and you will be contacted with
                    feedback.</p>
                <p>If you have completed the challenge, ${SPONSOR} will give you access to the performance report and all the other solutions. This will be a great opportunity to see how you rank, to see
                    how other approaches to the challenge played out.</p>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-10 container-centered">
            <h2>Possible problems</h2>
        </div>
        <div class="col-md-10 container-centered">
            <div id="problems-credentials-have-expired">
                <h3>Credentials to upload screen recording have expired</h3>
                <p>
                    You see the following error message on starting the screen recording:
                </p>
                <div class="alert alert-danger">
                    ERROR [main] - User does not have enough permissions to upload. Reason: The provided token has expired.
                </div>
                <p><strong>Causes:</strong> The credentials you have for uploading your recordinghave expired.
                </p>
                <p>
                    <strong>Actions:</strong> You need to download new credentials. See the <a
                        href="#clone-repo-section">"Clone repo and download credentials"</a> section
                    above.
                </p>
            </div>
        </div>
        <div class="col-md-10 container-centered">
            <div id="technical-issues">
                <h3>Other technical issues</h3>
                <p>
                    If, for any technical reason, you are not able to go through the challenge please email:
                </p>
                <pre><code>participant@exocode.co.uk</code></pre>
                <p>
                    We can provide you with alternative ways of completing the challenge.
                </p>
            </div>
        </div>
    </div>
</div>
<div class="footer text-center">
    <p></p>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>

<script>
    $(document).ready(function(){
        var osList = $('.os-list');
        var userOS = navigator['platform'] || '';

        var downloadTabContentDiv = $('#download');
        var downloadTabContentDivCode = downloadTabContentDiv.find('code.copy-code');
        var downloadTabContentDivCodeText = downloadTabContentDivCode.text();
        var downloadTabContentDivLink = downloadTabContentDiv.find('a.download-link');
        var downloadTabContentDivLinkHref = downloadTabContentDivLink.attr('href');

        var unzipEl = $('#unzip');
        var unzipCodeEl = unzipEl.find('code.copy-code');
        var unzipElText = unzipCodeEl.text();

        var startRecTextLinuxMac = './record_screen_and_upload.sh';
        var startRecTextWindows = './record_screen_and_upload.bat';

        var os = '';

        // default values
        if(userOS.indexOf("Win") >= 0){
            os = 'windows';
        }
        if(userOS.indexOf("Linux") >= 0){
            os = 'linux';
        }
        if(userOS.indexOf("Mac") >= 0){
            os = 'macos';
        }

        if(os === ''){
            os = 'windows';
        }

        $.each(osList, function( index, value ) {
            var hrefValue = $(value).find('a').attr('href').split('#')[1];

            if(hrefValue === os){
                $(value).find('a').addClass('active');
            }
        });

        downloadTabContentDivCodeText = downloadTabContentDivCodeText.replace(/\windows/g, os);
        downloadTabContentDivCode.text(downloadTabContentDivCodeText);

        downloadTabContentDivLinkHref = downloadTabContentDivLinkHref.replace(/\windows/g, os);
        downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

        unzipElText = unzipElText.replace('windows', os);
        unzipCodeEl.text(unzipElText);

        startRec(os);




        // if you click on os button
        osList.click(function (event) {
            event.preventDefault();

            var target = event.target;
            var targetHrefValue = $(target).attr('href').split('#')[1];

            // default values are changed to particular os
            downloadTabContentDivCodeText = replaceOSText(downloadTabContentDivCodeText, targetHrefValue);
            downloadTabContentDivCode.text(downloadTabContentDivCodeText);

            downloadTabContentDivLinkHref = replaceOSText(downloadTabContentDivLinkHref, targetHrefValue);
            downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

            unzipElText = replaceOSText(unzipElText, targetHrefValue);
            unzipCodeEl.text(unzipElText);

            startRec(targetHrefValue);

         });

        function startRec(op_syst) {

            if(op_syst === 'macos'){
                op_syst = 'Mac';
            }

            $('.start-rec p').text(op_syst + ':');
            if(op_syst === 'windows'){
                $('.start-rec code').text(startRecTextWindows);
            }else{
                $('.start-rec code').text(startRecTextLinuxMac);
            }
        }

        function replaceOSText(text, newText){
            text = text.replace(/\linux/g, newText);
            text = text.replace(/\windows/g, newText);
            text = text.replace(/\macos/g, newText);

            return text;
        }

        // if you click on languages button
        $('#languagesTab').on('click', '.nav-link', function (event) {
            event.preventDefault();

            var target = event.target;
            var targetHrefValue = $(target).attr('href').split('#')[1];

            downloadTabContentDivCodeText = changeText(downloadTabContentDivCodeText, targetHrefValue);
            downloadTabContentDivCode.text(downloadTabContentDivCodeText);

            downloadTabContentDivLinkHref = changeText(downloadTabContentDivLinkHref, targetHrefValue);
            downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

            unzipElText = changeText(unzipElText, targetHrefValue);
            unzipCodeEl.text(unzipElText);
        });

        function changeText(text, newText){
            var textArr = text.split('-');
            textArr[2] = newText;
            text = textArr.join('-');

            return text;
        }


        // copy text to the clipboar
        $('.copy-text').click(function (event) {
            event.preventDefault();

            var target = event.target;
            var parentDiv = $(target).parents('div.copy-block');
            var codeText = parentDiv.find('code.copy-code').text();

            var $temp = $("<input>");
            $("body").append($temp);
            $temp.val(codeText).select();
            document.execCommand("copy");
            $temp.remove();
        });


        // if expiration date is passed, the download button is disabled and alert message is shown
        var months = ["January", "February", "March", "April", "May", "Juny", "July", "August", "Septemper", "October", "November", "December"];
        var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];


        var expDate = ${EXPIRATION_DATE};
        var date = new Date(expDate);

        var hr = date.getHours();
        var ampm = "am ";

        var expDateSpan = $('span.exp-date');
        var expDateText = '';
        var minutes = date.getMinutes();

        if(minutes < 10){
            minutes = '0' + minutes;
        }

        if( hr > 12 ) {
            hr -= 12;
            ampm = "pm ";
        }

        if(hr < 10){
            hr = '0' + hr;
        }

        expDateText = hr + ':' + minutes + ampm +
            days[date.getDay()] + ', ' +
            date.getDate() + ' ' +
            months[date.getMonth()] + ' ' +
            date.getFullYear();

        expDateSpan.text(expDateText);

        var timeNow = new Date();

        if (timeNow >= expDate){
            $('#download_credentials').attr('disabled', 'disabled');
            $('.my-alert-danger').css('display', 'inline-block');
        }

    });
</script>


</body>
</html>