<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<div class="row dark-background margins-mobile" id="clone-repo-section">
    <div class="col-md-10 container-centered">
        <div class="row text-left">
            <div class="col-lg-12">
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

                            <div id="" class="collapse show" aria-labelledby="headingOne"
                                 data-parent="#accordionExample">
                                <div class="card-body">
                                    <ul class="nav nav-tabs os-list-tab" id="osTab" role="tablist">
                                        <li class="nav-item os-list">
                                            <a class="nav-link" id="windows-tab" data-toggle="tab" href="#windows"
                                               role="tab" aria-selected="false">Windows</a>
                                        </li>
                                        <li class="nav-item os-list">
                                            <a class="nav-link" id="macos-tab" data-toggle="tab" href="#macos"
                                               role="tab" aria-selected="false">Mac</a>
                                        </li>
                                        <li class="nav-item os-list">
                                            <a class="nav-link" id="linux-tab" data-toggle="tab" href="#linux"
                                               role="tab" aria-selected="true">Linux</a>
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
                            <div id="" class="collapse show" aria-labelledby="headingTwo"
                                 data-parent="#accordionExample">
                                <div class="card-body">
                                    <ul class="nav nav-tabs lang-list-tab" id="languagesTab" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link" id="csharp-tab" data-toggle="tab" href="#csharp"
                                               role="tab" aria-selected="false">C#</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="fsharp-tab" data-toggle="tab" href="#fsharp"
                                               role="tab" aria-selected="false">F#</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link active" id="java-tab" data-toggle="tab" href="#java"
                                               role="tab" aria-selected="true">Java</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="nodejs-tab" data-toggle="tab" href="#nodejs"
                                               role="tab" aria-selected="false">Node.js</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="python-tab" data-toggle="tab" href="#python"
                                               role="tab" aria-selected="false">Python</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="ruby-tab" data-toggle="tab" href="#ruby" role="tab"
                                               aria-selected="false">Ruby</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="scala-tab" data-toggle="tab" href="#scala"
                                               role="tab" aria-selected="false">Scala</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="vbnet-tab" data-toggle="tab" href="#vbnet"
                                               role="tab" aria-selected="false">VB.Net</a>
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
                            <div id="" class="collapse show" aria-labelledby="headingThree"
                                 data-parent="#accordionExample">
                                <div class="card-body">
                                    <div class="tab-content" id="languagesTabContent">
                                        <div class="copy-block" id="download" role="tabpanel"
                                             aria-labelledby="down-tab">
                                            <div class="copy-block-line">
                                                <pre><code class="copy-code">wget https://get.accelerate.io/v0/runner-for-java-windows.zip</code></pre>
                                                <p>
                                                    <a href="#" class="copy-btn copy-text">
                                                        <img src="${RESOURCES_WEB_PATH}/img/runner_setup/clippy.svg" width="14" height="16">
                                                    </a>
                                                </p>
                                            </div>
                                            <div>
                                                OR <a href="https://get.accelerate.io/v0/runner-for-java-windows.zip"
                                                      class="btn btn-primary btn-primary-darkBlue download-link"
                                                      target="_blank">Download runner project</a>
                                            </div>
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
                            <div id="" class="collapse show" aria-labelledby="headingThree"
                                 data-parent="#accordionExample">
                                <div class="card-body">
                                    <div class="tab-content" id="unzipTabContent">
                                        <div id="unzip" class="copy-block" role="tabpanel" aria-labelledby="unzip-tab">
                                            <div class="copy-block-line">
                                                <pre><code class="copy-code">unzip runner-for-java-windows.zip</code></pre>
                                                <p>
                                                    <a href="#" class="copy-btn copy-text">
                                                        <img src="${RESOURCES_WEB_PATH}/img/runner_setup/clippy.svg" width="14" height="16">
                                                    </a>
                                                </p>
                                            </div>
                                            <p>The <code>accelerate_runner</code> folder will be created, which
                                                represents the root of your project.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
