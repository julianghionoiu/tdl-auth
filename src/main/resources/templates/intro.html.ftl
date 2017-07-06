<#-- @ftlvariable name="CHALLENGE_NAME" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="API_VERIFY_ENDPOINT" type="java.lang.String" -->
<#-- @ftlvariable name="USERNAME" type="java.lang.String" -->
<#-- @ftlvariable name="TOKEN" type="java.lang.String" -->
<#-- @ftlvariable name="SESSION_ID" type="java.lang.String" -->
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
      top: 40%;"
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
  <h1>Welcome to the ${CHALLENGE_NAME} challenge</h1>
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
            <h3>Warm up</h3>
            <h4>30 mins</h4>
            <p>
              You will complete some simple warmup challenges to allow you to get used to the system
            </p>
          </div>
          <div class="col-md-4">
            <img src="../000common/img/ic_timer_black_48px.svg">
            <h3>Challenge</h3>
            <h4>~4 hours</h4>
            <p>
              A realistic coding challenge designed to put your design skills to the test. This is the one that counts!
            </p>
        </div>
      </div>
    </div>
  </div>

  <div class="row dark-background">
    <div class="col-md-offset-1 col-md-5">
      <h2>It is not just a coding assignment</h2>
      <p>You go through a realistic coding challenge designed to put your core software development skills to the test.</p>
      <p>We promise that you will learn a lot by just going through this challenge.</p>

      <p>Focus on your development technique and see how well it plays out. You will be able to compare metrics afterwards.</p>
      <p>Each screen gets recorded so after the challenge you get the opportunity to see how other people have approached this problem.
      Who knows, maybe you can learn something from them.</p>
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
        <li>45 minute for Preparation + Warmup</li>
        <li>4 hours of uninterrupted coding challenge</li>
        <li>your laptop + your IDE and a connection to the internet</li>
      </ul>
      <div class="alert alert-success">
        We encourage you put aside some time in a quiet environment where you won't be interrupted so you can focus on
        the challenge.
      </div>
      <p>Only people who have completed the challenge or spent at least 4 hours working on the challenge will have
      access to the other results and recordings</p>
      <p>If you are not ready you can come back to this page whenever you have
        enough time.The link will expire on: DATE</p>
      <p>You can stop any time you want. We will consider the progress you have made so far.</p>
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
            <li class="active"><a data-toggle="tab" href="#java-warmup">Java</a></li>
            <li><a data-toggle="tab" href="#nodejs-warmup">Node.js</a></li>
            <li><a data-toggle="tab" href="#python-warmup">Python</a></li>
            <li><a data-toggle="tab" href="#ruby-warmup">Ruby</a></li>
            <li><a data-toggle="tab" href="#scala-warmup">Scala</a></li>
          </ul>
          <div class="tab-content">
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
          </div>
        </div>
        <div>
          <p>
            Download the credentials by clicking the button below and save to the `config` directory of the repo you
            cloned above.
          </p>
          <form action="${API_VERIFY_ENDPOINT}" method="post">
            <input type="hidden" name="username" value="${USERNAME}">
            <input type="hidden" name="token" value="${TOKEN}">
            <input type="submit" class="btn btn-primary" value="Download credentials">
          </form>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-offset-1 col-md-5">
      <div class="bottom-border">
        <h2>Screen recording</h2>
        <p>While working on the main challenge you are going to record your screen and stream this to a storage
          server.</p>
        <p>After you perform the challenge you get to see (anonymously) how the
          other participants tackled the same challenge.</p>
        <p>It will be a learning exercise for both you and us.</p>
      </div>
      <div id="setup-screen-recording">
        <h2>2. Start recording</h2>
        <div class="alert alert-danger" role="alert">
          <span class="glyphicon glyphicon-warning-sign"></span>
          Close all the tabs that contain <strong>personal identifiable information</strong> (email, social media)
          before
          you start recording.
        </div>
        <p>
          If your name appears in your command prompt you can change this by following the instructions in the
          <a href="#problems-remove-name-from-prompt">problems section</a> below.
        </p>
        <ul>
          <li>
            <p>Requires Java 8. If you do not have Java 8 <a
              href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">download</a>
              and install it.
            </p>
          </li>
          <li>
            <p>Start the screen recorder before starting the main challenge</p>
            <p>Windows:</p>
            <pre><code>./record_screen_and_upload.bat</code></pre>
            <p>OSX/Linux:</p>
            <pre><code>./record_screen_and_upload.sh</code></pre>
          </li>
        </ul>
      </div>
    </div>
    <div class="col-md-5">
      <img src="../000common/img/hello-challenge.gif" class="img-responsive">
    </div>
  </div>

  <div class="row dark-background">
    <div class="col-md-offset-1 col-md-6">
      <h3>3. Start the warmup</h3>
      <p>Follow the instructions in the <code>README.md</code> file of the repo that you cloned.</p>
      <p>
        Remember to start the screen recorder before you commence the main challenge.
      </p>
      <h4>Still ready? Let's get go for it!</h4>
      <p>Copy the following <code>sessionId</code> and then press "Start challenge":</p>
      <pre><code>${SESSION_ID}</code></pre>
      <a href="http://run.befaster.io:8111" target="_blank" class="btn btn-primary">Start challenge</a>
    </div>
  </div>

  <div class="row">
    <div id="challenge-flow" class="col-md-offset-1 col-md-10">
      <h2>4. The challenge flow</h2>
      <p>Working on a challenge has the following flow.  Do not worry about the details, the Warmup will guide you through the process</p>
      <div class="row">
        <div class="col-md-4">
          <img src="../000common/img/start-challenge.png" alt="Start challenge." class="img-responsive">
          <p>1. In a web browser, start the challenge</p>
          <div class="horizontal-arrow">
            <span class="glyphicon glyphicon-arrow-right"></span>
          </div>
        </div>
        <div class="col-md-4">
          <img src="../000common/img/get_round_descriptionv3.png" alt="Get challenge instructions" class="img-responsive">
          <p>2. On your command line, get the challenge instructions</p>
          <div class="horizontal-arrow">
            <span class="glyphicon glyphicon-arrow-right"></span>
          </div>
        </div>
        <div class="col-md-4">
          <img src="../000common/img/read-instructionsv3.png" alt="Example of challenge instructions" class="img-responsive">
          <p>3. In your IDE, read the challenge instructions</p>
          <div class="text-center">
            <span class="glyphicon glyphicon-arrow-down"></span>
          </div>
        </div>
        <div class="col-md-4">
          <img src="../000common/img/check-resultsv2.png" alt="See if your solution worked." class="img-responsive">
          <p>6. In a web browser, check your results.</p>
          <div class="horizontal-arrow">
            <span class="glyphicon glyphicon-arrow-left"></span>
          </div>
        </div>
        <div class="col-md-4">
          <img src="../000common/img/deployv3.png" alt="Deploy your solution to production." class="img-responsive">
          <p>5. On your command line, deploy your solution</p>
          <div class="horizontal-arrow">
            <span class="glyphicon glyphicon-arrow-left"></span>
          </div>
        </div>
        <div class="col-md-4">
          <img src="../000common/img/write-a-solution.png" alt="Code a solution." class="img-responsive">
          <p>4. In your IDE, write a solution</p>
        </div>
      </div>
    </div>
  </div>

  <div class="row bottom-border dark-background">
    <div class="col-md-offset-1 col-md-10">
      <div>
        <h3>5. Finishing</h3>
        <p>You can stop any time you want. We will consider the progress you have made so far.</p>
        <p>Only people who have completed the challenge or spent at least 4 hours working on the challenge will have
          access to the other results and recordings.</p>
        <p>Stop the recorder (<code>CTRL + C</code>)</p>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-md-offset-1 col-md-10">
      <h2>Common problems</h2>
    </div>
    <div class="col-md-offset-1 col-md-10">
      <div id="problems-credentials-have-expired" >
        <h3>Credentials to upload screen recording have expired</h3>
        <p>
          You see the following error message on starting the screen recording:
        </p>
        <div class="alert alert-danger">
          ERROR [main] - User does not have enough permissions to upload. Reason: The provided token has expired.
        </div>
        <p><strong>Causes:</strong> The credentials you have for uploading your screen recording have expired.</p>
        <p>
          <strong>Actions:</strong> You need to download new credentials.  See the  <a href="#clone-repo-section">"Clone repo and download credentials"</a> section
          above.
        </p>
      </div>
    </div>
    <div class="col-md-offset-1 col-md-10">
      <div id="problems-copy-and-paste-into-gotty" >
        <h3>Can't copy and paste session id into http://run.befaster.io website</h3>
        <p>Some browser versions have problems with copying and pasting text into a gotty terminal.</p>
        <p>
          <strong>Actions:</strong> Type the session id into the gotty terminal on the  http://run.befaster.io website
        </p>
      </div>
    </div>
    <div class="col-md-offset-1 col-md-10">
      <div id="problems-remove-name-from-prompt">
        <h3>I want to remove my name from the command prompt</h3>
        <p>For bash shells you can add the following to the bottom of your <code>.bashrc</code> file so that just a
        <code>&#36; </code> is shown in your command prompt.
        </p>
      </div>
      <pre><code>PS1='$ '</code></pre>
    </div>
  </div>
</div>
<div class="footer text-center">
  <p> </p>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
