<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        var expDate = ${EXPIRATION_DATE};
        var timeNow = new Date();
        timeNow = timeNow.getTime();

        var leftDays = 0,
            leftHours = 0,
            leftMinutes = 0;

        var daysText = '',
            hoursText = '',
            minutesText = '';

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
        }

        if (timeNow >= expDate){
            $('#you-have').text('Link expired');
            $('#you-have-message').text('For an extension, please email your contact.');
        }else{
            $('#you-have').text(leftDays + ' ' + daysText + ', ' + leftHours + ' ' + hoursText + ', ' + leftMinutes + ' ' + minutesText);
        }
    });
</script>