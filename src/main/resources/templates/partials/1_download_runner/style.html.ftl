<style>
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

    .btn-primary-darkBlue{
        background-color: #4C5675;
        border-color: #4C5675;
    }
    .btn-primary-darkBlue:hover{
        background-color: #6b769f;
        border-color: #4C5675;
    }

    .copy-block-line{
        display: flex;
        display: -webkit-flex;
        width: 100%;
        height: auto;
    }
    .copy-block-line p{
        display: flex;
        display: -webkit-flex;
        justify-content: center;
    }
    .copy-block-line p a{
        display: flex;
        display: -webkit-flex;
        justify-content: center;
        -webkit-justify-content: center;
        align-items: center;
        -webkit-align-items: center;
    }
    .copy-btn{
        text-decoration: none;
        border-top: 1px solid #ccc;
        border-bottom: 1px solid #ccc;
        border-right: 1px solid #ccc;
        display: inline-block;
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
        background-color: #f6f8fa;
    }
    .copy-btn:hover{
        background-color: #ecf0f4;
    }
    .copy-block-line pre{
        border: 1px solid #ccc;
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
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

    @media only screen and (max-width: 767px) {
        .copy-block-line p a img{
            margin: 10px;
        }
    }


</style>
