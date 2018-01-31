<#-- @ftlvariable name="MAIN_CHALLENGE_TITLE" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="CODING_SESSION_DURATION" type="java.lang.String" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="CHALLENGE" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.String" -->
<#-- @ftlvariable name="JOURNEY_ID" type="java.lang.String" -->
<!DOCTYPE html>
<!--suppress HtmlUnknownTarget -->
<html lang="en">
<head>
    <title>BeFaster</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link
            href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
            crossorigin="anonymous">
    <link rel='shortcut icon' href='img/favicon.ico' type='image/x-icon'>
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
            background-image: url('../000common/img/mountain-largev2.jpg');
            background-position: top center;
            background-repeat: no-repeat;
            background-size: cover;
            margin-bottom: 0;
            padding-bottom: 75px;
            padding-top: 80px;
        }

        .jumbotron h1 {
            color: #fff;
            text-shadow: 2px 2px 0 #333;
        }

        .jumbotron h2 {
            color: #fff;
            text-shadow: 1px 1px 0 #333;
        }

        #challenge-flow .glyphicon {
            color: #337ab7;
        }

        .horizontal-arrow {
            position: absolute;
            left: 100%;
            top: 40%;
        "
        }

        #challenge-flow .glyphicon {
            font-size: 150%;
        }

        .bottom-border {
            border-bottom-width: 2px;
            border-bottom-color: #b1afaf;
            border-bottom-style: solid;
        }
    </style>
</head>
<body>

