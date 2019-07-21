<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<#-- @ftlvariable name="VIDEO_RECORDING_OPTION" type="tdl.auth.linkgenerator.VideoRecordingOption" -->

<script>
    $(document).ready(function(){
        var osList = $('.os-list');

        var recordingOption = "${VIDEO_RECORDING_OPTION.name()}";
        var startRecTextLinuxMac = './record_and_upload.sh';
        var startRecTextWindows = './record_and_upload.bat';

        var os = detectOS();

        startRec(os);

        osList.click(function (event) {
            event.preventDefault();

            var target = event.target;
            var targetHrefValue = $(target).attr('href').split('#')[1];

            startRec(targetHrefValue);

        });

        function startRec(op_syst) {

            if(op_syst === 'macos'){
                op_syst = 'Mac';
            }

            if (recordingOption === "DISABLED") {
                extra_args = " "+ "--no-video"
            } else {
                extra_args = ""
            }

            if(op_syst === 'windows'){
                $('.start-rec code').text(startRecTextWindows + extra_args);
            }else{
                $('.start-rec code').text(startRecTextLinuxMac + extra_args);
            }
        }
    });
</script>