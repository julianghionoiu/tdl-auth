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
    .row {
        padding: 15px 0;
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
        padding: 7px 8px 10px 8px;
        border-radius: 3px;
        display: inline-block;
        margin-left: 5px;
    }
    .download-link{
        margin-left: 10px;
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
        background: transparent;
        border: 0px solid #b1afaf;
        border-radius: 3px;
        padding: 5px;
        width: 250px;
    }
    .form-input-readonly input:focus{
        box-shadow: none;
        border: 0px solid #b1afaf;
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
        height: auto;
    }

    .img-margins{
        border: 1px solid rgba(0,0,0,.125);
        border-radius: 4px;
        padding: 0;
        margin: 0;
        max-width: 600px;
        width: 100%;
    }
    .img-margins2{
        margin-top: 68px;
    }
    .accelerate_runner{
        margin-top: 20px;
    }
    #challenge-flow img{
        width: 96%;
    }
    @media only screen and (max-width: 990px) {
        .margins-mobile {
            padding: 0;
        }
        .img-margins{
            max-width: 100%;
        }
        .img-margins2{
            margin-top: 20px;
        }
    }
    @media only screen and (max-width: 767px) {
        img {
            margin: 0;
        }
        .horizontal-arrow{
            display: none;
        }
    }
</style>
