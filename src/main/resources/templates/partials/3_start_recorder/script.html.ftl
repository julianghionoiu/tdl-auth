<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        var osList = $('.os-list');

        var startRecTextLinuxMac = './record_screen_and_upload.sh';
        var startRecTextWindows = './record_screen_and_upload.bat';

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

            if(op_syst === 'windows'){
                $('.start-rec code').text(startRecTextWindows);
            }else{
                $('.start-rec code').text(startRecTextLinuxMac);
            }
        }
    });
</script>