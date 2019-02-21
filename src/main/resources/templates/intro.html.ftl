<!DOCTYPE html>
<#-- @ftlroot "." -->
<#-- @ftlvariable name="RESOURCES_WEB_PATH" type="java.lang.String" -->
<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#setting number_format="computer">
<html lang="en">
<head>
    <title>${SPONSOR} - Coding Challenge</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
    <#--noinspection HtmlUnknownTarget-->
    <link rel="apple-touch-icon" sizes="57x57" href="${RESOURCES_WEB_PATH}/img/logo_favicon_apple.png"/>
    <#--noinspection HtmlUnknownTarget-->
    <link rel="icon" type="image/png" sizes="32x32" href="${RESOURCES_WEB_PATH}/img/logo_favicon.png"/>


    <#include "/partials/global.style.html.ftl">
    <#include "/partials/global_copybutton.style.html.ftl">
    <#include "/partials/0a_header/style.html.ftl">
    <#include "/partials/0b_expectation_shields/style.html.ftl">
    <#include "/partials/4_start_with_warmup/style.html.ftl">
    <#include "/partials/1_download_runner/style.html.ftl">
    <#include "/partials/2_download_credentials/style.html.ftl">
    <#include "/partials/3_start_recorder/style.html.ftl">
</head>
<body>

<#include "/partials/0a_header/content.html.ftl">

<div class="container-fluid">
    <#include "/partials/0b_expectation_shields/content.html.ftl">
    <#include "/partials/0c_the_challenge/content.html.ftl">
    <#include "/partials/0d_before_you_start/content.html.ftl">

    <#include "/partials/1_download_runner/content.html.ftl">
    <#include "/partials/2_download_credentials/content.html.ftl">
    <#include "/partials/3_start_recorder/content.html.ftl">
    <#include "/partials/4_start_with_warmup/content.html.ftl">
    <#include "/partials/5_finishing/content.html.ftl">

    <#include "/partials/99_problems/content.html.ftl">
</div>

<div class="footer text-center"><p></p></div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
        integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>

<#include "/partials/global_copybutton.script.html.ftl">
<#include "/partials/global_detectos.script.html.ftl">
<#include "/partials/global_datespan.script.html.ftl">
<#include "/partials/0b_expectation_shields/script.html.ftl">
<#include "/partials/1_download_runner/script.html.ftl">
<#include "/partials/2_download_credentials/script.html.ftl">
<#include "/partials/3_start_recorder/script.html.ftl">

</body>
</html>