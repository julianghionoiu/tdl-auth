<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<#-- @ftlvariable name="ENABLE_APPLY_PRESSURE" type="java.lang.Boolean" -->
<#-- @ftlvariable name="FAKE_CURRENT_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        var expDate = ${EXPIRATION_DATE};
        var timeNow = ${FAKE_CURRENT_DATE!"new Date().getTime()"};

        <#if ENABLE_APPLY_PRESSURE>
        var enableApplyPressure = true;
        <#else >
        var enableApplyPressure = false;
        </#if>

        var leftDays = 0,
            leftHours = 0,
            leftMinutes = 0;

        var daysText = '',
            hoursText = '',
            minutesText = '',
            outputStr = '';

        // day is 86400 sec
        // hour is 3600 sec
        // minute is 60 sec
        var timeDiffInSec = Math.round((expDate - timeNow) / 1000);

        if(timeDiffInSec > 0){
            leftDays = Math.floor(timeDiffInSec / 86400);
            leftHours = Math.floor((timeDiffInSec - (leftDays * 86400)) / 3600);
            leftMinutes = Math.floor((timeDiffInSec - (leftDays * 86400) - (leftHours * 3600)) / 60);

            daysText = leftDays > 1 || leftDays === 0 ? 'days' : 'day';
            hoursText = leftHours > 1 || leftHours === 0 ? 'hours' : 'hour';
            minutesText = leftMinutes > 1 || leftMinutes === 0 ? 'minutes' : 'minute';

            if(leftDays > 0){
                outputStr += leftDays + ' ' + daysText;
            }

            if(leftDays === 0 && leftHours > 0){
                outputStr += leftHours + ' ' + hoursText;
            }

            if(leftDays === 0 && leftHours === 0 && leftMinutes > 0){
                outputStr += leftMinutes + ' ' + minutesText;
            }
        }

        if (timeNow >= expDate) {
            $('#you-have-time').text('Link expired');
            $('#you-have-time-message').text('For an extension, please email your contact.');
        } else if (enableApplyPressure) {
            $('#you-have-time').text(outputStr + ' remaining');
        } else {
            $('#you-have-time').text('plenty of time');
            $('#you-have-time-message').text('Start whenever you are ready.');
        }
    });
</script>