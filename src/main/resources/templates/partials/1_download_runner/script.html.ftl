<#-- @ftlvariable name="DEFAULT_LANGUAGE" type="java.lang.String" -->
<script>
    $(document).ready(function(){

        function defaultLanguage() {
            var defaultLanguageStr = "${DEFAULT_LANGUAGE}" || "java";
            return defaultLanguageStr.toLowerCase()
        }

        function getPlatformSelection() {
            var platformTabValue = $(".os-list li a.active").attr("href") || "#macos";
            return platformTabValue.split("#")[1]
        }

        function getLanguageSelection() {
            var languageTabValue = $(".lang-list li a.active").attr("href") || "#java";
            return languageTabValue.split("#")[1]
        }

        function getRunnerFileName(platform, language) {
            return "runner-for-"+language+"-"+platform+".zip"
        }

        function updateFields() {
            var runnerFileName = getRunnerFileName(getPlatformSelection(), getLanguageSelection());

            $('#download code.copy-code').text("wget https://get.accelerate.io/v0/"+runnerFileName);
            $('#download a.download-link').attr('href', "https://get.accelerate.io/v0/"+runnerFileName);
            $('#unzip code.copy-code').text("unzip "+runnerFileName);
        }

        function clickEvent(event) {
            event.preventDefault();
            $(this).tab('show');
            updateFields();
        }

        $("#"+detectOS()+"-tab").addClass('active');
        $("#"+defaultLanguage()+"-tab").addClass('active');

        updateFields();

        $('#osTab').on('click', '.nav-link', clickEvent);
        $('#languagesTab').on('click', '.nav-link', clickEvent);
    });
</script>