<div class="jumbotron text-center">
    <h1>Welcome to the ${MAIN_CHALLENGE_TITLE} challenge</h1>
    <h2>Sponsored by ${SPONSOR}</h2>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-offset-1 col-md-10">
            <h2>How does this work?</h2>
            <div class="row text-center">
                <div class="col-md-4">
                    <img src="../000common/img/ic_settings_black_24dp_2x.png">
                    <h3>Set up</h3>
                    <h4>15 mins</h4>
                    <p>
                        You will clone a github repo, install some libraries and will then be ready
                        to write code in your own IDE using your choice of frameworks and tools.
                    </p>
                </div>
                <div class="col-md-4">
                    <img src="../000common/img/ic_whatshot_black_24dp_2x.png">
                    <h3>Challenge</h3>
                    <h4>~${CODING_SESSION_DURATION}</h4>
                    <p>
                        A realistic coding challenge designed to measure code quality. Code through multiple incremental requirements.
                    </p>
                </div>
                <div class="col-md-4">
                    <img src="../000common/img/ic_timer_black_48px.svg">
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
        <div class="col-md-offset-1 col-md-5">
            <h2>It is not just a coding exercise</h2>
            <p>You go through a realistic coding challenge designed to put your core software development skills to the
                test.</p>
            <p>We promise that you will learn a lot by just going through this challenge.</p>

            <p>Focus on your development technique and see how well it plays out. You will be able to compare metrics
                afterwards.</p>
            <p>Each screen gets recorded so after the challenge you get the opportunity to see (anonymously) how other
                people have approached this problem. You might be able to learn something from them.</p>
        </div>
        <div class="col-md-5">
            <img src="../000common/img/leaderboard-transparent-bg.png" class="img-responsive"/>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-1 col-md-10">
            <h2>Before you start</h2>
            You will need:
            <ul>
                <li>15 minutes for Preparation + Warmup</li>
                <li>${CODING_SESSION_DURATION} challenge time, can be done in multiple coding sessions</li>
                <li>your laptop + your IDE and a connection to the internet</li>
            </ul>
            <div class="alert alert-success">
                We encourage you put aside some time in a quiet environment where you won't be interrupted so you can
                focus on
                the challenge.
            </div>
            <p>People who have completed the challenge will have access to the other results and recordings.</p>
            <p>If you are not ready you can come back to this page whenever you have
                enough time. The link will expire on: <b>${EXPIRATION_DATE}</b></p>
            <p>The challenge can pe paused and resumed, you are in control of your time. You can do it one go or multiple sessions.</p>
            <p>Ready? Let's get started.</p>
        </div>
    </div>


    <div class="row dark-background" id="clone-repo-section">
        <div class="col-md-offset-1 col-md-6">
            <div>
                <h3>1. Clone repo and download credentials</h3>
                <p>Clone the git repository for the language of your choice.</p>
                <div>
                    <ul class="nav nav-tabs">
                        <li><a data-toggle="tab" href="#csharp-warmup">C#</a></li>
                        <li><a data-toggle="tab" href="#fsharp-warmup">F#</a></li>
                        <li class="active"><a data-toggle="tab" href="#java-warmup">Java</a></li>
                        <li><a data-toggle="tab" href="#nodejs-warmup">Node.js</a></li>
                        <li><a data-toggle="tab" href="#python-warmup">Python</a></li>
                        <li><a data-toggle="tab" href="#ruby-warmup">Ruby</a></li>
                        <li><a data-toggle="tab" href="#scala-warmup">Scala</a></li>
                        <li><a data-toggle="tab" href="#vb-warmup">VB.Net</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="csharp-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-csharp</code></pre>
                        </div>
                        <div id="fsharp-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-fsharp</code></pre>
                        </div>
                        <div id="java-warmup" class="tab-pane fade in active">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-java</code></pre>
                        </div>
                        <div id="nodejs-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-nodejs</code></pre>
                        </div>
                        <div id="python-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-python</code></pre>
                        </div>
                        <div id="ruby-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-ruby</code></pre>
                        </div>
                        <div id="scala-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-scala</code></pre>
                        </div>
                        <div id="vb-warmup" class="tab-pane fade">
                            <pre><code>git clone https://github.com/julianghionoiu/tdl-runner-vbnet</code></pre>
                        </div>
                    </div>
                </div>
                <div>
                    <p>To securely connect to the server you need to download and save the following credentials to the
                        <code>config</code> directory:</p>
                    <form action="${API_VERIFY_ENDPOINT}" method="post">
                        <input type="hidden" name="username" value="${USERNAME}">
                        <input type="hidden" name="challenge" value="${CHALLENGE}">
                        <input type="hidden" name="token" value="${TOKEN}">
                        <input type="submit" class="btn btn-primary" value="Download credentials">
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-offset-1 col-md-5">
            <div id="setup-screen-recording">
                <h2>2. Start recording</h2>
                <p>While working on the main challenge you are going to record your screen and stream this to a storage
                    server.</p>
                <p>After you perform the challenge you get to see (anonymously) how the
                    other participants tackled the same challenge.</p>
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-warning-sign"></span>
                    Close all the tabs that contain <strong>personal identifiable information</strong> (email, social
                    media)
                    before
                    you start recording.
                </div>
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
                            <p>Start the screen recorder in a new terminal tab/window.</p>
                            <p>Windows:</p>
                            <pre><code>./record_screen_and_upload.bat</code></pre>
                            <p>OSX/Linux:</p>
                            <pre><code>./record_screen_and_upload.sh</code></pre>
                        </li>
                    </ul>
                </p>
            </div>
        </div>
        <div class="col-md-5">
            <img src="../000common/img/hello-challenge.gif" class="img-responsive">
        </div>
    </div>

    <div class="row">
        <div id="challenge-flow" class="col-md-offset-1 col-md-10">
            <h2>3. Start the warmup</h2>
            <p>In your IDE of choice, open up the <code>README.md</code> file of the repo that you cloned.</p>
            <p>To get started, follow the instructions provided in the README.md and source code comments.</p>
            <br/>
            <p>While working on the challenge you will be interacting with the server by running the code and you will provide your solutions within the same project.</p>
            <p>The challenge has the following flow. Do not worry about the details, the Warmup will guide you through
                the process.</p>
            <div class="row">
                <div class="col-sm-12 col-md-5">
                    <img src="../000common/img/start-challenge.png" alt="Start challenge." class="img-responsive">
                    <p>1. Start the challenge</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-right hidden-sm hidden-xs"></span>
                    </div>
                </div>
                <div class="col-sm-12 col-md-5">
                    <img src="../000common/img/read-instructionsv3.png" alt="Get challenge instructions"
                         class="img-responsive">
                    <p>2. Read the challenge instructions</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-down hidden-sm hidden-xs"></span>
                    </div>
                </div>
                <div class="col-sm-12 col-md-5">
                    <img src="../000common/img/check-resultsv2.png" alt="Deploy your solution to production."
                         class="img-responsive">
                    <p>4. Deploy and get feedback</p>
                    <div class="horizontal-arrow">
                        <span class="glyphicon glyphicon-arrow-left hidden-sm hidden-xs"></span>
                    </div>
                </div>
                <div class="col-sm-12 col-md-5">
                    <img src="../000common/img/write-a-solution.png" alt="Example of challenge instructions"
                         class="img-responsive">
                    <p>3. Write a solution</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row bottom-border dark-background">
        <div class="col-md-offset-1 col-md-10">
            <div>
                <h3>4. Finishing</h3>
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
        <div class="col-md-offset-1 col-md-10">
            <h2>Possible problems</h2>
        </div>
        <div class="col-md-offset-1 col-md-10">
            <div id="problems-credentials-have-expired">
                <h3>Credentials to upload screen recording have expired</h3>
                <p>
                    You see the following error message on starting the screen recording:
                </p>
                <div class="alert alert-danger">
                    ERROR [main] - User does not have enough permissions to upload. Reason: The credentials have
                    expired. Solution: Download new credentials from the challenge link.
                </div>
                <p><strong>Causes:</strong> The credentials you have for uploading your screen recording have expired.
                </p>
                <p>
                    <strong>Actions:</strong> You need to download new credentials. See the <a
                        href="#clone-repo-section">"Clone repo and download credentials"</a> section
                    above.
                </p>
            </div>
        </div>
        <div class="col-md-offset-1 col-md-10">
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
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
