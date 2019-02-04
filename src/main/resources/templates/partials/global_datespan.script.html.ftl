<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        // if expiration date is passed, the download button is disabled and alert message is shown
        var months = ["January", "February", "March", "April", "May", "Juny", "July", "August", "Septemper", "October", "November", "December"];
        var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];


        var expDate = ${EXPIRATION_DATE};
        var date = new Date(expDate);

        var hr = date.getHours();
        var ampm = "am ";

        var expDateSpan = $('span.exp-date');
        var expDateText = '';
        var minutes = date.getMinutes();

        if(minutes < 10){
            minutes = '0' + minutes;
        }

        if( hr > 12 ) {
            hr -= 12;
            ampm = "pm ";
        }

        if(hr < 10){
            hr = '0' + hr;
        }

        expDateText =
                days[date.getDay()] + ', ' +
                date.getDate() + ' ' +
                months[date.getMonth()] + ' ' +
                date.getFullYear()  + ', ' +
                hr + ':' + minutes + ampm;

        expDateSpan.text(expDateText);
    });
</script>