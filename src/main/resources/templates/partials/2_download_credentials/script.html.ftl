<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        var expDate = ${EXPIRATION_DATE};
        var timeNow = ${FAKE_CURRENT_DATE!"new Date().getTime()"};

        if (timeNow >= expDate){
            $('#download_credentials').attr('disabled', 'disabled');
            $('.my-alert-danger').css('display', 'inline-block');
        }
    });
</script>